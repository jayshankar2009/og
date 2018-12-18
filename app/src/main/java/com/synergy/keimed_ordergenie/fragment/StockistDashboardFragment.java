package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.AllPendingBills;
import com.synergy.keimed_ordergenie.activity.CallPlan;
import com.synergy.keimed_ordergenie.activity.CustomerlistActivity;
import com.synergy.keimed_ordergenie.activity.CustomerlistActivityNew;
import com.synergy.keimed_ordergenie.activity.DeliveryCustomerList;
import com.synergy.keimed_ordergenie.activity.HistoryOrdersActivity;
import com.synergy.keimed_ordergenie.activity.InventorylistActivity;
import com.synergy.keimed_ordergenie.activity.InventorylistOfflineActivity;
import com.synergy.keimed_ordergenie.activity.MainActivity;
import com.synergy.keimed_ordergenie.activity.OrderHistoryActivity;
import com.synergy.keimed_ordergenie.activity.PendingOrderCustomerList;
import com.synergy.keimed_ordergenie.activity.ReportActivity;
import com.synergy.keimed_ordergenie.activity.SalesReturnActivity;
import com.synergy.keimed_ordergenie.activity.SelectedDailyDeliveryList;
import com.synergy.keimed_ordergenie.activity.SelectedDailypaymentList;
import com.synergy.keimed_ordergenie.activity.SelectedpaymentList;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.database.StockistProducts;
import com.synergy.keimed_ordergenie.model.m_call_activities;
import com.synergy.keimed_ordergenie.utils.LoadJsonFromAssets;
import com.synergy.keimed_ordergenie.utils.SharedPref;
import com.synergy.keimed_ordergenie.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.POWER_SERVICE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_ORDER_DELIVERY_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.STOCKIST_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class StockistDashboardFragment extends Fragment implements View.OnClickListener {

    ImageButton btn;
    TextView textView, textView_userName;
    String perstock,stockColour,legendName,color_code,final_legend_mode, str1="online";
    int STOCK;
    String LegendColor,LegendName,delivery_UpdateFlag;
    /* Power Manager */
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private LinearLayout linearLayoutSync,linearLayoutReport,linearLayoutDelivery,linearLayoutPendingOrders;
    /* Date For Call Plan Schedule API */
    private Date filter_start_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /* Fragment View */
    private View v_view;
    /* For Legend & Product APIs */
    private SQLiteHandler sqLiteHandler;
    private String clientId;
    private String userId;

    SharedPreferences pref;
    //static AppController globalVariable;
    private String accessKey;
    private OnmenuitemSelected mListener;

    public static StockistDashboardFragment newInstance() {
        StockistDashboardFragment fragment = new StockistDashboardFragment();
        return fragment;
    }

    public StockistDashboardFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnmenuitemSelected) {
            mListener = (OnmenuitemSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnmenuitemSelected.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        v_view = inflater.inflate(R.layout.stockist_fragment_dashboard, container, false);
        textView = (TextView) v_view.findViewById(R.id.tx_inv_value);
        textView_userName = (TextView) v_view.findViewById(R.id.txt_stcockist);
        MainActivity set_UserName = (MainActivity) getActivity();
        String user_profileName = set_UserName.User_name;
        textView_userName.setText(user_profileName);
        MainActivity asdfdffffff = (MainActivity) getActivity();
        String total_Inventory_Value = asdfdffffff.text_int_total_vallue;
        textView.setText(total_Inventory_Value);
        linearLayoutSync = (LinearLayout) v_view.findViewById(R.id.lnr_sync);
        linearLayoutReport=(LinearLayout)v_view.findViewById(R.id.lnr_collect_report) ;
        linearLayoutDelivery=(LinearLayout)v_view.findViewById(R.id.lnr_all_delivery_customer);
        linearLayoutPendingOrders=(LinearLayout)v_view.findViewById(R.id.lnr_pending_order);
       ///Waseem to Hide Delivery Layout
       /* if (pref.getBoolean(IS_ORDER_DELIVERY_ASSIGNED, false)==null&&) {

            linearLayoutDelivery.setVisibility(View.VISIBLE);
        } else {
            linearLayoutDelivery.setVisibility(View.GONE);
        }*/
        ButterKnife.bind(this, v_view);
        return v_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Utility.internetCheck(getContext())){
            sqLiteHandler.deleteSavedDelivery();
            getDeliverySchedule("");
            getDailyCollectionReport(); // imple 16 oct 2018
        }
    }

    /* onView Created */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         pref = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        clientId = pref.getString(CLIENT_ID, "0");
        userId = pref.getString(USER_ID, "0");
        /* Get Access Token */
        accessKey = pref.getString("key", "");
        /* Current Date */
        currentDateDDMMYYYY();
        /* Initialize SQLite Class */
        sqLiteHandler = new SQLiteHandler(getContext());
        if (Utility.internetCheck(getContext())) {
       //     getDeliverySchedule();
            Cursor cursor = sqLiteHandler.getSalesmanProductList();
            int cursorCount = cursor.getCount();
            if (cursorCount == 0) {
                getCallPlanChemistList("");
                productsAPI();
                getAllInvoiceList();
                getDailyCollectionReport();
                getDeliverySchedule("");
                Log.d("ReportDAily111","on create");
            }
        }

        /* Update Click */
        linearLayoutSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.internetCheck(getContext())) {
                    getCallPlanChemistList("update");//same
                    updateInvoiceList();//updated
                    getDeliverySchedule("update");//same
                    productsAPI(); //updated api
                }
            }
        });

        linearLayoutDelivery.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DeliveryCustomerList.class);
                startActivity(intent);
            } });

        /* Daily Collection */
        linearLayoutReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("Click","cliked collection");
                create_payment_list();
            }
        });
        /* PendingOrders Collection */
        linearLayoutPendingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_pendingorders();
            }
        });
    }
    private void loadData() {
        LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("DeliveryData.json", getContext());
        String jsondata = _LoadJsonFromAssets.getJson();
        if (!jsondata.isEmpty())
        {
            insertDeliveryScheduleSQLite(jsondata.toString());
        }
    }

    @OnClick(R.id.ib_customer)
    public void customerlist() {
        create_customeractivity();
    }

    @OnClick(R.id.LL_Inventory)
    public void Inventory(View view) {
        create_Inventorysactivity();
    }

    @OnClick(R.id.lnr_call_plan)
    public void callplan(View view) {
        Show_CallPlan();
    }

    @OnClick(R.id.pending_fragment)
    public void pendingbills(View view) {
        create_pendingbillsactivity();
    }

    @OnClick(R.id.orders_fragment)
    public void orderhistory(View view) {
        Show_Order();
    }

    @Override
    public void onClick(View v) {
        mListener.OnmenuitemSelected(1, "", "", "");
    }

    @OnClick(R.id.customer_fragment)
    public void onClick() {
        create_customeractivity();
    }

    public interface OnmenuitemSelected {
        void OnmenuitemSelected(int imageResId, String name, String description, String url);
    }
    private void create_customeractivity() {
        Intent intent = new Intent(getActivity(), CustomerlistActivityNew.class);
        startActivity(intent);

    }
    private void create_pendingorders() {
        Intent intent = new Intent(getActivity(), PendingOrderCustomerList.class);
        startActivity(intent);

    }
    private void create_payment_list() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        startActivity(intent);
    }
    private void create_salesreturnactivity() {
        Intent intent = new Intent(getActivity(), SalesReturnActivity.class);
        startActivity(intent);
    }

    private void create_pendingbillsactivity() {
        Intent intent = new Intent(getActivity(), AllPendingBills.class);
        startActivity(intent);
    }

    private void create_Inventorysactivity() {
        Intent intent = new Intent(getActivity(), InventorylistOfflineActivity.class);
        startActivity(intent);
    }

    private void create_Orderhistory() {
        Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
        startActivity(intent);
    }
    /* Orders Click Salesman */
    private void Show_Order() {
        Intent intent = new Intent(getActivity(), HistoryOrdersActivity.class);
        startActivity(intent);
    }

    private void Show_CallPlan() {
        Intent intent = new Intent(getActivity(), CallPlan.class);
        startActivity(intent);
    }
    /* Get Week Day in Number */
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
                break;
        }
        return daynumber;
    }


    /* Get Call Plan Chemist List API */
    private void getCallPlanChemistList(final String update) {
        String url = AppConfig.GET_STOCKIST_CALL_PLAN + "[" + userId + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]";
     Log.d("callplan",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    insertCallPlanChemistList(response.toString());
                    /*if(update.equals("update"))
                    {
                        updateInvoiceList();
                    }*/ // commented by waseem

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.d("callPlanResp", error.getMessage());
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }


    /* Get All Invoice List */
    private void getAllInvoiceList() {
        String url = AppConfig.GetPendingBillBySalesman + "[" + clientId + "," + userId + "]";
        Log.d("URL01",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("URL02",response.toString());
                if (response != null) {
                    insertInvoiceSQLite(response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessKey);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }


    /* Product(Items) API */
    private void productsAPI() {
        Utility.showSyncDialog(getContext());
        String _key = "M"; //Multiple or list
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(clientId); //stockist
        jsonArray.put(2); //salesman flag
        jsonArray.put(""); //search key
        jsonArray.put(-99);//
        jsonArray.put(_key);//for multiple
        jsonArray.put(userId); //user id
        String url = AppConfig.GET_SALESMAN_INVENTORY + jsonArray;
        Log.d("urll",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                  //  Log.d("urll123",response.toString());
                    powerManager = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
                    wakeLock = null;
                    if (powerManager != null) {
                        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StockistDashboardFragment");
                    }
                    if (wakeLock != null) {
                        wakeLock.acquire();
                    }
                    Constant.posts = new ArrayList<>();
                    sqLiteHandler.deleteRecordFromSalesmanProduct();

                    sqLiteHandler.create_Write_db_object();
                    sqLiteHandler.mydb.beginTransaction();
                    try {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject j_obj = response.getJSONObject(i);
                            stockColour = j_obj.getString("LN");
                            if(stockColour.contains(",")){
                                String parts[] = stockColour.split(",");
                                color_code =parts[0];
                                legendName =parts[1];
                            }
                            else{
                                color_code =  stockColour ;
                                legendName = "NA";
                            }
                            StockistProducts stockistProducts = new StockistProducts();
                            stockistProducts.setProduct_ID(j_obj.getString("P_ID"));
                            stockistProducts.setItemcode(j_obj.getString("IC"));
                            stockistProducts.setItemname(j_obj.getString("IM"));
                            stockistProducts.setPacksize(j_obj.getString("PZ"));
                            stockistProducts.setMRP(j_obj.getString("Mrp"));
                            stockistProducts.setRate(j_obj.getString("Rate"));
                            stockistProducts.setStock(j_obj.getString("Stk"));
                            stockistProducts.setMfgCode(j_obj.getString("MfgC"));
                            stockistProducts.setMfgName(j_obj.getString("MfgN"));
                            stockistProducts.setDoseForm(j_obj.getString("DF"));
                            stockistProducts.setScheme(j_obj.getString("Sche"));
                            stockistProducts.setHalfScheme(j_obj.getString("HSche"));
                            stockistProducts.setPercentScheme(j_obj.getString("PSche"));
                            stockistProducts.setBoxSize(j_obj.getString("BoxSize"));
                            stockistProducts.setLegendName(legendName);
                            stockistProducts.setLegendColor(color_code);
                            stockistProducts.setMinQ(j_obj.getString("MinQ"));
                            stockistProducts.setMaxQ(j_obj.getString("MaxQ"));
                            Constant.posts.add(stockistProducts);
                            sqLiteHandler.inset_into_salesman_products(stockistProducts.getProduct_ID(), stockistProducts.getItemcode(), clientId, stockistProducts.getItemname(),
                                    stockistProducts.getPacksize(), stockistProducts.getMRP(), stockistProducts.getRate(), stockistProducts.getStock(),
                                    stockistProducts.getType(), stockistProducts.getMfgCode(), stockistProducts.getMfgName(), stockistProducts.getDoseForm(),
                                    stockistProducts.getScheme(), stockistProducts.getHalfScheme(), stockistProducts.getPercentScheme(),stockistProducts.getLegendName(),stockistProducts.getLegendColor()
                                    ,stockistProducts.getBoxSize(),stockistProducts.getMinQ(),stockistProducts.getMaxQ());

                        }
                        sqLiteHandler.mydb.setTransactionSuccessful();
                        sqLiteHandler.mydb.endTransaction();
                        sqLiteHandler.mydb.close();
                        Utility.hideSyncDialog();
                        currentDate();
                        if (response != null) {
                            Toast.makeText(getContext(), "" + response.length() + " Products Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        wakeLock.release();
                    }
                    catch (JSONException e) {
                        sqLiteHandler.mydb.close();
                        e.printStackTrace();
                    }
                }else{
                    Utility.hideSyncDialog();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideSyncDialog();
                Toast.makeText(getContext(),  " Something went wrong please Update again", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }
    private void getDailyCollectionReport() {
        String url = AppConfig.DailyCollections + "[" + userId + "," + clientId + "]";
        Log.d("ReportDAily10",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    Log.d("ReportDAily11",response.toString());
                    insertDailyCollectionSQLite(response.toString());
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
                headers.put("Authorization", "Bearer " + accessKey);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);


    }
    private void getDeliverySchedule(final String update) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date=simpleDateFormat.format(calendar.getTime());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(Integer.valueOf(userId));
        jsonArray.put(date);
        String url = AppConfig.DeliveryScheduleSalesman + jsonArray;
        Log.d("Delivery_schedule11",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    Log.d("Delivery_schedule12",response.toString());
                    insertDeliveryScheduleSQLite(response.toString());
                    /*if(update.equals("update"))
                    {
                        //productsAPI();
                        updatedProductList(); //updated api
                    }*/
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
                headers.put("Authorization", "Bearer " + accessKey);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);

    }

    /* Get Updated Products */
    private void updatedProductList() {
        Utility.showSyncDialog(getContext());
        String _key = "M$$$"; //Multiple or list
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(clientId); //stockist
        jsonArray.put(2); //salesman flag
        jsonArray.put(""); //search key
        jsonArray.put(-99);//
        jsonArray.put(_key+SharedPref.getLastSyncDate(getContext()));//for multiple
        jsonArray.put(userId); // user id

        String url = AppConfig.GET_SALESMAN_INVENTORY + jsonArray;
        Log.d("urll1",url);
        Log.d("Date","Date:"+SharedPref.getLastSyncDate(getContext()));
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    powerManager = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
                    wakeLock = null;
                    if (powerManager != null) {
                        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StockistDashboardFragment");
                    }
                    if (wakeLock != null) {
                        wakeLock.acquire();
                    }
                    Constant.posts = new ArrayList<>();
                    sqLiteHandler.create_Write_db_object();
                    sqLiteHandler.mydb.beginTransaction();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                            JSONObject j_obj = response.getJSONObject(i);
                            stockColour = j_obj.getString("LN");
                            if (stockColour.contains(",")) {
                                String parts[] = stockColour.split(",");
                                color_code = parts[0];
                                legendName = parts[1];
                            } else {
                                color_code = stockColour;
                                legendName = "NA";
                            }
                            StockistProducts stockistProducts = new StockistProducts();
                            stockistProducts.setProduct_ID(j_obj.getString("P_ID"));
                            String productId = j_obj.getString("P_ID");
                            stockistProducts.setItemcode(j_obj.getString("IC"));
                            stockistProducts.setItemname(j_obj.getString("IM"));
                            stockistProducts.setPacksize(j_obj.getString("PZ"));
                            stockistProducts.setMRP(j_obj.getString("Mrp"));
                            stockistProducts.setRate(j_obj.getString("Rate"));
                            stockistProducts.setStock(j_obj.getString("Stk"));
                            stockistProducts.setMfgCode(j_obj.getString("MfgC"));
                            stockistProducts.setMfgName(j_obj.getString("MfgN"));
                            stockistProducts.setDoseForm(j_obj.getString("DF"));
                            stockistProducts.setScheme(j_obj.getString("Sche"));
                            stockistProducts.setHalfScheme(j_obj.getString("HSche"));
                            stockistProducts.setPercentScheme(j_obj.getString("PSche"));
                            stockistProducts.setBoxSize(j_obj.getString("BoxSize"));
                            stockistProducts.setLegendName(legendName);
                            stockistProducts.setLegendColor(color_code);
                            Constant.posts.add(stockistProducts);
                                Cursor cursor = sqLiteHandler.getProductByProductId(productId);
                                if (cursor != null)
                                {
                                 /* sqLiteHandler.updateProductsSalesman(stockistProducts.getProduct_ID(), stockistProducts.getItemcode(), stockistProducts.getItemname(),
                                            stockistProducts.getPacksize(), stockistProducts.getMRP(), stockistProducts.getRate(), stockistProducts.getStock(),
                                            stockistProducts.getMfgCode(), stockistProducts.getMfgName(), stockistProducts.getDoseForm(),
                                            stockistProducts.getScheme(), stockistProducts.getHalfScheme(), stockistProducts.getPercentScheme(), stockistProducts.getLegendName(), stockistProducts.getLegendColor(),stockistProducts.getBoxSize(),stockistProducts.getType(),clientId);
                                  Log.i("Legend Color--","----"+stockistProducts.getLegendName());
                               */
                                    sqLiteHandler.insetupdate_delete_into_salesman_products(stockistProducts.getProduct_ID(), stockistProducts.getItemcode(), stockistProducts.getItemname(),
                                            stockistProducts.getPacksize(), stockistProducts.getMRP(), stockistProducts.getRate(), stockistProducts.getStock(),
                                            stockistProducts.getMfgCode(), stockistProducts.getMfgName(), stockistProducts.getDoseForm(),
                                            stockistProducts.getScheme(), stockistProducts.getHalfScheme(), stockistProducts.getPercentScheme(), stockistProducts.getLegendName(), stockistProducts.getLegendColor(),stockistProducts.getBoxSize(),stockistProducts.getType(),clientId);

                                } else {
                                    sqLiteHandler.insetupdate_into_salesman_products(stockistProducts.getProduct_ID(), stockistProducts.getItemcode(), stockistProducts.getItemname(),
                                            stockistProducts.getPacksize(), stockistProducts.getMRP(), stockistProducts.getRate(), stockistProducts.getStock(),
                                            stockistProducts.getMfgCode(), stockistProducts.getMfgName(), stockistProducts.getDoseForm(),
                                            stockistProducts.getScheme(), stockistProducts.getHalfScheme(), stockistProducts.getPercentScheme(), stockistProducts.getLegendName(), stockistProducts.getLegendColor(),stockistProducts.getBoxSize(),stockistProducts.getType(),clientId);
                                }
                        }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    sqLiteHandler.mydb.setTransactionSuccessful();
                    sqLiteHandler.mydb.endTransaction();
                    sqLiteHandler.mydb.close();
                    Utility.hideSyncDialog();
                    currentDate();
                    if (response != null) {
                        Toast.makeText(getContext(), "" + response.length() + " Products Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                    wakeLock.release();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideSyncDialog();
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    /* Update Invoice List */
    private void updateInvoiceList() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(Integer.valueOf(userId));
        jsonArray.put(Integer.valueOf(clientId));
        jsonArray.put(SharedPref.getLastSyncDate(getContext()));
        String url = AppConfig.GetUpdatedPendingBillBySalesman + jsonArray;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    getInvoiceToUpdate(response);
                    //getDeliverySchedule("update");
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }
    /* Get Invoice List To Update */
    private void getInvoiceToUpdate(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject.getJSONArray("Invoice");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                    String chemistId = jsonObject1.getString("Client_ID");
                    String invoiceNo = jsonObject1.getString("InvoiceNo");
                    String invoiceId = jsonObject1.getString("Invoice_Id");
                    String invoiceDate = jsonObject1.getString("InvoiceDate");
                    String totalItems = jsonObject1.getString("Total_Items");
                    String billAmount = jsonObject1.getString("TotalAmount"); //Discounted Amount
                    String paymentReceived = jsonObject1.getString("Amount_Paid");
                    String balanceAmount = jsonObject1.getString("Balanceamt");
                    String paymentStatus = jsonObject1.getString("PaymentStatus");
                    String grandTotal = jsonObject1.getString("GrandTotal");
                    String totalDiscount = jsonObject1.getString("TotalDiscount");
                    String ledgerBalance = "NA";
                    String invoice_flag = jsonObject1.getString("CreditDebit");
                    if (invoiceId.equalsIgnoreCase("null")) {
                        invoiceId = "0";
                    }
                    if (invoiceDate.equalsIgnoreCase("null")) {
                        invoiceDate = "";
                    }
                    if (totalItems.equalsIgnoreCase("null")) {
                        totalItems = "0";
                    }
                    if (billAmount.equalsIgnoreCase("null")) {
                        billAmount = "0";
                    }
                    if (paymentReceived.equalsIgnoreCase("null")) {
                        paymentReceived = "0";
                    }
                    if (balanceAmount.equalsIgnoreCase("null")) {
                        balanceAmount = "0";
                    }
                    if (paymentStatus.equalsIgnoreCase("null")) {
                        paymentStatus = "";
                    }
                    if (grandTotal.equalsIgnoreCase("null")) {
                        grandTotal = "0";
                    }
                    if (totalDiscount.equalsIgnoreCase("null")) {
                        totalDiscount = "0";
                    }
                    if (ledgerBalance.equalsIgnoreCase("null")) {
                        ledgerBalance = "0";
                    }

                    Cursor cursor = sqLiteHandler.getInvoiceByInvoiceId(invoiceId);
                    if (cursor.getCount() > 0) {
                        sqLiteHandler.updateInvoiceReceivedAmount(userId, clientId, chemistId, invoiceNo, invoiceId, invoiceDate, totalItems,
                                billAmount, paymentReceived, balanceAmount, paymentStatus,grandTotal,totalDiscount,ledgerBalance,invoice_flag);
                    } else {
                        sqLiteHandler.insertInvoiceListPayment(userId, clientId, chemistId, invoiceNo, invoiceId, invoiceDate, totalItems,
                                billAmount, paymentReceived, balanceAmount, paymentStatus,grandTotal,totalDiscount,ledgerBalance,invoice_flag);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /* Current Date */
    private void currentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SharedPref.saveLastSyncDate(getContext(), simpleDateFormat.format(calendar.getTime()));
    }
    /* Current Date Format(dd-mm-yyyy) */
    private void currentDateDDMMYYYY() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Constant.currentDate = simpleDateFormat.format(calendar.getTime());
    }
    /* Save Call Plan Chemist List With Their Invoice  */
    private void insertCallPlanChemistList(String responseArray) {
        Cursor cursor = sqLiteHandler.getSalesmanChemistList(userId);
        if (cursor.getCount() > 0) {
            sqLiteHandler.deleteRecordFromSalesmanChemistList(userId);
        }
        sqLiteHandler.create_Write_db_object();
        sqLiteHandler.mydb.beginTransaction();
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
                if(DLExpiry.equalsIgnoreCase("null")) {
                    DLExpiry = "NA";
                }
                if(Block_Reason.equalsIgnoreCase("null")) {
                    Block_Reason="NA";
                }
                /* Insert Chemist Into Local DB */
                sqLiteHandler.insertChemistList(userId, chemistId, stockistCallPlanId, dayNumber, clientName, latitude, longitude,
                        address, profileImg, inTime, outTime, status, billAmount, sequence, taskStatus, orderReceived, passedDate,isLocked,DLExpiry,Block_Reason);
            }
            sqLiteHandler.mydb.setTransactionSuccessful();
            sqLiteHandler.mydb.endTransaction();
            sqLiteHandler.mydb.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /* Insert Invoices into SQLite */
    private void insertInvoiceSQLite(String responseData) {
        Cursor cursor = sqLiteHandler.getInvoiceByUserId(userId);
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            sqLiteHandler.deleteInvoiceByUserId(userId);
        }
        sqLiteHandler.create_Write_db_object();
        sqLiteHandler.mydb.beginTransaction();
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject.getJSONArray("Invoice");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                    String chemistId = jsonObject1.getString("Client_ID");
                    String invoiceNo = jsonObject1.getString("InvoiceNo");
                    String invoiceId = jsonObject1.getString("Invoice_Id");
                    String invoiceDate = Utility.convertDDMMYYYYtoYYYYMMDD(jsonObject1.getString("InvoiceDate"));
                    String totalItems = jsonObject1.getString("Total_Items");
                    String billAmount = jsonObject1.getString("TotalAmount");
                    String paymentReceived = jsonObject1.getString("Amount_Paid");
                    String balanceAmount = jsonObject1.getString("Balanceamt");
                    String paymentStatus = jsonObject1.getString("PaymentStatus");
                    String grandTotal = jsonObject1.getString("GrandTotal");
                    String totalDiscount = jsonObject1.getString("TotalDiscount");
                    String ledgerBalance = "NA";
                    String invoice_flag = jsonObject1.getString("CreditDebit");
                    //0=invoice, 1= credit note , 2= debit note

                    if (invoiceId.equalsIgnoreCase("null")) {
                        invoiceId = "0";
                    }
                    if (invoiceDate.equalsIgnoreCase("null")) {
                        invoiceDate = "";
                    }
                    if (totalItems.equalsIgnoreCase("null")) {
                        totalItems = "0";
                    }
                    if (billAmount.equalsIgnoreCase("null")) {
                        billAmount = "0";
                    }
                    if (paymentReceived.equalsIgnoreCase("null")) {
                        paymentReceived = "0";
                    }
                    if (balanceAmount.equalsIgnoreCase("null")) {
                        balanceAmount = "0";
                    }
                    if (paymentStatus.equalsIgnoreCase("null")) {
                        paymentStatus = "";
                    }
                    if (grandTotal.equalsIgnoreCase("null")) {
                        grandTotal = "0";
                    }
                    if (totalDiscount.equalsIgnoreCase("null")) {
                        totalDiscount = "0";
                    }
                    if (ledgerBalance.equalsIgnoreCase("null")) {
                        ledgerBalance = "0";
                    }

                    sqLiteHandler.insertInvoiceListPayment(userId, clientId, chemistId, invoiceNo, invoiceId, invoiceDate,
                            totalItems, billAmount, paymentReceived, balanceAmount, paymentStatus,grandTotal,totalDiscount,ledgerBalance,invoice_flag);
                }
            }
            sqLiteHandler.mydb.setTransactionSuccessful();
            sqLiteHandler.mydb.endTransaction();
            sqLiteHandler.mydb.close();
        } catch (JSONException e) {
            e.printStackTrace();
            sqLiteHandler.mydb.close();
        }
    }

    /* Insert Invoices into SQLite */
    private void insertDailyCollectionSQLite(String responseData) {

        Cursor cursor = sqLiteHandler.getDailyCollectionByUserId(userId);
        sqLiteHandler.deleteDailyCollectionByUserId(str1);
        sqLiteHandler.create_Write_db_object();
        sqLiteHandler.mydb.beginTransaction();
        try {
            JSONArray jsonArray = new JSONArray(responseData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String collection_PaymentID = jsonObject.getString("PaymentID");
                String collection_PaymnetAppID = jsonObject.getString("PaymnetAppID");
                String collection_Amount = jsonObject.getString("Amount");
                String collection_PaymentMode = jsonObject.getString("PaymentMode");
                String collection_PaymentDate = jsonObject.getString("PaymentDate");
                String collection_CreatedDate = jsonObject.getString("CreatedDate");
                String collection_Bank = jsonObject.getString("Bank");
                String collection_ChequeNo = jsonObject.getString("ChequeNo");
                String collection_ChequeAmt = jsonObject.getString("ChequeAmt");
                String collection_ChequeDate = jsonObject.getString("ChequeDate");
                String collection_MicrCode = jsonObject.getString("MicrCode");
                String collection_Narration = jsonObject.getString("Narration");
                String collection_ChemistID = jsonObject.getString("ChemistID");
                String collection_StockiestID = jsonObject.getString("StockiestID");
                String collection_CreatedBy = jsonObject.getString("CreatedBy");
                String collection_Comments = jsonObject.getString("Comments");
                String collection_Status = jsonObject.getString("Status");//InvoicesList
                String collection_InvoiceList = jsonObject.getString("InvoicesList");
                String collection_ChemistName = jsonObject.getString("Client_LegalName");

                String Flag = "online";
                /*Insert Payment Collection*/
                if (collection_PaymentMode.equalsIgnoreCase("Cheque")) {
                    sqLiteHandler.insertPaymentCollection(collection_StockiestID, userId, collection_ChemistID, collection_PaymentID, userId, collection_Amount,
                            collection_PaymentDate, "", "Submitted", collection_CreatedDate, collection_PaymentMode, collection_Bank, collection_ChequeNo,
                            collection_Amount, collection_ChequeDate, collection_MicrCode, collection_Narration, "", Flag,collection_InvoiceList,collection_ChemistName);
                } else {
                    sqLiteHandler.insertPaymentCollection(collection_StockiestID, userId, collection_ChemistID, collection_PaymentID, userId, collection_Amount,
                            collection_PaymentDate, "", "Submitted", collection_CreatedDate, collection_PaymentMode, collection_Bank, collection_ChequeNo, "", collection_ChequeDate,
                            collection_MicrCode, collection_Narration, "", Flag,collection_InvoiceList,collection_ChemistName);
                }


            }
            sqLiteHandler.mydb.setTransactionSuccessful();
            sqLiteHandler.mydb.endTransaction();
            sqLiteHandler.mydb.close();

        } catch (JSONException e) {
            e.printStackTrace();
            sqLiteHandler.mydb.close();
        }
    }


    private void insertDeliveryScheduleSQLite(String responseData) {
        Cursor cursor = sqLiteHandler.getDeliveryScheduleByUserId(userId);
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            sqLiteHandler.deleteDeliveryScheduleByUserId(userId);
            sqLiteHandler.deleteDeliveryInvoicesByUserId(userId);
        }
        sqLiteHandler.create_Write_db_object();
        sqLiteHandler.mydb.beginTransaction();
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String delivery_ClientID = jsonObject.getString("ClientID");
                String delivery_Client_Code = jsonObject.getString("Client_Code");
                String delivery_Client_Name = jsonObject.getString("Client_Name");
                String delivery_Client_Address = jsonObject.getString("Client_Address");

                JSONArray jsonArray1 = jsonObject.getJSONArray("invoice");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                    String delivery_ErpsalesmanID = jsonObject1.getString("ErpsalesmanID");
                    String delivery_DeliveryDate = jsonObject1.getString("DeliveryDate");
                    String delivery_DeliveryStatus = jsonObject1.getString("DeliveryStatus");
                    String delivery_OrderID = jsonObject1.getString("OrderID");
                    String delivery_InvoiceDate = jsonObject1.getString("InvoiceDate");
                    String delivery_BoxCount = "0";
                    String delivery_InvoiceNo = jsonObject1.getString("InvoiceNo");
                    String delivery_TotalAmount = jsonObject1.getString("TotalAmount");
                    String delivery_TotalItems = jsonObject1.getString("TotalItems");
                    String delivery_Package_count = jsonObject1.getString("Package_count");
                    String delivery_DeliveryId = jsonObject1.getString("DeliveryId");
                    String delivery_Description = jsonObject1.getString("Description");
                    if (delivery_DeliveryStatus.equals("1"))
                    {
                        delivery_UpdateFlag = "1";
                        //delivery_UpdateFlag = "Delivered";
                    }
                    else if (delivery_DeliveryStatus.equals("0"))
                    {
                        delivery_UpdateFlag = "0";
                        //delivery_UpdateFlag = "Pending";
                    }
                    else if (delivery_DeliveryStatus.equals("2"))
                    {
                        delivery_UpdateFlag = "2";
                    }
                    else
                    {
                        delivery_UpdateFlag = "0";
                    }

                    if (delivery_ErpsalesmanID.equalsIgnoreCase("null")) {
                        delivery_ErpsalesmanID = "0";
                    }
                    if (delivery_DeliveryDate.equalsIgnoreCase("null")) {
                        delivery_DeliveryDate = "0";
                    }
                    if (delivery_OrderID.equalsIgnoreCase("null")) {
                        delivery_OrderID = "0";
                    }
                    if (delivery_InvoiceDate.equalsIgnoreCase("null")) {
                        delivery_InvoiceDate = "0";
                    }
                    if (delivery_BoxCount.equalsIgnoreCase("null")) {
                        delivery_BoxCount = "0";
                    }
                    if (delivery_InvoiceNo.equalsIgnoreCase("null")) {
                        delivery_InvoiceNo = "0";
                    }
                    if (delivery_TotalAmount.equalsIgnoreCase("null")) {
                        delivery_TotalAmount = "0";
                    }
                    if (delivery_TotalItems.equalsIgnoreCase("null")) {
                        delivery_TotalItems = "0";
                    }
                    if (delivery_Package_count.equalsIgnoreCase("null")) {
                        delivery_Package_count = "0";
                    }
                    if (delivery_DeliveryId.equalsIgnoreCase("null")) {
                        delivery_DeliveryId = "0";
                    }
                    if (delivery_DeliveryStatus.equalsIgnoreCase("null")) {
                        delivery_DeliveryStatus = "Pending";
                    }
                    if (delivery_Description.equalsIgnoreCase("null")||delivery_Description.equalsIgnoreCase(""))
                    {
                        delivery_Description="Not Initiated";
                    }
                    if (delivery_Client_Name.equalsIgnoreCase("null") || delivery_Client_Name.equalsIgnoreCase("")) {
                        delivery_Client_Name = "Null";
                    }
                    sqLiteHandler.insertDeliveryScheduleInvoices(userId,delivery_ClientID,delivery_ErpsalesmanID,  delivery_DeliveryDate,delivery_DeliveryStatus, delivery_OrderID,
                            delivery_InvoiceDate,delivery_BoxCount,delivery_InvoiceNo,delivery_TotalAmount, delivery_TotalItems,
                            delivery_Package_count,delivery_DeliveryId,delivery_UpdateFlag,delivery_Description,delivery_Client_Name);

                }
                /*Insert Delivery Collection*/
                sqLiteHandler.insertTableDeliveryScheduleSalesman(delivery_ClientID,userId, delivery_Client_Code,  delivery_Client_Name,  delivery_Client_Address);

            }
            sqLiteHandler.mydb.setTransactionSuccessful();
            sqLiteHandler.mydb.endTransaction();
            sqLiteHandler.mydb.close();
        } catch (JSONException e) {
            e.printStackTrace();
            sqLiteHandler.mydb.close();
        }
    }
}

