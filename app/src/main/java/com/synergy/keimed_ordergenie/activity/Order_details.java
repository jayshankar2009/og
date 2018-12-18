package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_orderhistoryitems;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class Order_details extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    @BindView(R.id.rv_orderDetailslist)
    RecyclerView rv_orderDetailslist;
    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    @BindView(R.id.product_count)
    TextView _product_count;
    @BindView(R.id.Invdate)
    TextView _Invdate;
    @BindView(R.id.order_Id)
    TextView _order_Id;
    @BindView(R.id.invoice_amount)
    TextView _invoice_amount;

    @BindView(R.id.invoice_Id)
    TextView invoice_Id;

    @BindView(R.id.lbl_invoice_Id)
    TextView lbl_invoice_Id;

    @BindView(R.id.lbl_order_Id)
    TextView _lbl_order_Id;


    String login_type;
    private String User_id, Stockist_id;
    TextView txt_msg;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String orderDate;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    private String order_id;
    public Double MRP = 0.0;
    private SQLiteHandler db;
    public Double Price;

    @OnClick(R.id.txt_payment)
    void payment_click() {
        show_payment();
    }

 /*   @OnClick(R.id.fab)
    void orderhistory()
    {
        show_orderhistory();
    }
*/

    SharedPreferences pref;
    public static final String CHEMIST_ORDER_ID = "chemist_order_id";
    public static final String CHEMIST_ORDER_DATE = "chemist_order_date";
    public static final String STOCKIST_INVOICE_No = "invoice_id";
    String InvoiceId = "",Type="";
    String Status;

    List<m_orderhistoryitems> posts;


    /* Get Intent String */
    private String docNo;
    private String orderDateSql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
        }
        //Log.d("ClickORDERDETALS", "yse come click order details");

        ButterKnife.bind(this);
        db = new SQLiteHandler(this);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);


        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        txt_msg=(TextView)findViewById(R.id.empty_vieww);
        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");

        docNo = getIntent().getStringExtra("docNo");
        orderDateSql = getIntent().getStringExtra("orderDate");

        //Log.d("orderNoCheck", ""+docNo);

        try {
            Bundle bundle = getIntent().getExtras();
            String orderId = bundle.getString(CHEMIST_ORDER_ID);
            String invoice_no = bundle.getString("invoice_no");
            InvoiceId = bundle.getString(STOCKIST_INVOICE_No);
            Type=bundle.getString("type");
            orderDate = bundle.getString(CHEMIST_ORDER_DATE);
            if(Type.equals("orderlist"))
            {
                Status=bundle.getString("status");
                int status=Integer.parseInt(Status);
                if(status<3||status==5)
                    txt_msg.setVisibility(View.GONE);
            }
            Log.d("dataorderer11",Status);
            Log.d("dataorderer", orderId + "  " + invoice_no + "  " + orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                create_order();
                return true;
//            case R.id.home:
//            this.finish();
//                return true;

            case android.R.id.home:
                onBackPressed();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
        getintent();
    }




    private void get_order_Details_json(String jsondata) {
        if (!jsondata.isEmpty()) {
            Log.d("print_responced11", jsondata);
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            posts = new ArrayList<m_orderhistoryitems>();
            posts = Arrays.asList(mGson.fromJson(jsondata, m_orderhistoryitems[].class));
            _product_count.setText(posts.size() + " Products");

            for (int i = 0; i < posts.size(); i++) {
                if (posts.get(i).getMRP() != null) {
                    //Log.d("getNULL", posts.get(i).getMRP().toString());
                    MRP = MRP + posts.get(i).getMRP();
                }
            }
            _invoice_amount.setText("Rs." + df2.format(MRP));
            fill_orderlist();

        }
    }


    private void fill_orderlist() {
        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(Order_details.this);
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.adpter_order_details_list, parent, false);
                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(BindingViewHolder holder, int position) {
                m_orderhistoryitems order_Item = posts.get(position);
                holder.getBinding().setVariable(BR.v_orderDetailstlist, order_Item);
                holder.getBinding().executePendingBindings();

                /*if (Utility.internetCheck(Order_details.this)) {

                } else {
                    TextView itemMrp = (TextView) holder.getBinding().getRoot().findViewById(R.id.H_mrp);
                    itemMrp.setText(""+posts.get(position).getRate());
                }*/

                // _invoice_amount.setText("Rs."+MRP);
                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(Order_list.this,"clicked" , Toast.LENGTH_SHORT).show();
                        //Intent intent=new Intent(this,RequestStockistAccess.class);
                        // startActivity(intent);
                    }
                });

            }

            @Override
            public int getItemCount() {
                return posts.size();
            }
        };


        rv_orderDetailslist.setLayoutManager(new LinearLayoutManager(this));
        rv_orderDetailslist.setAdapter(adapter);


    }

    private void create_order() {
        Intent i = new Intent(Order_details.this, Order_Search.class);
      //  Intent i = new Intent(Order_details.this, Create_Order.class);
        startActivity(i);
    }

    private void show_payment() {
        Intent i = new Intent(Order_details.this, Payment_Option.class);
        startActivity(i);
    }

    private void show_orderhistory() {
        Intent i = new Intent(Order_details.this, OrderHistoryActivity.class);
        startActivity(i);
    }


    void getintent() {

        if (getIntent().getStringExtra("invoice_no") != null) {
            lbl_invoice_Id.setVisibility(View.VISIBLE);
            invoice_Id.setVisibility(View.VISIBLE);
            invoice_Id.setText(getIntent().getStringExtra("invoice_no"));
            _order_Id.setVisibility(View.GONE);
            _lbl_order_Id.setVisibility(View.GONE);
        } else {
            _order_Id.setVisibility(View.VISIBLE);
            _lbl_order_Id.setVisibility(View.VISIBLE);
            lbl_invoice_Id.setVisibility(View.GONE);
            invoice_Id.setVisibility(View.GONE);
        }

        order_id = getIntent().getStringExtra(CHEMIST_ORDER_ID);
        //Log.d("CHEMISTORDERID11",order_id);
        if (Utility.internetCheck(this)) {
            _order_Id.setText(order_id);
        } else {
            _order_Id.setText(docNo);
        }
        try {
//            Date convertedDate = dateFormat.parse(getIntent().getStringExtra(CHEMIST_ORDER_DATE));
//            String c_date = sdf.format(convertedDate);
            //_Invdate.setText(orderDate);
            _Invdate.setText(orderDateSql);
            //_Invdate.setText(c_date);

        } catch (Exception e) {
        }

        if (order_id != null) {

            if (login_type.equals(CHEMIST)) {
                Cursor cur_order_detail = db.get_chemist_order_details(order_id);
                JSONArray Orderdetail = ConstData.get_jsonArray_from_cursor(cur_order_detail);
                if (Orderdetail.length() < 1) {
                    /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_ORDERL_DETAILS,
                            AppConfig.GET_CHEMIST_ORDERL_DETAILS + order_id, this, true);*/
                    if (Utility.internetCheck(this)) {
                        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_ORDERL_DETAILS,
                                AppConfig.GET_CHEMIST_ORDERL_DETAILS + order_id, this, true);
                    } else {
                        getOfflineOrderItemsByOrderNo();
                    }
                } else {
                    get_order_Details_json(Orderdetail.toString());
                }
            } else {
                //if (InvoiceId!=null)
                //if invoice is not null the pass invoice id here
                /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_ORDERL_DETAILS,
                        AppConfig.GET_CHEMIST_ORDERL_DETAILS + order_id, this, true);*/
                if (Utility.internetCheck(this)) {
                    MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_ORDERL_DETAILS,
                            AppConfig.GET_CHEMIST_ORDERL_DETAILS + order_id, this, true);
                } else {
                    getOfflineOrderItemsByOrderNo();
                }
            }
        }
    }




    /* Get Offline Order Items */
    private void getOfflineOrderItemsByOrderNo() {
        Cursor cursor = db.getOrderItemsByOrderNo(docNo);
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            Toast.makeText(this, "Items not found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    String itemArray = cursor.getString(14);
                    //Log.d("itemArrayCheck", itemArray);
                    if (itemArray != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(itemArray);
                            //Log.d("offlineOrderArray", jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String itemName = jsonObject.getString("itemName");
                                String packSize = jsonObject.getString("packSize");
                                String quantity = jsonObject.getString("Qty");
                                //String rate = jsonObject.getString("Rate");
                                String price = jsonObject.getString("Price");


                                m_orderhistoryitems m_orderhistoryitems = new m_orderhistoryitems(itemName, packSize, Integer.valueOf(quantity),
                                        Double.valueOf(price), 0, 0);
                                posts.add(m_orderhistoryitems);


                                if (cursorCount == posts.size()) {
                                  //  _product_count.setText(posts.size() + " Products");
                                    _product_count.setText(String.valueOf(jsonArray.length())+ " Products");
                                    fill_orderlist();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (posts.size() > 0) {
                            for (int j = 0; j < posts.size(); j++) {
                                MRP = (MRP + posts.get(j).getMRP());
                            }
                        }
                        _invoice_amount.setText("Rs." + df2.format(MRP));
                    }
                } while (cursor.moveToNext());
            }
        }
    }





    /* APIs Success Response */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        try {
            if (response != null) {

            } else {
                OGtoast.OGtoast("unable to contact the server",
                        getBaseContext());
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            if (f_name.equals(AppConfig.GET_CHEMIST_ORDERL_DETAILS)) {
                get_order_Details_json(response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        db.insertInto_Chemist_order_details(response.getJSONObject(i).getString("OrderNo"),
                                response.getJSONObject(i).getString("ItemSrNo"),
                                response.getJSONObject(i).getString("Item_Name"),
                                //response.getJSONObject(i).getInt("MRP"),
                                response.getJSONObject(i).getDouble("MRP"),
                                response.getJSONObject(i).getInt("Packsize"),
                                response.getJSONObject(i).getInt("Qty"));

                        //Log.d("getttsMRPMRP", String.valueOf(response.getJSONObject(i).getInt("MRP")));
                    }
                } catch (Exception e) {

                }
            }

        } else {
            OGtoast.OGtoast("unable to contact the server",
                    getBaseContext());
        }

    }

    void save_order_details() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
