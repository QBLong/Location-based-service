package com.example.location_based_service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup recyclerview
        RecyclerView locationListView = (RecyclerView) findViewById(R.id.location_list);
        LocationListAdapter adapter=new LocationListAdapter(this);
        cLocation[] locations=createDummyVariables();
        adapter.setmLocations(locations);
        locationListView.setAdapter(adapter);
        locationListView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private cLocation[] createDummyVariables() {
        cLocation[] res=new cLocation[15];
        for(int i=0;i<5;i++){
            res[i]=new cLocation();
            String name="Notre Dame SaiGon "+String.valueOf(i);
            res[i].setmName(name);
            res[i].setmNumberOfStar(5);
            Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.notre_dame_sai_gon);
            res[i].setmImage(bmp);
        }
        for(int i=0;i<10;i++){
            res[i+5]=new cLocation();
            String name="HCMUS SaiGon "+String.valueOf(i);
            res[i+5].setmName(name);
            res[i+5].setmNumberOfStar(5);
            Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.hcmus);
            res[i+5].setmImage(bmp);
        }
        return res;
    }
}