package com.example.Library_Management_System.dto;

import java.sql.Date;

public class Borrow {
    private int borrowID;
    private int userID;
    private int bookID;
    private Date borrowDate;
    private Date returnDate;
    private double fine;

    public Borrow() {
    }

    public Borrow(int userID, int bookID, Date borrowDate) {
        this.userID = userID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
    }

    public Borrow(int borrowID, int userID, int bookID, Date borrowDate, Date returnDate, double fine) {
        this.borrowID = borrowID;
        this.userID = userID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.fine = fine;
    }

    public int getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(int borrowID) {
        this.borrowID = borrowID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
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
