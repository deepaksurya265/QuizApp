package com.wiates.quizapp.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Adapters.ListTestAdapterStudent;
import com.wiates.quizapp.Adapters.QuestionsAdapter;
import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.Models.AnswerModel;
import com.wiates.quizapp.Models.QuestionsModel;
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
import java.util.ArrayList;
import java.util.List;

public class ViewQuestions extends AppCompatActivity {

    private List<AnswerModel> questionsModels = new ArrayList<AnswerModel>();
    private QuestionsAdapter questionsAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView questions_list;
    ProgressDialog progressDialog;

    TextView test_name,test_id,timerr;
    public static Button next,previous;

    public int correctCount = 0;
    public int wrongCount = 0;
    TestListModel testListModel;

    CountDownTimer countDownTimer;

    long timeLeft = 120*60*1000;
    boolean timerrunning;

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"You cannot go back at this moment! Please complete the test to leave the test!",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();





        final Intent intent = getIntent();
        testListModel = (TestListModel) intent.getSerializableExtra("class");

        test_name.setText(testListModel.getQuiz_name());
        test_id.setText(testListModel.getQuiz_id());

        new FetchQuestions().execute("SELECT * from appathon_quiz_questions");



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (questionsAdapter.getItemCount() - 1)) {
                    linearLayoutManager.scrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }


                if(next.getText().toString().matches("Submit")){
                    correctCount = 0;
                    wrongCount = 0;
                    for(int i=0;i<QuestionsAdapter.questionsModels.size();i++){
                        if(QuestionsAdapter.questionsModels.get(i).getChoosen_option().matches(QuestionsAdapter.questionsModels.get(i).getOption_correction())){
                            correctCount++;
                        }
                        else {
                            wrongCount++;
                        }
                    }




                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewQuestions.this);
                    alertDialogBuilder.setTitle("Are you Sure?");
                    alertDialogBuilder.setMessage("Do you really want to submit your Answers?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    SharedPreferences userSP = getSharedPreferences("user_loggedin",MODE_PRIVATE);
                                    String user = userSP.getString("user_loggedin","NOID");

                                    if(!user.matches("NOID")){
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<UserModel>>() {}.getType();
                                        ArrayList<UserModel> userModels = gson.fromJson(user, type);

                                        String sql = "INSERT INTO appathon_quiz_attempts (quiz_id,user_id,questions_correct,questions_wrong,marks_obtained) VALUES ('"+testListModel.getQuiz_id()+"',"+userModels.get(0).getId()+","+correctCount+","+wrongCount+","+correctCount+")";

                                        new SubmitAnswers().execute(sql);
                                    }





                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"Cancelled!",Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() >= (questionsAdapter.getItemCount() - 1)) {
                    linearLayoutManager.scrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() - 1);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You are in the First Question of this Quiz!",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void initialize(){

        progressDialog = new ProgressDialog(this);
        questions_list = findViewById(R.id.questions_list);

        test_id = findViewById(R.id.test_id);
        test_name = findViewById(R.id.test_name);

        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        timerr = findViewById(R.id.timerr);
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchQuestions extends AsyncTask<String,String,String>
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
                URL url = new URL(Constants.location+"fetch_qs.php");
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
                if (result.matches("NOTOK")){
                    Toast.makeText(getApplicationContext(),"There are No New Tests Created! Please create a new Test before you come here!",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {

                    String json = result.substring(1,result.length()-1);

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<AnswerModel>>() {}.getType();
                    questionsModels = gson.fromJson(json, type);

                    recyclerView = findViewById(R.id.questions_list);
                    recyclerView.setNestedScrollingEnabled(true);

                    linearLayoutManager = new LinearLayoutManager(ViewQuestions.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    questionsAdapter = new QuestionsAdapter(ViewQuestions.this, questionsModels);
                    recyclerView.setAdapter(questionsAdapter);

                    countDownTimer = new CountDownTimer(timeLeft,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timeLeft = millisUntilFinished;
                            timerr.setText("Time Left: "+(int)timeLeft/60000 +":"+(int)timeLeft%60000/1000);
                        }

                        @Override
                        public void onFinish() {
                            correctCount = 0;
                            wrongCount = 0;
                            for(int i=0;i<QuestionsAdapter.questionsModels.size();i++){
                                if(QuestionsAdapter.questionsModels.get(i).getChoosen_option().matches(QuestionsAdapter.questionsModels.get(i).getOption_correction())){
                                    correctCount++;
                                }
                                else {
                                    wrongCount++;
                                }

                                SharedPreferences userSP = getSharedPreferences("user_loggedin",MODE_PRIVATE);
                                String user = userSP.getString("user_loggedin","NOID");

                                if(!user.matches("NOID")){
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<ArrayList<UserModel>>() {}.getType();
                                    ArrayList<UserModel> userModels = gson.fromJson(user, type);

                                    String sql = "INSERT INTO appathon_quiz_attempts (quiz_id,user_id,questions_correct,questions_wrong,marks_obtained) VALUES ('"+testListModel.getQuiz_id()+"',"+userModels.get(0).getId()+","+correctCount+","+wrongCount+","+correctCount+")";

                                    new ForceSubmitAnswers().execute(sql);
                                }


                            }


                        }
                    }.start();


                }
            }
            else{
                Toast.makeText(getApplicationContext(),"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SubmitAnswers extends AsyncTask<String,String,String>
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
                URL url = new URL(Constants.location+"submit_answer.php");
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
                    Toast.makeText(getApplicationContext(),"Your Test is successfully submitted!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),SuccessfullySubmittedActivity.class);
                    intent.putExtra("correct",String.valueOf(correctCount));
                    intent.putExtra("wrong",String.valueOf(wrongCount));
                    intent.putExtra("class",testListModel);
                    startActivity(intent);
                    finish();
                }
                else {

                    String json = result.substring(1,result.length()-1);

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<AnswerModel>>() {}.getType();
                    questionsModels = gson.fromJson(json, type);

                    recyclerView = findViewById(R.id.questions_list);
                    recyclerView.setNestedScrollingEnabled(true);

                    linearLayoutManager = new LinearLayoutManager(ViewQuestions.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    questionsAdapter = new QuestionsAdapter(ViewQuestions.this, questionsModels);
                    recyclerView.setAdapter(questionsAdapter);

                }
            }
            else{
                Toast.makeText(getApplicationContext(),"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ForceSubmitAnswers extends AsyncTask<String,String,String>
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
                URL url = new URL(Constants.location+"submit_answer.php");
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

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewQuestions.this);
                    alertDialogBuilder.setTitle("Answers are Auto Submitted!");
                    alertDialogBuilder.setMessage("As the time was over, your answers were submitted automatically!");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(getApplicationContext(),SuccessfullySubmittedActivity.class);
                                    intent.putExtra("correct",String.valueOf(correctCount));
                                    intent.putExtra("wrong",String.valueOf(wrongCount));
                                    intent.putExtra("class",testListModel);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                }
                else {

                    Toast.makeText(getApplicationContext(),"Some Error Occured! Error Code: "+result,Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

}
