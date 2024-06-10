package com.shan.library.filter;

import com.shan.library.entity.book.Author;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class AuthorFilter implements Specification<Author> {

    private String searchTerms;

    @Override
    public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.distinct(true);
       Predicate predicate = criteriaBuilder.conjunction();
       if (searchTerms != null && !searchTerms.isEmpty())
           predicate = criteriaBuilder.and(predicate, hasSearchTerms(root, criteriaBuilder, searchTerms));
       return predicate;
    }

    private static Predicate hasSearchTerms(Root<Author> root, CriteriaBuilder criteriaBuilder, String searchTerms) {
        searchTerms = searchTerms.toLowerCase();
        String[] terms = searchTerms.split(" ");
        Predicate predicate = criteriaBuilder.disjunction();
        for (String term : terms) {
            predicate = criteriaBuilder.or(
                    predicate,
                    hasFirstName(root, criteriaBuilder, term),
                    hasLastName(root, criteriaBuilder, term)
            );
        }
        return predicate;
    }

    private static Predicate hasFirstName(Root<Author> root, CriteriaBuilder criteriaBuilder, String firstName) {
        return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName + "%");
    }

    private static Predicate hasLastName(Root<Author> root, CriteriaBuilder criteriaBuilder, String lastName) {
        return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName + "%");
    }
}
