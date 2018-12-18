package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.databinding.library.baseAdapters.BR;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.adapter.ad_AutocompleteCustomArray_Salesman;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.database.MasterPlacedOrder;
import com.synergy.keimed_ordergenie.database.StockistProducts;
import com.synergy.keimed_ordergenie.utils.BadgeDrawable;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.CustomAutoCompleteView;
import com.synergy.keimed_ordergenie.utils.LocationActivity;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.RefreshData;
import com.synergy.keimed_ordergenie.utils.SharedPref;
import com.synergy.keimed_ordergenie.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class Create_Order_Salesman extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    CoordinatorLayout coordinatorLayout;
    ArrayList<StockistProducts> posts = new ArrayList<>();
    List<StockistProducts> sreach_product_list = new ArrayList<>();
    private SharedPreferences pref;
    private SQLiteHandler db;
    Button Search_History;
    String str1 = "NA";
    private static String ProductName;
    private EditText editText, txt_comment;
    Boolean Clicked_cart = false;
    public static final String CHEMIST_STOCKIST_ID = "chemist_stockist_id";
    public static final String CHEMIST_STOCKIST_NAME = "chemist_stockist_name";
    // List<ChemistCart> cart;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private Snackbar snackbar;
    private BottomSheetBehavior behavior;
    private String Doc_Id, client_id;
    private String legend_data, legend_mode, legendName;
    private String call_plan_customer_id, Flag;
    private Boolean Cart_Id_available = false;
    private Menu mToolbarMenu;
    private Integer n_product_cart_count = 0;
    AppController globalVariable;
    private String Client_id;
    String stockk;
    ResultReceiver resultReceiver;
    private TextView txt_cust_name, txt_order_id, txt_total_items, txt_total, editQty;
    // @BindView(R.id.autoCompleteTextView)
    CustomAutoCompleteView _autoCompleteTextView;
    ad_AutocompleteCustomArray_Salesman adpter;
    //ad_AutocompleteCustomArray adpter;
    private ChemistCartDao chemistCartDao;
    TextView mfg, Dosefrom, Packsize, PTR, MRP, Scheme, Halfscheme, Percentscheme;
    Toolbar toolbar;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    FragmentManager fm = getSupportFragmentManager();
    private boolean isAvailable = true;
    private SearchView searchView;
    private LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    private String locationProvider;
    private double currentLocLat = 0;
    private double currentLocLong = 0;

    @BindView(R.id.stock)
    TextView stock;

    @BindView(R.id.btnminus)
    Button _btnminus;

    @BindView(R.id.rv_Cartdatalist)
    RecyclerView _rv_Cartdatalist;

    @BindView(R.id.btnplus)
    Button _btnplus;

    @BindView(R.id.Qty)
    EditText _Qty;

    @BindView(R.id.OrderAmt)
    TextView _OrderAmt;

    @BindView(R.id.remark)
    TextView _remark;

    @BindView(R.id.txt_customer_name)
    TextView _txt_customer_name;

    @BindView(R.id.addProduct)
    Button _addProduct;

    @BindView(R.id.cancelOrder)
    Button _cancelOrder;

    @BindView(R.id.confirmOrder)
    Button _confirmOrder;

    @BindView(R.id.bottom_sheet)
    RelativeLayout _bottumLayout;

    @Nullable
    @BindView(R.id.fab)
    FloatingActionButton _fab;

    @BindView(R.id.main_coordinate)
    CoordinatorLayout _main_coordinate;

    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.layout9)
    LinearLayout layout9;

    @BindView(R.id.layout1)
    RelativeLayout layout1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @OnClick(R.id.fab)
    void onclickfab(View view) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setHideable(true);
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            //behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            // get_CartData();
            // _fab.setVisibility(View.GONE);
        }
    }

    int Qty = 1;

    Integer type;
    String Itemname, Pack, MfgName, DoseForm, Sche, Remark, Itemcode, MfgCode, ProductId, UOM, halfScheme, perScheme,minQ,maxQ;
    Float vMrp, vRate;
    DaoSession daoSession;

    String login_type;
    private String User_id;
    private List<ChemistCart> chemistCartList;
    //  double orderAmount;
    float orderAmount;

    /* Get Intent String */
    private String chemistName;
    private String accessKey;

    private double Latitude;
    private double Longitude;
    private String address;

    /* Current Date */
    //private String currentDate;

    /* Cart Edited Quantity */
    private RecyclerView.Adapter<BindingViewHolder> cartAdapter;
    int editedQuantity = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__order);

        ButterKnife.bind(this);
        db = new SQLiteHandler(this);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(_rv_Cartdatalist.getContext(), DividerItemDecoration.VERTICAL);
        _rv_Cartdatalist.addItemDecoration(dividerItemDecoration);


        // setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        resultReceiver = getIntent().getParcelableExtra("receiver");
        SharedPreferences prefs = getSharedPreferences("MY PREF", MODE_PRIVATE);

        // ***call_plan_customer_id = prefs.getString("chemist_id", null);
        call_plan_customer_id = getIntent().getStringExtra("client_id");
        Flag = getIntent().getStringExtra("flag");
        globalVariable = (AppController) getApplicationContext();
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");

        User_id = pref.getString(USER_ID, "0");
        Client_id = pref.getString(CLIENT_ID, "0");
        chemistName = getIntent().getStringExtra(CHEMIST_STOCKIST_NAME);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .main_coordinate);
        editText = (EditText) findViewById(R.id.edit_comment);
        _txt_customer_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
        _autoCompleteTextView = (CustomAutoCompleteView) findViewById(R.id.autoCompleteTextView);
        mfg = (TextView) findViewById(R.id.mfg);
        //final TextView mfg=(TextView)mContext.getApplicationInfo().findViewById(R.id.mfg);
        //Toast.makeText(this, "CallPlanId: " + call_plan_customer_id + "\nClientId: " + Client_id, Toast.LENGTH_SHORT).show();
        Dosefrom = (TextView) findViewById(R.id.Doseform);
        Search_History = (Button) findViewById(R.id.search_history);
        Packsize = (TextView) findViewById(R.id.Packsize);

        PTR = (TextView) findViewById(R.id.PTR);
        MRP = (TextView) findViewById(R.id.MRP);
        Scheme = (TextView) findViewById(R.id.Scheme);
        Halfscheme = (TextView) findViewById(R.id.txt_half_scheme);
        Percentscheme = (TextView) findViewById(R.id.txt_percentage);

        behavior = BottomSheetBehavior.from(_bottumLayout);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset == 0.0) {
                    _rv_Cartdatalist.removeAllViews();
                }
            }
        });
        client_id = pref.getString(CLIENT_ID, "");

        /* Initialize Dao Session Class */
        daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();

        setTitle("Take Order");
        //setSupportActionBar(mToolbarMenu);


        /* Get Access Token */
        accessKey = pref.getString("key", "");


        Utility.syncWithoutProgress(this, "Wait a moment...");
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOfflineProductList();
                }
            }, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if (Utility.internetCheck(this)) {
            new checkInternetConnection().execute(0);
        }*/
        ViewTreeObserver vto = layout1.getViewTreeObserver();
        //   final View content = activity.findViewById(android.R.id.content).getRootView();
        //get_stockist_legends();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                layout1.getWindowVisibleDisplayFrame(r);
                int screenHeight = layout1.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    layout9.setVisibility(View.GONE);
                } else {
                    // keyboard is closed
                    layout9.setVisibility(View.VISIBLE);
                }
            }
        });
        init();

       /* _autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(posts.size()>0) {
                    filter(_autoCompleteTextView.getText().toString());
                   *//* adpter = new ad_AutocompleteCustomArray(Create_Order_Salesman.this, sreach_product_list);
                    _autoCompleteTextView.setAdapter(adpter);*//*
                }else
                {
                    get_ProductList_json();
                }

            }
        });
*/

