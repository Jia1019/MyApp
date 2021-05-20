package com.example.myapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapp.database.Database;
import com.example.myapp.database.UserDao;
import com.example.myapp.model.Course;
import com.example.myapp.model.User;
import com.example.myapp.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private static UserRepository instance;
    private LiveData<List<User>> allUsers;
    private final ExecutorService executorService;
    private LiveData<User> currentUser;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();


    public UserRepository(Application application)
    {
        Database database = Database.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
        if (user!=null)
        {
            System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWTHE USER");
            currentUser = userDao.getCurrentUser(user.getEmail());
        }
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized UserRepository getInstance(Application application)
    {
        if (instance==null)
        {
            instance = new UserRepository(application);
        }
        return instance;
    }


    public LiveData<List<User>> getAllUsers()
    {
        return allUsers;
    }


    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void insert(User user)
    {
        executorService.execute(()-> userDao.insert(user));
    }

    public void update(User user)
    {
        executorService.execute(()->userDao.update(user));
    }

}
