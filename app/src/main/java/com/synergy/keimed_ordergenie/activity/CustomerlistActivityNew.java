package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.app.AlertDialog;
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

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.CustomerListAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.model.m_customerlist_new;
import com.synergy.keimed_ordergenie.model.m_stokiest_distributor_list;
import com.synergy.keimed_ordergenie.utils.LocationActivity;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;
import com.synergy.keimed_ordergenie.utils.get_current_location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by prakash on 08/07/16.
 */
public class CustomerlistActivityNew extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, MakeWebRequest.OnResponseSuccess, CustomerListAdapter.ContactsAdapterListener {

    // HARISH CHANGES

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    Boolean isGPSEnabled = false;
    /* Get Current Location */
    private LocationManager locationManager;
    private String locationProvider;
    private double currentLocLat = 0;
    private double currentLocLong = 0;
    get_current_location getcurrentLocation;
    private static final String CHEMIST_ID = "Chemist_id";
    private Snackbar snackbar;
    private BottomSheetBehavior behavior;
    //private SearchView searchView;
    private Menu oMenu;
    private Integer selected_customer_id;
    RecyclerView.Adapter<BindingViewHolder> adapter;
    SharedPreferences pref;
    private String constraint1, constraint2;
    public static final String CHEMIST_STOCKIST_NAME = "chemist_stockist_name";
    private String Selected_chemist_id;