//        _autoCompleteTextView.setOnFocusChangeListener(new AdapterView.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//            }
//        });

        _autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if(_rv_Cartdatalist.getVisibility()==View.VISIBLE){
                _rv_Cartdatalist.setVisibility(View.GONE);
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        // _autoCompleteTextView.setThreshold(1);

        _autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    _addProduct.setEnabled(true);
                    _addProduct.setClickable(true);
                    _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
                    _Qty.requestFocus();
                    _Qty.setText("");
                    // if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                    int state = behavior.getState();
//                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    // }

                    //Student selected = (Student) arg0.getAdapter().getItem(arg2);

                    //arg1.getTag(R.id.key_product_Mfg);
                    //String fmgname=(String)_autoCompleteTextView.getTag(R.id.key_product_Mfg);
                    ProductId = arg1.getTag(R.id.key_product_code).toString();
                    Itemcode = (arg1.getTag(R.id.key_product_ItemCode).toString());
                    //minQ="-6";
                    minQ = arg1.getTag(R.id.key_product_minQ).toString();
                    maxQ = arg1.getTag(R.id.key_product_maxQ).toString();
                   // maxQ="6";
                    //AppConfig.AppCode==1 is For Unnati
                    if(AppConfig.AppCode==1){
                        halfScheme = (arg1.getTag(R.id.key_product_BoxSize).toString());

                    }else{
                        halfScheme = (arg1.getTag(R.id.key_product_halfscheme).toString());
                    }

                    perScheme = (arg1.getTag(R.id.key_product_percentscheme).toString());
                    // mfg.setText(String.valueOf(arg1.getTag(R.id.key_product_Mfg)));
                    mfg.setText(String.valueOf(arg1.getTag(R.id.key_product_Mfg)));

                    _autoCompleteTextView.setText(String.valueOf(arg1.getTag(R.id.key_product_Name)));
                    if (String.valueOf(arg1.getTag(R.id.key_product_Dose)) == null || String.valueOf(arg1.getTag(R.id.key_product_Pack)).equals("null")) {
                        Dosefrom.setText("---");
                    } else {
                        Dosefrom.setText(String.valueOf(arg1.getTag(R.id.key_product_Dose)));
                    }
                    if (String.valueOf(arg1.getTag(R.id.key_product_Pack)) == null || String.valueOf(arg1.getTag(R.id.key_product_Pack)).equals("null")) {
                        Packsize.setText("---");
                    } else {
                        Packsize.setText(String.valueOf(arg1.getTag(R.id.key_product_Pack)));
                    }

                    if (String.valueOf(arg1.getTag(R.id.key_product_Scheme)) == null || String.valueOf(arg1.getTag(R.id.key_product_Scheme)).equals("null") || String.valueOf(arg1.getTag(R.id.key_product_Scheme)).equals("")) {
                        Scheme.setText("---");
                    } else {
                        Scheme.setText(String.valueOf(arg1.getTag(R.id.key_product_Scheme)));
                    }
                    //---------------------------half scheme percent scheme edit

                    if(AppConfig.AppCode == 1){
                        if (String.valueOf(arg1.getTag(R.id.key_product_BoxSize)) == null || String.valueOf(arg1.getTag(R.id.key_product_BoxSize)).equals("null") || String.valueOf(arg1.getTag(R.id.key_product_BoxSize)).equals("")) {
                            Halfscheme.setText("Box Pack Size: NA");
                        } else {
                            Halfscheme.setText("Box Pack Size: " + String.valueOf(arg1.getTag(R.id.key_product_BoxSize)));
                            // Halfscheme.setText("Box Pack Size: NA");

                        }

                    }else{
                        if (String.valueOf(arg1.getTag(R.id.key_product_halfscheme)) == null || String.valueOf(arg1.getTag(R.id.key_product_halfscheme)).equals("null") || String.valueOf(arg1.getTag(R.id.key_product_halfscheme)).equals("")) {
                            Halfscheme.setText("Half Scheme: NA");
                        } else {
                            Halfscheme.setText("Half Scheme: " + String.valueOf(arg1.getTag(R.id.key_product_halfscheme)));
                            // Halfscheme.setText("Box Pack Size: NA");

                        }

                    }



                    if (String.valueOf(arg1.getTag(R.id.key_product_percentscheme)) == null || String.valueOf(arg1.getTag(R.id.key_product_percentscheme)).equals("null") || String.valueOf(arg1.getTag(R.id.key_product_percentscheme)).equals("")) {
                        Percentscheme.setText("% Scheme: NA");
                    } else {
                        Percentscheme.setText("% Scheme: " + String.valueOf(arg1.getTag(R.id.key_product_percentscheme)));
                    }


                    // if (!arg1.getTag(R.id.key_product_PTR).equals("null")) {
                    if (!arg1.getTag(R.id.key_product_PTR).equals("null") || !arg1.getTag(R.id.key_product_PTR).equals(" ") || !arg1.getTag(R.id.key_product_PTR).toString().isEmpty()) {
                        vRate = Float.parseFloat(arg1.getTag(R.id.key_product_PTR).toString());
                    } else {
                        vRate = (float) 0;
                    }
                    if (!arg1.getTag(R.id.key_product_MRP).equals("null")) {
                        vMrp = Float.parseFloat(arg1.getTag(R.id.key_product_MRP).toString());
                    } else {
                        vMrp = (float) 0;
                    }
                    if (arg1.getTag(R.id.key_product_Dose).equals("null") || !arg1.getTag(R.id.key_product_Dose).equals(" ") || !arg1.getTag(R.id.key_product_Dose).toString().isEmpty()) {
                        UOM = arg1.getTag(R.id.key_product_Dose).toString();
                    }

                    if (!arg1.getTag(R.id.key_product_stock).equals("null")) {
                        //  setStockLegend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                        // set_stock_color_legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));

                        if (arg1.getTag(R.id.key_product_stock) == str1 || arg1.getTag(R.id.key_product_stock).toString().equalsIgnoreCase("NA")) {

                            stock.setBackgroundColor(Color.parseColor(arg1.getTag(R.id.key_product_legendcolor).toString()));

                            //  stock.setText(arg1.getTag(R.id.key_product_legendname).toString());

                        } else {
//                            stock.setBackgroundColor(Color.parseColor(arg1.getTag(R.id.key_product_legendcolor).toString()));
//                            stock.setText(arg1.getTag(R.id.key_product_legendname).toString());
                            if (arg1.getTag(R.id.key_product_legendname).toString().equals("NA")) {

                                stock.setBackgroundColor(Color.parseColor(arg1.getTag(R.id.key_product_legendcolor).toString()));
                                stock.setText("");

                            } else {

                                stock.setBackgroundColor(Color.parseColor(arg1.getTag(R.id.key_product_legendcolor).toString()));
                                stock.setText(arg1.getTag(R.id.key_product_legendname).toString());

                            }
                        }
                    }

                    // arg1.getTag(R.id.key_product_stock).toString();
                    PTR.setText("Rs." + String.valueOf(vRate));
                    MRP.setText("Rs." + String.valueOf(vMrp));
                    //Scheme.setText(String.valueOf(arg1.getTag(R.id.key_product_Scheme)));

                    MfgCode = (arg1.getTag(R.id.key_product_MfgCode).toString());
                    //    type = Integer.parseInt(arg1.getTag(R.id.key_product_Type).toString());
                    type = 0;

                    Itemname = (arg1.getTag(R.id.key_product_Name) != null ? arg1.getTag(R.id.key_product_Name).toString() : "NA");
                    // Pack = arg1.getTag(R.id.key_product_Pack).toString();
                    Pack = (arg1.getTag(R.id.key_product_Pack) == null) ? "NA" : arg1.getTag(R.id.key_product_Pack).toString();
                    MfgName = arg1.getTag(R.id.key_product_Mfg).toString();
                    DoseForm = arg1.getTag(R.id.key_product_Dose).toString();

                    if (arg1.getTag(R.id.key_product_Scheme).toString().equalsIgnoreCase("")) {
                        Sche = "NA";
                    } else {
                        Sche = arg1.getTag(R.id.key_product_Scheme).toString();
                    }

                    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.setHideable(true);
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
                }
                //comment by apurva to show keyboard

