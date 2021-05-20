package com.example.myapp.model;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey
    @NonNull
    private String email;
    private String username;
    private int coinNum;

    public User(String email,String username,int coinNum)
    {
        this.email = email;
        this.username = username;
        this.coinNum = coinNum;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public int getCoinNum() {
        return coinNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }
}
