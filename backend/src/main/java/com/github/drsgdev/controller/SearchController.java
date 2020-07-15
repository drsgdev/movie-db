package com.github.drsgdev.controller;

import java.util.List;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping(value = "/find/{type}")
    public ResponseEntity<List<DBObject>> getObjectByType(
            @PathVariable(value = "type") String type) {
        return response.fetchAllObjectsByType(type);
    }

    @GetMapping(value = "find/credits/cast")
    public ResponseEntity<List<DBObject>> getCastByMovieId(@RequestParam String id) {
        return response.fetchCreditsByMovieId(id, "cast");
    }

    @GetMapping(value = "find/credits/crew")
    public ResponseEntity<List<DBObject>> getCrewByMovieId(@RequestParam String id) {
        return response.fetchCreditsByMovieId(id, "crew");
    }
}
