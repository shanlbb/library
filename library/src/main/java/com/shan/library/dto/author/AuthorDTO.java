package com.shan.library.dto.author;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthorDTO {
    private UUID id;

    private String firstName;

    private String lastName;
}
