package com.example.c12437908.fypv2.Entities;

import java.io.Serializable;

/**
 * Created by cinema on 1/22/2018.
 */

public class User implements Serializable{
    private int id;
    private String username;
    private String password;
    private String email;
    private String type;

    public User(){

    }

    public User(String username,String email, String password, String type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