//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(_autoCompleteTextView.getWindowToken(), 0);

                //Toast.makeText(Create_Order_Salesman.this, "itemClick", Toast.LENGTH_SHORT).show();

            }
        });


        //btnminus Button click Events

        _btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                // int num1;
                try {
                    _addProduct.setEnabled(true);
                    _addProduct.setClickable(true);
                    _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
                    String value = _Qty.getText().toString();
                    int num1 = Integer.parseInt(value);
                    if (num1 > 1) {
                        Qty = num1 - 1;
                    }
                    _Qty.setText(String.valueOf(Qty));
                } catch (Exception e) {

                }

            }
        });


        //btnplus Button click Events

        _btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    _addProduct.setEnabled(true);
                    _addProduct.setClickable(true);
                    _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
                    String value = _Qty.getText().toString();
                    if (!value.isEmpty()) {
                        int num1 = Integer.parseInt(value);
                        Qty = num1 + 1;
                    }
                    _Qty.setText(String.valueOf(Qty));
                } catch (Exception e) {

                }
            }
        });

        _Qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                _addProduct.setEnabled(true);
                _addProduct.setClickable(true);
                _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Search_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ProductName != null) {
                    _autoCompleteTextView.setText(ProductName);
                    _autoCompleteTextView.setSelection(_autoCompleteTextView.getText().length());
                } else {
                    Toast.makeText(Create_Order_Salesman.this, "No Search History Found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /* dd Product Button */
        _addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    _addProduct.setEnabled(false);
                    _addProduct.setClickable(false);
                    _addProduct.setBackgroundColor(Color.parseColor("#d0ebfa"));

                    if (Itemcode != null && ProductId != null) {


                        if(QTYCheck(_Qty.getText().toString().trim(),minQ,maxQ)){
                            String value = _Qty.getText().toString();
                            if(value!=null&&value.toString().length()>0) {
                                int num1 = Integer.parseInt(value);
                                Qty = num1;
                            }
                        }else{
                            return;
                        }

                        if (_autoCompleteTextView.getText().toString().isEmpty() || _autoCompleteTextView.getText().toString().equals(null)) {
                            OGtoast.OGtoast("Please select a product", Create_Order_Salesman.this);
                        }
                        else {
//                            if (_Qty.getText().length() == 0) {
//                                OGtoast.OGtoast("Please select quantity", Create_Order_Salesman.this);
//
//                            } else {
                            // ProductName= _autoCompleteTextView.getText().toString();
                            String str = _autoCompleteTextView.getText().toString();
                            if (str.contains(" ")) {
                                String[] arrOfStr = str.split(" ", 0);
                                ProductName = arrOfStr[0];
                            }
//                            else if(str.contains("."))
//                            {
//                                String[] arrOfStr = str.split(".", 0);
//                                ProductName = arrOfStr[0];
//                            }
//                            else if(str.contains("-"))
//                            {
//                                String[] arrOfStr = str.split("-", 0);
//                                ProductName = arrOfStr[0];
//                            }
                            else {
                                ProductName = _autoCompleteTextView.getText().toString();
                            }
                            /*if (_Qty.getText().toString().equals("")) {
                                Qty = 1;
                            } else if (_Qty.getText().toString().equals("0")) {
                                Qty = 1;
                            } else {
                                Qty = Integer.parseInt(_Qty.getText().toString());
                            }*/
                            // addItemCart();
                            if (chemistCartList.size() > 0) {
                                isAvailable = true;
                                for (int i = 0; i < chemistCartList.size(); i++) {
                                    //  if (Itemcode.equals(chemistCartList.get(i).get)) {
                                    //  if (_autoCompleteTextView.getText().toString().equals(chemistCartList.get(i).getItemname())) {
                                    if (ProductId.equals(chemistCartList.get(i).getProduct_ID())) {
                                        isAvailable = false;
                                        Snackbar snackbar = Snackbar
                                                .make(findViewById(R.id.placeSnackBar), "This Product is Already Available", Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                        snackbar.show();

                                        //Toast.makeText(Create_Order_Salesman.this, "This Product Already Available", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            if (isAvailable) {
                                addItemCart();
                            }


                            mfg.setText("");
                            _autoCompleteTextView.setText("");
                            _autoCompleteTextView.requestFocus();


                            Dosefrom.setText("");
                            Packsize.setText("");
                            PTR.setText("");
                            MRP.setText("");
                            Scheme.setText("");
                            Halfscheme.setText("");
                            Percentscheme.setText("");
                            Qty = 1;
                            _Qty.setText("");
                            stock.setText("");
                            // stock.setBackgroundColor(Color.parseColor("#fff"));/*#fff*/
                            stock.setBackgroundColor(Color.WHITE);
                            Itemcode = null;
                            ProductId = null;
                            //  _rv_Cartdatalist.setVisibility(View.GONE);
                            //   hideKeyboard();
                            // OGtoast.OGtoast("Product added to cart succesfully", Create_Order_Salesman.this);
                            //  Snackbar snackbar = Snackbar
                            //   .make(v, "Product added to cart succesfully", Snackbar.LENGTH_LONG);
                    /*    View view = snackbar.getView();
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                        params.gravity = Gravity.TOP;
                        view.setLayoutParams(params);
                        snackbar.show();  */
                            //  }
                        }
                    } else {
                        OGtoast.OGtoast("Please select a product", Create_Order_Salesman.this);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //cancelOrder Button click Events

        _cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_order(v);
            }
        });

        //confirmOrder Button click Events

        _confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   Cursor crs_cart = db.get_chemist_cart(call_plan_customer_id);

                // Log.d("cardid", String.valueOf(crs_cart));


                if (chemistCartList != null && chemistCartList.size() > 0) {
                    LayoutInflater inflater = LayoutInflater.from(Create_Order_Salesman.this);
                    final View dialogview = inflater.inflate(R.layout.fragment_order_summary, null);
                    final Dialog infoDialog = new Dialog(Create_Order_Salesman.this);//builder.create();
                    infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    infoDialog.setContentView(dialogview);
                    txt_comment = (EditText) dialogview.findViewById(R.id.edit_comment);
                    txt_cust_name = (TextView) dialogview.findViewById(R.id.customer_name);
                    txt_order_id = (TextView) dialogview.findViewById(R.id.order_id);
                    txt_total_items = (TextView) dialogview.findViewById(R.id.total_items);
                    txt_total = (TextView) dialogview.findViewById(R.id.total_amount);
                    //  pgr.setVisibility(View.VISIBLE);
                    ChemistCart chemistCart = chemistCartList.get(0);
                    txt_cust_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
                    txt_order_id.setText(chemistCart.getDOC_ID());
                    txt_total_items.setText(String.valueOf(chemistCartList.size()));
                    txt_total.setText(String.valueOf(orderAmount));
                    // txt_comment.setFocusable(false);
                    //    hideKeyboard();
                    dialogview.findViewById(R.id.confirm_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            confirm_dialog(txt_comment.getText().toString());
                            //confirm_order(txt_comment.getText().toString());
                            infoDialog.dismiss();
                        }
                    });
//                    dialogview.findViewById(R.id.cancel_order).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view)
//                        {
//                            infoDialog.dismiss();
//                        }
//                    });

                    infoDialog.show();
                } else {
                    OGtoast.OGtoast("Cart is empty", Create_Order_Salesman.this);
                }
            }
        });

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
            //showSettingsAlert();
            Intent intent = new Intent(Create_Order_Salesman.this, LocationActivity.class);
            startActivity(intent);
        }


    }


    /* Current Date */
    /*private void currentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SharedPref.saveLastSyncDate(getContext(), simpleDateFormat.format(calendar.getTime()));
    }*/


