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
            }
//                Log.i("Stub",
//                        "getCurrentLocation Error");
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
