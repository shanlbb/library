package com.shan.library.service.impl;

import com.shan.library.dto.author.CreateAuthorDTO;
import com.shan.library.entity.book.Book;
import com.shan.library.exception.BadRequestException;
import com.shan.library.exception.ForbiddenException;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.filter.BookFilter;
import com.shan.library.repository.IBookRepository;
import com.shan.library.service.intf.IAuthorService;
import com.shan.library.service.intf.IBookService;
import com.shan.library.service.intf.IGenreService;
import com.shan.library.service.intf.IUserService;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class BookService implements IBookService {

    private final IBookRepository bookRepository;
    private final IAuthorService authorService;
    private final IGenreService genreService;
    private final IUserService userService;

    public BookService(IBookRepository bookRepository, IAuthorService authorService, IGenreService genreService,
                       IUserService userService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Book create(@NonNull Book book, @NonNull Set<CreateAuthorDTO> createAuthorDTOSet,
                       @NonNull Set<String> genreNames, @NonNull UUID userId) {
        if (createAuthorDTOSet.isEmpty())
            throw new BadRequestException("The book must have at least one author");
        if (genreNames.isEmpty())
            throw new BadRequestException("The book must have at least one genre");
        book.setDownloads(0L);
        book.setRating(0.0);
        book.setGenres(genreService.getOrCreate(genreNames));
        book.setAuthors(authorService.getOrCreate(createAuthorDTOSet));
        book.setUser(userService.getById(userId));
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(@NonNull Book book, Set<CreateAuthorDTO> createAuthorDTOSet, Set<String> genreNames,
                       @NonNull UUID userId) {
        Book updatedBook = this.getById(book.getId());
        if (!book.getUser().getId().equals(userId))
            throw new ForbiddenException("Access denied");

        if (book.getTitle() != null)
            updatedBook.setTitle(book.getTitle());
        if (book.getDescription() != null)
            updatedBook.setDescription(book.getDescription());
        if (book.getPages() != null)
            updatedBook.setPages(book.getPages());
        if (book.getPublishDate() != null)
            updatedBook.setPublishDate(book.getPublishDate());
        if (createAuthorDTOSet != null && !createAuthorDTOSet.isEmpty())
            updatedBook.setAuthors(authorService.getOrCreate(createAuthorDTOSet));
        if (genreNames != null && !genreNames.isEmpty())
            updatedBook.setGenres(genreService.getOrCreate(genreNames));

        return bookRepository.save(updatedBook);
    }

    @Override
    @Transactional
    public void deleteById(@NonNull UUID bookId, @NonNull UUID userId) {
        bookRepository.findById(bookId).ifPresent(book -> {
            if (!book.getUser().getId().equals(userId))
                throw new ForbiddenException("Access denied");
            bookRepository.delete(book);
        });
    }

    @Override
    public Book getById(@NonNull UUID id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Override
    public Book getByIdWithGenresAndAuthorsAndUser(@NonNull UUID id) {
        return bookRepository.findByIdWithGenresAndAuthorsAndUser(id).orElseThrow(() ->
                new EntityNotFoundException("Book not found"));
    }

    @Override
    public Page<Book> get(@NonNull BookFilter bookFilter, @NonNull Pageable pageable) {
        return bookRepository.findAll(bookFilter, pageable);
    }
}
