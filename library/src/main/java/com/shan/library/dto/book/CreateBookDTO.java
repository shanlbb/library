package com.shan.library.dto.book;

import com.shan.library.dto.author.CreateAuthorDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateBookDTO {
    @NotNull
    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Min(1)
    private Integer pages;

    private LocalDate publishDate;

    @NotNull
    private Set<String> genres;

    @NotNull
    private Set<CreateAuthorDTO> authors;
}
