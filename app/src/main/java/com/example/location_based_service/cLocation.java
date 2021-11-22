package com.example.location_based_service;


import java.util.ArrayList;

public class cLocation {

    public String mName;
    public ArrayList<String> mUrls;
    public float mNumberOfStar;
    public double lattitude;
    public double longitude;
    private int mImageID;
    private String mDetail;
    private String mPhone;
    private String mEmail;
    private String[] mComment;
    private double[] mCoop;






    public ArrayList<String> getmUrls() {
        return mUrls;
    }
    public void setmUrls(ArrayList<String> mUrls) {
        this.mUrls = mUrls;
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
        mName="";
        mNumberOfStar=0;
        // mImage=null;
        mDetail="";
        mPhone="";
        mEmail="";
        mComment=null;
        mCoop=null;
    }
    public String getmDetail(){ return mDetail;}
    public String getmPhone(){ return mPhone;}
    public String getmEmail(){ return mEmail;}
    public double[] getmCoop(){ return mCoop;}

    public void setmPhone(String name){ mPhone=name;}
    public int getmImageID(){return mImageID;}
    public void setmImageID(int id){mImageID=id;}
    public void setmEmail(String name){ mEmail=name;}
    public void setmDetail(String name){ mDetail=name;}
}
