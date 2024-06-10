package com.shan.library.repository;

import com.shan.library.entity.file.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IBookFileRepository extends JpaRepository<BookFile, UUID> {
    Optional<BookFile> findByBookId(UUID bookId);
}
