package com.example.location_based_service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    protected int[] CimageId={R.drawable.a, R.drawable.b, R.drawable.c};
    protected String[] Cname={"Saigon University", "NOWZONE Fashion Mall", "Pizza 4P's Vo Van Kiet"};
    protected double[][] Ccoop={{10.7596667, 106.6807954}, {10.7596667, 106.6807954}, {10.7596667, 106.6807954}};
    protected String[] Cdetail={"hello","Supp","Let's Catchup"};
    protected String[] Cemail={"8:45 pm","9:00 am","7:34 pm"};
    protected String[] CphoneNo= {"7656610000","9999043232","7834354323"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cdetail[0]=getString(R.string.detail);
        // Setup recyclerview
        RecyclerView locationListView = (RecyclerView) findViewById(R.id.location_list);
        LocationListAdapter adapter=new LocationListAdapter(this);
        cLocation[] locations=createDummyVariables();
        adapter.setmLocations(locations);
        locationListView.setAdapter(adapter);
        locationListView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private cLocation[] createDummyVariables() {
        cLocation[] res=new cLocation[3];
        for(int i=0;i<3;i++){
            res[i]=new cLocation();
            String name=Cname[i];
            res[i].setmName(name);
            res[i].setmEmail(Cemail[i]);
            res[i].setmImageID(CimageId[i]);
            res[i].setmPhone(CphoneNo[i]);
            res[i].setmDetail(Cdetail[i]);
            res[i].setmNumberOfStar(5);
            res[i].setmCoop(Ccoop[i]);
            Bitmap bmp= BitmapFactory.decodeResource(getResources(), CimageId[i]);
            res[i].setmImage(bmp);
        }
        return res;
    }

}