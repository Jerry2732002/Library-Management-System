package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(path = "admin/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody User admin) {
        return adminService.adminLogin(admin);
    }

    @PostMapping(path = "book/add")
    public ResponseEntity<Map<String, String>> addBook(@RequestBody Book book) {
        return adminService.addBook(book);
    }

    @DeleteMapping(path = "book/remove/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> removeBook(@PathVariable("title") String title) {
        return adminService.removeBook(title);
    }

    @PutMapping(path = "book/update/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> updateBook(@PathVariable("title") String title, @RequestBody Book book) {
        return adminService.updateBook(title, book);
    }

    @GetMapping(path = "book/list", produces = "application/json")
    public List<Book> getAllBooks() {
        return adminService.getAllBooks();
    }


}
