package com.shan.library.controller;

import com.shan.library.dto.book.GenreDTO;
import com.shan.library.mapper.GenreMapper;
import com.shan.library.service.intf.IGenreService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final IGenreService genreService;
    private final GenreMapper genreMapper;

    public GenreController(IGenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @GetMapping("/top")
    public Page<GenreDTO> getTopGenre(@ParameterObject Pageable pageable) {
        return genreMapper.toGenreDTOPage(genreService.getTopGenres(pageable));
    }
}
