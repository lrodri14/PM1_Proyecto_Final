package com.example.myapplication;

public class ListaGrupos {

    private int ImageResourceId;
    private String NameA;
    private String CareerA;
    private int IconResourceId;

    public ListaGrupos(int imageResourceId, String name, int iconResourceId) {
        ImageResourceId = imageResourceId;
        NameA = name;
        IconResourceId = iconResourceId;
    }

    public int getImageResourceId() {
        return ImageResourceId;
    }

    public String getName() {
        return NameA;
    }

    public int getIconResourceId() {
        return IconResourceId;
    }

    public void clear() {
    }

    }

