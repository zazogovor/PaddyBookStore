package com.example.c12437908.fypv2.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by c12437908 on 10/04/2018.
 */

public class Book  implements Serializable {
    private int id;
    private String ISBN;
    private String title;
    private String author;
    private int rating;
    private String image;
    private int quantity;
    private double price;
    List<Review> reviews = new ArrayList<Review>();

    public Book() {
    }

    public Book(int id, String ISBN, String title, String author, int rating, String image, int quantity, double price, List<Review> reviews) {
        this.id = id;
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
