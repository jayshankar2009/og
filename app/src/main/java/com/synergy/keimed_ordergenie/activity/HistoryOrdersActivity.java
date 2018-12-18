package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.database.MasterPlacedOrder;
import com.synergy.keimed_ordergenie.model.m_order;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;
import com.synergy.keimed_ordergenie.utils.get_current_location;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_ORDERLIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class HistoryOrdersActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.rv_orderlist)
    RecyclerView rv_orderlist;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.total_count)
    TextView _total_count;

    @BindView(R.id.empty_view)
    TextView empty_view;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.fab_options)
    FloatingActionMenu fab_options;

    private SQLiteHandler db;

    public static final String SELECTED_CHEMIST_ID = "selected_chemist_id";
    public static final String CHEMIST_IS_LAST_10_ORDER = "chemist_is_last_10_orders";

    public static final String CHEMIST_ORDER_ID = "chemist_order_id";
    public static final String CHEMIST_ORDER_DATE = "chemist_order_date";

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private Boolean isRequestCurrentLocation = false;

    private SearchView searchView;
    List<m_order> posts = new ArrayList<m_order>();

    private Context context;
    SharedPreferences pref;
    TextView txt_start_date;
    String login_type;
    TextView txt_end_date;
    private String User_id,Stockist_id ,Type,Status;

    private Boolean IsCallPlanTask;
    DatePickerDialog dpd_start_date, dpd_end_date;
    private Date current_date = Calendar.getInstance().getTime();
   // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    private Date filter_start_date, filter_end_date;
    //private Button button_export;
    LocationManager mLocationManager;


    /* Is Last 10 Orders */
    private boolean isLast10Orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_orders);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
        }
        ButterKnife.bind(this);

        db = new SQLiteHandler(this);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        IsCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);
        setSupportActionBar(_toolbar);
        _toolbar.setTitle("Orders");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
            //actionBar.setTitle(" Last 10 Orders");
        }

        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");
        isLast10Orders = getIntent().getBooleanExtra("isLast10Order", false);


        if (login_type.equals(CHEMIST)) {
            fab_options.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            get_stockist_order_list();
        } else {
            fab.setVisibility(View.VISIBLE);
            fab_options.setVisibility(View.GONE);
            String selected_chemist_id = getIntent().getStringExtra(SELECTED_CHEMIST_ID);

//            Log.d("DELECTEDORDER",selected_chemist_id);
           /* MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_SUMMARY,
                    AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_SUMMARY + "[" + Stockist_id + "," + User_id + "," + selected_chemist_id + "]", this, true);
*/
            //  Log.d("Tested_ids",Stockist_id+" "+User_id);
//            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY,
//                    AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY + "[" + Stockist_id + "," + User_id + "]", this, true);


            /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY,
                    AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY +"["+Stockist_id+","+User_id+"]" , this, true);*/

            if (Utility.internetCheck(this)) {

                Log.d("showList11",AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY +"["+Stockist_id+","+User_id+"]");
                MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY,
                        AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY +"["+Stockist_id+","+User_id+"]" , this, true);
            } else {
                getOfflineOrdersByUserId();
            }

///api/orders/APP_GetStockistOrderHistory/["1240","1982"];
            //   http://www.ordergenie.co.in/api/orders/APP_GetStockistOrderHistory/["1240","1982"]
        }
