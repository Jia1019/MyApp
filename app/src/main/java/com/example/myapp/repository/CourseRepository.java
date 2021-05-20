package com.example.myapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapp.database.CourseDao;
import com.example.myapp.database.Database;
import com.example.myapp.model.Course;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseRepository {
    private CourseDao courseDao;
    private static CourseRepository instance;
    private LiveData<List<Course>> allCourse;
    private final ExecutorService executorService;

    public CourseRepository(Application application)
    {
        Database database = Database.getInstance(application);
        courseDao = database.courseDao();
        allCourse = courseDao.getAllCourse();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized CourseRepository getInstance(Application application)
    {
        if (instance==null)
        {
            instance = new CourseRepository(application);
        }
        return instance;
    }

    public LiveData<List<Course>> getAllCourse()
    {
        return allCourse;
    }

    public void insert(Course course)
    {
        executorService.execute(()-> courseDao.insert(course));
    }

    public void update(Course course)
    {
        executorService.execute(()-> courseDao.update(course));
    }
}
