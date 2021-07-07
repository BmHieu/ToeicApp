package com.congnghejava.toeicapp.model;

public class Vocabulary {
    public String english;
    public String vietnamese;
    public String keyword;

    public Vocabulary() {
    }

    public Vocabulary(String keyword, String english, String vietnameses) {
        this.keyword = keyword;
        this.english = english;
        this.vietnamese = vietnameses;
    }
}
