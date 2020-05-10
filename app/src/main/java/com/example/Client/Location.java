package com.example.Client;
import android.location.LocationManager;
import android.util.Log;
import android.content.Context;

// Need Permission -> 참고 : https://copycoding.tistory.com/36

public class Location {
    Context mContext;
    android.location.Location myLocation;
    LocationManager manager;

    public Location(Context mContext){
        this.mContext = mContext;
        manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        /*
        권한 부분 어떻게 할것인지?
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext.activi, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
        */
        getCurrentLocation();
    }

    public double[] getCurrentLocation(){

        double[] locationArray = new double[2];

        try {

            myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(myLocation != null) {
                locationArray[0] = myLocation.getLatitude(); //latitude
                locationArray[1] = myLocation.getLongitude(); //longitude

                Log.i("Stub",
                        String.format(
                                "Check now Location: latitude : %f, longitude : %f",
                                locationArray[0], locationArray[1]));
            }else
                Log.i("Stub",
                        "getCurrentLocation Error");
        }catch (SecurityException e){
            e.printStackTrace();
        }

        return locationArray;
    }
}



/*
class GPSListener implements LocationListener{
    public void onLocationChanged(android.location.Location myLocation){
        Double latitude = myLocation.getLatitude();
        Double longitude = myLocation.getLongitude();
        Log.i("Stub",
                String.format(
                        "Location: latitude : %f, longitude : %f",
                        latitude, longitude));
    }
    public void onProviderDisabled(String provider){}
    public void onProviderEnabled(String provider){}
    public void onStatusChanged(String provider, int status,Bundle extras){}
}*/
