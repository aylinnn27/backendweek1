package com.library.service.impl;

import com.library.dto.AuthorRequestDTO;
import com.library.dto.AuthorResponseDTO;
import com.library.entity.Author;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.AuthorRepository;
import com.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public AuthorResponseDTO create(AuthorRequestDTO request) {
        Author author = Author.builder()
                .name(request.getName())
                .biography(request.getBiography())
                .build();
        Author saved = authorRepository.save(author);
        return toResponse(saved);
    }

    @Override
    public AuthorResponseDTO getById(Long id) {
        Author author = findAuthorOrThrow(id);
        return toResponse(author);
    }

    @Override
    public Page<AuthorResponseDTO> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public AuthorResponseDTO update(Long id, AuthorRequestDTO request) {
        Author author = findAuthorOrThrow(id);
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        return toResponse(authorRepository.save(author));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author author = findAuthorOrThrow(id);
        authorRepository.delete(author);
    }

    private Author findAuthorOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    private AuthorResponseDTO toResponse(Author author) {
        return AuthorResponseDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .biography(author.getBiography())
                .build();
    }
}
