package com.congnghejava.toeicapp.model;

public class ReadingExam {
    public String part, test, question, a , b, c, d, result, paragraph;

    public ReadingExam(){

    }

    public ReadingExam(String part, String test, String question, String a, String b, String c, String d, String result, String paragraph) {
        this.part = part;
        this.test = test;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.result = result;
        this.paragraph = paragraph;
    }
}
