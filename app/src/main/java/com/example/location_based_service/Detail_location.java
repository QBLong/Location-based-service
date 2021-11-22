package com.example.location_based_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Detail_location extends AppCompatActivity {
    private TextView tDetail;
    private TextView tPhone;
    private TextView tEmail;
    private TextView tName;
    private ScrollView sc;
    private ImageView image;
    private ImageButton bDetail;
    private ImageButton bDirect;
    private ImageButton bContact;
    private ImageButton bComment;
    private double[] Dcoop=new double[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);
        Intent intent = this.getIntent();
        if (intent != null){
            String dt = intent.getStringExtra("Detail");
            String p = intent.getStringExtra("Phone");
            String e = intent.getStringExtra("Email");
            String n = intent.getStringExtra("Name");
            Dcoop= intent.getDoubleArrayExtra("coop");
            int idImage = intent.getIntExtra("image",R.drawable.a);
            image=findViewById(R.id.mainImage);
            image.setImageResource(idImage);
            tDetail=findViewById(R.id.in4Description);
            tDetail.setText(dt);
            tName=findViewById(R.id.mainName);
            tName.setText(n);
            tPhone=findViewById(R.id.in4Phone);
            tPhone.setText(p);
            tEmail=findViewById(R.id.in4Email);
            tEmail.setText(e);
            bDetail=findViewById(R.id.detail);
            bDirect=findViewById(R.id.direct);
            bContact=findViewById(R.id.contact);
            bComment=findViewById(R.id.comment);
            sc=findViewById((R.id.scrollView));
        }
    }
    public void onClick(View v){
        if (v==bDetail){
            sc.post(new Runnable() {
                @Override
                public void run() {
                    TextView a=findViewById(R.id.textDetail);
                    sc.scrollTo(0, (int)a.getY());;
                }
            });
        }
        if (v==bContact){
            sc.post(new Runnable() {
                @Override
                public void run() {
                    TextView a=findViewById(R.id.textContact);
                    sc.scrollTo(0, (int)a.getY());;
                }
            });
        }
        if (v==bComment){
            sc.post(new Runnable() {
                @Override
                public void run() {
                    TextView a=findViewById(R.id.textComent);
                    sc.scrollTo(0, (int)a.getY());;
                }
            });
        }

        if (v==bDirect){
            Intent intent=new Intent(this, MapsActivity.class);
            intent.putExtra("latitude",Dcoop[0]);
            intent.putExtra("longitude",Dcoop[1]);
            intent.putExtra("name",tName.getText());
            intent.putExtra("description",tDetail.getText());
            image.buildDrawingCache();
            intent.putExtra("byteArray",image.getDrawingCache());
            startActivity(intent);
        }
    }
}