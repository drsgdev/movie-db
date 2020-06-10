package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.repository.DBObjectTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBObjectService {
  @Autowired
  DBObjectRepository objRepository;
  @Autowired
  DBObjectTypeRepository typesRepository;
  @Autowired
  AttributeValueRepository attrRepository;

  public Optional<DBObject> findObjectById(Long id) {
    Optional<DBObject> object = objRepository.findById(id);

    if (object.isPresent()) {
      System.out.println(object.get().getAttributes().size());

      return object;
    }

    return Optional.empty();
  }

  public Optional<List<DBObject>> findAllByType(String type) {
    Optional<DBObjectType> objType = typesRepository.findByDescr(type);

    if (!objType.isPresent()) {
      return Optional.empty();
    }

    Optional<List<DBObject>> obj = objRepository.findAllByType(objType.get());

    if (!obj.isPresent()) {
      return Optional.empty();
    }

    return obj;
  }
}
