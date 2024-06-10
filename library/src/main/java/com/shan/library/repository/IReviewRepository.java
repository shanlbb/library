package com.shan.library.repository;

import com.shan.library.entity.book.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface IReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByBookId(UUID bookId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") UUID bookId);
}
