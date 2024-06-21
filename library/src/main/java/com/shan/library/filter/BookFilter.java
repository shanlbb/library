package com.shan.library.filter;

import com.shan.library.entity.book.Author;
import com.shan.library.entity.book.Book;
import com.shan.library.entity.book.Genre;
import com.shan.library.entity.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@Getter
@Setter
public class BookFilter implements Specification<Book> {

    private UUID genreId;

    private UUID authorId;

    private String username;

    private UUID userId;

    private String searchTerms;


    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.distinct(true);
        Predicate predicate = criteriaBuilder.conjunction();
        if (genreId != null)
            predicate = criteriaBuilder.and(predicate, hasGenreId(root, criteriaBuilder, genreId));
        if (authorId != null)
            predicate = criteriaBuilder.and(predicate, hasAuthorId(root, criteriaBuilder, authorId));
        if (username != null && !username.isEmpty())
            predicate = criteriaBuilder.and(predicate, hasUsername(root, criteriaBuilder, username));
        if (userId != null)
            predicate = criteriaBuilder.and(predicate, hasUserId(root, criteriaBuilder, userId));
        if (searchTerms != null && !searchTerms.isEmpty())
            predicate = criteriaBuilder.and(predicate, hasSearchTerms(root, criteriaBuilder, searchTerms));
        return predicate;
    }

    private static Predicate hasGenreId(Root<Book> root, CriteriaBuilder criteriaBuilder, UUID genreId) {
        Join<Book, Genre> genreJoin = root.join("genres", JoinType.INNER);
        return criteriaBuilder.equal(genreJoin.get("id"), genreId);
    }

    private static Predicate hasAuthorId(Root<Book> root, CriteriaBuilder criteriaBuilder, UUID authorId) {
        Join<Book, Author> authorJoin = root.join("authors", JoinType.INNER);
        return criteriaBuilder.equal(authorJoin.get("id"), authorId);
    }

    private static Predicate hasUsername(Root<Book> root, CriteriaBuilder criteriaBuilder, String username) {
        Join<Book, User> userJoin = root.join("user", JoinType.INNER);
        return criteriaBuilder.equal(userJoin.get("username"), username);
    }

    private static Predicate hasUserId(Root<Book> root, CriteriaBuilder criteriaBuilder, UUID userId) {
        Join<Book, User> userJoin = root.join("user", JoinType.INNER);
        return criteriaBuilder.equal(userJoin.get("id"), userId);
    }

    private static Predicate hasSearchTerms(Root<Book> root, CriteriaBuilder criteriaBuilder, String searchTerms) {
        searchTerms = searchTerms.toLowerCase();
        String[] terms = searchTerms.split(" ");
        Predicate predicate  = criteriaBuilder.disjunction();
        for (String term : terms) {
            if (term.length() > 2) {
                predicate = criteriaBuilder.or(
                        predicate,
                        hasTitle(root, criteriaBuilder, term),
                        hasAuthorLastName(root, criteriaBuilder, term),
                        hasAuthorFirstName(root, criteriaBuilder, term)
                );
            }
        }
        return predicate;
    }

    private static Predicate hasTitle(Root<Book> root, CriteriaBuilder criteriaBuilder, String title) {
        return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title + "%");
    }

    private static Predicate hasAuthorLastName(Root<Book> root, CriteriaBuilder criteriaBuilder, String lastName) {
        Join<Book, Author> authorJoin = root.join("authors", JoinType.INNER);
        return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.get("lastName")), "%" + lastName + "%");
    }

    private static Predicate hasAuthorFirstName(Root<Book> root, CriteriaBuilder criteriaBuilder, String firstName) {
        Join<Book, Author> authorJoin = root.join("authors", JoinType.INNER);
       return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.get("firstName")), "%" + firstName + "%");
    }
}
