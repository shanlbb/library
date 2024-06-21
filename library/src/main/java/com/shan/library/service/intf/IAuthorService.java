package com.shan.library.service.intf;

import com.shan.library.dto.author.CreateAuthorDTO;
import com.shan.library.entity.book.Author;
import com.shan.library.filter.AuthorFilter;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface IAuthorService {

    Author getById(@NonNull UUID id);

    Set<Author> getOrCreate(@NonNull Set<CreateAuthorDTO> createAuthorDTOSet);

    Page<Author> getAuthors(@NonNull AuthorFilter authorFilter, @NonNull Pageable pageable);
}
