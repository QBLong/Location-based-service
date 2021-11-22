package com.example.location_based_service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
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
    private int markerclicked = 0;
    private String locationName;
    private String locationDescription;
    private ImageView locationImage;
    private Bitmap _imv;

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
        if (intent != null && intent.getExtras() != null) {
            double lat = intent.getExtras().getDouble("latitude", 0);
            double lng = intent.getExtras().getDouble("longitude", 0);
            destLatLng = new LatLng(lat, lng);
            locationName = intent.getExtras().getString("name", "");
            locationDescription = intent.getExtras().getString("description", "");
            if (intent.hasExtra("byteArray")) {
                _imv = BitmapFactory.decodeByteArray(
                        intent.getByteArrayExtra("byteArray"),
                        0,
                        intent.getByteArrayExtra("byteArray").length
                );
                locationImage.setImageBitmap(_imv);
            }
        }

        findYourPosition();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.infowindowlayout, null);

                LatLng latLng = marker.getPosition();

                ImageView im = (ImageView) v.findViewById(R.id.imageView1);
                TextView tv1 = (TextView) v.findViewById(R.id.textView1);
                TextView tv2 = (TextView) v.findViewById(R.id.textView2);
                String title = marker.getTitle();
                String informations = marker.getSnippet();

                tv1.setText(title);
                tv2.setText(informations);

//                if(onMarkerClick(marker)==true && markerclicked==1){
//                    im.setImageResource(R.drawable.common_full_open_on_phone);
//                }


                return v;
            }

            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }
        });

    }

    private boolean onMarkerClick(Marker marker) {
        if (marker.equals(myPosition))
        {
            markerclicked=1;
            return true;
        }
        return false;
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