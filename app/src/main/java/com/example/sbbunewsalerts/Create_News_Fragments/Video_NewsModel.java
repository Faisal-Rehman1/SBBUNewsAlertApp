package com.example.sbbunewsalerts.Create_News_Fragments;

public class Video_NewsModel {
    String VideoTitle;
    String videoUri;
    String time;
    String date;

    public Video_NewsModel() {
    }

    public Video_NewsModel(String VideoTitle, String videoUri, String time, String date) {
        this.VideoTitle = VideoTitle;
        this.videoUri = videoUri;
        this.time = time;
        this.date = date;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String VideoTitle) {
        this.VideoTitle = VideoTitle;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
