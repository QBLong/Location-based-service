package com.example.location_based_service;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class locationDAO {
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private List<cLocation> locations=new ArrayList<cLocation>();

    public interface DataStatus{
        void DataIsLoaded(List<cLocation> locations, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public locationDAO(){
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
                       locations.clear();
                       List<String> keys=new ArrayList<>();
                       for(DataSnapshot keyNode: snapshot.getChildren()){
                           keys.add(keyNode.getKey());
                           cLocation location=keyNode.getValue(cLocation.class);
                           locations.add(location);
                       }
                        dataStatus.DataIsLoaded(locations, keys);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               }
       );
    }
}
