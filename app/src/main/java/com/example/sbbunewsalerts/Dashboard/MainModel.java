package com.example.sbbunewsalerts.Dashboard;

public class MainModel {
    String fullname, phone, email, department ,imageUrl;

    public MainModel()
    {

    }
    public MainModel(String fullname, String phone, String email, String department,  String imageUrl) {
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.department=department;
        this.imageUrl=imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getfullname() {
        return fullname;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }
}
