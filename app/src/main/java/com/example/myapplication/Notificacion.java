package com.example.myapplication;

public class Notificacion {

    private String name;
    private String userName;
    private String subject;
    private int image;
    private String time;
    private String date;

    public Notificacion(String name, String userName, String subject, int image, String time, String date) {
        this.name = name;
        this.userName = userName;
        this.subject = subject;
        this.image = image;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
