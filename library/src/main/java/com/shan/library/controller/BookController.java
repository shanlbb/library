package com.shan.library.controller;

import com.shan.library.dto.book.BookDTO;
import com.shan.library.dto.book.CreateBookDTO;
import com.shan.library.dto.book.UpdateBookDTO;
import com.shan.library.entity.book.Book;
import com.shan.library.filter.BookFilter;
import com.shan.library.mapper.BookMapper;
import com.shan.library.service.intf.IBookCoverFileService;
import com.shan.library.service.intf.IBookFileService;
import com.shan.library.service.intf.IBookService;
import com.shan.library.util.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@CrossOrigin(originPatterns = "http://127.0.0.1:5173")
@RestController
@RequestMapping("/books")
public class BookController {

    protected final IBookService bookService;
    private final IBookFileService bookFileService;
    private final IBookCoverFileService bookCoverFileService;
    protected final BookMapper bookMapper;

    protected BookController(IBookService bookService, IBookFileService bookFileService,
                             IBookCoverFileService bookCoverFileService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookFileService = bookFileService;
        this.bookCoverFileService = bookCoverFileService;
        this.bookMapper = bookMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('PUBLISHER')")
    @SecurityRequirement(name = "bearerAuth")
    public BookDTO createBook(@RequestBody @Valid CreateBookDTO createBookDTO) {
        return bookMapper.toBookDTO(bookService.create(
                bookMapper.toBook(createBookDTO),
                createBookDTO.getAuthors(),
                createBookDTO.getGenres(),
                SecurityUtils.getCurrentUserId()
        ));
    }

    @PostMapping("/{bookId}")
    @PreAuthorize("hasRole('PUBLISHER')")
    @SecurityRequirement(name = "bearerAuth")
    public BookDTO updateBook(@PathVariable UUID bookId, @RequestBody @Valid UpdateBookDTO updateBookDTO) {
        Book book = bookMapper.toBook(updateBookDTO);
        book.setId(bookId);
        return bookMapper.toBookDTO(bookService.update(
                book,
                updateBookDTO.getAuthors(),
                updateBookDTO.getGenres(),
                SecurityUtils.getCurrentUserId()
        ));
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('PUBLISHER')")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteBook(@PathVariable UUID bookId) {
        bookFileService.deleteByBookId(bookId, SecurityUtils.getCurrentUserId());
        bookCoverFileService.deleteByBookId(bookId, SecurityUtils.getCurrentUserId());
        bookService.deleteById(bookId, SecurityUtils.getCurrentUserId());
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable UUID id) {
        return bookMapper.toBookDTO(bookService.getByIdWithGenresAndAuthorsAndUser(id));
    }

    @GetMapping
    public Page<BookDTO> getBooks(@ParameterObject BookFilter filter, @ParameterObject Pageable pageable) {
        return bookMapper.toBookDTOPage(bookService.get(filter, pageable));
    }
}
