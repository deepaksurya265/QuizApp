package com.wiates.quizapp.Models;

import java.io.Serializable;

public class TestListModel implements Serializable {

    String quiz_id,quiz_start_timing,quiz_end_timing,total_time_for_quiz,quiz_name,quiz_type,quiz_availablity;

    public TestListModel(String quiz_id, String quiz_start_timing, String quiz_end_timing, String total_time_for_quiz, String quiz_name, String quiz_type, String quiz_availablity) {
        this.quiz_id = quiz_id;
        this.quiz_start_timing = quiz_start_timing;
        this.quiz_end_timing = quiz_end_timing;
        this.total_time_for_quiz = total_time_for_quiz;
        this.quiz_name = quiz_name;
        this.quiz_type = quiz_type;
        this.quiz_availablity = quiz_availablity;
    }

    public TestListModel() {
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_start_timing() {
        return quiz_start_timing;
    }

    public void setQuiz_start_timing(String quiz_start_timing) {
        this.quiz_start_timing = quiz_start_timing;
    }

    public String getQuiz_end_timing() {
        return quiz_end_timing;
    }

    public void setQuiz_end_timing(String quiz_end_timing) {
        this.quiz_end_timing = quiz_end_timing;
    }

    public String getTotal_time_for_quiz() {
        return total_time_for_quiz;
    }

    public void setTotal_time_for_quiz(String total_time_for_quiz) {
        this.total_time_for_quiz = total_time_for_quiz;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getQuiz_type() {
        return quiz_type;
    }

    public void setQuiz_type(String quiz_type) {
        this.quiz_type = quiz_type;
    }

    public String getQuiz_availablity() {
        return quiz_availablity;
    }

    public void setQuiz_availablity(String quiz_availablity) {
        this.quiz_availablity = quiz_availablity;
    }
}
