package com.example.sbbunewsalerts.Gallery;

public class GalleryModel {
    String imageURLS;
    String imgTitle;
    String currentDate;

    public GalleryModel() {
    }

    public GalleryModel(String imageURLS, String imgTitle, String currentDate) {
        this.imageURLS = imageURLS;
        this.imgTitle = imgTitle;
        this.currentDate = currentDate;
    }

    public GalleryModel(String imageURLS) {
        this.imageURLS = imageURLS;
    }

    public String getimageURLS() {
        return imageURLS;
    }

    public void setimageURLS(String imageURLS) {
        this.imageURLS = imageURLS;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

}
