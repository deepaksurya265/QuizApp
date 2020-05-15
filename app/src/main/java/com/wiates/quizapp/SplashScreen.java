package com.wiates.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Admin.HomeScreenAdmin;
import com.wiates.quizapp.Models.UserModel;
import com.wiates.quizapp.User.HomeScreenStudent;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                SharedPreferences userSP = getSharedPreferences("user_loggedin",MODE_PRIVATE);
                String user = userSP.getString("user_loggedin","NOID");



                if(user.matches("NOID")){
                    Intent intent = new Intent(getApplicationContext(),LoginScreen.class);
                    startActivity(intent);
                    finish();
                }
                else{

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<UserModel>>() {}.getType();
                    ArrayList<UserModel> userModels = gson.fromJson(user, type);

                    if(userModels.get(0).getUser_role().matches("student")){
                        Intent intent = new Intent(getApplicationContext(), HomeScreenStudent.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(userModels.get(0).getUser_role().matches("admin")){
                        Intent intent = new Intent(getApplicationContext(), HomeScreenAdmin.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

        }, 3000);
    }
}