    public static String selected_Ten_orders;
    private String Chemist_id;
    private String Selected_client_name;
    private String User_id, Stockist_id, Role;
    public static final String SELECTED_CHEMIST_ID = "selected_chemist_id";
    private List<m_customerlist_new> mModels = new ArrayList<>();
    LinearLayout linearLayout;
    private CustomerListAdapter mAdapter;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private List<m_stokiest_distributor_list> mStokiestmodels = new ArrayList<>();
    AppController globalVariable;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private long UPDATE_INTERVAL = 5000;  /* 5 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
//    @BindView(R.id.rv_datalist)
//    RecyclerView rvCustomerlist;

    @BindView(R.id.bottom_sheet)
    View _bottomSheet;

    @Nullable
    @BindView(R.id.fab)
    FloatingActionButton _fab;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.sp_customer_type)
    Spinner _sp_customer_type;

    @Nullable
    @BindView(R.id.txt_cust_count)
    TextView txt_cust_count;

    @BindView(R.id.btm_text_cust_name)
    TextView btm_text_cust_name;


    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_cus_code)
    TextView txt_cus_code;
    @BindView(R.id.txt_cus_address)
    TextView txt_cus_address;
    @BindView(R.id.txt_cus_email)
    TextView txt_cus_email;
    @BindView(R.id.txt_cus_phone)
    TextView txt_cus_phone;
    @BindView(R.id.txt_cus_outstanding)
    TextView txt_cus_outstanding;


    @BindView(R.id.main_coordinate)
    CoordinatorLayout _main_coordinate;


    @OnClick(R.id.main_coordinate)
    void onclickcoordinate(View view) {
        if (snackbar != null) {
            snackbar.dismiss();
            if (_fab.getVisibility() == View.GONE) {
                _fab.setVisibility(View.VISIBLE);
            }
        }
    }


    @OnClick(R.id.fab)
    void onclickfab(View view) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            findViewById(R.id.list_options).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_details).setVisibility(View.GONE);
            _bottomSheet.invalidate();
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            //create_snack_bar(view);
            new get_current_location(CustomerlistActivityNew.this);

        }
    }

    @OnClick(R.id.lnr_view_profile)
    void onclickprofile(View view) {

        findViewById(R.id.list_options).setVisibility(View.GONE);
        findViewById(R.id.profile_details).setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.lnr_view_pending_bills)
    void pendingbills(View view) {
        // Show_pending_bills();
        Show_pending_bills_new();
    }

    @OnClick(R.id.lnr_view_all_orders)
    void allorders(View view) {
        Show_Orders();
    }

    @OnClick(R.id.lnr_view_sales_return)
    void salesreturns(View view) {

        Show_sales_returns();

    }

    @OnClick(R.id.lnr_create_new_order)
    void createorder(View view) {
        Create_new_order();
    }


    // HARISH UP_LOCATION ONCLICK EVENT

   /* @OnClick(R.id.lnr_view_up_location)
    void upcurrentLocation(View view) {
        update_Current_locationn();
    }*/


    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar_customerlist_new);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        }
        // HARISH CHAGES

        linearLayout = (LinearLayout) findViewById(R.id.lnr_view_up_location);

        ButterKnife.bind(this);
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("CustomerList");
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.rv_datalist);
        globalVariable = (AppController) getApplicationContext();
        Chemist_id = pref.getString(CHEMIST_ID, "0");
        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");
        Role = pref.getString(CLIENT_ROLE, "0");


        /*Log.d("listStockistCheck", Stockist_id);
        Log.d("listStockistCheck", User_id);*/

        /* Initialize SQLite Class */
        sqLiteHandler = new SQLiteHandler(this);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("onLocationupdate10", "on click linerlayout");
                //   current_location();
                get_location();

            }
        });

        //     Log.e("Role", Role);
        //     Log.e("Stockist_id", Stockist_id + User_id);
        behavior = BottomSheetBehavior.from(_bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) _fab.getLayoutParams();
                    p.setAnchorId(R.id.lnr_bottom);
                    _fab.setLayoutParams(p);
                    findViewById(R.id.list_options).setVisibility(View.VISIBLE);
                    findViewById(R.id.profile_details).setVisibility(View.GONE);
                    _bottomSheet.invalidate();
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });
        if (Role.equals("stockist")) {
            Log.d("CustomerList11", AppConfig.GET_STOCKIST_CUSTOMERLIST_STOCKISTLOGIN + Stockist_id);
            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CUSTOMERLIST_STOCKISTLOGIN,
                    AppConfig.GET_STOCKIST_CUSTOMERLIST_STOCKISTLOGIN + Stockist_id, this, true);
        } else {
            if (Utility.internetCheck(this)) {
                Log.d("CustomerList12", AppConfig.GET_STOCKIST_CUSTOMERLIST + "[" + Stockist_id + "," + User_id + "]");
                MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CUSTOMERLIST,
                        AppConfig.GET_STOCKIST_CUSTOMERLIST + "[" + Stockist_id + "," + User_id + "]", this, true);
            } else {
                getChemistList();
                //Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customerlist, menu);
        oMenu = menu;
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                // filter_on_text(newText);
//                if (newText.length() == 3) {
//                    mAdapter.getFilter().filter(newText);
//                }
                mAdapter.getFilter().filter(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
//                if (query.length() == 3) {
//                                mAdapter.getFilter().filter(query);
//            }

                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                _toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.GONE);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* onResume */
    @Override
    public void onResume() {

        super.onResume();
    }

    /* Get Chemist List */
    private void getChemistList() {
        Cursor cursor = sqLiteHandler.getChemistListSalesmanForCreateOrder(User_id);
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            Toast.makeText(this, "Customer List Not Found", Toast.LENGTH_SHORT).show();
        } else {
            mModels = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    m_customerlist_new m_customerlist_new = new m_customerlist_new();
                    m_customerlist_new.setChemist_id(cursor.getString(2));
                    m_customerlist_new.setCust_Code(cursor.getString(3));
                    m_customerlist_new.setCustomerName(cursor.getString(4));
                    m_customerlist_new.setEmail_id(cursor.getString(5));
                    m_customerlist_new.setMobile(cursor.getString(6));
                    m_customerlist_new.setLocation(cursor.getString(7));
                    m_customerlist_new.setLatitude(Double.valueOf(cursor.getString(8)));
                    m_customerlist_new.setLongitude(Double.valueOf(cursor.getString(9)));
                    m_customerlist_new.setOutstanding_Bill(cursor.getString(10));
                 /*   m_customerlist_new.setDLExpiry(cursor.getString(11));
                    m_customerlist_new.setDLExpiryIND(cursor.getString(12));*/
                    mModels.add(m_customerlist_new);
                    if (cursorCount == mModels.size()) {
                        //update_recycleview(mModels);
                        fill_payment_list(mModels);
                    }
                } while (cursor.moveToNext());
            }
        }
    }



    @Override
    public void onContactSelected(m_customerlist_new contact, View v, int pos) {
        Log.d("click12", "Name:" + contact.getCustomerName());
        Log.d("click13", "Position:" + String.valueOf(pos));
        // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        show_bottom_sheet(v, contact.getCustomerName());
        Selected_chemist_id = contact.getChemist_id();
        selected_Ten_orders = Selected_chemist_id;
        Selected_client_name = contact.getCustomerName();
        txt_name.setText((contact.getCustomerName() == null) ? "NA" : contact.getCustomerName());
        txt_cus_code.setText((contact.getCust_Code() == null) ? "NA" : contact.getCust_Code());
        txt_cus_address.setText((contact.getLocation() == null) ? "NA" : contact.getLocation());
        txt_cus_email.setText((contact.getEmail_id() == null) ? "NA" : contact.getEmail_id());
        txt_cus_phone.setText((contact.getMobile() == null) ? "NA" : contact.getMobile());

        Log.d("print_data11", contact.getLatitude() + " " + contact.getLongitude());
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("Selected_chemist_id", Selected_chemist_id);
        editor.commit();

        String cheLatitude = String.valueOf(contact.getLatitude());
        String cheLogitude = String.valueOf(contact.getLongitude());

        Log.d("print_data12", cheLatitude + " " + cheLogitude);

        if (cheLatitude.equals("0.0") || cheLogitude.equals("0.0")) {

            linearLayout.setVisibility(View.VISIBLE);

        } else {

            linearLayout.setVisibility(View.GONE);
        }

        if (contact.getOutstanding_Bill() != null) {
            txt_cus_outstanding.setText(contact.getOutstanding_Bill());
        }

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

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocLat = location.getLatitude();
            currentLocLong = location.getLongitude();
            //  locationManager.removeUpdates(CustomerlistActivity.this);
            Log.d("onLocationupdate11", currentLocLat + "  " + currentLocLong);
            String adress = LocationAddress.getAddressFromLocation(currentLocLat, currentLocLong, this);
            // Log.d("onLocationupdate12",adress);

            JSONObject jsonParams = new JSONObject();

            try {

                // jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                jsonParams.put("UserID", Selected_chemist_id);
                jsonParams.put("Latitude", String.valueOf(location.getLatitude()));
                jsonParams.put("Longitude", String.valueOf(location.getLongitude()));
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
        }
    }


    private class BindingViewHolder extends RecyclerView.ViewHolder {
        public BindingViewHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }
    }


