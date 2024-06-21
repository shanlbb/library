package com.shan.library.controller;

import com.shan.library.dto.review.ReviewDTO;
import com.shan.library.mapper.ReviewMapper;
import com.shan.library.service.intf.IReviewService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@CrossOrigin(originPatterns = "http://127.0.0.1:5173")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final IReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(IReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping("/books/{bookId}")
    public ReviewDTO create(@PathVariable UUID bookId, @Valid @RequestBody ReviewDTO reviewDTO) {
        return reviewMapper.toReviewDTO(reviewService.create(
                reviewMapper.toReview(reviewDTO),
                bookId
        ));
    }

    @GetMapping("/books/{bookId}")
    public Page<ReviewDTO> getByBookId(@PathVariable UUID bookId, @ParameterObject Pageable pageable) {
        return reviewMapper.toReviewDTOPage(reviewService.getByBookId(bookId, pageable));
    }
}
