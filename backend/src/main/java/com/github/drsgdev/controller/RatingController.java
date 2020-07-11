package com.github.drsgdev.controller;

import java.util.List;
import javax.validation.Valid;
import com.github.drsgdev.dto.RatingRequest;
import com.github.drsgdev.dto.ReviewRequest;
import com.github.drsgdev.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final ResponseService response;

    @PostMapping(value = "/rate")
    public ResponseEntity<String> rateObject(@Valid @RequestBody RatingRequest req) {
        return response.rateObject(req);
    }

    @GetMapping(value = "/rate/get")
    public ResponseEntity<List<Integer>> getRating(@RequestParam Long id) {
        return response.getRatingById(id);
    }

    @PostMapping(value = "/review")
    public ResponseEntity<String> reviewObject(@Valid @RequestBody ReviewRequest req) {
        return response.reviewObject(req);
    }

    @GetMapping(value = "/review/get")
    public ResponseEntity<List<ReviewRequest>> getReviews(@RequestParam Long id) {
        return response.getReviewsById(id);
    }
}
