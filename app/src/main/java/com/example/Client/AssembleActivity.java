package com.example.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class AssembleActivity extends AppCompatActivity implements Screen, OnMapReadyCallback {
    private Map _map;
    private SupportMapFragment _mapFragment;

    View marker_root_view;
    TextView tv_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble);
        _mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        _mapFragment.getMapAsync(this);
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
//        setCustomMarkerView();

        // TODO: default camera position
        LatLng position = new LatLng(3, 3);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(3));

        _map = new GoogleMapAdapter(googleMap);
        Core.getInstance().setRenderer(new MapRenderer(_map));
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }
}