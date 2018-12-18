package com.synergy.keimed_ordergenie.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.synergy.keimed_ordergenie.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by Rajan on 12-06-2017.
 */
public class LocationService extends Service implements MakeWebRequest.OnResponseSuccess {
    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
   // private static final int LOCATION_INTERVAL = 10000;
    private static final int LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = 100f;

    private SharedPreferences pref;

    Context context = this;

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
      //  Log.e(TAG, response.toString());
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
//        Log.e(TAG, response.toString());
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
        //    Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            if (location != null) {
                String adress = LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), getApplicationContext());
                JSONObject jsonParams = new JSONObject();

                try {
                    jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                    jsonParams.put("Latitude", String.valueOf(location.getLatitude()));
                    jsonParams.put("Longitude", String.valueOf(location.getLongitude()));
                    jsonParams.put("CurrentLocation", adress);
                    Log.e(TAG, jsonParams.toString());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("lat", String.valueOf(location.getLatitude()));
                    editor.putString("long", String.valueOf(location.getLongitude()));
                    editor.commit();
                    //globalVariable.setToken(null);
                    MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_USER_LOCATION, AppConfig.POST_UPDATE_USER_LOCATION, jsonParams, context, false);
                } catch (Exception e) {

                }

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    /*
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    */

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }*/
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }



}