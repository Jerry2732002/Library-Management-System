package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.enums.Category;
import com.example.Library_Management_System.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public ResponseEntity<Map<String, String>> updateBook(String title, Book book) {
        return bookRepository.updateBook(title, book);
    }

    public ResponseEntity<Map<String, String>> updateBookCount(String title, int newAvailableCopies) {
        return bookRepository.updateBookCount(title, newAvailableCopies);
    }

    public ResponseEntity<Map<String, String>> addBook(Book book) {
        return bookRepository.addBook(book);
    }

    public ResponseEntity<Map<String, String>> removeBook(String title) {
        return bookRepository.removeBook(title);
    }

    public List<Book> getBookByCategory(Category category) {
        return bookRepository.getAllBooks().stream().filter(book -> book.getCategory() == category).collect(Collectors.toList());
    }

    public List<Book> getBookByAuthor(String author) {
        return bookRepository.getAllBooks().stream().filter(book -> book.getAuthor().equalsIgnoreCase(author)).collect(Collectors.toList());
    }

    public List<Book> getBookByTitle(String title) {
        return bookRepository.getAllBooks().stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).collect(Collectors.toList());
    }
}

