package com.shan.library.service.impl;

import com.shan.library.dto.FileDTO;
import com.shan.library.entity.book.Book;
import com.shan.library.entity.file.BookFile;
import com.shan.library.exception.BadRequestException;
import com.shan.library.exception.ForbiddenException;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.repository.IBookFileRepository;
import com.shan.library.repository.IBookRepository;
import com.shan.library.service.intf.IBookFileService;
import com.shan.library.service.intf.IBookService;
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
public class BookFileService implements IBookFileService {

    private final IFileStorageService fileStorageService;
    private final IBookFileRepository bookFileRepository;
    private final IBookRepository bookRepository;
    private final IBookService bookService;

    @Value("#{${app.file.book.extensions}}")
    private Set<String> validExtensions;
    @Value("${app.file.book.size.max}")
    private Long maxSize;

    public BookFileService(IFileStorageService fileStorageService, IBookFileRepository bookFileRepository,
                           IBookRepository bookRepository, IBookService bookService) {
        this.fileStorageService = fileStorageService;
        this.bookFileRepository = bookFileRepository;
        this.bookRepository = bookRepository;
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
        BookFile bookFile = new BookFile (
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                LocalDateTime.now()
        );
        if (!validExtensions.contains(FileUtils.getExtension(multipartFile.getOriginalFilename())))
            throw new BadRequestException("Invalid extension");
        if (bookFile.getSize() > maxSize)
            throw new BadRequestException("File is too large");
        bookFile.setBook(book);
        bookFileRepository.save(bookFile);
        fileStorageService.saveFile(bookFile, multipartFile.getInputStream());
    }

    @Override
    @Transactional
    public FileDTO getByBookId(@NonNull UUID bookId) {
        BookFile bookFile = bookFileRepository.findByBookId(bookId).orElseThrow(() ->
                new EntityNotFoundException("Book Cover File Not Found"));
        FileDTO fileDTO = new FileDTO(bookFile, fileStorageService.getFileUrl(bookFile.getId().toString()));
        Book book = bookFile.getBook();
        book.setDownloads(book.getDownloads() + 1);
        bookRepository.save(book);
        return fileDTO;
    }

    @Override
    public void deleteById(@NonNull UUID id, @NonNull UUID userId) {
        bookFileRepository.findById(id).ifPresent(bookFile -> {
            if (!bookFile.getBook().getUser().getId().equals(userId))
                throw new ForbiddenException("Access denied");
            fileStorageService.deleteFile(bookFile.getId().toString());
            bookFileRepository.delete(bookFile);
        });
    }

    @Override
    public void deleteByBookId(@NonNull UUID bookId, @NonNull UUID userId) {
        bookFileRepository.findByBookId(bookId).ifPresent(bookFile -> {
            if (!bookFile.getBook().getUser().getId().equals(userId))
                throw new ForbiddenException("Access denied");
            fileStorageService.deleteFile(bookFile.getId().toString());
            bookFileRepository.delete(bookFile);
        });
    }
}
