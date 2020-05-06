package com.example.Client;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;

import androidx.core.app.ActivityCompat;

// Need Permission -> 참고 : https://copycoding.tistory.com/36

public class Location {
    Context mContext;
    android.location.Location myLocation;
    LocationManager manager;

    public Location(Context mContext){
        this.mContext = mContext;
        manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //GPS 작동 유무 확인
        if (!isGPSEnabled) {
            Log.i("Stub",
                    "Check GPS enable, Please turn on GPS");
            return;
        }

        setLocation();
    }

    private void setLocation(){

        /*
           1. , WI-FI/Cell Tower(기지국)으로부터 위치정보를 얻지 못할 때
            2. 가장 최근에 읽은 위치정보가 없을 때(디바이스를 재부팅하면 이 값이 사라집니다)
        */

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Stub",
                    "Please Check App's GPS Permission");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);    //이전에 저장하고 있던 위치만 돌려주는 API.

        if(myLocation != null) {
            Log.i("Stub",
                    "myLocation has value");

            Log.i("Stub",
                    String.format(
                            "Set now Location: latitude : %f, longitude : %f",
                            myLocation.getLatitude(), myLocation.getLongitude()));
        }
        else
        {
            Log.i("Stub",
                    "myLocation is null");
        }
    }

    public void getCurrentLocation() {
        double[] locationArray = new double[2];
        GPSListener gpsListener = new GPSListener();

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Stub",
                    "Please Check App's GPS Permission");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(manager.GPS_PROVIDER,100,0,gpsListener);

        locationArray[0]= gpsListener.latitude;
        locationArray[1]= gpsListener.longitude;

        Log.i("Stub",
                String.format(
                        "Check now Location2: latitude : %f, longitude : %f",locationArray[0]
                        ,locationArray[1]));

    }
}





class GPSListener implements LocationListener {
    public double latitude, longitude;

    @Override   //위치가 확인되었을 때 자동으로 호출되는 메서드
    public void onLocationChanged(android.location.Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {}
}
