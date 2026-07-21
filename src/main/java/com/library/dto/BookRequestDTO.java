package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @NotBlank(message = "ISBN is required")
    @Size(min = 5, max = 20, message = "ISBN must be between 5 and 20 characters")
    private String isbn;

    private LocalDate publishedDate;

    @NotNull(message = "authorId is required")
    private Long authorId;
}
