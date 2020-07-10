package com.github.drsgdev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

  private String username;
  private String token;
  private String message;
  private String refreshToken;
  private long expiresAt;
}
