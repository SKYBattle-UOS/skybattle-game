package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.function.Consumer;

public class MatchActivity extends AppCompatActivity implements Screen, OnMapReadyCallback {
    private View marker_root_view;
    private TextView _topText;
    private TextView tv_marker;
    private GoogleMap _map;
    private AndroidGoogleMap _adapter;
    private SupportMapFragment _mapFragment;
    private ScreenType _currentScreenType;

    private Runnable _clickMapAfter;
    private FragmentManager.OnBackStackChangedListener
            _clickMapBackStack = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1){
                _clickMapAfter.run();
                _map.setOnMapClickListener(null);
                getSupportFragmentManager()
                        .removeOnBackStackChangedListener(_clickMapBackStack);
            }
        }
    };

    private Runnable _targetPlayerAfter;
    private FragmentManager.OnBackStackChangedListener
            _targetPlayerBackStack = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1){
                _targetPlayerAfter.run();
                getSupportFragmentManager()
                        .removeOnBackStackChangedListener(_targetPlayerBackStack);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        _mapFragment = new SupportMapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag, _mapFragment)
                .commit();
        _mapFragment.getMapAsync(this);
        _topText = findViewById(R.id.topText);
        ((AndroidUIManager) Core.getInstance().getUIManager())
                .getTopText().observe(this, text -> _topText.setText(text));
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        _adapter.save();
        outState.putInt("screenType", _currentScreenType.ordinal());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ScreenType stype = ScreenType.values()[savedInstanceState.getInt("screenType")];
        switchTo(stype);
    }

    @Override
    public void switchTo(ScreenType type) {
        _currentScreenType = type;

        Fragment currentFragmnet = getSupportFragmentManager()
                .findFragmentById(R.id.frag);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        if (currentFragmnet != _mapFragment)
            trans.remove(currentFragmnet);

        switch (type){
            case CHARACTERSELECT:
                trans.add(R.id.frag, new SelectCharacterFragment());
                break;

            case GETREADY:
                // nothing
                break;

            case INGAME:
                trans.add(R.id.frag, new InGameFragment());
                break;

            case DEATH:
                // nothing
                trans.add(R.id.frag, new DebugMapFragment());
                break;
        }

        trans.commit();
        Core.getInstance().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setCustomMarkerView();

        _map = googleMap;

        if (Core.getInstance().getRenderer() == null){
            _adapter = new AndroidGoogleMap(googleMap, this, marker_root_view, tv_marker);
            MapRenderer mapRenderer = new MapRenderer(_adapter);
            Core.getInstance().setRenderer(mapRenderer);
            Core.getInstance().setCamera(mapRenderer);
            _currentScreenType = ScreenType.ASSEMBLE;
        }
        else {
            _adapter = (AndroidGoogleMap) ((MapRenderer) Core.getInstance().getRenderer()).getMap();
            _adapter.setContext(googleMap, this, marker_root_view, tv_marker);
            _adapter.restore();
        }

        Core.getInstance().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = marker_root_view.findViewById(R.id.tv_marker);
    }

    public void showDebugMap(){
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frag, _mapFragment)
            .add(R.id.frag, new DebugMapFragment())
            .addToBackStack(null)
            .commit();
    }

    public void showClickMap(ClickMapOnClickListener listener, Runnable after){
        _clickMapAfter = after;
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

    public void showTargetPlayers(Consumer<Integer> onButtonClick, Runnable after) {
        _targetPlayerAfter = after;
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.frag, new TargetPlayersFragment(integer -> {
                onButtonClick.accept(integer);
                getSupportFragmentManager()
                        .popBackStackImmediate();
            }))
            .addToBackStack(null)
            .commit();

        getSupportFragmentManager()
                .addOnBackStackChangedListener(_targetPlayerBackStack);
    }
}