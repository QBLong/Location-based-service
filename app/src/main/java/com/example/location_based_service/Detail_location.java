package com.example.location_based_service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class Detail_location extends AppCompatActivity {


    private TextView tDetail;
    private TextView tPhone;
    private TextView tEmail;
    private ScrollView sc;
    private ImageView image;
    private ImageButton bDetail;
    private ImageButton bContact;
    private ImageButton bComment;
    ArrayList<String> urls;
    ArrayList<Bitmap> bitmaps;

    locationImageDAO mImageDAO;
    LocationImageAdapter adapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);

        bitmaps=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewImage);
        adapter=new LocationImageAdapter(this, R.layout.another_item_layout);
        adapter.setmImages(bitmaps);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        Intent intent = this.getIntent();
        if (intent != null){
            mImageDAO=new locationImageDAO(null, this);
            urls=intent.getStringArrayListExtra("urls");
            for(String url: urls){
                mImageDAO.downloadFile(url, new locationImageDAO.ImageStatus() {
                    @Override
                    public void ImageIsLoaded(Bitmap bmp) {
                        bitmaps.add(bmp);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            String dt = intent.getStringExtra("Detail");
            String p = intent.getStringExtra("Phone");
            String e = intent.getStringExtra("Email");
            int idImage = intent.getIntExtra("image",R.drawable.a);
            // image=findViewById(R.id.mainImage);
            // image.setImageResource(idImage);
            tDetail=findViewById(R.id.in4Description);
            tDetail.setText(dt);
            tPhone=findViewById(R.id.in4Phone);
            tPhone.setText(p);
            tPhone.setFocusable(true);
            tEmail=findViewById(R.id.in4Email);
            tEmail.setText(e);
            bDetail=findViewById(R.id.detail);
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
    }
}