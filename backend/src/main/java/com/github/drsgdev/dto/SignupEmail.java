package com.github.drsgdev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupEmail {

  private String recipent;
  private String subject;
  private String body;
}
