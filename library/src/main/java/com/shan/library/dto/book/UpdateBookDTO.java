package com.shan.library.dto.book;

import com.shan.library.dto.author.CreateAuthorDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateBookDTO {
    @NotNull
    @NotBlank
    private String title;

    private String description;

    @Min(1)
    private Integer pages;

    private LocalDate publishDate;

    private Set<String> genres;

    private Set<CreateAuthorDTO> authors;
}
