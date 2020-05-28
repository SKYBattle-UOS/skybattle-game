package com.example.Client;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import android.app.Service;
import android.os.IBinder;
import java.util.regex.Pattern;

public class Location
{
    private GpsTracker gpsTracker;

    public static final int GPS_ENABLE_REQUEST_CODE = 2001;    //gps 사용 requestCode
    Context mContext;

    public Location(Context mContext){
        this.mContext = mContext;

        gpsTracker = new GpsTracker(mContext);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        Log.i("Stub",
                String.format("now Location : latitude : %f, longitude : %f", latitude, longitude));

        /* for test
         for(int i =0 ; i <10000 ; i++ ){
                    double[] data = lo.getLocation();
                   String t1 = Double.toString(data[0]);
                   String t2 = Double.toString(data[1]);

                   Log.i("Stub",
                           String.format("now Location : latitude : %s, longitude : %s", t1, t2));
               }
        */
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public double[] getLocation() {
        gpsTracker.getLocation();
        return new double[]{gpsTracker.getLatitude(), gpsTracker.getLongitude() };
    }
}

class GpsTracker extends Service implements LocationListener {

    private final Context mContext;
    android.location.Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000;
    protected LocationManager locationManager;


    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation();
        test();
    }


    public android.location.Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.i("Stub",
                        "Location 기기의 문제로 gps 위치를 가져올 수 없습니다.");
            } else {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);

                if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.i("Stub",
                    ""+e.toString());
        }

        return location;
    }

    public void test(){

        Log.i("Stub",
                String.format("now Location : latitude : %s, longitude : %s", converter(latitude), converter(longitude)));
    }

    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public String converter(double value){
        String temp = Double.toString(value);
        return temp.split(Pattern.quote("."))[1];
    }

    @Override
    public void onLocationChanged(android.location.Location location)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }


    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }
}