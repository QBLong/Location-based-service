package com.example.location_based_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Detail_location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);
        Intent intent = this.getIntent();
        if (intent != null){
            String dt = intent.getStringExtra("Detail");
            String p = intent.getStringExtra("Phone");
            String e = intent.getStringExtra("Email");
            int idImage = intent.getIntExtra("image",R.drawable.a);
            ImageView image=findViewById(R.id.mainImage);
            image.setImageResource(idImage);
            TextView tDetail=findViewById(R.id.in4Description);
            tDetail.setText(dt);
            TextView tPhone=findViewById(R.id.in4Phone);
            tPhone.setText(p);
            TextView tEmail=findViewById(R.id.in4Email);
            tEmail.setText(e);
        }
    }
}