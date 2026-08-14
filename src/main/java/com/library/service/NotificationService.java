package com.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendBorrowNotification(Long memberId, Long bookId) {
        log.info("Starting asynchronous borrow notification for member {} and book {}",
                memberId, bookId);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Notification task was interrupted for member {} and book {}",
                    memberId, bookId);
            return;
        }

        log.info("Borrow notification sent successfully for member {} and book {}",
                memberId, bookId);
    }
}
