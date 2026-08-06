package com.library.specification;

import com.library.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> hasTitle(String title) {
        return (root, query, cb) -> (title == null || title.isEmpty()) ? null :
                cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> hasAuthorName(String authorName) {
        return (root, query, cb) -> (authorName == null || authorName.isEmpty()) ? null :
                cb.like(cb.lower(root.get("author").get("name")), "%" + authorName.toLowerCase() + "%");
    }
}