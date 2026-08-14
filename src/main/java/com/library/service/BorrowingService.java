package com.library.service;

import com.library.service.NotificationService;

import com.library.entity.Book;
import com.library.entity.BookStatus;
import com.library.entity.BorrowRecord;
import com.library.entity.Member;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final NotificationService notificationService;

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Transactional
    @CacheEvict(value = "books", key = "#bookId")
    public void borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        if (book.getStatus() == BookStatus.BORROWED) {
            throw new IllegalStateException("Book is already borrowed");
        }

        BorrowRecord record = BorrowRecord.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .build();

        book.setStatus(BookStatus.BORROWED);

        bookRepository.save(book);
        borrowRecordRepository.save(record);
        notificationService.sendBorrowNotification(memberId, bookId);
    }
}
