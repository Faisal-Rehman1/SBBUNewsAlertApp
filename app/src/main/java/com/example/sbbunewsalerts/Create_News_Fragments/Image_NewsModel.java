package com.example.sbbunewsalerts.Create_News_Fragments;

public class Image_NewsModel {
    String newsTitle;
    String NewsDescription;
    String time;
    String date;
    String imageUri;

    public Image_NewsModel() {

    }

    public Image_NewsModel(String newsTitle, String newsDescription, String time, String date, String imageUri) {
        this.newsTitle = newsTitle;
        this.NewsDescription = newsDescription;
        this.time = time;
        this.date = date;
        this.imageUri = imageUri;
    }


    public String getimageUri() {
        return imageUri;
    }

    public void setimageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return NewsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.NewsDescription = newsDescription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }
}
