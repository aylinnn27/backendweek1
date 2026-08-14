package com.library.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

@Component
@Slf4j
public class FileCleanupTask {

    @Value("${app.file.upload-dir:uploads/books}")
    private String uploadDir;
    @Value("${app.cleanup.retention-days:7}")
    private int retentionDays;

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanOldFiles() {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            return;
        }

        Instant cutoff = Instant.now().minus(retentionDays, ChronoUnit.DAYS);

        try (Stream<Path> files = Files.list(uploadPath)) {
            files.filter(Files::isRegularFile)
                    .filter(path -> {
                        try {
                            return Files.getLastModifiedTime(path).toInstant().isBefore(cutoff);
                        } catch (IOException e) {
                            log.warn("Could not read file timestamp: {}", path, e);
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                            log.info("Deleted old uploaded file: {}", path);
                        } catch (IOException e) {
                            log.warn("Could not delete old file: {}", path, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Could not scan upload directory: {}", uploadPath, e);
        }
    }
}
