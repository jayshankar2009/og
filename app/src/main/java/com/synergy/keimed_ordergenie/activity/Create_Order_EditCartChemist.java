package com.synergy.keimed_ordergenie.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.adapter.ad_AutocompleteCustomArray;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.database.MasterPlacedOrder;
import com.synergy.keimed_ordergenie.database.StockistProducts;
import com.synergy.keimed_ordergenie.model.m_CartData;
import com.synergy.keimed_ordergenie.utils.BadgeDrawable;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.CustomAutoCompleteView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_CHEMIST_PRODUCT_DATA;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_LAST_DATA_SYNC;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class Create_Order_EditCartChemist extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    ArrayList<StockistProducts> posts = new ArrayList<StockistProducts>();
    List<StockistProducts> sreach_product_list = new ArrayList<StockistProducts>();
    List<StockistProducts> new_sreach_product_list = new ArrayList<StockistProducts>();
    private SharedPreferences pref;
    private SQLiteHandler db;
    Boolean Clicked_cart = false;
    String delivery_option;
    //ProgressDialog progressDialog;
    AlertDialog alertDialog1;
    CharSequence[] values = {" PickUp ", " Delivery "};
    Boolean isEditable = false;
    String legend_mode, legendName;
    public static final String CHEMIST_STOCKIST_ID = "chemist_stockist_id";
    public static final String CHEMIST_STOCKIST_NAME = "chemist_stockist_name";
    List<m_CartData> cart;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    SimpleDateFormat dateFormat_sync = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private Snackbar snackbar;
    private BottomSheetBehavior behavior;
    private String Doc_Id, Stockist_id, client_id;
    private Boolean Cart_Id_available = false;
    private Menu mToolbarMenu;
    private Integer n_product_cart_count;
    AppController globalVariable;
    private String legend_data;
    // @BindView(R.id.autoCompleteTextView)
    CustomAutoCompleteView _autoCompleteTextView;
    private EditText editText;

    ad_AutocompleteCustomArray adpter;
    private ChemistCartDao chemistCartDao;

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

    @OnClick(R.id.fab)
    void onclickfab(View view) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            get_CartData();

            // _fab.setVisibility(View.GONE);

        }
    }


    int Qty = 1;
    Integer type;
    String vPack, Itemname, Pack, MfgName, DoseForm, Sche, Remark, Itemcode, MfgCode, ProductId, UOM;
    Float vMrp, vRate;
    DaoSession daoSession;
    String login_type;

    private List<ChemistCart> chemistCartList;
    float orderAmount;


    /* Stockist Name */
    private String stockistName;
    private String accessKey;

    /* Edit Quantity Cart */
    private RecyclerView.Adapter<BindingViewHolder> cartAdapter;
    private int editedQuantity = 0;

    /* Current Date */
    private String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_create__order);
        //ActiveAndroid.initialize(configurationBuilder.create());

        ButterKnife.bind(this);
        db = new SQLiteHandler(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        globalVariable = (AppController) getApplicationContext();

        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        editText = (EditText) findViewById(R.id.edit_comment);

        _autoCompleteTextView = (CustomAutoCompleteView) findViewById(R.id.autoCompleteTextView);
        // _autoCompleteTextView.setThreshold(1);

        final TextView mfg = (TextView) findViewById(R.id.mfg);
        //final TextView mfg=(TextView)mContext.getApplicationInfo().findViewById(R.id.mfg);

        final TextView Dosefrom = (TextView) findViewById(R.id.Doseform);
        final TextView Packsize = (TextView) findViewById(R.id.Packsize);

        final TextView PTR = (TextView) findViewById(R.id.PTR);
        final TextView MRP = (TextView) findViewById(R.id.MRP);
        final TextView Scheme = (TextView) findViewById(R.id.Scheme);

        behavior = BottomSheetBehavior.from(_bottumLayout);

        /* Get Intent */
        stockistName = getIntent().getStringExtra(CHEMIST_STOCKIST_NAME);
        Stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
        client_id = pref.getString(CLIENT_ID, "");
        accessKey = pref.getString("key", "");
        //Log.d("accessKey", accessKey);


        //get_stockist_legends();


        Cursor cursor = db.getChemistProductListByStockist(Stockist_id);
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            if (Utility.internetCheck(this)) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(Stockist_id);
                jsonArray.put(1);

                /* For Legend Data */
                MakeWebRequest.MakeWebRequest("out_array", AppConfig.POST_GET_STOCKIST_LEGENDS, AppConfig.POST_GET_STOCKIST_LEGENDS,
                        jsonArray, this, false, "");
                /* For Products */
                productListAPI();
                /* Updated Product List API */
                new checkInternetConnection().execute(0);
            } else {
                Toast.makeText(this, "Products are not available for this Stockist", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            getOfflineProductList();
        }

        init();


    /*    _autoCompleteTextView.addTextChangedListener(new TextWatcher() {
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
        });*/

        _autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //Student selected = (Student) arg0.getAdapter().getItem(arg2);
                //arg1.getTag(R.id.key_product_Mfg);
                //String fmgname=(String)_autoCompleteTextView.getTag(R.id.key_product_Mfg);
                _addProduct.setEnabled(true);
                _addProduct.setClickable(true);
                _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
                _Qty.requestFocus();
                _Qty.setText("");
                try {
                    mfg.setText(String.valueOf(arg1.getTag(R.id.key_product_Mfg)));

                    _autoCompleteTextView.setText(String.valueOf(arg1.getTag(R.id.key_product_Name)));

                    Dosefrom.setText(String.valueOf(arg1.getTag(R.id.key_product_Dose)));

                    if (String.valueOf(arg1.getTag(R.id.key_product_Pack)) == null || String.valueOf(arg1.getTag(R.id.key_product_Pack)).equals("null")) {
                        Packsize.setText("---");
                    } else {
                        Packsize.setText(String.valueOf(arg1.getTag(R.id.key_product_Pack)));
                    }

                    if (String.valueOf(arg1.getTag(R.id.key_product_Scheme)) == null || String.valueOf(arg1.getTag(R.id.key_product_Scheme)).equals("null")) {
                        Scheme.setText("---");
                    } else {
                        Scheme.setText(String.valueOf(arg1.getTag(R.id.key_product_Scheme)));
                    }


                    PTR.setText(String.valueOf(arg1.getTag(R.id.key_product_PTR)));
                    MRP.setText(String.valueOf(arg1.getTag(R.id.key_product_MRP)));
                    Itemcode = (arg1.getTag(R.id.key_product_ItemCode).toString());
                    MfgCode = (arg1.getTag(R.id.key_product_MfgCode).toString());
                    // type = Integer.parseInt(arg1.getTag(R.id.key_product_Type).toString());
                    type = 0;
                    Itemname = arg1.getTag(R.id.key_product_Name).toString();
                    Pack = (arg1.getTag(R.id.key_product_Pack) == null) ? "NA" : arg1.getTag(R.id.key_product_Pack).toString();
                    MfgName = arg1.getTag(R.id.key_product_Mfg).toString();
                    DoseForm = arg1.getTag(R.id.key_product_Dose).toString();
                    Sche = arg1.getTag(R.id.key_product_Scheme).toString();

                    if (!arg1.getTag(R.id.key_product_PTR).equals("null")) {
                        vRate = Float.parseFloat(arg1.getTag(R.id.key_product_PTR).toString());
                    } else {
                        vRate = (float) 0;
                    }
                    if (!arg1.getTag(R.id.key_product_MRP).equals("null")) {
                        vMrp = Float.parseFloat(arg1.getTag(R.id.key_product_MRP).toString());
                    } else {
                        vMrp = (float) 0;
                    }
                    //***************pack in cart************************//
                    if (!arg1.getTag(R.id.key_product_Pack).equals("null")) {
                        vPack = arg1.getTag(R.id.key_product_Pack).toString();
                        //   vPack = Float.parseFloat(arg1.getTag(R.id.key_product_Pack).toString());
                    } else {
                        vPack = "0";
                    }

                    //***************pack in cart************************//
                    if (!arg1.getTag(R.id.key_product_Dose).equals("null")) {
                        UOM = arg1.getTag(R.id.key_product_Dose).toString();
                    }

                    if (!arg1.getTag(R.id.key_product_stock).equals("null")) {
                        // setStockLegend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                        set_stock_color_legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                    }

                    ProductId = arg1.getTag(R.id.key_product_code).toString();
                    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    // UOM = arg1.getTag(R.id.key_product_Dose).toString();

                    //  stock.setText(arg1.getTag(R.id.key_product_stock).toString());
                    //  set_stock_color_legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                    //  setStock
                    // Legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //commemnt by apurva
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(_autoCompleteTextView.getWindowToken(), 0);
            }
        });

        //btnminus Button click Events

        _btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                // int num1;
                try {


                    String value = _Qty.getText().toString();
                    if (!value.isEmpty()) {
                        int num1 = Integer.parseInt(value);
                        if (num1 > 1) {
                            Qty = num1 - 1;
                        }
                    }

                    _Qty.setText(String.valueOf(Qty));
                } catch (Exception e) {

                }

            }
        });
        // Log.d("enterValue11", String.valueOf(Qty));
        //btnplus Button click Events

        _btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

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




        /* Add Button Click */
        _addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    _addProduct.setEnabled(false);
                    _addProduct.setClickable(false);
                    _addProduct.setBackgroundColor(Color.parseColor("#d0ebfa"));
                    //         Log.d("enterValue13", String.valueOf(Qty));
                    if (Itemcode != null && ProductId != null) {

                        /*Log.d("itemCodeCheck", Itemcode);
                        Log.d("productCodeCheck", ProductId);*/
                        if (_autoCompleteTextView.getText().toString().isEmpty()) {
                            OGtoast.OGtoast("Please select a product", Create_Order_EditCartChemist.this);
                        } else {
//                            if (_Qty.getText().length() == 0) {
//                                OGtoast.OGtoast("Please select quantity", Create_Order.this);
//
//                            } else {
                            if (_Qty.getText().toString().equals("")) {
                                Qty = 1;
                            } else {
                                Qty = Integer.parseInt(_Qty.getText().toString());
                            }

                            addItemCart();

                      /*  if (Cart_Id_available) {
                            db.insert_into_chemist_cart_details(Doc_Id, Itemcode, ProductId, Qty, UOM, vRate.toString(), price.toString(),
                                    vMrp.toString(),
                                    dateFormat.format(Calendar.getInstance().getTime()));


                            price = price + db.get_total_order_amount(Doc_Id);
                            Integer item_count = (db.get_total_order_item_count(Doc_Id)) + 1;
                            _OrderAmt.setText(" Rs." + price.toString());
                            db.update_into_chemist_cart(Doc_Id, item_count, price.toString());


                            if (mToolbarMenu != null) {
                                createCartBadge(item_count);
                            }*/

                            // } else {
                           /* Log.e("status", "hiiiiiiiii222222222222i");
                            db.insert_into_chemist_cart(Doc_Id, call_plan_customer_id, price.toString(),
                                    dateFormat.format(Calendar.getInstance().getTime()),
                                    1, "cart", 0, 0);

                            db.insert_into_chemist_cart_details(Doc_Id, Itemcode, ProductId, Qty, UOM, vRate.toString(), price.toString(),
                                    vMrp.toString(),
                                    dateFormat.format(Calendar.getInstance().getTime()));
                            _OrderAmt.setText(" Rs." + price.toString());

                            if (mToolbarMenu != null) {
                                createCartBadge(1);
                            }
                            Cart_Id_available = true;*/
                            // }
                            //--Clearing TextView data after Adding to Cart---
                            mfg.setText("");
                            _autoCompleteTextView.setText("");
                            _autoCompleteTextView.requestFocus();
                            Dosefrom.setText("");
                            Packsize.setText("");
                            PTR.setText("");
                            MRP.setText("");
                            Scheme.setText("");
                            Qty = 1;
                            _Qty.setText("");
                            Itemcode = null;
                            ProductId = null;
                            hideKeyboard();
                            stock.setText("");
                            //stock.getBackground().clearColorFilter();
                            stock.setBackgroundColor(Color.WHITE);
                            // OGtoast.OGtoast("Product added to cart succesfully", Create_Order_Salesman.this);
                            //  Snackbar snackbar = Snackbar
                            //   .make(v, "Product added to cart succesfully", Snackbar.LENGTH_LONG);
                            View view = snackbar.getView();
                            //     snackbar.setAction("Action", null).show();
                            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            params.gravity = Gravity.TOP;
                            view.setLayoutParams(params);
                            // Snackbar.make(getView(), "Snackbar", Snackbar.LENGTH_LONG)
                            snackbar.show();

                            //          Log.d("enterValue15", String.valueOf(Qty));
                            // }
                        }
                    } else {
                        OGtoast.OGtoast("Please select a product", Create_Order_EditCartChemist.this);
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


                if (chemistCartList != null && chemistCartList.size() > 0) {

                    new AlertDialog.Builder(Create_Order_EditCartChemist.this)
                            .setTitle("Order")
                            .setMessage("Do you wish to confirm your order?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    delivery_options();
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


                } else {
                    OGtoast.OGtoast("Cart is empty", Create_Order_EditCartChemist.this);
                }
            }
        });

    }


    /* Current Date */
    private void currentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDateFormat.format(calendar.getTime());
    }


    /* onStart */
    @Override
    protected void onStart() {
        super.onStart();
        /* Increase Display Off Time */     //-- This is because of Load Product List in SQLite
        android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 300000);
    }


    /* */
    private void init() {
        Stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
        client_id = pref.getString(CLIENT_ID, "");

        /*Log.d("chemistId", "");
        Log.d("Stockist_id", Stockist_id);
        Log.d("client_id", client_id);*/

        daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();
        chemistCartList = chemistCartDao.getCartItem(Stockist_id, false);
        if (chemistCartList != null) {
            n_product_cart_count = chemistCartList.size();
        }
        //get_stockist_inventory(client_id);

        get_product_data_on_stockist();

        if (n_product_cart_count > 0) {
            for (int i = 0; i < chemistCartList.size(); i++) {
                orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
            }
        }
        _OrderAmt.setText("Rs." + String.format("%.2f", orderAmount));
        // get_ProductList_json();
    }



    /* Add Item Into Cart */
    private void addItemCart() {
        // sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;

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
        //chemistCart.setStatus(false);
        //chemistCart.
        //chemistCart.se

//        price += orderAmount;
//        orderAmount = price;

        chemistCartDao.insertOrUpdateCart(chemistCart, false);
        chemistCartList.clear();
        chemistCartList = chemistCartDao.getCartItem(Stockist_id, false);
        n_product_cart_count = chemistCartList.size();
        createCartBadge(n_product_cart_count);
        adpter.notifyDataSetChanged();


        orderAmount = 0;
        for (int i = 0; i < chemistCartList.size(); i++) {
            orderAmount += Double.valueOf(chemistCartList.get(i).getAmount());
        }
        _OrderAmt.setText("Rs." + String.format("%.2f", orderAmount));

        // createCartBadge(chemistCartDao.ge);
        //Log.v("GreenDao", chemistCart.getId().toString());

        if (Qty == 1) {
            OGtoast.OGtoast("one quantity added", Create_Order_EditCartChemist.this);
        } else {
            OGtoast.OGtoast("Product added to cart succesfully", Create_Order_EditCartChemist.this);
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
     //   menu.findItem(R.id.action_inventory).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart:
                hideKeyboard();
                Clicked_cart = true;
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    if (chemistCartList.size() > 0) {
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

    @Override
    public void onResume() {
        super.onResume();
        //get_ProductList_json();
        //*************

        //************
        get_cart_id();
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    private void get_ProductList_json() {
       /* //LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Product_list.json", Create_Order.this);
        //String jsondata = _LoadJsonFromAssets.getJson();
        String jsondata = ConstData.get_jsonArray_from_cursor(db.get_stockist_inventory()).toString();//_LoadJsonFromAssets.getJson();
        if (!jsondata.isEmpty()) {
            Log.e("jsondata", jsondata);
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            posts = new ArrayList<StockistProducts>();
            sreach_product_list = Arrays.asList(mGson.fromJson(jsondata, m_Product_list[].class));
            posts = new ArrayList<m_Product_list>(sreach_product_list);

            adpter = new ad_AutocompleteCustomArray(this, posts);
            _autoCompleteTextView.setAdapter(adpter);
            // fill_stockist();


        }*/

        if (daoSession.getStockistProductsDao().getStockistProducts(Stockist_id).size() > 0) {
            new_sreach_product_list = daoSession.getStockistProductsDao().getStockistProducts(Stockist_id);
            //   Log.d("List",String.valueOf(new_sreach_product_list.size()));
            //  Log.d("List",new_sreach_product_list.toString());
        }

        posts = new ArrayList<>();
        if (sreach_product_list.size() == 0) {
            sreach_product_list = daoSession.getStockistProductsDao().getStockistProducts(Stockist_id);
        }
        posts = new ArrayList<>(sreach_product_list);
        adpter = new ad_AutocompleteCustomArray(this, posts, legend_data);
        _autoCompleteTextView.setAdapter(adpter);
    }

    public void filter(String charText) {
      /*  if(arraylist.size()<1) {
            this.arraylist.addAll(alternativeitems);
        }*/
        sreach_product_list.clear();
        if (charText.length() == 0) {
//            charText = "a";
//            Toast.makeText(getApplicationContext(),"chartext"+charText.toString(),Toast.LENGTH_SHORT).show();
//            // alternativeitems.addAll(arraylist);
        } else {
            for (StockistProducts wp : posts) {
                if (wp.getItemname().toUpperCase()
                        .contains(charText.toUpperCase())) {
                    sreach_product_list.add(wp);
                } else if (wp.getItemname()
                        .contains(charText)) {
                    sreach_product_list.add(wp);
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


    /* Show Cart Items */
    private void fill_Cartdata() {
        cartAdapter = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(Create_Order_EditCartChemist.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.adpter_cartdata_list_new, parent, false);
                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                final ChemistCart chemistCart = chemistCartList.get(position);
                // Log.e("Cart_list", Cart_list.toString());
                holder.getBinding().setVariable(BR.v_cartDatalist, chemistCart);
                holder.getBinding().executePendingBindings();

                /* Quantity Click */
                holder.getBinding().getRoot().findViewById(R.id.edit_quantity).setOnClickListener(new View.OnClickListener() {
                    //                    @Override
//                    public void onClick(View v) {
                    //holder.getBinding().getRoot().findViewById(R.id.linear_quantity_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editQuantityDialog(chemistCartList.get(position).getStockist_Client_id(), chemistCartList.get(position).getProduct_ID(),
                                chemistCartList.get(position).getItemname(), chemistCartList.get(position).getRate(),
                                chemistCartList.get(position).getQty());
                    }
                });

                /* Cancel Click */
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
                        _autoCompleteTextView.setText(chemistCartList.get(position).getItemname());
                        _autoCompleteTextView.requestFocus();

                        // Dosefrom.setText(chemistCartList.get(position).get());
//                        Packsize.setText(chemistCartList.get(position).getPACK());
//                        PTR.setText(chemistCartList.get(position).getRate());
//                        MRP.setText(chemistCartList.get(position).getMRP());
                        _Qty.setText(chemistCartList.get(position).getQty());
                        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }

                        // _OrderAmt.setText(chemistCartList.get(position).getAmount());

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
                    Clicked_cart = false;
                }
            }
        });
    }


    /* Edit Quantity in Cart */
    private void editQuantityDialog(final String stockistClientId, final String productId, String item, final String ptr, final String quantity) {
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

        /* Minus Click */
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editedQuantity > 1) {
                    editedQuantity = Integer.valueOf(editTextQuantity.getText().toString()) - 1;
                    editTextQuantity.setText(String.valueOf(editedQuantity));
                } else {
                    Toast.makeText(Create_Order_EditCartChemist.this, "Min. quantity should be 1", Toast.LENGTH_SHORT).show();
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
                int quantity = 0;
                Float price = null;
                if (editTextQuantity.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Create_Order_EditCartChemist.this, "Min. quantity should be one", Toast.LENGTH_SHORT).show();
                } else {
                    quantity = Integer.valueOf(editTextQuantity.getText().toString());
                    price = Float.valueOf(ptr) * quantity;
                }
//
//                ChemistCart chemistCart = new ChemistCart();
//                chemistCart.setStockist_Client_id(stockistClientId);
//                chemistCart.setProduct_ID(productId);
//                chemistCart.setQty(String.valueOf(quantity));
//                chemistCart.setPrice(String.valueOf(price));
//                chemistCart.setAmount(String.valueOf(price));

                ChemistCart chemistCart = new ChemistCart();
                chemistCart.setDOC_ID(Doc_Id);
                chemistCart.setItems(Itemcode);
                chemistCart.setItemname(Itemname);
                chemistCart.setProduct_ID(productId);
                chemistCart.setQty(String.valueOf(quantity));
                chemistCart.setUOM(UOM);
                chemistCart.setRate(vRate.toString());
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


    /* Cart Section */
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


            if (Doc_Id == null) {
                Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
                ;
                Cart_Id_available = false;

            }
        }

    }


    /* Delivery Option Dialog */
    private void delivery_options() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Create_Order_EditCartChemist.this);
        builder.setTitle("Select Delivery Options");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        delivery_option = "PickUp";
                        break;
                    case 1:
                        delivery_option = "Delivery";
                        break;
