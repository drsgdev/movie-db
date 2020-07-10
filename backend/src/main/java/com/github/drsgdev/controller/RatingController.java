package com.github.drsgdev.controller;

import java.util.List;
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
    public ResponseEntity<String> rateObject(@RequestParam Long id, @RequestParam Integer rate,
            @RequestParam String username) {
        return response.rateObject(id, rate, username);
    }

    @GetMapping(value = "/rate/get")
    public ResponseEntity<List<Integer>> getRating(@RequestParam Long id) {
        return response.getRatingById(id);
    }
}
