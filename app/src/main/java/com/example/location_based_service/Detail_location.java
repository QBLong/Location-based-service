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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Detail_location extends AppCompatActivity {


    private TextView tDetail;
    private TextView tPhone;
    private TextView tEmail;
    private ScrollView sc;
    private ImageView image;


    private ImageButton bDetail;
    private ImageButton bContact;
    private ImageButton bComment;
    private ImageButton bDirect;


    ArrayList<String> urls;
    ArrayList<Bitmap> bitmaps;
    List<cComment> comments;
    TextView userInfo;

    Date date;
    String userName;
    String userEmail;

    locationImageDAO mImageDAO;
    LocationImageAdapter adapter;
    RecyclerView recyclerView;


    String locationName;

    Button readCommentButton, addCommentButton;
    double lattitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);

        bitmaps=new ArrayList<>();
        comments=new ArrayList<>();

        readCommentButton=(Button) findViewById(R.id.readComment);
        readCommentButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Detail_location.this, ReadCommentActivity.class);
                        intent.putExtra("locationName", locationName);
                        startActivity(intent);
                    }
                }
        );

        addCommentButton=(Button) findViewById(R.id.addComment);
        addCommentButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processComment();
                    }
                }
        );


        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewImage);
        // bDirect=findViewById(R.id.direct);


        adapter=new LocationImageAdapter(this, R.layout.another_item_layout);
        adapter.setmImages(bitmaps);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FloatingActionButton fab;
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processComment();
                    }
                }
        );

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


            date=(Date) intent.getSerializableExtra("date");
            userEmail= intent.getStringExtra("userEmail");
            userName=intent.getStringExtra("userName");

            String dt = intent.getStringExtra("Detail");
            String p = intent.getStringExtra("Phone");
            String e = intent.getStringExtra("Email");

            lattitude=intent.getDoubleExtra("lattitude", 0);
            longitude=intent.getDoubleExtra("longitude", 0);


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
            userInfo=(TextView) findViewById(R.id.InfoWriter);

            locationName=intent.getStringExtra("locationName");

            if(date!=null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String time="";
                time=dateFormat.format(date);
                userInfo.setText("Written by "+userName+" at "+time);
            }

            tPhone.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent phoneIntent=new Intent();
                        }
                    }
            );
        }
    }

    private void processComment() {
        Toast.makeText(
                Detail_location.this, "Clicked", Toast.LENGTH_LONG
        ).show();
        Intent intent=new Intent(Detail_location.this, AddCommentActivity.class);
        intent.putExtra("locationName", locationName);
        startActivity(intent);

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
        else if (v==bContact){
            sc.post(new Runnable() {
                @Override
                public void run() {
                    TextView a=findViewById(R.id.textContact);
                    sc.scrollTo(0, (int)a.getY());;
                }
            });
        }
        else if (v==bComment){
            sc.post(new Runnable() {
                @Override
                public void run() {
                    TextView a=findViewById(R.id.textComent);
                    sc.scrollTo(0, (int)a.getY());;
                }
            });
        }
        else{
            openMap();
        }
    }

    private void openMap() {
        Intent openMapIntent=new Intent(this, MapsActivity.class);
        openMapIntent.putExtra("latitude", lattitude);
        openMapIntent.putExtra("longitude", longitude);
        startActivity(openMapIntent);
    }
}