package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;

import com.github.drsgdev.model.Attribute;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeRepository;
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
  interface FieldList {
    public long getId();
  }
  enum MovieFields implements FieldList {
    TITLE(1), ORIGINAL_TITLE(2), TAGLINE(3), ORIGINAL_LANGUAGE(4), POSTER_PATH(5), RELEASE_DATE(6), RUNTIME(7),
    REVENUE(8), BUDGET(9), HOMEPAGE(10);

    private final long id;

    private MovieFields(long id) {
      this.id = id;
    }

    @Override
    public long getId() {
      return this.id;
    }

    public String toString() {
      return this.name().toLowerCase();
    }
  }

  private final String apiKey = "903ffebdb80a3af1d4c8a15ad338e3ea";
  private final String apiPath = "https://api.themoviedb.org/3";
  private final String imgPath = "https://image.tmdb.org/t/p/original";

  private final AttributeRepository attributes;
  private final AttributeValueRepository attrValues;
  private final DBObjectRepository objects;
  private final DBObjectTypeRepository objectTypes;

  private HttpStatus status;

  public HttpStatus addMovieToDB(String id) {
    JSONObject json = new JSONObject();

    try {
      HttpResponse<JsonNode> res = Unirest.get(apiPath + "/movie/{id}").routeParam("id", id)
          .queryString("api_key", apiKey).asJson();

      status = HttpStatus.valueOf(res.getStatus());

      json = res.getBody().getObject();
    } catch (UnirestException ex) {
      log.error("Fetching failed!", ex);
    }

    if (status != HttpStatus.OK) {
      return status;
    } else {
      log.info("Fetched movie: {}, status: {}", json.get("title").toString(), status.toString());
    }

    parseObject(json, "movie");

    return status;
  }

  private void parseObject(JSONObject json, String type) {
    DBObject object = new DBObject();

    Optional<DBObjectType> objTypeFromDB = objectTypes.findByType(type);

    if (!objTypeFromDB.isPresent()) {
      log.error("Object type {} not found!", type);
      status = HttpStatus.BAD_REQUEST;
      return;
    }

    object.setType(objTypeFromDB.get());
    object.setDescr(json.get("title").toString());
    
    objects.save(object);

    switch (type) {
      case "movie":
        parseAttributes(json, object, List.of(MovieFields.values()));
        break;
      default:
        break;
    }
  }

  private void parseAttributes(JSONObject json, DBObject object, List<? extends FieldList> fields) {
    fields.forEach((field) -> {
      String value = json.get(field.toString()).toString();
      
      AttributeValue attributeValue = new AttributeValue();

      Optional<Attribute> attributeFromDB = attributes.findById(field.getId());

      if (!attributeFromDB.isPresent()) {
        log.error("Attribute with id {} not found!", field.getId());
        status = HttpStatus.BAD_REQUEST;
        return;
      }
      attributeValue.setAttribute(attributeFromDB.get());

      Optional<DBObject> objectFromDB = objects.findById(object.getId());

      if (!objectFromDB.isPresent()) {
        log.error("Object with id {} not found!", object.getId());
        status = HttpStatus.BAD_REQUEST;
        return;
      }
      attributeValue.setObject(objectFromDB.get());

      if (field.getId() == 5) {
        value = imgPath + value;
      }

      attributeValue.setVal(value);
      attrValues.save(attributeValue);
    });
  }
}