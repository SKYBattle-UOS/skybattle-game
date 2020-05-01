package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AssembleActivity extends AppCompatActivity implements GameState, OnMapReadyCallback {
    public GoogleMap mMap;
    public Googlemap googlemap_fun=new Googlemap();
    Location location;
    double[] locationArray;
    @Override
    public void update(int ms) {
        // TODO: DEBUG EDIT
        Log.d("Stub", "MatchStateAssemble: Showing assemble screen");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btn_pointer = (Button) findViewById(R.id.btn_pointer);
        btn_pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selection_intent = new Intent(AssembleActivity.this, SelectCharacterActivity.class);
                startActivity(selection_intent);
                finish();
            }
        });
        Log.i("Stub", "AssembleActivity: created");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location location=new Location(this);
        double[] locationArray=location.getCurrentLocation();
        LatLng position = new LatLng(locationArray[0], locationArray[1]);
        Log.i("Stub",
                String.format(
                        "Check now Location: latitude : %f, longitude : %f",
                        locationArray[0], locationArray[1]));
        googlemap_fun.onAddMarker(mMap,locationArray);
    }

}