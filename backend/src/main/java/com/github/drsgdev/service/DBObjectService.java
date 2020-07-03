package com.github.drsgdev.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.repository.DBObjectTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DBObjectService {
  @Autowired
  DBObjectRepository objRepository;
  @Autowired
  DBObjectTypeRepository typesRepository;
  @Autowired
  AttributeValueRepository attrRepository;

  private void mapAttributes(List<DBObject> list) {
    list.forEach((object) -> mapAttributes(object));
  }

  private void mapAttributes(DBObject obj) {
    Map<String, String> fields = obj.getAttributes().parallelStream()
        .collect(Collectors.toMap((attr) -> attr.getAttribute().getDescr(),
            (attr) -> attr.getVal() == null ? attr.getDate_val().toString() : attr.getVal()));

    obj.setAttributeMap(fields);
  }

  public Optional<DBObject> findObjectById(Long id) {
    Optional<DBObject> object = objRepository.findById(id);

    if (!object.isPresent()) {
      return Optional.empty();
    }

    log.info("Found object: {}", object.get());

    mapAttributes(object.get());

    return object;
  }

  public Optional<List<DBObject>> findAllByType(String type) {
    Optional<DBObjectType> objType = typesRepository.findByType(type);

    if (!objType.isPresent()) {
      return Optional.empty();
    }

    Optional<List<DBObject>> objectList = objRepository.findAllByType(objType.get());

    if (!objectList.isPresent()) {
      return Optional.empty();
    }

    log.info("Found {} objects: {}", objectList.get().size(), objectList.get());

    mapAttributes(objectList.get());

    return objectList;
  }



  public Optional<List<DBObject>> findCastById(Long id) {
    Optional<DBObject> object = findObjectById(id);

    if (!object.isPresent()) {
      return Optional.empty();
    }

    String[] castIds = object.get().getAttributeMap().getOrDefault("cast", "").split(",");
    List<DBObject> cast = new ArrayList<>();
    for (String entryId : castIds) {
      findObjectById(Long.parseLong(entryId)).ifPresent((entry) -> cast.add(entry));
    }

    if (cast.size() == 0) {
      return Optional.empty();
    }

    return Optional.of(cast);
  }
}
