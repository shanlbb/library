package com.shan.library.dto.book;

import com.shan.library.dto.author.AuthorDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class BookDTO {
    private UUID id;

    private String title;

    private String description;

    private Integer pages;

    private LocalDate publishDate;

    private Long downloads;

    private Double rating;

    private Set<GenreDTO> genres;

    private Set<AuthorDTO> authors;

    private String username;
}
