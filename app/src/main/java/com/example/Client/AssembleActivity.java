package com.example.Client;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class AssembleActivity extends AppCompatActivity implements Screen, OnMapReadyCallback {
    public GoogleMap Mmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.getInstance().getUIManager().setCurrentScreen(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.getInstance().getUIManager().setCurrentScreen(null);
    }

    @Override
    public void switchTo(ScreenType type) {
        Intent selection_intent = new Intent(AssembleActivity.this, SelectCharacterActivity.class);
        startActivity(selection_intent);
        finish();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        setGoogleMap(googleMap);
        Renderer renderer=Core.getInstance().getRenderer();
        renderer.setMap(Mmap);
        //renderer.drawFilledCircle(googleMap,37.56,126.97,2337,1000);
    }
    public void setGoogleMap(GoogleMap map){
        Mmap=map;
    }
}