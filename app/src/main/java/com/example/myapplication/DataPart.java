package com.example.myapplication;

import java.io.InputStream;

public class DataPart {
    private String fileName;
    private byte[] content;
    private String type;

    public DataPart(String name, byte[] data) {
        fileName = name;
        content = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
