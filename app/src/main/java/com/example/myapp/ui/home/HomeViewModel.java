package com.example.myapp.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapp.model.Background;
import com.example.myapp.model.CountdownStudy;
import com.example.myapp.model.Course;
import com.example.myapp.model.User;
import com.example.myapp.repository.BackgroundRepository;
import com.example.myapp.repository.CountdownStudyRepository;
import com.example.myapp.repository.CourseRepository;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.ui.login.LoginViewModel;

import java.net.PortUnreachableException;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private BackgroundRepository backgroundRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private CountdownStudyRepository countdownStudyRepository;

    public HomeViewModel(Application application) {
        super(application);
        backgroundRepository = BackgroundRepository.getInstance(application);
        userRepository = UserRepository.getInstance(application);
        courseRepository = CourseRepository.getInstance(application);
        countdownStudyRepository = CountdownStudyRepository.getInstance(application);
    }

    public LiveData<Background> getCurrentBackground()
    {
        return backgroundRepository.getCurrentBackground();
    }

    public LiveData<List<User>> getAllUsers()
    {
        return userRepository.getAllUsers();
    }

    public LiveData<User> getCurrentUser()
    {
        return userRepository.getCurrentUser();
    }

    public int getCurrentUserCoin()
    {
        if (userRepository.getCurrentUser().getValue()!=null)
        {
            return userRepository.getCurrentUser().getValue().getCoinNum();

        }
        return 0;

    }

    public void updateCoin(int coinNum)
    {
        getCurrentUser().getValue().setCoinNum(coinNum);
        userRepository.update(getCurrentUser().getValue());
    }

    public LiveData<List<Course>> getAllCourses()
    {
        return courseRepository.getAllCourse();
    }

    public Course getCourseByColor(String color)
    {
        for (int i = 0 ;i<getAllCourses().getValue().size(); i++)
        {
            if (getAllCourses().getValue().get(i).getCourseColor().equals(color))
            {
                return getAllCourses().getValue().get(i);
            }
        }

        return null;
    }

    public void updateCourse(Course course)
    {
        courseRepository.update(course);
    }

    public void insertStudy(CountdownStudy countdownStudy)
    {
        countdownStudyRepository.insert(countdownStudy);
        getCurrentUser().getValue().setCoinNum(getCurrentUserCoin()+countdownStudy.getOwnedCoinNum());
        userRepository.update(getCurrentUser().getValue());
    }


}