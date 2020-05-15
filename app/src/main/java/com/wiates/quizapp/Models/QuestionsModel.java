package com.wiates.quizapp.Models;

public class QuestionsModel {

    String quiz_id,question,option_a,option_b,option_c,option_d,option_correction;

    public QuestionsModel(String quiz_id, String question, String option_a, String option_b, String option_c, String option_d, String option_correction) {
        this.quiz_id = quiz_id;
        this.question = question;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.option_correction = option_correction;
    }

    public QuestionsModel() {
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public String getOption_correction() {
        return option_correction;
    }

    public void setOption_correction(String option_correction) {
        this.option_correction = option_correction;
    }

}
