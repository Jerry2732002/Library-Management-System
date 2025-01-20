package com.example.Library_Management_System.dto;

import com.example.Library_Management_System.enums.Category;
import com.example.Library_Management_System.enums.Membership;

import java.sql.Date;

public class BorrowDetails {
    private String email;
    private String firstName;
    private String lastName;
    private Membership membership;
    private String title;
    private String author;
    private Category category;
    private boolean rare;
    private int copiesAvailable;
    private Date borrowDate;
    private Date returnDate;
    private double fine;

    public BorrowDetails(String email, String firstName, String lastName, String membership, String title, String author, String category, boolean rare, int copiesAvailable, Date borrowDate, Date returnDate, double fine) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membership = Membership.valueOf(membership);
        this.title = title;
        this.author = author;
        this.category = Category.valueOf(category);
        this.rare = rare;
        this.borrowDate = borrowDate;
        this.copiesAvailable = copiesAvailable;
        this.returnDate = returnDate;
        this.fine = fine;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = Membership.valueOf(membership);
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

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}