//        button_export=(Button)findViewById(R.id.button_export);
//        button_export.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               // getOfflineOrdersByUserId();
//            }
//        });
    }

    @OnClick(R.id.fab_bills)
    void show_pendingbills() {

        Intent intent = new Intent(HistoryOrdersActivity.this, AllPendingBills.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab_sales)
    void show_sales() {
        Intent intent = new Intent(HistoryOrdersActivity.this, SalesReturnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    void onclick_fab() {
        new get_current_location(HistoryOrdersActivity.this);

    }

    @OnClick(R.id.fab_stockist_list)
    void show_Stockist() {
        Intent intent = new Intent(HistoryOrdersActivity.this, StockistList.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_list, menu);
        if (login_type.equals(CHEMIST)) {
            menu.findItem(R.id.action_location).setVisible(true);
        } else {
            menu.findItem(R.id.action_location).setVisible(false);
        }
         searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {


                newText = newText.toLowerCase();

                final List<m_order> filteredModelList = new ArrayList<>();
                for (m_order model : posts) {
                    //final String text = model.getCustomer_Name().toLowerCase();


                    //  final String order_id = model.getOrder_Id().toString();

                    final String text = model.getCustomer().toLowerCase();

                    //Log.d("VALUESPPORDERLIST12",text +" "+order_id);

                    /*if (text.contains(newText) || order_id.contains(newText)) {*/

                    if (text.contains(newText)) {
                        filteredModelList.add(model);
                    }
                }
                fill_orderlist(filteredModelList);
                rv_orderlist.scrollToPosition(0);

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
            case R.id.action_search:
                return true;

            case R.id.action_add:
                create_order();
                return true;
            case R.id.action_filter:
                show_dialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_location:
                new get_current_location(HistoryOrdersActivity.this);

                default:
                return super.onOptionsItemSelected(item);
        }
    }



    /* onResume */
    @Override
    public void onResume() {
        super.onResume();

    }

    /* Get Offline Orders By UserId */
    private void getOfflineOrdersByUserId() {
        Cursor cursor = db.getAllOrderByUserId(User_id);
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show();
        } else {
            if (isLast10Orders) {
                if (cursor.moveToFirst()) {
                    do {
                        m_order m_order = new m_order();
                        m_order.setOrder_Id(0);
                        m_order.setCustomer_Name(cursor.getString(4));
                        m_order.setCustomer(cursor.getString(5));
                        m_order.setOrderNo(cursor.getString(6));
                        m_order.setOrderDate(cursor.getString(7));
                        m_order.setOrderAmount(cursor.getString(10));
                        m_order.setOrdersStatus(cursor.getString(11));
                        m_order.setType(-99);

                        posts.add(m_order);

                    } while (cursor.moveToNext());
                }
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        m_order m_order = new m_order();
                        m_order.setOrder_Id(0);
                        m_order.setCustomer_Name(cursor.getString(4));
                        m_order.setCustomer(cursor.getString(5));
                        m_order.setOrderNo(cursor.getString(6));
                        m_order.setOrderDate(cursor.getString(7));
                        m_order.setOrderAmount(cursor.getString(10));
                        m_order.setOrdersStatus(cursor.getString(11));
                        m_order.setType(-99);

                        posts.add(m_order);
                    //    Log.d("postupdate",posts.toString());

                    } while (cursor.moveToNext());
                }
            }

            if (cursorCount == posts.size()) {
                Collections.reverse(posts);
                fill_orderlist(posts);
            }
        }
    }






    /* Order List Json */
    private void get_order_list_json(String jsondata) {

        // posts.clear();


/*
     1  // LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("order_list.json", Order_list.this);
      //  String jsondata = _LoadJsonFromAssets.getJson();*/
        if (!jsondata.isEmpty()) {
             Log.d("HISTORYORDERSS11", jsondata);
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            posts = new ArrayList<m_order>();
            posts = new LinkedList<m_order>(Arrays.asList(mGson.fromJson(jsondata, m_order[].class)));
            // posts = Arrays.asList(mGson.fromJson(jsondata, m_order[].class));
          // fill_orderlist(posts); //Commented By Vishal
            DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
            List<MasterPlacedOrder> offlineorders = daoSession.getMasterPlacedOrderDao().loadAll();

            if (offlineorders.size() > 0) {
                for (MasterPlacedOrder masterPlacedOrder : offlineorders) {

                    String json = masterPlacedOrder.getJson();

                    try {
                        JSONArray j_arr = new JSONArray(json);
                        m_order o_m_order = new m_order();
                        o_m_order.setCustomer_Name(pref.getString(CLIENT_NAME, ""));
                        o_m_order.setOrder_Id(0);
                        o_m_order.setCustomer(masterPlacedOrder.getCustomerID());
                        o_m_order.setOrderDate(j_arr.getJSONObject(0).getString("Doc_Date"));
                        o_m_order.setOrderNo(j_arr.getJSONObject(0).getString("DOC_NO"));
                        o_m_order.setStatus(0);
                        o_m_order.setType(-99);
                        o_m_order.setOrderAmount(j_arr.getJSONObject(0).getString("Amount"));
                    //    Log.d("SaiRam", String.valueOf(j_arr));
                        posts.add(o_m_order);
                     //    Log.e("posts", posts.toString());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

           /* try {
                Collections.sort(posts, new CustomComparator());
            } catch (Exception e) {

                e.printStackTrace();
            }*/


            try {
                sdf.setDateFormatSymbols(DateFormatSymbols.getInstance(Locale.ENGLISH));
                final String filterdate = sdf.format(Calendar.getInstance().getTime()).toString();
                //   Log.e("filterdate",filterdate);

                Order_list.Predicate<m_order> validPersonPredicate = new Order_list.Predicate<m_order>() {

                    Date date;

                    public boolean apply(m_order m_order) {
                        try {
                            String s = sdf.format(sdf.parse(m_order.getOrderDate()));

                            return s.equals(filterdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                };
                //commemt by apurva
                Collections.sort(posts, new CustomComparator());
                Collection<m_order> result = Order_list.filter(posts, validPersonPredicate);
                //Collections.sort(posts, new CustomComparator());

               fill_orderlist((List) result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /// fill_orderlist(posts);

            if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
                try {
                    Collections.sort(posts, new CustomComparator());
                } catch (Exception e) {

                }
            }

        }

    }



    /* Bind Orders */
    private void fill_orderlist(final List<m_order> posts_s) {
        if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
            _total_count.setText("10 Orders.");
        } else {
            _total_count.setText(posts_s.size() + " Orders.");
        }

Log.i("Posts ----"+posts_s.size(),""+posts_s.toString());
        if (posts_s.isEmpty()) {
            empty_view.setVisibility(View.VISIBLE);
            rv_orderlist.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            rv_orderlist.setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {

            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(HistoryOrdersActivity.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.adapter_historyy_orderss, parent, false);

                return new BindingViewHolder(binding.getRoot());
            }


            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                m_order order_list = posts_s.get(position);
                holder.getBinding().setVariable(BR.v_ordertlist, order_list);
                holder.getBinding().executePendingBindings();

                TextView order_Id = (TextView) holder.getBinding().getRoot().findViewById(R.id.order_Id);
                TextView orderDate = (TextView) holder.getBinding().getRoot().findViewById(R.id.orderDatee);
                TextView invoice_Idd = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoice_Id);
                TextView h_invoicedate = (TextView) holder.getBinding().getRoot().findViewById(R.id.h_invoicedate);
                TextView invoicedate = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoicedate);
                TextView h_invoiceamount = (TextView) holder.getBinding().getRoot().findViewById(R.id.h_invoiceamount);
                TextView invoiceamount = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoiceamount);
                TextView Orderamountt = (TextView) holder.getBinding().getRoot().findViewById(R.id.orderamount);
                //  Orderamountt.setText("hiee");
                //  Orderamountt.setText(posts_s.get(position).getOrder_Id());
                //  Orderamountt.setText(posts_s.get(position).getOrderAmount());
                //  Orderamountt.setText(posts_s.get(position).getAmount());
                //  Log.d("orderamount",posts_s.get(position).getOrder_Id().toString());

                //  Orderamountt.setText(posts_s.get(position).getOrderAmount());

                /*try {
                    orderDate.setText(sdf.format(dateFormat.parse(posts_s.get(position).getOrderDate())));

                } catch (Exception e) {

                }*/


                orderDate.setText(posts_s.get(position).getOrderDate());
               /* try {

                    if (posts_s.get(position).getOrderDate() != null) {

                        Log.d("ShowDate123",posts_s.get(position).getOrderDate());
                        //Orderamountt.setText("hiee");
                        orderDate.setText(posts_s.get(position).getOrderDate());
                       // orderDate.setText(sdf.format(dateFormat.parse(posts_s.get(position).getOrderDate())));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                if (order_list.getType() == -99) {
                    /*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));*/

                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.red_small_diamond));

                    if (posts_s.get(position).getStatus() == 0) {

                        order_Id.setTextColor(Color.parseColor("#4caf50"));
                    }


                } else {
                    if (posts_s.get(position).getOrdersStatus() != null) {
                        if (posts_s.get(position).getOrdersStatus().equals("0")) {


                   /*     ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));*/

                            ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.yellow_small_diamond));


                        }

                        else if (posts_s.get(position).getOrdersStatus().equals("1")) {


                   /*     ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));*/
                            ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.pink_small_diamond));


                        }
                        else if (posts_s.get(position).getOrdersStatus().equals("3")) {

                        /*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.bluesquaredimg));*/

                            ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.blue_small_diamond));
                        }
                        else if (posts_s.get(position).getOrdersStatus().equals("4")) {
                            ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.blue_small_diamond));
                        }
                        else if (posts_s.get(position).getOrdersStatus().equals("5")) {
                            ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.green_small_diamond));
                        }
                    }
                }


                //invoice_Idd

                /*if(posts_s.get(position).getOrdersStatus() != null)
                {
                    if(posts_s.get(position).getOrdersStatus().equals("0")) {
                        // invoice_Idd.setText(posts_s.get(position).getOrdersStatus());
                        invoice_Idd.setText("Submited");
                    }
                    else if(posts_s.get(position).getOrdersStatus().equals("1")) {

                        invoice_Idd.setText("Processing");
                    }
                    else if(posts_s.get(position).getOrdersStatus().equals("2")) {

                        invoice_Idd.setText("Partially Completed");
                    }
                    else if(posts_s.get(position).getOrdersStatus().equals("3")) {

                        //invoice_Idd.setText("Completed");


                        if (posts_s.get(position).getInvoiceNo() != null) {
                            invoice_Idd.setText(posts_s.get(position).getInvoiceNo());


                            try {
                                invoicedate.setText(sdf.format(dateFormat.parse(posts_s.get(position).getInvoiceDate())));

                            } catch (Exception e) {

                            }
                            invoiceamount.setText(posts_s.get(position).getInvoice_Amt());

                            invoicedate.setVisibility(View.VISIBLE);
                            invoiceamount.setVisibility(View.VISIBLE);
                            h_invoicedate.setVisibility(View.VISIBLE);
                            h_invoiceamount.setVisibility(View.VISIBLE);

                        } else {
                            invoice_Idd.setText("Invoice Not Created Yet.");
                            invoicedate.setVisibility(View.GONE);
                            invoiceamount.setVisibility(View.GONE);
                            h_invoicedate.setVisibility(View.GONE);
                            h_invoiceamount.setVisibility(View.GONE);
                        }





                    }
                    else if(posts_s.get(position).getOrdersStatus().equals("4")) {

                        invoice_Idd.setText("Invoiced");
                    }
                    else if(posts_s.get(position).getOrdersStatus().equals("5")) {
                        // invoice_Idd.setText(posts_s.get(position).getOrdersStatus());
                        invoice_Idd.setText("Delivered");
                    }
                }


*/

