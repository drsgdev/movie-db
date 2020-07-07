package com.github.drsgdev.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.DBObjectRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBObjectService {

  private final DBObjectRepository objRepository;

  public static void mapAttributes(List<DBObject> list) {
    list.forEach((object) -> mapAttributes(object));
  }

  public static void mapAttributes(DBObject obj) {
    Map<String, String> fields = obj.getAttributes().parallelStream()
        .collect(Collectors.toMap((attr) -> attr.getType().getName(),
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
    Optional<List<DBObject>> objectList = objRepository.findAllByTypeName(type);

    if (!objectList.isPresent()) {
      return Optional.empty();
    }

    log.info("Found {} objects: {}", objectList.get().size(), objectList.get());

    mapAttributes(objectList.get());

    return objectList;
  }
}
