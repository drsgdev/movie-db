package com.github.drsgdev.service;

import java.util.Optional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.DBObjectRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiService {
    private final String API_KEY = "903ffebdb80a3af1d4c8a15ad338e3ea";
    private final String API_PATH = "https://api.themoviedb.org/3";
    private final String IMG_PATH = "https://image.tmdb.org/t/p/original";
    private final String IMG_PLACEHOLDER_PATH =
            "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg";

    private final DBObjectService db;

    private final DBObjectRepository objects;

    private HttpStatus status;

    private JSONObject json = new JSONObject();
    private String objId;

    public HttpStatus addMovieToDB(String id) {
        return addObjectToDB(id, "movie");
    }

    public HttpStatus addShowToDB(String id) {
        return addObjectToDB(id, "tv");
    }

    public HttpStatus addPersonToDB(String id) {
        return addObjectToDB(id, "person");
    }

    private HttpStatus addObjectToDB(String id, String type) {
        try {
            HttpResponse<JsonNode> res = Unirest.get(API_PATH + "/" + type + "/{id}")
                    .routeParam("id", id).queryString("api_key", API_KEY).asJson();

            status = HttpStatus.valueOf(res.getStatus());
            json = res.getBody().getObject();
        } catch (UnirestException ex) {
            log.warn("Fetching failed!", ex);
        }

        if (status != HttpStatus.OK) {
            return status;
        } else {
            log.info("Fetched " + type + ": {}, status: {}", description(type), status.toString());
        }

        parseObject(type);

        String poster_path = "";
        if (json.has("poster_path")) {
            poster_path = json.get("poster_path").toString();
        }

        if (type.equals("movie")) {
            return addCreditsToDB(id, objId, "movie", json.get("title").toString(), poster_path);
        } else if (type.equals("tv")) {
            return addCreditsToDB(id, objId, "tv", json.get("name").toString(), poster_path);
        }

        return status;
    }

    public HttpStatus addCreditsToDB(String id, String objId, String linkType, String title,
            String poster_path) {
        JSONArray cast = new JSONArray();
        JSONArray crew = new JSONArray();

        try {
            HttpResponse<JsonNode> res =
                    Unirest.get(API_PATH + "/{type}/{id}/credits").routeParam("id", id)
                            .routeParam("type", linkType).queryString("api_key", API_KEY).asJson();

            status = HttpStatus.valueOf(res.getStatus());
            cast = res.getBody().getObject().getJSONArray("cast");
            crew = res.getBody().getObject().getJSONArray("crew");
        } catch (UnirestException ex) {
            log.warn("Fetching failed!", ex.getMessage());
        }

        if (status != HttpStatus.OK) {
            return status;
        } else {
            log.info("Fetched credits for id {}, status: {}", id, status.toString());
        }

        for (int i = 0; i < cast.length(); i++) {
            json = cast.getJSONObject(i);
            addCreditInfo(id, objId, title, poster_path);

            parseObject("cast");
        }

        for (int i = 0; i < crew.length(); i++) {
            json = crew.getJSONObject(i);
            addCreditInfo(id, objId, title, poster_path);

            parseObject("crew");
        }

        return status;
    }

    private void addCreditInfo(String id, String objId, String title, String poster_path) {
        json.put("person_id", json.get("id"));
        json.put("id", objId);
        json.put("api_id", id);
        json.put("title", title);
        json.put("poster_path", poster_path);
    }

    private void parseObject(String type) {
        DBObject newObject;

        Optional<DBObject> objFromDB = objects.findByDescrAndTypeName(description(type), type);

        if (objFromDB.isPresent()) {
            newObject = objFromDB.get();

            newObject.setDescr(description(type));
            objects.save(newObject);
            objId = Long.toString(newObject.getId());

            log.info("Saved new {} id {}", type, objId);

            parseAttributes(newObject);
        } else {
            newObject = new DBObject();
        }

        DBObjectType objTypeFromDB = db.findObjTypeByNameOrCreate(type);

        newObject.setType(objTypeFromDB);
        newObject.setDescr(description(type));

        objects.save(newObject);
        objId = Long.toString(newObject.getId());

        log.info("Saved new {} id {}", type, objId);

        parseAttributes(newObject);
    }

    private boolean checkIfArray(String key) {
        return json.optJSONArray(key) != null;
    }

    private boolean checkIfObject(String key) {
        return json.optJSONObject(key) != null;
    }

    private void parseAttributes(DBObject object) {

        json.keySet().forEach((key) -> {
            if (checkIfArray(key) || checkIfObject(key)) {
                return;
            }

            String value = json.get(key).toString();

            if (key.contains("_path")) {
                if (!value.equals("null")) {
                    value = IMG_PATH + value;
                } else {
                    value = IMG_PLACEHOLDER_PATH;
                }
            }

            if (key.equals("id") && (object.getType().getName().equals("movie")
                    || object.getType().getName().equals("tv"))) {
                value = objId;
            }

            db.saveOrUpdateNewAttributeValue(value, "text", key, object);
        });
    }

    private String description(String type) {
        String description = "";

        switch (type) {
            case "movie":
                description = json.get("title").toString();
                break;
            case "tv":
                description = json.get("name").toString();
                break;
            case "person":
                description = json.get("name").toString();
                break;
            case "cast":
                description = json.get("name").toString() + " as "
                        + json.get("character").toString() + " in " + json.get("title").toString();
                break;
            case "crew":
                description = json.get("name").toString() + " as " + json.get("job").toString()
                        + " in " + json.get("title").toString();
                break;
            default:
                break;
        }

        return description;
    }
}
