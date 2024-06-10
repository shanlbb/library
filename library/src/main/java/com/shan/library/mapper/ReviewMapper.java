package com.shan.library.mapper;

import com.shan.library.dto.review.ReviewDTO;
import com.shan.library.entity.book.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ReviewMapper {

    public abstract Review toReview(ReviewDTO dto);

    public abstract ReviewDTO toReviewDTO(Review review);

    public Page<ReviewDTO> toReviewDTOPage(Page<Review> reviews) {
        return reviews.map(this::toReviewDTO);
    }
}
