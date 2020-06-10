package com.github.drsgdev.controller;

import java.util.List;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.service.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/person")
public class PersonController {
  @Autowired
  ResponseService response;

  @GetMapping(value = "/find")
  public ResponseEntity<DBObject> getPersonById(@RequestParam Long id) {
    return response.fetchObjectById(id);
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<DBObject>> getAllPersons(@RequestParam String type) {
    return response.fetchAllObjectsByType(type);
  }

}
