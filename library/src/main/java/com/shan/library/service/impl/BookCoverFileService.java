package com.shan.library.service.impl;

import com.shan.library.dto.FileDTO;
import com.shan.library.entity.book.Book;
import com.shan.library.entity.file.BookCoverFile;
import com.shan.library.exception.BadRequestException;
import com.shan.library.exception.ForbiddenException;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.repository.IBookCoverFileRepository;
import com.shan.library.service.intf.IBookCoverFileService;
import com.shan.library.service.intf.IFileStorageService;
import com.shan.library.util.FileUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class BookCoverFileService implements IBookCoverFileService {

    private final IFileStorageService fileStorageService;
    private final IBookCoverFileRepository bookCoverFileRepository;
    private final BookService bookService;

    @Value("#{${app.file.cover.extensions}}")
    private Set<String> validExtensions;
    @Value("${app.file.cover.size.max}")
    private Long maxSize;

    public BookCoverFileService(IFileStorageService fileStorageService, IBookCoverFileRepository bookCoverFileRepository,
                                BookService bookService) {
        this.fileStorageService = fileStorageService;
        this.bookCoverFileRepository = bookCoverFileRepository;
        this.bookService = bookService;
    }

    @Override
    @Transactional
    @SneakyThrows
    public void upload(@NonNull MultipartFile multipartFile, @NonNull UUID bookId, @NonNull UUID userId) {
        Book book = bookService.getById(bookId);
        if (!book.getUser().getId().equals(userId))
            throw new ForbiddenException("Access denied");
        if (multipartFile.isEmpty())
            throw new BadRequestException("File is empty");
        BookCoverFile bookCoverFile = new BookCoverFile(
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                LocalDateTime.now()
        );
        if (!validExtensions.contains(FileUtils.getExtension(multipartFile.getOriginalFilename())))
            throw new BadRequestException("Invalid extension");
        if (bookCoverFile.getSize() > maxSize)
            throw new BadRequestException("File is too large");
        bookCoverFile.setBook(book);
        bookCoverFileRepository.save(bookCoverFile);
        fileStorageService.saveFile(bookCoverFile, multipartFile.getInputStream());
    }

    @Override
    public FileDTO getByBookId(@NonNull UUID bookId) {
        BookCoverFile bookCoverFile = bookCoverFileRepository.findByBookId(bookId).orElseThrow(() ->
                new EntityNotFoundException("Book Cover File Not Found"));
        return new FileDTO(bookCoverFile, fileStorageService.getFileUrl(bookCoverFile.getId().toString()));
    }

    @Override
    public void deleteById(@NonNull UUID id, @NonNull UUID userId) {
        bookCoverFileRepository.findById(id).ifPresent(bookCoverFile -> {
            if (!bookCoverFile.getBook().getUser().getId().equals(userId))
                throw new ForbiddenException("Access denied");
            fileStorageService.deleteFile(bookCoverFile.getId().toString());
            bookCoverFileRepository.delete(bookCoverFile);
        });
    }

    @Override
    public void deleteByBookId(@NonNull UUID bookId, @NonNull UUID userId) {
        bookCoverFileRepository.findByBookId(bookId).ifPresent(bookCoverFile -> {
            if (!bookCoverFile.getBook().getUser().getId().equals(userId))
                throw new ForbiddenException("Access denied");
            fileStorageService.deleteFile(bookCoverFile.getId().toString());
            bookCoverFileRepository.delete(bookCoverFile);
        });
    }
}
