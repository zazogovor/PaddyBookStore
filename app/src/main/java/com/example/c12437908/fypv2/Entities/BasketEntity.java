package com.example.c12437908.fypv2.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c12437908 on 11/04/2018.
 */

public final class BasketEntity {

    private static BasketEntity INSTANCE = new BasketEntity();

    private List<Book> books = new ArrayList<Book>();

    private BasketEntity(){

    }

    public static BasketEntity getINSTANCE(){
        return INSTANCE;
    }

    public List<Book> getBooks() {
        return books;
    }
}
