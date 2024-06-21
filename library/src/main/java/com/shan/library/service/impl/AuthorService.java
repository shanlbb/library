package com.shan.library.service.impl;

import com.shan.library.dto.author.CreateAuthorDTO;
import com.shan.library.entity.book.Author;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.filter.AuthorFilter;
import com.shan.library.repository.IAuthorRepository;
import com.shan.library.service.intf.IAuthorService;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthorService implements IAuthorService {

    private final IAuthorRepository authorRepository;

    public AuthorService(IAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getById(@NonNull UUID id) {
        return authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author not found"));
    }

    @Override
    public Set<Author> getOrCreate(@NonNull Set<CreateAuthorDTO> createAuthorDTOSet) {
        Set<CreateAuthorDTO> initCreateAuthorDTOSet = createAuthorDTOSet.stream()
                .map(createAuthorDTO -> new CreateAuthorDTO(
                        createAuthorDTO.getFirstName().toLowerCase(),
                        createAuthorDTO.getLastName().toLowerCase()
                )).collect(Collectors.toSet());

        Set<String> firstNames = initCreateAuthorDTOSet.stream()
                .map(CreateAuthorDTO::getFirstName)
                .collect(Collectors.toSet());
        Set<String> lastNames = initCreateAuthorDTOSet.stream()
                .map(CreateAuthorDTO::getLastName)
                .collect(Collectors.toSet());

        Set<Author> existingAuthors = authorRepository.findByFirstNameInAndLastNameIn(firstNames, lastNames).stream()
                .filter(author -> initCreateAuthorDTOSet.contains(new CreateAuthorDTO(
                        author.getFirstName(), author.getLastName()
                ))).collect(Collectors.toSet());

        Set<CreateAuthorDTO> existingCreateAuthorDTOSet = existingAuthors.stream()
                .map(existingAuthor ->
                        new CreateAuthorDTO(existingAuthor.getFirstName(), existingAuthor.getLastName()))
                .collect(Collectors.toSet());

        Set<Author> newAuthors = initCreateAuthorDTOSet.stream()
                .filter(createAuthorDTO -> !existingCreateAuthorDTOSet.contains(createAuthorDTO))
                .map(createAuthorDTO -> new Author(createAuthorDTO.getFirstName(), createAuthorDTO.getLastName()))
                .collect(Collectors.toSet());

        newAuthors = new HashSet<>(authorRepository.saveAll(newAuthors));
        newAuthors.addAll(existingAuthors);
        return newAuthors;
    }

    @Override
    public Page<Author> getAuthors(@NonNull AuthorFilter authorFilter, @NonNull Pageable pageable) {
        return authorRepository.findAll(authorFilter, pageable);
    }
}
