package com.synergy.keimed_ordergenie.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.adapter.ad_AutocompleteCustomArray_chemist;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.app.Config;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.database.MasterPlacedOrder;
import com.synergy.keimed_ordergenie.database.StockistProducts;
import com.synergy.keimed_ordergenie.model.m_CartData;
import com.synergy.keimed_ordergenie.model.m_product_list_chemist;
import com.synergy.keimed_ordergenie.utils.BadgeDrawable;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.CustomAutoCompleteView;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.RefreshData;
import com.synergy.keimed_ordergenie.utils.Utility;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_CHEMIST_PRODUCT_DATA;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_LAST_DATA_SYNC;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class Order_Search extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess,SearchView.OnQueryTextListener{
    private boolean isAvailable=true;
    String newTextLength,sub_id;
    boolean doubleBackToExitPressedOnce = false;
    List<m_product_list_chemist> posts = new ArrayList<m_product_list_chemist>();
    //List<m_product_list_chemist> sreach_product_list = new ArrayList<m_product_list_chemist>();
    List<StockistProducts> new_sreach_product_list = new ArrayList<StockistProducts>();
    private SharedPreferences pref;
    private TextView txt_cust_name,txt_order_id,txt_total_items,txt_total,txt_comment,txt_deliveryoption,PTR,MRP,Dosefrom,Packsize,mfg,Scheme,Halfscheme,Percentscheme;
    //TextView
    private String minQ,maxQ;
    private UUID mId;
    private SQLiteHandler db;
    Button Search_History;
    Boolean Clicked_cart = false;
    String delivery_option,hs;
    private static String ProductName;
    ProgressDialog progressDialog;
    AlertDialog alertDialog1;
    CharSequence[] values = {" PickUp "," Delivery "};
    Boolean isEditable=false;
    String legend_mode,legendName;
    public static final String CHEMIST_STOCKIST_ID = "chemist_stockist_id";
    public static final String CHEMIST_STOCKIST_NAME = "chemist_stockist_name";

    List<m_CartData> cart;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private LinearLayout linearLayout1,linearLayout2;
    SearchView.SearchAutoComplete searchAutoComplete;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    SimpleDateFormat dateFormat_sync = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private Snackbar snackbar;
    private BottomSheetBehavior behavior;
    private String Doc_Id, Stockist_id, client_id,user_id,
            minOrderValue;
    private int minOrderCheck;

    private Boolean Cart_Id_available = false;
    private Menu mToolbarMenu;
    private Integer n_product_cart_count;
    AppController globalVariable;
    private String legend_data;
    // @BindView(R.id.autoCompleteTextView)
    CustomAutoCompleteView _autoCompleteTextView;
    private EditText editText;

    ad_AutocompleteCustomArray_chemist adpter;
    private ChemistCartDao chemistCartDao;
    private TextView  txt_total_itemss,txt_order_value;
    private TextView txt_pending_countt,txt_pending_amount;


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

    @BindView(R.id.stock)
    TextView stock;

    @BindView(R.id.progress)
    ProgressBar progress;

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

    @BindView(R.id.layout9)
    LinearLayout layout9;

    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @OnClick(R.id.fab)
    void onclickfab(View view) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
          /*  behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            _rv_Cartdatalist.removeAllViewsInLayout();*/
            behavior.setHideable(true);
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        } else {
            //  get_CartData();

            // _fab.setVisibility(View.GONE);
        }
    }


    int Qty = 1;
    Integer type;
    String vPack,Itemname, Pack, MfgName, DoseForm, Sche, Remark, Itemcode, MfgCode, ProductId, UOM;
    Float vMrp, vRate;
    DaoSession daoSession;
    String login_type;

    private List<ChemistCart> chemistCartList;
    float orderAmount;
    double completeOrderAmount;
    Integer oldlength=0;

    /* Stockist Name */
    private String stockistName;
    private String accessKey;

    /* Edit Quantity Cart */
    private RecyclerView.Adapter<BindingViewHolder> cartAdapter;
    private int editedQuantity = 0;

    /* Current Date */
    private String currentDate;
    // TextView enter_text_char;



    RelativeLayout relativeLayout;
    String dummyjson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_search_chemist);
        //ActiveAndroid.initialize(configurationBuilder.create());

        ButterKnife.bind(this);
        db = new SQLiteHandler(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        globalVariable = (AppController) getApplicationContext();

        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        editText=(EditText)findViewById(R.id.edit_comment);
        // enter_text_char = (TextView) findViewById(R.id.enter_text_char);

        relativeLayout = (RelativeLayout) findViewById(R.id.rl_search);
        txt_pending_amount=(TextView)findViewById(R.id.pending_count);
        String amount=getIntent().getStringExtra("pending_amount");
        String count=getIntent().getStringExtra("pending_count");
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(_rv_Cartdatalist.getContext(),
                DividerItemDecoration.VERTICAL);
        _rv_Cartdatalist.addItemDecoration(dividerItemDecoration);

        _autoCompleteTextView = (CustomAutoCompleteView) findViewById(R.id.autoCompleteTextView);
        _autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if(_rv_Cartdatalist.getVisibility()==View.VISIBLE){
                _rv_Cartdatalist.setVisibility(View.GONE);
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    _rv_Cartdatalist.removeAllViewsInLayout();

                }
            }
        });
        // _autoCompleteTextView.setThreshold(1);
        // searchAutoComplete = (SearchView.SearchAutoComplete)_autoCompleteTextView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        _autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _rv_Cartdatalist.setVisibility(View.GONE);
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    _rv_Cartdatalist.removeAllViewsInLayout();

                }
            }
        });
        mfg = (TextView) findViewById(R.id.mfg);
        //final TextView mfg=(TextView)mContext.getApplicationInfo().findViewById(R.id.mfg);

        Dosefrom = (TextView) findViewById(R.id.Doseform);
        Packsize = (TextView) findViewById(R.id.Packsize);

        PTR = (TextView) findViewById(R.id.PTR);
        MRP = (TextView) findViewById(R.id.MRP);
        Scheme = (TextView) findViewById(R.id.Scheme);
        Halfscheme = (TextView) findViewById(R.id.txt_half_scheme);
        Percentscheme = (TextView) findViewById(R.id.txt_percentage);
        Button Search_History = (Button) findViewById(R.id.search_history);

        getLoadproducts();

        _txt_customer_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));

        behavior = BottomSheetBehavior.from(_bottumLayout);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    Log.e("newState","STATE_COLLAPSED");
                }else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    Log.e("newState","STATE_EXPANDED");
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.e("slideOffset",""+slideOffset);
                if(slideOffset==0.0){
                    _rv_Cartdatalist.removeAllViews();
                }
            }
        });
        //   final LinearLayout layout = (LinearLayout) findViewById(R.id.YOUR_VIEW_ID);
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

                //  Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    layout9.setVisibility(View.GONE);
                }
                else {
                    // keyboard is closed
                    layout9.setVisibility(View.VISIBLE);
                }
            }
        });
        init();

        //_autoCompleteTextView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        _autoCompleteTextView.setFocusable(true);


        // _autoCompleteTextView.setIconified(false);
        //  _autoCompleteTextView.setQueryHint("Search a medicine(minimum 3 character)");

        relativeLayout.setFocusableInTouchMode(true);
       /* _autoCompleteTextView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//        if(adpter!=null){
//            posts.clear();
//            adpter.notifyDataSetChanged();
//        }
//
//        if (query.length() >= 1) {
//            getLoadproducts(query);
//        } else {
//            String abc = "a";
//            getLoadproducts(abc);
//
//        }

                //jyott 19 june

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//        if(adpter!=null){
//            posts.clear();
//            adpter.notifyDataSetChanged();
//        }
//
//        return false;
//***jyott 19 june
                if(adpter!=null){
                    posts.clear();
                    // adpter.clear();
                    adpter.notifyDataSetChanged();
                }

        *//*Log.d("length1", String.valueOf(newText.length()));
        newTextLength = String.valueOf(newText.length());
        Log.d("length2", newTextLength);*//*
                //  if (newText.length() >= 2) {
                *//*-------------------------------*//*

         *//*if(newText.length()>=3){
            enter_text_char.setVisibility(View.GONE);
        }*//*



         *//*--------------------------------------*//*


                if (newText.length() == 0){
//rl_search

                    relativeLayout.setFocusableInTouchMode(true);
                    //searchAutoComplete.setFocusableInTouchMode(true);
                    // Log.d("length1", String.valueOf(newText.length()));
                }

                if (newText.length() == 3) {
                    relativeLayout.setFocusableInTouchMode(false);
                    //  Log.d("length2", String.valueOf(newText.length()));
                    getLoadproducts(newText);
                    //searchAutoComplete.showDropDown();
                    //_autoCompleteTextView.requestFocus();
                    // searchAutoComplete.requestFocus();
                    //searchAutoComplete.isFocusableInTouchMode();
                    //_autoCompleteTextView.setFocusable(false);
                    //_autoCompleteTextView.requestFocus();
                    //  searchAutoComplete.setSelection(searchAutoComplete.getText().length());
                    //  _autoCompleteTextView.clearFocus();
                    // hideKeyboard();
                }
                else {
                    Log.d("data11","dismisss");
                    //  searchAutoComplete.dismissDropDown();
                    if (posts.size()>0){
                        posts.clear();
                        adpter.clear();
                        adpter.notifyDataSetChanged();
                    }
                }
                return false;
            }

        });
*/
        // searchAutoComplete.clearFocus();
        _autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {

                mfg.setText("");
                _autoCompleteTextView.setText("");
                // _autoCompleteTextView.requestFocus();
                _autoCompleteTextView.clearFocus();
                _Qty.requestFocus();
                //showKeyBoard();
                Dosefrom.setText("");
                Packsize.setText("");
                PTR.setText("");
                MRP.setText("");
                Scheme.setText("");
                Halfscheme.setText("");
                Percentscheme.setText("");
                Qty = 1;
                _Qty.setText("");
                Itemcode = null;
                ProductId = null;

                //  hideKeyboard();
                stock.setText("");
                //stock.getBackground().clearColorFilter();
                stock.setBackgroundColor(Color.WHITE);
                _addProduct.setEnabled(true);
                _addProduct.setClickable(true);
                _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
                // HARISH COMMENT 2 LINES FOR 20 JULY
                /*_Qty.requestFocus();*/
                // showKeyBoard();
                //     _rv_Cartdatalist.setVisibility(View.GONE);
                _Qty.setText("");
                ProductId=view.getTag(R.id.key_product_ItemCode).toString();
                //minQ="-6";
                minQ = view.getTag(R.id.key_product_minQ).toString();
                maxQ = view.getTag(R.id.key_product_maxQ).toString();
                //   Toast.makeText(Order_Search.this, "Product Id " + ProductId, Toast.LENGTH_LONG).show();

                //**

                _autoCompleteTextView.setText(String.valueOf(view.getTag(R.id.key_product_Name)));
                //  Log.d("produdtid1",searchAutoComplete.getText().toString());

                if (Utility.internetCheck(getApplicationContext())) {

                    if (String.valueOf(view.getTag(R.id.key_product_Pack)) == null || String.valueOf(view.getTag(R.id.key_product_Pack)).equals("null")) {
                        Packsize.setText("---");
                    } else {
                        Packsize.setText(String.valueOf(view.getTag(R.id.key_product_Pack)));
                    }
                    if (String.valueOf(view.getTag(R.id.key_product_Mfg)) == null || String.valueOf(view.getTag(R.id.key_product_Mfg)).equals("null")) {
                        mfg.setText("NA");
                    } else {
                        mfg.setText(String.valueOf(view.getTag(R.id.key_product_Mfg)));
                    }

                    if(String.valueOf(view.getTag(R.id.key_product_stockscheme))!=null) {
                        String stockColour = String.valueOf(view.getTag(R.id.key_product_stockscheme));
                        if (stockColour.contains(",")) {
                            String parts[] = stockColour.split(",");
                            String legend_color = parts[0];
                            String legend_name = parts[1];
                            stock.setBackgroundColor(Color.parseColor(legend_color));
                            stock.setText(legend_name);
                        } else {
                            //stock.setBackgroundColor(Color.parseColor(stockColour));

                            stock.setText(" ");
                        }
                    }

                    getLoadproductsDetails(ProductId);
                    _addProduct.setEnabled(true);
                    _addProduct.setClickable(true);
                    _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));



                    //   _Qty.requestFocus();
                    //  _Qty.setText("");

                }
                else
                {
                    Toast.makeText(Order_Search.this, "No Product Details Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    if (!value.isEmpty()) {
                        int num1 = Integer.parseInt(value);
                        if (num1 > 1) {
                            Qty = num1 - 1;
                        }
                    }

                    _Qty.setText(String.valueOf(Qty));
                }catch (Exception e)
                {

                }

            }
        });

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
                }catch (Exception e)
                {

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
                if (ProductName!=null)
                {
                    _autoCompleteTextView.setText(ProductName);
                    _autoCompleteTextView.setSelection(_autoCompleteTextView.getText().length());
                }
                else
                {
                    Toast.makeText(Order_Search.this, "No Search History Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        _addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //jyott 19th june to clear list data
                    if(posts.size()>0){
                        posts.clear();
                        adpter.clear();
                        adpter.notifyDataSetChanged();
                    }

                    _addProduct.setEnabled(false);
                    _addProduct.setClickable(false);
                    _addProduct.setBackgroundColor(Color.parseColor("#d0ebfa"));

                   // _autoCompleteTextView.requestFocus();
                    //  _autoCompleteTextView.setFocusableInTouchMode(true);
                    if (ProductId != null) {

                        if (_autoCompleteTextView.getText().toString().isEmpty())
                        {
                            OGtoast.OGtoast("Please select a product", Order_Search.this);
                        }
                        else {



                            String str = _autoCompleteTextView.getText().toString();
                            if(str.contains(" ")){
                                String[] arrOfStr = str.split(" ",0);
                                ProductName= arrOfStr[0];
                            } else{
                                ProductName= _autoCompleteTextView.getText().toString();
                            }


                            if(QTYCheck(_Qty.getText().toString().trim(),minQ,maxQ)){
                                String value = _Qty.getText().toString();
                                if(value!=null&&value.toString().length()>0) {
                                    int num1 = Integer.parseInt(value);
                                    Qty = num1;
                                }
                            }else{
                                return;
                            }

                            //Have to enter qty Waseem
                            /*if (_Qty.getText().toString().equals(""))
                            {
                                Qty=1;
                            }
                            else
                            {
                                Qty = Integer.parseInt(_Qty.getText().toString());
                            }*/

                            //  addItemCart();
                            if(chemistCartList.size()>0)
                            {
                                isAvailable=true;
                                for (int i = 0; i < chemistCartList.size(); i++)
                                {
                                    if (ProductId.equals(chemistCartList.get(i).getProduct_ID())) {
                                        isAvailable=false;

                                        Log.d("productsAvai11", " Name Available");
                                        Toast.makeText(Order_Search.this, "This Product Already Available", Toast.LENGTH_SHORT).show();
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

                            _Qty.setText("");
                            // _Qty.clearFocus();
                            Itemcode = null;
                            ProductId = null;
                            //  hideKeyboard();
                            stock.setText("");
                            //stock.getBackground().clearColorFilter();
                            stock.setBackgroundColor(Color.WHITE);

                            View view = snackbar.getView();
                            //     snackbar.setAction("Action", null).show();
                            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            params.gravity = Gravity.TOP;
                            view.setLayoutParams(params);
                            // Snackbar.make(getView(), "Snackbar", Snackbar.LENGTH_LONG)
                            snackbar.show();

                        }
                    } else {
                        OGtoast.OGtoast("Please select a product", Order_Search.this);
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
                cancel_order();
            }
        });


        //confirmOrder Button click Events

        _confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //      Log.d("CLICKCONFIRMORDER", "YSE CLICK CONFIRM ORDER");

                Cursor crs_cart = db.get_chemist_cart(Stockist_id);


                if (chemistCartList != null && chemistCartList.size() > 0)
                {
                    if(Utility.internetCheck(Order_Search.this)){
                        new AlertDialog.Builder(Order_Search.this)
                                .setTitle("Order")
                                .setMessage("Do you wish to confirm your order?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        completeOrderAmount =0.0;
                                        for(ChemistCart cart : chemistCartList) {
                                            completeOrderAmount = completeOrderAmount + Double.parseDouble(cart.getPrice());
                                        }
                                        if(CheckMinimumOrder(completeOrderAmount)){
                                            delivery_options();
                                        }else{
                                            QTYDialog("Minimum order Should be "+minOrderValue);
                                        }

                                        // confirm_order();
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

                    }else{
                        Toast.makeText(getApplicationContext(),"Connection Not Available !",Toast.LENGTH_SHORT).show();
                        // OGtoast.OGtoast("Connection Not Available !", Order_Search.this);


                    }
                }

                else {
                    OGtoast.OGtoast("Cart is empty", Order_Search.this);
                }
            }
        });

    }

    void confirm_dialog(final String comment) {
        new AlertDialog.Builder(Order_Search.this)
                .setTitle("Order")
                .setMessage("Do you wish to confirm your order?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
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


    private void init() {
        Stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
        minOrderValue = getIntent().getStringExtra(StockistList.CHEMIST_MINIMUM_ORDER_VALUE);
        // minOrderValue = "500";
        minOrderCheck =getIntent().getIntExtra(StockistList.CHEMIST_MINIMUM_ORDER_CHECK,0);
        client_id = pref.getString(CLIENT_ID, "");
        user_id = pref.getString(USER_ID, "");
        Log.d("userid","id:"+user_id);
        daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();
        chemistCartList = chemistCartDao.getCartItem(Stockist_id, false);
        if (chemistCartList != null) {
            n_product_cart_count = chemistCartList.size();
        }

        Log.d("stcchem",client_id + Stockist_id);
        //get_stockist_inventory(client_id);

        //  get_product_data_on_stockist();


        if (n_product_cart_count > 0) {
            for (int i = 0; i < chemistCartList.size(); i++) {
                orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
            }
        }
        _OrderAmt.setText("Rs." + String.format("%.2f", orderAmount));
        // get_ProductList_json();
    }

    private void addItemCart() {
        // sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        //** Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
        //  private UUID mId;
        // mId =  UUID.randomUUID();
        //Generate random ID
        mId = mId.randomUUID();
        Doc_Id = "OG" + mId;
        // Log.d("docid",Doc_Id);
        Float price;

        price = vRate * Qty;

        ChemistCart chemistCart = new ChemistCart();

        chemistCart.setDOC_ID(Doc_Id);
        chemistCart.setItems(Itemcode);
        chemistCart.setItemname(Itemname);
        chemistCart.setProduct_ID(ProductId);
        chemistCart.setQty(String.valueOf(Qty));
        chemistCart.setUOM(UOM);
        // chemistCart.setRate("29");
        chemistCart.setRate(vRate.toString());
        chemistCart.setPrice(price.toString());
        //  chemistCart.setMRP(String.valueOf("37"));
        chemistCart.setMRP(String.valueOf(vMrp));
        // chemistCart.setPACK(String.valueOf(vPack)); //*********Pack in cart Double.valueOf(chemistCart.getPACK()
        //  chemistCart.setPACK(Double.valueOf(chemistCart.getPACK()));
        chemistCart.setPACK(Pack);
        chemistCart.setCreatedon(dateFormat.format(Calendar.getInstance().getTime()));
        // calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        chemistCart.setSalesman(false);
        chemistCart.setStockist_Client_id(Stockist_id);
        chemistCart.setRemarks("cart");
        chemistCart.setOrder_sync(false);
        // chemistCart.setDoc_Date(dateFormat.format(Calendar.getInstance().getTime()));
        chemistCart.setAmount(price.toString());
        chemistCart.setScheme(Sche);
        chemistCart.setHalfScheme(hs);
        chemistCart.setSub_stkID(sub_id);
        chemistCart.setMinQ(minQ);
        chemistCart.setMaxQ(maxQ);
        //chemistCart.
        //chemistCart.se

//        price += orderAmount;
//        orderAmount = price;

        chemistCartDao.insertOrUpdateCart(chemistCart, false);
        chemistCartList.clear();
        chemistCartList = chemistCartDao.getCartItem(Stockist_id, false);
        n_product_cart_count = chemistCartList.size();
        createCartBadge(n_product_cart_count);

        //9-6-2018 edit
        //        if (chemistCartList.size()>0)
//        {
//
//                ProductName = chemistCartList.get(chemistCartList.size() - 1).getItemname();
//                Log.d("Name",ProductName);
//
//
//
//        }

        adpter.notifyDataSetChanged();




        orderAmount = 0;
        for (int i = 0; i < chemistCartList.size(); i++) {

            orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
        }
        _OrderAmt.setText("Rs." + String.format("%.2f", orderAmount));

        // createCartBadge(chemistCartDao.ge);
        //Log.v("GreenDao", chemistCart.getId().toString());

        if (Qty==1)
        {
            //OGtoast.OGtoast("one quantity added", Order_Search.this);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.placeSnackBar), "one quantity added", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        }
        else
        {
            // OGtoast.OGtoast("Product added to cart succesfully", Order_Search.this);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.placeSnackBar), "Product added to cart succesfully", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        }
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        mToolbarMenu = paramMenu;


        if (n_product_cart_count != null) {
            createCartBadge(n_product_cart_count);
        }
        return super.onPrepareOptionsMenu(paramMenu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_order, menu);
        //  menu.findItem(R.id.action_inventory).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart:
                hideKeyboard();
                Clicked_cart = true;

                //Log.d("length4", newTextLength);


                // Log.d("click_bottmosheet11", String.valueOf(posts));
                //posts.clear();
                /*_autoCompleteTextView.setFocusable(true);
                _autoCompleteTextView.setIconified(false);
                searchAutoComplete.setText("");
                searchAutoComplete.dismissDropDown();
                searchAutoComplete.clearFocus();*/
                Log.d("click_bottmosheet12","click bottom sheet");
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                   /* behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    _rv_Cartdatalist.removeAllViewsInLayout();*/

                    behavior.setHideable(true);
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    Clicked_cart = false;
                } else {
                    if (chemistCartList.size() > 0) {
                        /*_autoCompleteTextView.setFocusable(true);
                        _autoCompleteTextView.setIconified(false);
                        searchAutoComplete.setText("");
                        searchAutoComplete.dismissDropDown();
                        _autoCompleteTextView.clearFocus();*/
                        //_rv_Cartdatalist.setVisibility(View.VISIBLE);
                        _rv_Cartdatalist.setVisibility(View.VISIBLE);
                        Log.d("click_bottmosheet18","click bottom sheet");
                        //    _rv_Cartdatalist.setVisibility(View.VISIBLE);
                        fill_Cartdata();
                    } else {
                        OGtoast.OGtoast("Cart is empty", this);
                    }

                    // _fab.setVisibility(View.GONE);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //return super.dispatchKeyEvent(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // keydown logic
            return true;
        }
        return false;
    }*/

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Log.d("click_On15","Click On Key Down pressed");
            finish();
            return true;
        }
        return false;
    }*/


   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Log.d("click_On10","back use onKey Down");

            _autoCompleteTextView.setIconified(false);
            onBackPressed();

            return true;
        }
        return super.onKeyDown(keyCode, event);
//don't forget call this
    }*/

    @Override
    public void onResume() {

        Log.d("click_On11","Click On Back pressed onResume");
        super.onResume();
        posts.clear();
        //adpter.clear();
        //adpter.notifyDataSetChanged();

        get_cart_id();
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            _rv_Cartdatalist.removeAllViewsInLayout();

        }

    }

    public void filter(String charText) {

        posts.clear();
        if (charText.length() == 0) {
//            charText = "a";
//            Toast.makeText(getApplicationContext(),"chartext"+charText.toString(),Toast.LENGTH_SHORT).show();
//            // alternativeitems.addAll(arraylist);
        } else {
            for (m_product_list_chemist wp : posts) {
                if (wp.get_in().toUpperCase()
                        .contains(charText.toUpperCase())) {
                    posts.add(wp);
                } else if (wp.get_in()
                        .contains(charText)) {
                    posts.add(wp);
                }
            }
        }
    }

    private void get_CartData() {

        Cursor crs_cart = db.get_chemist_cart_data(Doc_Id);
        String previousCartData = ConstData.get_jsonArray_from_cursor(crs_cart).toString();// pref.getString("CartList", null);

        if (!previousCartData.isEmpty() && crs_cart.getCount() > 0) {
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

    private void fill_Cartdata() {

        cartAdapter = new RecyclerView.Adapter<BindingViewHolder>() {
            //final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(Order_Search.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.adpter_cartdata_list_new, parent, false);
//adpter_cartdata_list
                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                final ChemistCart chemistCart = chemistCartList.get(position);
                Log.e("Cart_list", chemistCart.toString());
                holder.getBinding().setVariable(BR.v_cartDatalist, chemistCart);
                holder.getBinding().executePendingBindings();
                /* Quantity Click */
                holder.getBinding().getRoot().findViewById(R.id.txt_edit_qty).setOnClickListener(new View.OnClickListener() {
                    //                    @Override
//                    public void onClick(View v) {
                    //holder.getBinding().getRoot().findViewById(R.id.linear_quantity_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  _rv_Cartdatalist.setVisibility(View.GONE);
                        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            behavior.setHideable(true);
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }*/
                        editQuantityDialog(chemistCartList.get(position).getStockist_Client_id(), chemistCartList.get(position).getProduct_ID(),
                                chemistCartList.get(position).getItemname(), chemistCartList.get(position).getRate(),
                                chemistCartList.get(position).getQty(),chemistCartList.get(position).getMinQ(),chemistCartList.get(position).getMaxQ());
                    }
                });


                //cancel
                holder.getBinding().getRoot().findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   Log.e("position",getAd);
                        // int pos = _rv_Cartdatalist.getAdapter().getItemId(position);
                        delete_product_from_cart(chemistCartList.get(position));
                        //comment by apurva
                        _autoCompleteTextView.setText("");
                        _Qty.setText("");
                        stock.setBackgroundColor(Color.WHITE);
                        _autoCompleteTextView.requestFocus();
                        //  get_stockist_legends();

                    }
                });

                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //        Log.e("Click", "Item click");
                        //*********16/6 jyott
                     /*   searchAutoComplete.setText(chemistCartList.get(position).getItemname());
                      //  searchAutoComplete.requestFocus();
                        _Qty.setText(chemistCartList.get(position).getQty());
                        _Qty.requestFocus();*/
                   /* //    _rv_Cartdatalist.setVisibility(View.GONE);
                        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setHideable(true);
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }*/
                       /* _rv_Cartdatalist.setVisibility(View.GONE);
                        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            // behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            behavior.setHideable(true);
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }*/
                        editQuantityDialog(chemistCartList.get(position).getStockist_Client_id(), chemistCartList.get(position).getProduct_ID(),
                                chemistCartList.get(position).getItemname(), chemistCartList.get(position).getRate(),
                                chemistCartList.get(position).getQty(),chemistCartList.get(position).getMinQ(),chemistCartList.get(position).getMaxQ());

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

                if (Clicked_cart) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Clicked_cart = false;
                }
            }
        });
    }

