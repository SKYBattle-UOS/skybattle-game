package com.example.Client;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

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
public class GoogleMapAdapter extends Activity implements IMap {
    //Location location;
    //double[] locationArray;
    float hue_color= (float) 0.0; //default
    public GoogleMapAdapter(){
    }


    public void onAddMarker(GoogleMap activityMap, double latitude, double longitude, int color, float size ){

        LatLng position = new LatLng(37.56 , 126.97);

        Marker marker = activityMap.addMarker(new MarkerOptions()
                .position(position)
                .title("한국의 수도")
                .icon(BitmapDescriptorFactory.defaultMarker(hue_color)));

        marker.showInfoWindow();
        // 반경 1KM원

        String color_stroke="#FF0000ff";
        String color_fill="#880000ff";

        CircleOptions circle1KM = new CircleOptions()
                .center(position) //원점
                .radius(1000)      //반지름 단위 : 1000m
                .strokeWidth(3f)  //선너비 0f : 선없음,default=10
                .strokeColor(Color.parseColor(color_stroke))
                .fillColor(Color.parseColor(color_fill));
                //.fillColor(Color.parseColor("#880000ff")); //배경색
                //.fillColor((Color.RED));

        //마커추가

        activityMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        activityMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //원추가
        activityMap.addCircle(circle1KM);
    }
    public void onAddMarker(Context mContext, GoogleMap activityMap, double latitude, double longitude, int color, float size ){

        Bitmap drawableBitmap = getBitmap(mContext,R.drawable.ic_arrow_drop_down_black_24dp);

        LatLng position = new LatLng(37.56 , 126.97);
        //System.out.println(locationArray[0]);
        //나의위치 마커

        Marker marker = activityMap.addMarker(new MarkerOptions()
                .position(position)
                .title("한국의 수도")
                .icon(BitmapDescriptorFactory.fromBitmap(drawableBitmap)));

        marker.showInfoWindow();



        //마커추가

        activityMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        activityMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    private Bitmap getBitmap(Context mContext,int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }



}
