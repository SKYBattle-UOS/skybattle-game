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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

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
public class AndroidGoogleMap implements Map {
    private final float hue_color = (float) 0.0; //default

    private GoogleMap _googleMap;
    private Context _matchActivity;
    private View _marker_root_view; //text 띄우기 위해
    private TextView _tv_marker; //textview
    private Handler _mainHandler = new Handler(Looper.getMainLooper());

    private HashMap<Integer, Circle> _circles = new HashMap<>();
    private HashMap<Integer, Marker> _markers = new HashMap<>();
    private int _nextMarkerNumber;
    private int _nextCircleNumber;

    private HashMap<Integer, SavedMarker> _savedMarkers = new HashMap<>();
    private HashMap<Integer, SavedCircle> _savedCircles = new HashMap<>();
    private SavedCamera _savedCamera = new SavedCamera(0, 0, 0);

    public AndroidGoogleMap(GoogleMap googleMap, Context mContext,
                            View marker_root_view, TextView tv_marker) {
        setContext(googleMap, mContext, marker_root_view, tv_marker);
    }

    public void setContext(GoogleMap googleMap, Context context, View markerView, TextView textView){
        _googleMap = googleMap;
        _matchActivity = context;
        _marker_root_view = markerView;
        _tv_marker = textView;

        _googleMap.setOnMarkerClickListener((marker) -> true);
    }

    @Override
    public MapCircleHandle addCircle(double lat, double lon, int color, float radius) {
        int number = _nextCircleNumber++;
        GoogleCircleHandle ret = new GoogleCircleHandle(number);
        SavedCircle saved = new SavedCircle(lat, lon, color, radius);
        _savedCircles.put(number, saved);

        if (_googleMap != null)
            _mainHandler.post(() -> _addCircle(number, lat, lon, color, radius));
        return ret;
    }

    @Override
    public synchronized MapMarkerHandle addMarker(double latitude, double longitude,
                                                  int color, float size, String name) {
        int number = _nextMarkerNumber++;
        GoogleMarkerHandle ret = new GoogleMarkerHandle(number);
        SavedMarker saved = new SavedMarker(latitude, longitude, color, size, name);
        _savedMarkers.put(number, saved);

        if (_googleMap != null)
            _mainHandler.post(() -> _addMarker(number, latitude, longitude, color, size, name));

        return ret;
    }

    @Override
    public void setMarkerPosition(MapMarkerHandle marker, double lat, double lon) {
        SavedMarker saved = _savedMarkers.get(((GoogleMarkerHandle) marker).index);
        saved.lat = lat;
        saved.lon = lon;

        if (_googleMap != null)
            _mainHandler.post(() -> _setMarkerPosition(marker, lat, lon));
    }

    @Override
    public void setCirclePosition(MapCircleHandle circle, double lat, double lon){
        SavedCircle saved = _savedCircles.get(((GoogleCircleHandle) circle).index);
        saved.lat = lat;
        saved.lon = lon;

        if (_googleMap != null)
            _mainHandler.post(() -> _setCirclePosition(circle, lat, lon));
    }

    @Override
    public void setCircleRadius(MapCircleHandle circle, float radius){
        SavedCircle saved = _savedCircles.get(((GoogleCircleHandle) circle).index);
        saved.radius = radius;

        if (_googleMap != null)
            _mainHandler.post(() -> _setCircleRadius(circle, radius));
    }

    @Override
    public void removeMarker(MapMarkerHandle marker) {
        _savedMarkers.remove(((GoogleMarkerHandle) marker).index);

        if (_googleMap != null)
            _mainHandler.post(() -> _removeMarker(marker));
    }
    @Override
    public void removeCircle(MapCircleHandle circle){
        _savedCircles.remove(((GoogleCircleHandle) circle).index);

        if (_googleMap != null)
            _mainHandler.post(() -> _removeCircle(circle));
    }

    @Override
    public void moveCamera(double latitude, double longitude) {
        _mainHandler.post(() -> _moveCamera(latitude, longitude));
    }

    @Override
    public void zoomCamera(float zoom) {
        _mainHandler.post(()->_animateCamera(zoom));
    }
    @Override
    public void hideMarker(MapMarkerHandle marker){
        _mainHandler.post(()->_hideMarker(marker));
    }

    @Override
    public void showMarker(MapMarkerHandle marker){
        _mainHandler.post(()->_showMarker(marker));
    }

    public void save(){
        CameraPosition camPos = _googleMap.getCameraPosition();
        _savedCamera.lat = camPos.target.latitude;
        _savedCamera.lon = camPos.target.longitude;
        _savedCamera.zoom = camPos.zoom;

        _googleMap = null;
        _tv_marker = null;
        _matchActivity = null;
        _marker_root_view = null;
        _markers.clear();
        _circles.clear();
    }

