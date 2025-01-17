package com.example.Library_Management_System.enums;

public enum Membership {
    SILVER(5, 14),
    GOLD(10, 30),
    PLATINUM(20, 90);

    private final int borrowLimit;
    private final int dayLimit;

    Membership(int borrowLimit, int dayLimit) {
        this.borrowLimit = borrowLimit;
        this.dayLimit = dayLimit;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public int getDayLimit() {
        return dayLimit;
    }


}
