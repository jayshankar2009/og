package com.synergy.keimed_ordergenie.utils;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by 1132 on 21-12-2016.
 */

public class ProcessChecker extends Service {

    private static final int NOTIFICATION_ID=147894;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        //Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
        try {
            NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nMgr.cancel(NOTIFICATION_ID);
        }catch (Exception e)
        {

        }
      //  Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        try {
            NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nMgr.cancel(NOTIFICATION_ID);
        }catch (Exception e)
        {

        }
        //Toast.makeText(this, "End", Toast.LENGTH_SHORT).show();
        //Code here
        stopSelf();
    }
}