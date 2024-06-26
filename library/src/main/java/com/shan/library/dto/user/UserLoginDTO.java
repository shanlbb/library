package com.shan.library.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDTO {
    @Email
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 16)
    private String password;
}
