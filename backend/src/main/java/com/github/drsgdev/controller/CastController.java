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
@RequestMapping(value = "/cast")
public class CastController {

  @Autowired
  ResponseService response;

  @GetMapping(value = "/find")
  public ResponseEntity<List<DBObject>> getCastByMovieId(@RequestParam Long id) {
    return response.fetchCastById(id);
  }
}
