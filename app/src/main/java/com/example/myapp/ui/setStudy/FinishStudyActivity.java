package com.example.myapp.ui.setStudy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FinishStudyActivity extends AppCompatActivity {
    private FinishStudyViewModel viewModel;
    private TextView studyMin;
    private TextView coinNum;
    private ImageButton toHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finishstudy);
        viewModel = new ViewModelProvider(this).get(FinishStudyViewModel.class);

        studyMin = findViewById(R.id.studyTimeTextView2);
        coinNum = findViewById(R.id.coinTextView);
        toHomeBtn = findViewById(R.id.toHomeBtn);

        Bundle bundle = getIntent().getExtras();
        String min = bundle.getString("studyMinutes")+" Study Mins";
        String coin = "GET "+bundle.getString("rewardCoin");
        studyMin.setText(min);
        coinNum.setText(coin);

        toHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
