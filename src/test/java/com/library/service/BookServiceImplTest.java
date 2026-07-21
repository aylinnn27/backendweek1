package com.library.service;

import com.library.dto.BookRequestDTO;
import com.library.dto.BookResponseDTO;
import com.library.entity.Author;
import com.library.entity.Book;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        author = Author.builder().id(1L).name("George Orwell").biography("British author").build();
        book = Book.builder()
                .id(1L)
                .title("1984")
                .isbn("9780451524935")
                .publishedDate(LocalDate.of(1949, 6, 8))
                .author(author)
                .build();
    }

    @Test
    void create_shouldReturnBookResponseDTO_whenAuthorExists() {
        BookRequestDTO request = BookRequestDTO.builder()
                .title("1984")
                .isbn("9780451524935")
                .publishedDate(LocalDate.of(1949, 6, 8))
                .authorId(1L)
                .build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO result = bookService.create(request);

        assertThat(result.getTitle()).isEqualTo("1984");
        assertThat(result.getAuthorId()).isEqualTo(1L);
        assertThat(result.getAuthorName()).isEqualTo("George Orwell");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void create_shouldThrow_whenAuthorDoesNotExist() {
        BookRequestDTO request = BookRequestDTO.builder()
                .title("1984")
                .isbn("9780451524935")
                .authorId(99L)
                .build();

        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Author not found");

        verify(bookRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnBook_whenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponseDTO result = bookService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getIsbn()).isEqualTo("9780451524935");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found");
    }

    @Test
    void delete_shouldRemoveBook_whenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        bookService.delete(1L);

        verify(bookRepository, times(1)).delete(book);
    }
}
