package com.example.parul.newsdaily;

public class NewsDaily {

    private String sectionName;
    private String title;
    private String date;
    private String author;
    private String url;

    public NewsDaily(String sectionName, String title, String date, String author, String url) {
        this.sectionName = sectionName;
        this.title = title;
        this.date = date;
        this.author = author;
        this.url = url;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return this.author;
    }

}
