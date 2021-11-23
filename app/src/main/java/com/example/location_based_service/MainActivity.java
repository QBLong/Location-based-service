package com.example.location_based_service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Authenticator;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 11;
    Button addLocation, seeLocations;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignGlobalVariables();

        // Nếu thầy sử dụng máy ảo và không login vào được thì un comment 2 dòng này
        // Máy android thật thì vô như bình thường
        // Cái cơ chế authentication của Firebase em không hiểu lắm

        // MyGlobal.userEmail="Minhbaodinh0808@gmail.com";
        // MyGlobal.userName="Minh Bao Dinh";

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyGlobal.userEmail==null){
                    Toast.makeText(
                            MainActivity.this, "Please sign in first", Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                Intent addLocationIntent = new Intent(MainActivity.this, AddLocation.class);
                addLocationIntent.putExtra("do", "choosePos");
                startActivity(addLocationIntent);
            }
        });

        seeLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyGlobal.userEmail==null){
                    Toast.makeText(
                            MainActivity.this, "Please sign in first", Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                Intent seeLocationsIntent = new Intent(MainActivity.this, SeeLocations.class);
                startActivity(seeLocationsIntent);
            }
        });

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signin();
                    }
                }
        );

    }

    private void signin() {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void assignGlobalVariables() {
        addLocation = (Button) findViewById(R.id.add_location);
        seeLocations = (Button) findViewById(R.id.see_locations);

        signInButton=(SignInButton) findViewById(R.id.sign_in_button);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSigninResult(result);
        }
    }

    private void handleSigninResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount acct=result.getSignInAccount();

            MyGlobal.userName=acct.getDisplayName();
            MyGlobal.userEmail=acct.getEmail();

            Toast.makeText(
                    this, acct.getDisplayName(), Toast.LENGTH_LONG
            ).show();
        }
    }
}



