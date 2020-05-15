package com.wiates.quizapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.R;

public class AttemptView extends AppCompatActivity {

    TextView score;
    Button back;

    TextView test_name,test_id;
    ImageView backT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_view);

        test_id = findViewById(R.id.test_id);
        test_name = findViewById(R.id.test_name);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        TestListModel testListModel = (TestListModel) intent.getSerializableExtra("class");

        test_name.setText(testListModel.getQuiz_name());
        test_id.setText(testListModel.getQuiz_id());


        score = findViewById(R.id.score);
        back = findViewById(R.id.back);
        backT = findViewById(R.id.backT);

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


        Intent intent1 = getIntent();
        String correct = intent1.getStringExtra("correct");
        String wrong = intent1.getStringExtra("wrong");

        score.setText("You have scored \n"+correct+" out of "+(Integer.valueOf(correct)+Integer.valueOf(wrong)));



    }
}
