package com.example.location_based_service;

import android.app.Activity;

import android.content.Context;

import android.net.Uri;


import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class locationImageDAO {
    AddLocation mParent;
    FirebaseStorage storage;
    StorageReference storageRef;
    public locationImageDAO(AddLocation parent){
        mParent=parent;
        storage=FirebaseStorage.getInstance("gs://database-firebase-8d08c.appspot.com");
        storageRef=storage.getReference();
    }

    void uploadToFirebase(Uri uri, int i, String locationName){
        String name=locationName+String.valueOf(i);
        final StorageReference ref=storageRef.child(name);
        ref.putFile(uri).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mParent.setUrls(uri.toString(), i);
                                    }
                                }
                        );
                    }
                }
        ).addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }
        );
    }
}
