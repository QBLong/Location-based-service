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

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class AddLocation extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 2;
    Button addMoreImage, submit;
    RecyclerView displayImagesView;
    ArrayList<Bitmap> images;
    LocationImageAdapter adapter;
    TextView locationName;
    EditText comment;
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
        position=new LatLng(10.765024016757133, 106.68225066700836);

        addMoreImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getImage();
                    }
                }
        );

        adapter=new LocationImageAdapter(this);
        adapter.setmImages(images);
        displayImagesView.setAdapter(adapter);
        displayImagesView.setLayoutManager(new GridLayoutManager(this, 2));

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        for(int i=0;i<images.size();i++){
                            mImageDAO.uploadToFirebase(uris.get(i), i, locationName.getText().toString());
                        }
                    }
                }
        );
    }

    public void setUrls(String url, int i){
        urls.add(url);
        if(urls.size()==images.size()){
            submitToDatabase();
        }
    }

    private void submitToDatabase() {
        cLocation location=new cLocation();
        location.setmName(locationName.getText().toString());
        location.setmNumberOfStar(5);
        location.setComment(comment.getText().toString());
        location.setLattitude(position.latitude);
        location.setLongitude(position.longitude);
        location.setmUrls(urls);
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImage2();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
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
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    images.add(bitmap);
                    uris.add(imageUri);
                    adapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void assignGlobalVariables() {
        displayImagesView=(RecyclerView) findViewById(R.id.displayImagesView);
        addMoreImage=(Button) findViewById(R.id.add_more_image);
        images=new ArrayList<>();
        submit=(Button) findViewById(R.id.submit);
        locationName=(TextView)findViewById(R.id.location_name);
        comment=(EditText)findViewById(R.id.text_comment);
        uris=new ArrayList<>();
        urls=new ArrayList<>();

        mDAO=new locationDAO();
        mImageDAO=new locationImageDAO(this);
    }




}