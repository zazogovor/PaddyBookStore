package com.example.c12437908.fypv2.register_login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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

    public SessionManager(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref.getBoolean(this.IS_LOGGED_IN, false);
        this.editor = pref.edit();
    }

    public void loginSession(String username, String password){
        editor.putString(this.USERNAME, username);
        editor.putString(this.PASSWORD, password);
        editor.putBoolean(this.IS_LOGGED_IN, true);
        editor.commit();
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public String getUsername(){
        return pref.getString(this.USERNAME, "");
    }

    public String getAccountType(){ return pref.getString(ACCOUNT_TYPE, ""); }
}
