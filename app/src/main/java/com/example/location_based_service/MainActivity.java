package com.example.location_based_service;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView addLocation, seeLocations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignGlobalVariables();
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLocationIntent = new Intent(MainActivity.this, AddLocation.class);
                startActivity(addLocationIntent);
            }
        });

        seeLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seeLocationsIntent = new Intent(MainActivity.this, SeeLocations.class);
                startActivity(seeLocationsIntent);
            }
        });
    }

    private void assignGlobalVariables() {
        addLocation = (TextView) findViewById(R.id.add_location);
        seeLocations = (TextView) findViewById(R.id.see_locations);
    }
}



