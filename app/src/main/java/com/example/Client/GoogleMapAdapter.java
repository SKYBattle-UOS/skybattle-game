package com.example.Client;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.util.ArrayList;

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
public class GoogleMapAdapter implements Map {
    private final float hue_color = (float) 0.0; //default

    private GoogleMap _googleMap;
    private Handler _mainHandler;
    private ArrayList<Marker> _markers;
    private Context _matchActivity;
    private View _marker_root_view; //text 띄우기 위해
    private TextView _tv_marker; //textview

    public GoogleMapAdapter(GoogleMap googleMap, Context mContext, View marker_root_view, TextView tv_marker) {
        _googleMap = googleMap;
        _mainHandler = new Handler(Looper.getMainLooper());
        _markers = new ArrayList<>();
        _matchActivity = mContext;
        _marker_root_view = marker_root_view;
        _tv_marker = tv_marker;
    }

    @Override
    public synchronized MapMarkerHandle addMarker(double latitude, double longitude, int color, float size) {
        GoogleMarkerHandle ret = new GoogleMarkerHandle(_markers.size());
        _mainHandler.post(() -> _addMarker(latitude, longitude, color, size));
        return ret;
    }

    @Override
    public void setMarkerPosition(MapMarkerHandle marker, double lat, double lon) {
        _mainHandler.post(() -> _setMarkerPosition(marker, lat, lon));
    }

    @Override
    public void removeMarker(MapMarkerHandle marker) {
        _mainHandler.post(() -> _removeMarker(marker));
    }

    private synchronized void _setMarkerPosition(MapMarkerHandle marker, double lat, double lon) {

        int index = ((GoogleMarkerHandle) marker).index;
        Marker cur_marker = _markers.get(index);
        cur_marker.setPosition(new LatLng(lat, lon));
        cur_marker.showInfoWindow();
    }

    @Override
    public void moveCamera(double latitude, double longitude) {
        _mainHandler.post(() -> _moveCamera(latitude, longitude));
    }

    private synchronized void _addMarker(double latitude, double longitude, int color, float size) {
//        LatLng position = new LatLng(37.56 , 126.97);
        /*
        LatLng position = new LatLng(latitude, longitude);

        Marker marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(hue_color)));

        marker.showInfoWindow();
        _markers.add(marker);*/
        //_drawImage(latitude,longitude);
        //_drawText(latitude, longitude);
        _drawText2(latitude,longitude);
    }

    private void _moveCamera(double lat, double lon) {
        _googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
    }

    private synchronized void _removeMarker(MapMarkerHandle marker) {
        int index = ((GoogleMarkerHandle) marker).index;
        Marker cur_marker = _markers.get(index);
        _markers.remove(index);
        cur_marker.remove();
    }

    private synchronized void _drawText(double latitude, double longitude) {//textview를 marker로 띄움
        LatLng position = new LatLng(37.56, 126.97);
        int price=100;
        String formatted = NumberFormat.getCurrencyInstance().format((price));

        _tv_marker.setText(formatted);
        _tv_marker.setBackgroundResource(R.drawable.marker_mask);
        _tv_marker.setTextColor(Color.WHITE);

        Marker marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                //.title(Integer.toString(price))
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(_matchActivity, _marker_root_view))));

        marker.showInfoWindow();
        _markers.add(marker);
    }

    private synchronized void _drawText2(double latitude, double longitude) { //marker를 custom marker로 바꿔줌
        LatLng position = new LatLng(37.56, 126.97);
        Bitmap custom_marker=getBitmap(_matchActivity,R.drawable.ic_priority_high_black_24dp);

        Marker marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title("신난다")
                .icon(BitmapDescriptorFactory.fromBitmap(custom_marker)));

        marker.showInfoWindow();
        _markers.add(marker);
    }

    private synchronized void _drawImage(double latitude, double longitude) { //marker를 image로 만듬
        LatLng position = new LatLng(latitude, longitude);
        //Bitmap resized=getResizedBitmap(_matchActivity.getResources(),R.drawable.hp_portion,30,30,30); size 줄이기 위해

        Marker marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hp_portion)));
        //.icon(BitmapDescriptorFactory.fromBitmap(resized)));

        marker.showInfoWindow();
        _markers.add(marker);
    }

    private void _animateCamera() {
        _googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void _addCircle() {
        // 반경 1KM원
        LatLng position = new LatLng(37.56, 126.97);
        String color_stroke = "#FF0000ff";
        String color_fill = "#880000ff";

        CircleOptions circle1KM = new CircleOptions()
                .center(position) //원점
                .radius(1000)      //반지름 단위 : 1000m
                .strokeWidth(3f)  //선너비 0f : 선없음,default=10
                .strokeColor(Color.parseColor(color_stroke))
                .fillColor(Color.parseColor(color_fill));
        //.fillColor(Color.parseColor("#880000ff")); //배경색
        //.fillColor((Color.RED));

        //원추가
        _googleMap.addCircle(circle1KM);
    }

    public static Bitmap getResizedBitmap(Resources resources, int id, int size, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap src = BitmapFactory.decodeResource(resources, id, options);
        Bitmap resized = Bitmap.createScaledBitmap(src, width, height, true);
        return resized;
    }


//    public void addMarker(Context mContext, GoogleMap activityMap, double latitude, double longitude, int color, float size ){
//
//        Bitmap drawableBitmap = getBitmap(mContext,R.drawable.ic_arrow_drop_down_black_24dp);
//
//        LatLng position = new LatLng(37.56 , 126.97);
//        //System.out.println(locationArray[0]);
//        //나의위치 마커
//
//        Marker marker = activityMap.addMarker(new MarkerOptions()
//                .position(position)
//                .title("한국의 수도")
//                .icon(BitmapDescriptorFactory.fromBitmap(drawableBitmap)));
//
//        marker.showInfoWindow();
//
//
//
//        //마커추가
//
//        activityMap.moveCamera(CameraUpdateFactory.newLatLng(position));
//        activityMap.animateCamera(CameraUpdateFactory.zoomTo(12));
//
//    }

    private Bitmap getBitmap(Context mContext,int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
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

