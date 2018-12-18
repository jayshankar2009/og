package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.SelectedDeliveryListAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_delivery_invoice_list;
import com.synergy.keimed_ordergenie.utils.LoadJsonFromAssets;
import com.synergy.keimed_ordergenie.utils.LocationActivity;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class SaveDeliveryActivity extends AppCompatActivity implements LocationListener, MakeWebRequest.OnResponseSuccess, View.OnClickListener {

    private boolean iscalledUpdate = false;
    String provider;
    static String imgPath;
    private Boolean button_selected = false;
    private String Client_id;
    ResultReceiver resultReceiver;
    private String call_plan_customer_id, chemistName, customerName, accessKey;
    private String userId, Stockist_id, Delievry_Doc_Id;
    private SharedPreferences pref;
    private TextView empty_view, txt_cust_name;
    String DeliveryStatus;
    int totalBoxCount = 0;
    int totalItems = 0;
    int totalPackets = 0;
    Context context;
    Boolean saveLocation = false;
    String currentDate;
    CheckBox checkBoxSelectAll;
    List<m_delivery_invoice_list> posts = new ArrayList<m_delivery_invoice_list>();
    String selfie_url = "", sign_url = "";
    TextView txt_scanresult;
    String InvoiceNo;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dateFormat_o = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    SimpleDateFormat simpleDateFormatt = new SimpleDateFormat("yyyy-MM-dd");
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

    Button Signature, ScanQR, Selfie;

//    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";

    File mediaStorageDir;

    String DIRECTORY;
    String pic_name;
    String StoredPath;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "OG_Selfie";
    Bitmap bitmap2, bitmap1;
    ImageView selfie, signature;
    private RecyclerView recyclerView;

    private Uri fileUri;

    @BindView(R.id.rdb_g_delivery)
    RadioGroup rdb_g_delivery;

    @BindView(R.id.txt_no_of_item)
    TextView tx_total_items;

    @BindView(R.id.txt_no_of_boxes)
    TextView txt_no_of_boxes;

    @BindView(R.id.txt_no_of_packets)
    TextView txt_no_of_packets;

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
    private IntentIntegrator qrScan;
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
    boolean checkCamera = false;
    String formattedDatewithTime;
    SimpleDateFormat dateFormatwithtime;

    private UUID mUniqueId;
    String uniqueID;
    String getUniqueId;
    String adress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_delivery);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/DigitSign/");

        DIRECTORY = mediaStorageDir.toString();
        pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        StoredPath = DIRECTORY + pic_name + ".png";


        dateFormatwithtime = new SimpleDateFormat("yyyy-MM-dd hh.mm aa");
        Calendar c = Calendar.getInstance();
        formattedDatewithTime = dateFormatwithtime.format(c.getTime());


        selfie = (ImageView) findViewById(R.id.imageView);
        signature = (ImageView) findViewById(R.id.imageView2);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);
        empty_view = (TextView) findViewById(R.id.empty_view);
        txt_scanresult = (TextView) findViewById(R.id.txt_scanresult);
        txt_cust_name = (TextView) findViewById(R.id.cust_name);

        selfie.setOnClickListener(this);

        signature.setOnClickListener(this);
        //  selfie.setOnTouchListener(this);
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        accessKey = pref.getString("key", "");
        // Dialog Function
        dialog = new Dialog(SaveDeliveryActivity.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);
        resultReceiver = getIntent().getParcelableExtra("receiver");
        call_plan_customer_id = getIntent().getStringExtra("client_id");
        userId = pref.getString(USER_ID, "0");
        //  userId="39471";
        Client_id = pref.getString(CLIENT_ID, "0");
        Start_time = getIntent().getStringExtra("Start_time");
        //Toast.makeText(context, ""+Start_time, Toast.LENGTH_SHORT).show();

        //  order_no = getIntent().getStringExtra("order_no");
        //inv_no = getIntent().getStringExtra("invoice_no");
        Chemist_id = getIntent().getStringExtra("chemist_id");
        chemistName = getIntent().getStringExtra("chemist_name");
        //customerName = getIntent().getStringExtra("customer_name");
        txt_cust_name.setText(chemistName);
        userId = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");
        Calendar calendar = Calendar.getInstance();
        currentDate = simpleDateFormatt.format(calendar.getTime());
        Log.d("currentDate", currentDate);
        Log.d("data11", chemistName + "--" + Chemist_id + "--" + customerName);
        Log.d("data12", userId + "--" + Stockist_id);



        /* RecyclerView Item Decoration */
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Signature = (Button) findViewById(R.id.btTakeSignature);
        // loadData();
        totalDataCount(Constant.selectedDeliveryInvoice);
        fill_payment_list(Constant.selectedDeliveryInvoice);
        qrScan = new IntentIntegrator(this);
        Signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Satus", "hiiiiiiiiii");
                dialog_action();
            }
        });

        Selfie = (Button) findViewById(R.id.btnTakeSelfie);
        ScanQR = (Button) findViewById(R.id.btScanQRCode);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            selfie.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        Selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checkCamera=true;
                /*if (ContextCompat.checkSelfPermission(get,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(this, ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                        }
                    }
                } else {
                  captureImage();
                }*/
                //  captureImage();
                captureImage();

            }
        });
        ScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isDeviceSupportCamera();
