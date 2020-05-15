package com.wiates.quizapp.Models;

public class AdminModel {

    String quiz_id,quiz_name,name,questions_correct,questions_wrong;


    public AdminModel(String quiz_id, String quiz_name, String name, String questions_correct, String questions_wrong) {
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
        this.name = name;
        this.questions_correct = questions_correct;
        this.questions_wrong = questions_wrong;
    }

    public AdminModel() {
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestions_correct() {
        return questions_correct;
    }

    public void setQuestions_correct(String questions_correct) {
        this.questions_correct = questions_correct;
    }

    public String getQuestions_wrong() {
        return questions_wrong;
    }

    public void setQuestions_wrong(String questions_wrong) {
        this.questions_wrong = questions_wrong;
    }
}
