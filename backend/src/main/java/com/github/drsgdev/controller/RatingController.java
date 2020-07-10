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
public class RatingController {

    private final ResponseService response;

    @GetMapping(value = "/rate")
    public ResponseEntity<String> rateObject(@RequestParam("id") Long id,
            @RequestParam("rate") Integer rate) {
        return response.rateObject(id, rate);
    }

    @GetMapping(value = "/rate/get")
    public ResponseEntity<DBObject> getRating(@RequestParam Long id) {
        return response.getRatingById(id);
    }
}
