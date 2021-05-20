package com.example.myapp.ui.todoList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.model.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity implements TodoItemAdapter.OnListItemClickListener{
    private TodoListViewModel todoListViewModel;
    RecyclerView recyclerView;
    TodoItemAdapter adapter;
    private ArrayList<Todo> todoList;
    private EditText editText;
    private ScrollView scrollView;
    private RelativeLayout editLayout;
    private boolean isAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        todoListViewModel = new ViewModelProvider(this).get(TodoListViewModel.class);

        recyclerView = findViewById(R.id.todoListRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        todoList = new ArrayList<>();
        adapter = new TodoItemAdapter(todoList, this);
        recyclerView.setAdapter(adapter);
        Button addBtn = (Button) findViewById(R.id.addTodoBtn);
        FloatingActionButton backBtn = findViewById(R.id.backFromTodoFab);
        scrollView = findViewById(R.id.scrollviewTodo);
        editText = findViewById(R.id.editTodoContent);
        Button confirmBtn = findViewById(R.id.confirmTodoBtn);
        editLayout = findViewById(R.id.editLayout);
        editLayout.setVisibility(View.GONE);

        isAdded = true;



        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        params.height = outRect.bottom - outRect.top+50;

        todoListViewModel.getAllTodo().observe(this,todos -> {
            todoList.clear();
            todoList.addAll(todos);
            adapter.notifyDataSetChanged();
        } );



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdded = true;
                editLayout.setVisibility(View.VISIBLE);
                editText.callOnClick();

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText==null||editText.getText().toString().equals(""))
                {
                    Toast.makeText(TodoListActivity.this,"PLEASE WRITE THE TODO TASK", Toast.LENGTH_SHORT).show();
                }else {
                    if (isAdded)
                    {
                        Todo todo = new Todo(editText.getText().toString(),false);
                        todoListViewModel.addNewTodo(todo.getContext());
                        todoList.add(todo);
                        editText.setText("");
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        if (todoPosition>=0&&todoPosition<todoList.size())
                        {
                            Todo todo = todoList.get(todoPosition);
                            todo.setContext(editText.getText().toString());
                            todoListViewModel.updateContent(todo);
                            editText.setText("");
                            adapter.notifyDataSetChanged();
                        }
                    }
                    editLayout.setVisibility(View.GONE);
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void clickCheckbox(int position) {
        Todo todo = todoList.get(position);
        todoListViewModel.updateIfFinished(todo);
    }


    @Override
    public void deleteTodo(int position) {
        todoListViewModel.removeTodo(todoList.get(position));
        todoList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position,adapter.getItemCount());
        adapter.notifyDataSetChanged();

    }

    private int todoPosition;
    @Override
    public void editTodo(int position) {
        todoPosition = position;
        editText.setText(todoList.get(position).getContext());
        isAdded=false;
        editLayout.setVisibility(View.VISIBLE);
    }
}