//
//    /* Chemist List */
//    private void update_recycleview(final List<m_customerlist_new> post) {
//        txt_cust_count.setText(post.size() + " Customers");
//        adapter = new RecyclerView.Adapter<BindingViewHolder>() {
//            @Override
//            public BindingViewHolder onCreateViewHolder(ViewGroup parent,
//                                                        final int viewType) {
//                LayoutInflater inflater = LayoutInflater.from(CustomerlistActivityNew.this);
//                ViewDataBinding binding = DataBindingUtil
//                        .inflate(inflater, R.layout.fragement_customerlist_items, parent, false);
//
//                return new BindingViewHolder(binding.getRoot());
//            }
//
//            @Override
//            public void onBindViewHolder(BindingViewHolder holder, final int position) {
//                m_customerlist_new omline_customerlist = post.get(position);
//                holder.getBinding().setVariable(BR.v_customerlist, omline_customerlist);
//                holder.getBinding().executePendingBindings();
//
//
//
//                /* Save Chemist List Into SQLite */
//                /*sqLiteHandler.insertChemistListSalesmanForCreateOrder(User_id, omline_customerlist.getChemist_id(), omline_customerlist.getCust_Code(),
//                        omline_customerlist.getCustomerName(), omline_customerlist.getEmail_id(), omline_customerlist.getMobile(),
//                        omline_customerlist.getLocation(), String.valueOf(omline_customerlist.getLatitude()),
//                        String.valueOf(omline_customerlist.getLongitude()), omline_customerlist.getOutstanding_Bill());*/
//
//
//                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                        show_bottom_sheet(v, post.get(position).getCustomerName());
//                        Selected_chemist_id = post.get(position).getChemist_id();
//                        selected_Ten_orders = Selected_chemist_id;
//                        Selected_client_name = post.get(position).getCustomerName();
//                        txt_name.setText(post.get(position).getCustomerName());
//                        txt_cus_code.setText(post.get(position).getCust_Code());
//                        txt_cus_address.setText(post.get(position).getLocation());
//                        txt_cus_email.setText(post.get(position).getEmail_id());
//                        txt_cus_phone.setText(post.get(position).getMobile());
//
//                        String up_Location = post.get(position).getLocation();
//                        //String up_Latitude = String.valueOf(post.get(position).getLatitude());
//                        //String up_Logitude = String.valueOf(post.get(position).getLongitude());
//                        //  Log.d("Selected_chemist",Selected_chemist_id);
//
//                        Double upLangitude = post.get(position).getLatitude();
//                        Double upLogitude = post.get(position).getLongitude();
//
//                        Log.d("print_data11",post.get(position).getLatitude()+" "+post.get(position).getLongitude());
//
//                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                        //editor.clear();
//                        //editor.commit();
//                        editor.putString("Selected_chemist_id", Selected_chemist_id);
//                        editor.commit();
//                        //=editor.apply();
//
//                        //  if(upLangitude != null || upLogitude != null){
//
//                        String cheLatitude = String.valueOf(post.get(position).getLatitude());
//                        String cheLogitude = String.valueOf(post.get(position).getLongitude());
//
//                        Log.d("print_data12",cheLatitude+" "+cheLogitude);
//
//
//                        if(cheLatitude.equals("0.0") || cheLogitude.equals("0.0") ){
//
//                            linearLayout.setVisibility(View.VISIBLE);
//
//                        }else {
//
//                            linearLayout.setVisibility(View.GONE);
//                        }
//
//
//                      /*  if (up_Logitude != null || up_Latitude != null || !up_Latitude.isEmpty() || !up_Logitude.isEmpty() ) {
//
//                                linearLayout.setVisibility(View.GONE);
//
//                            //  Log.d("up_location11","location is null");
//                            } else {
//
//                                linearLayout.setVisibility(View.VISIBLE);
//                            //Log.d("up_location12","location not null");
//                            }*/
//
//
//                        if (post.get(position).getOutstanding_Bill() != null) {
//                            txt_cus_outstanding.setText(post.get(position).getOutstanding_Bill());
//                        }
//
//                    }
//                });
//
//            }
//
//
//            @Override
//            public int getItemCount() {
//                return post.size();
//            }
//
//
//        };
//        rvCustomerlist.setLayoutManager(new LinearLayoutManager(CustomerlistActivityNew.this));
//        rvCustomerlist.setAdapter(adapter);
//
//    }


    /* Show BottomView */
    private void show_bottom_sheet(View view, String cust_name) {
        //  int itemPosition = rvCustomerlist.getChildLayoutPosition(view);
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        //OGtoast.OGtoast(""+itemPosition , CustomerlistActivity.this);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        btm_text_cust_name.setText(cust_name);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) _fab.getLayoutParams();
        p.setAnchorId(R.id.bottom_sheet);
        _fab.setLayoutParams(p);
    }

    private void create_snack_bar(View view) {
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);

        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        LayoutInflater mInflater = LayoutInflater.from(CustomerlistActivityNew.this);
        View snackView = mInflater.inflate(R.layout.snackbar_customer_list, null);


        TextView txt_geo_location = (TextView) snackView.findViewById(R.id.txt_geo_location);
        TextView txt_logout = (TextView) snackView.findViewById(R.id.txt_logout);

        txt_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });


        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                _fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });
        layout.addView(snackView);
        layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_UP) {

                    if (snackbar != null) {
                        snackbar.dismiss();
                    }

                    return true;
                } else {
                    return false;
                }

            }
        });
        snackbar.show();
    }

    private void Show_pending_bills() {
        //    Log.d("Pending", "ALL ORDERS ARE SHOWING");
        Intent intent = new Intent(CustomerlistActivityNew.this, IndividualPendingBillsActivity.class);
        intent.putExtra(CHEMIST_ID, Selected_chemist_id);
        intent.putExtra(CLIENT_NAME, Selected_client_name);
        startActivity(intent);
    }

    private void Show_pending_bills_new() {
        //    Log.d("Pending", "ALL ORDERS ARE SHOWING");
        Intent i = new Intent(CustomerlistActivityNew.this, SelectedpaymentList.class);
        i.putExtra(CLIENT_NAME, Selected_client_name);
        //  i.putExtra("Call_plan_id", getIntent().getStringExtra("Call_plan_id"));
        i.putExtra("chemist_id", Selected_chemist_id);
        i.putExtra("flag", "customer");
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("chemist_id", Selected_chemist_id);
        editor.putString("flag", "customer");
        editor.putString(CLIENT_NAME, Selected_client_name);
        editor.apply();
        startActivity(i);
    }

    private void Show_Orders() {
        //    Log.d("SHOWORDERS", "ALL ORDERS ARE SHOWING");
        Intent intent = new Intent(CustomerlistActivityNew.this, Order_list.class);
        intent.putExtra(SELECTED_CHEMIST_ID, Selected_chemist_id);
        startActivity(intent);
    }

    private void Show_sales_returns() {
        Intent intent = new Intent(CustomerlistActivityNew.this, SalesReturnActivity.class);
        // Intent intent = new Intent(CustomerlistActivity.this, New_SalesReturn_screen.class);
        intent.putExtra(CHEMIST_STOCKIST_NAME, Selected_client_name);
        intent.putExtra("Chemist_Id", Selected_chemist_id);
        startActivity(intent);
    }

    private void Create_new_order() {
        Intent intent = new Intent(CustomerlistActivityNew.this, Create_Order_Salesman.class);
        intent.putExtra(CHEMIST_STOCKIST_NAME, Selected_client_name);
        intent.putExtra("client_id", Selected_chemist_id);
        startActivity(intent);
        globalVariable.setFromCustomerList(true);
    }

    private void current_location() {

        Log.d("you_click", "update_current location");
        //update_Current_locationn();

        locationManager = (LocationManager) getApplicationContext().getSystemService(this.LOCATION_SERVICE);

        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            Log.d("GPS", "enable");

          //  update_Current_locationn();
        } else {
            Log.d("GPS", "not enable");
            //showSettingsAlert();
            Intent intent = new Intent(CustomerlistActivityNew.this, LocationActivity.class);
            startActivity(intent);
        }


        //   new get_current_location_new(CustomerlistActivity.this);
        // new get_current_location(CustomerlistActivity.this);
    }

    void get_location() {


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
            } else {
                requestPermission();
            }

        } else {
            showSettingsAlert();

        }

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
        new android.support.v7.app.AlertDialog.Builder(CustomerlistActivityNew.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        // Log.d("onLocationupdate16", response.toString());

        if (response!=null) {
            if (f_name.equals(AppConfig.POST_UPDATE_CHEMIST_LOCATION)) {
                Log.d("LocationUpdate17", response.toString());
                try {
                    String status = response.getString("Status");
                    if (status.equalsIgnoreCase("success")) {
                        //  confirmed_dialog();
                        new android.support.v7.app.AlertDialog.Builder(this)
                                .setTitle("Location")
                                .setCancelable(false)
                                .setMessage("Updated successfully.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                         // finish();
                                  //  Log.i("Tag Check List",""+mModels.size());
                                    fill_payment_list(mModels);
                                       // behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                               //      _bottomSheet.setVisibility(View.GONE);
                                        behavior.setHideable(true);
                                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                                    }
                                }).show();
                    } else {
                        Toast.makeText(CustomerlistActivityNew.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    void confirmed_dialog() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Location")
                .setCancelable(false)
                .setMessage("Updated successfully.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
// Intent intent = new Intent(Delivery.this, Order_list_delivery.class);
// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
// startActivity(intent);
// finish();

                    }
                }).show();
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if (response != null) {
            try {
                Log.d("onLocationupdate16", response.toString());

                if (f_name.equals(AppConfig.GET_STOCKIST_CUSTOMERLIST_STOCKISTLOGIN)) {
                    //Log.e("Customers11",response.toString());

                    mStokiestmodels = new ArrayList<m_stokiest_distributor_list>();
                    String jsonData = response.toString();

                    if (!jsonData.isEmpty()) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        mStokiestmodels = Arrays.asList(gson.fromJson(jsonData, m_stokiest_distributor_list[].class));
                    }
                   // stokiest_recycleview(mStokiestmodels);
                }
                if (f_name.equals(AppConfig.GET_STOCKIST_CUSTOMERLIST)) {
                    Log.i("Call Api",""+AppConfig.GET_STOCKIST_CUSTOMERLIST);
                    mModels = new ArrayList<m_customerlist_new>();
                    String jsondata = response.toString();
                    if (!jsondata.isEmpty()) {
                        Log.d("jsondataCheck", jsondata.toString());
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        mModels = Arrays.asList(mGson.fromJson(jsondata, m_customerlist_new[].class));
                        saveChemistListInSQLite(jsondata);
                    }
                    //update_recycleview(mModels);
                    fill_payment_list(mModels);
                }

            } catch (Exception e) {
                e.toString();
            }
        }
    }

    public  void  fill_payment_list(final List<m_customerlist_new> posts_s)
    {
        txt_cust_count.setText(posts_s.size() + " Customers");
        Log.d("size",String.valueOf(posts_s.size()));
        mAdapter = new CustomerListAdapter(getApplicationContext(),posts_s,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.scrollToPosition(0);
    }

    /* Save List Into SQLite */
    private void saveChemistListInSQLite(String response) {
        Cursor cursor = sqLiteHandler.getChemistListSalesmanForCreateOrder(User_id);
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            sqLiteHandler.deleteChemistListSalesmanForCreateOrder(User_id);
        }
        try {
            JSONArray jsonArray = new JSONArray(response);
            //Log.d("JsonArrayCheck", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String chemistId = jsonObject.getString("Chemist_id");
                String custCode = jsonObject.getString("Cust_Code");
                String custName = jsonObject.getString("CustomerName");
                String email = jsonObject.getString("Email_id");
                String mobile = jsonObject.getString("Mobile");
                String location = jsonObject.getString("Location");
                String latitude = jsonObject.getString("Latitude");
                String longitude = jsonObject.getString("Longitude");
                String outStandingBill = jsonObject.getString("Outstanding_Bill");
            /*  String DLExpiry = jsonObject.getString("DLExpiry");
                String DLExpInd = jsonObject.getString("DLExpInd");
*/
              //  Log.d("CheckChemistId", ""+DLExpInd);
                if (chemistId.equalsIgnoreCase("null")) {
                    chemistId = "";
                }
                if (custCode.equalsIgnoreCase("null")) {
                    custCode = "";
                }
                if (email.equalsIgnoreCase("null")) {
                    email = "";
                }
                if (mobile.equalsIgnoreCase("null")) {
                    mobile = "";
                }
                if (location.equalsIgnoreCase("null")) {
                    location = "";
                }
                if (latitude.equalsIgnoreCase("null")) {
                    latitude = "0.0";
                }
                if (longitude.equalsIgnoreCase("null")) {
                    longitude = "0.0";
                }
                if (outStandingBill.equalsIgnoreCase("null")) {
                    outStandingBill = "0";
                }

              /*  if(DLExpiry.equals("null")||DLExpiry.equals(null)||DLExpiry.equals("")){
                    DLExpiry = "0";
                }
                if(DLExpInd.equals("null")||DLExpInd.equals(null)||DLExpInd.equals("")) {
                    DLExpInd ="0";
                }*/
                sqLiteHandler.insertChemistListSalesmanForCreateOrder(User_id, String.valueOf(chemistId), custCode,
                        custName, email, String.valueOf(mobile), location, String.valueOf(latitude), String.valueOf(longitude),
                        String.valueOf(outStandingBill));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






//    private void stokiest_recycleview(final List<m_stokiest_distributor_list> mStokiestmodels) {
//
//        //Past code Here
//        txt_cust_count.setText(mStokiestmodels.size() + " Customers");
//        adapter = new RecyclerView.Adapter<BindingViewHolder>() {
//            @Override
//            public BindingViewHolder onCreateViewHolder(ViewGroup parent,final int viewType) {
//                LayoutInflater inflater = LayoutInflater.from(CustomerlistActivityNew.this);
//                ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragement_stokiest_customerlist_items, parent, false);
//                //fragement_stokiest_customerlist_items
//                return new BindingViewHolder(binding.getRoot());
//            }
//
//
//            @Override
//            public void onBindViewHolder(BindingViewHolder holder, final int position) {
//                m_stokiest_distributor_list stock_customerlist = mStokiestmodels.get(position);
//                holder.getBinding().setVariable(BR.v_stokiestcustomerlist, stock_customerlist);
//                holder.getBinding().executePendingBindings();
//                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        show_bottom_sheet(v, mStokiestmodels.get(position).getClient_LegalName());
//                        Selected_chemist_id = mStokiestmodels.get(position).getClient_Code();
//                        selected_Ten_orders = Selected_chemist_id;
//                        Selected_client_name = mStokiestmodels.get(position).getClient_LegalName();
//                        txt_name.setText(mStokiestmodels.get(position).getClient_LegalName());
//                        txt_cus_code.setText(mStokiestmodels.get(position).getClient_Code());
//                        txt_cus_address.setText(mStokiestmodels.get(position).getCityName());
//                        txt_cus_email.setText(mStokiestmodels.get(position).getClient_Email());
//                        txt_cus_phone.setText(mStokiestmodels.get(position).getClient_Contact());
//
//                        /*String current_Location = mStokiestmodels.get(position).getClient_Address();
//
//                        Log.d("curent_Location",current_Location);*/
//
//                        /*txt_cus_phone.setText(post.get(position).getEmail_id());
//                        if (post.get(position).getOutstanding_Bill() != null) {
//                            txt_cus_outstanding.setText(post.get(position).getOutstanding_Bill());
//                        }*/
//                    }
//                });
//
//
//                /*m_customerlist_new omline_customerlist = post.get(position);
//                holder.getBinding().setVariable(BR.v_customerlist, omline_customerlist);
//                holder.getBinding().executePendingBindings();
//
//                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//
//                        show_bottom_sheet(v, post.get(position).getCustomerName());
//
//                        Selected_chemist_id = post.get(position).getChemist_id();
//
//                        selected_Ten_orders = Selected_chemist_id;
//
//                        Selected_client_name = post.get(position).getCustomerName();
//                        txt_name.setText(post.get(position).getCustomerName());
//                        txt_cus_code.setText(post.get(position).getCust_Code());
//                        txt_cus_address.setText(post.get(position).getLocation());
//                        txt_cus_email.setText(post.get(position).getEmail_id());
//                        txt_cus_phone.setText(post.get(position).getEmail_id());
//                        if (post.get(position).getOutstanding_Bill() != null) {
//                            txt_cus_outstanding.setText(post.get(position).getOutstanding_Bill());
//                        }
//
//                    }
//                });*/
//
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return mStokiestmodels.size();
//            }
//        };
//        rvCustomerlist.setLayoutManager(new LinearLayoutManager(CustomerlistActivityNew.this));
//        rvCustomerlist.setAdapter(adapter);
//    }

    void filter_on_text(String newText) {
        newText.toLowerCase();
        List<m_customerlist_new> filteredModelList = new ArrayList<>();
        for (m_customerlist_new model : mModels) {

            if (model.getCustomerName() != null) {
                final String text = model.getCustomerName().toLowerCase();
                if (text.contains(newText)) {
                    filteredModelList.add(model);
                }
            }
        }
       // update_recycleview(filteredModelList);
        fill_payment_list(filteredModelList);
        //rvCustomerlist.scrollToPosition(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(CustomerlistActivityNew.this,MainActivity.class);
        startActivity(i);
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        // stop the listener
        this.getcurrentLocation.stopGettingLocationUpdates();

        Log.d("location13","on DEstroy");
        //  get_current_location.stopGettingLocationUpdates();
    }*/


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

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

}
