package com.wiates.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wiates.quizapp.Constants.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SignupScreen extends AppCompatActivity {

    EditText email,name,mobileno,password,cpassword;
    Button signup;
    ProgressDialog progressDialog;
    ImageView backT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        backT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString();
                String sName = name.getText().toString();
                String sMobileNo = mobileno.getText().toString();
                String sPassword = password.getText().toString();
                String sCPassword = cpassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String sql = "";


                if(TextUtils.isEmpty(sEmail)){
                    Toast.makeText(getApplicationContext(),"Please enter your Email ID",Toast.LENGTH_LONG).show();
                }
                else if(!sEmail.matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),"Please enter a valid Email ID",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sName)){
                    Toast.makeText(getApplicationContext(),"Please enter your Full Name",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sMobileNo)){
                    Toast.makeText(getApplicationContext(),"Please enter your Mobile Number",Toast.LENGTH_LONG).show();
                }
                else if(!(sMobileNo.length() == 10)){
                    Toast.makeText(getApplicationContext(),"Please enter a valid Mobile Number",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sPassword)){
                    Toast.makeText(getApplicationContext(),"Please enter a Password (Min: 6 Charecters)",Toast.LENGTH_LONG).show();
                }
                else if(!(sPassword.length()>=6)){
                    Toast.makeText(getApplicationContext(),"Please enter a Password (Min: 6 Charecters)",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sCPassword)){
                    Toast.makeText(getApplicationContext(),"Please confirm your Password",Toast.LENGTH_LONG).show();
                }
                else if(!sPassword.equals(sCPassword)){
                    Toast.makeText(getApplicationContext(),"Please confirm your password correctly",Toast.LENGTH_LONG).show();
                }
                else{
                    sql = "INSERT INTO "+ Constants.user_table_name+" (username,password,mobile_number,user_role,name) VALUES ('"+sEmail+"','"+sPassword+"','"+sMobileNo+"','student','"+sName+"')";
                    new SignUp().execute(sql,Constants.connection_string);
                }
            }
        });
    }

    private void initialize(){
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        mobileno = findViewById(R.id.mobileno);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);

        signup = findViewById(R.id.signup);

        backT = findViewById(R.id.backT);
    }

    @SuppressLint("StaticFieldLeak")
    public class SignUp extends AsyncTask<String,String,String>
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
                URL url = new URL(Constants.location+"signup.php");
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
                else if(result.matches("DUPLICATE")){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupScreen.this);
                    alertDialogBuilder.setTitle("Signup Failed!");
                    alertDialogBuilder.setMessage("Your registration was failed because either of your email id or mobile number already exists! Please use the Forgot Password Option to retrive the password if you already have an account!");
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else if(result.matches("NOREG")){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupScreen.this);
                    alertDialogBuilder.setTitle("Signup Failed!");
                    alertDialogBuilder.setMessage("Your registration was failed due to some unknown reasons! Please retry again later!");
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupScreen.this);
                    alertDialogBuilder.setTitle("Signup Failed!");
                    alertDialogBuilder.setMessage("Your registration was failed because of the following error\n"+result);
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }


}