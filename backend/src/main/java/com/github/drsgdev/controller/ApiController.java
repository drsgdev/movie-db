package com.github.drsgdev.controller;

import com.github.drsgdev.service.ApiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/add")
@RequiredArgsConstructor
public class ApiController {

  private final ApiService api;

  @GetMapping(value = "/movie")
  public ResponseEntity<String> addMovie(@RequestParam String id) {
    return ResponseEntity.status(api.addMovieToDB(id)).build();
  }
}