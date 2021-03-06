package com.example.Client;

import android.os.Bundle;
import android.view.Gravity;
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

import Common.IngameInfoListener;

public class MatchActivity extends AppCompatActivity implements Screen, OnMapReadyCallback, IngameInfoListener {
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
        ((AndroidUIManager) Core.get().getUIManager())
                .getTopText().observe(this, text -> {
                    _topText.setText(text);
                    _topText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_currentScreenType != null)
            Core.get().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.get().getUIManager().setCurrentScreen(null, _currentScreenType);
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
        if (_currentScreenType == type) return;

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

            case MAP:
                // nothing
                trans.add(R.id.frag, new DebugMapFragment());
                break;

            case INGAME:
                trans.add(R.id.frag, new InGameFragment());
                ((PlayerClient) Core.get().getMatch().getThisPlayer()).setIngameInfoListener(this);
                break;

            case GAMEOVER:
                trans.add(R.id.frag, new GameOverFragment());
                break;
        }

        trans.commit();
        Core.get().getUIManager().setCurrentScreen(this, _currentScreenType);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setCustomMarkerView();

        _map = googleMap;

        if (Core.get().getRenderer() == null){
            _adapter = new AndroidGoogleMap(googleMap, this, marker_root_view, tv_marker);
            MapRenderer mapRenderer = new MapRenderer(_adapter);
            Core.get().setRenderer(mapRenderer);
            Core.get().setCamera(mapRenderer);
            switchTo(ScreenType.MAP);
        }
        else {
            _adapter = (AndroidGoogleMap) ((MapRenderer) Core.get().getRenderer()).getMap();
            _adapter.setContext(googleMap, this, marker_root_view, tv_marker);
            _adapter.restore();
        }

        Core.get().getUIManager().setCurrentScreen(this, _currentScreenType);
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

    public void showTargetPlayers(Consumer<Integer> onButtonClickListener, Runnable after) {
        _targetPlayerAfter = after;
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.frag, new TargetPlayersFragment(integer -> {
                onButtonClickListener.accept(integer);
                getSupportFragmentManager()
                        .popBackStackImmediate();
            }))
            .addToBackStack(null)
            .commit();

        getSupportFragmentManager()
                .addOnBackStackChangedListener(_targetPlayerBackStack);
    }

    @Override
    public void onPlayerStateChange(PlayerState state) {
        runOnUiThread(this::removeSkillFragment);
    }

    private void removeSkillFragment(){
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.frag);
        if (frag instanceof TargetPlayersFragment || frag == _mapFragment){
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
}