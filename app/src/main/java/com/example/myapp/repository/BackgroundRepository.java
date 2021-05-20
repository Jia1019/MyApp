package com.example.myapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapp.R;
import com.example.myapp.database.BackgroundDao;
import com.example.myapp.database.Database;
import com.example.myapp.model.Background;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundRepository {
    private BackgroundDao backgroundDao;
    private static BackgroundRepository instance;
    private LiveData<List<Background>> allBackground;
    private LiveData<Background> currentBackground;
    private final ExecutorService executorService;

    public BackgroundRepository(Application application)
    {
        Database database = Database.getInstance(application);
        backgroundDao = database.backgroundDao();
        allBackground = backgroundDao.getAllBackgrounds();
        currentBackground = backgroundDao.getCurrentBackground(true);
        executorService = Executors.newFixedThreadPool(2);
    }

    public static synchronized BackgroundRepository getInstance(Application application)
    {
        if (instance==null)
        {
            instance = new BackgroundRepository(application);
        }
        return instance;
    }

    public LiveData<List<Background>> getAllBackground()
    {
        return allBackground;
    }

    public void insert(Background background)
    {
        executorService.execute(()->backgroundDao.insert(background));
    }

    public void update(Background background)
    {
        executorService.execute(()->backgroundDao.update(background));
    }

    public LiveData<Background> getCurrentBackground()
    {
        return currentBackground;
    }
}
