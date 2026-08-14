package com.library.service.impl;

import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.service.BookFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookFileServiceImpl implements BookFileService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "application/pdf", ".pdf",
            "image/jpeg", ".jpg",
            "image/png", ".png"
    );

    private final BookRepository bookRepository;

    @Value("${app.file.upload-dir:uploads/books}")
    private String storageLocation;

    @Override
    public String upload(Long bookId, MultipartFile file) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id: " + bookId);
        }

        validateFile(file);

        String contentType = file.getContentType().toLowerCase(Locale.ROOT);
        String extension = ALLOWED_TYPES.get(contentType);
        Path directory = Paths.get(storageLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(directory);

            String storedFileName = bookId + "_" + UUID.randomUUID() + extension;
            Path target = directory.resolve(storedFileName).normalize();

            if (!target.getParent().equals(directory)) {
                throw new IllegalArgumentException("Invalid file name");
            }

            file.transferTo(target);
            return storedFileName;
        } catch (IOException e) {
            throw new IllegalStateException("Could not store file", e);
        }
    }

    @Override
    public Resource download(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id: " + bookId);
        }

        Path directory = Paths.get(storageLocation).toAbsolutePath().normalize();

        try {
            if (!Files.exists(directory)) {
                throw new ResourceNotFoundException("File not found for book id: " + bookId);
            }

            try (var files = Files.list(directory)) {
                Path file = files
                        .filter(path -> path.getFileName().toString().startsWith(bookId + "_"))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "File not found for book id: " + bookId));

                Resource resource = new UrlResource(file.toUri());
                if (!resource.exists() || !resource.isReadable()) {
                    throw new ResourceNotFoundException("File is not readable for book id: " + bookId);
                }
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Could not read stored file", e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not access stored file", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size must not exceed 5 MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.containsKey(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Only PDF, JPEG and PNG files are allowed");
        }
    }
}
