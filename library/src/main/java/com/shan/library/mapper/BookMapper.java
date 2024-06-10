package com.shan.library.mapper;

import com.shan.library.dto.author.AuthorDTO;
import com.shan.library.dto.book.BookDTO;
import com.shan.library.dto.book.CreateBookDTO;
import com.shan.library.dto.book.GenreDTO;
import com.shan.library.dto.book.UpdateBookDTO;
import com.shan.library.entity.book.Book;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BookMapper {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private GenreMapper genreMapper;

    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "genres", ignore = true)
    public abstract Book toBook(CreateBookDTO createBookDTO);

    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "genres", ignore = true)
    public abstract Book toBook(UpdateBookDTO updateBookDTO);

    @Mapping(target = "authors", source = "book", qualifiedByName = "toAuthorDTOSet")
    @Mapping(target = "genres", source = "book", qualifiedByName = "toGenreDTOSet")
    @Mapping(target = "username", source = "book", qualifiedByName = "toUsername")
    public abstract BookDTO toBookDTO(Book book);

    public Page<BookDTO> toBookDTOPage(Page<Book> books) {
        return books.map(this::toBookDTO);
    }

    @Named("toAuthorDTOSet")
    protected Set<AuthorDTO> toAuthorDTOSet(Book book) {
        if (book == null)
            return null;
        if (Hibernate.isInitialized(book.getAuthors())) {
            if (book.getAuthors() == null)
                return null;
            return authorMapper.toAuthorDTOSet(book.getAuthors());
        }
        return null;
    }

    @Named("toGenreDTOSet")
    protected Set<GenreDTO> toGenreDTOSet(Book book) {
        if (book == null)
            return null;
        if (Hibernate.isInitialized(book.getGenres())) {
            if (book.getGenres() == null)
                return null;
            return genreMapper.toGenreDTOSet(book.getGenres());
        }
        return null;
    }

    @Named("toUsername")
    protected String toUsername(Book book) {
        if (book == null)
            return null;
        if (Hibernate.isInitialized(book.getUser())) {
            if (book.getUser() == null)
                return null;
            return book.getUser().getUsername();
        }
        return null;
    }
}
