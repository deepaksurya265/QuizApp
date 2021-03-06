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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiates.quizapp.Admin.NewQuestionEntryActivity;
import com.wiates.quizapp.Constants.Constants;
import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.Models.UserModel;
import com.wiates.quizapp.R;
import com.wiates.quizapp.SignupScreen;
import com.wiates.quizapp.User.AttemptView;
import com.wiates.quizapp.User.ViewParticipation;

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
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ListTestAdapterForAdmin extends RecyclerView.Adapter<ListTestAdapterForAdmin.ViewHolder> {

    private List<TestListModel> testListModels;
    private Context context;
    private ProgressDialog progressDialog;

    public ListTestAdapterForAdmin
            (Context context, List<TestListModel> testListModels) {
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
        final TestListModel testListModel = testListModels.get(i);

        progressDialog = new ProgressDialog(context);


        viewHolder.testname.setText(testListModel.getQuiz_name());
        viewHolder.testid.setText(testListModel.getQuiz_id());

        viewHolder.start.setText("Start Time: "+testListModel.getQuiz_start_timing());
        viewHolder.end.setText("End Time: "+testListModel.getQuiz_end_timing());
        viewHolder.total.setText("Total Time: "+testListModel.getTotal_time_for_quiz());




        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewParticipation.class);
                intent.putExtra("id",testListModel.getQuiz_id());
                context.startActivity(intent);

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
        TextView testname,testid,start,end,total;


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
    public class GetAttempt extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Please wait!");
            progressDialog.setMessage("Please wait while we are fetching this details!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String ... params) {

            String sql = params[0];

            try {
                URL url = new URL(Constants.location+"get_attempt.php");
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
                    Toast.makeText(context,"Your have not attempted this test yet!",Toast.LENGTH_LONG).show();
                }
                else {
                    String arr[] = result.split(" ");
                    Intent intent = new Intent(context, AttemptView.class);
                    intent.putExtra("correct",arr[0]);
                    intent.putExtra("wrong",arr[1]);
                    context.startActivity(intent);
                }
            }
            else{
                Toast.makeText(context,"There is a Null Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

}
