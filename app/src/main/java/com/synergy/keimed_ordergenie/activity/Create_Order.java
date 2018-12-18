package com.synergy.keimed_ordergenie.activity;

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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import com.synergy.keimed_ordergenie.BR;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.adapter.ad_AutocompleteCustomArray_chemist;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
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
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.method.TextKeyListener.clear;
import static com.synergy.keimed_ordergenie.app.AppConfig.CHEMIST_PRODUCT_SEARCH;
import static com.synergy.keimed_ordergenie.app.AppConfig.GET_CHEMIST_PRODUCT_DATA;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_LAST_DATA_SYNC;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;
import java.util.Timer;
import java.util.TimerTask;
import java.util.LinkedList;
public class Create_Order extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {
    private Timer mTimer =new Timer();
    private final long DELAY = 5;
    private boolean isAvailable=true;

    List<m_product_list_chemist> posts = new ArrayList<m_product_list_chemist>();
    //List<m_product_list_chemist> sreach_product_list = new ArrayList<m_product_list_chemist>();
    List<StockistProducts> new_sreach_product_list = new ArrayList<StockistProducts>();
    private SharedPreferences pref;
    private  TextView txt_cust_name,txt_order_id,txt_total_items,txt_total,txt_comment,txt_deliveryoption;
    private SQLiteHandler db;
    Button Search_History;
    Boolean Clicked_cart = false;
    String delivery_option;
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
    private  EditText editText;

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

    /*  half scheme and percent scheme edit
    @BindView(R.id.txt_half_scheme)
    TextView txt_half_scheme;

    @BindView(R.id.txt_percentage)
    TextView txt_percentage;
*/
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
    String vPack,Itemname, Pack, MfgName, DoseForm, Sche, Remark, Itemcode, MfgCode, ProductId, UOM;
    Float vMrp, vRate;
    DaoSession daoSession;
    String login_type;

    private List<ChemistCart> chemistCartList;
    float orderAmount;
Integer oldlength=0;

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
        setContentView(R.layout.activity_create__order);
        //ActiveAndroid.initialize(configurationBuilder.create());

        ButterKnife.bind(this);
        db = new SQLiteHandler(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        globalVariable = (AppController) getApplicationContext();

        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        editText=(EditText)findViewById(R.id.edit_comment);

        txt_pending_amount=(TextView)findViewById(R.id.pending_count);
        /*txt_pending_countt=(TextView)findViewById(R.id.pending_amout);
        linearLayout1=(LinearLayout)findViewById(R.id.headerLayout1);
        linearLayout2=(LinearLayout)findViewById(R.id.headerLayout2);
        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);*/

        String amount=getIntent().getStringExtra("pending_amount");
        String count=getIntent().getStringExtra("pending_count");
/*
        txt_pending_amount.setText((amount== null) ? "NA" : "Rs."+amount);
        txt_pending_countt.setText((count== null) ? "NA" : count);
*/


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(_rv_Cartdatalist.getContext(),
                DividerItemDecoration.VERTICAL);
        _rv_Cartdatalist.addItemDecoration(dividerItemDecoration);

        _autoCompleteTextView = (CustomAutoCompleteView) findViewById(R.id.autoCompleteTextView);
        // _autoCompleteTextView.setThreshold(1);

        final TextView mfg = (TextView) findViewById(R.id.mfg);
        //final TextView mfg=(TextView)mContext.getApplicationInfo().findViewById(R.id.mfg);

        final TextView Dosefrom = (TextView) findViewById(R.id.Doseform);
        final TextView Packsize = (TextView) findViewById(R.id.Packsize);

        final TextView PTR = (TextView) findViewById(R.id.PTR);
        final TextView MRP = (TextView) findViewById(R.id.MRP);
        final TextView Scheme = (TextView) findViewById(R.id.Scheme);
        final TextView Halfscheme = (TextView) findViewById(R.id.txt_half_scheme);
        final TextView Percentscheme = (TextView) findViewById(R.id.txt_percentage);
        Button Search_History = (Button) findViewById(R.id.search_history);

//        @BindView(R.id.txt_half_scheme)
//        TextView txt_half_scheme;
//
//        @BindView(R.id.txt_percentage)

        //   txt_total_itemss=(TextView)findViewById(R.id.total_itemss);
        //  txt_order_value=(TextView)findViewById(R.id.order_value);

        behavior = BottomSheetBehavior.from(_bottumLayout);
        //get_stockist_legends();
        init();

        //  get_stockist_legends();

        // 13 june comment Line 293 - 319



//        _autoCompleteTextView.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                _autoCompleteTextView.showDropDown();
//                return false;
//            }
//        });
        _autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }


            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

//                String newText = String.valueOf(charSequence);
//                if(newText.length()>2)
//                {
//                    //posts.clear();
//                    getLoadproducts(newText);
//                }
//                else if(newText.length()==0)
//                {
//                    Toast.makeText(getApplicationContext(),"No Key",Toast.LENGTH_SHORT).show();;
//                }

            }

            @Override
            public void afterTextChanged(final Editable editable) {
               final String newText = editable.toString().trim();
               final int n_length;

                n_length = newText.length();

              if(newText.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"No Key",Toast.LENGTH_SHORT).show();;
                }
                else
                {
                    //posts.clear();
                    if (oldlength != n_length)
                    {
                        oldlength = n_length  ;
                        getLoadproducts(newText);
                    }
                }

              //*****
