package com.example.location_based_service;

import android.graphics.Bitmap;

public class cLocation {
    private String mName;
    private float mNumberOfStar;
    private Bitmap mImage;

    public cLocation(){
        mName="";
        mNumberOfStar=5;
        mImage=null;
    }

    public String getmName(){ return mName;}
    public float getmNumberOfStar(){return mNumberOfStar;}
    public void setmName(String name){ mName=name;}
    public void setmNumberOfStar(float numberOfStar){ mNumberOfStar=numberOfStar;}
    public Bitmap getmImage(){return mImage;}
    public void setmImage(Bitmap image){mImage=image;}
}
