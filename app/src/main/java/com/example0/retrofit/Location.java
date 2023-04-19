package com.example0.retrofit;

import java.io.Serializable;

public class Location implements Serializable {
     private int reX;
     private int reY;
     private int height;
     private int width;

    public Location() {
    }

    public Location(int reX, int reY, int width, int height) {
        this.reX = reX;
        this.reY = reY;
        this.height = height;
        this.width = width;
    }

    public Location(int[] highestConfidenceObjectWithLabel) {
        this.reX = highestConfidenceObjectWithLabel[0];
        this.reY = highestConfidenceObjectWithLabel[1];
        this.width = highestConfidenceObjectWithLabel[2];
        this.height = highestConfidenceObjectWithLabel[3];
    }

    public int getReX() {
        return reX;
    }

    public void setReX(int reX) {
        this.reX = reX;
    }

    public int getReY() {
        return reY;
    }

    public void setReY(int reY) {
        this.reY = reY;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
