package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.model.m_delivery_customerlist;
import com.synergy.keimed_ordergenie.model.m_delivery_invoicelist;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.LocationService;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.get_current_location;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by Apurva on 23-05-2018.
 */
public class DeliveryCustomerList extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LocationListener {

    @BindView(R.id.rv_callPlanlist)
    RecyclerView rv_callPlanlist;
    String login_type;
    private String User_id, Stockist_id;
    Date filter_start_date = Calendar.getInstance().getTime();
    AppController globalVariable;
    private Date current_date = Calendar.getInstance().getTime();

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

    List<m_delivery_customerlist> posts;
    List<m_delivery_invoicelist> posts_completed;
    List<m_delivery_invoicelist> posts_pending;
    List<m_delivery_invoicelist> posts_final;
    private static final String FORMAT = "%02d:%02d:%02d";
    ImageView img_take_order;
    ImageView img_payment;
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


    /* Get Current Location */
    private LocationManager locationManager;
    private String locationProvider;
    private double currentLocLat = 0;
    private double currentLocLong = 0;
    private UUID mUniqueId;
    String uniqueID, Chemist_id, userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_customerlist);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
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

        Chemist_id = getIntent().getStringExtra("chemist_id");
        userId = pref.getString(USER_ID, "0");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

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
                DeliveryCustomerList.this,
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
        Intent i = new Intent(DeliveryCustomerList.this, MapsActivity.class);
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

                List<m_delivery_customerlist> filteredModelList = new ArrayList<>();
                for (m_delivery_customerlist model : posts) {
                    final String text = model.getClient_Name().toLowerCase();
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
                new get_current_location(DeliveryCustomerList.this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
        getCustomerListDelivery();
    }




    /* Get Chemist List */
//    private void get_callist_json(String response) {
//        //  LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Call_Plan.json", CallPlan.this);
//        String jsondata = response.toString();
//              if (!jsondata.isEmpty()) {
//            //Log.d("RES", jsondata);
//            try {
//
//
//            } catch (Exception e) {
//            }
//
//
//            GsonBuilder builder = new GsonBuilder();
//
//            Gson mGson = builder.create();
//
//            posts = new ArrayList<m_delivery_customerlist>();
//
//            posts_final = new ArrayList<m_delivery_customerlist>();
//            posts_completed = new ArrayList<m_delivery_customerlist>();
//            posts_pending = new ArrayList<m_delivery_customerlist>();
//            posts = Arrays.asList(mGson.fromJson(jsondata, m_delivery_customerlist[].class));
//
//            posts_completed.clear();
//            posts_pending.clear();
//            posts_final.clear();
//
//            for (int i = 0; i < posts.size(); i++) {
//                if (posts.get(i).getTaskStatus() != null) {
//                    try {
//                        JSONObject taskStatusData = new JSONObject(posts.get(i).getTaskStatus());
//                        //Log.d("jsonObjRecord", taskStatusData.toString());
//                        if (taskStatusData != null && taskStatusData.getString("1").equals("yes") &&
//                                taskStatusData.getString("2").equals("yes") && taskStatusData.getString("3").equals("yes")) {
//                            posts_completed.add(posts.get(i));
//                            //Log.d("posts_completed", String.valueOf(posts_completed.size()));
//                        } else {
//                            posts_pending.add(posts.get(i));
//                            //Log.d("posts_pending1", String.valueOf(posts_pending.size()));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    posts_pending.add(posts.get(i));
//                    //Log.d("posts_pending2",String.valueOf(posts_pending.size()));
//                }
//            }
//
//            posts_final.clear();
//            posts_final.addAll(posts_pending);
//            posts_final.addAll(posts_completed);
//            //Log.d("posts_final", String.valueOf(posts_final.size()));
//            fill_callplan(posts_final);
//        }
//
//    }

    /* Get Chemist List */
    private void getCustomerListDelivery() {
        SharedPreferences share = getSharedPreferences("OGlogin", MODE_PRIVATE);
        String userId = share.getString("user_id", "");
        //  Log.d("userId",userId);

        // Cursor cursor = db.getDeliveryScheduleByUserId(userId);
        Cursor cursor = db.getDeliveryScheduleByUserId(userId);
        //   Log.d("countt", String.valueOf(cursor.getCount()));
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            Toast.makeText(this, "Invoice List Not Found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    m_delivery_customerlist m_callplans = new m_delivery_customerlist();

                    //ClientId
                    //UserID
                    //Client_Code
                    //Client_Name
                    //Client_Address
                    String ClientId = cursor.getString(1);
                    String UserID = cursor.getString(2);
                    String Client_Code = cursor.getString(3);
                    String Client_Name = cursor.getString(4);
                    String Client_Address = cursor.getString(5);

                    m_callplans.setClientID(ClientId);
                    if (ClientId.equalsIgnoreCase("null")) {
                        m_callplans.setClientID("0");
                    } else {
                        m_callplans.setClientID(ClientId);
                    }
                    m_callplans.setClient_Name(Client_Name);
                    if (Client_Address.equalsIgnoreCase("null")) {
                        m_callplans.setClient_Address("");
                    } else {
                        m_callplans.setClient_Address(Client_Address);
                    }

                    posts.add(m_callplans);
                    if (cursorCount == posts.size()) {
                        fill_callplan(posts);
                    }
                } while (cursor.moveToNext());
            }
        }
    }


    private void fill_callplan(final List<m_delivery_customerlist> posts) {
        if (posts.size() > 0) {
            findViewById(R.id.emptyview).setVisibility(View.GONE);
        } else {
            findViewById(R.id.emptyview).setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                ViewDataBinding binding = null;
                LayoutInflater inflater = LayoutInflater.from(DeliveryCustomerList.this);
                binding = DataBindingUtil.inflate(inflater, R.layout.adpter_delivery_customerlist, parent, false);

                img_take_order = (ImageView) binding.getRoot().findViewById(R.id.img_take_order);
                img_payment = (ImageView) binding.getRoot().findViewById(R.id.img_payment);
                img_delivery = (ImageView) binding.getRoot().findViewById(R.id.img_delivery);
                linearLayoutCart = (LinearLayout) binding.getRoot().findViewById(R.id.linear_cart_call_plan);
                textViewCartCount = (TextView) binding.getRoot().findViewById(R.id.txt_cart_count_call_plan);

                lnr_start_call = (LinearLayout) binding.getRoot().findViewById(R.id.call_plan_start);
                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(final BindingViewHolder holder, final int position) {
                final m_delivery_customerlist callplan_list = posts.get(position);
                holder.getBinding().setVariable(BR.v_delivery_customerlist, callplan_list);


//                JSONObject taskStatusData = null;
//                try {
//                    if (taskstatus != null) {
//                        taskStatusData = new JSONObject(taskstatus);
//                    }
//                    Resources res = getResources();
//                    if (pref.getBoolean(IS_TAKE_ORDER_ASSIGNED, false)) {
//                        if (taskStatusData != null && taskStatusData.getString("1").equals("yes")) {
//                            img_take_order.setImageDrawable(res.getDrawable(R.drawable.order_icon_done));
//                        } else {
//                            final Drawable drawable = res.getDrawable(R.drawable.order_icon);
//                            img_take_order.setImageDrawable(drawable);
//                        }
//                    } else {
//                        img_take_order.setBackgroundColor(getResources().getColor(R.color.white));
//                        final Drawable drawable = res.getDrawable(R.drawable.order_inactive);
//                        img_take_order.setImageDrawable(drawable);
//                        isTakOrder=false;
//                    }
//
//                    if (pref.getBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, false)) {
//                        if (taskStatusData != null && taskStatusData.getString("2").equals("yes")) {
//                            img_payment.setImageDrawable(res.getDrawable(R.drawable.rupee_icon_done));
//                        } else {
//                            final Drawable drawable = res.getDrawable(R.drawable.rupee_icon);
//                            img_payment.setImageDrawable(drawable);
//                        }
//                    } else {
//                        img_payment.setBackgroundColor(getResources().getColor(R.color.white));
//                        final Drawable drawable = res.getDrawable(R.drawable.rupee_inactive);
//                        img_payment.setImageDrawable(drawable);
//                        isPayment=false;
//                    }
//
//                    if (pref.getBoolean(IS_ORDER_DELIVERY_ASSIGNED, false)) {
//                        if (taskStatusData != null && taskStatusData.getString("3").equals("yes")) {
//                            img_delivery.setImageDrawable(res.getDrawable(R.drawable.delivery_icon_done));
//                        } else {
//                            final Drawable drawable = res.getDrawable(R.drawable.delivery_icon);
//                            img_delivery.setImageDrawable(drawable);
//                        }
//                    } else {
//                        img_delivery.setBackgroundColor(getResources().getColor(R.color.white));
//                        final Drawable drawable = res.getDrawable(R.drawable.delivery_inactive);
//                        img_delivery.setImageDrawable(drawable);
//                        isDelivery=false;
//                    }
//
//                } catch (JSONException js) {
//                    js.printStackTrace();
//                }
//                holder.getBinding().getRoot().setTag(R.id.key_call_plan_client_name, callplan_list.getClient_LegalName());
//                holder.getBinding().getRoot().setTag(R.id.key_call_plan_location, callplan_list.getClientLocation());

                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       if(get_location()) {

                       /* mUniqueId = mUniqueId.randomUUID();

                        uniqueID = String.valueOf(mUniqueId);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("uniqueID", String.valueOf(mUniqueId));
                        editor.commit();*/

                           Log.d("deliveryitem", "onclick delivery list");

                           Intent i = new Intent(DeliveryCustomerList.this, DeliveryInvoiceList.class);
                           i.putExtra("chemist_id", callplan_list.getClientID());
                           i.putExtra("chemist_name", callplan_list.getClient_Name());

                           startActivity(i);
                           Intent intent = new Intent(DeliveryCustomerList.this, LocationService.class);
                           startService(intent);

                       }else {
                           Toast.makeText(getApplicationContext(),"Please Enable GPS Location",Toast.LENGTH_SHORT).show();
                       }
                    }

                });

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
    }


    private class BindingViewHolder extends RecyclerView.ViewHolder {
        public BindingViewHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
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


//        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
//                AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]", this, true);
//        Log.d("callplan",AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]");
//
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

    /* onStart */
    @Override
    protected void onStart() {
        super.onStart();
        // new checkInternetConn().execute(0);
        //getCollectedPayment();
        // new checkCollectedPayments().execute(0);
    }


    /* onPause */
    @Override
    protected void onPause() {
        super.onPause();
        // new checkInternetConn().execute(0);
    }

    private boolean get_location() {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //  mLocationManager.getLastKnownLocation();
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
                return true;
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
            showSettingsAlert();
            return false;
        }

return true;
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


    }

    @Override
    public void onLocationChanged(Location location) {


        if (location != null) {
            currentLocLat = location.getLatitude();
            currentLocLong = location.getLongitude();

            String adress = LocationAddress.getAddressFromLocation(currentLocLat, currentLocLong, this);
            JSONObject jsonParams = new JSONObject();

            try {

                mUniqueId = mUniqueId.randomUUID();

                uniqueID = String.valueOf(mUniqueId);
                SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("uniqueID", String.valueOf(mUniqueId));
                editor.commit();


                String trasaction_No = pref.getString("Transaction_No", "");


                String callplan = "stop_callplan";


                //  new Updatelocationv1(CallPlanDetails.this,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),adress,pref.getString(USER_ID, "0"),locationcustID,callplan,String.valueOf(mUId));


                Log.d("delivery_location11", uniqueID);

                jsonParams.put("Latitude", String.valueOf(location.getLatitude()));
                jsonParams.put("Longitude", String.valueOf(location.getLongitude()));
                jsonParams.put("CurrentLocation", adress);
                jsonParams.put("UserID", userId);
                jsonParams.put("CustID", Chemist_id);
                jsonParams.put("task", "Delivery");
                jsonParams.put("Tran_No", "");
                // jsonParams.put("Tran_No", InvoiceNo);
                jsonParams.put("unqid", uniqueID);


                Log.d("delivery_location12", AppConfig.POST_UPDATE_CURRENT_LOCATION);
                Log.d("delivery_location13", String.valueOf(jsonParams));

                MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CURRENT_LOCATION, AppConfig.POST_UPDATE_CURRENT_LOCATION, jsonParams, this, false);

                locationManager.removeUpdates(DeliveryCustomerList.this);
            }
            catch(Exception e)
            {

            }
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Gps_is9", "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {

        Log.d("Gps_is10", "GPS Disable");
    }

    @Override
    public void onProviderDisabled(String s) {

        Log.d("Gps_is11", "GPS Disable");

    }


    public void onSuccess_json_object(String f_name, JSONObject response) {

        if (response != null) {
            if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                try {
                    String status = response.getString("Status");
                    Log.d("delivery_location14", response.toString());
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Location not Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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


}


