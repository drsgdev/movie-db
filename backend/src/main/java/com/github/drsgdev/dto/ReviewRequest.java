package com.github.drsgdev.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String username;

    private Integer rate = 0;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String date;

    @Override
    public String toString() {
        return "ReviewRequest [date=" + date + ", description=" + description + ", id=" + id
                + ", rate=" + rate + ", title=" + title + ", username=" + username + "]";
    }
}
