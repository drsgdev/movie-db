package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;

import com.github.drsgdev.model.DBObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
  @Autowired
  private DBObjectService service;

  public static <T> ResponseEntity<T> createResponse(Optional<T> obj) {
    if (!obj.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(obj.get());
  }

  public ResponseEntity<DBObject> fetchObjectById(Long id) {
    Optional<DBObject> movie = service.findObjectById(id);

    return ResponseService.createResponse(movie);
  }

  public ResponseEntity<List<DBObject>> fetchAllObjectsByType(String type) {
    Optional<List<DBObject>> movies = service.findAllByType(type);

    return ResponseService.createResponse(movies);
  }
}
