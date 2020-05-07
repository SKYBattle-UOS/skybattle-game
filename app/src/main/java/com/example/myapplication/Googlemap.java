package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/** google 지도 마커 추가및 반경 그리기등 지도에 기능을 추가하는 클래스
 *float	HUE_AZURE 210 .0
 *  float	HUE_BLUE 240 .0
 *  float	HUE_CYAN 180 .0
 *  float	HUE_GREEN 120 .0
 *  float	HUE_MAGENTA 300 .0
 *  float	HUE_ORANGE 30 .0
 *  float	HUE_RED 0 .0
 *  float	HUE_ROSE 330 .0
 *  float	HUE_VIOLET 270 .0
 *  float	HUE_YELLOW	60 .0 marker 색상
 */
public class Googlemap extends Activity implements IMap{
    //Location location;
    //double[] locationArray;
    public Googlemap(){
    }


    public void onAddMarker(GoogleMap activityMap,double[] locationArray,float hue_color){

        LatLng position = new LatLng(37.56 , 126.97);
        System.out.println(locationArray[0]);
        //나의위치 마커
        MarkerOptions mymarker = new MarkerOptions();
                //.position(position);   //마커위치
        mymarker.position(position);
        mymarker.title("서울");
        mymarker.snippet("한국의 수도");
        mymarker.icon(BitmapDescriptorFactory.defaultMarker(hue_color));
        // 반경 1KM원

        String color_stroke="#FF0000ff";
        String color_fill="#880000ff";

        CircleOptions circle1KM = new CircleOptions()
                .center(position) //원점
                .radius(1000)      //반지름 단위 : m
                .strokeWidth(3f)  //선너비 0f : 선없음,default=10
                .strokeColor(Color.parseColor(color_stroke))
                .fillColor(Color.parseColor(color_fill));
                //.fillColor(Color.parseColor("#880000ff")); //배경색
                //.fillColor((Color.RED));

        //마커추가
        activityMap.addMarker(mymarker);
        activityMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        activityMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //원추가
        activityMap.addCircle(circle1KM);
    }

}
