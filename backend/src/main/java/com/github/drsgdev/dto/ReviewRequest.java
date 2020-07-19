package com.github.drsgdev.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(min = 50)
    private String description;

    private String date;

    @Override
    public String toString() {
        return "ReviewRequest [date=" + date + ", description=" + description + ", id=" + id
                + ", rate=" + rate + ", title=" + title + ", username=" + username + "]";
    }
}
