package com.wiates.quizapp.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.R;
import com.wiates.quizapp.SignupScreen;

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
import java.util.Calendar;

public class NewTestCreating extends AppCompatActivity {

    EditText name,type,time;
    TextView start,stop;
    Button create;

    DatePickerDialog datePickerDialog;

    ProgressDialog progressDialog;

    ImageView backT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_test_creating);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        backT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar c= Calendar.getInstance();
                int yearr=c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(NewTestCreating.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                view.setMinDate(System.currentTimeMillis() - 1000);

                                start.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                final Calendar c = Calendar.getInstance();
                                int mHour = c.get(Calendar.HOUR_OF_DAY);
                                int mMinute = c.get(Calendar.MINUTE);

                                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTestCreating.this,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {

                                                start.setText(start.getText().toString()+" "+hourOfDay + ":" + minute);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();


                            }
                        }, yearr,month,day);
                datePickerDialog.show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c= Calendar.getInstance();
                int yearr=c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(NewTestCreating.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                view.setMinDate(System.currentTimeMillis() - 1000);


                                stop.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                final Calendar c = Calendar.getInstance();
                                int mHour = c.get(Calendar.HOUR_OF_DAY);
                                int mMinute = c.get(Calendar.MINUTE);

                                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTestCreating.this,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {


                                                stop.setText(stop.getText().toString()+" "+hourOfDay + ":" + minute);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();


                            }
                        }, yearr,month,day);
                datePickerDialog.show();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sName = name.getText().toString();
                String sType = type.getText().toString();
                String sStart = start.getText().toString();
                String sStop = stop.getText().toString();
                String sTime = time.getText().toString();

                if(TextUtils.isEmpty(sName)){
                    Toast.makeText(getApplicationContext(),"Please enter the Quiz Name",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sType)){
                    Toast.makeText(getApplicationContext(),"Please enter the Quiz Type",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sStart)){
                    Toast.makeText(getApplicationContext(),"Please enter the Quiz Start Time",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sStop)){
                    Toast.makeText(getApplicationContext(),"Please enter the Quiz End Time",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sTime)){
                    Toast.makeText(getApplicationContext(),"Please enter the Quiz Timings",Toast.LENGTH_LONG).show();
                }
                else{

                    String random = String.valueOf(System.currentTimeMillis());
                    String sql = "INSERT INTO appathon_quiz_tests (quiz_id,quiz_start_timing,quiz_end_timing,total_time_for_quiz,quiz_name,quiz_type,quiz_availablity) VALUES ('"+random+"','"+sStart+"','"+sStop+"','"+sTime+"','"+sName+"','"+sType+"','"+"student"+"')";

                    new NewTest().execute(sql);
                }

            }
        });


    }

    public void initialize(){

        backT = findViewById(R.id.backT);
        progressDialog = new ProgressDialog(this);

        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        time = findViewById(R.id.time);

        create = findViewById(R.id.create);

    }

    @SuppressLint("StaticFieldLeak")
    public class NewTest extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Please wait!");
            progressDialog.setMessage("Please wait while we are creating your Test!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String ... params) {

            String sql = params[0];

            try {
                URL url = new URL(Constants.location+"newtest.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("sql","UTF-8") +"="+URLEncoder.encode(sql,"UTF-8");
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
                    Toast.makeText(getApplicationContext(),"Your new test is created successfully! Please Add new Questions in the Questions Section",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewTestCreating.this);
                    alertDialogBuilder.setTitle("Test Creation Failed!");
                    alertDialogBuilder.setMessage("Your test creation was failed because of the following error\n"+result);
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