//    _confirmOrder.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            //   Cursor crs_cart = db.get_chemist_cart(call_plan_customer_id);
//
//            // Log.d("cardid", String.valueOf(crs_cart));
//
//            if (chemistCartList != null && chemistCartList.size() > 0) {
//
//
//
//                new AlertDialog.Builder(Create_Order_Salesman.this)
//                        .setTitle("Order")
//                        .setMessage("Do you wish to confirm your order?")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                //confirm_order();
////                                    Fragment_OrderSummary_Dialog fragment = new Fragment_OrderSummary_Dialog();
////                                    fragment.show(fm, "Dialog Fragment");
//                                show_location_dialog();
//                                dialog.dismiss();
//
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//
//            } else {
//                OGtoast.OGtoast("Cart is empty", Create_Order_Salesman.this);
//            }
//
//        }
//    });
//
//
//}


    /* Confirm Order Dialog */
    void confirm_dialog(final String comment) {
        new AlertDialog.Builder(Create_Order_Salesman.this)
                .setTitle("Order")
                .setMessage("Do you wish to confirm your order?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirm_order(comment);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();


    }



    public void QTYDialog(String text){
        new AlertDialog.Builder(Create_Order_Salesman.this)
                .setTitle("Alert")
                .setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })

                .show();


    }

    /* Hide Soft Keyboard */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_autoCompleteTextView.getWindowToken(), 0);
    }

    private void hideKeyboardonedit() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    /* Get Cart Count */
    private void init() {
        chemistCartList = chemistCartDao.getCartItem(call_plan_customer_id, true);
        if (chemistCartList != null) {
            n_product_cart_count = chemistCartList.size();
        }
        // get_stockist_inventory(Client_id);
        if (n_product_cart_count > 0) {
            for (int i = 0; i < chemistCartList.size(); i++) {
                orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
            }
        }

        _OrderAmt.setText("Rs." + String.format("%.2f", orderAmount));

        // get_ProductList_json();
    }


    private void setStockLegend(int stockNum) {
        if (stockNum > 300) {
            stock.setBackgroundColor(Color.parseColor("#5371D7"));
        } else if (stockNum > 200) {
            stock.setBackgroundColor(Color.parseColor("#5CDA6D"));
        } else if (stockNum > 100) {
            stock.setBackgroundColor(Color.parseColor("#EFE040"));
        } else {
            stock.setBackgroundColor(Color.parseColor("#EF9940"));
        }
    }

    public int gen() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    /* Add Item Into Cart */
    private void addItemCart() {
        // Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
        Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + gen();
        Float price = vRate * Qty;
        ChemistCart chemistCart = new ChemistCart();
        chemistCart.setDOC_ID(Doc_Id);
        chemistCart.setItems(Itemcode);
        chemistCart.setItemname(Itemname);
        chemistCart.setProduct_ID(ProductId);
        chemistCart.setQty(String.valueOf(Qty));
        chemistCart.setUOM(UOM);
        chemistCart.setRate(vRate.toString());
        chemistCart.setPrice(price.toString());
        chemistCart.setMRP(String.valueOf(vMrp));
        chemistCart.setCreatedon(dateFormat.format(Calendar.getInstance().getTime()));
        chemistCart.setSalesman(true);
        chemistCart.setStockist_Client_id(call_plan_customer_id);
        chemistCart.setRemarks("cart");
        chemistCart.setOrder_sync(false);
        // chemistCart.setSalesman(true);
        // chemistCart.setDoc_Date(dateFormat.format(Calendar.getInstance().getTime()));
        chemistCart.setAmount(price.toString());
        chemistCart.setPACK(Pack);
        chemistCart.setScheme(Sche);
        chemistCart.setHalfScheme(halfScheme);
        chemistCart.setSub_stkID("");
        chemistCart.setMinQ(minQ);
        chemistCart.setMaxQ(maxQ);

        //  chemistCart.setPercentScheme(perScheme);
        //chemistCart.setStatus(false);
        //chemistCart.
        //chemistCart.se
//comment by apurva 20/9
//        price += orderAmount;
//        orderAmount = price;
        //Waseem


        SharedPreferences pref = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String uID = pref.getString("UUID", "0");
        Log.d("location_order111", uID);
        String trasaction_No = pref.getString("Transaction_No", "");
        // Log.d("order_salesman10",trasaction_No);
        db.insetDataForLocation(User_id, call_plan_customer_id, trasaction_No, "Order", String.valueOf(Latitude),
                String.valueOf(Longitude), uID, address,Doc_Id);
        //  _OrderAmt.setText(" Rs." + String.format("%.2f", price));
        chemistCartDao.insertOrUpdateCart(chemistCart, true);
        chemistCartList.clear();
        chemistCartList = chemistCartDao.getCartItem(call_plan_customer_id, true);
        n_product_cart_count = chemistCartList.size();
        createCartBadge(n_product_cart_count);
        // Log.d("addcart",chemistCart.getMRP()+"sch"+chemistCart.getScheme()+"sale"+chemistCart.getSalesman()+"price"+chemistCart.getPrice()+"uom"+chemistCart.getUOM()+"itemname-"+chemistCart.getItemname()+"rate-"+chemistCart.getRate());
//        if (chemistCartList.size()>0)
//        {
//
//                ProductName = chemistCartList.get(chemistCartList.size() - 1).getItemname();
//                Log.d("Name", ProductName);
//
//        }

        adpter.notifyDataSetChanged();

        orderAmount = 0;
        for (int i = 0; i < chemistCartList.size(); i++) {
            orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
        }
        _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmount));
        // createCartBadge(chemistCartDao.ge);
        //Log.v("GreenDao", chemistCart.getId().toString());
        if (Qty == 1) {
            //OGtoast.OGtoast("one quantity added", Create_Order_Salesman.this);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.placeSnackBar), "one quantity added", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        } else {

            // CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.placeSnackBar), "Product added to cart succesfully", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
            //  View view = snackbar.getView();
            //  CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
//
//           params.gravity = Gravity.TOP;
//            view.setLayoutParams(params);

//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, "Product added to cart succesfully", Snackbar.LENGTH_SHORT);
//            snackbar.show();
            // OGtoast.OGtoast("Product added to cart succesfully", Create_Order_Salesman.this);
        }
    }


    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        mToolbarMenu = paramMenu;
        if (n_product_cart_count != null) {
            createCartBadge(n_product_cart_count);
        }
        return super.onPrepareOptionsMenu(paramMenu);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_order, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setVisibility(View.GONE);
        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchAutoComplete.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);
        //   String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};

        posts = new ArrayList<>();
        if (sreach_product_list.size() == 0 || sreach_product_list == null) {
            sreach_product_list = daoSession.getStockistProductsDao().loadAll();
        }
        //    Log.e("sreach_product_list", sreach_product_list.toString());
        posts = new ArrayList<>(sreach_product_list);

        // Log.d("posttt",posts.toString());
//        Log.d("legendd",legend_data);

        /* NEW */
        //adpter = new ad_AutocompleteCustomArray_Salesman(this, posts, SharedPref.getLegendData(this),sales_legend_mode);

        // adpter = new ad_AutocompleteCustomArray_Salesman(this, posts, SharedPref.getLegendData(this));
       //adpter = new ad_AutocompleteCustomArray_Salesman(this, posts, SharedPref.getLegendData(this));
        adpter = new ad_AutocompleteCustomArray_Salesman(this, posts);

        // adpter = new ad_AutocompleteCustomArray(this, posts, legend_data);
        // ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setAdapter(adpter);


        return true;
    }

    private boolean QTYCheck(String value,String minQ,String maxQ){
        boolean check =false;
        if (!maxQ.equalsIgnoreCase("0")) {
            if (!maxQ.equalsIgnoreCase("0")) {
                int max = Integer.parseInt(maxQ);

                if(max<0){
                    if(value.toString().length()>0) {
                        if (Integer.parseInt(value.toString().trim()) % Math.abs(max) == 0) {

                            check = true;
                        } else {
                            QTYDialog("Quantity should be Multiple of " + Math.abs(max));
                            // OGtoast.OGtoast("QTY must be Multiple of "+Math.abs(min),Create_Order_Salesman.this);
                            check = false;
                            return check;
                        }
                    }else{
                        QTYDialog("Quantity should be Multiple of " + Math.abs(max));
                        check = false;
                        return check;
                    }
                }else{
                    if(value.toString().length()>0) {
                        if (Integer.parseInt(value.toString().trim()) <= Math.abs(max)) {
                            check = true;
                        } else {
                            QTYDialog("Quantity can't be greater than " + Math.abs(max));
                            // OGtoast.OGtoast("QTY must be more than "+Math.abs(min),Create_Order_Salesman.this);
                            check = false;
                            return check;
                        }
                    }else{
                        QTYDialog("Quantity can't be greater than " + Math.abs(max));
                        // OGtoast.OGtoast("QTY must be more than "+Math.abs(min),Create_Order_Salesman.this);
                        check = false;
                        return check;
                    }
                }

            } /*if(!maxQ.equalsIgnoreCase("0")) {
                int max = Integer.parseInt(maxQ);
                if(value.toString().length()>0) {
                    if (Integer.parseInt(value.toString().trim()) <= Math.abs(max)) {
                        check = true;
                    } else {
                        QTYDialog("Quantity must be less than or equals to " + Math.abs(max));
                        // OGtoast.OGtoast("QTY must be less than or equals to "+Math.abs(max),Create_Order_Salesman.this);
                        check = false;
                        return check;
                    }
                }else{
                    QTYDialog("Quantity must be less than or equals to " + Math.abs(max));
                    // OGtoast.OGtoast("QTY must be less than or equals to "+Math.abs(max),Create_Order_Salesman.this);
                    check = false;
                    return check;
                }
            }*/
        }else{
            Qty = 1;
            check=true;
        }
        return check;
    }

    /* Option Item Selected */
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            hideKeyboard();
            Clicked_cart = true;

            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setHideable(true);
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Clicked_cart = false;
                //     _autoCompleteTextView.clearFocus();
            } else {
                if (chemistCartList.size() > 0) {
                    _rv_Cartdatalist.setVisibility(View.VISIBLE);
                    fill_Cartdata();
                } else {
                    OGtoast.OGtoast("Cart is empty", this);
                }
            }
        }
