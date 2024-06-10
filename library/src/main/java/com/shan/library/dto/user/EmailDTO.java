package com.shan.library.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailDTO {
    @Email
    @NotNull
    private String email;

}
