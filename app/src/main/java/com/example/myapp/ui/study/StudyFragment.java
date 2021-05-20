package com.example.myapp.ui.study;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.model.CountdownStudy;
import com.example.myapp.model.Course;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.datepicker.MaterialTextInputPicker;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class StudyFragment extends Fragment implements StudyHistoryAdapter.OnListItemClickListener{

    private StudyViewModel studyViewModel;
    private MaterialDatePicker picker;
    private TextView currentDate;
    //private ColumnChartView columnChart;
    private PieChartView pieChartView;
    private ArrayList<CountdownStudy> countdownStudies;
    private ArrayList<CountdownStudy> allCountdownStudies;
    private ArrayList<Course> courses;
    RecyclerView recyclerView;
   StudyHistoryAdapter adapter;
    PieChartData mPieChartData;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        studyViewModel =
                new ViewModelProvider(this).get(StudyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_study, container, false);
        currentDate = root.findViewById(R.id.currentDate);
        ImageButton showDatePiker = root.findViewById(R.id.showDatePikerBtn);
        courses = new ArrayList<>();
        countdownStudies = new ArrayList<>();
        allCountdownStudies =new ArrayList<>();

        pieChartView = root.findViewById(R.id.pieChart);



        recyclerView = root.findViewById(R.id.historyRv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudyHistoryAdapter(countdownStudies,courses,this);
        recyclerView.setAdapter(adapter);

        Date date=new Date();//此时date为当前的时间
        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");//设置当前时间的格式，为年-月-日
        currentDate.setText(dateFormat.format(date));

        picker=MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.Theme_App)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("SELECT DATE")
                .build();


        showDatePiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getActivity().getSupportFragmentManager(),"MATERIAL_DATE_PICKER");

            }
        });


        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                Date date = new Date(selectedDate);
                currentDate.setText(dateFormat.format(date));
            }
        });



        studyViewModel.getAllCourses().observe(getViewLifecycleOwner(),courses1 -> {
            courses.clear();
            courses.addAll(courses1);
            adapter.notifyDataSetChanged();
        });



        studyViewModel.getAllCountdownStudy().observe(getViewLifecycleOwner(),countdownStudies1 -> {
            allCountdownStudies.clear();
            allCountdownStudies.addAll(countdownStudies1);
            countdownStudies.clear();
            for (int i = 0;i<countdownStudies1.size();i++)
            {
                if (toDate(countdownStudies1.get(i).getStartDate()).equals(currentDate.getText().toString()))
                {
                    countdownStudies.add(countdownStudies1.get(i));
                }
            }
            adapter.notifyDataSetChanged();
            pieChartView.setPieChartData(new PieChartData());
            setPieDatas();
        });

        currentDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                countdownStudies.clear();
                for (int i = 0;i<allCountdownStudies.size();i++)
                {
                    if (toDate(allCountdownStudies.get(i).getStartDate()).equals(currentDate.getText().toString()))
                    {
                        countdownStudies.add(allCountdownStudies.get(i));
                    }
                }
                adapter.notifyDataSetChanged();

                pieChartView.setPieChartData(new PieChartData());
                setPieDatas();
            }
        });



        //columnChart = (ColumnChartView) root.findViewById(R.id.columnChart);
        //generateDefaultData();


        return root;
    }


    public void setPieDatas()
    {
        ArrayList<Course> studyCourses = new ArrayList<>();
        ArrayList<Long> studyTime = new ArrayList<>();
        long totalSec=0;
        for (int n = 0; n < courses.size(); n++) {
                for (int i = 0; i < countdownStudies.size(); i++) {
                    long sec=0;
                    if (countdownStudies.get(i).getCourseColor().equals(courses.get(n).getCourseColor()))
                    {
                        sec = (countdownStudies.get(i).getEndDate().getTime()-countdownStudies.get(i).getStartDate().getTime())/1000;
                        totalSec+=sec;
                    }
                }
                if (totalSec!=0)
                {
                    studyCourses.add(courses.get(n));
                    studyTime.add(totalSec);
                }
            totalSec = 0;
        }
        int numValues = studyCourses.size();                //把一张饼切成块
        List<SliceValue> values = new ArrayList<>();


        for (int i = 0; i < numValues; i++) {
            //设置每一块的大小和颜色
           // long sec = (countdownStudies.get(i).getEndDate().getTime()-countdownStudies.get(i).getStartDate().getTime())/1000;
           // SliceValue sliceValue = new SliceValue(sec, Color.parseColor(countdownStudies.get(i).getCourseColor()));
           // for (int n =0;n<courses.size();n++)
           // {
           //     if (courses.get(n).getCourseColor().equals(countdownStudies.get(i).getCourseColor()))
           //     {
           //         sliceValue.setLabel(courses.get(n).getCourseName());
           //     }
           // }
            SliceValue sliceValue = new SliceValue(studyTime.get(i), Color.parseColor(studyCourses.get(i).getCourseColor()));
            sliceValue.setLabel(studyCourses.get(i).getCourseName());

            values.add(sliceValue);
        }

        //SliceValue sliceValue = new SliceValue (1-(24*60*60-totalSec),Color.parseColor("#DCDCDC"));
        //values.add(sliceValue);

        mPieChartData = new PieChartData(values);
        mPieChartData.setHasLabels(true);
        mPieChartData.setHasCenterCircle(true); // 是否有中心圆环
        mPieChartData.setCenterCircleScale(0.4f);//设置中心环的大小
        pieChartView.setPieChartData(mPieChartData);
    }





   //private void generateDefaultData(){
   //    //定义有多少个柱子
   //    int numColumns = 1;
   //    //定义表格实现类
   //    ColumnChartData columnChartData;
   //    //Column 是下图中柱子的实现类
   //    List<Column> columns =new ArrayList<>();
   //    //SubcolumnValue 是下图中柱子中的小柱子的实现类，下面会解释我说的是什么
   //    List<SubcolumnValue> values=new ArrayList<>();
   //    //循环初始化每根柱子，
   //    for(int i=0;i<numColumns;i++){
   //        //每一根柱子中只有一根小柱子
   //        for (int n = 0; i<countdownStudies.size();i++)
   //        {
   //            if (toDate(countdownStudies.get(i).getStartDate()).equals(currentDate.getText().toString()))
   //            values.add(new SubcolumnValue( countdownStudies.get(i).getEndDate().getTime()-countdownStudies.get(i).getStartDate().getTime(), Color.parseColor(countdownStudies.get(i).getCourseColor())));
   //        }

   //        //初始化Column
   //        Column column = new Column(values);
   //        column.setHasLabels(true);
   //        columns.add(column);
   //     }
   //    //给表格添加写好数据的柱子
   //    columnChartData = new ColumnChartData(columns);

   //        Axis axisBootom = new Axis();
   //        Axis axisLeft = new Axis();

   //        List<AxisValue> axisValuess=new ArrayList<>();
   //        for(int j=0;j<numColumns;j++){
   //            axisValuess.add(new AxisValue(j).setLabel(currentDate.getText().toString()));
   //        }

   //    List<AxisValue> axisLeftValuess=new ArrayList<>();
   //    axisLeftValuess.add(new AxisValue(0).setLabel("0:00"));
   //    axisLeftValuess.add(new AxisValue(1).setLabel("1:00"));
   //    axisLeftValuess.add(new AxisValue(2).setLabel("2:00"));
   //    axisLeftValuess.add(new AxisValue(3).setLabel("3:00"));
   //    axisLeftValuess.add(new AxisValue(4).setLabel("4:00"));
   //    axisLeftValuess.add(new AxisValue(5).setLabel("5:00"));
   //    axisLeftValuess.add(new AxisValue(6).setLabel("6:00"));
   //    axisLeftValuess.add(new AxisValue(7).setLabel("7:00"));
   //    axisLeftValuess.add(new AxisValue(8).setLabel("8:00"));
   //    axisLeftValuess.add(new AxisValue(9).setLabel("9:00"));
   //    axisLeftValuess.add(new AxisValue(10).setLabel("10:00"));
   //    axisLeftValuess.add(new AxisValue(11).setLabel("11:00"));
   //    axisLeftValuess.add(new AxisValue(12).setLabel("12:00"));
   //    axisLeftValuess.add(new AxisValue(13).setLabel("13:00"));
   //    axisLeftValuess.add(new AxisValue(14).setLabel("14:00"));
   //    axisLeftValuess.add(new AxisValue(15).setLabel("15:00"));
   //    axisLeftValuess.add(new AxisValue(16).setLabel("16:00"));
   //    axisLeftValuess.add(new AxisValue(17).setLabel("17:00"));
   //    axisLeftValuess.add(new AxisValue(18).setLabel("18:00"));
   //    axisLeftValuess.add(new AxisValue(19).setLabel("19:00"));
   //    axisLeftValuess.add(new AxisValue(20).setLabel("20:00"));
   //    axisLeftValuess.add(new AxisValue(21).setLabel("21:00"));
   //    axisLeftValuess.add(new AxisValue(22).setLabel("22:00"));
   //    axisLeftValuess.add(new AxisValue(23).setLabel("23:00"));
   //    axisLeftValuess.add(new AxisValue(24).setLabel("24:00"));

   //        axisBootom.setValues(axisValuess);
   //        axisLeft.setValues(axisLeftValuess);
   //        axisBootom.setName("Date");
   //        axisLeft.setName("Time");


   //        columnChartData.setAxisXBottom(axisBootom);
   //        columnChartData.setAxisYLeft(axisLeft);
   //        columnChartData.setStacked(true);
   //        columnChart.setColumnChartData(columnChartData);

   //    //给画表格的View添加要画的表格
   //    columnChart.setColumnChartData(columnChartData);

   //}

    private String toDate(Timestamp timestamp)
    {
        return new SimpleDateFormat("YYYY-MM-dd").format(timestamp);
    }

    private String toTime(Timestamp timestamp)
    {
        return new SimpleDateFormat("HH:mm:ss").format(timestamp);
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}