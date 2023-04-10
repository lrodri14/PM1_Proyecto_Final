package com.example.myapplication;

public class Usuario {

    private int id;
    private int ImageResourceId;
    private String Name;
    private String Career;
    private int IconResourceId;

    public Usuario(int id, int imageResourceId, String name, String career, int iconResourceId) {
        this.id = id;
        ImageResourceId = imageResourceId;
        Name = name;
        Career = career;
        IconResourceId = iconResourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResourceId() {
        return ImageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        ImageResourceId = imageResourceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCareer() {
        return Career;
    }

    public void setCareer(String career) {
        Career = career;
    }

    public int getIconResourceId() {
        return IconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        IconResourceId = iconResourceId;
    }

    public void clear() {
    }
}

