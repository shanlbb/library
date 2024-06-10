package com.shan.library.dto.review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    @NotNull
    @NotBlank
    private String author;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String review;

    private LocalDateTime createdAt;
}
