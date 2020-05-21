package com.example.Client;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MatchActivity extends AppCompatActivity implements MatchScreen, OnMapReadyCallback {
    private TextView _topText;
    private SupportMapFragment _mapFragment;
    private GoogleMap _map;
    private ScreenType _currentScreenType = null;
    private FragmentManager.OnBackStackChangedListener
            _clickMapBackStack = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1){
                _map.setOnMapClickListener(null);
                getSupportFragmentManager().removeOnBackStackChangedListener(_clickMapBackStack);
            }
        }
    };

    View marker_root_view;
    TextView tv_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        _mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag);
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
                trans.add(R.id.frag, new SelectCharacterFragment());
                break;

            case GETREADY:
                trans.replace(R.id.frag, _mapFragment);
                break;

            case INGAME:
                trans.add(R.id.frag, new InGameFragment());
                break;
        }

        trans.commit();
        Core.getInstance().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        setCustomMarkerView();

        _map = googleMap;
        MapRenderer mapRenderer = new MapRenderer(new GoogleMapAdapter(googleMap,this,marker_root_view,tv_marker));
        Core.getInstance().setRenderer(mapRenderer);
        Core.getInstance().setCamera(mapRenderer);
        _currentScreenType = ScreenType.ASSEMBLE;
        Core.getInstance().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = marker_root_view.findViewById(R.id.tv_marker);
    }

    @Override
    public void setTopText(String text) {
        _topText.setText(text);
    }

    public void showDebugMap(){
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frag, _mapFragment)
            .add(R.id.frag, new DebugMapFragment())
            .addToBackStack(null)
            .commit();
    }

    public void showClickMap(ClickMapOnClickListener listener){
        _map.setOnMapClickListener(latLng -> {
            listener.onClick(latLng.latitude, latLng.longitude);
            getSupportFragmentManager()
                    .popBackStackImmediate();
        });

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frag, _mapFragment)
            .addToBackStack(null)
            .commit();

        getSupportFragmentManager()
                .addOnBackStackChangedListener(_clickMapBackStack);
    }
}