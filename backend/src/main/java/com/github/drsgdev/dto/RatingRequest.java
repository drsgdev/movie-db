package com.github.drsgdev.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    @NotNull
    private Long id;
    @NotNull
    private Integer rate;
    @NotBlank
    private String username;
}
