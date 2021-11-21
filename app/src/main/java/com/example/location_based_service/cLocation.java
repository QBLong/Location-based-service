package com.example.location_based_service;

import android.graphics.Bitmap;

public class cLocation  {
    private String mName;
    private float mNumberOfStar;
    private Bitmap mImage;
    private int mImageID;
    private String mDetail;
    private String mPhone;
    private String mEmail;
    private String[] mComment;
    private double[] mCoop;

    public cLocation(){
        mName="";
        mNumberOfStar=0;
        mImage=null;
        mDetail="";
        mPhone="";
        mEmail="";
        mComment=null;
        mCoop=null;
    }

    public String getmName(){ return mName;}
    public String getmDetail(){ return mDetail;}
    public String getmPhone(){ return mPhone;}
    public String getmEmail(){ return mEmail;}
    public double[] getmCoop(){ return mCoop;}
    public float getmNumberOfStar(){return mNumberOfStar;}
    public void setmName(String name){ mName=name;}
    public void setmPhone(String name){ mPhone=name;}
    public int getmImageID(){return mImageID;}
    public void setmImageID(int id){mImageID=id;}
    public void setmEmail(String name){ mEmail=name;}
    public void setmDetail(String name){ mDetail=name;}
    public void setmNumberOfStar(float numberOfStar){ mNumberOfStar=numberOfStar;}
    public Bitmap getmImage(){return mImage;}
    public void setmImage(Bitmap image){mImage=image;}

}
