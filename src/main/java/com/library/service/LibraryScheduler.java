package com.library.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class LibraryScheduler {

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        System.out.println("SCHEDULED TASK: The time is now " + LocalDateTime.now() + ". Checking for overdue books...");
    }
}