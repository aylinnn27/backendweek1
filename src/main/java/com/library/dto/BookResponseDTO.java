package com.library.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private LocalDate publishedDate;
    private Long authorId;
    private String authorName;
}
