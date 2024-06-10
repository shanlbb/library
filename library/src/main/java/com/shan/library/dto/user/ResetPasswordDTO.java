package com.shan.library.dto.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @NotNull
    @NotBlank
    private String token;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 16)
    private String password;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 16)
    private String confirmPassword;

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatch() {
        return password.equals(confirmPassword);
    }}
