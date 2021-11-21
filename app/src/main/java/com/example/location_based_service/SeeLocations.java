package com.example.location_based_service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class SeeLocations extends AppCompatActivity {
    private locationDAO mDAO;
    private RecyclerView recyclerView;
    LocationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_locations);

        generateGlobalVariables();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        readData();
                    }
                }
        ).start();


    }

    private void generateGlobalVariables() {
        mDAO=new locationDAO();
        recyclerView=(RecyclerView) findViewById(R.id.location_list);
        adapter=new LocationListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void readData() {
        new locationDAO().readData(
                new locationDAO.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<cLocation> locations, List<String> keys) {
                        adapter.setmLocations(locations);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                }
        );
    }


}