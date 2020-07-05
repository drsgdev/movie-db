package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.DBObjectRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {
  private final DBObjectRepository objects;

  public Optional<List<DBObject>> findCreditsByPersonId(String id, String type) {
    Optional<List<DBObject>> creditList = objects.findAllByTypeNameAndAttributesTypeNameAndAttributesVal(type,
        "person_id", id);

    if (!creditList.isPresent()) {
      return Optional.empty();
    }

    DBObjectService.mapAttributes(creditList.get());

    return creditList;
  }

}
