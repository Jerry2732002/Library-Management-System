package com.example.Library_Management_System.exception.book;

public class BookOutOfStock extends RuntimeException {
    public BookOutOfStock(String message) {
        super(message);
    }
}
