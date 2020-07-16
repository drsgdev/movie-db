package com.github.drsgdev.dto;

import javax.validation.constraints.NotBlank;
import com.github.drsgdev.util.UserListRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastVisitedRequest implements UserListRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String username;
}
