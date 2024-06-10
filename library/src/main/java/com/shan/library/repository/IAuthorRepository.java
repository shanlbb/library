package com.shan.library.repository;

import com.shan.library.entity.book.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface IAuthorRepository extends JpaRepository<Author, UUID>, JpaSpecificationExecutor<Author> {
    Set<Author> findByFirstNameInAndLastNameIn(Set<String> firstName, Set<String> lastName);
}
