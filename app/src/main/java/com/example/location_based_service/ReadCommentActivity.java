package com.example.location_based_service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ReadCommentActivity extends AppCompatActivity {
    CommentDAO mCommentDAO;
    CommentAdapter mCommentAdapter;
    RecyclerView commentRecycler;

    List<cComment> comments;
    String locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comment);

        Intent intent=getIntent();
        locationName=intent.getStringExtra("locationName");

        mCommentDAO=new CommentDAO(this);
        mCommentAdapter=new CommentAdapter(this);
        comments=new ArrayList<>();
        mCommentAdapter.setmComments(comments);

        commentRecycler=(RecyclerView) findViewById(R.id.commentViewRecycler);

        commentRecycler.setAdapter(mCommentAdapter);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));


        mCommentDAO.getAllComment(locationName, new CommentDAO.DataStatus() {
            @Override
            public void DataIsLoaded(List<cComment> cmts) {
                mCommentAdapter.setmComments(cmts);
                mCommentAdapter.notifyDataSetChanged();
            }
        });
    }


}