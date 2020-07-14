package com.github.drsgdev.controller;

import java.util.List;
import com.github.drsgdev.dto.FavoriteRequest;
import com.github.drsgdev.dto.UserProfileResponse;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final ResponseService res;

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserProfileResponse> findUser(@PathVariable("username") String username) {
        return res.fetchUserProfile(username);
    }

    @PostMapping(value = "add_favorite")
    public ResponseEntity<String> addTitleToFavorites(@RequestBody FavoriteRequest req) {
        return res.addTitleToFavorites(req);
    }

    @GetMapping(value = "/favorites")
    public ResponseEntity<List<DBObject>> getUserFavorites(@RequestParam String username) {
        return res.getUserFavorites(username);
    }

    @GetMapping(value = "/rated")
    public ResponseEntity<List<DBObject>> getUserRated(@RequestParam String username) {
        return res.getUserRated(username);
    }

    @GetMapping(value = "/reviewed")
    public ResponseEntity<List<DBObject>> getUserReviewed(@RequestParam String username) {
        return res.getUserReviewed(username);
    }
}