//                mTimer.cancel();
//                if (!newText.equals("")) {
//                    mTimer = new Timer();
//                    mTimer.schedule(
//                            new TimerTask() {
//                                @Override
//                                public void run() {
//                                    runOnUiThread(new TimerTask() {
//                                        @Override
//                                        public void run() {
//
//                                            getLoadproducts(newText);
//                                        }
//                                    });
//
//                                }
//                            },
//                            DELAY
//                    );
//                }
                //***
//                if(posts.size()>0) {
//                    filter(_autoCompleteTextView.getText().toString());
//                    adpter = new ad_AutocompleteCustomArray_chemist(Create_Order.this, sreach_product_list);
//                    _autoCompleteTextView.setAdapter(adpter);
//                }else
//                {
//                    get_ProductList_json();
//                }
             //  _autoCompleteTextView.setEnabled(true);
            }
        });



        _autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //Student selected = (Student) arg0.getAdapter().getItem(arg2);
                //arg1.getTag(R.id.key_product_Mfg);
               ProductId=arg1.getTag(R.id.key_product_ItemCode).toString();
                Log.d("produdtid",ProductId);

                _autoCompleteTextView.setText(String.valueOf(arg1.getTag(R.id.key_product_Name)));
                Log.d("produdtid1",_autoCompleteTextView.getText().toString());

                if (Utility.internetCheck(getApplicationContext())) {
                    getLoadproductsDetails(ProductId);
                    _addProduct.setEnabled(true);
                    _addProduct.setClickable(true);
                    _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
                    _Qty.requestFocus();
                    _Qty.setText("");
                    try {
                       // mfg.setText("NA");
                   //     _autoCompleteTextView.setText(String.valueOf(arg1.getTag(R.id.key_product_Name)));
                      //  Dosefrom.setText("NA");
                        if (posts.get(0).get_dform() == null || posts.get(0).get_dform().equals("null")) {
                            Dosefrom.setText("test");
                        } else {
                            Dosefrom.setText(posts.get(0).get_dform());
                        }
                        if (String.valueOf(arg1.getTag(R.id.key_product_Pack)) == null || String.valueOf(arg1.getTag(R.id.key_product_Pack)).equals("null")) {
                            Packsize.setText("---");
                        } else {
                            Packsize.setText(String.valueOf(arg1.getTag(R.id.key_product_Pack)));
                        }
                        if (String.valueOf(arg1.getTag(R.id.key_product_Mfg)) == null || String.valueOf(arg1.getTag(R.id.key_product_Mfg)).equals("null")) {
                            mfg.setText("NA");
                        } else {
                            mfg.setText(String.valueOf(arg1.getTag(R.id.key_product_Mfg)));
                        }

                        if (posts.get(0).get_schm()== null || posts.get(0).get_schm().equals("null")) {
                            Scheme.setText("NA");
                        } else {
                            Scheme.setText(posts.get(0).get_schm());
                        }
                        if (posts.get(0).get_ptr().toString()== null || posts.get(0).get_ptr().toString().equals("null")) {
                            PTR.setText("0.0");
                        } else {
                            PTR.setText(posts.get(0).get_ptr().toString());
                        }
                        if (posts.get(0).get_mrp().toString()== null || posts.get(0).get_mrp().toString().equals("null")) {
                            MRP.setText("0.0");
                        } else {
                            MRP.setText(posts.get(0).get_mrp().toString());
                        }

            Log.d("data","scheme--"+posts.get(0).get_schm()+"ptr--"+posts.get(0).get_ptr().toString()+"--mrp"+posts.get(0).get_mrp().toString());

                       // PTR.setText(posts.get(0).get_ptr());
                       // MRP.setText(posts.get(0).get_mrp());
                        //---------------------------half scheme percent scheme edit


                        if (posts.get(0).getHSche() == null || posts.get(0).getHSche().equals("null") || posts.get(0).getHSche().equals("")) {
                            Halfscheme.setText("Half Scheme: NA");
                        } else {
                            Halfscheme.setText("Half Scheme: "+posts.get(0).getHSche());
                        }

                        if (posts.get(0).get_pschm() == null || posts.get(0).get_pschm().equals("null") || posts.get(0).get_pschm().equals("")) {
                            Percentscheme.setText("% Scheme: NA");
                        } else {
                            Percentscheme.setText("% Scheme: "+posts.get(0).get_pschm());
                        }
//-------------------------------------------------
                        //String.valueOf(arg1.getTag(R.id.key_product_Name))
                        Itemcode = (posts.get(0).get_icode());
                        MfgCode = (posts.get(0).get_mn());
                        // type = Integer.parseInt(arg1.getTag(R.id.key_product_Type).toString());
                        type = 0;
                        Itemname = String.valueOf(arg1.getTag(R.id.key_product_Name));
                        Pack = (arg1.getTag(R.id.key_product_Pack) == null) ? "NA" : arg1.getTag(R.id.key_product_Pack).toString();
                        MfgName = arg1.getTag(R.id.key_product_Mfg).toString();
                       // DoseForm = "123";
                        DoseForm = (posts.get(0).get_dform() == null) ? "NA" : posts.get(0).get_dform();
                        Sche = (posts.get(0).get_schm() == null) ? "NA" : posts.get(0).get_schm();

                        //DoseForm = posts.get(0).get_dform().toString();
                        //Sche = "456";
                        //Sche = posts.get(0).get_schm().toString();

                        if (!posts.get(0).get_ptr().toString().equals("null")) {
                            vRate = posts.get(0).get_ptr();
                           // vRate = Float.parseFloat(posts.get(0).get_ptr().toString());
                        } else {
                            vRate = (float) 0;
                        }
                        if (!posts.get(0).get_mrp().toString().equals("null")) {
                            vMrp = posts.get(0).get_mrp();
                           // vMrp = Float.parseFloat(posts.get(0).get_mrp().toString());
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
                        if (!posts.get(0).get_dform().equals("null")) {
                            UOM = posts.get(0).get_dform();
                        }

                        if (!posts.get(0).get_s().equals("null")) {
                            // setStockLegend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                            set_stock_color_legend(Integer.parseInt(posts.get(0).get_s()));
                        }

                       // ProductId = produdtid;
                       // ProductId = arg1.getTag(R.id.key_product_ItemCode).toString();
                       // ProductId = arg1.getTag(R.id.key_product_code).toString();
                        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                        {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        // UOM = arg1.getTag(R.id.key_product_Dose).toString();
                        //  stock.setText(arg1.getTag(R.id.key_product_stock).toString());
                        //  set_stock_color_legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                        //  setStock
                        // Legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(Create_Order.this, "No Product Details Found", Toast.LENGTH_SHORT).show();
                }
            }
//                //Student selected = (Student) arg0.getAdapter().getItem(arg2);
//                //arg1.getTag(R.id.key_product_Mfg);
//                //String fmgname=(String)_autoCompleteTextView.getTag(R.id.key_product_Mfg);
//                _addProduct.setEnabled(true);
//                _addProduct.setClickable(true);
//                _addProduct.setBackgroundColor(Color.parseColor("#03a9f4"));
//                _Qty.requestFocus();
//                _Qty.setText("");
//                try {
//                    mfg.setText(String.valueOf(arg1.getTag(R.id.key_product_Mfg)));
//                    _autoCompleteTextView.setText(String.valueOf(arg1.getTag(R.id.key_product_Name)));
//                    Dosefrom.setText(String.valueOf(arg1.getTag(R.id.key_product_Dose)));
//
//                    if (String.valueOf(arg1.getTag(R.id.key_product_Pack)) == null || String.valueOf(arg1.getTag(R.id.key_product_Pack)).equals("null")) {
//                        Packsize.setText("---");
//                    } else {
//                        Packsize.setText(String.valueOf(arg1.getTag(R.id.key_product_Pack)));
//                    }
//
//                    if (String.valueOf(arg1.getTag(R.id.key_product_Scheme)) == null || String.valueOf(arg1.getTag(R.id.key_product_Scheme)).equals("null")) {
//                        Scheme.setText("---");
//                    } else {
//                        Scheme.setText(String.valueOf(arg1.getTag(R.id.key_product_Scheme)));
//                    }
//
//
//                    PTR.setText(String.valueOf(arg1.getTag(R.id.key_product_PTR)));
//                    MRP.setText(String.valueOf(arg1.getTag(R.id.key_product_MRP)));
//                    //---------------------------half scheme percent scheme edit
//
//
//                    if (String.valueOf(arg1.getTag(R.id.key_product_halfscheme)) == null || String.valueOf(arg1.getTag(R.id.key_product_halfscheme)).equals("null") || String.valueOf(arg1.getTag(R.id.key_product_halfscheme)).equals("")) {
//                        Halfscheme.setText("Half Scheme: NA");
//                    } else {
//                        Halfscheme.setText("Half Scheme: "+String.valueOf(arg1.getTag(R.id.key_product_halfscheme)));
//                    }
//
//                    if (String.valueOf(arg1.getTag(R.id.key_product_percentscheme)) == null || String.valueOf(arg1.getTag(R.id.key_product_percentscheme)).equals("null") || String.valueOf(arg1.getTag(R.id.key_product_percentscheme)).equals("")) {
//                        Percentscheme.setText("% Scheme: NA");
//                    } else {
//                        Percentscheme.setText("% Scheme: "+String.valueOf(arg1.getTag(R.id.key_product_percentscheme)));
//                    }
////-------------------------------------------------
//                    Itemcode = (arg1.getTag(R.id.key_product_ItemCode).toString());
//                    MfgCode = (arg1.getTag(R.id.key_product_MfgCode).toString());
//                    // type = Integer.parseInt(arg1.getTag(R.id.key_product_Type).toString());
//                    type = 0;
//                    Itemname = arg1.getTag(R.id.key_product_Name).toString();
//                    Pack = (arg1.getTag(R.id.key_product_Pack) == null) ? "NA" : arg1.getTag(R.id.key_product_Pack).toString();
//                    MfgName = arg1.getTag(R.id.key_product_Mfg).toString();
//                    DoseForm = arg1.getTag(R.id.key_product_Dose).toString();
//                    Sche = arg1.getTag(R.id.key_product_Scheme).toString();
//
//                    if (!arg1.getTag(R.id.key_product_PTR).equals("null")) {
//                        vRate = Float.parseFloat(arg1.getTag(R.id.key_product_PTR).toString());
//                    } else {
//                        vRate = (float) 0;
//                    }
//                    if (!arg1.getTag(R.id.key_product_MRP).equals("null")) {
//                        vMrp = Float.parseFloat(arg1.getTag(R.id.key_product_MRP).toString());
//                    } else {
//                        vMrp = (float) 0;
//                    }
//                    //***************pack in cart************************//
//                    if (!arg1.getTag(R.id.key_product_Pack).equals("null")) {
//                        vPack = arg1.getTag(R.id.key_product_Pack).toString();
//                        //   vPack = Float.parseFloat(arg1.getTag(R.id.key_product_Pack).toString());
//                    } else {
//                        vPack = "0";
//                    }
//
//                    //***************pack in cart************************//
//                    if (!arg1.getTag(R.id.key_product_Dose).equals("null")) {
//                        UOM = arg1.getTag(R.id.key_product_Dose).toString();
//                    }
//
//                    if (!arg1.getTag(R.id.key_product_stock).equals("null")) {
//                        // setStockLegend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
//                        set_stock_color_legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
//                    }
//
//                    ProductId = arg1.getTag(R.id.key_product_code).toString();
//                    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
//                    {
//                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    }
//                    // UOM = arg1.getTag(R.id.key_product_Dose).toString();
//
//                    //  stock.setText(arg1.getTag(R.id.key_product_stock).toString());
//                    //  set_stock_color_legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
//                    //  setStock
//                    // Legend(Integer.parseInt(arg1.getTag(R.id.key_product_stock).toString()));
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                //commemnt by apurva
////                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                imm.hideSoftInputFromWindow(_autoCompleteTextView.getWindowToken(), 0);
//            }
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
                }catch (Exception e)
                {

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
                }catch (Exception e)
                {

                }

            }
        });