//                case 2:
//                    delivery_option="Others";
//                    Toast.makeText(Create_Order.this, delivery_option, Toast.LENGTH_LONG).show();
//                    break;
                }
                confirm_order();
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();


        //**************
//    new AlertDialog.Builder(Create_Order.this)
//            .setTitle("Delivery Options")
//            .setMessage("Please select delivery options to place order.")
//            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                  //  delivery_options();
//
//                    confirm_order();
//                    dialog.dismiss();
//
//                }
//            })
//            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int i) {
//                    dialog.dismiss();
//                }
//            })
//            .show();


    }

    private void confirm_order() {
        try {

            JSONArray main_j_array = new JSONArray();
            JSONObject main_j_object = new JSONObject();
            JSONArray line_j_array = new JSONArray();
            main_j_object.put("Transaction_No", "");
            main_j_object.put("Client_ID", Integer.parseInt(client_id));
            main_j_object.put("CreatedBy", Integer.parseInt(client_id));
            //comment by apurva
            //  main_j_object.put("Comments", editText.getText().toString());
            ChemistCart chemistCart = chemistCartList.get(0);
            // Cursor crs_cart = db.get_chemist_cart(Stockist_id);

            if (chemistCartList.size() > 0) {
                //   while (crs_cart.moveToNext()) {

                main_j_object.put("DOC_NO", chemistCart.getDOC_ID());
                main_j_object.put("Doc_Date", chemistCart.getCreatedon());
                main_j_object.put("Stockist_Client_id", Stockist_id);
                main_j_object.put("Remarks", chemistCart.getRemarks());
                main_j_object.put("Items", chemistCartList.size());
                main_j_object.put("Amount", orderAmount);
                main_j_object.put("Status", 0);
                main_j_object.put("Createdon", chemistCart.getCreatedon());
                //**********
                main_j_object.put("Delivery_Option", delivery_option);
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
                if (Utility.internetCheck(this)) {

                } else {
                    line_j_object.put("itemName", chemistCart.getItemname());
                    line_j_object.put("packSize", String.valueOf(chemistCart.getPACK()));
                }
                line_j_object.put("Product_ID", chemistCart.getProduct_ID());
                line_j_object.put("Qty", Integer.parseInt(chemistCart.getQty()));
                line_j_object.put("UOM", chemistCart.getUOM());
                line_j_object.put("Rate", Double.valueOf(chemistCart.getRate()));
                line_j_object.put("Price", Double.valueOf(chemistCart.getPrice()));
                line_j_object.put("MRP", Double.valueOf(chemistCart.getMRP()));
                line_j_object.put("Createdon", chemistCart.getCreatedon());
                line_j_object.put("CreatedBy", Integer.parseInt(Stockist_id));
                line_j_object.put("Delivery_Option", delivery_option);

                //   Log.d("PRINTMRPP11", crs_cart_details.getString(crs_cart_details.getColumnIndex("MRP")));
                //   Log.d("PRINTMRPP12", String.valueOf(crs_cart_details.getDouble(crs_cart_details.getColumnIndex("MRP"))));

                line_j_array.put(line_j_object);

                //  } while (crs_cart_details.moveToNext());
                // }
            }

            main_j_array.put(main_j_object);
            main_j_array.put(line_j_array);
            Log.d("OrderData", String.valueOf(main_j_array));

            MasterPlacedOrder masterPlacedOrder = new MasterPlacedOrder();
            masterPlacedOrder.setJson(main_j_array.toString());
            masterPlacedOrder.setCustomerID(client_id);
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
                db.insertOrder(client_id, client_id, Stockist_id, Stockist_id, stockistName, chemistCart.getDOC_ID(),
                        chemistCart.getCreatedon(), chemistCart.getRemarks(), String.valueOf(chemistCartList.size()),
                        String.valueOf(orderAmount), "0", chemistCart.getCreatedon(), delivery_option, line_j_array.toString(), "");
            }

            //db.insert_into_chemist_order_sync(Doc_Id, main_j_array.toString(), 0);
            // db.delete_chemist_Cart(Doc_Id);
            // db.delete_chemist_Cart_Details(Doc_Id);

            order_confirmed_dialog(Doc_Id);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /* Stockist Product Data */
    void get_product_data_on_stockist() {
        _txt_customer_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
        client_id = pref.getString(CLIENT_ID, "");
       /* if (db.check_stockist_data(Stockist_id) > 0) {
        } else {*/
//comment by apurva
        // progress.setVisibility(View.VISIBLE);
        SharedPreferences.Editor edt = pref.edit();

        /* Old Code */
        /*MakeWebRequest.MakeWebRequest("get", GET_CHEMIST_PRODUCT_DATA,
                GET_CHEMIST_PRODUCT_DATA + "[" + client_id + "," + Stockist_id + "]", this, false);*/

        edt.putString(CHEMIST_LAST_DATA_SYNC, dateFormat_sync.format(Calendar.getInstance().getTime()));
        edt.commit();
        //String proapi = AppConfig.GET_CHEMIST_PRODUCT_DATA + "[" + client_id + "," + Stockist_id + "]";
        //System.out.println("chemist product api"+proapi);
        //   }
    }


    /* Chemist Product List API */
    private void productListAPI() {
        Utility.showSyncDialog(this);
        String url = GET_CHEMIST_PRODUCT_DATA + "[" + client_id + "," + Stockist_id + "]";
        Log.d("chemistProUrl", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("productList", "" + response.length());
                if (response != null) {
                    ArrayList<StockistProducts> posts = new ArrayList<>();
                    /*Cursor cursor = db.getChemistProductListByStockist(Stockist_id);
                    int cursorCount = cursor.getCount();
                    if (cursorCount > 0) {
                        db.deleteRecordFromChemistProduct(Stockist_id);
                    }*/

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject j_obj = response.getJSONObject(i);
                            StockistProducts stockistProducts = new StockistProducts();
                            stockistProducts.setProduct_ID(j_obj.getString("Product_ID"));
                            stockistProducts.setItemcode(j_obj.getString("Itemcode"));
                            stockistProducts.setStockist_id(j_obj.getString("Stockist_id"));
                            stockistProducts.setItemname(j_obj.getString("Itemname"));
                            stockistProducts.setPacksize(j_obj.getString("Packsize"));
                            stockistProducts.setMRP(j_obj.getString("MRP"));
                            stockistProducts.setRate(j_obj.getString("Rate"));
                            stockistProducts.setStock(j_obj.getString("stock"));
                            stockistProducts.setMfgCode(j_obj.getString("MfgCode"));
                            stockistProducts.setMfgName(j_obj.getString("MfgName"));
                            stockistProducts.setDoseForm(j_obj.getString("DoseForm"));
                            stockistProducts.setScheme(j_obj.getString("Scheme"));
                            stockistProducts.setType("0");

                            posts.add(stockistProducts);

                            db.insertIntoChemistProductListByStockist(stockistProducts.getItemcode(), stockistProducts.getStockist_id(), stockistProducts.getProduct_ID(),
                                    stockistProducts.getItemname(), stockistProducts.getPacksize(), stockistProducts.getMRP(), stockistProducts.getRate(),
                                    stockistProducts.getStock(), stockistProducts.getType(), stockistProducts.getMfgCode(), stockistProducts.getMfgName(),
                                    stockistProducts.getDoseForm(), stockistProducts.getScheme());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    daoSession.getStockistProductsDao().deleteAll();
                    daoSession.getStockistProductsDao().insertInTx(posts);
                    sreach_product_list = posts;
                    get_ProductList_json();
                    Utility.hideSyncDialog();
                    if (response != null) {
                        Toast.makeText(Create_Order_EditCartChemist.this, "" + response.length() + " Products added successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideSyncDialog();
                Toast.makeText(Create_Order_EditCartChemist.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            // Log.e("Response", response.toString());
            try {
                /*if (f_name.equals(GET_CHEMIST_PRODUCT_DATA)) {
                    new LoadData(response).execute();
                }*/
                if (f_name.equals(AppConfig.POST_GET_STOCKIST_LEGENDS)) {
                    legend_data = response.toString();
                    Log.d("legendDataCheck", legend_data);
                    SharedPref.saveLegendData(this, legend_data);
                }
            } catch (Exception e) {
            }
        } else {
            //   Log.d("Response","NUll");
//            if (f_name.equals(AppConfig.GET_CHEMIST_PRODUCT_DATA)) {
//                get_ProductList_json();
//            }
        }

    }


    /* Confirmed Order Dialog */
    void order_confirmed_dialog(String doc_number) {
        new AlertDialog.Builder(Create_Order_EditCartChemist.this)
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


    /* Load Data Service */
    public class LoadData extends AsyncTask<Void, Void, Void> {
        JSONArray response;

        public LoadData(JSONArray response) {
            this.response = response;
        }
        //declare other objects as per your need

        @Override
        protected void onPreExecute() {
            // progressDialog = ProgressDialog.show(Create_Order.this, "Please Wait", "Loading.... ", true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            insert_inventory_products(response);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // progress.setVisibility(View.GONE);
            //progressDialog.dismiss();
            get_ProductList_json();
        }
    }


    /* Show Badge */
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

            new AlertDialog.Builder(Create_Order_EditCartChemist.this)
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
            OGtoast.OGtoast("Cart is empty", Create_Order_EditCartChemist.this);
        }
    }


    /* onBack Pressed */
    @Override
    public void onBackPressed() {
        // It's expensive, if running turn it off.
        // DataHelper.cancelSearch();
        hideKeyboard();
        // daoSession.getStockistProductsDao().deleteAll();
        super.onBackPressed();
    }


    /* Get Inventory Products */
    void insert_inventory_products(JSONArray response) {

/*
        String jsondata = response.toString();
        GsonBuilder builder = new GsonBuilder();
        Gson mGson = builder.create();
        Type listType = new TypeToken<List<StockistProducts>>() {
        }.getType();
        posts = mGson.fromJson(jsondata, listType);*/
        JSONObject j_obj;
        StockistProducts stockistProducts;
        ArrayList<StockistProducts> posts = new ArrayList<StockistProducts>();

        Cursor cursor = db.getChemistProductListByStockist(Stockist_id);
        int cursorCount = cursor.getCount();
        //Log.d("existCursorCount", ""+cursorCount);
        if (cursorCount > 0) {
            db.deleteRecordFromChemistProduct(Stockist_id);
        }

        try {
            for (int i = 0; i < response.length(); i++) {
                j_obj = response.getJSONObject(i);
                stockistProducts = new StockistProducts();
                stockistProducts.setProduct_ID(j_obj.getString("Product_ID"));
                stockistProducts.setItemcode(j_obj.getString("Itemcode"));
                stockistProducts.setStockist_id(j_obj.getString("Stockist_id"));
                stockistProducts.setItemname(j_obj.getString("Itemname"));
                stockistProducts.setPacksize(j_obj.getString("Packsize"));
                stockistProducts.setMRP(j_obj.getString("MRP"));
                stockistProducts.setRate(j_obj.getString("Rate"));
                stockistProducts.setStock(j_obj.getString("stock"));
                stockistProducts.setMfgCode(j_obj.getString("MfgCode"));
                stockistProducts.setMfgName(j_obj.getString("MfgName"));
                stockistProducts.setDoseForm(j_obj.getString("DoseForm"));
                stockistProducts.setScheme(j_obj.getString("Scheme"));
                stockistProducts.setType("0");
                posts.add(stockistProducts);

                //daoSession.getStockistProductsDao().insert(stockistProducts);
                /*db.inset_into_stockist_wise_products(stockistProducts.getItemcode(), stockistProducts.getStockist_id(), stockistProducts.getProduct_ID(),
                        stockistProducts.getItemname(), stockistProducts.getPacksize(), stockistProducts.getMRP(), stockistProducts.getRate(),
                        stockistProducts.getStock(), stockistProducts.getType(), stockistProducts.getMfgCode(), stockistProducts.getMfgName(),
                        stockistProducts.getDoseForm(), stockistProducts.getScheme());*/

                db.insertIntoChemistProductListByStockist(stockistProducts.getItemcode(), stockistProducts.getStockist_id(), stockistProducts.getProduct_ID(),
                        stockistProducts.getItemname(), stockistProducts.getPacksize(), stockistProducts.getMRP(), stockistProducts.getRate(),
                        stockistProducts.getStock(), stockistProducts.getType(), stockistProducts.getMfgCode(), stockistProducts.getMfgName(),
                        stockistProducts.getDoseForm(), stockistProducts.getScheme());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        daoSession.getStockistProductsDao().deleteAll();
        //daoSession.getStockistProductsDao().deleteStockistProducts(Stockist_id);
        daoSession.getStockistProductsDao().insertInTx(posts);
        sreach_product_list = posts;
    }


    private void get_stockist_inventory(String Client_id) {
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INVENTORY,
                AppConfig.GET_STOCKIST_INVENTORY + Client_id, this, true);
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

        /*Log.e("itme_no", itme_no);
        db.delete_product_from_cart_chemist_Cart_Details(createdOn);

        price = db.get_total_order_amount(Doc_Id) - price;*/
        // Integer item_count = (db.get_total_order_item_count(Doc_Id));
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

        OGtoast.OGtoast("Product removed succesfully", Create_Order_EditCartChemist.this);

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        //   get_stockist_legends();
        adpter.notifyDataSetChanged();
    }

    /*void delete_product_from_cart(String itme_no, Float price) {

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

        OGtoast.OGtoast("Product removed succesfully", Create_Order.this);

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
     //   get_stockist_legends();
    }*/




    /* Product List API */
    /*void get_stockist_legends() {
        if (Utility.internetCheck(this)) {
            try {
//            Stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
//
//            legend_data = ConstData.get_jsonArray_from_cursor(db.get_legend_data(Stockist_id)).toString();

                Stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
                JSONArray j_arr= new JSONArray();
                j_arr.put(Stockist_id);

                //progressDialog = ProgressDialog.show(Create_Order.this, "Please Wait", "Loading Products.... ", true);
                MakeWebRequest.MakeWebRequest("out_array", AppConfig.POST_GET_STOCKIST_LEGENDS, AppConfig.POST_GET_STOCKIST_LEGENDS,
                        j_arr  , this, false,"");

            } catch (Exception e) {

                e.toString();
            }
        } else {
            getOfflineProductList();
        }
    }*/


    /* Get Offline ProductList */
    private void getOfflineProductList() {
        ArrayList<StockistProducts> arrayListStockistProduct = new ArrayList<>();
        Cursor cursor = db.getChemistProductListByStockist(Stockist_id);
        if (cursor.moveToFirst()) {
            do {
                StockistProducts stockistProducts = new StockistProducts();
                stockistProducts.setProduct_ID(cursor.getString(3));
                stockistProducts.setItemcode(cursor.getString(1));
                stockistProducts.setStockist_id(cursor.getString(1));
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

                arrayListStockistProduct.add(stockistProducts);
            } while (cursor.moveToNext());
        }
        sreach_product_list = new ArrayList<>();
        sreach_product_list = arrayListStockistProduct;
        posts = new ArrayList<>(sreach_product_list);
        adpter = new ad_AutocompleteCustomArray(this, posts, SharedPref.getLegendData(this));
        _autoCompleteTextView.setThreshold(1);
        _autoCompleteTextView.setAdapter(adpter);
    }


    /* Set Stock Colors */
    void set_stock_color_legend(Integer Stock) {

        try {
            JSONArray j_arr = new JSONArray(SharedPref.getLegendData(this));
            for (int i = 0; i < j_arr.length(); i++) {
                JSONObject j_ob = j_arr.getJSONObject(i);

                if (Stock >= j_ob.getInt("StartRange") &&
                        Stock <= j_ob.getInt("EndRange")) {

                    String color_code = j_ob.getString("ColorCode");

                    legend_mode = j_ob.getString("Legend_Mode");
                    legendName = j_ob.getString("LegendName");

                    if (legend_mode.equals("3")) {
                        stock.setBackgroundColor(Color.parseColor(color_code));
                        stock.setText(legendName);
                    } else if (legend_mode.equals("2")) {
                        stock.setBackgroundColor(Color.parseColor(color_code));
                        stock.setText(String.valueOf(Stock));
                        stock.setTextColor(getResources().getColor(R.color.white));
                    } else if (legend_mode.equals("1")) {
                        stock.setBackgroundColor(Color.parseColor(color_code));
                        stock.setText(String.valueOf(Stock));
                        stock.setTextColor(getResources().getColor(R.color.white));
                    }


                    // stock.getBackground().setColorFilter(Color.parseColor(color_code), PorterDuff.Mode.MULTIPLY);

                    /*if (j_ob.getInt("LegendMode") == 1) {
                        stock.setText("");
                    } else if (j_ob.getInt("LegendMode") == 3) {

                        try {
                            if (Integer.parseInt(stock.getText().toString()) > 0) {
                                stock.setText("Yes");
                            } else {
                                stock.setText("No");
                            }

                        } catch (Exception e) {

                        }

                        stock.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
                    }*/
                }
            }

        } catch (Exception e) {

            e.toString();

        }


       /* try {
            JSONArray j_arr = new JSONArray(legend_data);

            for (int i = 0; i < j_arr.length(); i++) {
                JSONObject j_ob = j_arr.getJSONObject(i);

                if (Stock >= j_ob.getInt("StartRange") &&
                        Stock <= j_ob.getInt("EndRange")) {


                    String color_code = j_ob.getString("ColorCode");




                  //  stock.setBackgroundColor(Color.parseColor(color_code));


                    legend_mode=j_ob.getString("Legend_Mode");

                    legendName =j_ob.getString("LegendName");
                    // Log.d("legendNameThirty", legendName);


                    if (legend_mode.equals("3"))
                    {
                        Log.d("chemist13","print three ");
                        stock.setBackgroundColor(Color.parseColor(color_code));
                        stock.setText(legendName);
                    }
                    else if(legend_mode.equals("2"))
                    {
                        Log.d("chemist12","print two");
                        stock.setBackgroundColor(Color.parseColor(color_code));

                        stock.setText(String.valueOf(Stock));
                        stock.setTextColor(getResources().getColor(R.color.white));
                    }
                    else if (legend_mode.equals("1"))
                    {
                        Log.d("chemist11","print one");
                        stock.setBackgroundColor(Color.parseColor(color_code));
                    }



                    // stock.getBackground().setColorFilter(Color.parseColor(color_code), PorterDuff.Mode.MULTIPLY);

                    *//*if (j_ob.getInt("LegendMode") == 1) {
                        stock.setText("");
                    } else if (j_ob.getInt("LegendMode") == 3) {

                        try {
                            if (Integer.parseInt(stock.getText().toString()) > 0) {
                                stock.setText("Yes");
                            } else {
                                stock.setText("No");
                            }

                        } catch (Exception e) {

                        }

                        stock.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
                    }*//*
                }
            }

        } catch (Exception e) {

            e.toString();

        }*/


    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_autoCompleteTextView.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        // daoSession.getStockistProductsDao().deleteAll();
        super.onDestroy();
    }


    /*private void setStockLegend(int stockNum) {
        if (stockNum > 300) {

            stock.setBackgroundColor(Color.parseColor("#5371D7"));

        } else if (stockNum > 200) {

            stock.setBackgroundColor(Color.parseColor("#5CDA6D"));

        } else if (stockNum > 100) {

            stock.setBackgroundColor(Color.parseColor("#EFE040"));

        } else {
            stock.setBackgroundColor(Color.parseColor("#EF9940"));
        }
    }*/


    /* Internet Available Or Not */
    public static class InternetConnection {
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            if (activeNetworkInfo != null) { // connected to the internet
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


    /* Update Current Internet State */
    private class checkInternetConnection extends AsyncTask<Integer, Integer, Void> {

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
            if (InternetConnection.checkConnection(Create_Order_EditCartChemist.this)) {
                updatedProductList();
            }
            super.onPostExecute(result);

            //Do it again!
            if (Utility.internetCheck(Create_Order_EditCartChemist.this)) {

            }

        }
    }


    /* Updated Product List API */
    private void updatedProductList() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(Integer.valueOf(Stockist_id));
        jsonArray.put(currentDate);
        String url = AppConfig.GetUpdatedProductList + jsonArray;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject j_obj = response.getJSONObject(i);
                            String productId = j_obj.getString("Product_ID");
                            String itemCode = j_obj.getString("Itemcode");
                            String itemName = j_obj.getString("Itemname");
                            String packSize = j_obj.getString("Packsize");
                            String mrp = j_obj.getJSONArray("line_items").getJSONObject(0).getString("Mrp");
                            String rate = j_obj.getJSONArray("line_items").getJSONObject(0).getString("Rate");
                            String stock = j_obj.getString("Stock");
                            String mfgCode = j_obj.getJSONArray("line_items").getJSONObject(0).getString("MfgCode");
                            String mfgName = j_obj.getJSONArray("line_items").getJSONObject(0).getString("MfgName");
                            String doseFrom = j_obj.getString("DoseForm");
                            String scheme = j_obj.getString("Scheme");

                            db.updateProductListChemist(productId, itemCode, itemName, packSize, mrp, rate, stock, "0", mfgCode,
                                    mfgName, doseFrom, scheme);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Create_Order_EditCartChemist.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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

}
