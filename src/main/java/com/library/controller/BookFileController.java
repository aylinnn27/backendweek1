package com.library.controller;

import com.library.service.BookFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book files", description = "Upload and download book files")
public class BookFileController {

    private final BookFileService bookFileService;

    @PostMapping(value = "/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file for a book")
    public ResponseEntity<String> upload(
            @PathVariable Long id,
            @Parameter(description = "PDF, JPEG or PNG file")
            @RequestPart("file") MultipartFile file) {

        String storedFileName = bookFileService.upload(id, file);
        return ResponseEntity.ok("File uploaded successfully: " + storedFileName);
    }

    @GetMapping("/{id}/file")
    @Operation(summary = "Download the file for a book")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Resource resource = bookFileService.download(id);

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        String filename = resource.getFilename();

        if (filename != null) {
            if (filename.endsWith(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (filename.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            }
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(filename == null ? "book-file" : filename)
                                .build()
                                .toString()
                )
                .body(resource);
    }
}
