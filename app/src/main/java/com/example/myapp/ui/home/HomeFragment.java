package com.example.myapp.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.AppActivity;
import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.model.Background;
import com.example.myapp.model.CountdownStudy;
import com.example.myapp.model.Course;
import com.example.myapp.ui.setStudy.FinishStudyActivity;
import com.example.myapp.ui.store.BackgroundItemAdapter;
import com.example.myapp.ui.todoList.TodoItemAdapter;
import com.example.myapp.ui.todoList.TodoListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements CourseItemAdapter.OnListItemClickListener{

    private HomeViewModel homeViewModel;
    private CoordinatorLayout homeFrag;
    private FloatingActionButton studyFab;
    private FloatingActionButton todoFab;
    private FloatingActionButton logoutFab;
    private TextView coinNum;
    private TextView username;
    RecyclerView recyclerView;
    CourseItemAdapter adapter;
    private EditText editText;
    private Course course;
    private TextView currentClickColortv;

    //count

    private Chronometer timer;
    private boolean isClickEnd = false;

    private int studyMin = 0;
    private int rewardCoins = 0;

    private Timestamp startTime;
    private Timestamp endTime;


    private ArrayList<Course> courseList;


    @SuppressLint("FragmentLiveDataObserve")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeFrag = root.findViewById(R.id.home_frag);
        studyFab = root.findViewById(R.id.startStudy);
        todoFab = root.findViewById(R.id.makeTodo);
        coinNum = root.findViewById(R.id.coinNum);
        username = root.findViewById(R.id.username);
        logoutFab = root.findViewById(R.id.logout);

        logoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(getContext());
                alterDiaglog.setIcon(R.drawable.ic_baseline_logout_24);//图标
                alterDiaglog.setTitle("LOG OUT");//文字
                alterDiaglog.setMessage("Are you sure?");//提示消息
                //积极的选择
                alterDiaglog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                        getActivity().finish();
                    }
                });
                //消极的选择
                alterDiaglog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alterDiaglog.show().dismiss();
                    }
                });
                //显示
                alterDiaglog.show();


            }
        });

        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            coinNum.setText(String.valueOf(homeViewModel.getCurrentUserCoin()));
            username.setText(homeViewModel.getCurrentUser().getValue().getUsername());
        });

        homeViewModel.getCurrentBackground().observe(getViewLifecycleOwner(), background -> {
            if (background!=null)
            {
                Drawable drawable = ContextCompat.getDrawable(homeFrag.getContext(),background.getUrl());
                homeFrag.setBackground(drawable);
            }


        });

        RelativeLayout startstudy = root.findViewById(R.id.startstudy);
        RelativeLayout studyContinue = root.findViewById(R.id.studycontinue);
        startstudy.setVisibility(View.INVISIBLE);
        studyContinue.setVisibility(View.INVISIBLE);
        editText = (EditText) root.findViewById(R.id.courseName);
        Button button = root.findViewById(R.id.startstudybtn);
        currentClickColortv = root.findViewById(R.id.currentClickColortv);

        recyclerView = root.findViewById(R.id.courseColorRv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.hasFixedSize();
        courseList = new ArrayList<>();
        adapter = new CourseItemAdapter(courseList,this);
        recyclerView.setAdapter(adapter);

        homeViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            courseList.clear();
            courseList.addAll(courses);
        });

        studyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startstudy.setVisibility(View.VISIBLE);
            }
        });


        if (AppActivity.rewardcoin!=null&&AppActivity.studymin!=null&&!AppActivity.rewardcoin.equals("")&&!AppActivity.studymin.equals(""))
        {
            Intent intent1 = new Intent(getActivity(), FinishStudyActivity.class);
            intent1.putExtra("studyMinutes", AppActivity.studymin);
            intent1.putExtra("rewardCoin", AppActivity.rewardcoin);
            startActivity(intent1);
            AppActivity.studymin ="";
            AppActivity.rewardcoin="";
        }

        todoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TodoListActivity.class);
                startActivity(intent);
            }
        });

        TextView currentColortv = root.findViewById(R.id.currentColortv);
        timer = root.findViewById(R.id.timer);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText()==null||editText.getText().toString().equals("")||course==null)
                {
                    Toast.makeText(getContext(),"PLEASE CHOOSE THE COURSE AND WRITE THE NAME",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    course.setCourseName(editText.getText().toString());
                    homeViewModel.updateCourse(course);
                    currentColortv.setBackgroundColor(Color.parseColor(course.getCourseColor()));
                    startstudy.setVisibility(View.INVISIBLE);
                    studyContinue.setVisibility(View.VISIBLE);
                    timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                    int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
                    timer.setFormat("0"+String.valueOf(hour)+":%s");
                    timer.start();
                    startTime = new Timestamp(System.currentTimeMillis());

                }
            }
        });

        Button endStudybtn = root.findViewById(R.id.endStudyBtn);


        endStudybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stop();
                String string = timer.getText().toString();
                    String[] split = string.split(":");
                    String string2 = split[0];
                    int hour = Integer.parseInt(string2);
                    int Hours =hour*60;
                    String string3 = split[1];
                    int min = Integer.parseInt(string3);
                    int Mins =min;
                    int  SS =Integer.parseInt(split[2]);
                    studyMin = Hours+Mins;

                rewardCoins = studyMin%10;

                endTime = new Timestamp(System.currentTimeMillis());
                homeViewModel.insertStudy(new CountdownStudy(course.getCourseColor(),startTime,endTime,rewardCoins));

                studyContinue.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(getActivity(), FinishStudyActivity.class);
                String min1 = String.valueOf(studyMin);
                String coin1 = String.valueOf(rewardCoins);
                intent1.putExtra("studyMinutes", min1);
                intent1.putExtra("rewardCoin", coin1);
                startActivity(intent1);
            }
        });




        return root;
    }




    @Override
    public void clickTextView(int position) {
        course = courseList.get(position);
        currentClickColortv.setBackgroundColor(Color.parseColor(course.getCourseColor()));
        editText.setText(course.getCourseName());

    }

    @Override
    public void onPause() {
        super.onPause();
        if (endTime==null&&course!=null&&startTime!=null)
        {
            timer.stop();
            String string = timer.getText().toString();
            String[] split = string.split(":");
            String string2 = split[0];
            int hour = Integer.parseInt(string2);
            int Hours =hour*60;
            String string3 = split[1];
            int min = Integer.parseInt(string3);
            int Mins =min;
            int  SS =Integer.parseInt(split[2]);
            studyMin = Hours+Mins;

            rewardCoins = studyMin%10;

            endTime = new Timestamp(System.currentTimeMillis());
            homeViewModel.insertStudy(new CountdownStudy(course.getCourseColor(),startTime,endTime,rewardCoins));

            AppActivity.studymin = String.valueOf(studyMin);
            AppActivity.rewardcoin = String.valueOf(rewardCoins);
        }

    }

    private void signOut()
    {
        FirebaseAuth.getInstance().signOut();
    }
}