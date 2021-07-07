package com.congnghejava.toeicapp.model;

public class ListeningExam {

    public String part, test, question, a , b, c, d, result, imagelink;

    public ListeningExam(){

    }

    public ListeningExam(String part, String test, String question, String a, String b, String c, String d, String result, String imagelink) {
        this.part = part;
        this.test = test;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.result = result;
        this.imagelink = imagelink;
    }
}
