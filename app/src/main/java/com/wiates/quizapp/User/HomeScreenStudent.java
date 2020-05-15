package com.wiates.quizapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Models.UserModel;
import com.wiates.quizapp.R;
import com.wiates.quizapp.SplashScreen;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomeScreenStudent extends AppCompatActivity {

    CardView attempt,attempts,logout,leaderboard;
    TextView intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_student);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();


        SharedPreferences userSP = getSharedPreferences("user_loggedin",MODE_PRIVATE);
        String user = userSP.getString("user_loggedin","NOID");

        if(!user.matches("NOID")){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<UserModel>>() {}.getType();
            ArrayList<UserModel> userModels = gson.fromJson(user, type);

            intro.setText("Hey\n"+userModels.get(0).getName()+"!");

        }

        attempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ListTestsActivity.class);
                startActivity(intent);
            }
        });

        attempts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAttempts.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_loggedin", 0);
                preferences.edit().remove("user_loggedin").commit();
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LeaderboardTestList.class);
                startActivity(intent);
            }
        });
    }

    private void initialize(){
        leaderboard = findViewById(R.id.leaderboard);
        attempt = findViewById(R.id.attempt);
        attempts = findViewById(R.id.attempts);
        logout = findViewById(R.id.logout);

        intro = findViewById(R.id.intro);
    }
}
