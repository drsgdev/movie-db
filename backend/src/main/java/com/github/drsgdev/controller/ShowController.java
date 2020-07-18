package com.github.drsgdev.controller;

import java.util.List;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.service.ResponseService;
import com.github.drsgdev.util.Types;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/show")
@RequiredArgsConstructor
public class ShowController {

  private final ResponseService response;

  @GetMapping(value = "/find")
  public ResponseEntity<DBObject> getShowById(@RequestParam Long id) {
    return response.fetchObjectById(id);
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<DBObject>> getAllShows() {
    return response.fetchAllObjectsByType(Types.SHOW);
  }

  @GetMapping(value = "/credits/cast")
  public ResponseEntity<List<DBObject>> getCastByShowId(@RequestParam String id) {
    return response.fetchCreditsByTitleId(id, Types.CAST);
  }

  @GetMapping(value = "/credits/crew")
  public ResponseEntity<List<DBObject>> getCreditsByShowId(@RequestParam String id) {
    return response.fetchCreditsByTitleId(id, Types.CREW);
  }

}
