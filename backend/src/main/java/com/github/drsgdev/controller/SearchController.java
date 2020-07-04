package com.github.drsgdev.controller;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.service.ResponseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchController {

  private final ResponseService response;

  @GetMapping(value = "/find")
  public ResponseEntity<DBObject> getObjectById(@RequestParam Long id) {
    return response.fetchObjectById(id);
  }
}