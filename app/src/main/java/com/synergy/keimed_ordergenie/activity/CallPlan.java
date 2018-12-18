package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.utils.LocationActivity;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.get_current_location;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_call_activities;
import com.synergy.keimed_ordergenie.model.m_callplans;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.LoadJsonFromAssets;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_ORDER_DELIVERY_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_PAYMENT_COLLECTION_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_TAKE_ORDER_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class CallPlan extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess, DatePickerDialog.OnDateSetListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @BindView(R.id.rv_callPlanlist)
    RecyclerView rv_callPlanlist;
    String login_type;
    private PopupWindow mPopupWindow;
    String statusflag = "online";
    private String User_id, Stockist_id;
    Date filter_start_date = Calendar.getInstance().getTime();
    AppController globalVariable;
    private Date current_date = Calendar.getInstance().getTime();
    String Flag = "offline";
    String strCopyCmstId;
    String strPostSet;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    boolean chkClick = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,dd LLL yyyy");
    long init;
    Boolean isTakOrder, isPayment, isDelivery;
    SimpleDateFormat sql_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    // SimpleDateFormat sql_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    LinearLayout lnrClick;
    List<m_callplans> posts;
    List<m_callplans> posts_completed;
    List<m_callplans> posts_pending;
    List<m_callplans> posts_final;
    private static final String FORMAT = "%02d:%02d:%02d";
    ImageView img_take_order;
    TextView txtCopy, txtPaste;
    RecyclerView.Adapter<BindingViewHolder> adapter;
    // TextView txtCopy,txtPaste;
    // int cartSize ;
    ImageView img_payment, img_lock;
    ImageView img_delivery;
    private LinearLayout linearLayoutCart;
    private TextView textViewCartCount;

    private String s_response_array;
    private SearchView searchView;
    SharedPreferences pref;
    private Menu oMenu;
    private SQLiteHandler db;

    private LinearLayout lnr_start_call;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.btn_show_route)
    TextView btn_show_route;

    @BindView(R.id.txt_sel_date)
    TextView txt_sel_date;


    /* Cart Dao */
    private DaoSession daoSession;
    private ChemistCartDao chemistCartDao;

    /* Post SQLite Data */
    private int postDeliverySQLite = 0;
    private int postPaymentSQLite = 0;
    private JSONArray jsonArray;
    //private String invoiceArray = "";

    private LocationManager locationManager;
    private String locationProvider;
    private double currentLocLat = 0;
    private double currentLocLong = 0;
    String locationcustID;
    private UUID mUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_plan);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        /* Initialize SQLite Class */
        db = new SQLiteHandler(this);
        /* Initialize Dao Session Class */
        daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();


        //new get_current_location(CallPlan.this);
        txt_sel_date.setText(dateFormat.format(filter_start_date));
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        globalVariable = (AppController) getApplicationContext();
        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        if (login_type.equals(CHEMIST)) {


        } else {


          /*  MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                    AppConfig.GET_STOCKIST_CALL_PLAN +"["+User_id+","+get_day_number(filter_start_date)+",\""+ sdf.format(filter_start_date)+"\"]" , this, true);
*/
        }

        /*   http://www.ordergenie.co.in/api/stockistcallplandetails/getUserCallPlanDetails/[527,1,"2016-12-27"]*/



        /* Post SQLite Data's */
        //new checkInternetConn().execute(0);

    }


    @OnClick(R.id.txt_sel_date)
    void select_date() {
        try {
            current_date = sdf.parse(txt_sel_date.getText().toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            nYear = Integer.parseInt(sdYear.format(current_date));
            nMonth = Integer.parseInt(sdMonth.format(current_date)) - 1;
            Nday = Integer.parseInt(sdDay.format(current_date));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                CallPlan.this,
                nYear,
                nMonth,
                Nday
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setMaxDate(Calendar.getInstance());
    }

    @OnClick(R.id.btn_show_route)
    void onclcik_route() {
        //    Log.e("insidelocation", "hiii");
        Intent i = new Intent(CallPlan.this, MapsActivity.class);
        i.putExtra("response", s_response_array);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_call_plan, menu);


        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<m_callplans> filteredModelList = new ArrayList<>();
                for (m_callplans model : posts) {
                    final String text = model.getClient_LegalName().toLowerCase();
                    if (text.contains(newText)) {
                        filteredModelList.add(model);
                    }
                }
                fill_callplan(filteredModelList);

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_location:
                globalVariable.setFromMenuItemClick(true);
                new get_current_location(CallPlan.this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
      /* if (Utility.internetCheck(this)) {
           MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                    AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]", this, true);
     } else {
            *//* For Task Plan Enable In Offline *//*
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(IS_TAKE_ORDER_ASSIGNED, true);
            editor.putBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, true);
            editor.putBoolean(IS_ORDER_DELIVERY_ASSIGNED, true);
            getChemistList();
        }*/

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_TAKE_ORDER_ASSIGNED, true);
        editor.putBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, true);
        editor.putBoolean(IS_ORDER_DELIVERY_ASSIGNED, true);
        getChemistList();
    }


    /* Get Chemist List */
    private void get_callist_json(String response) {
        //  LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Call_Plan.json", CallPlan.this);
        String jsondata = response.toString();
        //Log.d("callPlanRecord", jsondata);
        if (!jsondata.isEmpty()) {
            //Log.d("RES", jsondata);
            try {
           /*  JSONArray j_array=new JSONArray(jsondata);
             for(int i=0;i<j_array.length();i++)
             {
                 JSONObject j_object=j_array.getJSONObject(i);
                 db.insertInto_call_plan(j_object.getString("Call_Id"),j_object.getString("Client_id"),j_object.getString("Location"),
                        0,"",j_object.getInt("Delivery"),j_object.getInt("Payment"),j_object.getInt("Return"),0);
             }*/

            } catch (Exception e) {
            }
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            posts = new ArrayList<m_callplans>();
            posts_final = new ArrayList<m_callplans>();
            posts_completed = new ArrayList<m_callplans>();
            posts_pending = new ArrayList<m_callplans>();
            posts = Arrays.asList(mGson.fromJson(jsondata, m_callplans[].class));
            posts_completed.clear();
            posts_pending.clear();
            posts_final.clear();

            for (int i = 0; i < posts.size(); i++) {
                if (posts.get(i).getTaskStatus() != null) {
                    try {
                        JSONObject taskStatusData = new JSONObject(posts.get(i).getTaskStatus());
                        //Log.d("jsonObjRecord", taskStatusData.toString());
                        if (taskStatusData != null && taskStatusData.getString("1").equals("yes") &&
                                taskStatusData.getString("2").equals("yes") && taskStatusData.getString("3").equals("yes")) {
                            posts_completed.add(posts.get(i));
                            //Log.d("posts_completed", String.valueOf(posts_completed.size()));
                        } else {
                            posts_pending.add(posts.get(i));
                            //Log.d("posts_pending1", String.valueOf(posts_pending.size()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    posts_pending.add(posts.get(i));
                    //Log.d("posts_pending2",String.valueOf(posts_pending.size()));
                }
            }
            posts_final.clear();
            posts_final.addAll(posts_pending);
            posts_final.addAll(posts_completed);
            //Log.d("posts_final", String.valueOf(posts_final.size()));
            fill_callplan(posts_final);
        }

    }

    /* Get Chemist List */
    private void getChemistList() {
        SharedPreferences share = getSharedPreferences("OGlogin", MODE_PRIVATE);
        String userId = share.getString("user_id", "");
        Cursor cursor = db.getSalesmanChemistList(userId);
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            Toast.makeText(this, "Stockist Call Plan List Not Found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    m_callplans m_callplans = new m_callplans();
                    String chemistId = cursor.getString(2);
                    String stockistCallPlanId = cursor.getString(3);
                    String clientLegalName = cursor.getString(5);
                    String latitude = cursor.getString(6);
                    String longitude = cursor.getString(7);
                    String address = cursor.getString(8);
                    String billAmount = cursor.getString(13);
                    String sequence = cursor.getString(14);
                    String taskStatus = cursor.getString(15);
                    String orderReceived = cursor.getString(16);
                    String isLocked = cursor.getString(18);
                    String DLExpiry = cursor.getString(19);
                    String Block_Reason = cursor.getString(21);
                    String isLocationFlag = cursor.getString(20);

                    m_callplans.setChemistID(chemistId);
                    if (stockistCallPlanId.equalsIgnoreCase("null")) {
                        m_callplans.setStockistCallPlanID(0);
                    } else {
                        m_callplans.setStockistCallPlanID(Integer.valueOf(stockistCallPlanId));
                    }
                    m_callplans.setClient_LegalName(clientLegalName);
                    if (latitude.equalsIgnoreCase("null")) {
                        m_callplans.setLatitude(0.0);
                    } else {
                        m_callplans.setLatitude(Double.valueOf(latitude));
                    }
                    if (longitude.equalsIgnoreCase("null")) {
                        m_callplans.setLongitude(0.0);
                    } else {
                        m_callplans.setLongitude(Double.valueOf(longitude));
                    }
                    if (address.equalsIgnoreCase("null")) {
                        m_callplans.setClientLocation("");
                    } else {
                        m_callplans.setClientLocation(address);
                    }
                    if (billAmount.equalsIgnoreCase("null")) {
                        m_callplans.setBillAmount(0);
                    } else {
                        m_callplans.setBillAmount(Integer.valueOf(billAmount));
                    }
                    if (sequence.equalsIgnoreCase("null")) {
                        m_callplans.setSequence(0);
                    } else {
                        m_callplans.setSequence(Integer.valueOf(sequence));
                    }
                    if (DLExpiry.equalsIgnoreCase("null")) {
                        m_callplans.setDLExpiry("NA");
                    } else {
                        m_callplans.setDLExpiry(DLExpiry);
                    }
                    if (Block_Reason.equalsIgnoreCase("null")) {
                        m_callplans.setBlock_Reason("NA");
                    } else {
                        m_callplans.setBlock_Reason(Block_Reason);
                    }
                    //  Toast.makeText(getApplicationContext(),"Toast"+DLExpiry,Toast.LENGTH_LONG).show();
                    m_callplans.setTaskStatus(taskStatus);
                    if (orderReceived.equalsIgnoreCase("null")) {
                        m_callplans.setOrderRecevied(0);
                    } else {
                        m_callplans.setOrderRecevied(Integer.valueOf(orderReceived));
                    }
                    if (isLocationFlag != null) {
                        m_callplans.setIsLocationCheck(isLocationFlag);
                    } else {
                        m_callplans.setIsLocationCheck("");
                    }
//                    if (isLocked.equalsIgnoreCase("1")) {
//                        String lockstatus = "locked";
//                        m_callplans.setIsLocked(lockstatus);
//                    } else {
//                        String lockstatus = "unlocked";
//                        m_callplans.setIsLocked(lockstatus);
//                    }
                    m_callplans.setIsLocked(isLocked);
                    posts.add(m_callplans);

                    if (cursorCount == posts.size()) {
                        fill_callplan(posts);
                    }
                } while (cursor.moveToNext());
            }
        }
    }



    /* Final Post ArrayList */
    /*private void finalPostArrayList() {
        posts_final = new ArrayList<>();
        posts_completed = new ArrayList<>();
        posts_pending = new ArrayList<>();

        for (int i = 0; i < posts.size(); i++) {
            String taskStatus = posts.get(i).getTaskStatus();
            if (!taskStatus.equalsIgnoreCase("null")) {
                try {
                    JSONObject jsonObject = new JSONObject(taskStatus);
                    if (jsonObject.getString("1").equalsIgnoreCase("yes")
                            && jsonObject.getString("2").equalsIgnoreCase("yes")
                            && jsonObject.getString("3").equalsIgnoreCase("yes")) {
                        posts_completed.add(posts.get(i));
                    } else {
                        posts_pending.add(posts.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                posts_pending.add(posts.get(i));T
                Log.d("checkTask", ""+posts_pending.size());
            }
        }
        posts_final.addAll(posts_pending);
        posts_final.addAll(posts_completed);
        fill_callplan(posts_final);
    }*/


    private void fill_callplan(final List<m_callplans> posts) {
        if (posts.size() > 0) {
            findViewById(R.id.emptyview).setVisibility(View.GONE);
        } else {
            findViewById(R.id.emptyview).setVisibility(View.VISIBLE);
        }

        adapter =
                new RecyclerView.Adapter<BindingViewHolder>() {
                    @Override
                    public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                        ViewDataBinding binding = null;
                        LayoutInflater inflater = LayoutInflater.from(CallPlan.this);
                        binding = DataBindingUtil.inflate(inflater, R.layout.adpter_callplan_list, parent, false);

                        img_take_order = (ImageView) binding.getRoot().findViewById(R.id.img_take_order);
                        img_payment = (ImageView) binding.getRoot().findViewById(R.id.img_payment);
                        img_lock = (ImageView) binding.getRoot().findViewById(R.id.partylock);
                        img_delivery = (ImageView) binding.getRoot().findViewById(R.id.img_delivery);
                        lnrClick = (LinearLayout) binding.getRoot().findViewById(R.id.lnrClick);
                        linearLayoutCart = (LinearLayout) binding.getRoot().findViewById(R.id.linear_cart_call_plan);
                        textViewCartCount = (TextView) binding.getRoot().findViewById(R.id.txt_cart_count_call_plan);
                        lnr_start_call = (LinearLayout) binding.getRoot().findViewById(R.id.call_plan_start);
                        txtCopy = (TextView) binding.getRoot().findViewById(R.id.txtCopy);
                        txtPaste = (TextView) binding.getRoot().findViewById(R.id.txtPaste);
                        /*  txtPaste = (TextView)binding.getRoot().findViewById(R.id.txtPaste);*/
                        return new BindingViewHolder(binding.getRoot());
                    }

                    @Override
                    public void onBindViewHolder(final BindingViewHolder holder, final int position) {
                        final m_callplans callplan_list = posts.get(position);
                        holder.getBinding().setVariable(BR.v_callplanlist, callplan_list);
                        String taskstatus = callplan_list.getTaskStatus();
                        isDelivery = true;
                        isPayment = true;
                        isTakOrder = true;

                        List<ChemistCart> chemistCartList = chemistCartDao.getCartItem(String.valueOf(callplan_list.getChemistID()), true);
                        final int cartSize = chemistCartList.size();
                        Log.d("cartSize", String.valueOf(cartSize));
                        if (cartSize == 0) {
                            linearLayoutCart.setVisibility(View.GONE);

                            if (strCopyCmstId != null) {
                                txtPaste.setVisibility(View.VISIBLE);
                                txtCopy.setVisibility(View.GONE);
                            } else {
                                txtCopy.setVisibility(View.GONE);
                                txtPaste.setVisibility(View.GONE);
                            }

                            //  txtOrder.setText("Paste");

                  /*  if (chkClick==true){

                        txtPaste.setVisibility(View.VISIBLE);
                    chkClick=false;
                    }else {
                        txtCopy.setVisibility(View.GONE);
                        txtPaste.setVisibility(View.GONE);
                    }*/
                        } else {
                            txtPaste.setVisibility(View.GONE);
                            txtCopy.setVisibility(View.VISIBLE);
                            linearLayoutCart.setVisibility(View.VISIBLE);
                            textViewCartCount.setText(String.valueOf(cartSize));
                   /* txtCopy.setVisibility(View.VISIBLE);
                    if(chkClick==true) {
                        txtPaste.setVisibility(View.VISIBLE);
                        txtCopy.setVisibility(View.VISIBLE);
                        chkClick=false;
                    }*/
                            //  txtOrder.setText("Copy");
                            //   txtPaste.setVisibility(View.VISIBLE);
                        }
                        if (callplan_list.getIsLocked() == "1") {
                            // if(callplan_list.getIsLocked()=="0"){
                            img_lock.setVisibility(View.VISIBLE);

                        } else {
                            img_lock.setVisibility(View.GONE);

                        }
                        JSONObject taskStatusData = null;
                        try {
                            if (taskstatus != null) {
                                taskStatusData = new JSONObject(taskstatus);
                            }
                            Resources res = getResources();
                            if (pref.getBoolean(IS_TAKE_ORDER_ASSIGNED, false)) {
                                if (taskStatusData != null && taskStatusData.getString("1").equals("yes")) {
                                    img_take_order.setImageDrawable(res.getDrawable(R.drawable.order_icon_done));
                                } else {
                                    final Drawable drawable = res.getDrawable(R.drawable.order_icon);
                                    img_take_order.setImageDrawable(drawable);
                                }
                            } else {
                                img_take_order.setBackgroundColor(getResources().getColor(R.color.white));
                                final Drawable drawable = res.getDrawable(R.drawable.order_inactive);
                                img_take_order.setImageDrawable(drawable);
                                isTakOrder = false;
                            }

                            if (pref.getBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, false)) {
                                if (taskStatusData != null && taskStatusData.getString("2").equals("yes")) {
                                    img_payment.setImageDrawable(res.getDrawable(R.drawable.rupee_icon_done));
                                } else {
                                    final Drawable drawable = res.getDrawable(R.drawable.rupee_icon);
                                    img_payment.setImageDrawable(drawable);
                                }
                            } else {
                                img_payment.setBackgroundColor(getResources().getColor(R.color.white));
                                final Drawable drawable = res.getDrawable(R.drawable.rupee_inactive);
                                img_payment.setImageDrawable(drawable);
                                isPayment = false;
                            }

                            if (pref.getBoolean(IS_ORDER_DELIVERY_ASSIGNED, false)) {
                                if (taskStatusData != null && taskStatusData.getString("3").equals("yes")) {
                                    img_delivery.setImageDrawable(res.getDrawable(R.drawable.delivery_icon_done));
                                } else {
                                    final Drawable drawable = res.getDrawable(R.drawable.delivery_icon);
                                    img_delivery.setImageDrawable(drawable);
                                }
                            } else {
                                img_delivery.setBackgroundColor(getResources().getColor(R.color.white));
                                final Drawable drawable = res.getDrawable(R.drawable.delivery_inactive);
                                img_delivery.setImageDrawable(drawable);
                                isDelivery = false;
                            }

                        } catch (JSONException js) {
                            js.printStackTrace();
                        }
                        // holder.getBinding().getRoot().setTag(R.id.key_call_plan_client_name, callplan_list.getIsLocked());
                        holder.getBinding().getRoot().setTag(R.id.key_call_plan_client_name, callplan_list.getClient_LegalName());
                        holder.getBinding().getRoot().setTag(R.id.key_call_plan_location, callplan_list.getClientLocation());


                        txtCopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Cart Size", String.valueOf(cartSize));
                               /* if (cartSize == 0) {
                                    Toast.makeText(getApplicationContext(), "Sorry you can't copy, Cart is empty.", Toast.LENGTH_LONG).show();
                                } else {*/
                                strCopyCmstId = posts.get(position).getChemistID();
                                rv_callPlanlist.setLayoutManager(new LinearLayoutManager(CallPlan.this));

                                rv_callPlanlist.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                   /* Toast.makeText(getApplicationContext(), "Cart is Copied", Toast.LENGTH_LONG).show();
                                }*/
                            }
                        });
                        txtPaste.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (strCopyCmstId != null) {
                                    if (strCopyCmstId.equals(posts.get(position).getChemistID())) {
                                        Toast.makeText(getApplicationContext(), "You can't copy same chemist", Toast.LENGTH_LONG).show();
                                    } else {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CallPlan.this);
                                        alertDialogBuilder.setTitle("");
                                        alertDialogBuilder.setMessage("Are you sure you want a paste. ");
                                        alertDialogBuilder.setCancelable(false);
                                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  UpdateDialog();
                                                String strPasteChe = posts.get(position).getChemistID();
                                                chemistCartDao = daoSession.getChemistCartDao();
                                                int nsrcChemist = Integer.parseInt(strCopyCmstId);
                                                int ntrgtChemist = Integer.parseInt(strPasteChe);
                                                // Log.d("QueryBuilder12", "Chemist----" + nsrcChemist + "========" + ntrgtChemist);
                                                int ntotalCart = chemistCartDao.copyorders(CallPlan.this, nsrcChemist, ntrgtChemist, true);
                                                //   notifyItemChanged(position);


                             /*   if(cartSize>0) {
                                    textViewCartCount.setText(String.valueOf(ntotalCart));
                                } if(cartSize>0) {
                                    textViewCartCount.setText(String.valueOf(ntotalCart));
                                }*/
                                                rv_callPlanlist.setLayoutManager(new LinearLayoutManager(CallPlan.this));

                                                rv_callPlanlist.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to invoke NO event
                                                dialog.cancel();
                                            }
                                        });

                                        alertDialogBuilder.show();
                                    }
                                } else {

                                }
                            }
                        });


                        holder.getBinding().getRoot().findViewById(R.id.lnrClick).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (callplan_list.getIsLocked() == "1") {
                                    // if(callplan_list.getIsLocked()=="0"){
                                    Toast.makeText(getApplicationContext(), callplan_list.getClient_LegalName() + " is locked.", Toast.LENGTH_SHORT).show();
                                    // img_lock.setVisibility(View.GONE);
                                } else {
                                    locationcustID = posts.get(position).getChemistID();
                                    if (get_location()) {
                                        //   img_lock.setVisibility(View.GONE);
                                        if (isDelivery || isTakOrder || isPayment) {
                                            JSONObject j_obj = new JSONObject();
                                            JSONArray j_array = new JSONArray();
                                            try {
                                                j_obj.put("StockistCallPlanID", callplan_list.getStockistCallPlanID());
                                                j_obj.put("Client_LegalName", callplan_list.getClient_LegalName());
                                                j_obj.put("Latitude", callplan_list.getLatitude());
                                                j_obj.put("Longitude", callplan_list.getLongitude());
                                                j_obj.put("ClientLocation", callplan_list.getClientLocation());
                                                j_array.put(j_obj);
                                            } catch (Exception e) {

                                            }
                                            Intent i = new Intent(CallPlan.this, CallPlanDetails.class);
                                            SharedPreferences.Editor editor = getSharedPreferences("MY PREF", MODE_PRIVATE).edit();
                                            editor.putString("client_name", holder.getBinding().getRoot().getTag(R.id.key_call_plan_client_name).toString());
                                            editor.putString("chemist_id", posts.get(position).getChemistID());
                                            editor.commit();
                                            // i.putExtra("client_name", holder.getBinding().getRoot().getTag(R.id.key_call_plan_client_name).toString());
                                            i.putExtra("Call_plan_id", posts.get(position).getStockistCallPlanID().toString());
                                            i.putExtra("response", j_array.toString());
                                            i.putExtra("chemist_id", posts.get(position).getChemistID());
                                            i.putExtra("chemist_id", posts.get(position).getChemistID());
                                            i.putExtra("Latitude", callplan_list.getLatitude());
                                            i.putExtra("Longitude", callplan_list.getLongitude());
                                            i.putExtra("task_status", posts.get(position).getTaskStatus());
                                            i.putExtra("filter_start_date", sql_dateFormat.format(filter_start_date));
                                            i.putExtra("EXPDate", posts.get(position).getDLExpiry());
                                            i.putExtra("isLocked", posts.get(position).getIsLocked());
                                            i.putExtra("Block_Reason", posts.get(position).getBlock_Reason());
                                            i.putExtra("isLocation", posts.get(position).getIsLocationCheck());
                                            // globalVariable.setClick_on_menuitem(false);
                                            //  i.putExtra("client_location",  holder.getBinding().getRoot().getTag(R.id.key_call_plan_location).toString());
                                            globalVariable.setFromMenuItemClick(false);
                                            //Log.d("ResponseCheck", j_array.toString());
                                            startActivity(i);
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Enable GPS Location", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
              /*  holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //    Log.e("Click","item click");
                       if(callplan_list.getIsLocked()=="1"){
                      // if(callplan_list.getIsLocked()=="0"){
                           Toast.makeText(getApplicationContext(),callplan_list.getClient_LegalName()+" is locked.",Toast.LENGTH_SHORT).show();
                          // img_lock.setVisibility(View.GONE);
                       }else{
                        //   img_lock.setVisibility(View.GONE);
                           if (isDelivery || isTakOrder || isPayment) {
                            JSONObject j_obj = new JSONObject();
                            JSONArray j_array = new JSONArray();
                            try {
                                j_obj.put("StockistCallPlanID", callplan_list.getStockistCallPlanID());
                                j_obj.put("Client_LegalName", callplan_list.getClient_LegalName());
                                j_obj.put("Latitude", callplan_list.getLatitude());
                                j_obj.put("Longitude", callplan_list.getLongitude());
                                j_obj.put("ClientLocation", callplan_list.getClientLocation());
                                j_array.put(j_obj);
                            } catch (Exception e) {

                            }
                            Intent i = new Intent(CallPlan.this, CallPlanDetails.class);
                            SharedPreferences.Editor editor = getSharedPreferences("MY PREF", MODE_PRIVATE).edit();
                            editor.putString("client_name", holder.getBinding().getRoot().getTag(R.id.key_call_plan_client_name).toString());
                            editor.putString("chemist_id", posts.get(position).getChemistID());
                            editor.commit();
                            // i.putExtra("client_name", holder.getBinding().getRoot().getTag(R.id.key_call_plan_client_name).toString());
                            i.putExtra("Call_plan_id", posts.get(position).getStockistCallPlanID().toString());
                            i.putExtra("response", j_array.toString());
                            i.putExtra("chemist_id", posts.get(position).getChemistID());
                            i.putExtra("chemist_id", posts.get(position).getChemistID());
                            i.putExtra("task_status", posts.get(position).getTaskStatus());
                            i.putExtra("filter_start_date", sql_dateFormat.format(filter_start_date));
                            // globalVariable.setClick_on_menuitem(false);
                            //  i.putExtra("client_location",  holder.getBinding().getRoot().getTag(R.id.key_call_plan_location).toString());
                            globalVariable.setFromMenuItemClick(false);
                            //Log.d("ResponseCheck", j_array.toString());
                            startActivity(i);
                        }

                    }
                }
                });*/
//                lnr_start_call.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                    /*if(callplan_list.getOrderRecevied()==1)
//                    {
//                        return;
//                    }
//
//*/
//                        JSONObject j_obj = new JSONObject();
//                        JSONArray j_array = new JSONArray();
//                        try {
//                            j_obj.put("StockistCallPlanID", callplan_list.getStockistCallPlanID());
//                            j_obj.put("Client_LegalName", callplan_list.getClient_LegalName());
//                            j_obj.put("Latitude", callplan_list.getLatitude());
//                            j_obj.put("Longitude", callplan_list.getLongitude());
//                            j_obj.put("ClientLocation", callplan_list.getClientLocation());
//                            j_array.put(j_obj);
//                        } catch (Exception e) {
//
//                        }
//
//
//                        Intent i = new Intent(CallPlan.this, CallPlanDetails.class);
//                         i.putExtra("client_name", holder.getBinding().getRoot().getTag(R.id.key_call_plan_client_name).toString());
//                        i.putExtra("Call_plan_id", posts.get(position).getStockistCallPlanID().toString());
//                        i.putExtra("response", j_array.toString());
//                        i.putExtra("chemist_id", posts.get(position).getChemistID());
//                        i.putExtra("chemist_id", posts.get(position).getChemistID());
//                        i.putExtra("task_status", posts.get(position).getTaskStatus());
//                        i.putExtra("filter_start_date", sql_dateFormat.format(filter_start_date));
//
//                        //  i.putExtra("client_location",  holder.getBinding().getRoot().getTag(R.id.key_call_plan_location).toString());
//                        startActivity(i);
//
//                    }
//
//                });

                        try {

                            if (isDelivery || isPayment || isTakOrder) {
                                holder.getBinding().getRoot().findViewById(R.id.callstartImg).setVisibility(View.VISIBLE);
                                holder.getBinding().getRoot().findViewById(R.id.callstartText).setVisibility(View.VISIBLE);
                            } else {
                                lnr_start_call.setVisibility(View.INVISIBLE);
                            }
                            if (!callplan_list.getTaskStatus().equals("")) {

                                //  JSONObject taskStatusData = new JSONObject(callplan_list.getTaskStatus());

                                Boolean alltaskcompleted = false;
                                if (taskStatusData != null && taskStatusData.getString("1").equals("yes") && taskStatusData.getString("2").equals("yes") && taskStatusData.getString("3").equals("yes")) {
                                    alltaskcompleted = true;
                                }

                                if (!alltaskcompleted) {
                                    holder.getBinding().getRoot().findViewById(R.id.callstartImg).setVisibility(View.VISIBLE);
                                    holder.getBinding().getRoot().findViewById(R.id.callstartText).setVisibility(View.VISIBLE);
                                } else {
                                    holder.getBinding().getRoot().findViewById(R.id.callstartImg).setVisibility(View.GONE);
                                    ((TextView) holder.getBinding().getRoot().findViewById(R.id.callstartText)).setText("Call Completed");
                                }

                            } else {

                            }
                        } catch (Exception e) {

                        }
                        holder.getBinding().executePendingBindings();
                    }

                    @Override
                    public int getItemCount() {
                        return posts.size();
                    }

                    @Override
                    public int getItemViewType(int position) {

                        return position;
                    }
                };

        rv_callPlanlist.setLayoutManager(new LinearLayoutManager(this));
        rv_callPlanlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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


    private List<m_call_activities> get_call_activities_json() {
        List<m_call_activities> post_activiteis = new ArrayList<m_call_activities>();

        LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Call_Plan_activities.json", CallPlan.this);
        String jsondata = _LoadJsonFromAssets.getJson();
        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            post_activiteis = Arrays.asList(mGson.fromJson(jsondata, m_call_activities[].class));
        }

        return post_activiteis;
    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        if (response != null) {
            try {
                if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                    String status = response.getString("Status");
                    Log.d("callplan_Location15", response.toString());
                    if (status.equalsIgnoreCase("success")) {
                        //     Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            try {
                if (f_name.equals(AppConfig.GET_STOCKIST_CALL_PLAN)) {
                    s_response_array = response.toString();
                    //Log.d("chemistResp", ""+s_response_array);

                    get_callist_json(response.toString());
                    /* Insert Data Into SQLite */
                    insertChemistListSQLite(response.toString());
                } else if (f_name.equals(AppConfig.SAVE_ORDER_DELIVERY)) {
                    Cursor cursor = db.getSavedDelivery();
                    int cursorCount = cursor.getCount();
                    if (postDeliverySQLite == 1) {
                        if (cursorCount > 0) {
                            db.deleteSavedDelivery();
                        }
                    }
                }
            } catch (Exception e) {
                e.toString();
            }
        }
    }


    /* Insert Chemist List Into SQLite */
    private void insertChemistListSQLite(String responseArray) {
        SharedPreferences share = getSharedPreferences("OGlogin", MODE_PRIVATE);
        String userId = share.getString("user_id", "");
        Cursor cursor = db.getSalesmanChemistList(userId);
        if (cursor.getCount() > 0) {
            db.deleteRecordFromSalesmanChemistList(userId);
        }

        try {
            JSONArray jsonArray = new JSONArray(responseArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String chemistId = jsonObject.getString("ChemistID");
                String stockistCallPlanId = jsonObject.getString("StockistCallPlanID");
                String dayNumber = jsonObject.getString("DayNumber");
                String clientName = jsonObject.getString("Client_LegalName");
                String latitude = jsonObject.getString("Latitude");
                String longitude = jsonObject.getString("Longitude");
                String address = jsonObject.getString("ClientLocation");
                String profileImg = jsonObject.getString("ProfileImage");
                String inTime = jsonObject.getString("InTime");
                String outTime = jsonObject.getString("OutTime");
                String status = jsonObject.getString("Status");
                String billAmount = jsonObject.getString("BillAmount");
                String sequence = jsonObject.getString("Sequence");
                String taskStatus = jsonObject.getString("TaskStatus");
                String orderReceived = jsonObject.getString("OrderRecevied");
                String passedDate = jsonObject.getString("PassedDate");
                String isLocked = jsonObject.getString("isLocked");
                String DLExpiry = jsonObject.getString("DLExpiry");
                String Block_Reason = jsonObject.getString("Block_Reason");
                //Log.d("getTaskJson", taskStatus);

                if (chemistId.equalsIgnoreCase("null")) {
                    chemistId = "";
                }
                if (stockistCallPlanId.equalsIgnoreCase("null")) {
                    stockistCallPlanId = "";
                }
                if (latitude.equalsIgnoreCase("null")) {
                    latitude = "0.0";
                }
                if (longitude.equalsIgnoreCase("null")) {
                    longitude = "0.0";
                }
                if (address.equalsIgnoreCase("null")) {
                    address = "";
                }
                if (profileImg.equalsIgnoreCase("null")) {
                    profileImg = "";
                }
                if (inTime.equalsIgnoreCase("null")) {
                    inTime = "null";
                }
                if (outTime.equalsIgnoreCase("null")) {
                    outTime = "";
                }
                if (status.equalsIgnoreCase("null")) {
                    status = "";
                }
                if (billAmount.equalsIgnoreCase("null")) {
                    billAmount = "0";
                }
                if (sequence.equalsIgnoreCase("null")) {
                    sequence = "";
                }
                if (taskStatus.equalsIgnoreCase("null")) {
                    taskStatus = "null";
                }
                if (orderReceived.equalsIgnoreCase("null")) {
                    orderReceived = "0";
                }
                if (passedDate.equalsIgnoreCase("null")) {
                    passedDate = "";
                }
                if (DLExpiry.equalsIgnoreCase("null")) {
                    DLExpiry = "NA";
                }
                if (Block_Reason.equalsIgnoreCase("null")) {
                    Block_Reason = "NA";
                }

                db.insertChemistList(User_id, chemistId, stockistCallPlanId, dayNumber, clientName, latitude, longitude,
                        address, profileImg, inTime, outTime, status, billAmount, sequence, taskStatus, orderReceived, passedDate, isLocked, DLExpiry, Block_Reason);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        try {
            filter_start_date = sdf.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            txt_sel_date.setText(dateFormat.format(filter_start_date));
            //Log.d("todayDates11", dateFormat.format(filter_start_date));
            //Log.d("todayDates11", get_day_number(filter_start_date));

        } catch (Exception e) {
            e.toString();
        }

// Running API
        /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                AppConfig.GET_STOCKIST_CALL_PLAN +"["+User_id+","+get_day_number(filter_start_date)+",\""+ sdf.format(filter_start_date)+"\"]" , this, true);
*/
       /* MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                AppConfig.GET_STOCKIST_CALL_PLAN +"["+Stockist_id+","+"1"+",2016-12-27+]" , this, true);
*/


        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]", this, true);
        Log.d("callplan", AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]");
    }

    private String get_day_number(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String daynumber = "1";

        switch (day) {

            case Calendar.MONDAY:
                daynumber = "1";
                break;
            case Calendar.TUESDAY:
                daynumber = "2";
                break;

            case Calendar.WEDNESDAY:
                daynumber = "3";
                break;
            case Calendar.THURSDAY:
                daynumber = "4";
                break;
            case Calendar.FRIDAY:
                daynumber = "5";
                break;
            case Calendar.SATURDAY:
                daynumber = "6";
                break;

            case Calendar.SUNDAY:
                daynumber = "0";
                // daynumber = "7";
                // Current day is Sunday
                break;
        }
        return daynumber;
    }


    /*private String get_day_number(Date d)
    {
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String daynumber="1";

        switch (day) {

            case Calendar.SUNDAY:
                daynumber="1";
                break;

            case Calendar.MONDAY:
                daynumber="2";
                break;
            case Calendar.TUESDAY:
                daynumber="3";
                break;
            case Calendar.WEDNESDAY:
                daynumber="4";
                break;
            case Calendar.THURSDAY:
                daynumber="5";
                break;
            case Calendar.FRIDAY:
                daynumber="6";
                break;
            case Calendar.SATURDAY:
                daynumber="7";
                break;

            *//*case Calendar.SUNDAY:
                daynumber="7";
                // Current day is Sunday
                break;*//*
        }
        return daynumber;
    }*/


    /* onStart */
    @Override
    protected void onStart() {
        super.onStart();
        new checkInternetConn().execute(0);
        //getCollectedPayment();
        new checkCollectedPayments().execute(0);
    }


    /* onPause */
    @Override
    protected void onPause() {
        super.onPause();
        new checkInternetConn().execute(0);
    }


    /* Check Internet Connection */
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
                getSavedDelivery();
            }
            super.onPostExecute(result);

            //Do it again!
            new checkInternetConn().execute(5000);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
    }


    /* For Collected Payments */
    /* Check Internet Connection */
    private class checkCollectedPayments extends AsyncTask<Integer, Integer, Void> {
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
            if (InternetConnection.checkConnection(getApplication())) {
                getCollectedPayment();
            }
            super.onPostExecute(result);

            //Do it again!
            Cursor cursor = db.getCollectedPaymentRecord(Flag);
            if (cursor.getCount() > 0) {
                new checkInternetConn().execute(5000);
            }
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

    ////----    Post Delivery Data Section      ----////

    /* Get SQLite Saved Delivery */
    private void getSavedDelivery() {
        Cursor cursor = db.getSavedDelivery();
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
                            postDeliverySQLite = 1;
                            MakeWebRequest.MakeWebRequest("NPost", AppConfig.SAVE_ORDER_DELIVERY, AppConfig.SAVE_ORDER_DELIVERY, jsonObject, this, false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
    }

    ////----    End Delivery Data Section       ----////


    /* Get Collected Payment */
    private void getCollectedPayment() {
        Cursor cursor = db.getCollectedPaymentRecord(Flag);
        Log.d("cursorcount", String.valueOf(cursor.getCount()));
        Log.d("cursorcount1", cursor.toString());
        if (cursor.getCount() > 0) {
            String paymentId = null;
            if (cursor.moveToFirst()) {
                jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                String stockistId = cursor.getString(1);
                String userId = cursor.getString(2);
                String chemistId = cursor.getString(3);
                paymentId = cursor.getString(4);
                String createdBy = cursor.getString(5);
                String amount = cursor.getString(6);
                String paymentDate = cursor.getString(7);
                String remarks = cursor.getString(8);
                String status = cursor.getString(9);
                String createdDate = cursor.getString(10);
                String paymentMode = cursor.getString(11);
                String bankName = cursor.getString(12);
                String chequeNo = cursor.getString(13);
                String chequeAmount = cursor.getString(14);
                String chequeDate = cursor.getString(15);
                String micrNo = cursor.getString(16);
                String narration = cursor.getString(17);
                String comments = cursor.getString(18);

                try {
                    jsonObject.put("PaymnetAppID", paymentId);
                    jsonObject.put("CreatedBy", createdBy);
                    jsonObject.put("ChemistID", chemistId);
                    jsonObject.put("Amount", Integer.valueOf(amount));
                    jsonObject.put("PaymentDate", paymentDate);
                    jsonObject.put("StockiestID", stockistId);
                    jsonObject.put("Remarks", remarks);
                    jsonObject.put("Status", status);
                    jsonObject.put("CreatedDate", createdDate);
                    jsonObject.put("PaymentMode", paymentMode);
                    jsonObject.put("Bank", bankName);
                    jsonObject.put("ChequeNo", chequeNo);
                    jsonObject.put("ChequeAmt", chequeAmount);
                    jsonObject.put("ChequeDate", chequeDate);
                    jsonObject.put("MicrCode", micrNo);
                    jsonObject.put("Narration", narration);
                    jsonObject.put("Comments", comments);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }

            Cursor cursor1 = db.getSelectedInvoiceByPaymentId(paymentId);

            Log.d("cursorcount11", String.valueOf(cursor1.getCount()));
            Log.d("cursorcount1111", paymentId);
            JSONArray jsonArray1 = null;
            if (cursor1.getCount() > 0) {

                jsonArray1 = new JSONArray();
                if (cursor1.moveToFirst()) {
                    do {
                        String invoiceNo = cursor1.getString(5);
                        String paidAmount = cursor1.getString(6);
                        String paymentStatus = cursor1.getString(7);
                        String createdDate1 = cursor1.getString(8);
                        String status1 = cursor1.getString(9);

                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("InvoiceNo", invoiceNo);
                            jsonObject1.put("InvoiceAmt", paidAmount);
                            jsonObject1.put("PaymentStatus", paymentStatus);
                            jsonObject1.put("CreatedDate", createdDate1);
                            jsonObject1.put("Status", status1);
                            jsonArray1.put(jsonObject1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } while (cursor1.moveToNext());
                }
            }
            jsonArray.put(jsonArray1);
            Log.d("backgroundArray", jsonArray.toString());
            onlinePaymentAPI();
        } else {
            //  Toast.makeText(getApplicationContext(),"No Collection",Toast.LENGTH_SHORT).show();
        }
    }

    /* Online Payment API */
    private void onlinePaymentAPI() {

        String url = AppConfig.CollectPayment;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        Log.d("response123", response.toString());
                        Log.d("responselength", String.valueOf(response.length()));
                        try {
                            String paymentId = response.getJSONObject(i).getString("PaymnetAppID");
                            // String paymentId = jsonObject.getString("PaymnetAppID");
                            Log.d("paymentid", paymentId);
                            // db.deletePaymentRecord(paymentId);
                            db.updateFlagCollectPaymentSalesman(paymentId, statusflag);
                            db.deleteSelectedInvoiceByPaymentId(paymentId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Offline Payments", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + globalVariable.getToken());
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonArrayRequest);
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
            //showSettingsAlert();
            Intent intent = new Intent(CallPlan.this, LocationActivity.class);
            startActivity(intent);

            check = false;
        }
        return check;

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
        new AlertDialog.Builder(CallPlan.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
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


    @Override
    public void onLocationChanged(Location location) {
        Log.e("onLocationcallplan", "" + location.getLatitude());

        if (location != null) {
            currentLocLat = location.getLatitude();
            currentLocLong = location.getLongitude();
            //  locationManager.removeUpdates(CustomerlistActivity.this);
            //  Log.d("order_salesman11",currentLocLat+"  "+ currentLocLong);
            String adress = LocationAddress.getAddressFromLocation(currentLocLat, currentLocLong, this);
            // Log.d("onLocationupdate12",adress);
            JSONObject jsonParams = new JSONObject();

            try {

                String trasaction_No = pref.getString("Transaction_No", "");

                mUId = mUId.randomUUID();

                Log.d("location_update11", String.valueOf(mUId));
                SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("UUID", String.valueOf(mUId));
                editor.commit();

                String callplan = "start_callplan";

                // new Updatelocationv1(CallPlan.this,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),adress,User_id,locationcustID,callplan,trasaction_No);

                // jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                //jsonParams.put("UserID", Selected_chemist_id);
                jsonParams.put("Latitude", String.valueOf(location.getLatitude()));
                jsonParams.put("Longitude", String.valueOf(location.getLongitude()));
                jsonParams.put("CurrentLocation", adress);
                jsonParams.put("UserID", User_id);
                jsonParams.put("CustID", locationcustID);
                jsonParams.put("task", callplan);
                jsonParams.put("Tran_No", String.valueOf(mUId));
                jsonParams.put("unqid", " ");

                Log.d("callplan_Location12", AppConfig.POST_UPDATE_CURRENT_LOCATION);
                Log.d("callplan_Location13", String.valueOf(jsonParams));

                MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CURRENT_LOCATION, AppConfig.POST_UPDATE_CURRENT_LOCATION, jsonParams, this, false);

                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }

                Log.d("callplan_Location14", "Stop service location");
                // USE THIS API
                // MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CHEMIST_LOCATION, AppConfig.POST_UPDATE_CHEMIST_LOCATION, jsonParams, this, false);

            } catch (Exception e) {

            }
        }

    }


}


