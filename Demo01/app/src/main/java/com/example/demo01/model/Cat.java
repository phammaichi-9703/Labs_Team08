package com.example.demo01.model;

public class Cat {

    private String name;
    private int imageResId;

    public Cat(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}