//edit quantity

    /* Edit Quantity in Cart */
    private void editQuantityDialog(final String stockistClientId, final String productId, String item, final String ptr, final String quantity,final String minQ,final String maxQ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_quantity_dialog, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_close_dialog);
        TextView textViewItem = (TextView) view.findViewById(R.id.txt_item_name_dialog);
        TextView textViewPtr = (TextView) view.findViewById(R.id.txt_item_ptr_dialog);
        Button buttonMinus = (Button) view.findViewById(R.id.btn_minus_dialog);
        final EditText editTextQuantity = (EditText) view.findViewById(R.id.edt_quantity_dialog);
        Button buttonPlus = (Button) view.findViewById(R.id.btn_plus_dialog);
        final Button buttonDone = (Button) view.findViewById(R.id.btn_done_dialog);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        textViewItem.setText("Item: " + item);
        textViewPtr.setText("PTR: " + ptr);
        editTextQuantity.setText(quantity);
        editedQuantity = Integer.valueOf(editTextQuantity.getText().toString());
        editTextQuantity.setSelection(editTextQuantity.getText().length());
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
                    Toast.makeText(Order_Search.this, "Min. quantity should be 1", Toast.LENGTH_SHORT).show();
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
                // hideKeyboard();
                //  showKeyBoard();
                _autoCompleteTextView.clearFocus();
                hideKeyboardFrom(Order_Search.this,buttonDone);

                //     _autoCompleteTextView.setFocusableInTouchMode(false);
                if (editTextQuantity.getText().toString().equalsIgnoreCase("")&&editTextQuantity.getText().toString().trim().length()==0) {
                    Toast.makeText(Order_Search.this, "Min. quantity should be one", Toast.LENGTH_SHORT).show();
                }else{
                    if(QTYCheck(editTextQuantity.getText().toString(),minQ,maxQ)){
                        int quantity = 0;
                        Float price = null;
                        if (editTextQuantity.getText().toString().equalsIgnoreCase("")) {

                        } else {
                            quantity = Integer.valueOf(editTextQuantity.getText().toString());
                            price = Float.valueOf(ptr) * quantity;
                        }


                        ChemistCart chemistCart = new ChemistCart();
                        chemistCart.setDOC_ID(Doc_Id);
                        chemistCart.setItems(Itemcode);
                        chemistCart.setItemname(Itemname);
                        chemistCart.setProduct_ID(productId);
                        chemistCart.setQty(String.valueOf(quantity));
                        chemistCart.setUOM(UOM);
                        // chemistCart.setRate(vRate.toString());
                        chemistCart.setRate(String.valueOf(ptr));
                        chemistCart.setPrice(String.valueOf(price));
                        chemistCart.setMRP(String.valueOf(vMrp));
                        chemistCart.setCreatedon(dateFormat.format(Calendar.getInstance().getTime()));
                        chemistCart.setSalesman(false);
                        chemistCart.setStockist_Client_id(stockistClientId);
                        chemistCart.setRemarks("cart");
                        chemistCart.setOrder_sync(false);
                        chemistCart.setAmount(String.valueOf(price));
                        chemistCart.setPACK(Pack);
                        // set salesman false
                        chemistCartDao.insertOrUpdateCart(chemistCart, false);

                        orderAmount = 0;
                        for (int i = 0; i < chemistCartList.size(); i++) {
                            orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
                        }
                        _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmount));

                        cartAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                        hidealwaysKeyboard();

                    }else{
                        return;
                    }
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
    private void hidealwaysKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    private void search_json(JSONArray j_array, String search_word) {
        for (int i = 0; i < j_array.length(); i++) {

        }

    }

    void get_cart_id() {

        if (Stockist_id != null) {
            Cursor crs_cart = db.get_chemist_cart(Stockist_id);

            if (crs_cart != null && crs_cart.getCount() > 0) {
                while (crs_cart.moveToNext()) {
                    Doc_Id = crs_cart.getString(crs_cart.getColumnIndex("DOC_NO"));
                    Cart_Id_available = true;
                    n_product_cart_count = (db.get_total_order_item_count(Doc_Id));
                    _OrderAmt.setText(" Rs." + crs_cart.getString(crs_cart.getColumnIndex("Amount")));

                    if (n_product_cart_count != 0) {
                        if (mToolbarMenu != null) {
                            createCartBadge(n_product_cart_count);
                        }
                    }

                }
            }


            if (Doc_Id == null)
            {
                Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;

                Cart_Id_available = false;

            }
        }

    }
    private void delivery_options(){


        AlertDialog.Builder builder = new AlertDialog.Builder(Order_Search.this);

        builder.setTitle("Select Delivery Options");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        delivery_option="PickUp";
                        //  Toast.makeText(Order_Search.this, delivery_option, Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        delivery_option="Delivery";
                        //   Toast.makeText(Order_Search.this, delivery_option, Toast.LENGTH_LONG).show();
                        break;

                }
                // confirm_order();
                order_summary_dialog();
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();



    }

    private void order_summary_dialog()

    {
        LayoutInflater inflater = LayoutInflater.from(Order_Search.this);
        final View dialogview = inflater.inflate(R.layout.fragment_order_summary, null);
        final Dialog infoDialog = new Dialog(Order_Search.this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        txt_comment = (EditText) dialogview.findViewById(R.id.edit_comment);
        txt_cust_name = (TextView) dialogview.findViewById(R.id.customer_name);
        txt_order_id = (TextView) dialogview.findViewById(R.id.order_id);
        txt_total_items = (TextView) dialogview.findViewById(R.id.total_items);
        // txt_deliveryoption = (TextView) dialogview.findViewById(R.id.selected_deliveryoption);
        txt_total = (TextView) dialogview.findViewById(R.id.total_amount);
        //  pgr.setVisibility(View.VISIBLE);
        ChemistCart chemistCart = chemistCartList.get(0);
        txt_cust_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
        txt_order_id.setText(chemistCart.getDOC_ID());
        txt_total_items.setText(String.valueOf(chemistCartList.size()));
        txt_total.setText(String.valueOf(orderAmount));
        // txt_deliveryoption.setText(delivery_option);

        dialogview.findViewById(R.id.confirm_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("comment","hiiiiii");
                // confirm_dialog(txt_comment.getText().toString());
                if (txt_comment.getText().toString().equals(""))
                {
                    String Comment="No Instructions";
                    confirm_order(Comment);
                }
                else
                {
                    confirm_order(txt_comment.getText().toString());
                }

                infoDialog.dismiss();
            }
        });


        infoDialog.show();
    }

    private void confirm_order( String comment) {
        try {

            //   Log.d("Comment","Comment:"+comment);
            JSONArray main_j_array = new JSONArray();
            JSONArray main_j_array12 = new JSONArray();
            JSONObject main_j_object = new JSONObject();
            JSONObject main_j_object12 = new JSONObject();
            JSONArray line_j_array = new JSONArray();
            JSONArray line_j_array12 = new JSONArray();
            JSONArray line_j_array11 = new JSONArray();

            // JSONObject line_j_object = new JSONObject();
            main_j_object.put("Transaction_No", "");
            main_j_object.put("Client_ID", Integer.parseInt(client_id));
            //commented by apurva on (22-8-2018)
            // main_j_object.put("CreatedBy", Integer.parseInt(client_id));

            main_j_object.put("CreatedBy", Integer.parseInt(user_id));
            //comment by apurva
            //  main_j_object.put("Comments", editText.getText().toString());
            ChemistCart chemistCart = chemistCartList.get(0);
            // Cursor crs_cart = db.get_chemist_cart(Stockist_id);
            String document_id = chemistCartList.get(0).getDOC_ID();
            String parts[] = document_id.split("OG");
            String uniq_id = parts[1];
            if (chemistCartList.size() > 0) {
                //   while (crs_cart.moveToNext()) {
                // ------------new data------------

                main_j_object12.put("uniq_no", uniq_id);
                //  main_j_object12.put("CreatedBy", Integer.parseInt(client_id));

                // ------------new data------------
                main_j_object.put("DOC_NO", chemistCart.getDOC_ID());
                main_j_object.put("Doc_Date", chemistCart.getCreatedon());
                main_j_object.put("Stockist_Client_id", Stockist_id);
                main_j_object.put("Remarks", chemistCart.getRemarks());
                main_j_object.put("Items", chemistCartList.size());
                main_j_object.put("Amount", orderAmount);
                main_j_object.put("Status", 0);
                main_j_object.put("Createdon", chemistCart.getCreatedon());
                //**********
                main_j_object.put("Delivery_Option",delivery_option);
                main_j_object.put("Comments", comment);
                //**********

                Doc_Id = chemistCart.getDOC_ID();
                Cart_Id_available = true;
                // }

                //         Log.e("main_j_array", main_j_array.toString());
            }


            Cursor crs_cart_details = db.get_chemist_cart_detail(Doc_Id);


            int i = 1;

            for (i = 0; i < chemistCartList.size(); i++) {
                //   if (crs_cart_details.moveToFirst()) {
                //    do {


                chemistCart = chemistCartList.get(i);
                JSONObject line_j_object = new JSONObject();
                line_j_object.put("Transaction_No", "");
                line_j_object.put("Doc_item_No", chemistCart.getItems());
                //line_j_object.put("Doc_item_No", i);
                line_j_object.put("PACK", String.valueOf(chemistCart.getPACK()));
                // ***********error placing order  line_j_object.put("PACK", Double.valueOf(chemistCart.getPACK()));
                line_j_object.put("Product_ID", chemistCart.getProduct_ID());
                line_j_object.put("Qty", Integer.parseInt(chemistCart.getQty()));
                line_j_object.put("UOM", chemistCart.getUOM());
                line_j_object.put("Rate", Double.valueOf(chemistCart.getRate()));
                line_j_object.put("Price", Double.valueOf(chemistCart.getPrice()));
                line_j_object.put("MRP", Double.valueOf(chemistCart.getMRP()));
                line_j_object.put("Createdon", chemistCart.getCreatedon());

                //  line_j_object.put("Createdon", "0000-00-00 00:00:00");
                line_j_object.put("CreatedBy", Integer.parseInt(Stockist_id));
                line_j_object.put("Delivery_Option",delivery_option);
                line_j_object.put("StockistID", chemistCart.getSub_stkID());
                line_j_object.put("Scheme", chemistCart.getSub_stkID());
                // line_j_object.put("Scheme", chemistCart.getScheme());
                line_j_array.put(line_j_object);
                //  --------------new data-------------
                JSONObject line_j_object12 = new JSONObject();
                line_j_object12.put("Client_ID", Integer.parseInt(client_id));
                line_j_object12.put("Comments", comment);
                //commented by apurva add user id  to show created by on web (22-8-2018)
                // line_j_object12.put("CreatedBy", Integer.parseInt(client_id));
                line_j_object12.put("CreatedBy", Integer.parseInt(user_id));
                line_j_object12.put("Createdon", chemistCart.getCreatedon());
                line_j_object12.put("DOC_NO", document_id);
                line_j_object12.put("Deliveryoption", delivery_option);
                line_j_object12.put("Doc_Date", chemistCartList.get(0).getCreatedon());
                line_j_object12.put("Status", 0);
                line_j_object12.put("Stockist_Client_id", chemistCart.getSub_stkID());
                line_j_object12.put("supStkID", Stockist_id);
                line_j_object12.put("MRP", Double.valueOf(chemistCart.getMRP()));
                line_j_object12.put("Packsize", String.valueOf(chemistCart.getPACK()));
                line_j_object12.put("Price",Double.valueOf(chemistCart.getPrice()));
                line_j_object12.put("Product_ID", chemistCart.getProduct_ID());
                line_j_object12.put("Qty", Integer.parseInt(chemistCart.getQty()));
                line_j_object12.put("Rate", Double.valueOf(chemistCart.getRate()));
                line_j_object12.put("Scheme", "");
                line_j_object12.put("Transaction_No", "null");
                line_j_object12.put("UOM",chemistCart.getUOM());
                line_j_object12.put("Uniq_id", uniq_id);
                // line_j_object12.put("Remarks", chemistCartList.get(0).getRemarks());
                line_j_array12.put(line_j_object12);

                //  } while (crs_cart_details.moveToNext());
                // }
                //Waseem
                // completeOrderAmount = completeOrderAmount + Double.parseDouble(chemistCart.getPrice());
            }

            main_j_array.put(main_j_object);
            main_j_array.put(line_j_array);
            main_j_array12.put(line_j_array12);
            main_j_array12.put(line_j_array11);
            main_j_array12.put(uniq_id);
            Log.d("OrderData", String.valueOf(main_j_array));
            MasterPlacedOrder masterPlacedOrder = new MasterPlacedOrder();

            masterPlacedOrder.setJson(main_j_array12.toString());
//
            masterPlacedOrder.setCustomerID(client_id);
            masterPlacedOrder.setDoc_ID(document_id);
            //**   masterPlacedOrder.setDoc_ID(Doc_Id);
            long confirm = daoSession.getMasterPlacedOrderDao().insert(masterPlacedOrder);

            if (confirm > 0) {
                chemistCartDao.deleteInTx(chemistCartList);

                Intent download_intent1= new Intent(RefreshData.ACTION_CONFIRM_ORDER_CHEMIST, null, this, RefreshData.class);
                startService(download_intent1);
            }
            order_confirmed_dialog(Doc_Id);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void get_product_data_on_stockist() {
        _txt_customer_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
        client_id = pref.getString(CLIENT_ID, "");

       /* if (db.check_stockist_data(Stockist_id) > 0) {
        } else {*/
//comment by apurva
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


        SharedPreferences.Editor edt = pref.edit();
        MakeWebRequest.MakeWebRequest("get", GET_CHEMIST_PRODUCT_DATA,
                GET_CHEMIST_PRODUCT_DATA + "[" + client_id + "," + Stockist_id + "]", this, false);
        edt.putString(CHEMIST_LAST_DATA_SYNC, simpleDateFormat.format(calendar.getTime()));
        edt.commit();

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {


    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        // Log.e("chemist_product14", response.toString());
        if (response != null) {
            //posts.clear();
            //  Log.e("chemist_product11", response.toString());

            try {
                posts.clear();
                if (f_name.equals(AppConfig.GET_CHEMIST_PRODUCT_DATA)) {
                    posts = new ArrayList<m_product_list_chemist>();
                    String jsondata = response.toString();
                    Log.d("jsonArray113",jsondata);
                    if (!jsondata.isEmpty()) {

                        JSONObject j_obj = response.getJSONObject(0);
//*********jyott 18/6
                        try {

                            if (j_obj.getString("_dform") == null || j_obj.getString("_dform").equals("null")) {
                                Dosefrom.setText("--");
                            } else {
                                Dosefrom.setText(j_obj.getString("_dform"));
                            }


                            if (j_obj.getString("_schm")== null ||j_obj.getString("_schm").equals("null")||j_obj.getString("_schm")=="") {
                                Scheme.setText("NA");
                            } else {
                                Scheme.setText(j_obj.getString("_schm"));
                            }
                            if (String.valueOf(j_obj.getDouble("_ptr"))== null || String.valueOf(j_obj.getDouble("_ptr")).equals("null")) {
                                PTR.setText("0.0");
                            } else {
                                PTR.setText(String.valueOf(j_obj.getDouble("_ptr")));
                            }
                            if (String.valueOf(j_obj.getDouble("_mrp"))== null || String.valueOf(j_obj.getDouble("_mrp")).equals("null")) {
                                MRP.setText("0.0");
                            } else {
                                MRP.setText(String.valueOf(j_obj.getDouble("_mrp")));
                            }

                            //---------------------------half scheme percent scheme edit

                            //Log.d("Halfscheme_show",)

                            if(AppConfig.AppCode==1){
                                if (j_obj.getString("BoxSize") == null || j_obj.getString("BoxSize").equals("null") || j_obj.getString("BoxSize").equals("")) {
                                    Halfscheme.setText("Box Pack Size: NA");
                                    hs = "NA";
                                } else {
                                    hs = j_obj.getString("BoxSize");
                                    Halfscheme.setText("Box Pack Size: "+hs);
                                    // halfScheme=(arg1.getTag(R.id.key_product_halfscheme).toString());
                                }
                            }else{
                                if (j_obj.getString("_hschm") == null || j_obj.getString("_hschm").equals("null") || j_obj.getString("_hschm").equals("")) {
                                    Halfscheme.setText("Half Scheme: NA");
                                    hs = "NA";
                                } else {
                                    hs = j_obj.getString("_hschm");
                                    Halfscheme.setText("Half Scheme: "+hs);
                                    // halfScheme=(arg1.getTag(R.id.key_product_halfscheme).toString());
                                }

                            }


                            if (j_obj.getString("_pschm") == null || j_obj.getString("_pschm").equals("null") || j_obj.getString("_pschm").equals("")) {
                                Percentscheme.setText("% Scheme: NA");
                            } else {
                                String ps= j_obj.getString("_pschm");
                                Percentscheme.setText("% Scheme: "+ps);
                            }
//--------------------sub stockist id------------------------------------//
                            if(j_obj.has("_sid")){
                                if (j_obj.getString("_sid") == null || j_obj.getString("_sid").equals("null") || j_obj.getString("_sid").equals("")) {
                                    sub_id= "0";
                                    //  Log.d("_sid",sub_id);
                                } else {
                                    sub_id= j_obj.getString("_sid");
                                    // Log.d("_sid",sub_id);
                                }
                            } else{
                                sub_id= "NA";
                                //Log.d("_sid",sub_id);
                            }

//--------------------sub stockist id------------------------------------//

                            //String.valueOf(view.getTag(R.id.key_product_Name))
                            Itemcode = (j_obj.getString("_icode"));
                            MfgCode = (mfg.getText().toString());
                            // type = Integer.parseInt(view.getTag(R.id.key_product_Type).toString());
                            type = 0;
                            Itemname = _autoCompleteTextView.getText().toString();
                            Pack = (Packsize.getText().toString()== null) ? "NA" : Packsize.getText().toString();
                            MfgName = mfg.getText().toString();
//                Itemname = String.valueOf(view.getTag(R.id.key_product_Name));
//                Pack = (view.getTag(R.id.key_product_Pack) == null) ? "NA" : view.getTag(R.id.key_product_Pack).toString();
//                MfgName = view.getTag(R.id.key_product_Mfg).toString();
//                // DoseForm = "123";
                            DoseForm = (j_obj.getString("_dform") == null) ? "NA" : j_obj.getString("_dform");
                            Sche = (j_obj.getString("_schm") == null) ? "NA" : j_obj.getString("_schm");

                            //DoseForm = posts.get(0).get_dform().toString();
                            //Sche = "456";
                            //Sche = posts.get(0).get_schm().toString();

                            if (!String.valueOf(j_obj.getDouble("_ptr")).equals("null")) {
                                vRate = Float.parseFloat(String.valueOf(j_obj.getDouble("_ptr")));
                                // vRate = Float.parseFloat(posts.get(0).get_ptr().toString());
                            } else {
                                vRate = (float) 0;
                            }
                            if (!String.valueOf(j_obj.getDouble("_mrp")).equals("null")) {
                                vMrp = Float.parseFloat(String.valueOf(j_obj.getDouble("_mrp")));
                                // vMrp = Float.parseFloat(posts.get(0).get_mrp().toString());
                            } else {
                                vMrp = (float) 0;
                            }
                            //***************pack in cart************************//
                            if (Packsize.getText().toString().equals("null")) {
                                vPack = Packsize.getText().toString();
                                //   vPack = Float.parseFloat(view.getTag(R.id.key_product_Pack).toString());
                            } else {
                                vPack = "0";
                            }

                            //***************pack in cart************************//
                            if (!j_obj.getString("_dform").equals("null")) {
                                UOM = j_obj.getString("_dform");
                            }

                            ProductId = j_obj.getString("_pid");
                            // ProductId = view.getTag(R.id.key_product_code).toString();
                            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                            {
                                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                _rv_Cartdatalist.removeAllViewsInLayout();

                            }

                        }
                        catch (Exception e)
                        {

                            // Log.d("Message",e.getMessage());
                        }

                    }
                }
                else if(f_name.equals("GET_CHEMIST_PRODUCT_SEARCH")){
// search with keywords
                    posts=  new ArrayList<m_product_list_chemist>();
                    String jsondata = dummyjson.toString();
                    if (!jsondata.isEmpty()) {
                        Log.d("jsonArray222", jsondata.toString());
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        posts = new LinkedList<m_product_list_chemist>(Arrays.asList(mGson.fromJson(jsondata, m_product_list_chemist[].class)));
                        // adpter.notifyDataSetChanged();
                        //apurva
                        adpter = new ad_AutocompleteCustomArray_chemist(getApplicationContext(), posts);
                        _autoCompleteTextView.setAdapter(adpter);
                        // _autoCompleteTextView.setSuggestionsAdapter(adpter);
                        adpter.notifyDataSetChanged();

//                        searchAutoComplete.setFocusable(false);
//                        _autoCompleteTextView.setIconified(false);
                        //commented by apurva 16/7/2018
                        _autoCompleteTextView.clearFocus();
                        //     showKeyBoard();
//                        searchAutoComplete.requestFocus();
                        // hideKeyboard();
                        //adpter.notifyDataSetChanged();
                        //saveChemistListInSQLite(jsondata);
                    }
                }
                if (f_name.equals(AppConfig.POST_GET_STOCKIST_LEGENDS)) {
                    legend_data = response.toString();
                }
            } catch (Exception e)
            {

            }
        }
        else
        {

            //Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_SHORT).show();
        }

    }

    void order_confirmed_dialog(String doc_number) {
        new AlertDialog.Builder(Order_Search.this)
                .setTitle("Order")
                .setMessage("Order placed successfully.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        return false;
//    }




    public class LoadData extends AsyncTask<Void, Void, Void> {

        JSONArray response;


        public LoadData(JSONArray response) {
            this.response = response;
        }
        //declare other objects as per your need

        @Override
        protected void onPreExecute() {
            // progressDialog = ProgressDialog.show(Order_Search.this, "Please Wait", "Loading.... ", true);
        }



        @Override
        protected Void doInBackground(Void... params) {

            //insert_inventory_products(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // progress.setVisibility(View.GONE);
            //get_ProductList_json();
            progressDialog.dismiss();
        }
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
                badgeDrawable.setCount(paramInt);
                localLayerDrawable.mutate();
                localLayerDrawable.setDrawableByLayerId(R.id.ic_badge, badgeDrawable);
                cartItem.setIcon(localLayerDrawable);
            }
        }
    }


    void cancel_order() {

        Cursor crs_cart = db.get_chemist_cart(Stockist_id);

        if (chemistCartList.size() > 0) {


            new AlertDialog.Builder(Order_Search.this)
                    .setTitle("Order")
                    .setMessage("Cancel Order")
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            chemistCartDao.deleteInTx(chemistCartList);

                            Snackbar mySnackbar = Snackbar.make(getWindow().getDecorView(), "Products removed from cart", Snackbar.LENGTH_SHORT);
                            mySnackbar.show();
                            finish();
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
            OGtoast.OGtoast("Cart is empty", Order_Search.this);
        }
    }

    @Override
    public void onBackPressed() {
        // It's expensive, if running turn it off.
        // DataHelper.cancelSearch();
        // hideKeyboard();
        // daoSession.getStockistProductsDao().deleteAll();
        Log.d("click_On13","Click On Back pressed");


        super.onBackPressed();
        finish();
    }



    void delete_product_from_cart(ChemistCart chemistCart) {


        chemistCartDao.delete(chemistCart);
        chemistCartList.remove(chemistCart);

        int item_count = chemistCartList.size();
        float orderAmounts = .2f;

        for (ChemistCart cart : chemistCartList) {

            orderAmounts += Float.valueOf(cart.getAmount());

        }


        orderAmount = orderAmounts;


        _OrderAmt.setText(" Rs." + String.valueOf(orderAmounts));
        // db.update_into_chemist_cart(Doc_Id, item_count, price.toString());

        if (chemistCartList.size() == 0) {

            Cart_Id_available = false;
            _OrderAmt.setText("");

            orderAmount = 0;
        }

        if (mToolbarMenu != null) {
            createCartBadge(item_count);
        }

        OGtoast.OGtoast("Product removed succesfully", Order_Search.this);
        cartAdapter.notifyDataSetChanged();

    }

    void delete_product_from_cart(String itme_no, Float price) {

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

        OGtoast.OGtoast("Product removed succesfully", Order_Search.this);

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            _rv_Cartdatalist.removeAllViewsInLayout();

        }
        //   get_stockist_legends();
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_autoCompleteTextView.getWindowToken(), 0);
    }


    @Override
    protected void onDestroy() {

        Log.d("click_On12","Click On Back pressed");

        // daoSession.getStockistProductsDao().deleteAll();
        super.onDestroy();
    }

//
//    private void setStockLegend(int stockNum) {
//        if (stockNum > 300) {
//            stock.setBackgroundColor(Color.parseColor("#5371D7"));
//        } else if (stockNum > 200) {
//            stock.setBackgroundColor(Color.parseColor("#5CDA6D"));
//        } else if (stockNum > 100) {
//            stock.setBackgroundColor(Color.parseColor("#EFE040"));
//        } else {
//            stock.setBackgroundColor(Color.parseColor("#EF9940"));
//        }
//    }

    private void getLoadproducts() {

        Utility.syncWithoutProgress(this, "Wait a moment...");
        String productjson=Utility.read(this,Config.FILENAME);
        GsonBuilder builder = new GsonBuilder();
        Gson mGson = builder.create();
        posts = new LinkedList<m_product_list_chemist>(Arrays.asList(mGson.fromJson(productjson, m_product_list_chemist[].class)));
        // adpter.notifyDataSetChanged();
        //apurva
        adpter = new ad_AutocompleteCustomArray_chemist(getApplicationContext(), posts);
        _autoCompleteTextView.setAdapter(adpter);
        // _autoCompleteTextView.setSuggestionsAdapter(adpter);
        adpter.notifyDataSetChanged();

        Utility.hideSyncDialog();

    }

    private void getLoadproductsDetails(String product_id) {
        String _key1 = "S";
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(Stockist_id);
        jsonArray.put(1);
        jsonArray.put("");
        jsonArray.put(product_id);
        jsonArray.put(_key1);
        jsonArray.put(client_id); //chemist id
        //    Log.d("jsonArray1111", jsonArray.toString());
        //  Log.d("jsonArray1112", jsonArray.toString());

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_PRODUCT_DATA,
                AppConfig.GET_CHEMIST_PRODUCT_DATA + jsonArray,Order_Search.this, false);

        String url = AppConfig.GET_CHEMIST_PRODUCT_DATA + jsonArray;
        Log.d("jsonArray221",url);


    }

    private boolean CheckMinimumOrder(double cartAmount){
        if(minOrderCheck!=0) {
            if (cartAmount >= Double.parseDouble(minOrderValue)) {
                return true;
            } else {
                return false;
            }
        }else{
            return true;
        }
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

    public void QTYDialog(String text){
        new AlertDialog.Builder(Order_Search.this)
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

   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("onWindowFocusChanged",""+hasFocus);
        if (hasFocus) {
            View lFocused = getCurrentFocus();
            if (lFocused != null)
                lFocused.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager lInputManager = (InputMethodManager) Order_Search.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        lInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }, 100);//Modified to 100ms to intercept SoftKeyBoard on Android 8 (Oreo) and hide it.
        }
    }
*/
}
