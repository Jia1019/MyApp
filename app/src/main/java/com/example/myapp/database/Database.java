package com.example.myapp.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapp.model.Background;
import com.example.myapp.model.CountdownStudy;
import com.example.myapp.model.Course;
import com.example.myapp.model.Todo;
import com.example.myapp.model.User;

@androidx.room.Database(entities = {Todo.class, Course.class, Background.class, CountdownStudy.class, User.class}, version = 5)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public abstract TodoDao todoDao();
    public abstract CourseDao courseDao();
    public abstract BackgroundDao backgroundDao();
    public abstract CountdownStudyDao countdownStudyDao();
    public abstract UserDao userDao();

    public static  synchronized Database getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
            Database.class, "studyPartner_database").fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }

}
