package com.example.myapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapp.model.Course;
import com.example.myapp.model.Todo;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Query("SELECT * FROM course_table WHERE userId=(SELECT email FROM user_table)")
    LiveData<List<Course>> getAllCourse();

}
