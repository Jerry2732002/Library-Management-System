package com.example.Library_Management_System.dto;

import com.example.Library_Management_System.enums.Category;
import jakarta.validation.constraints.*;

public class Book {
    private int bookID;
    @NotNull(message = "Title cannot be null")
    @Size(min = 3, max = 100, message = "Title should be between 3 and 100 characters")
    private String title;

    @Size(min = 3, max = 100, message = "Author name should be between 3 and 100 characters")
    @Pattern(regexp = "[A-Za-z]*", message = "Author Name should only have alphabets")
    private String author;

    private Category category;
    private boolean rare;

    @PositiveOrZero(message = "No of Copies must be Zero or Positive")
    private int copiesAvailable;

    public Book() {
    }

    public Book(int bookID, String title, String author, Category category, boolean rare, int copiesAvailable) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.category = category;
        this.rare = rare;
        this.copiesAvailable = copiesAvailable;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isRare() {
        return rare;
    }

    public void setRare(boolean rare) {
        this.rare = rare;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

}
