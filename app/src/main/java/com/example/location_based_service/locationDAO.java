package com.example.location_based_service;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class locationDAO {
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    Activity mActivity;



    public interface DataStatus{
        void DataIsLoaded(List<cLocation> locations, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated(List<Bitmap> bitmaps);
        void DataIsDeleted();
    }

    public locationDAO(Activity activity){
        mActivity=activity;
        mDatabase=FirebaseDatabase
                .getInstance("https://database-firebase-8d08c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mReference=mDatabase.getReference("locations");
    }

    public void addLocation(cLocation location){
        mReference.child(location.mName).setValue(location);
    }

    public void readData(final DataStatus dataStatus){
       mReference.addValueEventListener(
               new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       ArrayList<cLocation> locations=new ArrayList<>();
                       List<String> keys=new ArrayList<>();
                       for(DataSnapshot keyNode: snapshot.getChildren()){
                           keys.add(keyNode.getKey());
                           cLocation location=keyNode.getValue(cLocation.class);
                           locations.add(location);
                       }
                       List<Bitmap> bitmaps=new ArrayList<>();
                        dataStatus.DataIsLoaded(locations, keys);
                       new Thread(
                               new Runnable() {
                                   @Override
                                   public void run() {

                                           for(int i=0;i<locations.size();i++){
                                               String src=locations.get(i).mUrls.get(0);
                                               try {
                                               java.net.URL url = new java.net.URL(src);
                                               HttpURLConnection connection = (HttpURLConnection) url
                                                       .openConnection();
                                               connection.setDoInput(true);
                                               connection.connect();
                                               InputStream input = connection.getInputStream();
                                               bitmaps.add(BitmapFactory.decodeStream(input));
                                                   mActivity.runOnUiThread(
                                                           new Runnable() {
                                                               @Override
                                                               public void run() {
                                                                   dataStatus.DataIsUpdated(bitmaps);
                                                               }
                                                           }
                                                   );
                                           } catch (IOException e) {
                                               e.printStackTrace();
                                           }
                                       }



                                   }
                               }
                       ).start();

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               }
       );

        /*mReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    cLocation location;
                    ArrayList<cLocation> locations=new ArrayList<>();
                    ArrayList<String> keys=new ArrayList<>();
                    DataSnapshot snapshot=task.getResult();
                    for(DataSnapshot keyNode: snapshot.getChildren()){
                        keys.add(keyNode.getKey());
                        location=keyNode.getValue(cLocation.class);
                        locations.add(location);
                    }
                    dataStatus.DataIsLoaded(locations, keys);
                }
            }
        });*/
    }
}
