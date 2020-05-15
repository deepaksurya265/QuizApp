package com.wiates.quizapp.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Admin.NewQuestionEntryActivity;
import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.Models.UserModel;
import com.wiates.quizapp.R;
import com.wiates.quizapp.SignupScreen;
import com.wiates.quizapp.User.TestInstructions;
import com.wiates.quizapp.User.ViewAttempts;
import com.wiates.quizapp.User.ViewLeaderboard;
import com.wiates.quizapp.User.ViewQuestions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
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
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class ListTestAdapterLeaderboard extends RecyclerView.Adapter<ListTestAdapterLeaderboard.ViewHolder> {

    private List<TestListModel> testListModels;
    private Context context;
    private ProgressDialog progressDialog;
    ArrayList<UserModel> userModels;

    TestListModel testListModel;

    public ListTestAdapterLeaderboard(Context context, List<TestListModel> testListModels) {
        this.context = context;
        this.testListModels = testListModels;

        Log.d("Tasg", testListModels.toString());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.ind_test, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        testListModel = testListModels.get(i);

        progressDialog = new ProgressDialog(context);


        viewHolder.testname.setText(testListModel.getQuiz_name());
        viewHolder.testid.setText(testListModel.getQuiz_id());

        viewHolder.start.setText("Start Time\n" + testListModel.getQuiz_start_timing());
        viewHolder.end.setText("End Time\n" + testListModel.getQuiz_end_timing());
        viewHolder.total.setText("Total Time: "+ testListModel.getTotal_time_for_quiz() + " mins");


        SharedPreferences userSP = context.getSharedPreferences("user_loggedin", MODE_PRIVATE);
        String user = userSP.getString("user_loggedin", "NOID");

        if (!user.matches("NOID")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<UserModel>>() {
            }.getType();
            userModels = gson.fromJson(user, type);


        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sql = "SELECT *,appathon_quiz_users.name FROM appathon_quiz_attempts,appathon_quiz_users where quiz_id = '"+testListModel.getQuiz_id()+"' and appathon_quiz_attempts.user_id = appathon_quiz_users.id ORDER by marks_obtained desc";

                new ViewLeaderBoard().execute(sql,userModels.get(0).getId());





            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return testListModels.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView testname, testid, start, end, total;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            testname = itemView.findViewById(R.id.testname);
            testid = itemView.findViewById(R.id.testid);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);
            total = itemView.findViewById(R.id.total);



        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ViewLeaderBoard extends AsyncTask<String,String,String>
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
            String user_id = params[1];


            try {
                URL url = new URL(Constants.location+"view_leaderboard.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("sql","UTF-8") +"="+URLEncoder.encode(sql,"UTF-8")+"&"+
                        URLEncoder.encode("user_id","UTF-8") +"="+URLEncoder.encode(user_id,"UTF-8");
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
                    Toast.makeText(context,"You have'nt attempted this test yet!",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(context, ViewLeaderboard.class);
                    intent.putExtra("result",result);
                    context.startActivity(intent);
                }
            }
            else{
                Toast.makeText(context,"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
