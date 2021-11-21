package com.example.location_based_service;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationImageAdapter extends RecyclerView.Adapter<LocationImageAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<Bitmap> mImages;
    private Context mContext;

    public void setmImages(ArrayList<Bitmap> mImages) {
        this.mImages = mImages;
    }

    public LocationImageAdapter(Context context) {
        mContext=context;
        mInflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public LocationImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_image_layout, parent, false);
        return new LocationImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationImageAdapter.ViewHolder holder, int position) {
        if(mImages!=null){
            Bitmap bmp=mImages.get(position);
            holder.imageView.setImageBitmap(mImages.get(position));
        }
    }

    @Override
    public int getItemCount() {
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
