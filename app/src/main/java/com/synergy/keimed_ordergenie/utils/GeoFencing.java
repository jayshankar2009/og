package com.synergy.keimed_ordergenie.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.synergy.keimed_ordergenie.activity.MapsActivity;

/**
 * Created by 1132 on 07-02-2017.
 */

public class GeoFencing {


    List<Geofence> mGeofenceList;
    PendingIntent mGeofencePendingIntent = null;

    public void createGeofences(double latitude, double longitude,Activity act,GoogleApiClient googleApiClient) {

        mGeofenceList=new ArrayList<Geofence>();
        String id = UUID.randomUUID().toString();
        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, 200) // Try changing your radius
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);


        addGeofence(getGeofencingRequest(),googleApiClient,act);

    }

    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();

    }

    private PendingIntent getGeofencePendingIntent(Activity ctx) {

        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(ctx, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(ctx, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);

    }

    private void addGeofence(GeofencingRequest request, GoogleApiClient googleApiClient, Activity ct) {


        LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                request,
                getGeofencePendingIntent(ct)
        ).setResultCallback((MapsActivity)ct);

    }
}
