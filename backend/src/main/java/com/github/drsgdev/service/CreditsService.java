package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.util.Types;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditsService {
  private final DBObjectRepository objects;
  private final DBObjectService db;

  public Optional<List<DBObject>> findCreditsByPersonId(String id, Types type) {
    return findCredits(id, type, "person_id");
  }

  public Optional<List<DBObject>> findCreditsByTitleId(String id, Types type) {
    return findCredits(id, type, "id");
  }

  private Optional<List<DBObject>> findCredits(String id, Types type, String field) {
    Optional<List<DBObject>> creditList =
        objects.findAllByTypeNameAndAttributesTypeNameAndAttributesVal(type.getValue(), field, id);

    if (!creditList.isPresent()) {
      return Optional.empty();
    }

    db.mapAttributes(creditList.get());

    return creditList;
  }

}
