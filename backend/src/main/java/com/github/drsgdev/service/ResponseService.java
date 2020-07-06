package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;

import com.github.drsgdev.model.DBObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseService {

  private final DBObjectService service;
  private final CreditsService personService;

  public static <T> ResponseEntity<T> createResponse(Optional<T> obj) {
    if (!obj.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(obj.get());
  }

  public ResponseEntity<DBObject> fetchObjectById(Long id) {
    Optional<DBObject> object = service.findObjectById(id);

    return createResponse(object);
  }

  public ResponseEntity<List<DBObject>> fetchAllObjectsByType(String type) {
    Optional<List<DBObject>> objectList = service.findAllByType(type);

    return createResponse(objectList);
  }

  public ResponseEntity<List<DBObject>> fetchCreditsByPersonId(String id, String type) {
    Optional<List<DBObject>> castList = personService.findCreditsByPersonId(id, type);

    return createResponse(castList);
  }

  public ResponseEntity<List<DBObject>> fetchCreditsByMovieId(String id, String type) {
    Optional<List<DBObject>> castList = personService.findCreditsByMovieId(id, type);

    return createResponse(castList);
  }
}