    public void restore(){
        for (java.util.Map.Entry<Integer, SavedMarker> e : _savedMarkers.entrySet()){
            SavedMarker saved = e.getValue();
            _addMarker(e.getKey(), saved.lat, saved.lon, saved.color, saved.size, saved.name);
        }

        for (java.util.Map.Entry<Integer, SavedCircle> e : _savedCircles.entrySet()){
            SavedCircle saved = e.getValue();
            _addCircle(e.getKey(), saved.lat, saved.lon, saved.color, saved.radius);
        }

        _moveCamera(_savedCamera.lat, _savedCamera.lon);
        _animateCamera(_savedCamera.zoom);
    }

    private synchronized void _setMarkerPosition(MapMarkerHandle marker, double lat, double lon) {
        if (_googleMap == null) return;

        int index = ((GoogleMarkerHandle) marker).index;
        Marker cur_marker = _markers.get(index);
        cur_marker.setPosition(new LatLng(lat, lon));
        cur_marker.showInfoWindow();
    }
    private synchronized void _setCirclePosition(MapCircleHandle circle, double lat, double lon){
        if (_googleMap == null) return;

        int index = ((GoogleCircleHandle) circle).index;
        Circle cur_circle = _circles.get(index);
        cur_circle.setCenter(new LatLng(lat,lon));
        //cur_circle.setVisible(true);
    }

    private synchronized void _setCircleRadius(MapCircleHandle circle, float radius){
        if (_googleMap == null) return;

        int index = ((GoogleCircleHandle) circle).index;
        Circle cur_circle = _circles.get(index);
        cur_circle.setRadius((double)radius);
        //cur_circle.setVisible(true);
    }

    private synchronized void _addMarker(int number, double latitude, double longitude,
                                         int color, float size, String name){
        _drawText(number, latitude, longitude, name);
    }

    private void _moveCamera(double lat, double lon) {
        _googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
    }

    private synchronized void _removeMarker(MapMarkerHandle marker) {
        if (_googleMap == null) return;

        int index = ((GoogleMarkerHandle) marker).index;
        Marker cur_marker = _markers.get(index);
        _markers.remove(index);
        cur_marker.remove();
    }

    private synchronized void _removeCircle(MapCircleHandle circle) {
        if (_googleMap == null) return;

        int index = ((GoogleCircleHandle) circle).index;
        Circle cur_circle = _circles.get(index);
        _circles.remove(index);
        cur_circle.remove();
    }

    private void _animateCamera(float zoom) {
        _googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    private synchronized void _drawText(int number, double latitude, double longitude, String name) {
        //textview를 marker로 띄움
        LatLng position = new LatLng(latitude, longitude);

        _tv_marker.setText(name);
        _tv_marker.setBackgroundResource(R.drawable.marker_mask_mm);
        _tv_marker.setTextColor(Color.WHITE);

        Marker marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(_matchActivity, _marker_root_view))));

        _markers.put(number, marker);
    }

//    private synchronized void _drawText2(double latitude, double longitude) {
//        //marker를 custom marker로 바꿔줌
//        LatLng position = new LatLng(latitude, longitude);
//        Bitmap custom_marker = getBitmap(_matchActivity,R.drawable.ic_priority_high_black_24dp);
//
//        Marker marker = _googleMap.addMarker(new MarkerOptions()
//                .position(position)
//                .title("신난다")
//                .icon(BitmapDescriptorFactory.fromBitmap(custom_marker)));
//
//        marker.showInfoWindow();
//        _markers.add(marker);
//    }

//    private synchronized void _drawImage(double latitude, double longitude) { //marker를 image로 만듬
//        LatLng position = new LatLng(latitude, longitude);
//        //Bitmap resized=getResizedBitmap(_matchActivity.getResources(),R.drawable.hp_portion,30,30,30); size 줄이기 위해
//
//        Marker marker = _googleMap.addMarker(new MarkerOptions()
//                .position(position)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hp_portion)));
//        //.icon(BitmapDescriptorFactory.fromBitmap(resized)));
//
//        marker.showInfoWindow();
//        _markers.add(marker);
//    }

    private void _addCircle(int number,double lat, double lon, int color, float radius) {
        String color_fill="";
        LatLng position = new LatLng(lat, lon);
        if(color==Color.RED)
            color_fill="#22FF0000";

        Circle circle = _googleMap.addCircle(new CircleOptions()
                .center(position)
                .radius(radius)
                .strokeColor(color)
                .strokeWidth((float) 1.0)
                .fillColor(Color.parseColor(color_fill)));

        _circles.put(number, circle);
    }

    private void _hideMarker(MapMarkerHandle marker) {
        int index = ((GoogleMarkerHandle) marker).index;
        Marker cur_marker = _markers.get(index);
        cur_marker.setVisible(false);
    }

    private void _showMarker(MapMarkerHandle marker) {
        int index = ((GoogleMarkerHandle) marker).index;
        Marker cur_marker = _markers.get(index);
        cur_marker.setVisible(true);
        cur_marker.showInfoWindow();
    }

    public static Bitmap getResizedBitmap(Resources resources, int id, int size, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap src = BitmapFactory.decodeResource(resources, id, options);
        Bitmap resized = Bitmap.createScaledBitmap(src, width, height, true);
        return resized;
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