// else if (item.getItemId() == R.id.action_inventory) {
//            show_inventory();
//        }
        else if (item.getItemId() == android.R.id.home) {
//            if (Flag.equals("pending")) {
//                Intent i = new Intent(this, PendingOrderCustomerList.class);
//                startActivity(i);
//            }
//            else
//            {
//                Intent i = new Intent(this, CallPlanDetails.class);
//                startActivity(i);
//            }
            globalVariable.setFromCustomerList(false);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
        //get_ProductList_json();
        //get_cart_id();

        //Log.d("Activity", "onResume");
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setHideable(true);
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            //behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        // get_ProductList_json();
    }


    /* Set Product List into AutoCompleteTextView */
    private void get_ProductList_json() {
        //comment by apurva
        posts = new ArrayList<>();
        //Log.d("searchListSize", ""+sreach_product_list.size());
        //****************************************************

        if (sreach_product_list == null || sreach_product_list.size() == 0) {
            sreach_product_list = daoSession.getStockistProductsDao().loadAll();

        }
//*******************************************************

        posts = new ArrayList<>(sreach_product_list);
        //    Log.d("postSize", ""+posts.size());
        //adpter = new ad_AutocompleteCustomArray_Salesman(this, posts, SharedPref.getLegendData(this),sales_legend_mode);
      //  adpter = new ad_AutocompleteCustomArray_Salesman(this, posts, SharedPref.getLegendData(this));
        adpter = new ad_AutocompleteCustomArray_Salesman(this, posts);
        //   _autoCompleteTextView.setThreshold(2);
        _autoCompleteTextView.setAdapter(adpter);
        //    Log.d("SizedArrList", String.valueOf(posts.size()));
    }

    /* Get Offline Data */
    private void getOfflineProductList() {
        ArrayList<StockistProducts> arrayListStockistProduct = new ArrayList<>();
        Cursor cursor = db.getSalesmanProductList();
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            Toast.makeText(this, "Products are not available", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    StockistProducts stockistProducts = new StockistProducts();
                    stockistProducts.setProduct_ID(cursor.getString(3));
                    stockistProducts.setItemcode(cursor.getString(1));
                    stockistProducts.setItemname(cursor.getString(4));
                    stockistProducts.setPacksize(cursor.getString(5));
                    stockistProducts.setMRP(cursor.getString(6));
                    stockistProducts.setRate(cursor.getString(7));
                    stockistProducts.setStock(cursor.getString(8));
                    stockistProducts.setType(cursor.getString(9));
                    stockistProducts.setMfgCode(cursor.getString(10));
                    stockistProducts.setMfgName(cursor.getString(11));
                    stockistProducts.setDoseForm(cursor.getString(12));
                    stockistProducts.setScheme(cursor.getString(13));
                    stockistProducts.setHalfScheme(cursor.getString(14));
                    stockistProducts.setPercentScheme(cursor.getString(15));
                    stockistProducts.setLegendName(cursor.getString(16));
                    stockistProducts.setLegendColor(cursor.getString(17));

                    stockistProducts.setMinQ(cursor.getString(18));
                    stockistProducts.setMaxQ(cursor.getString(19));
                    stockistProducts.setBoxSize(cursor.getString(20));

                    stockistProducts.setReplaceName(cursor.getString(4) .replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                            .replace("/", "").replace("*", "")
                            .replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").
                                    replace("^", "").replace("+", "")
                            .replace(",", "").replace("<","").
                                    replace(">","").toLowerCase());
                    arrayListStockistProduct.add(stockistProducts);

                } while (cursor.moveToNext());
            }
        }


        sreach_product_list = arrayListStockistProduct;
        //Log.d("SearchSize", ""+sreach_product_list.size());
        //Log.d("LegendData", ""+SharedPref.getLegendData(this));
        posts = new ArrayList<>(sreach_product_list);
        //  Log.d("PostSize", ""+posts.size());
        //get_legends(posts);
        //adpter = new ad_AutocompleteCustomArray_Salesman(this, posts, SharedPref.getLegendData(this));
   //      Log.i("Posts Size",""+posts.size());
        adpter = new ad_AutocompleteCustomArray_Salesman(this, posts);
        _autoCompleteTextView.setThreshold(1);
       /* adpter.sort(new Comparator<StockistProducts>() {
            @Override
            public int compare(StockistProducts lhs, StockistProducts rhs) {
                return lhs.getItemname().trim().compareTo(rhs.getItemname().trim());
            }
        });*/
        _autoCompleteTextView.setAdapter(adpter);

        Utility.hideSyncDialog();
    }


    /* Filter From Search List */
    public void filter(String charText) {
      /*  if(arraylist.size()<1) {
            this.arraylist.addAll(alternativeitems);
        }*/
        sreach_product_list.clear();
        if (charText.length() == 0) {
            // alternativeitems.addAll(arraylist);
        } else {
            for (StockistProducts wp : posts) {
                if (wp.getItemname().toUpperCase().contains(charText.toUpperCase())) {
                    sreach_product_list.add(wp);
                } else if (wp.getItemname().contains(charText)) {
                    sreach_product_list.add(wp);
                }
            }
        }

    }


    private void get_CartData() {
        Cursor crs_cart = db.get_saleman_cart_data(Doc_Id);
        String previousCartData = ConstData.get_jsonArray_from_cursor(crs_cart).toString();
        //    Log.e("previousCartData", previousCartData);// pref.getString("CartList", null);
        if (!previousCartData.isEmpty() || crs_cart.getCount() > 0) {
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            /*cart = new ArrayList<m_CartData>();
            cart = Arrays.asList(mGson.fromJson(previousCartData, m_CartData[].class));
*/
            if (chemistCartList.size() > 0) {
                fill_Cartdata();
            } else {
                OGtoast.OGtoast("Cart is empty", this);
            }
        } else {
            OGtoast.OGtoast("Cart is empty", this);
        }
    }


    /* Show Cart Data */
    private void fill_Cartdata() {
        cartAdapter = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(Create_Order_Salesman.this);
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.adpter_cartdata_list_new, parent, false);
                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                final ChemistCart chemistCart = chemistCartList.get(position);
                // Log.e("Cart_list", Cart_list.toString());
                holder.getBinding().setVariable(BR.v_cartDatalist, chemistCart);
                holder.getBinding().executePendingBindings();
                //int POS = holder.getAdapterPosition();
                /* Quantity Click */
                holder.getBinding().getRoot().findViewById(R.id.txt_edit_qty).setOnClickListener(new View.OnClickListener() {

                    // holder.getBinding().getRoot().findViewById(R.id.linear_quantity_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        editQuantityDialog(chemistCartList.get(position).getStockist_Client_id(), chemistCartList.get(position).getProduct_ID(),
//                                chemistCartList.get(position).getItemname(), chemistCartList.get(position).getRate(),
//                                chemistCartList.get(position).getQty(),chemistCartList.get(position).getScheme());
                       /*   _rv_Cartdatalist.setVisibility(View.GONE);
                          if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                              // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                              behavior.setHideable(true);
                              behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                          }*/

                        editQuantityDialog(chemistCart.getDOC_ID(), chemistCart.getStockist_Client_id(), chemistCart.getProduct_ID(), chemistCart.getItemname(), chemistCart.getRate(), chemistCart.getQty(), chemistCart.getScheme(),chemistCart.getMinQ(),chemistCart.getMaxQ());
                    }
                });

                /* Delete Order Click */
                holder.getBinding().getRoot().findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   Log.e("position",getAd);
                        // int pos = _rv_Cartdatalist.getAdapter().getItemId(position);
                        delete_product_from_cart(chemistCartList.get(position));
                        _autoCompleteTextView.setText("");
                        _Qty.setText("");
                        stock.setBackgroundColor(Color.WHITE);
                        _autoCompleteTextView.requestFocus();
                    }
                });


                /* ItemView Click */
                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //      Log.e("Click", "Item click");


                        /*  _rv_Cartdatalist.setVisibility(View.GONE);

                         *//*_Qty.setText(chemistCartList.get(position).getQty());
                        _Qty.requestFocus();*//*
                         if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                           // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            behavior.setHideable(true);
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }*/
                        editQuantityDialog(chemistCart.getDOC_ID(), chemistCart.getStockist_Client_id(), chemistCart.getProduct_ID(), chemistCart.getItemname(), chemistCart.getRate(), chemistCart.getQty(), chemistCart.getScheme(),chemistCart.getMinQ(),chemistCart.getMaxQ());

                    }
                });
            }

            @Override
            public int getItemCount() {
                return chemistCartList.size();
            }
        };

        _rv_Cartdatalist.setLayoutManager(new LinearLayoutManager(this));
        _rv_Cartdatalist.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        _rv_Cartdatalist.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //At this point the layout is complete and the
                //dimensions of recyclerView and any child views are known.
                if (Clicked_cart) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //  Clicked_cart = true;
                    Clicked_cart = false;
                }
            }
        });
    }

    /* Edit Quantity Cart Dialog */
    private void editQuantityDialog(final String docId, final String stockistClientId, final String productId, String item, final String ptr, final String quantity, final String Scheme,final String minQ,final String maxQ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_quantity_dialog, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_close_dialog);
        TextView textViewItem = (TextView) view.findViewById(R.id.txt_item_name_dialog);
        TextView textViewPtr = (TextView) view.findViewById(R.id.txt_item_ptr_dialog);
        Button buttonMinus = (Button) view.findViewById(R.id.btn_minus_dialog);
        final EditText editTextQuantity = (EditText) view.findViewById(R.id.edt_quantity_dialog);
        Button buttonPlus = (Button) view.findViewById(R.id.btn_plus_dialog);
        Button buttonDone = (Button) view.findViewById(R.id.btn_done_dialog);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        textViewItem.setText("Item: " + item);
        textViewPtr.setText("PTR: " + ptr);
        editTextQuantity.setText(quantity);
        editedQuantity = Integer.valueOf(editTextQuantity.getText().toString());

        editTextQuantity.setSelection(editTextQuantity.getText().length());

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //  requestShowKeyboardShortcuts();
        //  }
        showKeyBoard();
        editTextQuantity.requestFocus();

        /* Minus Click */
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editedQuantity > 1) {
                    editedQuantity = Integer.valueOf(editTextQuantity.getText().toString()) - 1;
                    editTextQuantity.setText(String.valueOf(editedQuantity));
                } else {
                    Toast.makeText(Create_Order_Salesman.this, "Min. quantity should be 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* Plus Click */
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedQuantity = Integer.valueOf(editTextQuantity.getText().toString()) + 1;
                editTextQuantity.setText(String.valueOf(editedQuantity));
            }
        });

        /* Done Click */
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoard();
                if (editTextQuantity.getText().toString().equalsIgnoreCase("") || editTextQuantity.getText().toString().isEmpty() || editTextQuantity.getText().toString().equalsIgnoreCase(null)) {
                    Toast.makeText(Create_Order_Salesman.this, "Min. quantity should be one", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if(QTYCheck(editTextQuantity.getText().toString(),minQ,maxQ)){
                        try {
                            int quantity = 0;
                            //int quantity = 0;
                            Float price = null;
                            if (editTextQuantity.getText().toString().equalsIgnoreCase("") || editTextQuantity.getText().toString().isEmpty() || editTextQuantity.getText().toString().equalsIgnoreCase(null)) {
                                Toast.makeText(Create_Order_Salesman.this, "Min. quantity should be one", Toast.LENGTH_SHORT).show();
                            } else {
                                quantity = Integer.valueOf(editTextQuantity.getText().toString());
                                price = Float.valueOf(ptr) * quantity;
                            }

                            ChemistCart chemistCart = new ChemistCart();

                            chemistCart.setDOC_ID(Doc_Id);
                            //  chemistCart.setItems(Itemcode);
                            //  chemistCart.setItemname(Itemname);
                            chemistCart.setProduct_ID(productId);
                            chemistCart.setQty(String.valueOf(quantity));
                            // chemistCart.setUOM(UOM);
                            chemistCart.setRate(String.valueOf(ptr));
                            // chemistCart.setRate(String.valueOf(vRate));
                            // chemistCart.setRate(vRate.toString());
                            chemistCart.setPrice(String.valueOf(price));
                            //  chemistCart.setMRP(String.valueOf(vMrp));
                            chemistCart.setCreatedon(dateFormat.format(Calendar.getInstance().getTime()));
                            chemistCart.setSalesman(true);
                            chemistCart.setStockist_Client_id(stockistClientId);
                            chemistCart.setRemarks("cart");
                            chemistCart.setOrder_sync(false);
                            //  chemistCart.setSalesman(true);
                            chemistCart.setAmount(String.valueOf(price));
                            // chemistCart.setPACK(Pack);
                            // chemistCart.setScheme(Scheme);
                            chemistCartDao.insertOrUpdateCart(chemistCart, true);
                            countTotalOrderAmount(chemistCartDao.getCartItem(call_plan_customer_id, true));
//                    orderAmount = 0;
//                    for (int i = 0; i < chemistCartList.size(); i++) {
//                        orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
//                    }
//                    _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmount));
//                 //   Log.d("addcartedit",chemistCart.getMRP()+"sch"+chemistCart.getScheme()+"sale"+chemistCart.getSalesman()+"price"+chemistCart.getPrice()+"uom"+chemistCart.getUOM());
//                    cartAdapter.notifyDataSetChanged();
//
                            alertDialog.dismiss();
                            // cart

                        } catch (Exception e) {

                            //   Log.d("prntException",e.getMessage());

                        }
                    }else{
                        return;
                    }

                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hideKeyboard();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    //    private void countTotalOrderAmount(List<ChemistCart> cartItem) {
//    }
    private void countTotalOrderAmount(List<ChemistCart> list) {
        orderAmount = 0;
        for (int i = 0; i < list.size(); i++) {
            orderAmount += Double.valueOf(list.get(i).getAmount());
        }
        _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmount));

        cartAdapter.notifyDataSetChanged();
    }

    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    /* Hide Soft Keyboard */
