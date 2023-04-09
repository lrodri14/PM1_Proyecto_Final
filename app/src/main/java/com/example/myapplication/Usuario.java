package com.example.myapplication;

public class Usuario {

    private int ImageResourceId;
    private String Name;
    private String Career;
    private int IconResourceId;

    public Usuario(int imageResourceId, String name, String career, int iconResourceId) {
        ImageResourceId = imageResourceId;
        Name = name;
        Career = career;
        IconResourceId = iconResourceId;
    }

    public int getImageResourceId() {
        return ImageResourceId;
    }

    public String getName() {
        return Name;
    }

    public String getCareer() {
        return Career;
    }

    public int getIconResourceId() {
        return IconResourceId;
    }

    public void clear() {
    }
}

