package com.wiates.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Admin.HomeScreenAdmin;
import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.Models.UserModel;
import com.wiates.quizapp.User.HomeScreenStudent;

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
import java.util.ArrayList;

public class LoginScreen extends AppCompatActivity {

    EditText email,password;
    Button signup,signin;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initialize();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String sql = "";




                if(TextUtils.isEmpty(sEmail)){
                    Toast.makeText(getApplicationContext(),"Please enter your Email ID",Toast.LENGTH_LONG).show();
                }
                else if(!sEmail.matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),"Please enter a valid Email ID",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sPassword)){
                    Toast.makeText(getApplicationContext(),"Please enter a Password (Min: 6 Charecters)",Toast.LENGTH_LONG).show();
                }
                else{
                    sql = "SELECT * FROM "+ Constants.user_table_name+" where username = '"+sEmail+"' and password = '"+sPassword+"'";
                    new SignIn().execute(sql);
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupScreen.class);
                startActivity(intent);
            }
        });



    }

    private void initialize(){

        progressDialog = new ProgressDialog(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);

    }


    @SuppressLint("StaticFieldLeak")
    public class SignIn extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Please wait!");
            progressDialog.setMessage("Please wait while we are creating your Account!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String ... params) {

            String sql = params[0];

            try {
                URL url = new URL(Constants.location+"signin.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("sql","UTF-8") +"="+URLEncoder.encode(sql,"UTF-8")+"&"+
                        URLEncoder.encode("connection_string","UTF-8") +"="+URLEncoder.encode(Constants.connection_string,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line;
                while ((line=bufferedReader.readLine())!=null)
                    response+=line;
                bufferedReader.close();
                inputStream.close();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "a";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Log.d("tag",result);
            if(result!=null){
                if (result.matches("OK")){
                    Toast.makeText(getApplicationContext(),"Your account is created successfully! Please Signin into your Account to Continue!",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{

                    Log.d("tt",result.substring(1,result.length()-1));
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<UserModel>>() {}.getType();
                    ArrayList<UserModel> userModels = gson.fromJson(result.substring(1,result.length()-1), type);

                    SharedPreferences sharedPreferences = getSharedPreferences("user_loggedin",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson1 = new Gson();

                    String json1 = gson1.toJson(userModels);
                    editor.putString("user_loggedin",json1);
                    editor.apply();

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
            else{
                Toast.makeText(getApplicationContext(),"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
