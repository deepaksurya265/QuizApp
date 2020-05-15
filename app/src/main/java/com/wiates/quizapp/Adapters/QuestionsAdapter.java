package com.wiates.quizapp.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wiates.quizapp.Admin.NewQuestionEntryActivity;
import com.wiates.quizapp.Models.AnswerModel;
import com.wiates.quizapp.Models.QuestionsModel;
import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.R;
import com.wiates.quizapp.User.ViewQuestions;

import java.io.Serializable;
import java.util.List;


public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    public static List<AnswerModel> questionsModels;
    private Context context;
    private ProgressDialog progressDialog;

    public QuestionsAdapter(Context context, List<AnswerModel> questionsModels) {
        this.context = context;
        this.questionsModels = questionsModels;

        Log.d("Tasg", questionsModels.toString());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_ind, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final AnswerModel questionsModel = questionsModels.get(i);

        progressDialog = new ProgressDialog(context);

        questionsModels.get(i).setChoosen_option("nil");
        viewHolder.question.setText(i+1+". "+questionsModel.getQuestion());

        viewHolder.optiona.setText(questionsModel.getOption_a());
        viewHolder.optionb.setText(questionsModel.getOption_b());
        viewHolder.optionc.setText(questionsModel.getOption_c());
        viewHolder.optiond.setText(questionsModel.getOption_d());

        if(i==questionsModels.size()-1){
            ViewQuestions.next.setText("Submit");
        }

        viewHolder.a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(viewHolder.a.isChecked()) {
                    questionsModels.get(i).setChoosen_option("a");
                    viewHolder.b.setChecked(false);
                    viewHolder.c.setChecked(false);
                    viewHolder.d.setChecked(false);
                }
            }
        });

        viewHolder.b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(viewHolder.b.isChecked()){
                    questionsModels.get(i).setChoosen_option("b");
                    viewHolder.a.setChecked(false);
                    viewHolder.c.setChecked(false);
                    viewHolder.d.setChecked(false);
                }

            }
        });

        viewHolder.c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(viewHolder.c.isChecked()){
                    questionsModels.get(i).setChoosen_option("c");
                    viewHolder.b.setChecked(false);
                    viewHolder.a.setChecked(false);
                    viewHolder.d.setChecked(false);
                }

            }
        });
        viewHolder.d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(viewHolder.d.isChecked()){
                    questionsModels.get(i).setChoosen_option("d");
                    viewHolder.b.setChecked(false);
                    viewHolder.c.setChecked(false);
                    viewHolder.a.setChecked(false);
                }

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
        return questionsModels.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView question,optiona,optionb,optionc,optiond;
        CheckBox a,b,c,d;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            optiona = itemView.findViewById(R.id.optiona);
            optionb = itemView.findViewById(R.id.optionb);
            optionc = itemView.findViewById(R.id.optionc);
            optiond = itemView.findViewById(R.id.optiond);

            a = itemView.findViewById(R.id.a);
            b = itemView.findViewById(R.id.b);
            c = itemView.findViewById(R.id.c);
            d = itemView.findViewById(R.id.d);



        }
    }


}
