package com.shan.library.service.intf;

import com.shan.library.entity.book.Genre;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IGenreService {
    Genre getById(@NonNull UUID id);

    Set<Genre> getOrCreate(@NonNull Set<String> genreNames);

    Page<Genre> getTopGenres(@NonNull Pageable pageable);
}
