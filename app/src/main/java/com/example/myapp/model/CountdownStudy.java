package com.example.myapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.myapp.repository.UserRepository;
import com.example.myapp.ui.login.LoginActivity;
import com.example.myapp.ui.login.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Timestamp;

@Entity(tableName = "study_table")
public class CountdownStudy {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String courseColor;
    @TypeConverters({Converter.class})
    private Timestamp startDate;
    @TypeConverters({Converter.class})
    private Timestamp endDate;
    private int ownedCoinNum;
    //@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    //@Embedded
    private String userId;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CountdownStudy(String courseColor, Timestamp startDate, Timestamp endDate,int ownedCoinNum)
    {
        this.courseColor = courseColor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownedCoinNum = ownedCoinNum;
        //if (LoginViewModel.userEmail!=null) {
        //    userId = LoginViewModel.userEmail;
        //}
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getEmail();
        }
    }

    public String getCourseColor() {
        return courseColor;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public int getOwnedCoinNum() {
        return ownedCoinNum;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setOwnedCoinNum(int ownedCoinNum) {
        this.ownedCoinNum = ownedCoinNum;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCourseColor(String courseColor) {
        this.courseColor = courseColor;
    }
}
