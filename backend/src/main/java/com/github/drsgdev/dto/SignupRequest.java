package com.github.drsgdev.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

  @NotBlank
  private String username;

  @NotBlank
  private String email;

  @NotBlank
  private String password;
}
