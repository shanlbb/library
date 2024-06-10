package com.shan.library.repository;

import com.shan.library.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface IBookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b JOIN FETCH b.genres JOIN FETCH b.authors JOIN FETCH b.user WHERE b.id = :id")
    Optional<Book> findByIdWithGenresAndAuthorsAndUser(@Param("id") UUID id);

    @Override
    @EntityGraph(attributePaths = {"genres", "authors", "user"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}
