package com.wiates.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.wiates.quizapp.R;
import com.wiates.quizapp.SplashScreen;

public class HomeScreenAdmin extends AppCompatActivity {

    CardView new_test,add_qs,logout,view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_admin);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        new_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewTestCreating.class);
                startActivity(intent);
            }
        });

        add_qs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ListTestsActivity.class);
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAttempts.class);
                startActivity(intent);
            }
        });

    }

    private void initialize(){
        new_test = findViewById(R.id.new_test);
        add_qs = findViewById(R.id.add_qs);
        logout = findViewById(R.id.logout);
        view = findViewById(R.id.view);
    }
}
