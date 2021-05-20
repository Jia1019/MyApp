package com.example.myapp.model;

import android.widget.Toast;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.example.myapp.repository.UserRepository;
import com.example.myapp.ui.login.LoginActivity;
import com.example.myapp.ui.login.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@Entity(tableName = "todo_table")
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String context;
    private boolean isDone;
    //@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    //@Embedded
    private String userId;

    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Todo(String context, boolean isDone)
    {
        this.context = context;
        this.isDone = isDone;
        //if (LoginViewModel.userEmail!=null) {
        //    userId = LoginViewModel.userEmail;
        //}
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getEmail();
        }
    }

    public String getContext() {
        return context;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
