package com.example.myapp.ui.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapp.model.Background;
import com.example.myapp.model.Course;
import com.example.myapp.model.User;
import com.example.myapp.repository.BackgroundRepository;
import com.example.myapp.repository.CourseRepository;
import com.example.myapp.repository.UserRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private BackgroundRepository backgroundRepository;
    private CourseRepository courseRepository;

    public LoginViewModel(Application application)
    {
        super(application);
        userRepository = UserRepository.getInstance(application);
        backgroundRepository = BackgroundRepository.getInstance(application);
        courseRepository = CourseRepository.getInstance(application);
    }

    public void insert(User user)
    {
        userRepository.insert(user);
    }

    public LiveData<List<User>> getUsers()
    {
        return userRepository.getAllUsers();
    }

    public LiveData<List<Course>> getCoourses()
    {
        return courseRepository.getAllCourse();
    }

    public void insert(Background background)
    {
        backgroundRepository.insert(background);
    }

    public void insertCourse(String color)
    {
        courseRepository.insert(new Course(color,""));
    }


    public LiveData<List<Background>> getAllBackgrounds()
    {
        return backgroundRepository.getAllBackground();
    }



}