//                if (posts_s.get(position).getBalenceAmount() != null) {
//                    Orderamountt.setText(posts_s.get(position).getBalenceAmount());
//                   // Log.d("balanceAMount",posts_s.get(position).getBalenceAmount());
//                }


                if (posts_s.get(position).getAmount() != null) {
                    Orderamountt.setText(posts_s.get(position).getAmount());

                   // Log.d("HistoryOrders12",posts_s.get(position).getAmount());
                }
                else{
                    Orderamountt.setText("NA");
                }

                if (posts_s.get(position).getInvoiceNo() != null) {

                    invoice_Idd.setText(posts_s.get(position).getInvoiceNo());
                    invoicedate.setText(posts_s.get(position).getOrderDate());

                    try {

                        // harish comment 17 august
                       // invoicedate.setText(posts_s.get(position).getOrderDate());
                      //  invoicedate.setText(sdf.format(dateFormat.parse(posts_s.get(position).getOrderDate())));

                    } catch (Exception e) {

                    }
                    invoiceamount.setText(posts_s.get(position).getInvoice_Amt());

                    invoicedate.setVisibility(View.VISIBLE);
                    invoiceamount.setVisibility(View.VISIBLE);
                    h_invoicedate.setVisibility(View.VISIBLE);
                    h_invoiceamount.setVisibility(View.VISIBLE);

                } else {
                    invoice_Idd.setText("Invoice Not Created Yet.");
                    invoicedate.setVisibility(View.GONE);
                    invoiceamount.setVisibility(View.GONE);
                    h_invoicedate.setVisibility(View.GONE);
                    h_invoiceamount.setVisibility(View.GONE);
                }

                if (login_type.equals(CHEMIST)) {

                    if (posts_s.get(position).getOrder_Id().toString().equals("0")) {
                        order_Id.setText("Offline");
                    } else {
                        order_Id.setText(posts_s.get(position).getOrder_Id().toString());
                        //Orderamount.setText(posts_s.get(position).getOrderAmount().toString());
                    }

                } else {
                    order_Id.setText(posts_s.get(position).getOrderNo().toString());
                }


                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(Order_list.this,"clicked" , Toast.LENGTH_SHORT).show();
                        Intent intent = null;

                        if (login_type.equals(CHEMIST)) {

                            Log.d("CHEMISTLOGIN", "CHEMIST LOGIN");
                            intent = new Intent(HistoryOrdersActivity.this, Order_details.class);
//                            intent.putExtra("type","orderlist");
//                            intent.putExtra("status",String.valueOf(posts_s.get(position).getStatus()));
                            intent.putExtra(CHEMIST_ORDER_ID, posts_s.get(position).getOrder_Id().toString());
                            intent.putExtra(CHEMIST_ORDER_DATE, posts_s.get(position).getOrderInvoiceDate().toString());
                            //intent.putExtra(CHEMIST_ORDER_DATE, posts_s.get(position).getOrderDate().toString());

                            if (posts_s.get(position).getType() == -99) {
                                OGtoast.OGtoast("This order is not completed yet. Please connect to a network to complete this order.", HistoryOrdersActivity.this);
                                return;
                            }
                            startActivity(intent);

                        } else {
                            //Log.d("CHEMISTLOGIN", "SALESSSS LOGIN");
                            if (!IsCallPlanTask) {
                                intent = new Intent(HistoryOrdersActivity.this, Order_details.class);
                                intent.putExtra(CHEMIST_ORDER_ID, posts_s.get(position).getOrderNo().toString());
                                intent.putExtra("type","orderlist");
                                intent.putExtra("status",String.valueOf(posts_s.get(position).getOrdersStatus()));
                                intent.putExtra("docNo", posts_s.get(position).getOrderNo());
                                intent.putExtra("orderDate", posts_s.get(position).getOrderDate());
                                /*if (posts_s.get(position).getOrderDate() != null) {
                                    intent.putExtra(CHEMIST_ORDER_DATE, posts_s.get(position).getOrderDate());
                                }*/
                                if (posts_s.get(position).getOrderInvoiceDate() != null) {
                                    intent.putExtra(CHEMIST_ORDER_DATE, posts_s.get(position).getOrderInvoiceDate());
                                }
                                startActivity(intent);
                            } else {

                                intent = new Intent(HistoryOrdersActivity.this, Delivery.class);
                                intent.putExtra("client_id", getIntent().getStringExtra("client_id"));
                                intent.putExtra("receiver", getIntent().getParcelableExtra("receiver"));
                                intent.putExtra("order_no", posts_s.get(position).getOrderNo());
                                intent.putExtra("customer_name", posts_s.get(position).getCustomer_Name());
                                intent.putExtra("order_count", posts_s.get(position).getItems());

                               /* if (posts_s.get(position).getInvoiceDate() != null) {
                                    intent.putExtra("order_date", posts_s.get(position).getInvoiceDate());
                                } else {
                                    intent.putExtra("order_date", posts_s.get(position).getOrderDate());
                                }*/

                                if (posts_s.get(position).getInvoiceDate() != null) {
                                    intent.putExtra("order_date", posts_s.get(position).getInvoiceDate());
                                } else {
                                    intent.putExtra("order_date", posts_s.get(position).getOrderInvoiceDate());
                                }


                                intent.putExtra("Start_time", getIntent().getStringExtra("Start_time"));
                                intent.putExtra("invoice_no", posts_s.get(position).getInvoiceNo());

                                if (posts_s.get(position).getInvoice_Amt() != null) {
                                    intent.putExtra("invoice_amount", posts_s.get(position).getInvoice_Amt());
                                } else {
                                    intent.putExtra("invoice_amount", posts_s.get(position).getAmount());
                                }


                                intent.putExtra("chemist_id", getIntent().getStringExtra(SELECTED_CHEMIST_ID));
                                intent.putExtra("Call_plan_id", getIntent().getStringExtra("Call_plan_id"));
                                startActivity(intent);
                                finish();
                            }
                        }


                    }
                });


                /*if (order_list.getType() == -99) {
                    *//*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));*//*

                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));

                } else {
                    if (order_list.getStatus() < 4) {

                        *//*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));*//*

                        ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));
                    }
                }*/

            }

            @Override
            public int getItemCount() {
                if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
                    if (posts_s.size() > 10) {
                        return 10;
                    } else {
                        return posts_s.size();
                    }
                } else {
                    return posts_s.size();
                }

            }
        };
        rv_orderlist.setAdapter(adapter);
        rv_orderlist.setLayoutManager(new LinearLayoutManager(this));
        //    rv_orderlist.setHasFixedSize(true);
        adapter.notifyDataSetChanged();


    }

    /*   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
           TextView Cus_Name,OrderId,invDate;
           ImageView icon;

           public MyViewHolder(View itemView) {
               super(itemView);
               context = itemView.getContext();
               Cus_Name=(TextView)itemView.findViewById(R.id.Cust_Name);
               OrderId=(TextView)itemView.findViewById(R.id.order_Id);
               invDate=(TextView)itemView.findViewById(R.id.invoiceDate);
               icon=(ImageView)itemView.findViewById(R.id.billStatus);
               itemView.setClickable(true);
               itemView.setOnClickListener(this);
           }

           @Override
           public void onClick(View v) {

               final Intent intent;
               intent =  new Intent(context, StockistList.class);
               context.startActivity(intent);
              // Toast.makeText(context,"The Item Clicked is: "+getPosition(),Toast.LENGTH_SHORT).show();
           }
       };*/
    private void create_order() {
        Intent i = new Intent(HistoryOrdersActivity.this, Order_Search.class);
      //  Intent i = new Intent(HistoryOrdersActivity.this, Create_Order.class);
        startActivity(i);
    }

    void get_stockist_order_list() {
        get_order_list_json(pref.getString(CHEMIST_ORDERLIST, "[]"));
    }



    /* APIs Success Responses */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            try {

                Log.d("showList12",response.toString());

                /*if (f_name.equals(AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_SUMMARY)) {
                    get_order_list_json(response.toString());
                }*/
                if (f_name.equals(AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY)) {
                    Log.d("showList123",response.toString());
                    get_order_list_json(response.toString());
                }


            } catch (Exception e) {
                e.toString();
            }
        }
    }



    private void show_dialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_chemist_order_list_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);

        txt_start_date = (TextView) dialogview.findViewById(R.id.txt_start_date);
        txt_end_date = (TextView) dialogview.findViewById(R.id.txt_end_date);
        final CheckBox chk_pending = (CheckBox) dialogview.findViewById(R.id.chk_pending);
        final CheckBox chk_completed = (CheckBox) dialogview.findViewById(R.id.chk_completed);


        if (filter_start_date != null) {
            txt_start_date.setText(sdf.format(filter_start_date));
        }
        if (filter_end_date != null) {
            txt_end_date.setText(sdf.format(filter_end_date));
        }


        txt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(txt_start_date.getText().toString());

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


                dpd_start_date = DatePickerDialog.newInstance(
                        HistoryOrdersActivity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_start_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_start_date.setMaxDate(Calendar.getInstance());

            }


        });


        txt_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(txt_end_date.getText().toString());

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
                dpd_end_date = DatePickerDialog.newInstance(
                        HistoryOrdersActivity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_end_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_end_date.setMaxDate(Calendar.getInstance());
                ;
            }


        });

        dialogview.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter_dialog_conditions(filter_start_date, filter_end_date, chk_pending.isChecked(), chk_completed.isChecked());
                infoDialog.dismiss();
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fill_orderlist(posts);
                infoDialog.dismiss();
            }
        });
        set_attributes(infoDialog);
        infoDialog.show();
    }



    private void set_attributes(Dialog dlg) {

        Window window = dlg.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);

        int[] textSizeAttr = new int[]{android.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedValue typedValue = new TypedValue();
        TypedArray a = this.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionbarsize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        int maxX = mdispSize.x;
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = maxX;   //x position
        wlp.y = actionbarsize - 20;   //y position
        window.setAttributes(wlp);

    }
