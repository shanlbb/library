package com.shan.library.service.impl;

import com.shan.library.entity.book.Genre;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.repository.IGenreRepository;
import com.shan.library.service.intf.IGenreService;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenreService implements IGenreService {

    private final IGenreRepository genreRepository;

    public GenreService(IGenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre getById(@NonNull UUID id) {
        return genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
    }

    @Override
    public Set<Genre> getOrCreate(@NonNull Set<String> genreNames) {
        genreNames = genreNames.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<Genre> existingGenres = genreRepository.findByNameIn(genreNames);
        Set<String> existingGenreNames = existingGenres.stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());

        Set<Genre> newGenres = genreNames.stream()
                .filter(genreName -> !existingGenreNames.contains(genreName))
                .map(Genre::new)
                .collect(Collectors.toSet());
        newGenres = new HashSet<>(genreRepository.saveAll(newGenres));
        newGenres.addAll(existingGenres);
        return newGenres;
    }

    @Override
    public Page<Genre> getTopGenres(@NonNull Pageable pageable) {
        return genreRepository.findTopGenres(pageable);
    }
}
