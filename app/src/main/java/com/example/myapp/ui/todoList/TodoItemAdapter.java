package com.example.myapp.ui.todoList;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.model.Todo;

import java.util.ArrayList;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder> {
   private ArrayList<Todo> todoList;
    final private OnListItemClickListener listener;

    public TodoItemAdapter(ArrayList<Todo> todoList, OnListItemClickListener listener)
    {
        this.todoList = todoList;
        this.listener = listener;

    }

    public class TodoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView todoContent;
        public CheckBox checkBox;
        public ImageButton deleteBtn;
        public ImageButton editBtn;

        public TodoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            todoContent =(TextView) itemView.findViewById(R.id.todoContent);
            checkBox =(CheckBox) itemView.findViewById(R.id.checktodoBox);
            deleteBtn =(ImageButton) itemView.findViewById(R.id.deletetodoBtn);
            editBtn = (ImageButton) itemView.findViewById(R.id.editTodoBtn);
        }

        @Override
        public void onClick(View v) {
            listener.clickCheckbox(getAdapterPosition());
            listener.deleteTodo(getAdapterPosition());
            listener.editTodo(getAdapterPosition());

        }
    }

    @NonNull
    @Override
    public TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        TodoItemAdapter.TodoItemViewHolder viewHolder = new TodoItemAdapter.TodoItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder holder, int position) {
        Todo todo = todoList.get(position);

        holder.todoContent.setText(todo.getContext());
        holder.checkBox.setChecked(todo.isDone());
        if (todo.isDone())
        {
            holder.todoContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    holder.todoContent.getPaint().setFlags(0);
                }
                else
                {
                     holder.todoContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                listener.clickCheckbox(position);
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editTodo(position);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteTodo(position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public interface OnListItemClickListener {
        void clickCheckbox(int position);
        void deleteTodo(int position);
        void editTodo(int position);
    }
}
