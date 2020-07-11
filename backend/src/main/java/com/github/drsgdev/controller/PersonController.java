package com.github.drsgdev.controller;

import java.util.List;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.service.ResponseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/person")
@RequiredArgsConstructor
public class PersonController {

  private final ResponseService response;

  @GetMapping(value = "/find")
  public ResponseEntity<DBObject> getPersonById(@RequestParam Long id) {
    return response.fetchPersonById(id);
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<DBObject>> getAllPersons() {
    return response.fetchAllObjectsByType("person");
  }

  @GetMapping(value = "/credits/cast")
  public ResponseEntity<List<DBObject>> getCastByPersonId(@RequestParam String id) {
    return response.fetchCreditsByPersonId(id, "cast");
  }

  @GetMapping(value = "/credits/crew")
  public ResponseEntity<List<DBObject>> getCreditsByPersonId(@RequestParam String id) {
    return response.fetchCreditsByPersonId(id, "crew");
  }
}