//    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isPendingChecked, Boolean isCompletedChecked) {
//
//
//        Date convertedDate = null;
//
//
//        final List<m_order> filteredModelList = new ArrayList<>();
//        for (m_order model : posts) {
//
//            if (startDate != null && endDate != null) {
//                //final String date = model.getOrderDate();
//                final String date = model.getOrderDate();
//                try {
//                    convertedDate = dateFormat.parse(date);
//                    String c_date = sdf.format(convertedDate);
//                    convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
//                } catch (Exception e) {
//                }
//
//                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
//                {
//                    if (isPendingChecked) {
//                        if
//                                (model.getStatus() == 0 || model.getStatus() == 1) {
//                            filteredModelList.add(model);
//                        }
//                    } else if (isCompletedChecked) {
//                        if (model.getStatus() == 1) {
//                        // if (model.getStatus() == 3 || model.getStatus()==4 || model.getStatus()==5) {
//                            filteredModelList.add(model);
//                           // fill_orderlist(filteredModelList);
//                        }
//                        else{
//                            if (model.getStatus() == 3 || model.getStatus()==4 || model.getStatus()==5) {
//                              //  filteredModelList.add(model);
//                            }
//
//
//                            //Toast.makeText(getApplicationContext(),"no data available",Toast.LENGTH_LONG).show();}
//                    }
//                    }
//                    else {
//                        filteredModelList.add(model);
//
//                    }
//                }
//
//            } else {
//                if (isPendingChecked) {
//                    if (model.getStatus() == 0 || model.getStatus() == 1) {
//                        filteredModelList.add(model);
//                    }
//                }
//
//                if (isCompletedChecked) {
//                  //  if (model.getStatus() == 3 || model.getStatus()==4 || model.getStatus()==5) {
//                        if (model.getStatus() == 1) {
//                        filteredModelList.add(model);
//                        //    fill_orderlist(filteredModelList);
//                    }
////                    else{
////                          Toast.makeText(getApplicationContext(),"no data available",Toast.LENGTH_LONG).show();
//////                            if (model.getStatus() == 3 || model.getStatus()==4 || model.getStatus()==5) {
//////                                filteredModelList.add(model);
//////                            }
////                        }
//                }
//            }
//        }
//        fill_orderlist(filteredModelList);
//    }


    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isPendingChecked, Boolean isCompletedChecked) {

        Date convertedDate = null;

        final List<m_order> filteredModelList = new ArrayList<>();
        for (m_order model : posts) {

            if (startDate != null && endDate != null) {
                final String date = model.getOrderDate();
                try {
                    convertedDate = dateFormat.parse(date);
                    String c_date = sdf.format(convertedDate);
                    convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                } catch (Exception e) {
                }

                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                {
                    if (isPendingChecked) {
                        if (model.getOrdersStatus().equals("0") || model.getOrdersStatus().equals("1")) {
                            filteredModelList.add(model);
                        }
                    } else if (isCompletedChecked) {
                        if (model.getOrdersStatus().equals("3") || model.getOrdersStatus().equals("4") || model.getOrdersStatus().equals("5")) {
                            filteredModelList.add(model);
                        }
                    } else {
                        filteredModelList.add(model);
                    }
                }

            } else {
                if (isPendingChecked) {
                    if (model.getOrdersStatus().equals("0")|| model.getOrdersStatus().equals("1")) {
                        filteredModelList.add(model);
                    }
                }

                if (isCompletedChecked) {
                    if (model.getOrdersStatus().equals("3") || model.getOrdersStatus().equals("4") || model.getOrdersStatus().equals("5")) {
                        filteredModelList.add(model);
                    }
                }

            }
        }
        fill_orderlist(filteredModelList);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (dpd_start_date != null) {
            if (view.getId() == dpd_start_date.getId()) {
                try {
                    filter_start_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "T00:00:00.000Z");
                    txt_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (Exception e) {
                    e.toString();
                }
            }
            dpd_start_date = null;
        }

        if (dpd_end_date != null) {
            if (view.getId() == dpd_end_date.getId()) {
                try {
                    filter_end_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "T00:00:00.000Z");
                    txt_end_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (Exception e) {
                }
                dpd_end_date = null;
            }

        }
    }
   /* void get_current_location()
    {
        isRequestCurrentLocation=true;
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //  mLocationManager.getLastKnownLocation();
        Boolean isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled)
        {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);



            Location location = mLocationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

          if(location!=null)
            show_snack_bar("Current location : "+LocationAddress.getAddressFromLocation(location.getLatitude(),location.getLongitude(),this));

        }else {
            showSettingsAlert();
        }

    }*/

    /*  public void showSettingsAlert() {
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
                  OGtoast.OGtoast("Location Services not enabled !. Unable to get the location",Order_list.this);
                  dialog.cancel();
              }
          });


          alertDialog.show();
      }
      private void show_snack_bar(String Message)
      {
          Snackbar mySnackbar = Snackbar.make(this.getWindow().getDecorView(), Message, Snackbar.LENGTH_LONG);
          View view = mySnackbar.getView();
          CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
          params.gravity = Gravity.TOP;
          view.setLayoutParams(params);
          mySnackbar.show();
      }

      @Override
      public void onLocationChanged(Location location) {
          if(isRequestCurrentLocation) {
              if (location != null)
                  show_snack_bar("Current location : " + LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), this));
              isRequestCurrentLocation=false;
          }

      }


      @Override
      public void onProviderDisabled(String provider) {

      }
          @Override

      public void onProviderEnabled(String provider)
          {

          }


      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {

          }*/

    public class CustomComparator implements Comparator<m_order> {
        @Override
        public int compare(m_order o1, m_order o2) {

            // return o2.getOrderInvoiceDate().compareTo(o1.getOrderInvoiceDate());
            return o2.getOrderDate().compareTo(o1.getOrderDate());
            //  return o1.getOrderDate().compareTo(o2.getOrderDate());
        }
    }
    public Intent getSupportParentActivityIntent() {

        Intent newIntent = null;
        try {
            if (!IsCallPlanTask) {
                newIntent = new Intent(HistoryOrdersActivity.this, MainActivity.class);
            }
            //you need t o define the class with package name
            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIntent;

    }
}
