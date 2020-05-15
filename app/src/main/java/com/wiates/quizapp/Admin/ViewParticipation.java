package com.wiates.quizapp.User;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Adapters.ListTestAdapter;
import com.wiates.quizapp.Adapters.ListTestAdapterAttempts;
import com.wiates.quizapp.Adapters.ListTestAdapterAttemptsAdmin;
import com.wiates.quizapp.Adapters.ListTestAdapterStudent;
import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.Models.AdminModel;
import com.wiates.quizapp.Models.TestListModel;
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
import java.util.ArrayList;
import java.util.List;

public class ViewParticipation extends AppCompatActivity {

    RecyclerView test_list;
    ProgressDialog progressDialog;


    private List<AdminModel> testListModels = new ArrayList<AdminModel>();
    private ListTestAdapterAttemptsAdmin listTestAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    ImageView backT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_participation);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        backT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        String sql = "SELECT *,appathon_quiz_users.name, appathon_quiz_tests.quiz_name FROM appathon_quiz_attempts,appathon_quiz_users,appathon_quiz_tests where user_id = appathon_quiz_users.id and appathon_quiz_tests.quiz_id = appathon_quiz_attempts.quiz_id and appathon_quiz_attempts.quiz_id = '"+id+"' order by appathon_quiz_attempts.marks_obtained;";


        new FetchTests().execute(sql);

    }

    private void initialize(){

        backT = findViewById(R.id.backT);
        progressDialog = new ProgressDialog(this);
        test_list = findViewById(R.id.test_list);

    }

    @SuppressLint("StaticFieldLeak")
    public class FetchTests extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please wait while we are fetching the your Tests ");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String ... params) {

            String sql = params[0];

            try {
                URL url = new URL(Constants.location+"get_attempt_admin.php");
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
                if (result.matches("FAIL")){
                    Toast.makeText(getApplicationContext(),"There are no attempts for this Test! Please view after some attempts are made!",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {

                    String json = result.substring(1,result.length()-1);



                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<AdminModel>>() {}.getType();
                    testListModels = gson.fromJson(json, type);

                    recyclerView = findViewById(R.id.test_list);
                    recyclerView.setNestedScrollingEnabled(false);

                    linearLayoutManager = new LinearLayoutManager(ViewParticipation.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    listTestAdapter = new ListTestAdapterAttemptsAdmin(ViewParticipation.this, testListModels);
                    recyclerView.setAdapter(listTestAdapter);

                }
            }
            else{
                Toast.makeText(getApplicationContext(),"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
