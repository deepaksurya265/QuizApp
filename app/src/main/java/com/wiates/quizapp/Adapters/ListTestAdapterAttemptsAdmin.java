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
import com.wiates.quizapp.Models.AdminModel;
import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.Models.UserModel;
import com.wiates.quizapp.R;
import com.wiates.quizapp.SignupScreen;
import com.wiates.quizapp.User.AttemptView;
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
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ListTestAdapterAttemptsAdmin extends RecyclerView.Adapter<ListTestAdapterAttemptsAdmin.ViewHolder> {

    private List<AdminModel> adminModels;
    private Context context;
    private ProgressDialog progressDialog;

    public ListTestAdapterAttemptsAdmin
            (Context context, List<AdminModel> adminModels) {
        this.context = context;
        this.adminModels = adminModels;

        Log.d("Tasg", adminModels.toString());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final AdminModel adminModel = adminModels.get(i);

        progressDialog = new ProgressDialog(context);


        viewHolder.name.setText(adminModel.getName());
        viewHolder.testid.setText(adminModel.getQuiz_id());

        viewHolder.correct.setText("Correct Answers\n"+adminModel.getQuestions_correct());
        viewHolder.wrong.setText("Wrong Answers\n"+adminModel.getQuestions_wrong());
        viewHolder.total.setText("Total Questions\n"+(Integer.valueOf(adminModel.getQuestions_correct())+Integer.valueOf(adminModel.getQuestions_wrong())));




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
        return adminModels.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,testid,correct,wrong,total;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            testid = itemView.findViewById(R.id.testid);
            correct = itemView.findViewById(R.id.correct);
            wrong = itemView.findViewById(R.id.wrong);
            total = itemView.findViewById(R.id.total);




        }
    }



}
