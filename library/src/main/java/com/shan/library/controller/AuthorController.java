package com.shan.library.controller;

import com.shan.library.dto.author.AuthorDTO;
import com.shan.library.filter.AuthorFilter;
import com.shan.library.mapper.AuthorMapper;
import com.shan.library.service.intf.IAuthorService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@CrossOrigin(originPatterns = "http://127.0.0.1:5173")
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final IAuthorService authorService;
    private final AuthorMapper authorMapper;

    public AuthorController(IAuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @GetMapping("/{id}")
    public AuthorDTO getById(@PathVariable UUID id) {
        return authorMapper.toAuthorDTO(authorService.getById(id));
    }

    @GetMapping
    public Page<AuthorDTO> getAuthors(@ParameterObject AuthorFilter filter, @ParameterObject Pageable pageable) {
        return authorMapper.toAuthorDTOPage(authorService.getAuthors(filter, pageable));
    }
}
