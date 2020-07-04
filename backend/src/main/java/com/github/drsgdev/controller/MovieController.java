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
@RequestMapping(value = "/movie")
@RequiredArgsConstructor
public class MovieController {

  private final ResponseService response;

  @GetMapping(value = "/find")
  public ResponseEntity<DBObject> getMovieById(@RequestParam Long id) {
    return response.fetchObjectById(id);
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<DBObject>> getAllMovies() {
    return response.fetchAllObjectsByType("movie");
  }
}
