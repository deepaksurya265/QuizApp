package com.wiates.quizapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.R;

public class SuccessfullySubmittedActivity extends AppCompatActivity {


    TextView test_name,test_id,status,score;
    Button home;
    ImageView backT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_submitted);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        Intent intent = getIntent();
        final TestListModel testListModel = (TestListModel) intent.getSerializableExtra("class");
        String correct = intent.getStringExtra("correct");
        String wrong = intent.getStringExtra("wrong");

        backT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        test_name.setText(testListModel.getQuiz_name());
        test_id.setText(testListModel.getQuiz_id());

        if(Integer.valueOf(correct)>0){
            status.setText("Congrats!");
            score.setText("Your Score is "+correct+" out of "+(Integer.valueOf(correct)+Integer.valueOf(wrong)));
        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),HomeScreenStudent.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });

    }

    private void initialize(){


        test_name = findViewById(R.id.test_name);
        test_id = findViewById(R.id.test_id);

        status = findViewById(R.id.status);
        score = findViewById(R.id.score);

        home = findViewById(R.id.home);
        backT = findViewById(R.id.backT);

    }
}
