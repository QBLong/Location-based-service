package com.example.location_based_service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    private DatabaseReference mDatabase;
    Activity mActivity;
    int currentStar;
    int currentVote;
    public CommentDAO(Activity activity){
        mActivity=activity;
        mDatabase= FirebaseDatabase.getInstance("https://database-firebase-8d08c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    public void getStar(cComment comment){
        mDatabase.child("locations").child(comment.getmLocation()).child("mNumberOfStar").get().addOnSuccessListener(
                new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        currentStar=dataSnapshot.getValue(Integer.class);
                        getVote(comment);
                    }
                }
        );
    }

    public void getVote(cComment comment){
        mDatabase.child("locations").child(comment.getmLocation()).child("mNumberOfVote").get().addOnSuccessListener(
                new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        currentVote=dataSnapshot.getValue(Integer.class);
                        mDatabase.child("location_comments").child(comment.getmLocation()).child(comment.getId()).setValue(comment);
                        mDatabase.child("locations").child(comment.getmLocation()).child("mNumberOfStar").setValue(comment.getmNumberOfStar()+currentStar);
                        mDatabase.child("locations").child(comment.getmLocation()).child("mNumberOfVote").setValue(1+currentVote);
                    }
                }
        );
    }

    public void addComment(cComment comment){
        comment.setId(mDatabase.child("location_comments").child(comment.getmLocation()).push().getKey());
        getStar(comment);

    }

    public interface DataStatus{
        void DataIsLoaded(List<cComment> comments);
    }

    public void getAllComment(String locationName, DataStatus dataStatus){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.child("location_comments").child(locationName).addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList<cComment> comments=new ArrayList<>();
                                        List<String> keys=new ArrayList<>();
                                        for(DataSnapshot keyNode: snapshot.getChildren()){
                                            keys.add(keyNode.getKey());
                                            cComment comment=keyNode.getValue(cComment.class);
                                            comments.add(comment);
                                        }
                                        mActivity.runOnUiThread(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        System.out.println("called here");
                                                        dataStatus.DataIsLoaded(comments);
                                                    }
                                                }
                                        );
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }
                        );
                    }
                }
        ).start();
    }
}
