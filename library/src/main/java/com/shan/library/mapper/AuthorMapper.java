package com.shan.library.mapper;

import com.shan.library.dto.author.AuthorDTO;
import com.shan.library.entity.book.Author;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AuthorMapper {

    public abstract AuthorDTO toAuthorDTO(Author author);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    public abstract Set<AuthorDTO> toAuthorDTOSet(Set<Author> authors);

    public Page<AuthorDTO> toAuthorDTOPage(Page<Author> authors) {
        return authors.map(this::toAuthorDTO);
    }
}
