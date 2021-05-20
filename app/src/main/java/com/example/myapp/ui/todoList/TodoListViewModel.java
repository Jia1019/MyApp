package com.example.myapp.ui.todoList;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapp.model.Todo;
import com.example.myapp.repository.TodoRepository;

import java.util.List;

public class TodoListViewModel extends AndroidViewModel {
    private TodoRepository todoRepository;

    public TodoListViewModel(Application application)
    {
        super(application);
        todoRepository = TodoRepository.getInstance(application);
    }

    public LiveData<List<Todo>> getAllTodo()
    {
        return todoRepository.getAllTodo();
    }

    public void addNewTodo(String content)
    {
        todoRepository.insert(new Todo(content, false));
    }

    public void updateIfFinished(Todo todo)
    {
        if (todo.isDone())
        {
            todo.setDone(false);
            todoRepository.update(todo);
        }
        else
        {
            todo.setDone(true);
            todoRepository.update(todo);
        }
    }

    public void updateContent(Todo todo)
    {
        if (todo!=null)
        {
            todoRepository.update(todo);
        }
    }

    public void removeTodo(Todo todo)
    {
        todoRepository.delete(todo);
    }
}
