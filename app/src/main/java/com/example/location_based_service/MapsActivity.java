package com.example.location_based_service;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.location_based_service.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private MapDirectionHelper mapDirectionHelper;

    private LatLng startLatLng;
    private LatLng destLatLng;
    private LatLng myPosition = null;
    private Location lastLocation;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        destLatLng = new LatLng(intent.getDoubleExtra("latitude", 0),
                intent.getDoubleExtra("longitude", 0));

        findYourPosition();

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (startLatLng == null) {
            startLatLng = latLng;
            Toast.makeText(this,
                    "start:" + latLng.toString(), Toast.LENGTH_SHORT).show();
        } else {
            destLatLng = latLng;
            Toast.makeText(this,
                    "dest:" + latLng.toString(), Toast.LENGTH_SHORT).show();
            mapDirectionHelper.startDirection(startLatLng, destLatLng);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        startLatLng = null;
        destLatLng = null;
        mapDirectionHelper.clearDirectionResult();
        Toast.makeText(this,
                "clear start+dest",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void findYourPosition() {
        if(checkLocationPermission()){
            // Permission granted
            startTracking();
        }
        requestPermission();
    }


    final int REQUEST_PERMISSION_LOCATION=1;
    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET},
                REQUEST_PERMISSION_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION_LOCATION){
            for(int result: grantResults){
                if(result!=PackageManager.PERMISSION_GRANTED){
                    return;
                }
            }
        }

        // Permission granted
        startTracking();
    }

    @SuppressLint("MissingPermission")
    private void startTracking() {
        FusedLocationProviderClient flpClient= LocationServices.getFusedLocationProviderClient(this);
        flpClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            // Biến location là chứa địa điểm ấy
                            // gọi location.getLattitude(), location.getLongitude()
                            lastLocation = location;
                            myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    mMap = googleMap;
                                    startLatLng = myPosition;
                                    System.out.println("This is new startLatLng: " + startLatLng);
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(startLatLng)
                                            .title("My position")
                                            .visible(true)
                                            ;
                                    mMap.addMarker(markerOptions);
                                    CameraPosition point = new CameraPosition.Builder()
                                            .target(startLatLng)
                                            .zoom(15)
                                            .bearing(90)
                                            .tilt(30)
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(point));
                                    mapDirectionHelper = new MapDirectionHelper(mMap, getApplicationContext());
                                }
                            });
                        }

                    }
                }
        );
        flpClient.getLastLocation().addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }
        );
    }

    private boolean checkLocationPermission() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED;
    }

}