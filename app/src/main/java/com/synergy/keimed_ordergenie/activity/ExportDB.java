package com.synergy.keimed_ordergenie.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ExportDB extends AppCompatActivity {
  private Button btExport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_db);
        btExport=(Button)findViewById(R.id.export_data);
        btExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            //   exportDb();
            //   exportDB();
               // exportdb();
            }
        });
    }


    protected void exportDb() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File dataDirectory = Environment.getDataDirectory();

        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = "/data/" + getApplicationContext().getApplicationInfo().packageName + "/databases/SampleDB";
        String backupDBPath = "SampleDB.sqlite";
        Toast.makeText(this, "Path:"+currentDBPath, Toast.LENGTH_LONG).show();
        File currentDB = new File(dataDirectory, currentDBPath);
        File backupDB = new File(dataDirectory, backupDBPath);
        //File backupDB = new File(externalStorageDirectory, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());

            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (source != null) source.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Error11",e.getMessage());
            }
            try {
                if (destination != null) destination.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Error12",e.getMessage());
            }
        }
    }

    private void exportDB(){
        ////File sd = Environment.getExternalStorageDirectory();
      //  File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
      //  File sd = Environment.getDataDirectory();
        File sd = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/Mydb/");

        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
      //  String currentDBPath = "/data/"+ getApplicationContext().getApplicationInfo().packageName +"/databases/SampleDB1";
       // String currentDBPath = "/data/"+getApplicationContext().getApplicationInfo().packageName +"/databases/ordergenie.db";
        String currentDBPath = Environment.getDataDirectory().getAbsolutePath().toString() + "/storage/emulated/0/OrderGenie/ordergenie.db";
        Log.d("path",currentDBPath);
       // String backupDBPath = "SampleDB1.sqlite";
        String backupDBPath = "Mydb.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported Success!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
           Log.d("Error",e.getMessage());
        }
    }

//    private void exportdb()
//    {
//        String currentDBPath = "/data/" + getApplicationContext().getApplicationInfo().packageName + "/databases/SampleDB.db";
//        Log.d("data",currentDBPath);
//        File f=new File(currentDBPath);
//        FileInputStream fis=null;
//        FileOutputStream fos=null;
//
//        try
//        {
//            String path = Environment.getDataDirectory().getAbsolutePath().toString() + "/storage/emulated/0/OrderGenie/ordergenie.db";
//            Log.d("path12",path);
//            fis=new FileInputStream(f);
//           // fos=new FileOutputStream("/mnt/sdcard/db_dump.db");
//            fos=new FileOutputStream(path);
//          //  fos=new FileOutputStream(Environment.getDataDirectory()+"/db_dump.db");
//            while(true)
//            {
//                int i=fis.read();
//                if(i!=-1)
//                {fos.write(i);}
//                else
//                {break;}
//            }
//            fos.flush();
//            Toast.makeText(this, "DB dump OK", Toast.LENGTH_LONG).show();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            Toast.makeText(this, "DB dump ERROR", Toast.LENGTH_LONG).show();
//        }
//        finally
//        {
//            try
//            {
//                if (fos!=null) {
//                    fos.close();
//                }
//                if (fis!=null) {
//                    fis.close();
//                }
//            }
//            catch(IOException ioe)
//            {}
//        }
//    }


}
