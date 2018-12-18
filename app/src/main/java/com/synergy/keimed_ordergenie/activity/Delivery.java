package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class Delivery extends AppCompatActivity
        implements MakeWebRequest.OnResponseSuccess, LocationListener {

    private Boolean button_selected = false;
    private String Client_id;
    ResultReceiver resultReceiver;
    private String call_plan_customer_id;
    private String User_id;
    private SharedPreferences pref;
    String DeliveryStatus;
    Context context;

    String selfie_url = "", sign_url = "";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dateFormat_o = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static final String CHEMIST_ORDER_ID = "chemist_order_id";
    public static final String CHEMIST_ORDER_DATE = "chemist_order_date";
    String Start_time = "", End_time = "";
    String order_no, inv_no, Chemist_id, Cal_plan_id;
    Button btn_get_sign, mClear, mGetSign, mCancel;

    String res;

    Dialog dialog;
    LinearLayout mContent;
    View view;
    signature mSignature;

    Button Signature, Selfie;

//    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";

    File mediaStorageDir = new File(
            Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
             "/DigitSign/");

    String DIRECTORY = mediaStorageDir.toString();

    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";





    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "OG_Selfie";
    Bitmap bitmap2,bitmap1;
    ImageView selfie,signature;

    private Uri fileUri;

    @BindView(R.id.rdb_g_delivery)
    RadioGroup rdb_g_delivery;

    @BindView(R.id.orderId)
    TextView orderId;

    @BindView(R.id.orderDate)
    TextView orderDate;

    @BindView(R.id.invoice_number)
    TextView invoice_number;

    @BindView(R.id.txt_toatal)
    TextView txt_toatal;

    @BindView(R.id.inv_amount)
    TextView inv_amount;


    @BindView(R.id.rdb_g_undelivery)
    RadioGroup rdb_g_undelivery;

    @BindView(R.id.customer_name)
    TextView customer_name;


    @BindView(R.id.btn_undelivered)
    TextView btn_undelivered;

    @BindView(R.id.btn_delivered)
    TextView btn_delivered;

    @BindView(R.id.img_name)
    TextView img_name;

    @BindView(R.id.below_layout)
    LinearLayout below_layout;

    LinearLayout linearLayout;


    /* Get Current Location */
    private LocationManager locationManager;
    private String locationProvider;
    private double currentLocLat = 0;
    private double currentLocLong = 0;

    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private int postSQLite = 0;

    /* String Values */
    private String strCustomerName = "";
    private String strOrderDate = "";
    private String strOrderItemCount = "";
    private String strInvoiceAmount = "";
    private String strStatus = "";
    private String strDescription = "";
    private String currentTime = "";
    private String noOfPackets = "";
    private String noOfCases = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        selfie = (ImageView)findViewById(R.id.imageView);
        signature = (ImageView)findViewById(R.id.imageView2);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Dialog Function
        dialog = new Dialog(Delivery.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

        resultReceiver = getIntent().getParcelableExtra("receiver");
        call_plan_customer_id = getIntent().getStringExtra("client_id");
        User_id = pref.getString(USER_ID, "0");
        Client_id = pref.getString(CLIENT_ID, "0");
        Start_time = getIntent().getStringExtra("Start_time");
        //Toast.makeText(context, ""+Start_time, Toast.LENGTH_SHORT).show();

        order_no = getIntent().getStringExtra("order_no");
        inv_no = getIntent().getStringExtra("invoice_no");
        Chemist_id = getIntent().getStringExtra("chemist_id");
        Cal_plan_id = getIntent().getStringExtra("Call_plan_id");

        Signature = (Button) findViewById(R.id.btTakeSignature);
        Signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Satus", "hiiiiiiiiii");
                dialog_action();
            }
        });

        Selfie = (Button) findViewById(R.id.btnTakeSelfie);
        Selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeviceSupportCamera();
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
                    // will close the app if the device does't have camera
                    finish();
                }
                captureImage();
            }
        });


        orderId.setText(order_no);


        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            strOrderDate = simpleDateFormat.format(dateFormat_o.parse(getIntent().getStringExtra("order_date")));
            //orderDate.setText(sdf.format(dateFormat_o.parse(getIntent().getStringExtra("order_date"))));
            orderDate.setText(strOrderDate);
        } catch (Exception e) {

        }

        if (invoice_number == null) {
            invoice_number.setText("Invoice not created yet");
        } else {
            invoice_number.setText(inv_no);
        }

        strOrderItemCount = String.valueOf(getIntent().getIntExtra("order_count", 0));
        txt_toatal.setText(strOrderItemCount);

        strInvoiceAmount = getIntent().getStringExtra("invoice_amount");
        inv_amount.setText(strInvoiceAmount);

        strCustomerName = getIntent().getStringExtra("customer_name");
        customer_name.setText(strCustomerName);
        img_name.setText(strCustomerName.substring(0, 1));



        /* Get Current Location Latitude and Longitude */
        getCurrentLocationLatLong();
        sqLiteHandler = new SQLiteHandler(this);

        /* Get SQLite Save Delivery Orders If Available */
        //getSavedDelivery();

        /* Check Internet Connectivity */
        new checkInternetConn().execute(0);
    }



    //function will launch the camera to snap a picture.
    private void captureImage() {
        // Requesting camera app to capture image using ACTION_IMAGE_CAPTURE
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        //Specifying a path where the image has to be stored using EXTRA_OUTPUT
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    criarFoto();
                } else {
                    Toast.makeText(this, "You did not allow camera usage :(", Toast.LENGTH_SHORT).show();
//                    noFotoTaken();
                }
                return;
            }
        }
    }
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();

                //bitmap1=decodeFile(fileUri.getPath());

                if (bitmap1!=null) {
                    selfie.setAdjustViewBounds(true);
                    selfie.setImageBitmap(bitmap1);
                }

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    private void previewCapturedImage() {
        try {

            selfie.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for large images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            selfie.setImageBitmap(bitmap);

            if(bitmap != null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream); //compress to which format you want.
                byte [] byte_arr = stream.toByteArray();

                selfie_url = Base64.encodeToString(byte_arr,Base64.NO_WRAP);

                //image=image.trim();
            //    Log.e("image_str", ""+selfie_url);
//                JSONObject jsonObject=new JSONObject();
//                try {
//                    jsonObject.put("image",selfie_url);
//                    Log.e("image",jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
    public  Bitmap decodeFile(String path) {//you can provide file path here

        int orientation;


        try {
            if (path == null) {
                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

         //   Log.e("ExifInteface .........", "rotation ="+orientation);

//          exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

          //  Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
//              m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
           //     Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createScaledBitmap(bm,200,100, false);
//                selfie.setAdjustViewBounds(true);
//                selfie.setImageBitmap(bitmap);


                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
            //    Log.e("in orientation", "" + orientation);
//                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
//                        bm.getHeight(), m, true);
                Bitmap.createScaledBitmap(bm,200,100, false);
//                selfie.setAdjustViewBounds(true);
//                selfie.setImageBitmap(bitmap);

                return bitmap;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
        //        Log.e("in orientation", "" + orientation);
//                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
//                        bm.getHeight(), m, true);
                Bitmap.createScaledBitmap(bm,200,100, false);
//                selfie.setAdjustViewBounds(true);
//                selfie.setImageBitmap(bitmap);

                return bitmap;
            }

            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }
    public Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type)
    {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
           //     Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    public void dialog_action() {
        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.v("signature11", "Signature Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });
        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //Log.v("signature12", "Signature Saved");
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();


                // Calling the same class
                //  recreate();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.v("tag", "Panel Cancelled");
                dialog.dismiss();
                // Calling the same class
                // recreate();
            }
        });
        dialog.show();
    }

    @OnClick(R.id.order_detail)
    void onclick_order_detail() {
        Intent intent = new Intent(Delivery.this, Order_details.class);
        intent.putExtra(CHEMIST_ORDER_ID, order_no);
        intent.putExtra(CHEMIST_ORDER_DATE, getIntent().getStringExtra("order_date"));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delivery, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                if (!button_selected) {
                    OGtoast.OGtoast("Please select a Delivery status", this);
                } else if (rdb_g_delivery.getCheckedRadioButtonId() != -1 || rdb_g_undelivery.getCheckedRadioButtonId() != -1) {
                    save_delivery();
                }else {
                    OGtoast.OGtoast("Please select a Reason", this);
                }

//                if (selfie.getDrawable()==null&&signature.getDrawable()==null)
//                {
//                    OGtoast.OGtoast("Please take selfie or take  signature", this);
//                }
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /* Undelivered Click */
    @OnClick(R.id.btn_undelivered)
    void undelivery() {
        button_selected = true;
        below_layout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        rdb_g_undelivery.setVisibility(View.VISIBLE);
        rdb_g_delivery.setVisibility(View.GONE);
        btn_undelivered.setBackgroundColor(getResources().getColor(R.color.dark_red));
        btn_undelivered.setTextColor(getResources().getColor(R.color.white));

        btn_delivered.setBackgroundColor(Color.TRANSPARENT);
        btn_delivered.setTextColor(getResources().getColor(R.color.button_grey));
        DeliveryStatus = "NDEL";
    }



    /* Delivered Click */
    @OnClick(R.id.btn_delivered)
    void udelivery() {
        button_selected = true;
        linearLayout.setVisibility(View.VISIBLE);
        below_layout.setVisibility(View.VISIBLE);
        rdb_g_undelivery.setVisibility(View.GONE);
        rdb_g_delivery.setVisibility(View.VISIBLE);
        btn_delivered.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_delivered.setTextColor(getResources().getColor(R.color.white));
        DeliveryStatus = "DEL";
        btn_undelivered.setBackgroundColor(Color.TRANSPARENT);
        btn_undelivered.setTextColor(getResources().getColor(R.color.button_grey));
    }



    /* Save Delivery */
    void save_delivery() {
        End_time = dateFormat.format(Calendar.getInstance().getTime());
        try {
            JSONObject Main_j_obj = new JSONObject();
            JSONArray j_arr = new JSONArray();
            JSONObject inner_j_obj = new JSONObject();

            inner_j_obj.put("Transaction_No", Integer.parseInt(order_no));
            inner_j_obj.put("Stockist_Client_id", pref.getString(CLIENT_ID, "0"));
            if (DeliveryStatus.equals("DEL")) {
                int id = rdb_g_delivery.getCheckedRadioButtonId();
                View radioButton = rdb_g_delivery.findViewById(id);
                int radioId = rdb_g_delivery.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rdb_g_delivery.getChildAt(radioId);
                String selection = (String) btn.getText();

                strStatus = "5";
                strDescription = selection;
                /*strStatus = "4";      ////---- Old Code
                strDescription = selection;*/
                inner_j_obj.put("Status", strStatus);
                inner_j_obj.put("Description", strDescription);
                inner_j_obj.put("Order_Image", selfie_url);
                inner_j_obj.put("Sign_Image", sign_url);


            } else {
                int id = rdb_g_undelivery.getCheckedRadioButtonId();
                View radioButton = rdb_g_undelivery.findViewById(id);
                int radioId = rdb_g_undelivery.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rdb_g_undelivery.getChildAt(radioId);
                String selection = (String) btn.getText();

                strStatus = "2";
                strDescription = selection;
                inner_j_obj.put("Status", strStatus);
                inner_j_obj.put("Description", strDescription);
            }
            j_arr.put(inner_j_obj);
            Main_j_obj.put("orders", j_arr);
            //Log.d("deliveryPayload", Main_j_obj.toString());

          //  Log.e("Delivery",Main_j_obj.toString());
            // SAVE_ORDER_DELIVERY

            if (Utility.internetCheck(this)) {
                MakeWebRequest.MakeWebRequest("NPost", AppConfig.SAVE_ORDER_DELIVERY, AppConfig.SAVE_ORDER_DELIVERY, Main_j_obj, this, true);
            } else {
                saveDeliveryInSQLite(strStatus);
            }

        /*    new SaveCallPlan(this, j_arr.getJSONObject(0).getString("StockistCallPlanID"),
                    call_plan_customer_id, pref.getString(USER_ID, "0"), Start_time, End_time, "2", "3", "yes");*/
        } catch (Exception e) {
            e.toString();
        }
    }




    /* Get Current Location Lat Long */
    private void getCurrentLocationLatLong() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        locationProvider = locationManager.getBestProvider(criteria, false);

        if (locationProvider != null && !locationProvider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(locationProvider);
            locationManager.requestLocationUpdates(locationProvider, 10000, 1, this);

            if (location != null){
                onLocationChanged(location);
            } else {
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            }
        }
    }



    ////----        Start Get Current Location Latitude and Longitude     ----////
    @Override
    public void onLocationChanged(Location location) {
        currentLocLat = location.getLatitude();
        currentLocLong = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    ////----        End Current Location Latitude and Longitude     ----////




    /* Current Time */
    private void currentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        currentTime = simpleDateFormat.format(calendar.getTime());
    }



    /* Save Delivery Locally */
    private void saveDeliveryInSQLite(final String orderStatus) {
        currentTime();
        getCurrentLocationLatLong();
        sqLiteHandler.insertIntoSaveDelivery(User_id, Start_time, End_time, strCustomerName, Client_id, Chemist_id, Cal_plan_id, order_no, inv_no,
                strOrderDate, strOrderItemCount, strInvoiceAmount, strStatus, strDescription, selfie_url, sign_url, currentTime,
                String.valueOf(currentLocLat), String.valueOf(currentLocLong), noOfPackets, noOfCases);
        if (DeliveryStatus.equalsIgnoreCase("NDEL")) {
            order_undelivered_dialog();
        } else {
            order_confirmed_dialog();
        }
        sqLiteHandler.updateInvoiceListDelivery(inv_no, orderStatus);
    }





    /* Get SQLite Saved Delivery */
    private void getSavedDelivery() {
        Cursor cursor = sqLiteHandler.getSavedDelivery();
        int cursorCount = cursor.getCount();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("Stockist_Client_id", cursor.getString(5));
                        jsonObject1.put("Transaction_No", Integer.valueOf(cursor.getString(8)));
                        jsonObject1.put("Status", cursor.getString(13));
                        jsonObject1.put("Description", cursor.getString(14));
                        jsonObject1.put("Order_Image", cursor.getString(15));
                        jsonObject1.put("Sign_Image", cursor.getString(16));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject1);
                    if (cursorCount == jsonArray.length()) {
                        try {
                            jsonObject.put("orders", jsonArray);
                            //Log.d("SQLiteSavedOrders", jsonObject.toString());
                            postSQLite = 1;
                            MakeWebRequest.MakeWebRequest("NPost", AppConfig.SAVE_ORDER_DELIVERY, AppConfig.SAVE_ORDER_DELIVERY, jsonObject, this, false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
    }





    /* Server Response */
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
          //  Log.e("Response12",response.toString());
            try {
                if (f_name.equals(AppConfig.SAVE_ORDER_DELIVERY)) {

                    Cursor cursor = sqLiteHandler.getSavedDelivery();
                    int cursorCount = cursor.getCount();
                    if (postSQLite == 1) {
                        if (cursorCount > 0) {
                            sqLiteHandler.deleteSavedDelivery();
                        }
                    }

                    for (int i=0;i<response.length();i++) {
                        res= response.getJSONObject(i).getString("result");
                    }
                    if (res.contains("updated") && DeliveryStatus.equals("DEL")) {
                        //Log.e("res","sucesssss");
                        order_confirmed_dialog();
                    }
                    else {
                        order_undelivered_dialog();
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public void onSuccess_json_object(String f_name, JSONObject response) {

//        if (response != null) {
//            Log.d("Response11",response.toString());
//            try {
//                if (f_name.equals(AppConfig.SAVE_ORDER_DELIVERY)) {
//                    if (response.getString("result").contains("updated")) {
//                       Log.e("res","sucesssss");
//                        order_confirmed_dialog();
//                    }
//                }
//            } catch (Exception e) {
//
//            }
//        }

    }




    /* Successfully Delivered Dialog */
    void order_confirmed_dialog() {
        new AlertDialog.Builder(Delivery.this)
                .setTitle("Delivery")
                .setCancelable(false)
                .setMessage("Delivered successfully.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Bundle bundle = new Bundle();
                        bundle.putString("Delivery", "Done");
                        if (resultReceiver != null) {
                            resultReceiver.send(400, bundle);
                        }
//                        Intent intent = new Intent(Delivery.this, Order_list_delivery.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        finish();

                    }
                }).show();
    }



    /* Undeliverd Dialog */
    void order_undelivered_dialog() {
        new AlertDialog.Builder(Delivery.this)
                .setTitle("Delivery Status")
                .setCancelable(false)
                .setMessage("Undelivered.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Intent intent = new Intent(Delivery.this, CallPlanDetails.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        finish();
                    }
                }).show();
    }



    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,70, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b,Base64.NO_WRAP);
        return temp;
    }




    /* OPEN Signature View */
    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
        //    Log.v("tag", "Width: " + v.getWidth());
        //    Log.v("tag", "Height: " + v.getHeight());
            if (bitmap2 == null) {
                bitmap2 = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap2);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                // Convert the output file to Image such as .png
                //bitmap2.compress(Bitmap.CompressFormat.PNG, 70, mFileOutStream);
                // Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                // Bitmap photo1=ThumbnailUtils.extractThumbnail(photo,200,100);
                if (bitmap2!=null)
                    signature.setAdjustViewBounds(true);
                signature.setImageBitmap(Bitmap.createScaledBitmap(bitmap2, 200, 100, false));
                sign_url = BitMapToString(bitmap2);
                //   sign_url=sign_url.trim();
                //  byte[] ImagBytes =  ImageUtils.compressImage(StoredPath);
                // byte [] b=baos.toByteArray();
                // sign_url= Base64.encodeToString(ImagBytes, Base64.DEFAULT);
            //    Log.e("sign_url",sign_url);
                mFileOutStream.flush();
                mFileOutStream.close();
            } catch (Exception e) {
              //  Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;
                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            //Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }




    /* Check Internet Connection Class */
    private class checkInternetConn extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update your layout here
            if (InternetConnection.checkConnection(getApplication())) {
                //offline.setVisibility(View.GONE);
                //online.setVisibility(View.VISIBLE);
                getSavedDelivery();
            } /*else {
                online.setVisibility(View.GONE);
                offline.setVisibility(View.VISIBLE);
            }*/

            super.onPostExecute(result);

            //Do it again!
            new checkInternetConn().execute(5000);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
    }




    /* Check Internet Connection on Current State */
    public static class InternetConnection {
        /**
         * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
         */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                //  Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }
    }



    /* onBackPressed */
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        this.finish();
    }
}
