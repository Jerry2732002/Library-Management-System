package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.rowmapper.BookRowMapper;
import com.example.Library_Management_System.enums.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:test-database.sql")
public class BookRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookRepository bookRepository;

    @Test
    void testGetBookByTitleFound() {
        Book book = new Book(1, "The Great Gatsby" , "F. Scott Fitzgerald", Category.FICTION, false, 5);

        String sql = "SELECT * FROM Books WHERE Title = ?";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{"The Great Gatsby"}), any(BookRowMapper.class))).thenReturn(book);

        Book result = bookRepository.getBookByTitle("The Great Gatsby");

        assertNotNull(result);
        assertEquals("The Great Gatsby", result.getTitle());
        assertEquals("F. Scott Fitzgerald", result.getAuthor());
    }

    @Test
    void testGetBookByTitleNotFound() {
        String title = "Nonexistent Book";
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM Books WHERE Title = ?"), eq(new Object[]{title}), any(BookRowMapper.class)))
                .thenReturn(null);

        Book result = bookRepository.getBookByTitle(title);

        assertNull(result);
    }

    @Test
    void testGetAllBooksFound() {
        List<Book> books = Arrays.asList(
                new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", Category.FICTION, false, 5),
                new Book(2, "1984", "George Orwell", Category.FICTION, false, 10)
        );

        String sql = "SELECT * FROM Books";
        when(jdbcTemplate.query(eq(sql), any(BookRowMapper.class))).thenReturn(books);

        List<Book> actualBooks = bookRepository.getAllBooks();

        assertEquals(actualBooks, books);

    }

    @Test
    void testGetAllBooksNoBooks() {
        when(jdbcTemplate.query(eq("SELECT * FROM Books"), any(BookRowMapper.class))).thenReturn(null);

        List<Book> book = bookRepository.getAllBooks();

        assertNull(book);
    }

    @Test
    void testUpdateBookSuccess() {
        Book book = new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", Category.FICTION, false, 5);
        String title = "The Great Gatsby";
        String sql = "UPDATE Books SET Title = ?, Author = ?, Category = ?, Rare = ?, CopiesAvailable = ? WHERE Title = ?";

        when(jdbcTemplate.update(eq(sql), eq(book.getTitle()), eq(book.getAuthor()), eq(book.getCategory().name()),
                eq(book.isRare()), eq(book.getCopiesAvailable()), eq(title))).thenReturn(1);

        ResponseEntity<Map<String, String>> response = bookRepository.updateBook(title, book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book Updated Successfully", response.getBody().get("message"));
    }

    @Test
    void testUpdateBookFailure() {
        Book book = new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", Category.FICTION, false, 5);
        String title = "The Great Gatsby";
        String sql = "UPDATE Books SET Title = ?, Author = ?, Category = ?, Rare = ?, CopiesAvailable = ? WHERE Title = ?";

        when(jdbcTemplate.update(eq(sql), eq(book.getTitle()), eq(book.getAuthor()), eq(book.getCategory().name()),
                eq(book.isRare()), eq(book.getCopiesAvailable()), eq(title))).thenReturn(0);

        ResponseEntity<Map<String, String>> response = bookRepository.updateBook(title, book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed To Update Book", response.getBody().get("message"));
    }
    @Test
    void testUpdateBookCountSuccess() {
        String title = "The Great Gatsby";
        int newAvailableCopies = 1;
        String sql = "UPDATE Books SET CopiesAvailable = ? WHERE Title = ?";

        when(jdbcTemplate.update(eq(sql), eq(newAvailableCopies), eq(title))).thenReturn(1);

        ResponseEntity<Map<String, String>> response = bookRepository.updateBookCount(title, newAvailableCopies);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Available Copies Updated Successfully", response.getBody().get("message"));
    }

    @Test
    void testUpdateBookCountFailure() {
        String title = "The Great Gatsby";
        int newAvailableCopies = 1;
        String sql = "UPDATE Books SET CopiesAvailable = ? WHERE Title = ?";

        when(jdbcTemplate.update(eq(sql), eq(newAvailableCopies), eq(title))).thenReturn(0);

        ResponseEntity<Map<String, String>> response = bookRepository.updateBookCount(title, newAvailableCopies);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed To Update Available Copies", response.getBody().get("message"));
    }

    @Test
    void testUpdateBookCountNegativeCopies() {
        String title = "The Great Gatsby";
        int newAvailableCopies = -1;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookRepository.updateBookCount(title, newAvailableCopies);
        });

        assertEquals("Book: The Great Gatsby out of stock", exception.getMessage());
    }
    @Test
    void testAddBookSuccess() {
        Book book = new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", Category.FICTION, false, 5);
        String sql = "INSERT INTO Books (Title, Author, Category, Rare, CopiesAvailable) VALUES (?,?,?,?,?)";

        when(jdbcTemplate.update(eq(sql), eq(book.getTitle()), eq(book.getAuthor()), eq(book.getCategory().name()),
                eq(book.isRare()), eq(book.getCopiesAvailable()))).thenReturn(1);

        ResponseEntity<Map<String, String>> response = bookRepository.addBook(book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book Added Successfully", response.getBody().get("message"));
    }

    @Test
    void testAddBookFailure() {
        Book book = new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", Category.FICTION, false, 5);
        String sql = "INSERT INTO Books (Title, Author, Category, Rare, CopiesAvailable) VALUES (?,?,?,?,?)";

        when(jdbcTemplate.update(eq(sql), eq(book.getTitle()), eq(book.getAuthor()), eq(book.getCategory().name()),
                eq(book.isRare()), eq(book.getCopiesAvailable()))).thenReturn(0);

        ResponseEntity<Map<String, String>> response = bookRepository.addBook(book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed To Add Book", response.getBody().get("message"));
    }

}
