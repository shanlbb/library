package com.shan.library.mapper;

import com.shan.library.dto.book.GenreDTO;
import com.shan.library.entity.book.Genre;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class GenreMapper {

    public abstract GenreDTO toGenreDTO(Genre genre);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    public abstract Set<GenreDTO> toGenreDTOSet(Set<Genre> genres);

    public Page<GenreDTO> toGenreDTOPage(Page<Genre> genres) {
        return genres.map(this::toGenreDTO);
    }
}
