package com.synergy.keimed_ordergenie.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
/**
 * Created by 1132 on 16-01-2017.
 */

public class LocationAddress {
    private static final String TAG = "LocationAddress";
    public static String getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
            //    sb.append(address.getSubLocality());
                sb.append(address.getAddressLine(0)).append("\n");
              //  sb.append(", ");
             //   sb.append(address.getAddressLine(1)).append("\n");
             //   sb.append(address).append("\n");
             //   sb.append(address.getAdminArea()).append("\n"); //maharashtra
             //   sb.append(address.getLocale()).append("\n");
             //   sb.append(address.getPremises()).append("\n");
//                sb.append(address.getSubAdminArea()).append("\n");
//                sb.append(address.getSubLocality()).append("\n");
//                sb.append(address.getLocality()).append("\n"); //
                sb.append(address.getPostalCode()).append("\n"); //400614
                sb.append(address.getCountryName()); //india
                result = sb.toString();

            }
        }

        catch (IOException e)

        {
          //  Log.e(TAG, "Unable connect to Geocoder", e);
        }
        return result;
    }
}