//    private void hideKeyboard1() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(InputMethodManager.getWindowToken(), 0);
//    }


    /* Cart Data */
    void get_cart_id() {
        if (call_plan_customer_id != null) {
            Cursor crs_cart = db.get_chemist_cart(call_plan_customer_id);
            if (crs_cart != null && crs_cart.getCount() > 0) {
                while (crs_cart.moveToNext()) {
                    Doc_Id = crs_cart.getString(crs_cart.getColumnIndex("DOC_NO"));
                    Cart_Id_available = true;
                    n_product_cart_count = (db.get_total_order_item_count(Doc_Id));
                    //   Log.e("Count11", n_product_cart_count.toString());
                    _OrderAmt.setText(" Rs." + crs_cart.getString(crs_cart.getColumnIndex("Amount")));

                    if (n_product_cart_count != 0) {
                        if (mToolbarMenu != null) {
                            createCartBadge(n_product_cart_count);
                        }
                    }
                }
            }

            if (Doc_Id == null) {

                Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + gen();
                // Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
                Cart_Id_available = false;
            }
        }
    }


    /* Order Placed */
    private void confirm_order(String Comment) {
        try {
            JSONArray main_j_array = new JSONArray();
            JSONObject main_j_object = new JSONObject();

            JSONArray line_j_array = new JSONArray();
            JSONArray productarray = new JSONArray();

            // main_j_object.put("Transaction_No","");
            // main_j_object.put("CreatedBy", Integer.parseInt(Client_id));
            main_j_object.put("CreatedBy", pref.getString(USER_ID, ""));

            // Cursor crs_cart = db.get_chemist_cart(call_plan_customer_id);

            chemistCartList = chemistCartDao.getCartItem(call_plan_customer_id, true);
            ChemistCart chemistCart = chemistCartList.get(0);

            if (chemistCartList.size() > 0) {
                main_j_object.put("Client_ID", Integer.parseInt(chemistCart.getStockist_Client_id()));
                main_j_object.put("DOC_NO", chemistCart.getDOC_ID());
                Log.i("Doc_Date", "DocDate" + chemistCart.getCreatedon());
                main_j_object.put("Doc_Date", chemistCart.getCreatedon());
                //main_j_object.put("Stockist_Client_id",Client_id+"  "+crs_cart.getString(crs_cart.getColumnIndex("Stockist_Client_id")));
                main_j_object.put("Stockist_Client_id", Integer.parseInt(Client_id));
                //    Log.d("stockisttID", client_id);
                main_j_object.put("Remarks", chemistCart.getRemarks());
                main_j_object.put("Items", chemistCartList.size());
                main_j_object.put("Amount", orderAmount);
                main_j_object.put("Status", 0);
                main_j_object.put("Createdon", chemistCart.getCreatedon());
                main_j_object.put("Comments", Comment);
                // main_j_object.put("LoginName",pref.getString(CLIENT_LOGIN_NAME,""));
                //main_j_object.put("USER_ID",pref.getString(USER_ID,""));

                //   Log.d("asdfdff", String.valueOf(Integer.parseInt(crs_cart.getString(crs_cart.getColumnIndex("status")))));
                Doc_Id = chemistCart.getDOC_ID();
                Cart_Id_available = true;

                //    }
                // Cursor crs_cart_details = db.get_chemist_cart_detail(Doc_Id);

                // if (crs_cart_details != null && crs_cart_details.getCount() > 0) {

                for (int i = 0; i < chemistCartList.size(); i++) {
                    chemistCart = chemistCartList.get(i);
                    JSONObject line_j_object = new JSONObject();
                    line_j_object.put("Transaction_No", "");
                    //line_j_object.put("Doc_item_No", crs_cart_details.getString(crs_cart_details.getColumnIndex("Doc_item_No")));
                    line_j_object.put("Doc_item_No", i + 1);
                    line_j_object.put("Product_ID", Integer.parseInt(chemistCart.getProduct_ID()));
                    line_j_object.put("Qty", Integer.parseInt(chemistCart.getQty()));
                    line_j_object.put("UOM", chemistCart.getUOM());
                    line_j_object.put("Rate", Double.valueOf(chemistCart.getRate()));
                    line_j_object.put("Price", Double.valueOf(chemistCart.getPrice()));

                    if (Utility.internetCheck(this)) {

                    } else {
                        line_j_object.put("itemName", chemistCart.getItemname());
                        line_j_object.put("packSize", chemistCart.getPACK());
                    }
                    //  String.format("%.2f", crs_cart_details.getDouble(crs_cart_details.getColumnIndex("Price")));
                    line_j_object.put("MRP", Float.valueOf(chemistCart.getMRP()));
                    line_j_object.put("Createdon", chemistCart.getCreatedon());
                    line_j_object.put("CreatedBy", Integer.parseInt(call_plan_customer_id));
                    line_j_array.put(line_j_object);
                    productarray.put(Integer.parseInt(chemistCart.getProduct_ID()));
                    //   Log.e("line_j_object", line_j_object.toString());
                }

                //  }

                main_j_array.put(main_j_object);
                main_j_array.put(line_j_array);
                main_j_array.put(productarray);

                Log.d("orderPlaceData11", String.valueOf(main_j_array));
                /*Log.d("orderPlaceData", String.valueOf(main_j_array));
                Log.d("orderCustomerId", call_plan_customer_id);
                Log.d("orderDocId", Doc_Id);*/

                MasterPlacedOrder masterPlacedOrder = new MasterPlacedOrder();
                masterPlacedOrder.setJson(main_j_array.toString());
                masterPlacedOrder.setCustomerID(call_plan_customer_id);
                masterPlacedOrder.setDoc_ID(Doc_Id);

                long confirm = daoSession.getMasterPlacedOrderDao().insert(masterPlacedOrder);

                if (confirm > 0) {
                    chemistCartDao.deleteInTx(chemistCartList);
                    adpter.notifyDataSetChanged();
                    Intent download_intent = new Intent(RefreshData.ACTION_CONFIRM_ORDER, null, this, RefreshData.class);
                    startService(download_intent);
                }

                if (Utility.internetCheck(this)) {

                } else {
                    /* For Insert Offline Orders */
                    db.insertOrder(User_id, User_id, Client_id, call_plan_customer_id, chemistName, Doc_Id,
                            chemistCart.getCreatedon(), chemistCart.getRemarks(), String.valueOf(chemistCartList.size()),
                            String.valueOf(orderAmount), "0", chemistCart.getCreatedon(), "", line_j_array.toString(),
                            String.valueOf(productarray));
                }
                //db.insert_into_chemist_order_sync(Doc_Id, main_j_array.toString(), 0);
                // db.delete_chemist_Cart(Doc_Id);
                // db.delete_chemist_Cart_Details(Doc_Id);
                order_confirmed_dialog(Doc_Id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void get_product_data_on_stockist() {
        _txt_customer_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
        client_id = pref.getString(CLIENT_ID, "");
        call_plan_customer_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);

        if (db.check_stockist_data(call_plan_customer_id) > 0) {


        } else {
            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_PRODUCT_DATA,
                    AppConfig.GET_CHEMIST_PRODUCT_DATA + "[" + client_id + "," + call_plan_customer_id + "]", this, false);

        }
    }


    /* Interface Methods */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        if (response != null) {

            if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                try {
                    String status = response.getString("Status");
                    Log.d("location_order115", response.toString());
                    if (status.equalsIgnoreCase("success")) {
                        //     Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        //      Toast.makeText(getBaseContext(), "Location not Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        try {
            if (response != null) {
                //   Log.e("Inventory",response.toString());
                /*if (f_name.equals(AppConfig.GET_STOCKIST_INVENTORY)) {
                    new LoadData(response).execute();
                    //Log.d("listResp", response.toString());
                }*/
                if (f_name.equals(AppConfig.POST_GET_STOCKIST_LEGENDS)) {
                    //Log.e("Inventory11", response.toString());
                    legend_data = response.toString();
                    //  Log.e("legendData", legend_data);
                    SharedPref.saveLegendData(this, legend_data);
                    //****check
                    get_stockist_inventory(Client_id);
                    //comment by apurva
                    get_cart_id();
                    //Log.d("LoadData", "else");
                }

                //****check
                get_stockist_inventory(Client_id);
            }
        } catch (Exception e) {
            //   Log.e("Message",e.getMessage());

        }
    }


    /* Confirm Order Dialog */
    void order_confirmed_dialog(String doc_number) {
        new AlertDialog.Builder(Create_Order_Salesman.this)
                .setTitle("Order")
                .setCancelable(false)
                .setMessage("Order placed successfully.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        get_location();
                        // new get_current_location(Create_Order_Salesman.this);
                        Bundle bundle = new Bundle();
                        bundle.putString("Order", "Placed");
                        if (resultReceiver != null) {
                            resultReceiver.send(200, bundle);
                        }
                        finish();
                    }
                }).show();
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


    /* Load Data Service */
    public class LoadData extends AsyncTask<Void, Void, Void> {

        JSONArray response;

        public LoadData(JSONArray response) {
            this.response = response;
        }

        //declare other objects as per your need
        @Override
        protected void onPreExecute() {
            // progressDialog = ProgressDialog.show(Create_Order.this, "Progress Dialog Title Text", "Process Description Text", true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            insert_inventory_products(response);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            // progress.setVisibility(View.GONE);
            // get_ProductList_json(response);
            get_ProductList_json();
        }
    }


    /* onRestart */
    @Override
    protected void onRestart() {
        // Log.d("Activity", "Restart");

       // chemistCartList = chemistCartDao.loadAll();
        //   int previouscartValue = n_product_cart_count;

        //  n_product_cart_count = chemistCartList.size();
        if (n_product_cart_count < (n_product_cart_count = chemistCartList.size())) {
            ChemistCart chemistCart = chemistCartList.get(n_product_cart_count - 1);
           // createCartBadge(n_product_cart_count);
            orderAmount = 0;
            for (int i = 0; i < chemistCartList.size(); i++) {

                orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
            }
            _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmount));
        } else {
            orderAmount = 0;

            for (int i = 0; i < chemistCartList.size(); i++) {

                orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
            }
            _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmount));
        }


        super.onRestart();
    }

    private void createCartBadge(int paramInt) {
        if (Build.VERSION.SDK_INT <= 15) {
            return;
        }
        if (mToolbarMenu != null) {
            MenuItem cartItem = this.mToolbarMenu.findItem(R.id.action_cart);

            if (cartItem != null) {
                LayerDrawable localLayerDrawable = (LayerDrawable) cartItem.getIcon();
                Drawable cartBadgeDrawable = localLayerDrawable
                        .findDrawableByLayerId(R.id.ic_badge);

                BadgeDrawable badgeDrawable;
                if ((cartBadgeDrawable != null)
                        && ((cartBadgeDrawable instanceof BadgeDrawable))
                        && (paramInt < 10)) {
                    badgeDrawable = (BadgeDrawable) cartBadgeDrawable;
                } else {
                    badgeDrawable = new BadgeDrawable(this);
                }
                //     Log.e("Count", String.valueOf(paramInt));
                badgeDrawable.setCount(paramInt);
                localLayerDrawable.mutate();
                localLayerDrawable.setDrawableByLayerId(R.id.ic_badge, badgeDrawable);
                cartItem.setIcon(localLayerDrawable);
            }
        }
    }


    void cancel_order(final View v) {


        if (chemistCartList.size() > 0) {

            new AlertDialog.Builder(Create_Order_Salesman.this)
                    .setTitle("Order")
                    .setMessage("Cancel Order")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            chemistCartDao.deleteCartItem(call_plan_customer_id);
                           // chemistCartDao.deleteInTx(chemistCartList);  by Vishal
//                            Snackbar mySnackbar = Snackbar.make(v, "Products removed from cart", Snackbar.LENGTH_SHORT);
////                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
////                            mySnackbar.show();
                            Log.d("cancelpress", "yes cancel");
//                            Intent i = new Intent(getApplicationContext(), CallPlanDetails.class);
//                            startActivity(i);
                            finish();
                            behavior.setHideable(true);
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        } else {
            OGtoast.OGtoast("Cart is empty", Create_Order_Salesman.this);
        }
    }

    public Intent getSupportParentActivityIntent() {
        Intent newIntent = null;
        try {
            if (globalVariable.getCall_plan_Started()) {
                globalVariable.setFromCustomerList(false);
                finish();
            } else {
                globalVariable.setFromCustomerList(false);
                newIntent = new Intent(Create_Order_Salesman.this, MainActivity.class);
            }
            //you need to define the class with package name

            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIntent;
    }


    /* onBack Pressed */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // finish();
        Intent newIntent = null;
        try {
            //  Log.d("Activity","Back to home");
            if (globalVariable.getCall_plan_Started()) {
                globalVariable.setFromCustomerList(false);
                finish();
                // newIntent = new Intent();
            } else if (Flag.equals("pending")) {
                newIntent = new Intent(Create_Order_Salesman.this, PendingOrderCustomerList.class);
                globalVariable.setFromCustomerList(false);
            } else {
                newIntent = new Intent(Create_Order_Salesman.this, MainActivity.class);
                globalVariable.setFromCustomerList(false);
            }
            //you need to define the class with package name
            //  daoSession.getStockistProductsDao().deleteAll();
            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Insert Product list into SQLite */
    void insert_inventory_products(JSONArray response) {

       /* String jsondata = response.toString();
        GsonBuilder builder = new GsonBuilder();
        Gson mGson = builder.create();
        Type listType = new TypeToken<List<StockistProducts>>() {
        }.getType();
        posts = mGson.fromJson(jsondata, listType);*/

        ArrayList<StockistProducts> posts = new ArrayList<>();

        JSONObject j_obj;
        StockistProducts stockistProducts;
        db.deleteRecordFromSalesmanProduct();
        try {
            for (int i = 0; i < response.length(); i++) {
                j_obj = response.getJSONObject(i);
                stockistProducts = new StockistProducts();
                stockistProducts.setProduct_ID(j_obj.getString("Product_ID"));
                stockistProducts.setItemcode(j_obj.getString("Itemcode"));
                stockistProducts.setItemname(j_obj.getString("Itemname"));
                stockistProducts.setPacksize(j_obj.getString("Packsize"));
                stockistProducts.setMRP(j_obj.getJSONArray("line_items").getJSONObject(0).getString("Mrp"));
                stockistProducts.setRate(j_obj.getJSONArray("line_items").getJSONObject(0).getString("Rate"));
                stockistProducts.setStock(j_obj.getString("Stock"));
                stockistProducts.setMfgCode(j_obj.getJSONArray("line_items").getJSONObject(0).getString("MfgCode"));
                stockistProducts.setMfgName(j_obj.getJSONArray("line_items").getJSONObject(0).getString("MfgName"));
                stockistProducts.setDoseForm(j_obj.getString("DoseForm"));
                stockistProducts.setScheme(j_obj.getString("Scheme"));
                stockistProducts.setHalfScheme(j_obj.getString("HalfScheme"));
                stockistProducts.setPercentScheme(j_obj.getString("PercentScheme"));

                //  add flag  to show scheme if set
                //  stockistProducts.setSalesman(j_obj.getString("salesman"));
                posts.add(stockistProducts);
                //  Log.d("ResponseCheck",posts.toString());



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.e("Post",posts.toString());
        daoSession.getStockistProductsDao().deleteAll();
        daoSession.getStockistProductsDao().insertInTx(posts, true);
        //change by apurva
        sreach_product_list = posts;
        //  Log.e("Responsee",response.toString());
    }


    /* Stock Items List */
    private void get_stockist_inventory(String Client_id) {
        //Log.d("cliendid", client_id);
        //progress.setVisibility(View.VISIBLE);
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INVENTORY,
                AppConfig.GET_STOCKIST_INVENTORY + Client_id, this, false);
    }


    void show_inventory() {
        Intent i = new Intent(Create_Order_Salesman.this, InventorylistOfflineActivity.class);
        //Intent i = new Intent(Create_Order_Salesman.this, InventorylistActivity.class);
        i.putExtra("client_id", call_plan_customer_id);
        i.putExtra("order_amount", orderAmount);
        startActivity(i);
    }


    /* Delete Cart Product */
    void delete_product_from_cart(ChemistCart chemistCart) {
        chemistCartDao.delete(chemistCart);
        chemistCartList.remove(chemistCart);
        int item_count = chemistCartList.size();
        float orderAmounts = .2f;

        for (ChemistCart cart : chemistCartList) {
            orderAmounts += Float.valueOf(cart.getAmount());
        }
        orderAmount = orderAmounts;
        /*Log.e("itme_no", itme_no);
        db.delete_product_from_cart_chemist_Cart_Details(createdOn);

        price = db.get_total_order_amount(Doc_Id) - price;*/
        // Integer item_count = (db.get_total_order_item_count(Doc_Id));
        // _OrderAmt.setText(" Rs." + String.valueOf(orderAmounts));
        _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmounts));
        // db.update_into_chemist_cart(Doc_Id, item_count, price.toString());

        if (chemistCartList.size() == 0) {

            Cart_Id_available = false;
            _OrderAmt.setText("");
            orderAmount = 0;
        }

        if (mToolbarMenu != null) {
            createCartBadge(item_count);
        }

        OGtoast.OGtoast("Product removed successfully", Create_Order_Salesman.this);

//        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
        cartAdapter.notifyDataSetChanged();
        // adpter.notifyDataSetChanged();
    }


    /* Unused Method */
    void delete_product_from_cart(String itme_no, Float price) {
        //  Log.e("itme_no", itme_no);
        db.delete_product_from_cart_chemist_Cart_Details(Doc_Id, itme_no);

        price = db.get_total_order_amount(Doc_Id) - price;
        Integer item_count = (db.get_total_order_item_count(Doc_Id)) - 1;
        _OrderAmt.setText(" Rs." + price.toString());
        db.update_into_chemist_cart(Doc_Id, item_count, price.toString());

        if (item_count == 0) {
            db.delete_chemist_Cart(Doc_Id);
        }

        if (mToolbarMenu != null) {
            createCartBadge(item_count);
        }

        OGtoast.OGtoast("Product removed successfully", Create_Order_Salesman.this);

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setHideable(true);
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    /* Stock List API Data */
    void get_stockist_legends() {
        if (Utility.internetCheck(this)) {
            try {
                JSONArray j_arr = new JSONArray();
                j_arr.put(client_id);

                //Log.d("printjarray", String.valueOf(j_arr));
                //progressDialog = ProgressDialog.show(this, "Please Wait", "Loading Products.... ", true);
                MakeWebRequest.MakeWebRequest("out_array", AppConfig.POST_GET_STOCKIST_LEGENDS, AppConfig.POST_GET_STOCKIST_LEGENDS,
                        j_arr, this, false, "");
            } catch (Exception e) {

            }
        } else {
            getOfflineProductList();
        }
    }


    /* onDestroy */
    @Override
    protected void onDestroy() {
        // Log.d("Activity", "onDestroy");
        //  daoSession.getStockistProductsDao().deleteAll();
        super.onDestroy();
    }


    private void get_location() {

           sendData();

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
                    boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if (cameraPermission) {
                        get_location();
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
        new AlertDialog.Builder(Create_Order_Salesman.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
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
        Log.e("onLocationChanged", "" + location.getLatitude() + " " + location.getLongitude());

        if(location!=null){
            Latitude = location.getLatitude();
            Longitude =location.getLongitude();
            address = LocationAddress.getAddressFromLocation(Latitude, Longitude, this);
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
          }
        //sendData(location);



    }

    public void sendData() {
        final JSONObject jsonParams;


            String adress = LocationAddress.getAddressFromLocation(Latitude, Longitude, this);

            jsonParams = new JSONObject();

            try {
                SharedPreferences pref = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                String uID = pref.getString("UUID", "0");
                Log.d("location_order111", uID);
                String trasaction_No = pref.getString("Transaction_No", "");
                // Log.d("order_salesman10",trasaction_No);
                // jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                //jsonParams.put("UserID", Selected_chemist_id);
                jsonParams.put("Latitude", String.valueOf(Latitude));
                jsonParams.put("Longitude", String.valueOf(Longitude));
                jsonParams.put("CurrentLocation", adress);
                jsonParams.put("UserID", User_id);
                jsonParams.put("CustID", call_plan_customer_id);
                jsonParams.put("task", "Order");
                jsonParams.put("Tran_No", trasaction_No);
                jsonParams.put("unqid", uID);




                try {
                    RequestQueue queue = Volley.newRequestQueue(this);
                    VolleyLog.DEBUG = true;
                    JsonObjectRequest jsObjRequest_post = new JsonObjectRequest(
                            //Method.POST,
                            AppConfig.POST_UPDATE_CURRENT_LOCATION,
                            jsonParams,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("OrderonResponse", response.toString());
                                    //hidePDialog();

                                    db.deleteLocationDatabyCustomerId(Doc_Id);


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.d("Volley", "error:f_name " + error.getMessage());
                                    if (error instanceof TimeoutError) {

                                    } else if (error instanceof ServerError) {

                                    }
                                    // hidePDialog();

                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            if (globalVariable.getToken() != null) {
                                headers.put("Authorization", "Bearer " + globalVariable.getToken());
                            }

                            return headers;
                        }

                    };


                    jsObjRequest_post.setRetryPolicy(new DefaultRetryPolicy(
                            15000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(jsObjRequest_post);


                } catch (Exception e) {
                    Log.e("SendData", e.toString());
                }


                // USE THIS API
                // MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CHEMIST_LOCATION, AppConfig.POST_UPDATE_CHEMIST_LOCATION, jsonParams, this, false);

            } catch (Exception e) {
                Log.e("OnLocationChange", e.toString());
            }


    }

}