//        Log.d("enterValue12", String.valueOf(Qty));
        //addProduct Button click Events

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
                    Toast.makeText(Create_Order.this, "No Search History Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        _addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    _addProduct.setEnabled(false);
                    _addProduct.setClickable(false);
                    _addProduct.setBackgroundColor(Color.parseColor("#d0ebfa"));
                    //         Log.d("enterValue13", String.valueOf(Qty));
                  //  if (Itemcode != null && ProductId != null) {

                   // ProductId = "18693";
                    if (ProductId != null) {

                        if (_autoCompleteTextView.getText().toString().isEmpty()) {
                            OGtoast.OGtoast("Please select a product123", Create_Order.this);
                        }
                        else {
//                            if (_Qty.getText().length() == 0) {
//                                OGtoast.OGtoast("Please select quantity", Create_Order.this);
//
//                            } else {

                            String str = _autoCompleteTextView.getText().toString();
                            if(str.contains(" ")){
                                String[] arrOfStr = str.split(" ",0);
                                ProductName= arrOfStr[0];
                            } else{
                                ProductName= _autoCompleteTextView.getText().toString();
                            }

                            if (_Qty.getText().toString().equals(""))
                            {
                                Qty=1;
                            }
                            else
                            {
                                Qty = Integer.parseInt(_Qty.getText().toString());
                            }

                            //  addItemCart();
                            if(chemistCartList.size()>0)
                            {
                                isAvailable=true;
                                for (int i = 0; i < chemistCartList.size(); i++)
                                {
                                    if (ProductId.equals(chemistCartList.get(i).getProduct_ID())) {
                                        isAvailable=false;
                                        Log.d("productsAvai11", " Name Available");
                                        Toast.makeText(Create_Order.this, "This Product Already Available", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            if (isAvailable) {
                                addItemCart();
                            }

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
                        OGtoast.OGtoast("Please select a product", Create_Order.this);
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

//                    LayoutInflater inflater = LayoutInflater.from(Create_Order.this);
//                    final View dialogview = inflater.inflate(R.layout.fragment_order_summary, null);
//                    final Dialog infoDialog = new Dialog(Create_Order.this);//builder.create();
//                    infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    infoDialog.setContentView(dialogview);
//                    txt_comment = (EditText) dialogview.findViewById(R.id.edit_comment);
//                    txt_cust_name = (TextView) dialogview.findViewById(R.id.customer_name);
//                    txt_order_id=(TextView)dialogview.findViewById(R.id.order_id);
//                    txt_total_items=(TextView)dialogview.findViewById(R.id.total_items);
//                    txt_total = (TextView) dialogview.findViewById(R.id.total_amount);
//                    //  pgr.setVisibility(View.VISIBLE);
//                    ChemistCart chemistCart = chemistCartList.get(0);
//                    txt_cust_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
//                    txt_order_id.setText(chemistCart.getDOC_ID());
//                    txt_total_items.setText(String.valueOf(chemistCartList.size()));
//                    txt_total.setText(String.valueOf(orderAmount));
//
//                    dialogview.findViewById(R.id.confirm_order).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view)
//                        {
//
//                            if (txt_comment.getText().toString().equals(""))
//                            {
//                                String Comment="No Instructions";
//                                confirm_dialog(Comment);
//                            }
//                            else
//                            {
//                                confirm_dialog(txt_comment.getText().toString());
//                            }
//
//                            //confirm_order(txt_comment.getText().toString());
//                            infoDialog.dismiss();
//                        }
//                    });
//                    infoDialog.show();

                    new AlertDialog.Builder(Create_Order.this)
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


                }

                else {
                    OGtoast.OGtoast("Cart is empty", Create_Order.this);
                }
            }
        });

    }

    void confirm_dialog(final String comment) {
        new AlertDialog.Builder(Create_Order.this)
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
        client_id = pref.getString(CLIENT_ID, "");
        daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();
        chemistCartList = chemistCartDao.getCartItem(Stockist_id, false);
        if (chemistCartList != null) {
            n_product_cart_count = chemistCartList.size();
        }
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
        Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
        Float price;
      //  Float price = 23.45f;
//        if(vRate==0.0){
//            price = 0.0f;
//        }
       //
        price = vRate * Qty;
       // }
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
            OGtoast.OGtoast("one quantity added", Create_Order.this);
        }
        else
        {
            OGtoast.OGtoast("Product added to cart succesfully", Create_Order.this);
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

                //  txt_total_itemss.setText(String.valueOf(chemistCartList.size()));
                // txt_order_value.setText(String.valueOf(orderAmount));
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

    public void filter(String charText) {
      /*  if(arraylist.size()<1) {
            this.arraylist.addAll(alternativeitems);
        }*/
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
                LayoutInflater inflater = LayoutInflater.from(Create_Order.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.adpter_cartdata_list_new, parent, false);
//adpter_cartdata_list
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
                       /* _autoCompleteTextView.setText(chemistCartList.get(position).getItemname());
                        _autoCompleteTextView.requestFocus();

                        _Qty.setText(chemistCartList.get(position).getQty());
                        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setHideable(true);
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }*/
                        editQuantityDialog(chemistCartList.get(position).getStockist_Client_id(), chemistCartList.get(position).getProduct_ID(),
                                chemistCartList.get(position).getItemname(), chemistCartList.get(position).getRate(),
                                chemistCartList.get(position).getQty());
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
//edit quantity

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
                    Toast.makeText(Create_Order.this, "Min. quantity should be 1", Toast.LENGTH_SHORT).show();
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
                int quantity = 0;
                Float price = null;
                if (editTextQuantity.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Create_Order.this, "Min. quantity should be one", Toast.LENGTH_SHORT).show();
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
               // chemistCart.setRate(String.valueOf(ptr));
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

    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }


    //******************888
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
                ;
                Cart_Id_available = false;

            }
        }

    }
    private void delivery_options(){


        AlertDialog.Builder builder = new AlertDialog.Builder(Create_Order.this);

        builder.setTitle("Select Delivery Options");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        delivery_option="PickUp";
                        //  Toast.makeText(Create_Order.this, delivery_option, Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        delivery_option="Delivery";
                        //   Toast.makeText(Create_Order.this, delivery_option, Toast.LENGTH_LONG).show();
                        break;
//                case 2:
//                    delivery_option="Others";
//                    Toast.makeText(Create_Order.this, delivery_option, Toast.LENGTH_LONG).show();
//                    break;
                }
                // confirm_order();
                order_summary_dialog();
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

    private void order_summary_dialog()

    {
        LayoutInflater inflater = LayoutInflater.from(Create_Order.this);
        final View dialogview = inflater.inflate(R.layout.fragment_order_summary, null);
        final Dialog infoDialog = new Dialog(Create_Order.this);//builder.create();
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
//                    dialogview.findViewById(R.id.cancel_order).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view)
//                        {
//                            infoDialog.dismiss();
//                        }
//                    });

        infoDialog.show();
    }

    private void confirm_order( String comment) {
        try {
            Log.d("Comment","Comment:"+comment);
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
                main_j_object.put("Delivery_Option",delivery_option);
                // main_j_object.put("Comments", comment);
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
            //db.insert_into_chemist_order_sync(Doc_Id, main_j_array.toString(), 0);
            // db.delete_chemist_Cart(Doc_Id);
            // db.delete_chemist_Cart_Details(Doc_Id);

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

        // SharedPref.saveLastSyncDate(getContext(), simpleDateFormat.format(calendar.getTime()));
        // progress.setVisibility(View.VISIBLE);
        SharedPreferences.Editor edt = pref.edit();
        MakeWebRequest.MakeWebRequest("get", GET_CHEMIST_PRODUCT_DATA,
                GET_CHEMIST_PRODUCT_DATA + "[" + client_id + "," + Stockist_id + "]", this, false);
        edt.putString(CHEMIST_LAST_DATA_SYNC, simpleDateFormat.format(calendar.getTime()));
        //edt.putString(CHEMIST_LAST_DATA_SYNC, dateFormat_sync.format(Calendar.getInstance().getTime()));
        edt.commit();
        /*String proapi = GET_CHEMIST_PRODUCT_DATA + "[" + client_id + "," + Stockist_id + "]";
        System.out.println("chemist product api"+proapi);*/

        //   }
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {


    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
       // Log.e("chemist_product14", response.toString());
        if (response != null) {
            //posts.clear();
            Log.e("chemist_product11", response.toString());

            try {
                posts.clear();
                if (f_name.equals(AppConfig.GET_CHEMIST_PRODUCT_DATA)) {
                  posts = new ArrayList<m_product_list_chemist>();
                    String jsondata = response.toString();
                    if (!jsondata.isEmpty()) {
                        //Log.d("jsondataCheck", jsondata.toString());
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                      //  posts = new ArrayList<m_product_list_chemist>();
                        posts = new LinkedList<m_product_list_chemist>(Arrays.asList(mGson.fromJson(jsondata, m_product_list_chemist[].class)));
                       //posts = Arrays.asList(mGson.fromJson(jsondata, m_order[].class));
                        Log.d("posts1",posts.toString());

                        Log.d("posts11",posts.get(0).get_qty());
                        //posts.get(0).g
      // adpter = new ad_AutocompleteCustomArray_chemist(getApplicationContext(), posts);
      // _autoCompleteTextView.setAdapter(adpter);
                        //saveChemistListInSQLite(jsondata);
                    }
                }
            else if(f_name.equals("GET_CHEMIST_PRODUCT_SEARCH")){

                  posts=  new ArrayList<m_product_list_chemist>();
                    String jsondata = response.toString();
                    if (!jsondata.isEmpty()) {
                    Log.d("jsondataCheck", jsondata.toString());
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        posts = new LinkedList<m_product_list_chemist>(Arrays.asList(mGson.fromJson(jsondata, m_product_list_chemist[].class)));
                       // adpter.notifyDataSetChanged();
                        adpter = new ad_AutocompleteCustomArray_chemist(getApplicationContext(), posts);
                        _autoCompleteTextView.setAdapter(adpter);
                        _autoCompleteTextView.showDropDown();
                        // adpter.notifyDataSetChanged();
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
            adpter.notifyDataSetChanged();
           //   Log.d("Response","NUll");
           // if (f_name.equals(AppConfig.GET_CHEMIST_PRODUCT_DATA)) {
//                get_ProductList_json();
//            }
            Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_SHORT).show();
        }

    }

    void order_confirmed_dialog(String doc_number) {
        new AlertDialog.Builder(Create_Order.this)
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

        ;

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


            new AlertDialog.Builder(Create_Order.this)
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
            OGtoast.OGtoast("Cart is empty", Create_Order.this);
        }
    }

    @Override
    public void onBackPressed() {
        // It's expensive, if running turn it off.
        // DataHelper.cancelSearch();
        // hideKeyboard();
        // daoSession.getStockistProductsDao().deleteAll();
      //  finish();
        super.onBackPressed();
    }


//    void insert_inventory_products(JSONArray response) {
//
///*
//        String jsondata = response.toString();
//        GsonBuilder builder = new GsonBuilder();
//        Gson mGson = builder.create();
//        Type listType = new TypeToken<List<StockistProducts>>() {
//        }.getType();
//        posts = mGson.fromJson(jsondata, listType);*/
//        JSONObject j_obj;
//        StockistProducts stockistProducts;
//        ArrayList<StockistProducts> posts = new ArrayList<StockistProducts>();
//        try {
//            for (int i = 0; i < response.length(); i++) {
//                j_obj = response.getJSONObject(i);
//                stockistProducts = new StockistProducts();
//                stockistProducts.setProduct_ID(j_obj.getString("Product_ID"));
//                stockistProducts.setItemcode(j_obj.getString("Itemcode"));
//                stockistProducts.setStockist_id(j_obj.getString("Stockist_id"));
//                stockistProducts.setItemname(j_obj.getString("Itemname"));
//                stockistProducts.setPacksize(j_obj.getString("Packsize"));
//                stockistProducts.setMRP(j_obj.getString("MRP"));
//                stockistProducts.setRate(j_obj.getString("Rate"));
//                stockistProducts.setStock(j_obj.getString("stock"));
//                stockistProducts.setMfgCode(j_obj.getString("MfgCode"));
//                stockistProducts.setMfgName(j_obj.getString("MfgName"));
//                stockistProducts.setDoseForm(j_obj.getString("DoseForm"));
//                stockistProducts.setScheme(j_obj.getString("Scheme"));
//                stockistProducts.setHalfScheme(j_obj.getString("HalfScheme"));
//                stockistProducts.setPercentScheme(j_obj.getString("PercentScheme"));
//                //PercentScheme   and  HalfScheme
//                stockistProducts.setType("0");
//                posts.add(stockistProducts);
//
//                //daoSession.getStockistProductsDao().insert(stockistProducts);
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //daoSession.getStockistProductsDao().deleteAll();
//        // commented by prakash
//        //daoSession.getStockistProductsDao().deleteStockistProducts(Stockist_id);
//        //daoSession.getStockistProductsDao().insertInTx(posts,true);
//        sreach_product_list = posts;
//        // Log.e("Size11", String.valueOf(sreach_product_list.size()));
//        //   Toast.makeText(getApplicationContext(),String.valueOf(sreach_product_list.size())+"products",Toast.LENGTH_SHORT).show();
//    }





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

        OGtoast.OGtoast("Product removed succesfully", Create_Order.this);
        cartAdapter.notifyDataSetChanged();

//        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
        //   get_stockist_legends();
        // adpter.notifyDataSetChanged();
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

        OGtoast.OGtoast("Product removed succesfully", Create_Order.this);

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        //   get_stockist_legends();
    }

    void get_stockist_legends() {
        try {
//            Stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
//
//            legend_data = ConstData.get_jsonArray_from_cursor(db.get_legend_data(Stockist_id)).toString();

            Stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
            JSONArray j_arr= new JSONArray();
            j_arr.put(Stockist_id);
            j_arr.put(1);

            progressDialog = ProgressDialog.show(Create_Order.this, "Please Wait", "Loading Products.... ", true);
            MakeWebRequest.MakeWebRequest("out_array", AppConfig.POST_GET_STOCKIST_LEGENDS, AppConfig.POST_GET_STOCKIST_LEGENDS,
                    j_arr  , this, true,"");

        } catch (Exception e) {

            e.toString();
        }
    }

    void set_stock_color_legend(Integer Stock) {

        try {
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

                    if (legend_mode.equals("3")) {
                        //       Log.d("chemist13","print three ");
                        stock.setBackgroundColor(Color.parseColor(color_code));
                        stock.setText(legendName);
                    }
                    else if(legend_mode.equals("2"))
                    {
                        //        Log.d("chemist12","print two");
                        stock.setBackgroundColor(Color.parseColor(color_code));

                        stock.setText(String.valueOf(Stock));
                        stock.setTextColor(getResources().getColor(R.color.white));
                    }
                    else if (legend_mode.equals("1"))
                    {
                        //              Log.d("chemist11","print one");
                        stock.setBackgroundColor(Color.parseColor(color_code));
                    }


                }
            }

        } catch (Exception e) {

            e.toString();

        }





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

    private void getLoadproducts(String newText) {

        String _key = "M"; //Multiple or list
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(7);
        jsonArray.put(1);
        jsonArray.put(newText);
        jsonArray.put(-99);
        jsonArray.put(_key);

        //  String url_search  = "http://13.126.224.235/api/products/APP_GetFullProductSearch/";
        Log.d("chemist_product10", newText);

                /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_FULL_CHEMIST_PRODUCT_NAMES,
                        AppConfig.GET_FULL_CHEMIST_PRODUCT_NAMES + newText,getApplicationContext(), false);
*/

        MakeWebRequest.MakeWebRequest("get", "GET_CHEMIST_PRODUCT_SEARCH",
                AppConfig.GET_CHEMIST_PRODUCT_DATA + jsonArray,Create_Order.this, false);

    }

    private void getLoadproductsDetails(String product_id) {
        String _key1 = "S";
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(7);
        jsonArray.put(1);
        jsonArray.put("");
        jsonArray.put(product_id);
        jsonArray.put(_key1);

        //  String url_search  = "http://13.126.224.235/api/products/APP_GetFullProductSearch/";
        Log.d("jsonArray11", jsonArray.toString());

                /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_FULL_CHEMIST_PRODUCT_NAMES,
                        AppConfig.GET_FULL_CHEMIST_PRODUCT_NAMES + newText,getApplicationContext(), false);
*/

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_PRODUCT_DATA,
                AppConfig.GET_CHEMIST_PRODUCT_DATA + jsonArray,Create_Order.this, false);

        String url = AppConfig.GET_CHEMIST_PRODUCT_DATA + jsonArray;
        Log.d("urll",url);

    }


}
