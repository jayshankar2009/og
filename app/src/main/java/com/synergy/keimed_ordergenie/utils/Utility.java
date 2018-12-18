package com.synergy.keimed_ordergenie.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 2/27/2018.
 */

public class Utility {

    /* Progress Dialog */
    private static ProgressDialog progressDialog;


    /* SQLite Sync Dialog */
    private static Dialog dialog;


    /* Check Internet Connectivity */
    public static boolean internetCheck(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(context, "Internet Connection Not Available !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /* Show Progress */
    public static void showProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    /* Hide Progress */
    public static void hideProgress() {
        progressDialog.dismiss();
    }




    /* Show Sync Dialog */
    public static void showSyncDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_sync_progress);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.relative_sync);
        TextView textViewSync = (TextView) dialog.findViewById(R.id.txt_sync_dialog);
        textViewSync.setText("Please wait..Synchronizing product list\n'Do not touch anywhere while Synchronizing'");
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }



    /* Export Dialog */
    public static void syncWithoutProgress(Context context, String msg) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.export_dialog);
        dialog.show();
        TextView textView = (TextView) dialog.findViewById(R.id.txt_export_dialog);
        textView.setText(msg);
    }


    public static File storagePath() {

        File storagePath;

        if (Environment.getExternalStorageState().equalsIgnoreCase("mounted")) {
            storagePath = Environment.getExternalStorageDirectory();
            if (storagePath.getFreeSpace() / 1024 / 1024 < 10) {
                storagePath = Environment.getDataDirectory();
            }
        } else {
            storagePath = Environment.getDataDirectory();
        }

        storagePath = new File(storagePath, "OG_Selfie");
        if (!storagePath.exists()) {

            storagePath.mkdirs();
        }



        return storagePath;
    }


    /* Hide Sync Dialog */
    public static void hideSyncDialog() {
        dialog.dismiss();
    }



    /* Convert Date(dd-MM-yyyy) to Date(yyyy-MM-dd) */
    public static String convertDDMMYYYYtoYYYYMMDD(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        return timeFormat.format(myDate);
    }



    /* Convert String Date into Date Variable */
    public static Date convertStringIntoDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myDate;
    }

    public static String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    public static boolean create(Context context, String fileName, String jsonString){
        String FILENAME = "storage.json";
        FileOutputStream fos =null;
        try {
            if(isFilePresent(context,fileName)){
                String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
                File file = new File(path);
                file.delete();
            }
            fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();



            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    public static boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}
