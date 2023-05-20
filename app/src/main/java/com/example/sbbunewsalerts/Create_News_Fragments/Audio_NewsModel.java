package com.example.sbbunewsalerts.Create_News_Fragments;

public class Audio_NewsModel {

    String AudioTitle;
    String AudioUri;
    String time;
    String date;


    public Audio_NewsModel() {
    }

    public Audio_NewsModel(String audioTitle, String audioUri, String time, String date) {
        AudioTitle = audioTitle;
        AudioUri = audioUri;
        this.time = time;
        this.date = date;
    }

    public Audio_NewsModel(String audioTitle, String audioUri) {
        AudioTitle = audioTitle;
        AudioUri = audioUri;
    }

    public String getAudioTitle() {
        return AudioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        AudioTitle = audioTitle;
    }

    public String getAudioUri() {
        return AudioUri;
    }

    public void setAudioUri(String audioUri) {
        AudioUri = audioUri;
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
