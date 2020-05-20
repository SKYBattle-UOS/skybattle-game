package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MatchActivity extends AppCompatActivity implements MatchScreen, OnMapReadyCallback {
    private TextView _topText;
    private SupportMapFragment _mapFragment;
    private Map _map;
    private ScreenType _currentScreenType = null;

    View marker_root_view;
    TextView tv_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        _mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        _mapFragment.getMapAsync(this);
        _topText = findViewById(R.id.topText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_currentScreenType != null)
            Core.getInstance().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.getInstance().getUIManager().setCurrentScreen(null, _currentScreenType);
    }

    @Override
    public void switchTo(ScreenType type) {
        _currentScreenType = type;

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        switch (type){
            case CHARACTERSELECT:
                trans.add(R.id.map, new SelectCharacterFragment());
                break;

            case GETREADY:
                trans.replace(R.id.map, _mapFragment);
                break;

            case INGAME:
                trans.add(R.id.map, new InGameFragment());
                break;
        }

        trans.commit();
        Core.getInstance().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        setCustomMarkerView();

        // TODO: default camera position
        LatLng position = new LatLng(3, 3);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(3));

        _map = new GoogleMapAdapter(googleMap);
        MapRenderer mapRenderer = new MapRenderer(_map);
        Core.getInstance().setRenderer(mapRenderer);
        Core.getInstance().setCamera(mapRenderer);
        _currentScreenType = ScreenType.ASSEMBLE;
        Core.getInstance().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }

    @Override
    public void setTopText(String text) {
        _topText.setText(text);
    }
}