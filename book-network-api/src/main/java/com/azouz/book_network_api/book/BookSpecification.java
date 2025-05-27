package com.azouz.book_network_api.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> withOwnerId(String owner) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdBy"), owner);
    }
}
