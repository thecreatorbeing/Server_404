package com.sarveshdev.techlearn.recyclerview_api.news;

public class HomeRecyclerModel {
    private String url, title, date;

    public HomeRecyclerModel(){}

    public HomeRecyclerModel(String url, String title, String date) {
        this.url = url;
        this.title = title;
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}//HomeRecyclerModel class ended!