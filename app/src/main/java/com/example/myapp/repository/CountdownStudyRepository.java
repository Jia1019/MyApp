package com.example.myapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapp.database.CountdownStudyDao;
import com.example.myapp.database.Database;
import com.example.myapp.model.CountdownStudy;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountdownStudyRepository {
    private CountdownStudyDao countdownStudyDao;
    private static CountdownStudyRepository instance;
    private LiveData<List<CountdownStudy>> allStudy;
    private final ExecutorService executorService;

    public CountdownStudyRepository(Application application)
    {
        Database database = Database.getInstance(application);
        countdownStudyDao = database.countdownStudyDao();
        allStudy = countdownStudyDao.getAllStudy();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized CountdownStudyRepository getInstance(Application application)
    {
        if (instance==null)
        {
            instance = new CountdownStudyRepository(application);
        }
        return instance;
    }

    public LiveData<List<CountdownStudy>> getAllStudy()
    {
        return allStudy;
    }

    public void insert(CountdownStudy countdownStudy)
    {
        executorService.execute(()-> countdownStudyDao.insert(countdownStudy));
        //new InsertStudyAsync(countdownStudyDao).doInBackground(countdownStudy);
    }


      //private static class InsertStudyAsync extends AsyncTask<CountdownStudy,Void,Void> {
      //  private CountdownStudyDao countdownStudyDao;
//
      //private InsertStudyAsync(CountdownStudyDao countdownStudyDao)
      //{
      //    this.countdownStudyDao = countdownStudyDao;
      //}
//
      //@Override
      //protected Void doInBackground(CountdownStudy... studies)
      //{
      //    countdownStudyDao.insert(studies[studies.length]);
      //    return null;
      //}  }
}
