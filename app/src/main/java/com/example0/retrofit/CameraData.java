package com.example0.retrofit;

public class CameraData {

    private int [][] location;
    private int hand1;
    private int hand2;
    private int eye1;
    private int eye2;
    private int mouth;

    public int[][] getLocation() {
        return location;
    }

    public void setLocation(int[][] location) {
        this.location = location;
    }



    public int getHand1() {
        return hand1;
    }

    public void setHand1(int hand1) {
        this.hand1 = hand1;
    }

    public int getHand2() {
        return hand2;
    }

    public void setHand2(int hand2) {
        this.hand2 = hand2;
    }

    public int getEye1() {
        return eye1;
    }

    public void setEye1(int eye1) {
        this.eye1 = eye1;
    }

    public int getEye2() {
        return eye2;
    }

    public void setEye2(int eye2) {
        this.eye2 = eye2;
    }

    public int getMouth() {
        return mouth;
    }

    public void setMouth(int mouth) {
        this.mouth = mouth;
    }
}
