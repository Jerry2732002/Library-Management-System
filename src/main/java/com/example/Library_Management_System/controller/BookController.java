package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.enums.Category;
import com.example.Library_Management_System.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("book")
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(path = "/add", produces = "application/json")
    public ResponseEntity<Map<String, String>> addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @DeleteMapping(path = "/remove/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> removeBook(@PathVariable("title") String title) {
        return bookService.removeBook(title);
    }

    @PutMapping(path = "/update/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> updateBook(@PathVariable("title") String title, @RequestBody Book book) {
        return bookService.updateBook(title, book);
    }

    @GetMapping(path = "/list", produces = "application/json")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(path = "/category/{category}", produces = "application/json")
    public List<Book> getBookByCategory(@PathVariable("category") Category category) {
        return bookService.getBookByCategory(category);
    }

    @GetMapping(path = "/author/{author}", produces = "application/json")
    public List<Book> getBookByAuthor(@PathVariable("author") String author) {
        return bookService.getBookByAuthor(author);
    }

    @GetMapping(path = "/title/{title}", produces = "application/json")
    public List<Book> getBookByTitle(@PathVariable("title") String title) {
        return bookService.getBookByTitle(title);
    }
}
