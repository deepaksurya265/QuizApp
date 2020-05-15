package com.wiates.quizapp.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.Models.QuestionsModel;
import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.R;

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
import java.util.ArrayList;
import java.util.List;

public class NewQuestionEntryActivity extends AppCompatActivity {

    EditText question,optiona,optionb,optionc,optiond;
    CheckBox a,b,c,d;
    Button ok,addmore;
    TextView test_name,test_id;

    ProgressDialog progressDialog;

    private List<QuestionsModel> questionsModels = new ArrayList<QuestionsModel>();


    ImageView backT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question_entry);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initialize();

        Intent intent = getIntent();
        final TestListModel testListModel = (TestListModel) intent.getSerializableExtra("class");

        test_name.setText(testListModel.getQuiz_name());
        test_id.setText(testListModel.getQuiz_id());


        backT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(a.isChecked()){
                    b.setChecked(false);
                    c.setChecked(false);
                    d.setChecked(false);
                }
            }
        });

        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(b.isChecked()){
                    a.setChecked(false);
                    c.setChecked(false);
                    d.setChecked(false);
                }

            }
        });

        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(c.isChecked()){
                    b.setChecked(false);
                    a.setChecked(false);
                    d.setChecked(false);
                }

            }
        });

        d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(d.isChecked()){
                    b.setChecked(false);
                    c.setChecked(false);
                    a.setChecked(false);
                }
            }
        });

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sQuestion = question.getText().toString();

                String sOptiona = optiona.getText().toString();
                String sOptionb = optionb.getText().toString();
                String sOptionc = optionc.getText().toString();
                String sOptiond = optiond.getText().toString();

                String sCorrect = "";

                if(a.isChecked()){
                    sCorrect = "a";
                }
                else if(b.isChecked()){
                    sCorrect = "b";
                }
                else if(c.isChecked()){
                    sCorrect = "c";
                }
                else if(d.isChecked()){
                    sCorrect = "d";
                }

                if(!a.isChecked() && !b.isChecked() && !c.isChecked() && !d.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please select any one of the Options as Correct answer",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sQuestion)){
                    Toast.makeText(getApplicationContext(),"Please give the Question",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptiona)){
                    Toast.makeText(getApplicationContext(),"Please give Option A",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptionb)){
                    Toast.makeText(getApplicationContext(),"Please give Option B",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptionc)){
                    Toast.makeText(getApplicationContext(),"Please give Option C",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptiond)){
                    Toast.makeText(getApplicationContext(),"Please give Option D",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sCorrect)){
                    Toast.makeText(getApplicationContext(),"Please select the Correct Option",Toast.LENGTH_LONG).show();
                }
                else{
                    QuestionsModel questionsModel = new QuestionsModel(testListModel.getQuiz_id(),sQuestion,sOptiona,sOptionb,sOptionc,sOptiond,sCorrect);
                    questionsModels.add(questionsModel);
                    Toast.makeText(getApplicationContext(),"This question is saved locally! Enter the next Question!",Toast.LENGTH_LONG).show();
                    question.setText("");

                    optiona.setText("");
                    optionb.setText("");
                    optionc.setText("");
                    optiond.setText("");

                    a.setChecked(false);
                    b.setChecked(false);
                    c.setChecked(false);
                    d.setChecked(false);
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sQuestion = question.getText().toString();

                String sOptiona = optiona.getText().toString();
                String sOptionb = optionb.getText().toString();
                String sOptionc = optionc.getText().toString();
                String sOptiond = optiond.getText().toString();

                String sCorrect = "";

                if(a.isChecked()){
                    sCorrect = "a";
                }
                else if(b.isChecked()){
                    sCorrect = "b";
                }
                else if(c.isChecked()){
                    sCorrect = "c";
                }
                else if(d.isChecked()){
                    sCorrect = "d";
                }

                if(!a.isChecked() && !b.isChecked() && !c.isChecked() && !d.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please select any one of the Options as Correct answer",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sQuestion)){
                    Toast.makeText(getApplicationContext(),"Please give the Question",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptiona)){
                    Toast.makeText(getApplicationContext(),"Please give Option A",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptionb)){
                    Toast.makeText(getApplicationContext(),"Please give Option B",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptionc)){
                    Toast.makeText(getApplicationContext(),"Please give Option C",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sOptiond)){
                    Toast.makeText(getApplicationContext(),"Please give Option D",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(sCorrect)){
                    Toast.makeText(getApplicationContext(),"Please select the Correct Option",Toast.LENGTH_LONG).show();
                }
                else{
                    QuestionsModel questionsModel = new QuestionsModel(testListModel.getQuiz_id(),sQuestion,sOptiona,sOptionb,sOptionc,sOptiond,sCorrect);
                    questionsModels.add(questionsModel);

                    String sql = "INSERT INTO appathon_quiz_questions (quiz_id,question,option_a,option_b,option_c,option_d,option_correction) VALUES ";

                    String text = "";

                    for(int i=0;i<questionsModels.size();i++){
                        if(i<=questionsModels.size()-2){
                            text = text + "('"+questionsModels.get(i).getQuiz_id()+"','"+questionsModels.get(i).getQuestion()+"','"+questionsModels.get(0).getOption_a()+"','"+questionsModels.get(0).getOption_b()+"','"+questionsModels.get(0).getOption_c()+"','"+questionsModels.get(0).getOption_d()+"','"+questionsModels.get(0).getOption_correction()+"'),";
                        }
                        else {
                            text = text + "('"+questionsModels.get(i).getQuiz_id()+"','"+questionsModels.get(i).getQuestion()+"','"+questionsModels.get(0).getOption_a()+"','"+questionsModels.get(0).getOption_b()+"','"+questionsModels.get(0).getOption_c()+"','"+questionsModels.get(0).getOption_d()+"','"+questionsModels.get(0).getOption_correction()+"')";
                        }
                    }

                    sql+=text;

                    Log.d("Tag",sql);

                    new NewQuestion().execute(sql);


                }
            }
        });
    }

    private void initialize(){

        progressDialog = new ProgressDialog(this);


        backT = findViewById(R.id.backT);
        question = findViewById(R.id.question);
        optiona = findViewById(R.id.optiona);
        optionb = findViewById(R.id.optionb);
        optionc = findViewById(R.id.optionc);
        optiond = findViewById(R.id.optiond);

        test_name = findViewById(R.id.test_name);
        test_id = findViewById(R.id.test_id);


        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);

        ok = findViewById(R.id.ok);
        addmore = findViewById(R.id.addmore);

    }

    @SuppressLint("StaticFieldLeak")
    public class NewQuestion extends AsyncTask<String,String,String>
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
                URL url = new URL(Constants.location+"new_qs_add.php");
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
                    Toast.makeText(getApplicationContext(),"Your new set of Questions are added Successfully! There are totally "+questionsModels.size()+" questions uploaded to this test!",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewQuestionEntryActivity.this);
                    alertDialogBuilder.setTitle("Question Creation Failed!");
                    alertDialogBuilder.setMessage("Your question creation was failed because of the following error\n"+result);
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
