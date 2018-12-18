package com.synergy.keimed_ordergenie.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.CallPlan;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by AS Infotech on 25-10-2017.
 */

public class get_current_location_ondashboard implements LocationListener, MakeWebRequest.OnResponseSuccess {
    private Context ctx;
    LocationManager mLocationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 10 * 1000;
    private TextView txt_address;
    private ProgressBar pgr;

    private SharedPreferences pref;
    AppController globalVariable;

    public get_current_location_ondashboard(Activity ctx) {
        this.ctx = ctx;
        pref = ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        globalVariable = (AppController) ctx.getApplicationContext();
        get_location();
    }

    void get_location() {

        show_location_dialog(globalVariable.getFromMenuItemClick());

        //  isRequestCurrentLocation=true;
        mLocationManager = (LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
        //  mLocationManager.getLastKnownLocation();
        Boolean isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled||isNetworkEnabled) {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER,
//                    MIN_TIME_BW_UPDATES,
//                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = mLocationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                String adress = LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), ctx);
                if (adress!=null) {
                    txt_address.setText(adress);
                    pgr.setVisibility(View.GONE);
                }
            }

        } else {
            //showSettingsAlert();
            Intent intent = new Intent(ctx, LocationActivity.class);
            ctx.startActivity(intent);
        }

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);

        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Please enable the Location");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OGtoast.OGtoast("Location Services not enabled !. Unable to get the location", ctx);
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            String adress = LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), ctx);
            if (adress!=null) {
                txt_address.setText(adress);
                pgr.setVisibility(View.GONE);
            }




            JSONObject jsonParams = new JSONObject();

            try {
                jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                jsonParams.put("Latitude", String.valueOf(location.getLatitude()));
                jsonParams.put("Longitude", String.valueOf(location.getLongitude()));
                jsonParams.put("CurrentLocation", adress);

                Log.e("location", "hii");
              //  Log.e("location", jsonParams.toString());
                //Log.e("location", pref.getString(CLIENT_ID, "0"));
                //globalVariable.setToken(null);
                MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_USER_LOCATION, AppConfig.POST_UPDATE_USER_LOCATION, jsonParams, ctx, false);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        //Log.e("LocationUpdate",response.toString());
//locationManager.removeUpdates(LocationTrack.this);

    }
    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override

    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }


    void show_location_dialog(boolean isTrue) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        final View dialogview = inflater.inflate(R.layout.dialog_current_location, null);
        final Dialog infoDialog = new Dialog(ctx);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);


        txt_address = (TextView) dialogview.findViewById(R.id.txt_address);
        pgr = (ProgressBar) dialogview.findViewById(R.id.prgr_bar);
        pgr.setVisibility(View.VISIBLE);

        dialogview.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                infoDialog.dismiss();
            }
        });

        if (isTrue) {
            infoDialog.show();
        }
        else
        {
            infoDialog.hide();
        }
    }

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
}
