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

import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.SalesmanMakePayment;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by 1132 on 16-01-2017.
 */

public class get_current_location implements android.location.LocationListener, MakeWebRequest.OnResponseSuccess {
    private Context ctx;
    LocationManager mLocationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 10000;
    // private static final long MIN_TIME_BW_UPDATES = 1 * 60000;
    // private static final long MIN_TIME_BW_UPDATES = 300 * 1000;
    // private static final long MIN_TIME_BW_UPDATES = 20 * 1000;
    private TextView txt_address;
    private ProgressBar pgr;

    private SharedPreferences pref;
    String accessKey;

    AppController globalVariable;

    public get_current_location(Activity ctx) {
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
        if (isGPSEnabled || isNetworkEnabled) {
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


            if(isNetworkEnabled)
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            else
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            accessKey = pref.getString("key", "");

            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {

                String adress = LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), ctx);
                if (adress != null) {
                    txt_address.setText(adress);
                    // txt_address1.setText(LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), ctx));
                    pgr.setVisibility(View.GONE);
                }
            }

        } else {
           // showSettingsAlert();
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
            if (adress != null) {
                txt_address.setText(adress);
                pgr.setVisibility(View.GONE);
            }

            get_LocationUpdate(location);

        }
    }

    private void get_LocationUpdate(Location location) {

        try {
            final JSONArray jsonArray = new JSONArray();
            jsonArray.put(Integer.parseInt(pref.getString(CLIENT_ID, "0")));
            jsonArray.put(Integer.parseInt(pref.getString(USER_ID, "0")));
            jsonArray.put(location.getLatitude());
            jsonArray.put(location.getLongitude());
            jsonArray.put(1);
            String url = AppConfig.GET_CURRENT_LOCATION + jsonArray;
            Log.d("Locationupdate11", url);


            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response != null) {

                        try {

                            JSONObject jsonObject = response.getJSONObject(0);
                            String message = jsonObject.getString("msg");
                            Log.d("Locationupdate12", message);

                            if (message.equals("1")) {

                                Log.d("Locationupdate13", "Location is Updated");

                            } else {

                                Log.d("Locationupdate14", "Location not Updated");


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Locationupdate15", response.toString());
                        //getInvoiceToUpdate(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Locationupdate16", error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + accessKey);
                    return headers;
                }
            };
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(ctx).add(jsonArrayRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if (response != null) {
            if (f_name.equals(AppConfig.GET_CURRENT_LOCATION)) {
                Log.d("get_location13", String.valueOf(response));
            }
        }
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        Log.d("get_location14", "JsonObject" + String.valueOf(response));

        // Log.e("LocationUpdate", response.toString());
        // Log.e("location13", response.toString());

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
        //  txt_address1 = (TextView) dialogview.findViewById(R.id.txt_address1);
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
        } else {
            infoDialog.hide();
        }
    }


}
