package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.databinding.library.baseAdapters.BR;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.model.PaymentGetSet;
import com.synergy.keimed_ordergenie.model.m_call_activities;
import com.synergy.keimed_ordergenie.model.m_customerlist;
import com.synergy.keimed_ordergenie.utils.LocationActivity;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;
import com.synergy.keimed_ordergenie.utils.get_current_location;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_ORDER_DELIVERY_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_PAYMENT_COLLECTION_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_TAKE_ORDER_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class CallPlanDetails extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    AppController globalVariable;
    private static final int NOTIFICATION_ID = 147894;
    private String s_response_array;
    String chemist_id = "";
    public static final String CHEMIST_STOCKIST_NAME = "chemist_stockist_name";
    SharedPreferences pref, prefSecond;
    public static final String SELECTED_CHEMIST_ID = "selected_chemist_id";
    String Start_time, End_time;
    private JSONObject taskStatusData = new JSONObject();
    private Boolean isAnyTaskCompleted = false;
    private String call_plan_date;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    String EXPDate;
    private int TaskPendingCount = 0;
    MyResultReceiver resultReceiver;
    public boolean locationStaus = true;
    String isLOcked;
    String blockReason;
    String Name = "";
    private boolean ChemistLocationClicked = false;
    ProgressDialog myPd_ring;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private long UPDATE_INTERVAL = 3000;  /* 15 secs */
    private long FASTEST_INTERVAL = 4000; /* 5 secs */

    @BindView(R.id.chrono)
    Chronometer stopWatch;

    @BindView(R.id.Client_name)
    TextView Client_name;

    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.ts_txt_time)
    TextView ts_txt_time;

    @BindView(R.id.del_txt_description)
    TextView del_txt_description;


    @BindView(R.id.txt_description)
    TextView txt_description;

    @BindView(R.id.cp_txt_description)
    TextView cp_txt_description;

    @BindView(R.id.cp_txt_time)
    TextView cp_txt_time;

    @BindView(R.id.txt_date_select)
    TextView txt_date_select;

    @BindView(R.id.pending_count)
    TextView pending_count;


    @BindView(R.id.Cust_adress)
    TextView Cust_adress;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.take_order)
    RelativeLayout take_order;

    @BindView(R.id.collect_payment)
    RelativeLayout collect_payment;

    @BindView(R.id.Order_delivery)
    RelativeLayout Order_delivery;

    @BindView(R.id.txt_Date)
    TextView txt_Date;

    @OnClick(R.id.Order_delivery)
    void order_delivery() {
        if (get_location()) {

            if (del_txt_description.getText().toString().equals("Completed")) {
                // OGtoast.OGtoast("This task is completed", CallPlanDetails.this);
                create_order_delivery();
            } else {
                create_order_delivery();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please Enable GPS Location", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.take_order)
    void order() {
        //   Toast.makeText(getApplicationContext(),"Enable GpS "+locationStaus,Toast.LENGTH_LONG).show();
        //   get_location();
        //   if (locationStaus) {
        if (get_location()) {
            if (del_txt_description.getText().toString().equals("Completed")) {
                // EXPDate = getIntent().getStringExtra("EXPDate");
                isLOcked = getIntent().getStringExtra("isLocked");
                blockReason = getIntent().getStringExtra("Block_Reason");

                if (isLOcked != null && isLOcked.equals("1")) {


                    if (blockReason != null) {
                        if (!blockReason.equals("NA")) {
                            Toast.makeText(getApplicationContext(), "This Chemist is Blocked - " + blockReason, Toast.LENGTH_LONG).show();
                            create_order();
                        } else {
                            create_order();
                            // Toast.makeText(getApplicationContext(), "Drug Licence will expired with in " + noDays + " Days", Toast.LENGTH_LONG).show();
                        }  // create_order();
                    } else {
                        create_order();
                    }


                } else {
                    create_order();
                }

                //    return  noDays;

            } else {
                //  EXPDate = getIntent().getStringExtra("EXPDate");
                isLOcked = getIntent().getStringExtra("isLocked");
                blockReason = getIntent().getStringExtra("Block_Reason");
                if (isLOcked != null && isLOcked.equals("1")) {


                    if (blockReason != null) {
                        if (!blockReason.equals("NA")) {
                            Toast.makeText(getApplicationContext(), "This Chemist is Blocked - " + blockReason, Toast.LENGTH_LONG).show();
                            create_order();
                        } else {
                            create_order();
                            // Toast.makeText(getApplicationContext(), "Drug Licence will expired with in " + noDays + " Days", Toast.LENGTH_LONG).show();
                        }  // create_order();
                    } else {
                        create_order();
                    }


                } else {
                    create_order();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Enable GPS Location ", Toast.LENGTH_LONG).show();

        }
       /* }else {
            Toast.makeText(getApplicationContext(),"Please Enable GPS Location ",Toast.LENGTH_LONG).show();
        }*/
    }

    @OnClick(R.id.collect_payment)
    void payment() {
        if (get_location()) {
            if (cp_txt_description.getText().toString().equals("Completed")) {
                collect_payment();

            } else {
                collect_payment();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Enable GPS Location", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.Order_delivery)
    void Order_delivery() {

    }

   /*@OnClick(R.id.take_selfie)
    void take_selfie()
    {
        Take_selfie();
    }*/

    @OnClick(R.id.stop_call)
    void end_Call() {
        end_call();
    }

    @OnClick(R.id.end_call)
    void endCall() {
        end_call();
    }

    @OnClick(R.id.btn_show_route)
    void show_route() {
        Intent i = new Intent(CallPlanDetails.this, MapsActivity.class);
        i.putExtra("response", s_response_array);
        startActivity(i);
    }


    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    TextView profile;
    ArrayList<m_customerlist> mModels;


    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private ArrayList<PaymentGetSet> arrayListPaymentSQLite;
    private PaymentGetSet paymentGetSet;
    private int exportPosition = 0;
    private JSONObject jsonObjectPayment;
    private int postSQLitePayment = 0;

    private LocationManager locationManager;
    private String locationProvider;
    private double currentLocLat = 0;
    private double currentLocLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_plan_details);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // prefSecond = getApplicationContext().getSharedPreferences("MY PREF",MODE_PRIVATE);


        //  Toast.makeText(getApplicationContext(),"Toast"+EXPDate,Toast.LENGTH_LONG).show();

        globalVariable = (AppController) getApplicationContext();
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }


        /* Initialize SQLite Class */
        sqLiteHandler = new SQLiteHandler(this);


        if (Utility.internetCheck(this)) {
            new get_current_location(CallPlanDetails.this);
        }
        profile = (TextView) findViewById(R.id.view_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CallPlanDetails.this, ProfileActivity.class);
//                startActivity(intent);
                mModels = new ArrayList<m_customerlist>();
                for (int i = 0; i < mModels.size(); i++) {
                    Log.e("ChemistId", mModels.get(i).getChemist_id());
                }
            }
        });
        // new get_current_location(CallPlanDetails.this);
        editor = pref.edit();

        String task = getIntent().getStringExtra("task_status");
        call_plan_date = getIntent().getStringExtra("filter_start_date");


        try {
            if (task != null) {
                taskStatusData = new JSONObject(task);
            } else {
                taskStatusData.put("1", "no");
                taskStatusData.put("2", "no");
                taskStatusData.put("3", "no");
            }
        } catch (Exception e) {

        }

        resultReceiver = new MyResultReceiver(null);
        show_hide_tasks();
        SharedPreferences prefs = getSharedPreferences("MY PREF", MODE_PRIVATE);
        String client_name = prefs.getString("client_name", null);
        chemist_id = prefs.getString("chemist_id", null);
        // EXPDate=prefSecond.getString("EXPDate",null);
        //txt_Date.setText(EXPDate);

        // Log.d("printpaymentId", chemist_id);

        if (client_name != null) {
            Name = prefs.getString(CLIENT_NAME, "");//"No name defined" is the default value.
            //0 is the default value.
        }
        // Client_name.setText(getIntent().getStringExtra("client_name"));
        Client_name.setText(Name);
        Cust_adress.setText(getIntent().getStringExtra("client_location"));

        create_notification();

        txt_date_select.setText(dateFormat.format(Calendar.getInstance().getTime()));
        s_response_array = getIntent().getStringExtra("response");
        stopWatch.setText("00:00:00");

        stopWatch.start();
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                cArg.setText(hh + ":" + mm + ":" + ss);

            }
        });


    }

    private void fill_call_plan_activities(final List<m_call_activities> cal_activity_array) {
        final RecyclerView.Adapter<BindingViewHolder> adapter_activities = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                ViewDataBinding binding = null;
                LayoutInflater inflater = LayoutInflater.from(CallPlanDetails.this);
                binding = DataBindingUtil
                        .inflate(inflater, R.layout.adapter_call_activities_list, parent, false);

                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(BindingViewHolder holder, int position) {
                m_call_activities call_activity_list = cal_activity_array.get(position);
                holder.getBinding().setVariable(BR.v_call_plan_activities, call_activity_list);
                holder.getBinding().executePendingBindings();
            }

            @Override
            public int getItemCount() {
                return cal_activity_array.size();
            }

            @Override
            public int getItemViewType(int position) {

                return position;
            }
        };

       /* rv_call_activities.setLayoutManager(new LinearLayoutManager(this));
        rv_call_activities.setHasFixedSize(false);
        rv_call_activities.setLayoutManager(new LinearLayoutManager(CallPlan.this));
        rv_call_activities.setAdapter(adapter_activities);
*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class BindingViewHolder extends RecyclerView.ViewHolder {

        public BindingViewHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }
    }


    /* Create Order */
    private void create_order() {
        Intent i = new Intent(this, Create_Order_Salesman.class);
        i.putExtra("Call_plan_id", chemist_id);
        i.putExtra("receiver", resultReceiver);
        i.putExtra("flag", "callplan");
        i.putExtra("client_id", getIntent().getStringExtra("chemist_id"));
        i.putExtra(CHEMIST_STOCKIST_NAME, Name);
        txt_time.setText("Task Started at : " + dateFormat.format(Calendar.getInstance().getTime()));

        Start_time = dateFormat.format(Calendar.getInstance().getTime());
        txt_description.setText("Task in progress");
        globalVariable.setCall_plan_Started(true);
        startActivity(i);
    }


    /* Delivery Click */
    private void create_order_delivery() {
        Start_time = dateFormat.format(Calendar.getInstance().getTime());
        Intent i = new Intent(this, Order_list_delivery.class);
        i.putExtra("type", "Delivery");
        i.putExtra("Call_plan_id", getIntent().getStringExtra("Call_plan_id"));
        i.putExtra("receiver", resultReceiver);
        i.putExtra("s_response_array", s_response_array);
        i.putExtra("Start_time", Start_time);
        i.putExtra(CLIENT_NAME, Name);
        i.putExtra("call_plan_task", true);
        i.putExtra(SELECTED_CHEMIST_ID, getIntent().getStringExtra("chemist_id"));
        i.putExtra(CHEMIST_STOCKIST_NAME, getIntent().getStringExtra("client_name"));
        i.putExtra("chemistId", getIntent().getStringExtra("chemist_id"));
        ts_txt_time.setText("Task Started at : " + dateFormat.format(Calendar.getInstance().getTime()));
        del_txt_description.setText("Task in progress");
        globalVariable.setCall_plan_Started(true);
        startActivity(i);
    }

    /* Payment Collection Click */
    private void collect_payment() {
        cp_txt_time.setText("Task Started at : " + dateFormat.format(Calendar.getInstance().getTime()));
        // Intent i = new Intent(this, IndividualPendingBillsActivity.class);
        Intent i = new Intent(this, SelectedpaymentList.class);
        i.putExtra("receiver", resultReceiver);
        i.putExtra("call_plan_task", true);
        i.putExtra("flag", "isFromCallPlan");
        i.putExtra(CLIENT_NAME, Name);
        i.putExtra("Call_plan_id", getIntent().getStringExtra("Call_plan_id"));
        i.putExtra("chemist_id", chemist_id);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("chemist_id", chemist_id);
        editor.putString("flag", "isFromCallPlan");
        editor.putString(CLIENT_NAME, Name);
        editor.apply();
        startActivity(i);
    }


    private void Order_delivery_start() {
        Intent i = new Intent(this, Payment_Option.class);
        startActivity(i);
    }

    private void Take_selfie() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }


    private void end_call() {
        //  get_location() {
        if (get_location()) {
            globalVariable.setCall_plan_Started(false);
            NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nMgr.cancel(NOTIFICATION_ID);

            if (isAnyTaskCompleted) {
                try {
                    //  JSONArray j_arr = new JSONArray(s_response_array);
                    //Log.e("j_arr", j_arr.toString());
//commented by harish and praksh sir
//                new SaveCallPlan(CallPlanDetails.this, call_plan_date, j_arr.getJSONObject(0).getString("StockistCallPlanID"),
//                        getIntent().getStringExtra("chemist_id"), pref.getString(USER_ID, "0"), Start_time, End_time, "1", taskStatusData.toString());
                    JSONArray j_arr = new JSONArray(s_response_array);
                    //  Log.d("click_endcall12", j_arr.toString());
                    //   Log.d("click_endcall13", String.valueOf(j_arr));

                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("Date", call_plan_date);
                    jsonParams.put("StockistCallPlanID",
                            j_arr.getJSONObject(0).getString("StockistCallPlanID"));
                    jsonParams.put("ChemistID", getIntent().getStringExtra("chemist_id"));
                    jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                    jsonParams.put("InTime", Start_time);
                    jsonParams.put("OutTime", End_time);
                    jsonParams.put("Status", "1");
                    jsonParams.put("TaskStatus", taskStatusData.toString());
// Log.e("jsonParams",jsonParams.toString());
//globalVariable.setToken(null);
                    //   Toast.makeText(getApplicationContext(),"Locat"+ )
                    MakeWebRequest.MakeWebRequest("Post",
                            AppConfig.POST_SAVE_CALL_PLAN,
                            AppConfig.POST_SAVE_CALL_PLAN, jsonParams,
                            CallPlanDetails.this, true);


                } catch (Exception e) {
                    e.toString();
                }
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Please Enable GPS Tracker", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean get_location() {
        boolean check = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //  mLocationManager.getLastKnownLocation();
        Boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled || isNetworkEnabled) {
            if (checkPermission()) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();
                check = true;
            } else {
                requestPermission();
                check = false;
            }

        } else {
           // showSettingsAlert();
            Intent intent = new Intent(CallPlanDetails.this, LocationActivity.class);
            startActivity(intent);
            check = false;
        }
        return check;

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

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }

        return true;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean location = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if (location) {
                        // get_location();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                showMessageOKCancel("You need to allow access permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermission();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CallPlanDetails.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }


    @Override
    public void onLocationChanged(Location location) {


        if (location != null) {
            currentLocLat = location.getLatitude();
            currentLocLong = location.getLongitude();

            String adress = LocationAddress.getAddressFromLocation(currentLocLat, currentLocLong, this);

            if (ChemistLocationClicked) {
                try {
                    JSONObject jsonParams = new JSONObject();
                    ChemistLocationClicked = false;
                    // jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                    jsonParams.put("UserID", getIntent().getStringExtra("chemist_id"));
                    jsonParams.put("Latitude", String.valueOf(currentLocLat));
                    jsonParams.put("Longitude", String.valueOf(currentLocLong));
                    jsonParams.put("CurrentLocation", adress);
                    Log.d("onLocationupdate13", adress);
                    Log.d("onLocationupdate14", jsonParams.toString());
                    //globalVariable.setToken(null);

                    // HARISH COMMENT
                    // MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_USER_LOCATION, AppConfig.POST_UPDATE_USER_LOCATION, jsonParams, this, false);

                    MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CHEMIST_LOCATION, AppConfig.POST_UPDATE_CHEMIST_LOCATION, jsonParams, this, true);
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                        mGoogleApiClient.disconnect();
                    }


                    Log.d("onLocationupdate15", "Stop service location");
                    // USE THIS API
                    // MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CHEMIST_LOCATION, AppConfig.POST_UPDATE_CHEMIST_LOCATION, jsonParams, this, false);
                } catch (Exception e) {

                }
            } else {
                try {
                    JSONObject jsonParams = new JSONObject();
                    SharedPreferences pref = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    String uID = pref.getString("UUID", "0");
                    Log.d("location_update1111", uID);

                    String trasaction_No = pref.getString("Transaction_No", "");

                    String callplan = "stop_callplan";

                    //  new Updatelocationv1(CallPlanDetails.this,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),adress,pref.getString(USER_ID, "0"),locationcustID,callplan,String.valueOf(mUId));

                    Log.d("callplan_Location112", chemist_id);
                    jsonParams.put("Latitude", String.valueOf(location.getLatitude()));
                    jsonParams.put("Longitude", String.valueOf(location.getLongitude()));
                    jsonParams.put("CurrentLocation", adress);
                    jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                    jsonParams.put("CustID", chemist_id);
                    jsonParams.put("task", callplan);
                    jsonParams.put("Tran_No", trasaction_No);
                    jsonParams.put("unqid", " ");

                    Log.d("callplan_Location1222", AppConfig.POST_UPDATE_CURRENT_LOCATION);
                    Log.d("callplan_Location1333", String.valueOf(jsonParams));


                    MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CURRENT_LOCATION, AppConfig.POST_UPDATE_CURRENT_LOCATION, jsonParams, this, false);

                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                        mGoogleApiClient.disconnect();
                    }


                } catch (Exception e) {
                }
            }
        }

    }


    void create_notification() {
        // Intent intent = new Intent(this,CallPlanDetails.class);
//        Intent intent = new Intent();
//
//       // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//       // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent1 = new Intent(this, CallPlanDetails.class);
        intent1.setAction(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent1, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logo)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle("OG Call")
                .setContentText("Call started for : " + Name)
                // Set Ticker Message
                .setTicker("OrderGenie")
                .setUsesChronometer(true)
                .setProgress(100, 100, true)
                // Dismiss Notification
                .setAutoCancel(false)
                // Set PendingIntent into Notification
                // .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this.getIntent()), PendingIntent.FLAG_UPDATE_CURRENT));

                .setContentIntent(pendingIntent);


        //****** .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, CallPlanDetails.class), PendingIntent.FLAG_UPDATE_CURRENT));
        //.setContentIntent(contentIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder.setOngoing(true);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_call_plandetail, menu);
        MenuItem item = menu.findItem(R.id.action_location);
        if (getIntent().getStringExtra("isLocation") != null &&
                getIntent().getStringExtra("isLocation").trim().length() > 0 && getIntent().getStringExtra("isLocation").equalsIgnoreCase("Y")) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (get_location()) {
                    ChemistLocationClicked = true;
                    myPd_ring = ProgressDialog.show(CallPlanDetails.this, "Please wait",
                            "Updating Location..", true);
                    myPd_ring.setCancelable(false);
                    myPd_ring.show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (myPd_ring != null && myPd_ring.isShowing()) {
                                myPd_ring.dismiss();
                            }


                        }
                    }, 10000);
                } else {
                    ChemistLocationClicked = false;
                }
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            back_callPlan_currentCall();
            //Log.d("todaybackwindow","Show Here dialog");
            //  finish();
        }


        return super.onOptionsItemSelected(menuItem);
    }

    private void back_callPlan_currentCall() {

        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to End Current call ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        end_Call();
                        finish();
                        //  System.exit(0);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .show();

    }

    @Override
    public void onBackPressed() {

        back_callPlan_currentCall();
        //end_Call();
//        super.onBackPressed();
        // finish();
    }


    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
    }


    class MyResultReceiver extends ResultReceiver {
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 200) {
                isAnyTaskCompleted = true;
                TaskPendingCount = TaskPendingCount - 1;
                pending_count.setText("" + TaskPendingCount);
                txt_description.setText("Completed");

                txt_time.setText("Call completed at : " + dateFormat.format(Calendar.getInstance().getTime()));
                End_time = dateFormat.format(Calendar.getInstance().getTime());
                try {
                    //  JSONArray j_arr = new JSONArray(s_response_array);

                    taskStatusData.put("1", "yes");
//                    editor.putBoolean(IS_TAKE_ORDER_ASSIGNED, true);
//                    editor.commit();
//                    Log.e("IS_TAKE_ORDER_ASSIGNED",pref.getBoolean(IS_TAKE_ORDER_ASSIGNED,false)+"");
                    // new SaveCallPlan(CallPlanDetails.this, j_arr.getJSONObject(0).getString("StockistCallPlanID"),
                    //    getIntent().getStringExtra("client_id"), pref.getString(USER_ID, "0"), Start_time, End_time, "2", "1", "yes");
                } catch (Exception e) {
                    e.toString();
                }
            } else if (resultCode == 400) {
                isAnyTaskCompleted = true;
                TaskPendingCount = TaskPendingCount - 1;

                pending_count.setText("" + TaskPendingCount);
                del_txt_description.setText("Completed");

                ts_txt_time.setText("Call completed at : " + dateFormat.format(Calendar.getInstance().getTime()));
                End_time = dateFormat.format(Calendar.getInstance().getTime());
                try {
                    // JSONArray j_arr = new JSONArray(s_response_array);

                    taskStatusData.put("3", "yes");
//                    editor.putBoolean(IS_ORDER_DELIVERY_ASSIGNED, true);
//                    editor.commit();
//
//                    Log.e("IS_ORDER_DELIVER",pref.getBoolean(IS_ORDER_DELIVERY_ASSIGNED,false)+"");


                    //   new SaveCallPlan(CallPlanDetails.this, j_arr.getJSONObject(0).getString("StockistCallPlanID"),
                    // getIntent().getStringExtra("chemist_id"), pref.getString(USER_ID, "0"), Start_time, End_time, "2", "3", "yes");

                } catch (Exception e) {
                    e.toString();
                }
            } else if (resultCode == 300) {

                isAnyTaskCompleted = true;
                TaskPendingCount = TaskPendingCount - 1;

                pending_count.setText("" + TaskPendingCount);
                cp_txt_description.setText("Completed");

                cp_txt_time.setText("Call completed at : " + dateFormat.format(Calendar.getInstance().getTime()));
                End_time = dateFormat.format(Calendar.getInstance().getTime());
                try {
                    // JSONArray j_arr = new JSONArray(s_response_array);

                    taskStatusData.put("2", "yes");

//                    editor.putBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, true);
//                    editor.commit();
//
//                    Log.e("IS_PAYMENT_COLLECTION",pref.getBoolean(IS_PAYMENT_COLLECTION_ASSIGNED,false)+"");
                    //  new SaveCallPlan(CallPlanDetails.this, j_arr.getJSONObject(0).getString("StockistCallPlanID"),
                    //    getIntent().getStringExtra("chemist_id"), pref.getString(USER_ID, "0"), Start_time, End_time, "2", "2", "yes");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /* Task Status */
    void show_hide_tasks() {
        if (pref.getBoolean(IS_TAKE_ORDER_ASSIGNED, false)) {
            take_order.setVisibility(View.VISIBLE);
            TaskPendingCount = TaskPendingCount + 1;
        } else {
            take_order.setVisibility(View.GONE);
        }
        if (pref.getBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, false)) {
            collect_payment.setVisibility(View.VISIBLE);
            TaskPendingCount = TaskPendingCount + 1;
        } else {
            collect_payment.setVisibility(View.GONE);
        }
        if (pref.getBoolean(IS_ORDER_DELIVERY_ASSIGNED, false)) {
            TaskPendingCount = TaskPendingCount + 1;
            Order_delivery.setVisibility(View.VISIBLE);
        } else {
            Order_delivery.setVisibility(View.GONE);
        }

        try {
            if (taskStatusData.getString("1").equals("yes")) {
                txt_description.setText("Completed");
                TaskPendingCount = TaskPendingCount - 1;
            }
            if (taskStatusData.getString("2").equals("yes")) {
                cp_txt_description.setText("Completed");
                TaskPendingCount = TaskPendingCount - 1;
            }
            if (taskStatusData.getString("3").equals("yes")) {
                del_txt_description.setText("Completed");
                TaskPendingCount = TaskPendingCount - 1;
            }
            if (taskStatusData.getString("1").equals("yes") && taskStatusData.getString("2").equals("yes") && taskStatusData.getString("3").equals("yes")) {
                TaskPendingCount = 3;
             /*   editor.remove(IS_TAKE_ORDER_ASSIGNED);
                editor.remove(IS_PAYMENT_COLLECTION_ASSIGNED);
                editor.remove(IS_ORDER_DELIVERY_ASSIGNED);*/
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pending_count.setText("" + TaskPendingCount);
    }


    /* APIs Success Responses */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        if (response != null) {
            try {
                //Log.e("Saveplan", response.toString());
                if (f_name.equals(AppConfig.POST_SAVE_CALL_PLAN)) {

                } else if (f_name.equals(AppConfig.SAVE_PAYMENT)) {
                    if (postSQLitePayment == 1) {
                        updateStatusPayment();
                    }
                } else if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                    String status = response.getString("Status");
                    Log.d("location_update1444", response.toString());
                    if (status.equalsIgnoreCase("success")) {
                        //      Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        //    Toast.makeText(getBaseContext(), "Location not Updated", Toast.LENGTH_SHORT).show();
                    }
                } else if (f_name.equalsIgnoreCase(AppConfig.POST_UPDATE_CHEMIST_LOCATION)) {
                    if (response != null) {
                        if (myPd_ring != null && myPd_ring.isShowing()) {
                            myPd_ring.dismiss();
                        }
                        Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                        sqLiteHandler.updateLocationstatusChemistList(getIntent().getStringExtra("chemist_id"), "Y");
                    }
                }

            } catch (Exception e) {
                //e.toString();
                e.printStackTrace();
            }
        } else {
            if (f_name.equalsIgnoreCase(AppConfig.POST_UPDATE_CHEMIST_LOCATION)) {
                if (myPd_ring != null && myPd_ring.isShowing()) {
                    myPd_ring.dismiss();
                }
                Toast.makeText(getBaseContext(), "Location not Updated Please check internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            try {
                if (f_name.equals(AppConfig.GET_STOCKIST_CALL_PLAN)) {

                } else if (f_name.equals(AppConfig.SAVE_ORDER_INVOICE)) {
                    if (postSQLitePayment == 1) {
                        sqLiteHandler.deletePaymentRecord(paymentGetSet.getInvoiceNo());
                        exportPosition = exportPosition + 1;
                        if (arrayListPaymentSQLite.size() != exportPosition) {
                            addDataInJsonFormat();
                        }
                    }
                }
            } catch (Exception e) {
                //e.toString();
                e.printStackTrace();
            }
        }
    }


    /* onStart */
    @Override
    protected void onStart() {
        super.onStart();
        new checkInternetConn().execute(0);
    }


    /* onPause */
    @Override
    protected void onPause() {
        super.onPause();
        new checkInternetConn().execute(0);
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
                //uploadSQLiteData();
            }
            super.onPostExecute(result);

            //Do it again!
            new checkInternetConn().execute(8000);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
    }


    /* Check Internet Connection on Current State */
    public static class InternetConnection {
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
            return false;
        }
    }


    ////----    Post Payment SQLite Data    ----////

    /* Upload SQLite Data To Server */
    /*private void uploadSQLiteData() {
        arrayListPaymentSQLite = new ArrayList<>();
        Cursor cursor = sqLiteHandler.getPaymentRecord(chemist_id);
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            //Log.d("RecordNotFound", ""+cursorCount);
        } else {
            if (cursor.moveToFirst()) {
                do {
                    PaymentGetSet paymentGetSet = new PaymentGetSet();
                    paymentGetSet.setUserId(cursor.getString(1));
                    paymentGetSet.setChemistId(cursor.getString(2));
                    paymentGetSet.setInvoiceNo(cursor.getString(3));
                    paymentGetSet.setInvoiceDate(cursor.getString(4));
                    paymentGetSet.setSavedDate(cursor.getString(5));
                    paymentGetSet.setTotalItems(cursor.getString(6));
                    paymentGetSet.setCashReceived(cursor.getString(7));
                    paymentGetSet.setTotalAmount(cursor.getString(8));
                    paymentGetSet.setBalanceAmount(cursor.getString(9));
                    paymentGetSet.setPaymentModeId(cursor.getString(10));
                    paymentGetSet.setPaymentMode(cursor.getString(11));
                    paymentGetSet.setChequeDate(cursor.getString(12));
                    paymentGetSet.setChequeNo(cursor.getString(13));
                    paymentGetSet.setBankId(cursor.getString(14));
                    paymentGetSet.setBankName(cursor.getString(15));
                    paymentGetSet.setPaymentId(cursor.getString(16));
                    paymentGetSet.setNarration(cursor.getString(17));
                    paymentGetSet.setStatus(cursor.getString(18));
                    paymentGetSet.setStockistId(cursor.getString(19));
                    paymentGetSet.setMicrNo(cursor.getString(20));

                    arrayListPaymentSQLite.add(paymentGetSet);

                    if (cursorCount == arrayListPaymentSQLite.size()) {
                        addDataInJsonFormat();
                    }
                } while (cursor.moveToNext());
            }
        }
    }*/

    /* Add Data in Json */
    private void addDataInJsonFormat() {
        paymentGetSet = arrayListPaymentSQLite.get(exportPosition);
        jsonObjectPayment = new JSONObject();
        try {
            jsonObjectPayment.put("Invoice_No", paymentGetSet.getInvoiceNo());
            jsonObjectPayment.put("Doc_No", "");
            jsonObjectPayment.put("Doc_Date", paymentGetSet.getInvoiceDate());
            jsonObjectPayment.put("PaymodeId", paymentGetSet.getPaymentModeId());
            jsonObjectPayment.put("PaymodeDesc", paymentGetSet.getPaymentMode());
            jsonObjectPayment.put("Chq_No", paymentGetSet.getChequeNo());
            jsonObjectPayment.put("Chq_Date", paymentGetSet.getChequeDate());
            jsonObjectPayment.put("BankAccountID", "");
            jsonObjectPayment.put("Bank_Id", paymentGetSet.getBankId());
            jsonObjectPayment.put("BankName", paymentGetSet.getBankName());
            jsonObjectPayment.put("Amount", paymentGetSet.getCashReceived());
            jsonObjectPayment.put("Narration", paymentGetSet.getNarration());
            jsonObjectPayment.put("PaymentID", paymentGetSet.getPaymentId());
            jsonObjectPayment.put("Stockist_Client_ID", paymentGetSet.getStockistId());
            jsonObjectPayment.put("Chemist_ID", paymentGetSet.getChemistId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("postParamsJson", jsonObjectPayment.toString());
        if (Utility.internetCheck(this)) {
            postSQLitePayment = 1;
            MakeWebRequest.MakeWebRequest("Post", AppConfig.SAVE_PAYMENT, AppConfig.SAVE_PAYMENT, jsonObjectPayment, this, false);
        }
    }


    /* Get Payment Data From SQLite */
    private void updateStatusPayment() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("InvoiceNo", paymentGetSet.getInvoiceNo());
            jsonObject1.put("StockistID", paymentGetSet.getStockistId());
            jsonObject1.put("Status", "4");

            jsonArray.put(jsonObject1);

            jsonObject.put("invoices", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("updatedInvoiceStatus", jsonObject.toString());
        if (Utility.internetCheck(this)) {
            MakeWebRequest.MakeWebRequest("out_array", AppConfig.SAVE_ORDER_INVOICE, AppConfig.SAVE_ORDER_INVOICE,
                    null, this, false, jsonObject.toString());
        }
    }


}
