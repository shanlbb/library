package com.shan.library.entity.file;

import com.shan.library.entity.book.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_cover_files")
@Getter
@Setter
public class BookCoverFile extends File {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    public BookCoverFile() {
        super();
    }

    public BookCoverFile(String originalName, String type, Long size, LocalDateTime createdAt) {
        super(originalName, type, size, createdAt);
    }
}
