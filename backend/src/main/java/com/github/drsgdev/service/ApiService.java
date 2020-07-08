package com.github.drsgdev.service;

import java.util.Optional;

import com.github.drsgdev.model.Attribute;
import com.github.drsgdev.model.AttributeType;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeRepository;
import com.github.drsgdev.repository.AttributeTypeRepository;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.repository.DBObjectTypeRepository;
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
  private final String apiKey = "903ffebdb80a3af1d4c8a15ad338e3ea";
  private final String apiPath = "https://api.themoviedb.org/3";
  private final String imgPath = "https://image.tmdb.org/t/p/original";
  private final String imgPlaceholderPath =
      "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg";

  private final AttributeRepository attributes;
  private final AttributeValueRepository attrValues;
  private final AttributeTypeRepository attrTypes;
  private final DBObjectRepository objects;
  private final DBObjectTypeRepository objectTypes;

  private HttpStatus status;

  private JSONObject json = new JSONObject();
  private String objId;

  public HttpStatus addMovieToDB(String id) {
    return addObjectToDB(id, "movie");
  }

  public HttpStatus addShowToDB(String id) {
    return addObjectToDB(id, "show");
  }

  public HttpStatus addPersonToDB(String id) {
    return addObjectToDB(id, "person");
  }

  private HttpStatus addObjectToDB(String id, String type) {
    try {
      HttpResponse<JsonNode> res = Unirest.get(apiPath + "/" + type + "/{id}").routeParam("id", id)
          .queryString("api_key", apiKey).asJson();

      status = HttpStatus.valueOf(res.getStatus());
      json = res.getBody().getObject();
    } catch (UnirestException ex) {
      log.error("Fetching failed!", ex);
    }

    if (status != HttpStatus.OK) {
      return status;
    } else {
      log.info("Fetched " + type + ": {}, status: {}", description(type), status.toString());
    }

    parseObject(type);

    if (type.equals("movie")) {
      return addCreditsToDB(id, objId, "movie", json.get("title").toString());
    } else if (type.equals("show")) {
      return addCreditsToDB(id, objId, "tv", json.get("title").toString());
    }

    return status;
  }

  private HttpStatus addCreditsToDB(String id, String objId, String linkType, String title) {
    JSONArray cast = new JSONArray();
    JSONArray crew = new JSONArray();

    try {
      HttpResponse<JsonNode> res =
          Unirest.get(apiPath + "/{type}/{id}/credits").routeParam("id", id)
              .routeParam("type", linkType).queryString("api_key", apiKey).asJson();

      status = HttpStatus.valueOf(res.getStatus());
      cast = res.getBody().getObject().getJSONArray("cast");
      crew = res.getBody().getObject().getJSONArray("crew");
    } catch (UnirestException ex) {
      log.error("Fetching failed!", ex);
    }

    if (status != HttpStatus.OK) {
      return status;
    } else {
      log.info("Fetched credits for id {}, status: {}", id, status.toString());
    }

    for (int i = 0; i < cast.length(); i++) {
      json = cast.getJSONObject(i);
      json.put("person_id", json.get("id"));
      json.put("id", objId);
      json.put("title", title);

      parseObject("cast");
    }

    for (int i = 0; i < crew.length(); i++) {
      json = crew.getJSONObject(i);
      json.put("person_id", json.get("id"));
      json.put("id", objId);
      json.put("title", title);

      parseObject("crew");
    }

    return status;
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

    Optional<DBObjectType> objTypeFromDB = objectTypes.findByName(type);

    if (!objTypeFromDB.isPresent()) {
      log.error("Object type {} not found!", type);

      DBObjectType newType = new DBObjectType();
      newType.setName(type);

      objectTypes.save(newType);
      log.info("Saved new type: {}", type);

      objTypeFromDB = Optional.of(newType);
    }

    newObject.setType(objTypeFromDB.get());
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
          value = imgPath + value;
        } else {
          value = imgPlaceholderPath;
        }
      }

      if (key.equals("id") && (object.getType().getName().equals("movie")
          || object.getType().getName().equals("show"))) {
        value = objId;
      }

      AttributeValue newAttrValue;

      Optional<AttributeValue> attrValFromDB =
          attrValues.findByTypeNameAndValAndObjectId(key, value, object.getId());

      if (attrValFromDB.isPresent()) {
        newAttrValue = attrValFromDB.get();

        newAttrValue.setVal(value);
        attrValues.save(newAttrValue);

        return;
      } else {
        newAttrValue = new AttributeValue();
      }

      Optional<Attribute> attributeFromDB = attributes.findByName(key);

      if (!attributeFromDB.isPresent()) {
        log.error("Attribute {} not found!", key);

        Attribute newAttribute = new Attribute();
        newAttribute.setName(key);

        Optional<AttributeType> attrTypeFromDB = attrTypes.findByName("text");

        if (!attrTypeFromDB.isPresent()) {
          log.error("Attribute type \"text\" not found!");

          AttributeType newAttrType = new AttributeType();
          newAttrType.setName("text");

          attrTypes.save(newAttrType);
          log.info("Saved new attribute type: text");

          attrTypeFromDB = Optional.of(newAttrType);
        }
        newAttribute.setType(attrTypeFromDB.get());

        attributes.save(newAttribute);
        log.info("Saved new attribute: {}", key);

        attributeFromDB = Optional.of(newAttribute);
      }

      newAttrValue.setType(attributeFromDB.get());
      newAttrValue.setObject(object);
      newAttrValue.setVal(value);

      attrValues.save(newAttrValue);
    });
  }

  private String description(String type) {
    String description = "";

    switch (type) {
      case "movie":
        description = json.get("title").toString();
        break;
      case "show":
        description = json.get("title").toString();
        break;
      case "person":
        description = json.get("name").toString();
        break;
      case "cast":
        description = json.get("name").toString() + " as " + json.get("character").toString()
            + " in " + json.get("title").toString();
        break;
      case "crew":
        description = json.get("name").toString() + " as " + json.get("job").toString() + " in "
            + json.get("title").toString();
        break;
      default:
        break;
    }

    return description;
  }
}
