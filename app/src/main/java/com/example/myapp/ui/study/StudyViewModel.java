package com.example.myapp.ui.study;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapp.model.CountdownStudy;
import com.example.myapp.model.Course;
import com.example.myapp.repository.CountdownStudyRepository;
import com.example.myapp.repository.CourseRepository;

import java.util.List;

public class StudyViewModel extends AndroidViewModel {
    private CountdownStudyRepository countdownStudyRepository;
    private CourseRepository courseRepository;


    public StudyViewModel(@NonNull Application application) {
        super(application);
        countdownStudyRepository = CountdownStudyRepository.getInstance(application);
        courseRepository = CourseRepository.getInstance(application);
    }

    public LiveData<List<CountdownStudy>> getAllCountdownStudy()
    {
        return countdownStudyRepository.getAllStudy();
    }

    public LiveData<List<Course>> getAllCourses()
    {
        return courseRepository.getAllCourse();
    }
}