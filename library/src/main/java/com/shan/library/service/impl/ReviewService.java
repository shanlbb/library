package com.shan.library.service.impl;

import com.shan.library.entity.book.Book;
import com.shan.library.entity.book.Review;
import com.shan.library.repository.IBookRepository;
import com.shan.library.repository.IReviewRepository;
import com.shan.library.service.intf.IBookService;
import com.shan.library.service.intf.IReviewService;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IBookRepository bookRepository;
    private final IBookService bookService;

    public ReviewService(IReviewRepository reviewRepository, IBookRepository bookRepository, IBookService bookService) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @Override
    @Transactional
    public Review create(@NonNull Review review, @NonNull UUID bookId) {
        Book book = bookService.getById(bookId);
        review.setBook(book);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        book.setRating(reviewRepository.findAverageRatingByBookId(bookId));
        bookRepository.save(book);
        return review;
    }

    @Override
    public Page<Review> getByBookId(@NonNull UUID bookId, Pageable pageable) {
        return reviewRepository.findByBookId(bookId, pageable);
    }
}
