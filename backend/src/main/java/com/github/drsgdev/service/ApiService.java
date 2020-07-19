package com.github.drsgdev.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.github.drsgdev.model.AttributeType;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.util.AttrTypes;
import com.github.drsgdev.util.Types;
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

    public HttpStatus addMovieToDB(String id) {
        return addObjectToDB(id, Types.MOVIE);
    }

    public HttpStatus addShowToDB(String id) {
        return addObjectToDB(id, Types.SHOW);
    }

    public HttpStatus addPersonToDB(String id) {
        return addObjectToDB(id, Types.PERSON);
    }

    public HttpStatus addMoviesToDB(String startId, int count) {
        return addAll(startId, count, Types.MOVIE);
    }

    public HttpStatus addShowsToDB(String startId, int count) {
        return addAll(startId, count, Types.SHOW);
    }

    public HttpStatus addPeopleToDB(String startId, int count) {
        return addAll(startId, count, Types.PERSON);
    }

    private HttpStatus addAll(String startId, int count, Types type) {
        HttpStatus status = HttpStatus.OK;
        int start = Integer.parseInt(startId);

        List<Integer> ids = new ArrayList<>();
        for (int i = start + 1; i <= start + count; i++) {
            ids.add(i);
        }

        status = addObjectToDB(startId, type);
        ids.parallelStream().forEach(id -> addObjectToDB(id.toString(), type));

        return status;
    }

    private HttpStatus addObjectToDB(String id, Types type) {
        JSONObject json = new JSONObject();
        try {
            HttpResponse<JsonNode> res = Unirest.get(API_PATH + "/" + type.getValue() + "/{id}")
                    .routeParam("id", id).queryString("api_key", API_KEY).asJson();

            status = HttpStatus.valueOf(res.getStatus());
            json = res.getBody().getObject();
        } catch (UnirestException ex) {
            log.warn("Fetching failed!", ex);
        }

        if (status != HttpStatus.OK) {
            return status;
        } else {
            log.info("Fetched " + type.getValue() + ": {}, status: {}", description(type, json),
                    status.toString());
        }

        json.put("api_id", id);

        String objId = parseObject(type, json);

        final String poster_path =
                json.has("poster_path") ? json.get("poster_path").toString() : "";

        if (type == Types.MOVIE || type == Types.SHOW) {
            final String titleField = parseTitle(json, type);

            addCreditsToDB(id, objId, type, titleField, poster_path, Types.CAST);
            addCreditsToDB(id, objId, type, titleField, poster_path, Types.CREW);
        }

        return status;
    }

    private String parseTitle(JSONObject json, Types type) {
        switch (type) {
            case MOVIE:
                return json.get("title").toString();
            case SHOW:
                return json.get("name").toString();
            default:
                return "";
        }
    }

    public HttpStatus addCreditsToDB(String apiId, String objId, Types linkType, String title,
            String poster_path, Types creditType) {
        JSONArray credits = new JSONArray();

        try {
            HttpResponse<JsonNode> res = Unirest.get(API_PATH + "/{type}/{id}/credits")
                    .routeParam("id", apiId).routeParam("type", linkType.getValue())
                    .queryString("api_key", API_KEY).asJson();

            status = HttpStatus.valueOf(res.getStatus());
            credits = res.getBody().getObject().getJSONArray(creditType.getValue());
        } catch (UnirestException ex) {
            log.warn("Fetching failed!", ex.getMessage());
        }

        if (status != HttpStatus.OK) {
            return status;
        } else {
            log.info("Fetched credits for id {}, status: {}", apiId, status.toString());
        }

        List<JSONObject> creditList = new ArrayList<>();
        for (int i = 0; i < credits.length(); i++) {
            JSONObject json = credits.getJSONObject(i);
            addCreditInfo(json, apiId, objId, title, poster_path);

            creditList.add(json);
        }

        if (!creditList.isEmpty()) {
            parseObject(creditType, creditList.get(0));
            creditList.parallelStream().forEach(record -> parseObject(creditType, record));
        }

        return status;
    }

    private void addCreditInfo(JSONObject json, String id, String objId, String title,
            String poster_path) {
        json.put("person_id", json.get("id"));
        json.put("id", objId);
        json.put("api_id", id);
        json.put("title", title);
        json.put("poster_path", poster_path);
    }

    private String parseObject(Types type, JSONObject json) {
        DBObject newObject;
        String description = description(type, json);
        String objId;

        Optional<DBObject> objFromDB = objects.findByDescrAndTypeName(description, type.getValue());

        if (objFromDB.isPresent()) {
            newObject = objFromDB.get();

            newObject.setDescr(description);
            objects.save(newObject);
            objId = Long.toString(newObject.getId());

            log.info("Saved new {} id {}", type.getValue(), objId);

            parseAttributes(newObject, json, objId);
        } else {
            newObject = new DBObject();
        }

        DBObjectType objTypeFromDB = db.findObjTypeByNameOrCreate(type);

        newObject.setType(objTypeFromDB);
        newObject.setDescr(description);

        objects.save(newObject);
        objId = Long.toString(newObject.getId());

        log.info("Saved new {} id {}", type.getValue(), objId);

        parseAttributes(newObject, json, objId);

        return objId;
    }

    private boolean checkIfArray(String key, JSONObject json) {
        return json.optJSONArray(key) != null;
    }

    private boolean checkIfObject(String key, JSONObject json) {
        return json.optJSONObject(key) != null;
    }

    private void parseAttributes(DBObject object, JSONObject json, String objId) {

        AttributeType attrType = db.findAttrTypeByNameOrCreate(AttrTypes.TEXT.getValue());
        json.keySet().forEach(key -> {
            db.findAttrByNameAndTypeOrCreate(key, attrType);
        });

        json.keySet().parallelStream().forEach((key) -> {
            if (checkIfArray(key, json) || checkIfObject(key, json)) {
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

            if (key.equals("id") && (object.getType().getName().equals(Types.MOVIE.getValue())
                    || object.getType().getName().equals(Types.SHOW.getValue()))) {
                value = objId;
            }

            db.saveOrUpdateNewAttributeValue(value, AttrTypes.TEXT, key, object);
        });
    }

    private String description(Types type, JSONObject json) {
        String description = "";

        switch (type) {
            case MOVIE:
                description = json.get("title").toString();
                break;
            case SHOW:
                description = json.get("name").toString();
                break;
            case PERSON:
                description = json.get("name").toString();
                break;
            case CAST:
                description = json.get("name").toString() + " as "
                        + json.get("character").toString() + " in " + json.get("title").toString();
                break;
            case CREW:
                description = json.get("name").toString() + " as " + json.get("job").toString()
                        + " in " + json.get("title").toString();
                break;
            default:
                break;
        }

        return description;
    }
}
