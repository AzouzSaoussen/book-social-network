package com.azouz.book_network_api.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


import java.util.UUID;

public record BookRequest(
        Integer id,
        @NotBlank(message = "100")
        String title,
        @NotBlank(message = "101")
        String authorName,
        @NotBlank(message = "102")
        String isbn,
        @NotBlank(message = "103")
        String synopsis,
        boolean shareable
) {
}

