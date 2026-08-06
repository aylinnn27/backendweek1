package com.library.service;

import com.library.dto.BookRequestDTO;
import com.library.dto.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookService {
    BookResponseDTO create(BookRequestDTO request);
    BookResponseDTO getById(Long id);
    Page<BookResponseDTO> getAll(Pageable pageable);
    BookResponseDTO update(Long id, BookRequestDTO request);
    void delete(Long id);

    List<BookResponseDTO> search(String title, String author);
}