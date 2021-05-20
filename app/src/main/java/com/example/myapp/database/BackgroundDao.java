package com.example.myapp.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapp.model.Background;
import com.example.myapp.model.Todo;

import java.util.List;

@Dao
public interface BackgroundDao {
    @Insert
    void insert(Background background);

    @Update
    void update(Background background);

    @Query("SELECT * FROM background_table WHERE userId=(SELECT email FROM user_table)")
    LiveData<List<Background>> getAllBackgrounds();

    @Query("SELECT * FROM background_table WHERE isApplied =:isApplied AND userId=(SELECT email FROM user_table)")
    LiveData<Background> getCurrentBackground(boolean isApplied);
}
