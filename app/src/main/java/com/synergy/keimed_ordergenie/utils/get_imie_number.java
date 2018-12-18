package com.synergy.keimed_ordergenie.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by 1132 on 13-02-2017.
 */

public class get_imie_number {

    public static final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 125;

    public String check_imie_permission(Activity act) {
        String imei_number = "";

        if (ContextCompat.checkSelfPermission(act,
                Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            imei_number = get_imie(act);
        } else {
            imei_number = "";
            //Request Location Permission
            checkLocationPermission(act);
        }
        return imei_number;
    }

    public String get_imie(Activity act) {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) act.getSystemService
                (TELEPHONY_SERVICE);
        String yourNumber = "";
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            checkLocationPermission(act);
        }
        else
        {
            yourNumber = mTelephonyMgr.getDeviceId();
        }
        return yourNumber;
    }

    private void checkLocationPermission(final Activity act) {
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(act)
                        .setTitle("Permission")
                        .setMessage("We need IMEI number to validate your login.Please accept to login ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(act,
                                        new String[]{Manifest.permission.READ_PHONE_STATE},
                                        MY_PERMISSIONS_REQUEST_PHONE_STATE);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(act,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_PHONE_STATE);
            }
        }
    }

}
