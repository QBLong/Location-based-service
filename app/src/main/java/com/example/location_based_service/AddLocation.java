package com.example.location_based_service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class AddLocation extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 2;
    private static final int GET_LATLNG = 3;
    Button addMoreImage, submit, choosePos;
    RecyclerView displayImagesView;
    LocationImageAdapter adapter;
    TextView locationName, latlngView;
    EditText comment, phone, email;
    LatLng position;
    ArrayList<Uri> uris;
    public ArrayList<String>urls;

    locationDAO mDAO;
    locationImageDAO mImageDAO;

    final static int REQUEST_GET_IMAGE=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        assignGlobalVariables();
        choosePos.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent choosePosIntent=new Intent(AddLocation.this, MapsActivity.class);
                        choosePosIntent.putExtra("do", "choosePos");
                        startActivityForResult(choosePosIntent, GET_LATLNG);
                    }
                }
        );

        addMoreImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getImage();
                    }
                }
        );

        adapter=new LocationImageAdapter(this, R.layout.item_image_layout);
        adapter.setmUriImages(uris);
        displayImagesView.setAdapter(adapter);
        displayImagesView.setLayoutManager(new GridLayoutManager(this, 2));

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(check()){
                            for(int i=0;i<uris.size();i++){
                                mImageDAO.uploadToFirebase(uris.get(i), i, locationName.getText().toString());
                            }
                        }
                    }
                }
        );
    }

    private boolean check() {
        System.out.println("called");
        String name=locationName.getText().toString();
        if(name.length()==0){
            System.out.println("name");
            Toast.makeText(
                    AddLocation.this, "Please enter location name", Toast.LENGTH_LONG
            ).show();
            return false;
        }
        String commentt=comment.getText().toString();
        if(commentt.length()==0){
            Toast.makeText(
                    AddLocation.this, "Please enter your review", Toast.LENGTH_LONG
            ).show();
            return false;
        }
        if(position==null){
            Toast.makeText(
                    AddLocation.this, "Please mark your location", Toast.LENGTH_LONG
            ).show();
            return false;
        }
        if(uris.size()==0){
            Toast.makeText(
                    AddLocation.this, "Must have at least 1 image", Toast.LENGTH_LONG
            ).show();
            return false;
        }
        return true;
    }

    public void setUrls(String url, int i){
        urls.add(url);
        if(urls.size()==uris.size()){
            submitToDatabase();
        }
    }



    private void submitToDatabase() {
        cLocation location=new cLocation();
        location.setmName(locationName.getText().toString());
        location.setmNumberOfStar(5);
        location.setmDetail(comment.getText().toString());
        location.setLattitude(position.latitude);
        location.setLongitude(position.longitude);
        location.setmUrls(urls);
        location.setmPhone(phone.getText().toString());
        location.setmEmail(email.getText().toString());

        mDAO.addLocation(location);
        finish();
    }

    private void getImage() {
        try {
            if (ActivityCompat.checkSelfPermission(AddLocation.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddLocation.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                getImage2();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getImage2(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImage2();
                } else {

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_FROM_GALLERY && resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            if(imageUri!=null){
                    uris.add(imageUri);
                    adapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==GET_LATLNG){
            double lattitude=data.getDoubleExtra("lat", 0);
            double longitude=data.getDoubleExtra("long", 0);
            String text=String.valueOf(lattitude)+", "+String.valueOf(longitude);
            position=new LatLng(lattitude, longitude);
            latlngView.setText(text);
        }
    }

    private void assignGlobalVariables() {
        displayImagesView=(RecyclerView) findViewById(R.id.displayImagesView);
        addMoreImage=(Button) findViewById(R.id.add_more_image);
        submit=(Button) findViewById(R.id.submit);
        locationName=(TextView)findViewById(R.id.location_name);
        comment=(EditText)findViewById(R.id.text_comment);
        uris=new ArrayList<>();
        urls=new ArrayList<>();
        choosePos=(Button) findViewById(R.id.choosePos);
        position=null;
        phone=(EditText) findViewById(R.id.phone);
        email=(EditText) findViewById(R.id.email);

        mDAO=new locationDAO(this);
        mImageDAO=new locationImageDAO(this, this);

        latlngView=(TextView) findViewById(R.id.latlng);
    }




}