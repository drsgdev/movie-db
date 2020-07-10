package com.github.drsgdev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private String very_bad;
    private String bad;
    private String ok;
    private String good;
    private String very_good;
}
