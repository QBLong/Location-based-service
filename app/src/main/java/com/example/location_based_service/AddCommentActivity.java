package com.example.location_based_service;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddCommentActivity extends AppCompatActivity {
    CommentDAO mCommentDAO;
    EditText content, star;
    Button submit;
    String locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        assignGlobalVariables();
        submit.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
                        int numberOfStar=Integer.parseInt(star.getText().toString());
                        if(numberOfStar>5 || numberOfStar<0) {
                            Toast.makeText(
                                    AddCommentActivity.this, R.id.numberOfStar, Toast.LENGTH_LONG
                            ).show();
                            return;
                        }
                        cComment comment=new cComment();
                        comment.setmLocation(locationName);
                        comment.setmContent(content.getText().toString());
                        comment.setDate(new Date());
                        comment.setmUserName(MyGlobal.userName);
                        comment.setmUserEmail(MyGlobal.userEmail);
                        comment.setmNumberOfStar(numberOfStar);

                        mCommentDAO.addComment(comment);

                        finish();
                    }
                }
        );
    }

    private void assignGlobalVariables() {
        content=(EditText) findViewById(R.id.contentComment);
        mCommentDAO=new CommentDAO(this);
        submit=(Button) findViewById(R.id.submitContent);
        star=(EditText) findViewById(R.id.numberOfStar);

        Intent intent=getIntent();
        locationName=intent.getStringExtra("locationName");
    }
}