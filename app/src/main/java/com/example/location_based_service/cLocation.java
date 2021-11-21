package com.example.location_based_service;


import java.util.ArrayList;

public class cLocation {
    public String mName;

    public ArrayList<String> getmUrls() {
        return mUrls;
    }

    public void setmUrls(ArrayList<String> mUrls) {
        this.mUrls = mUrls;
    }

    public ArrayList<String> mUrls;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float mNumberOfStar;
    public String comment;
    public double lattitude;
    public double longitude;
    // public Bitmap mImage;

    public void setmNumberOfStar(float mNumberOfStar) {
        this.mNumberOfStar = mNumberOfStar;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public cLocation(String mName, float mNumberOfStar) {
        this.mName = mName;
        this.mNumberOfStar = mNumberOfStar;
    }

    public String getmName() {
        return mName;
    }

    public float getmNumberOfStar() {
        return mNumberOfStar;
    }

    public cLocation(){

    }


}
