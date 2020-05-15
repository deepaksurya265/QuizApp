package com.wiates.quizapp.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wiates.quizapp.Admin.NewQuestionEntryActivity;
import com.wiates.quizapp.Models.TestListModel;
import com.wiates.quizapp.R;

import java.io.Serializable;
import java.util.List;


public class ListTestAdapter extends RecyclerView.Adapter<ListTestAdapter.ViewHolder> {

    private List<TestListModel> testListModels;
    private Context context;
    private ProgressDialog progressDialog;

    public ListTestAdapter(Context context, List<TestListModel> testListModels) {
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

                Intent intent = new Intent(context, NewQuestionEntryActivity.class);
                intent.putExtra("class", (Serializable) testListModel);
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


}
