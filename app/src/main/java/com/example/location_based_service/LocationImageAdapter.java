package com.example.location_based_service;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationImageAdapter extends RecyclerView.Adapter<LocationImageAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<Uri> mUriImages;
    private Context mContext;
    int mLayout;

    public void setmImages(ArrayList<Bitmap> mImages) {
        this.mImages = mImages;
    }

    private ArrayList<Bitmap> mImages;

    public void setmUriImages(ArrayList<Uri> mUriImages) {
        this.mUriImages = mUriImages;
    }

    public LocationImageAdapter(Context context, int layout) {
        mLayout=layout;
        mContext=context;
        mInflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public LocationImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(mLayout, parent, false);
        return new LocationImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationImageAdapter.ViewHolder holder, int position) {
        if(mUriImages!=null){
            Uri bmp=mUriImages.get(position);
            holder.imageView.setImageURI(bmp);
        }
        else if(mImages!=null){
            holder.imageView.setImageBitmap(mImages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(mImages==null) return mUriImages.size();
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view);
        }
    }
}