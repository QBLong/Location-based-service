package com.example.location_based_service;


import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<cLocation> mLocations;
    private Context mContext;
    public LocationListAdapter(Context context) {
        mContext=context;
        mInflater=LayoutInflater.from(context);

        mLocations=new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mLocations!=null){
            cLocation location=mLocations.get(position);
            holder.nameView.setText(location.getmName());
            holder.starView.setText(String.valueOf(location.getmNumberOfStar())+"/5");

            new DownloadImageTask(holder.imageView).execute(location.mUrls.get(0));


            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            processItemClick(location);
                        }
                    }
            );
        }
    }

    private void processItemClick(cLocation location) {
        Intent intent=new Intent(mContext, Detail_location.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    public void setmLocations(List<cLocation> mLocations) {
        this.mLocations = mLocations;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, starView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView=itemView.findViewById(R.id.text_name);
            starView=itemView.findViewById(R.id.text_star);
            imageView=itemView.findViewById(R.id.image_view);
        }
    }
}
