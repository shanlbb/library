package com.shan.library.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;

    private String email;

    private String lastName;

    private String firstName;
}
