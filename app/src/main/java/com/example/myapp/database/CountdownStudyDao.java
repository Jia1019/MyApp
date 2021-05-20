package com.example.myapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapp.model.CountdownStudy;
import com.example.myapp.model.Todo;

import java.util.List;

@Dao
public interface CountdownStudyDao {
    @Insert
    void insert(CountdownStudy countdownStudy);

    @Query("SELECT * FROM study_table WHERE userId=(SELECT email FROM user_table)")
    LiveData<List<CountdownStudy>> getAllStudy();
}
