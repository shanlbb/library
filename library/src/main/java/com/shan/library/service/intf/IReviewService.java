package com.shan.library.service.intf;

import com.shan.library.entity.book.Review;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IReviewService {
    Review create(@NonNull Review review, @NonNull UUID bookId);

    Page<Review> getByBookId(@NonNull UUID bookId, Pageable pageable);
}
