package com.synergy.keimed_ordergenie.utils;

import android.content.Context;
import android.util.Log;


import com.synergy.keimed_ordergenie.BuildConfig;
import com.synergy.keimed_ordergenie.app.AppController;

import org.jsoup.Jsoup;

public class CheckVersionControll implements Runnable {
     public Context ctx;
     AppController globalVariable;
     Thread t;
     public CheckVersionControll(Context context) {
         this.ctx = context;
         t = new Thread(this);
         t.start();

         globalVariable = (AppController) ctx.getApplicationContext();
         Log.e("CheckVersionControll", "running");
     }
    @Override

    public void run() {
        String newVersion = null;

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
            Log.e("newVersion",""+newVersion);
            if(newVersion!=null&&newVersion.trim().length()>0){
                globalVariable.setPlayStoreAppVersion(newVersion);
            }
        } catch (Exception e) {

        }

    }
}
