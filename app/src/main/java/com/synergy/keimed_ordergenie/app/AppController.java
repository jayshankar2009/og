package com.synergy.keimed_ordergenie.app;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.App;
import com.synergy.keimed_ordergenie.database.DaoMaster;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.utils.LocationService;
import android.support.multidex.MultiDexApplication;
import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AppController extends MultiDexApplication {
    //private static final String TAG = App.class.getSimpleName();
    public static final String TAG = "AppController";
    public String Token;
    public String PlayStoreAppVersion;
    public Boolean Call_plan_Started = false;
    public Boolean FromCustomerList = false;
    public Boolean FromViewPagerclk = false;
    public Boolean FromHomeActivity = false;
    public Boolean FromMenuItemClick = false;

    public Boolean getFromHomeActivity() {
        return FromHomeActivity;
    }

    public void setFromHomeActivity(Boolean fromHomeActivity) {
        FromHomeActivity = fromHomeActivity;
    }

    public Boolean getFromMenuItemClick() {
        return FromMenuItemClick;
    }

    public void setFromMenuItemClick(Boolean fromMenuItemClick) {
        FromMenuItemClick = fromMenuItemClick;
    }

    public Boolean getFromViewPagerclk()
    {
        return FromViewPagerclk;
    }
    public void setFromViewPagerclk(Boolean fromViewPagerclk)
    {
        FromViewPagerclk = fromViewPagerclk;
    }

    public Boolean getFromCustomerList() {
        return FromCustomerList;
    }

    public void setFromCustomerList(Boolean fromCustomerList) {
        FromCustomerList = fromCustomerList;
    }

    public Boolean getCall_plan_Started() {
        return Call_plan_Started;
    }

    public void setCall_plan_Started(Boolean call_plan_Started) {
        Call_plan_Started = call_plan_Started;
    }
    private RequestQueue mRequestQueue;
    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        Token = token;
    }

    public String getPlayStoreAppVersion() {
        return PlayStoreAppVersion;
    }
    public void setPlayStoreAppVersion(String token) {
        PlayStoreAppVersion = token;
    }
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

/* code to check playstore updates
   */
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(com.synergy.keimed_ordergenie.activity.ForceUpdateChecker.KEY_UPDATE_REQUIRED, true);
        remoteConfigDefaults.put(com.synergy.keimed_ordergenie.activity.ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.1");
        remoteConfigDefaults.put(com.synergy.keimed_ordergenie.activity.ForceUpdateChecker.KEY_UPDATE_URL,
 // "https://play.google.com/store/apps/details?id=net.one97.paytm");
        "https://play.google.com/store/apps/details?id=com.synergy.keimed_ordergenie");
        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });

        /* code to check playstore updates
   */
        /*String path = Environment
                .getExternalStorageDirectory()
                + File.separator
                + "OrderGenie" + File.separator;*/

        String path = getDatabasePath("OrderGenie").getPath();

        File dbPathFile = new File (path);
        if (!dbPathFile.exists())
            dbPathFile.getParentFile().mkdirs();
        // sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, "password", null);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? path + "ordergenie-db-encrypted" : path + "ordergenie");
        Database db = helper.getEncryptedWritableDb("ordergenie-secret-key");
        daoSession = new DaoMaster(db).newSession();
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void deleteDatabase() {


        /*String path = Environment
                .getExternalStorageDirectory()
                + File.separator
                + "OrderGenie" + File.separator;*/

        String path = getDatabasePath("OrderGenie").getPath();

    /*    File dbPathFile = new File (path);
        if (!dbPathFile.exists())
            dbPathFile.getParentFile().mkdirs();*/

        //  sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, "password", null);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? path + "ordergenie-db-encrypted" : path + "ordergenie");
        Database db = helper.getEncryptedWritableDb("ordergenie-secret-key");
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}