package com.shan.library.dto.book;

import com.shan.library.dto.author.CreateAuthorDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateBookDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Size(min = 1)
    private Integer pages;

    private LocalDate publishDate;

    private Set<String> genres;

    private Set<CreateAuthorDTO> authors;
}
