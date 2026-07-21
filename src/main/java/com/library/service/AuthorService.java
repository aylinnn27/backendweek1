package com.library.service;

import com.library.dto.AuthorRequestDTO;
import com.library.dto.AuthorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponseDTO create(AuthorRequestDTO request);
    AuthorResponseDTO getById(Long id);
    Page<AuthorResponseDTO> getAll(Pageable pageable);
    AuthorResponseDTO update(Long id, AuthorRequestDTO request);
    void delete(Long id);
}
