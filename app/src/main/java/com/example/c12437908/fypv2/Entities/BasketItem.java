package com.example.c12437908.fypv2.Entities;

/**
 * Created by cinema on 4/14/2018.
 */

public class BasketItem {

    private int amount;
    private Book book;

    public BasketItem(Book book, int amount) {
        this.amount = amount;
        this.book = book;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
