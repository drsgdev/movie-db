package com.github.drsgdev.controller;

import com.github.drsgdev.dto.AuthResponse;
import com.github.drsgdev.dto.SignupRequest;
import com.github.drsgdev.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final ResponseService response;

  @PostMapping(value = "/signup")
  public ResponseEntity<String> signup(@RequestBody SignupRequest req) {
    return response.addUserToDB(req);
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<String> verify(@RequestParam String token) {
    return response.verifyUser(token);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<AuthResponse> postMethodName(@RequestBody SignupRequest req) {
    return response.login(req);
  }

}
