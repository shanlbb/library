package com.shan.library.service.intf;

import com.shan.library.dto.author.CreateAuthorDTO;
import com.shan.library.entity.book.Book;
import com.shan.library.entity.file.BookFile;
import com.shan.library.filter.BookFilter;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface IBookService {
    Book create(@NonNull Book book, @NonNull Set<CreateAuthorDTO> createAuthorDTOSet, @NonNull Set<String> genreNames,
                @NonNull UUID userId);

    Book update(@NonNull Book book, Set<CreateAuthorDTO> createAuthorDTOSet, Set<String> genreNames,
                @NonNull UUID userId);

    void deleteById(@NonNull UUID bookId, @NonNull UUID userId);

    Book getById(@NonNull UUID id);

    Book getByIdWithGenresAndAuthorsAndUser(@NonNull UUID id);

    Page<Book> get(@NonNull BookFilter bookFilter, @NonNull Pageable pageable);
}
