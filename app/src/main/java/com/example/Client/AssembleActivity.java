package com.example.Client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AssembleActivity extends AppCompatActivity implements Screen, OnMapReadyCallback {
    public GoogleMap Mmap;
    View marker_root_view;
    TextView tv_marker;
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
        LatLng position = new LatLng(37.56 , 126.97);
        setCustomMarkerView();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12)); //map 초기화
        setGoogleMap(googleMap);
        Renderer renderer=Core.getInstance().getRenderer();
        renderer.setMap(Mmap);
        renderer.getActivity(this);
        addMarker(googleMap);
        //renderer.drawFilledCircle(this,googleMap,37.56,126.97,2337,1000);
        //renderer.drawFilledCircle(googleMap,37.56,126.97,2337,1000);
    }
    public void setGoogleMap(GoogleMap map){
        Mmap=map;
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }


    private void addMarker(GoogleMap googleMap) {


        LatLng position = new LatLng(37.56, 126.97);
        int price=100;
        String formatted = NumberFormat.getCurrencyInstance().format((price));

        tv_marker.setText(formatted);
        tv_marker.setBackgroundResource(R.drawable.marker_mask);
        tv_marker.setTextColor(Color.WHITE);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(Integer.toString(price));
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));
        Marker marker=googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        /*
        if(marker == null){
            marker = googleMap.addMarker(markerOptions);
        }
        else {
            LatLng position_ = new LatLng(37.66,126.77);
            marker.setPosition(position_);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position_));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }*/

        //marker.setPosition(new LatLng(37.56+10,126.97+10));
        //marker.setVisible(true);

    }


    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }





}