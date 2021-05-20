package com.example.myapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapp.database.Database;
import com.example.myapp.database.TodoDao;
import com.example.myapp.model.Todo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoRepository {
    private TodoDao todoDao;
    private static TodoRepository instance;
    private LiveData<List<Todo>> allTodo;
    private final ExecutorService executorService;

    public TodoRepository(Application application)
    {
        Database database = Database.getInstance(application);
        todoDao = database.todoDao();
        allTodo = todoDao.getAllNotes();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized TodoRepository getInstance(Application application)
    {
        if (instance==null)
        {
            instance = new TodoRepository(application);
        }
        return instance;
    }

    public LiveData<List<Todo>> getAllTodo()
    {
        return allTodo;
    }

    public void insert(Todo todo)
    {
        executorService.execute(()-> todoDao.insert(todo));

    }

    public void update(Todo todo)
    {
        executorService.execute(()->todoDao.update(todo));
    }

    public void delete(Todo todo)
    {
        executorService.execute(()->todoDao.delete(todo));
    }

    
}
