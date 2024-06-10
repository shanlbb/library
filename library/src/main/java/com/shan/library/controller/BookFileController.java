package com.shan.library.controller;

import com.shan.library.dto.FileDTO;
import com.shan.library.service.intf.IBookFileService;
import com.shan.library.util.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/book-files")
public class BookFileController {

    private final IBookFileService bookFileService;

    public BookFileController(IBookFileService bookFileService) {
        this.bookFileService = bookFileService;
    }

    @PostMapping(value = "/books/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PUBLISHER')")
    @SecurityRequirement(name = "bearerAuth")
    public void uploadFile(@PathVariable UUID bookId, @RequestParam("file") MultipartFile file) {
        bookFileService.upload(file, bookId, SecurityUtils.getCurrentUserId());
    }

    @GetMapping("/books/{bookId}")
    public FileDTO getByBookId(@PathVariable UUID bookId) {
        return bookFileService.getByBookId(bookId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PUBLISHER')")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteById(@PathVariable UUID id) {
        bookFileService.deleteById(id, SecurityUtils.getCurrentUserId());
    }
}
