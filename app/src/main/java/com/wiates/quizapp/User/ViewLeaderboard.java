package com.wiates.quizapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiates.quizapp.R;

public class ViewLeaderboard extends AppCompatActivity {

    TextView score;

    ImageView backT;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leaderboard);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        String arr[] = result.split(" ");

        score = findViewById(R.id.score);
        back = findViewById(R.id.back);
        backT = findViewById(R.id.backT);

        score.setText("You are in \nRank "+arr[0]+"\namong\n"+arr[1]+" Participants!");


        backT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
