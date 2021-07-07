package com.congnghejava.toeicapp.model;

public class User {
    public  String uid;
    public String name;
    public String email;
    public String avtlink;
    public String score;
    public String countexam;
    public String roles;

    public User() {
    }

    public User(String uid,String name, String email, String avtlink, String score, String countexam, String roles) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avtlink = avtlink;
        this.score = score;
        this.countexam = countexam;
        this.roles = roles;
    }
}
