package com.congnghejava.toeicapp.model;

public class Comments {
    public String content;
    public String userid;
    public String useravatar;
    public String username;
    public String category;

    public Comments() {
    }

    public Comments(String content, String userid, String useravatar, String username, String category) {
        this.content = content;
        this.userid = userid;
        this.useravatar = useravatar;
        this.username = username;
        this.category = category;
    }
}
