package com.example.myapp.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;

import com.example.myapp.R;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.ui.login.LoginActivity;
import com.example.myapp.ui.login.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@Entity(tableName = "background_table")
public class Background {
    @PrimaryKey
    @NonNull
    private int url;
    private int costNum;
    private boolean isOwned;
    private boolean isApplied;
    //@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    //@Embedded
    private String userId;

    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Background(int url, int costNum, boolean isOwned, boolean isApplied)
    {
        this.url = url;
        this.costNum = costNum;
        this.isOwned = isOwned;
        this.isApplied = isApplied;
       // if (LoginViewModel.userEmail!=null) {
       //     userId = LoginViewModel.userEmail;
       // }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getEmail();
        }
    }

    public int getCostNum() {
        return costNum;
    }

    public int getUrl() {
        return url;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public boolean isApplied() {
        return isApplied;
    }

    public String getUserId() {
        return userId;
    }

    public void setCostNum(int costNum) {
        this.costNum = costNum;
    }

    public void setOwned(boolean owned) {
        isOwned = owned;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
