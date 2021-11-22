package com.example.location_based_service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    public void setmComments(List<cComment> mComments) {
        this.mComments = mComments;
    }

    private List<cComment> mComments;
    Context mContext;
    LayoutInflater mInflater;

    public CommentAdapter(Context context){
        mContext=context;
        mInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameView.setText(mComments.get(position).getmUserName());
        holder.dateView.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(mComments.get(position).getDate()));
        holder.contentView.setText(mComments.get(position).getmContent());
        holder.starView.setText("Đã cho "+String.valueOf(mComments.get(position).getmNumberOfStar())+" sao");
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView, dateView, contentView, starView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView=(TextView) itemView.findViewById(R.id.nameWritter);
            dateView=(TextView) itemView.findViewById(R.id.timeWrite);
            contentView=(TextView) itemView.findViewById(R.id.content);
            starView=(TextView) itemView.findViewById(R.id.star);
        }
    }
}
