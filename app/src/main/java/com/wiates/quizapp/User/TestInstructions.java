package com.wiates.quizapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Adapters.ListTestAdapterAttemptsAdmin;
import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.Models.AdminModel;
import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.Models.UserModel;
import com.wiates.quizapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestInstructions extends AppCompatActivity {

    TextView test_name,test_id,instructions;
    Button ok,notok;
    ProgressDialog progressDialog;
    ArrayList<UserModel> userModels;

    TestListModel testListModel = null;
    ImageView backT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_instructions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();


        final Intent intent = getIntent();
        testListModel = (TestListModel) intent.getSerializableExtra("class");

        test_name.setText(testListModel.getQuiz_name());
        test_id.setText(testListModel.getQuiz_id());


        SharedPreferences userSP = getSharedPreferences("user_loggedin",MODE_PRIVATE);
        String user = userSP.getString("user_loggedin","NOID");

        if(!user.matches("NOID")){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<UserModel>>() {}.getType();
            userModels = gson.fromJson(user, type);


        }

        backT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        instructions.setText("Your test is for "+testListModel.getTotal_time_for_quiz()+" minutes\n\nYour test is available from "+testListModel.getQuiz_start_timing()+" and ends at "+testListModel.getQuiz_end_timing()+"\n\nAll Questions are Multiple Choice Questions with no Negative Markings.\n\nYou cannot leave the test in between!");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ViewQuestions.class);
                intent1.putExtra("class",testListModel);
                startActivity(intent1);
                finish();
            }
        });

        notok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initialize(){

        backT = findViewById(R.id.backT);


        progressDialog = new ProgressDialog(this);
        test_id = findViewById(R.id.test_id);
        test_name = findViewById(R.id.test_name);
        instructions = findViewById(R.id.instructions);

        ok = findViewById(R.id.ok);
        notok = findViewById(R.id.notok);

    }




}
