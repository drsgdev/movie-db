package com.github.drsgdev.controller;

import javax.validation.Valid;
import com.github.drsgdev.dto.AuthResponse;
import com.github.drsgdev.dto.RefreshTokenRequest;
import com.github.drsgdev.dto.SignupRequest;
import com.github.drsgdev.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final ResponseService response;

  @PostMapping(value = "/signup")
  public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest req) {
    return response.signup(req);
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<String> verify(@RequestParam String token) {
    return response.verifyUser(token);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody SignupRequest req) {
    return response.login(req);
  }

  @PostMapping(value = "/logout")
  public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest req) {
    return response.logout(req);
  }

  @PostMapping(value = "/refresh")
  public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
    return response.refresh(req);
  }
}
