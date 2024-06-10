package com.shan.library.repository;

import com.shan.library.entity.book.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

public interface IGenreRepository extends JpaRepository<Genre, UUID> {

    Set<Genre> findByNameIn(Set<String> genreNames);

    @Query("SELECT g FROM Genre g LEFT JOIN g.books b GROUP BY g ORDER BY COUNT(b) DESC")
    Page<Genre> findTopGenres(Pageable pageable);
}

