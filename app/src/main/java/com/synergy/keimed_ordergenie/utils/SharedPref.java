package com.synergy.keimed_ordergenie.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 2/28/2018.
 */

public class SharedPref {
    private static String SHARED_PREFERENCE_NAME = "mySharedPreference";
    private static String LegendData = "LegendData";
    private static String LastSyncDate = "LastSyncDate";
    private static String LegendDataMode = "LegendMode";

    /* Save, Get and Remove UrlPath */
    public static void saveLegendData(Context context, String legendData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(LegendData, legendData).commit();
    }

    public static String getLegendData(Context context) {
        SharedPreferences share = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return share.getString(LegendData, "");
    }

    public static void removeLegendData(Context context) {
        SharedPreferences share = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        share.edit().remove(LegendData).commit();
    }

    public static void saveLegendMode(Context context, String legendData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(LegendDataMode, legendData).commit();
    }

    public static String getLegendMode(Context context) {
        SharedPreferences share = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return share.getString(LegendDataMode, "");
    }
    public static void removeLegendMode(Context context) {
        SharedPreferences share = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        share.edit().remove(LegendDataMode).commit();
    }



    /* Save, Get and Remove Last Sync Date */
    public static void saveLastSyncDate(Context context, String lastSyncDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(LastSyncDate, lastSyncDate).commit();
    }

    public static String getLastSyncDate(Context context) {
        SharedPreferences share = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return share.getString(LastSyncDate, "");
    }

    public static void removeLastSyncDate(Context context) {
        SharedPreferences share = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        share.edit().remove(LastSyncDate).commit();
    }

}
