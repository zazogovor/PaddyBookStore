package com.example.c12437908.fypv2.Entities;

import com.example.c12437908.fypv2.register_login.SessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by c12437908 on 11/04/2018.
 */

public final class BasketEntity implements Serializable {

    private static BasketEntity INSTANCE = new BasketEntity();

    private List<BasketItem> books = new ArrayList<BasketItem>();

    private BasketEntity(){

    }

    public static BasketEntity getINSTANCE(){
        return INSTANCE;
    }

    public List<BasketItem> getBooks() {
        return books;
    }

    public void addBookToBasket(Book book, int quantity){
        BasketItem item = new BasketItem(book, quantity);

        books.add(item);
    }
}
