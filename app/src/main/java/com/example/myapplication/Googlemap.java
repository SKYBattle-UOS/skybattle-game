package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Googlemap  extends Activity implements IMap{
    //Location location;
    //double[] locationArray;
    public Googlemap(){
    }

    public void onAddMarker(GoogleMap activityMap,double[] locationArray){

        LatLng position = new LatLng(37.56 , 126.97);
        System.out.println(locationArray[0]);
        //나의위치 마커
        MarkerOptions mymarker = new MarkerOptions()
                .position(position);   //마커위치
        mymarker.position(position);
        mymarker.title("서울");
        mymarker.snippet("한국의 수도");

        // 반경 1KM원
        CircleOptions circle1KM = new CircleOptions().center(position) //원점
                .radius(1000)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        //마커추가
        activityMap.addMarker(mymarker);
        activityMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        activityMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //원추가
        activityMap.addCircle(circle1KM);
    }

}
