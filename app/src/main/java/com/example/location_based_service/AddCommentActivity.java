package com.example.location_based_service;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

public class AddCommentActivity extends AppCompatActivity {
    CommentDAO mCommentDAO;
    EditText content, star;
    Button submit;
    String locationName;
    Spinner dropdown;
    int numberOfStar=1;

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
                        // int numberOfStar=Integer.parseInt(star.getText().toString());

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
        // star=(EditText) findViewById(R.id.numberOfStar);

        Intent intent=getIntent();
        locationName=intent.getStringExtra("locationName");


        dropdown = findViewById(R.id.spinner1);
        dropdown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        numberOfStar=i+1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );

        String[] items = new String[]{"1", "2", "3", "4", "5"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }
}