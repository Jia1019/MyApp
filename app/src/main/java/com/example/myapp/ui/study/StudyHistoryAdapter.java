package com.example.myapp.ui.study;

import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.model.Background;
import com.example.myapp.model.CountdownStudy;
import com.example.myapp.model.Course;
import com.example.myapp.ui.store.BackgroundItemAdapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class StudyHistoryAdapter extends RecyclerView.Adapter<StudyHistoryAdapter.ViewHolder> {
    private ArrayList<CountdownStudy> countdownStudyList;
    private ArrayList<Course> courseList;
    final private StudyHistoryAdapter.OnListItemClickListener listener;

    public StudyHistoryAdapter(ArrayList<CountdownStudy> countdownStudyList,ArrayList<Course> courseList,StudyHistoryAdapter.OnListItemClickListener listener) {
        this.countdownStudyList = countdownStudyList;
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudyHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.studyhistory, parent, false);
        StudyHistoryAdapter.ViewHolder viewHolder = new StudyHistoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudyHistoryAdapter.ViewHolder holder, int position) {

        CountdownStudy countdownStudy = countdownStudyList.get(position);

        if (countdownStudy!=null)
        {
            holder.studyColorBtn.setBackgroundColor(Color.parseColor(countdownStudy.getCourseColor()));
            for (Course course:courseList) {
                if (course.getCourseColor().equals(countdownStudy.getCourseColor()))
                {
                    String item = toDate(countdownStudy.getStartDate())+" "+toTime(countdownStudy.getStartDate())+" - "+toTime(countdownStudy.getEndDate());
                    holder.studyItem.setText(item);
                }
            }

        }



    }

    @Override
    public int getItemCount() {
       if (countdownStudyList!=null)
       {
           return countdownStudyList.size();
       }
       return 0;
        //return countdownStudyList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView studyItem;
        public Button studyColorBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studyItem = itemView.findViewById(R.id.studyItem);
            studyColorBtn = itemView.findViewById(R.id.studyColorBtn);
        }

        @Override
        public void onClick(View v) {
            listener.onListItemClick(getAdapterPosition());

        }

    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    private String toDate(Timestamp timestamp)
    {
        return new SimpleDateFormat("YYYY-MM-dd").format(timestamp);
    }

    private String toTime(Timestamp timestamp)
    {
        return new SimpleDateFormat("HH:mm:ss").format(timestamp);
    }


}
