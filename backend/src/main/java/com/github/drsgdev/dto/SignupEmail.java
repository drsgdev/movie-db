package com.github.drsgdev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupEmail {
    String recipent;
    String subject;
    String body;
}
