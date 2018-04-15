package com.example.c12437908.fypv2.register_login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.c12437908.fypv2.Entities.BasketEntity;
import com.example.c12437908.fypv2.Entities.User;
import com.google.gson.Gson;

/**
 * Created by c12437908 on 10/25/2017.
 */

public class SessionManager extends Application{

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MyPreferences";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String IS_LOGGED_IN = "loggedin";
    private static final String ACCOUNT_TYPE = "none";
    private static final User USER = new User();
    private static final BasketEntity BASKET = BasketEntity.getINSTANCE();

    public SessionManager(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref.getBoolean(this.IS_LOGGED_IN, false);
        this.editor = pref.edit();
    }

    public void loginSession(User u){
        editor.putBoolean(this.IS_LOGGED_IN, true);
        Gson gson = new Gson();
        String json = gson.toJson(u);
        editor.putString("user", json);
        editor.commit();
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void saveBasket(BasketEntity b){
        Gson gson = new Gson();
        String json = gson.toJson(b);
        editor.putString("basket", json);
        editor.commit();
    }

    public BasketEntity getBasket(){
        Gson gson = new Gson();
        String json = pref.getString("basket", "");
        BasketEntity b = gson.fromJson(json, BasketEntity.class);
        return b;
    }

    public User getUser(){
        Gson gson = new Gson();
        String json = pref.getString("user", "");
        User u = gson.fromJson(json, User.class);
        return u;
    }
}
