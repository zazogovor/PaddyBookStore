package com.example.c12437908.fypv2.Entities;

import java.io.Serializable;

/**
 * Created by cinema on 4/14/2018.
 */

public class Review  implements Serializable {

    private int id;
    private int rating;
    private String comment;
    private Book book;
    private User postee;

    public Review(){

    }

    public Review(Book book, int rating, String comment, User postee){
        this.book = book;
        this.rating = rating;
        this.comment = comment;
        this.postee = postee;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getRating(){
        return this.rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public String getComment(){
        return this.comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public User getPostee(){
        return this.postee;
    }

    public void setPostee(User postee){
        this.postee = postee;
    }

}
