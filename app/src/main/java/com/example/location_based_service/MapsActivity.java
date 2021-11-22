package com.example.location_based_service;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

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
import com.example.location_based_service.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener {

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/

        // mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        Intent intent = getIntent();
        destLatLng = new LatLng(intent.getDoubleExtra("latitude", 0),
                intent.getDoubleExtra("longitude", 0));

        findYourPosition();


    }



    @Override
    public void onMapClick(LatLng latLng) {
        String doWhat = getIntent().getStringExtra("do");
        System.out.println(doWhat);
        if(doWhat!=null && doWhat.equals("choosePos")){
            Intent replyIntent=new Intent(this, AddLocation.class);
            replyIntent.putExtra("lat", latLng.latitude);
            replyIntent.putExtra("long", latLng.longitude);
            setResult(Activity.RESULT_OK, replyIntent);
            finish();
            return;
        }

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
            mMap.setMyLocationEnabled(true);
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
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
                startTracking();
            }
        }

        // Permission granted

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
                            startLatLng=myPosition;

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(startLatLng)
                                    .title("My position")
                                    .visible(true)
                                    ;
                            CameraPosition point = new CameraPosition.Builder()
                                    .target(startLatLng)
                                    .zoom(15)
                                    .bearing(90)
                                    .tilt(30)
                                    .build();
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(point));
                            mapDirectionHelper = new MapDirectionHelper(mMap, getApplicationContext());
                            mapDirectionHelper.startDirection(startLatLng, destLatLng);

                            /*mapFragment.getMapAsync(new OnMapReadyCallback() {
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
                            });*/
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

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String doWhat = getIntent().getStringExtra("do");
        System.out.println(doWhat);
        if(doWhat!=null && doWhat.equals("choosePos")){
            Intent replyIntent=new Intent(this, AddLocation.class);
            replyIntent.putExtra("lat", marker.getPosition().latitude);
            replyIntent.putExtra("long", marker.getPosition().longitude);
            setResult(Activity.RESULT_OK, replyIntent);
            finish();
            return false;
        }
        return false;


    }
}