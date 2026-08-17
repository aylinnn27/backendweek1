package com.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendAsyncNotification(String message) {
        log.info("Starting asynchronous notification: {}", message);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("ASYNC NOTIFICATION SENT: {}", message);
    }

    @Async
    public void sendBorrowNotification(Long memberId, Long bookId) {
        log.info("Starting asynchronous borrow notification for member {} and book {}", memberId, bookId);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        log.info("Borrow notification sent successfully for member {} and book {}", memberId, bookId);
    }
}