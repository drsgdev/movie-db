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

  private final AttributeRepository attributes;
  private final AttributeValueRepository attrValues;
  private final AttributeTypeRepository attrTypes;
  private final DBObjectRepository objects;
  private final DBObjectTypeRepository objectTypes;

  private HttpStatus status;

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
    JSONObject json = new JSONObject();

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
      log.info("Fetched " + type + ": {}, status: {}", description(json), status.toString());
    }

    parseObject(json, type);

    return status;
  }

  private void parseObject(JSONObject json, String type) {
    DBObject object = new DBObject();

    Optional<DBObjectType> objTypeFromDB = objectTypes.findByType(type);

    if (!objTypeFromDB.isPresent()) {
      log.error("Object type {} not found!", type);

      DBObjectType newType = new DBObjectType();
      newType.setType(type);

      objectTypes.save(newType);
      log.info("Saved new type: {}", type);

      objTypeFromDB = Optional.of(newType);
    }

    object.setType(objTypeFromDB.get());
    object.setDescr(description(json));

    objects.saveAndFlush(object);

    parseAttributes(json, object);
  }

  private void parseAttributes(JSONObject json, DBObject object) {

    json.keySet().forEach((key) -> {
      String value = json.get(key).toString();

      AttributeValue attributeValue = new AttributeValue();

      Optional<Attribute> attributeFromDB = attributes.findByDescr(key);

      if (!attributeFromDB.isPresent()) {
        log.error("Attribute {} not found!", key);

        Attribute newAttribute = new Attribute();
        newAttribute.setDescr(key);

        Optional<AttributeType> attrTypeFromDB = attrTypes.findByType("text");

        if (!attrTypeFromDB.isPresent()) {
          log.error("Attribute type \"text\" not found!");

          AttributeType newAttrType = new AttributeType();
          newAttrType.setType("text");

          attrTypes.save(newAttrType);
          log.info("Saved new attribute type: text");

          attrTypeFromDB = Optional.of(newAttrType);
        }
        newAttribute.setType(attrTypeFromDB.get());

        attributes.save(newAttribute);
        log.info("Saved new attribute: {}", key);

        attributeFromDB = Optional.of(newAttribute);
      }
      attributeValue.setAttribute(attributeFromDB.get());

      attributeValue.setObject(object);

      if (key.contains("_path")) {
        value = imgPath + value;
      }

      attributeValue.setVal(value);
      attrValues.saveAndFlush(attributeValue);
    });
  }

  private String description(JSONObject json) {
    return json.has("title") ? json.get("title").toString() : json.get("name").toString();
  }
}