//                if (!isDeviceSupportCamera()) {
//                    Toast.makeText(getApplicationContext(),
//                            "Sorry! Your device doesn't support camera",
//                            Toast.LENGTH_LONG).show();
//                    // will close the app if the device does't have camera
//                    finish();
//                }
//                captureImage();
                qrScan.initiateScan();
            }
        });

        //  tx_total_items.setText(order_no);

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            strOrderDate = simpleDateFormat.format(dateFormat_o.parse(getIntent().getStringExtra("order_date")));
            //orderDate.setText(sdf.format(dateFormat_o.parse(getIntent().getStringExtra("order_date"))));
            txt_no_of_boxes.setText(strOrderDate);
        } catch (Exception e) {

        }

//        if (txt_no_of_packets == null) {
//            txt_no_of_packets.setText("Invoice not created yet");
//        } else {
//            txt_no_of_packets.setText(inv_no);
//        }

        //  strOrderItemCount = String.valueOf(getIntent().getIntExtra("order_count", 0));
        //  txt_toatal.setText(strOrderItemCount);

        //  strInvoiceAmount = getIntent().getStringExtra("invoice_amount");
        // inv_amount.setText(strInvoiceAmount);

        strCustomerName = getIntent().getStringExtra("customer_name");
        customer_name.setText(strCustomerName);
        /* Get Current Location Latitude and Longitude */

        get_current_lat_long();
        // getCurrentLocationLatLong();
        //  getLocation();
        sqLiteHandler = new SQLiteHandler(this);
        //  new checkInternetConn().execute(0);
    }

    private void get_current_lat_long() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled||isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1, this);


            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {

                String adress = LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), this);
            }

        } else {
            //showSettingsAlert();
            Intent intent = new Intent(SaveDeliveryActivity.this, LocationActivity.class);
            startActivity(intent);
        }
    }


    /* RecyclerView Adapter */
    public void fill_payment_list(final List<m_delivery_invoice_list> posts_s) {
        if (posts_s.size() > 0) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new SelectedDeliveryListAdapter(this, posts_s));
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
                    selfie.setEnabled(true);
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
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();

                //bitmap1=decodeFile(fileUri.getPath());

                if (bitmap1 != null) {

                    Log.d("bitmap_image01", bitmap1.toString());
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

        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {


                //if qrcode has nothing in it
                if (result.getContents() == null) {
                    Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                    txt_scanresult.setText("Scan Result Not Found");
                } else {
                    //if qr contains data
                    try {
                        //converting the data to json
                        JSONObject obj = new JSONObject(result.getContents());
                        String scandata = obj.toString();
                        //setting values to textviews
                        Toast.makeText(getApplicationContext(), "scan" + obj.toString(), Toast.LENGTH_SHORT).show();
                        //   Log.d("scan",scandata);

                        txt_scanresult.setText("Scan Result: " + scandata);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //if control comes here
                        //that means the encoded format not matches
                        //in this case you can display whatever data is available on the qrcode
                        //to a toast
                        Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
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

            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream); //compress to which format you want.
                byte[] byte_arr = stream.toByteArray();

                selfie_url = Base64.encodeToString(byte_arr, Base64.NO_WRAP).trim();
                //Toast.makeText(getApplicationContext(), "selfie:" + selfie_url, Toast.LENGTH_SHORT).show();

                //image=image.trim();
                Log.e("image_str", "" + selfie_url);
//                JSONObject jsonObject=new JSONObject();
//                try
//                    jsonObject.put("image",selfie_url);
//                    Log.e("image",jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Bitmap decodeFile(String path) {//you can provide file path here

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
                bitmap = Bitmap.createScaledBitmap(bm, 200, 100, false);
//                selfie.setAdjustViewBounds(true);
//                selfie.setImageBitmap(bitmap);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                //    Log.e("in orientation", "" + orientation);
//                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
//                        bm.getHeight(), m, true);
                Bitmap.createScaledBitmap(bm, 200, 100, false);
//                selfie.setAdjustViewBounds(true);
//                selfie.setImageBitmap(bitmap);

                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                //        Log.e("in orientation", "" + orientation);
//                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
//                        bm.getHeight(), m, true);
                Bitmap.createScaledBitmap(bm, 200, 100, false);
//                selfie.setAdjustViewBounds(true);
//                selfie.setImageBitmap(bitmap);

                return bitmap;
            }

            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

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

            imgPath = mediaFile.toString();
            Log.d("image_filename01 ", imgPath);
            Log.d("image_filename02 ", mediaFile.toString());
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

                Log.d("signature11", StoredPath);
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
        Intent intent = new Intent(SaveDeliveryActivity.this, Order_details.class);
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
                    if (Utility.internetCheck(this)) {
                        saveDeliveryOnline();
                    } else {
                        saveDeliveryOffline();
                    }
                } else {
                    OGtoast.OGtoast("Please select a Reason", this);
                }
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


    /* Save Delivery Online */
    private void saveDeliveryOnline() {
        Delievry_Doc_Id = "OG_" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
        Log.d("Delievry_Doc_Id", Delievry_Doc_Id);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Delivery_doc_no", Delievry_Doc_Id);
            Log.i("Delivery_Date", "---" + currentDate());
            jsonObject.put("Delivery_date", currentDate);
            jsonObject.put("Order_Image", selfie_url.toString().trim());
            jsonObject.put("Sign_Image", sign_url);
            if(currentLocLat==0.0&&currentLocLong==0.0){
                if(pref.getString("lat",null)!=null&&pref.getString("long",null)!=null){
                    String lat=pref.getString("lat",null);
                    String longitude = pref.getString("long",null);
                    jsonObject.put("Latitude", lat);
                    jsonObject.put("Longitude", longitude);
                }else{
                    jsonObject.put("Latitude", currentLocLat);
                    jsonObject.put("Longitude", currentLocLong);
                }
            }else{
                jsonObject.put("Latitude", currentLocLat);
                jsonObject.put("Longitude", currentLocLong);

            }

            if (DeliveryStatus.equals("DEL")) {
                int id = rdb_g_delivery.getCheckedRadioButtonId();
                View radioButton = rdb_g_delivery.findViewById(id);
                int radioId = rdb_g_delivery.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rdb_g_delivery.getChildAt(radioId);
                String selection = (String) btn.getText();
                strStatus = "1";
                strDescription = formattedDatewithTime + " : " + selection;
                jsonObject.put("Status", strStatus);
                jsonObject.put("Description", strDescription);
            } else {
                int id = rdb_g_undelivery.getCheckedRadioButtonId();
                View radioButton = rdb_g_undelivery.findViewById(id);
                int radioId = rdb_g_undelivery.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rdb_g_undelivery.getChildAt(radioId);
                String selection = (String) btn.getText();
                strStatus = "2";
                strDescription = formattedDatewithTime + " : " + selection;
                jsonObject.put("Status", strStatus);
                jsonObject.put("Description", strDescription);
                Log.i("JsonObject", "--" + jsonObject.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray1 = new JSONArray();
        for (m_delivery_invoice_list m_pendingbills : Constant.selectedDeliveryInvoice) {
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("DeliveryId", m_pendingbills.getDeliveryId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray1.put(jsonObject1);
        }
        try {
            jsonObject.put("Delivery_key", jsonArray1);
            jsonArray.put(jsonObject);
            Log.d("Del_location1111", jsonArray.toString());
            onlineDeliveryAPI(jsonArray, strDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Online Payment API */
    private void onlineDeliveryAPI(JSONArray jsonArray, final String Description) {
        Log.d("JSONArray", jsonArray.toString());
        String url = AppConfig.SAVE_ORDER_DELIVERY_SALESMAN;
        //String url = AppConfig.DailyCollections;
        try {
            Utility.showProgress(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Utility.hideProgress();
                if (response != null) {
                    try {
                        Log.d("Response11", response.toString());
                        String status = response.getJSONObject(0).getString("result");
                        if (status.equalsIgnoreCase("Failed")) {
                            paymentSuccessDialog("Failed", "Delivery Failed! Thanks");
                        } else if (status.equalsIgnoreCase("Success")) {
                            for (int i = 0; i < response.length(); i++) {
                                String Doc_ID = response.getJSONObject(i).getString("Delivery_doc_no");
                                InvoiceNo = response.getJSONObject(i).getString("DeliveryId");
                                String Flag2 = response.getJSONObject(i).getString("Status");
                                Log.d("Doc_ID", Doc_ID);
                                Log.d("InvoiceNo", InvoiceNo);
                                sqLiteHandler.updateFlagCollectInvoicesSalesman(InvoiceNo, Flag2);
                                sqLiteHandler.updateFlagDeliveryDescription(InvoiceNo, Description);
                                sqLiteHandler.deleteDeliverySalesmanByDocid(Doc_ID);
                                sqLiteHandler.deleteDeliverySelectedInvoiceByDocid(Doc_ID);
                                // saveDeliveryOffline();
                            }
                            paymentSuccessDialog("Success", "Status Updated! Thanks");
                            Log.d("location_test10", String.valueOf(currentLocLat) + " " + String.valueOf(currentLocLong) + " " + adress);
                            if(currentLocLat==0.0&&currentLocLong==0.0){
                                if(pref.getString("lat",null)!=null&pref.getString("long",null)!=null){
                                    String lat=pref.getString("lat",null);
                                    String longitude = pref.getString("long",null);
                                    get_deliveryLocation(lat, longitude, adress);
                                }
                            }else{
                                get_deliveryLocation(String.valueOf(currentLocLat), String.valueOf(currentLocLong), adress);

                            }

                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress();
                Toast.makeText(SaveDeliveryActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessKey);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    /* Current Date */
    private String currentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return sdf.format(Calendar.getInstance().getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();


//        Log.d("resume","resume");
//        if (saveLocation)
//        {
//            Log.d("resume","saveLocation");
//
//            getCurrentLocationLatLong();
//
//        }
    }

    private void loadData() {
        // List<m_call_activities> post_activiteis = new ArrayList<m_call_activities>();
        LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("delivery_post.json", getApplicationContext());
        String jsondata = _LoadJsonFromAssets.getJson();
        //    Log.d("DeliveryJson", jsondata);
        if (!jsondata.isEmpty()) {
            // insertDeliveryScheduleSQLite(jsondata.toString());
        }

    }

    /* Get Current Location Lat Long */
    /*private void getCurrentLocationLatLong() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        locationProvider = locationManager.getBestProvider(criteria, false);

        if (locationProvider != null && !locationProvider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(locationProvider);
            locationManager.requestLocationUpdates(locationProvider, 10000, 1, this);

            if (location != null) {
                onLocationChanged(location);
            } else {
                //  Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            }
        }
    }*/


    ////----        End Current Location Latitude and Longitude     ----////
    /* Current Time */
    private void currentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        currentTime = simpleDateFormat.format(calendar.getTime());
    }


    /* Save Delivery Locally */
    /* commented by jyott 20/6 */
//    private void saveDeliveryInSQLite(final String orderStatus) {
//        currentTime();
//
//        getCurrentLocationLatLong();
//        sqLiteHandler.insertIntoSaveDelivery(userId, Start_time, End_time, strCustomerName, Client_id, Chemist_id, Cal_plan_id, order_no, inv_no,
//                strOrderDate, strOrderItemCount, strInvoiceAmount, strStatus, strDescription, selfie_url, sign_url, currentTime,
//                String.valueOf(currentLocLat), String.valueOf(currentLocLong), noOfPackets, noOfCases);
//        if (DeliveryStatus.equalsIgnoreCase("NDEL")) {
//            order_undelivered_dialog();
//        } else {
//            order_confirmed_dialog();
//        }
//        sqLiteHandler.updateInvoiceListDelivery(inv_no, orderStatus);
//    }


    /* Get SQLite Saved Delivery to push online */
    private void getSavedDeliveryOffline() {
        Cursor cursor = sqLiteHandler.getSavedDeliveryOffline(userId, "1");
        int cursorCount = cursor.getCount();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String Delivery_docno = "";
        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                do {
                    // JSONObject jsonObject = new JSONObject();

                    try {
                        Delivery_docno = cursor.getString(2);
                        //  jsonObject.put("StockistID", cursor.getString(1));
                        jsonObject.put("Delivery_doc_no", Delivery_docno);
                        //  jsonObject.put("ErpchemistID", cursor.getString(3));
                        //  jsonObject.put("Package_count", cursor.getString(4));
                        //  jsonObject.put("ErpsalesmanID", cursor.getString(6));
                        jsonObject.put("Delivery_date", cursor.getString(7));
                        jsonObject.put("Order_Image", cursor.getString(8));
                        jsonObject.put("Sign_Image", cursor.getString(9));
                        jsonObject.put("Latitude", cursor.getString(10));
                        jsonObject.put("Longitude", cursor.getString(11));
                        jsonObject.put("Status", cursor.getString(5));
                        strDescription = cursor.getString(12);
                        jsonObject.put("Description", strDescription);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Cursor cursor1 = sqLiteHandler.getSavedDeliveryInvoicesOffline(Delivery_docno);
                    JSONObject jsonObject1 = null;
                    JSONArray jsonArray1 = null;
                    if (cursor1.getCount() > 0) {

                        jsonArray1 = new JSONArray();
                        jsonObject1 = new JSONObject();
                        if (cursor1.moveToFirst()) {
                            do {
                                try {
                                     InvoiceNo = cursor1.getString(7);
                                    jsonObject1.put("DeliveryId", InvoiceNo);
                                    // jsonObject1.put("InvoiceNo", invoiceNo);
                                    jsonArray1.put(jsonObject1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } while (cursor1.moveToNext());
                        }
                    }
//                    jsonObject.put("Delivery_key", jsonArray1);
//                    jsonArray.put(jsonObject);
//                   // jsonArray.put(jsonArray1);
//                    Log.d("postArray", jsonArray.toString());
//                    onlineDeliveryAPI(jsonArray);
                    try {
                        jsonObject.put("Delivery_key", jsonArray1);
                        jsonArray.put(jsonObject);
                        Log.d("jsonArray", jsonArray.toString());
                        // jsonArray.put(jsonArray1);
                        //hide
                        onlineDeliveryAPI(jsonArray, strDescription);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }
        }


    }

//
//    /* Server Response */
//    public void onSuccess_json_array(String f_name, JSONArray response) {
//        if (response != null) {
//            //  Log.e("Response12",response.toString());
//            try {
//                if (f_name.equals(AppConfig.SAVE_ORDER_DELIVERY)) {
//
//                    Cursor cursor = sqLiteHandler.getSavedDelivery();
//                    int cursorCount = cursor.getCount();
//                    if (postSQLite == 1) {
//                        if (cursorCount > 0) {
//                            sqLiteHandler.deleteSavedDelivery();
//                        }
//                    }
//
//                    for (int i = 0; i < response.length(); i++) {
//                        res = response.getJSONObject(i).getString("result");
//                    }
//                    if (res.contains("updated") && DeliveryStatus.equals("DEL")) {
//                        //Log.e("res","sucesssss");
//                        order_confirmed_dialog();
//                    } else {
//                        order_undelivered_dialog();
//                    }
//                }
//            } catch (Exception e) {
//
//            }
//        }
//    }


    /* Successfully Delivered Dialog */
    void order_confirmed_dialog() {
        new AlertDialog.Builder(SaveDeliveryActivity.this)
                .setTitle("Delivery")
                .setCancelable(false)
                .setMessage("Status Updated.")
                // .setMessage("Delivered successfully.")
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
        new AlertDialog.Builder(SaveDeliveryActivity.this)
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

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.NO_WRAP);
        Log.d("temp", temp);
        return temp;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //selfie.setImageBitmap(bitmap);
            case R.id.imageView:

                Log.d("click_signview01", "click on camera img view");
                show_zoomDialog();

                break;

            case R.id.imageView2:

                Log.d("click_signview02", "click on sign img view");
                //   show_zoomDialog();

                show_zoomDialogSignimg();

                break;


        }

    }

    private void show_zoomDialogSignimg() {

        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SaveDeliveryActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                mBuilder.setView(mView);
                //PhotoView photoView = mView.findViewById(R.id.imageView);
                @SuppressLint("WrongViewCast") PhotoView photoView = (PhotoView) mView.findViewById(R.id.imageViewphoto);

                Bitmap myBitmap = Bitmap.createScaledBitmap(bitmap2, 250, 600, false);

                photoView.setImageBitmap(myBitmap);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    private void show_zoomDialog() {

        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SaveDeliveryActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                mBuilder.setView(mView);
                //PhotoView photoView = mView.findViewById(R.id.imageView);
                @SuppressLint("WrongViewCast") PhotoView photoView = (PhotoView) mView.findViewById(R.id.imageViewphoto);

                Bitmap myBitmap = BitmapFactory.decodeFile(fileUri.getPath());

                //               Log.d("image_filename04 " ,fileUri.getPath());
                photoView.setImageBitmap(myBitmap);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

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
                if (bitmap2 != null)

                    signature.setAdjustViewBounds(true);
                signature.setImageBitmap(Bitmap.createScaledBitmap(bitmap2, 200, 100, false));
                sign_url = BitMapToString(bitmap2);
                //   sign_url=sign_url.trim();
                //  byte[] ImagBytes =  ImageUtils.compressImage(StoredPath);
                // byte [] b=baos.toByteArray();
                // sign_url= Base64.encodeToString(ImagBytes, Base64.DEFAULT);
                Log.e("sign_url", sign_url);
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
            super.onPostExecute(result);
            if (InternetConnection.checkConnection(getApplication())) {
                getSavedDeliveryOffline();
            }
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

    /* Total Amount Count */
    private void totalDataCount(List<m_delivery_invoice_list> post) {
//        int totalBoxCount = 0;
//        int totalItems = 0;
//        int totalPackets = 0;
        for (int i = 0; i < post.size(); i++) {
            totalBoxCount = totalBoxCount + Integer.valueOf(post.get(i).getBoxCount());
            totalItems = totalItems + Integer.valueOf(post.get(i).getTotalItems());
            totalPackets = totalPackets + Integer.valueOf(post.get(i).getPackage_count());
        }

        Log.d("totalBoxCount", String.valueOf(totalBoxCount));
        Log.d("totalItems", String.valueOf(totalItems));
        Log.d("totalPackets", String.valueOf(totalPackets));

        txt_no_of_boxes.setText(String.valueOf(totalBoxCount));
        tx_total_items.setText(String.valueOf(totalItems));
        txt_no_of_packets.setText(String.valueOf(totalPackets));

        // return totalAmount;
    }


    /* Save Delivery Offline */
    private void saveDeliveryOffline() {
        String Flag1 = "offline";
        Delievry_Doc_Id = "OG_" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
        Log.d("Delievry_Doc_Id12", Delievry_Doc_Id);
        if (DeliveryStatus.equals("DEL")) {
            int id = rdb_g_delivery.getCheckedRadioButtonId();
            View radioButton = rdb_g_delivery.findViewById(id);
            int radioId = rdb_g_delivery.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rdb_g_delivery.getChildAt(radioId);
            String selection = (String) btn.getText();
            strStatus = "1";
            // str1 = "delivered";
            // strDescription = currentDate+":"+selection;
            strDescription = formattedDatewithTime + " : " + selection;

        } else {
            int id = rdb_g_undelivery.getCheckedRadioButtonId();
            View radioButton = rdb_g_undelivery.findViewById(id);
            int radioId = rdb_g_undelivery.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rdb_g_undelivery.getChildAt(radioId);
            String selection = (String) btn.getText();
            //  strStatus = "0";
            strStatus = "2";
            // str1 = "undelievred";
            strDescription = currentDate + ":" + selection;

        }
        for (m_delivery_invoice_list m_pendingbills : Constant.selectedDeliveryInvoice) {
            //remove description
            sqLiteHandler.insertSelectedDeliveryInvoices(Stockist_id, userId, Chemist_id, Delievry_Doc_Id, m_pendingbills.getInvoiceNo(), m_pendingbills.getDeliveryId(), strStatus);
            Log.d("invoiceno", m_pendingbills.getInvoiceNo());
            Log.d("invoicestatus", m_pendingbills.getDeliveryStatus());
            Log.d("invoicestatus1", m_pendingbills.getDeliveryId());
            sqLiteHandler.updateFlagCollectInvoicesSalesman(m_pendingbills.getDeliveryId(), strStatus);
            //offine update description in I1.(Delivery Schedule Invoices)
            sqLiteHandler.updateFlagDescriptionInvoices(m_pendingbills.getDeliveryId(), strDescription);

        }
       /* db.insetDataForLocation(User_id, call_plan_customer_id, trasaction_No, "Order", String.valueOf(Latitude),
                String.valueOf(Longitude), uID, address,Doc_Id);*/
        if(currentLocLat==0.0&&currentLocLong==0.0){
            if(pref.getString("lat",null)!=null&pref.getString("long",null)!=null){
                String lat=pref.getString("lat",null);
                String longitude = pref.getString("long",null);
                sqLiteHandler.insertDeliveryCollection(Stockist_id, userId, Chemist_id, Delievry_Doc_Id, userId, String.valueOf(totalPackets), currentDate, strStatus, selfie_url, sign_url, String.valueOf(lat), String.valueOf(longitude), strDescription, Flag1);
                get_deliveryLocation(String.valueOf(lat), String.valueOf(longitude), adress);
            }
        }else{

            sqLiteHandler.insertDeliveryCollection(Stockist_id, userId, Chemist_id, Delievry_Doc_Id, userId, String.valueOf(totalPackets), currentDate, strStatus, selfie_url, sign_url, String.valueOf(currentLocLat), String.valueOf(currentLocLong), strDescription, Flag1);
            get_deliveryLocation(String.valueOf(currentLocLat), String.valueOf(currentLocLong), adress);

        }

        paymentSuccessDialog("Offline Mode", "Due to the internet connection absence delivery stored in offline" +
                " mode.\n\nIn the presence of internet it will be sync automatically to the server");
    }

    /* Payment SQLite */
    private void paymentSuccessDialog(String title, String msg) {
        new AlertDialog.Builder(this).setCancelable(false).setTitle(title).setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.putExtra("Save", "SAVE_DELIVERY");
                        setResult(Activity.RESULT_OK, intent);
                        Log.d("SAVE", "done");
                        // saveLocation=true;
                        //  Log.d("SAVE11",saveLocation.toString());
                        dialog.dismiss();
                        finish();

                        Toast.makeText(getApplicationContext(), "Please Refresh List", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("stop", "stop");
//        if (saveLocation)
//        {
//            Log.d("stop","saveLocation");
//            getCurrentLocationLatLong();
//
//        }
    }

    /* onBackPressed */
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        this.finish();
    }

   /* private void get_location() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //  mLocationManager.getLastKnownLocation();
        Boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1, this);


            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {

                String adress = LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), this);
            }

        } else {
            showSettingsAlert();
        }

    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Please enable the Location");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OGtoast.OGtoast("Location Services not enabled !. Unable to get the location", getApplicationContext());
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void update_Current_locationn() {
        Log.d("GPS", "inside location");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        locationProvider = locationManager.getBestProvider(criteria, false);

        if (locationProvider != null && !locationProvider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //  locationManager.requestLocationUpdates(locationProvider, 10000, 1, this);
            Location location = locationManager.getLastKnownLocation(locationProvider);
            locationManager.requestLocationUpdates(locationProvider, 10000, 1, this);

            if (location != null) {
                Log.d("GPS", "location not null");
                onLocationChanged(location);
            } else {
                Log.d("GPS", "location null");
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            }
        }


    }*/

    ////----        Start Get Current Location Latitude and Longitude     ----////
    /*@Override
    public void onLocationChanged(Location location) {


        currentLocLat = location.getLatitude();
        currentLocLong = location.getLongitude();


        if (location != null) {
            currentLocLat = location.getLatitude();
            currentLocLong = location.getLongitude();

            String adress = LocationAddress.getAddressFromLocation(currentLocLat, currentLocLong, this);
            // Log.d("onLocationupdate12",adress);




        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/


    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

    }

    public void onSuccess_json_object(String f_name, JSONObject response) {

        if (response != null) {
            try {
                if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                    String status = response.getString("Status");
                    Log.d("location_test125", response.toString());
                    if (status.equalsIgnoreCase("success")) {
                        //  Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(getBaseContext(), "Location not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
            }

        }



        /*if (response != null) {
            if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                saveLocation = false;
                Log.d("location_updateDeliver5", response.toString());
                Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
            }
        }*/


    }


   /* @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d("click_takepicture04","Click image OnTouch  event");

        return true;

       // return false;
    }*/


    //Getting the scan results
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//
//
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                //if qr contains data
//                try {
//                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
//                    //setting values to textviews
//                  Toast.makeText(getApplicationContext(),"scan"+obj.toString(),Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

  /*  void getLocation() {
        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Creating an empty criteria object
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

            if(provider!=null && !provider.equals("")){

                // Get the location from the given provider
                Location location = locationManager.getLastKnownLocation(provider);

                locationManager.requestLocationUpdates(provider, 20000, 1, this);

                if(location!=null)
                    onLocationChanged(location);
                else
                    Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
            }
          //  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onLocationChanged(Location location) {
        //  locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            currentLocLat = location.getLatitude();
            currentLocLong = location.getLongitude();

            adress = LocationAddress.getAddressFromLocation(currentLocLat, currentLocLong, this);

            Log.d("location_test11", String.valueOf(currentLocLat));
            Log.d("location_test12", String.valueOf(currentLocLong));
            Log.d("location_test13", String.valueOf(location.getLatitude()));
            Log.d("location_test14", String.valueOf(location.getLongitude()));
            Log.d("location_test15", adress);

            //  get_deliveryLocation(String.valueOf(currentLocLat), String.valueOf(currentLocLong), adress);

            locationManager.removeUpdates(SaveDeliveryActivity.this);

        } catch (Exception e) {

        }

    }

    private void get_deliveryLocation(String sLatitude, String sLongitude, String sAddress) {


        try {

            getUniqueId = pref.getString("uniqueID", "");
            Log.d("location_test16", getUniqueId);
            Log.d("location_test17", sLatitude);
            Log.d("location_test18", sLongitude);
            Log.d("location_test19", sAddress);

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("Latitude", sLatitude);
            jsonParams.put("Longitude", sLongitude);
            jsonParams.put("CurrentLocation", sAddress);
            jsonParams.put("UserID", userId);
            jsonParams.put("CustID", Chemist_id);
            jsonParams.put("task", "Delivery");
            jsonParams.put("Tran_No", InvoiceNo);
            jsonParams.put("unqid", getUniqueId);

            Log.d("location_test120", jsonParams.toString());
            Log.d("location_test21", AppConfig.POST_UPDATE_CURRENT_LOCATION);
            if (Utility.internetCheck(this)) {
                MakeWebRequest.MakeWebRequest("Post",
                        AppConfig.POST_UPDATE_CURRENT_LOCATION,
                        AppConfig.POST_UPDATE_CURRENT_LOCATION
                        , jsonParams
                        , this
                        , false);
            } else {
                Log.e("dLocinstert", "insert");
                sqLiteHandler.insetDataForLocation(userId, Chemist_id, InvoiceNo, "Delivery", String.valueOf(sLatitude),
                        String.valueOf(sLongitude), getUniqueId, sAddress,Delievry_Doc_Id);
            }


            locationManager.removeUpdates(SaveDeliveryActivity.this);

            Log.d("location_test122", "stop loation update");

        } catch (Exception e) {

        }


    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SaveDeliveryActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}
