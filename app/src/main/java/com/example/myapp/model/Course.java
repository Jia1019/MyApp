package com.example.myapp.model;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@Entity(tableName = "course_table",primaryKeys = {"courseColor","userId"})
public class Course {
    @NonNull
    private String courseColor;
    private String courseName;
    @NonNull
    private String userId;


    public Course(String courseColor, String courseName)
    {
        this.courseColor = courseColor;
        this.courseName = courseName;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getEmail();
        }
    }

    public String getCourseColor() {
        return courseColor;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getUserId() {
        return userId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseColor(String courseColor) {
        this.courseColor = courseColor;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
