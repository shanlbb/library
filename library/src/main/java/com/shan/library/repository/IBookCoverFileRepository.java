package com.shan.library.repository;

import com.shan.library.entity.file.BookCoverFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IBookCoverFileRepository extends JpaRepository<BookCoverFile, UUID> {
    Optional<BookCoverFile> findByBookId(UUID bookId);
}
