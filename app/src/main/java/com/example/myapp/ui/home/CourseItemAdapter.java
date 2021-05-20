package com.example.myapp.ui.home;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.model.Course;
import com.example.myapp.model.Todo;
import com.example.myapp.ui.todoList.TodoItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.CourseItemViewHolder>{
    private ArrayList<Course> courseList;
    final private CourseItemAdapter.OnListItemClickListener listener;

    public CourseItemAdapter(ArrayList<Course> courseList, CourseItemAdapter.OnListItemClickListener listener)
    {
        this.courseList = courseList;
        this.listener = listener;

    }

    public class CourseItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button courseColorbtn;

        public CourseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            courseColorbtn =(Button) itemView.findViewById(R.id.courseColorBtn);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public CourseItemAdapter.CourseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.courselist, parent, false);
        CourseItemAdapter.CourseItemViewHolder viewHolder = new CourseItemAdapter.CourseItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseItemAdapter.CourseItemViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.courseColorbtn.setBackgroundColor(Color.parseColor(course.getCourseColor()));

        holder.courseColorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickTextView(position);

            }
        });


    }



    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public interface OnListItemClickListener {
        void clickTextView(int position);
    }
}
