package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.utils.get_current_location;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_order;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_ORDERLIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class OrderHistoryActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess,DatePickerDialog.OnDateSetListener {

    @BindView(R.id.empty_view)
    TextView empty_view;

    @BindView(R.id.total_count)
    TextView _total_count;


    @BindView(R.id.rv_orderhistory)
    RecyclerView rv_orderhistory;

    @BindView(R.id.fab_options)
    FloatingActionMenu fab_options;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    //@BindView(R.id.toolbar)
   // Toolbar _toolbar;
    TextView txt_start_date;
    TextView txt_end_date;


    String login_type;
    DatePickerDialog dpd_start_date, dpd_end_date;
    private Date current_date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static final String CHEMIST_ORDER_DATE = "chemist_order_date";
    public static final String CHEMIST_ORDER_ID = "chemist_order_id";
    private SearchView searchView;
    public static final String SELECTED_CHEMIST_ID = "selected_chemist_id";
    private Boolean IsCallPlanTask;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    private Date filter_start_date, filter_end_date;
    //List<m_orderhistory> posts = new ArrayList<m_orderhistory>();
    public static final String CHEMIST_IS_LAST_10_ORDER = "chemist_is_last_10_orders";
    private SQLiteHandler db;
    List<m_order> posts = new ArrayList<m_order>();
    private String User_id, Stockist_id;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_order_list);
        setContentView(R.layout.activity_order_history);



        ButterKnife.bind(this);
        db = new SQLiteHandler(this);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        IsCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);
      //  setSupportActionBar(_toolbar);
        //_toolbar.setTitle("Orders");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
            actionBar.setTitle(" Last 10 Orders");
        }


        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");

        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");

        if (login_type.equals(CHEMIST)) {

            Log.d("useCHEMIST11","YES USE CHEMIST ACC");

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


           //Log.d("chemist_id",Stockist_id+" "+User_id);
//***************testing***************//
//            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY,
//                    AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY +"["+Stockist_id+","+User_id+"]" , this, true);
//
//***************testing***************//
            Log.d("useCHEMIST112","YES USE CHEMIST ACC");
            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY,
                    AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY +"["+Stockist_id+","+User_id+"]" , this, true);
            System.out.println("Stockistidcheck:"+Stockist_id+"Useridcheck: "+User_id);
//http://www.ordergenie.co.in/ap Log.d("useCHEMIST11","YES USE CHEMIST ACC");i/orders/APP_GetStockistOrderHistory/["1240","1982"]
            //URL_IPADDRESS +"/api/orders/APP_GetStockistOrderHistory/";

        }

    }

    @OnClick(R.id.fab_bills)
    void show_pendingbills() {

        Intent intent = new Intent(OrderHistoryActivity.this, AllPendingBills.class);
        startActivity(intent);

    }

    @OnClick(R.id.fab_sales)
    void show_sales() {
        Intent intent = new Intent(OrderHistoryActivity.this, SalesReturnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    void onclick_fab() {
        new get_current_location(OrderHistoryActivity.this);

    }

    @OnClick(R.id.fab_stockist_list)
    void show_Stockist() {
        Intent intent = new Intent(OrderHistoryActivity.this, StockistList.class);
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
                    final String text = model.getCustomer_Name().toLowerCase();
                    final String order_id = model.getOrder_Id().toString();

                    if (text.contains(newText) || order_id.contains(newText)) {
                        filteredModelList.add(model);
                    }
                }
                fill_orderlist(filteredModelList);
                rv_orderhistory.scrollToPosition(0);

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

            case R.id.action_location:

                new get_current_location(OrderHistoryActivity.this);


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    private void get_order_list_json(String jsondata) {

        // posts.clear();

        Log.d("useCHEMIST1233","YES USE CHEMIST ACC");

       // Log.d("useCHEMIST1333",jsondata);

/*
       // LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("order_list.json", Order_list.this);
      //  Stri//ng jsondata = _LoadJsonFromAssets.getJson();*/
        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            posts = new ArrayList<m_order>();

            posts = new LinkedList<m_order>(Arrays.asList(mGson.fromJson(jsondata, m_order[].class)));
            // posts = Arrays.asList(mGson.fromJson(jsondata, m_order[].class));

            Cursor crs_order_json = db.get_order_json();

            if (crs_order_json != null && crs_order_json.getCount() > 0) {
                while (crs_order_json.moveToNext()) {

                    String json = crs_order_json.getString(crs_order_json.getColumnIndex("json"));

                    try {

                        JSONArray j_arr = new JSONArray(json);
                        m_order o_m_order = new m_order();
                        o_m_order.setCustomer_Name(pref.getString(CLIENT_NAME, ""));
                        o_m_order.setOrder_Id(0);
                        o_m_order.setOrderDate(j_arr.getJSONObject(0).getString("Doc_Date"));
                        o_m_order.setOrderNo(j_arr.getJSONObject(0).getString("DOC_NO"));
                        o_m_order.setStatus(0);
                        o_m_order.setOrderInvoiceDate(j_arr.getJSONObject(0).getString("Invoice_Date"));
                        o_m_order.setLine_Item_Count((j_arr.getJSONObject(0).getString("line_Item_Count")));

                        o_m_order.setBalenceAmount((j_arr.getJSONObject(0).getString("Balance_Amt")));

                        o_m_order.setOrdersStatus((j_arr.getJSONObject(0).getString("Order_Status")));

                       // o_m_order.setInvoice_Amt((j_arr.getJSONObject(0).getString("Invoice_Amt")));

                        //j_arr.getJSONObject(0).getString("line_Item_Count")
                        o_m_order.setOrderDate(String.valueOf(0));
                        o_m_order.setType(-99);
                      //  Log.d("SaiRam", String.valueOf(j_arr));
                        posts.add(o_m_order);
                    } catch (Exception e) {
                        e.toString();
                    }
                }
            }

            try {
                Collections.sort(posts, new CustomComparator());
            } catch (Exception e) {

            }
            fill_orderlist(posts);

            if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
                try {
                    Collections.sort(posts, new  CustomComparator());
                } catch (Exception e) {

                }
            }

        }


    }


    private void fill_orderlist(final List<m_order> posts_s) {


        if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
            _total_count.setText("10 Orders.");
        } else {
            _total_count.setText(posts_s.size() + " Orders.");
        }


        if (posts_s.isEmpty()) {
            empty_view.setVisibility(View.VISIBLE);
            rv_orderhistory.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            rv_orderhistory.setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {

            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(OrderHistoryActivity.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.adapterr_orderrhistory, parent, false);


                return new BindingViewHolder(binding.getRoot());
            }


            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                m_order order_list = posts_s.get(position);
                holder.getBinding().setVariable(BR.v_ordertlist, order_list);
                holder.getBinding().executePendingBindings();

                TextView order_Id = (TextView) holder.getBinding().getRoot().findViewById(R.id.order_Id);
                TextView orderDate = (TextView) holder.getBinding().getRoot().findViewById(R.id.orderDate);
                TextView invoice_Idd = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoice_Idd);
                TextView h_invoicedate = (TextView) holder.getBinding().getRoot().findViewById(R.id.h_invoicedate);
                TextView invoicedate = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoicedate);
                TextView h_invoiceamount = (TextView) holder.getBinding().getRoot().findViewById(R.id.h_invoiceamount);
                TextView invoiceamount = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoiceamount);

                TextView invoiceDate = (TextView) holder.getBinding().getRoot().findViewById(R.id.Cust_Namee);

                TextView totalItems = (TextView) holder.getBinding().getRoot().findViewById(R.id.orderDatee);

                TextView totalorderamount = (TextView) holder.getBinding().getRoot().findViewById(R.id.orderamount);

                totalorderamount.setText(posts_s.get(position).getBalenceAmount());




                invoice_Idd.setText(posts_s.get(position).getOrdersStatus());
//orderamount

                //totalinvoiceamountt.setText(posts_s.get(position).getInvoice_Amt());
               totalItems.setText(posts_s.get(position).getLine_Item_Count());
                //orderDatee
                //invoiceamountt
                try {

                    if (posts_s.get(position).getOrderInvoiceDate()!=null)
                    {
                        invoiceDate.setText(sdf.format(dateFormat.parse(posts_s.get(position).getOrderInvoiceDate())));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    orderDate.setText(sdf.format(dateFormat.parse(posts_s.get(position).getOrderDate())));

                } catch (Exception e) {

                    e.printStackTrace();
                }


                if (posts_s.get(position).getInvoiceNo() != null) {
                   // invoice_Id.setText(posts_s.get(position).getInvoiceNo());


                    try {
                        invoicedate.setText(sdf.format(dateFormat.parse(posts_s.get(position).getInvoiceDate())));

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    invoiceamount.setText(posts_s.get(position).getInvoice_Amt());

                    invoicedate.setVisibility(View.VISIBLE);
                    invoiceamount.setVisibility(View.VISIBLE);
                    h_invoicedate.setVisibility(View.VISIBLE);
                    h_invoiceamount.setVisibility(View.VISIBLE);

                } else {
                    //invoice_Id.setText("Invoice Not Created Yet.");
                    invoicedate.setVisibility(View.GONE);
//                    invoiceamount.setVisibility(View.GONE);
                    h_invoicedate.setVisibility(View.GONE);
                    h_invoiceamount.setVisibility(View.GONE);
                }


                if (login_type.equals(CHEMIST)) {

                    if(posts_s.get(position).getOrder_Id().toString().equals("0")){
                        order_Id.setText("Offline");
                    }else {
                        order_Id.setText(posts_s.get(position).getOrder_Id().toString());
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

                            Log.d("CHEMISTLOGIN","CHEMIST LOGIN");
                            intent = new Intent(OrderHistoryActivity.this, Order_details.class);
                            intent.putExtra(CHEMIST_ORDER_ID, posts_s.get(position).getOrder_Id().toString());
                            intent.putExtra(CHEMIST_ORDER_DATE, posts_s.get(position).getOrderDate().toString());
                            if (posts_s.get(position).getType() == -99) {
                                OGtoast.OGtoast("This order is not completed yet. Please connect to a network to complete this order.", OrderHistoryActivity.this);
                                return;
                            }
                            startActivity(intent);
                        } else {

                            Log.d("CHEMISTLOGIN","SALESSSS LOGIN");
                            if (!IsCallPlanTask) {
                                intent = new Intent(OrderHistoryActivity.this, Order_details.class);
                                intent.putExtra(CHEMIST_ORDER_ID, posts_s.get(position).getOrderNo().toString());
                                if (posts_s.get(position).getOrderDate() != null) {
                                    intent.putExtra(CHEMIST_ORDER_DATE, posts_s.get(position).getOrderDate());
                                }
                                startActivity(intent);
                            } else {

                                intent = new Intent(OrderHistoryActivity.this, Delivery.class);
                                intent.putExtra("client_id", getIntent().getStringExtra("client_id"));
                                intent.putExtra("receiver", getIntent().getParcelableExtra("receiver"));
                                intent.putExtra("order_no", posts_s.get(position).getOrderNo());
                                intent.putExtra("customer_name", posts_s.get(position).getCustomer_Name());
                                intent.putExtra("order_count", posts_s.get(position).getItems());

                                if (posts_s.get(position).getInvoiceDate() != null) {
                                    intent.putExtra("order_date", posts_s.get(position).getInvoiceDate());
                                } else {
                                    intent.putExtra("order_date", posts_s.get(position).getOrderDate());
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


                if (order_list.getType() == -99) {
                    /*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));*/

                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));

                } else {
                    if (order_list.getStatus() < 4) {

                        /*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));*/

                        ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));
                    }
                }


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

        rv_orderhistory.setLayoutManager(new LinearLayoutManager(this));
        rv_orderhistory.setAdapter(adapter);

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
        Intent i = new Intent(OrderHistoryActivity.this, Order_Search.class);
      //  Intent i = new Intent(OrderHistoryActivity.this, Create_Order.class);
        startActivity(i);
    }

    void get_stockist_order_list() {

        get_order_list_json(pref.getString(CHEMIST_ORDERLIST, "[]"));
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {


        if (response != null) {
            try {


             //   Log.d("RESPONCEORDER11",f_name);
              //  Log.d("RESPONCEORDER1233", String.valueOf(response));

//GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY
                /*if (f_name.equals(AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_SUMMARY)) {

                    get_order_list_json(response.toString());
                }*/

                if (f_name.equals(AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY)) {


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
                        OrderHistoryActivity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_start_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_start_date.setMaxDate(Calendar.getInstance());
                ;
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
                        OrderHistoryActivity.this,
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
                        if (model.getStatus() == 0) {
                            filteredModelList.add(model);
                        }
                    } else if (isCompletedChecked) {
                        if (model.getStatus() == 1) {
                            filteredModelList.add(model);
                        }
                    } else {
                        filteredModelList.add(model);
                    }
                }

            } else {
                if (isPendingChecked) {
                    if (model.getStatus() == 0) {
                        filteredModelList.add(model);
                    }
                }

                if (isCompletedChecked) {
                    if (model.getStatus() == 1) {
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

S

          }*/
    public class CustomComparator implements Comparator<m_order> {
        @Override
        public int compare(m_order o1, m_order o2) {

            return o2.getOrderDate().compareTo(o1.getOrderDate());
            //  return o1.getOrderDate().compareTo(o2.getOrderDate());
        }
    }

    public Intent getSupportParentActivityIntent() {

        Intent newIntent = null;
        try {
            if (!IsCallPlanTask) {
                newIntent = new Intent(OrderHistoryActivity.this, MainActivity.class);
            }
            //you need t o define the class with package name
            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIntent;
    }














       /* ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String login_type=pref.getString(ConstData.user_info.CLIENT_ROLE,"");

        User_id=pref.getString(USER_ID,"0");
        Stockist_id=pref.getString(CLIENT_ID,"0");

        Log.d("STOCKIST",Stockist_id);
        Log.d("USER",User_id);


        if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
            getSupportActionBar().setTitle(" Last 10 Orders");

        }


        if (login_type.equals(CHEMIST)) {


        } else {
            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY,
                    AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY +"["+Stockist_id+","+User_id+"]" , this, true);

        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_history_list, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter:
                show_filter_dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void json_update(  List<m_orderhistory> posts_s) {

           // ad_orderhistory adapter;

        LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("orders.json", OrderHistoryActivity.this);
        String jsondata = _LoadJsonFromAssets.getJson();
        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            Type listType = new TypeToken<List<m_orderhistory>>() {}.getType();
            posts_s = mGson.fromJson(jsondata, listType);
        }
       // adapter = new ad_orderhistory(posts_s,this);
        rv_orderhistory.setLayoutManager(new LinearLayoutManager(this));
        //rv_orderhistory.setAdapter(adapter);


        }





    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        Log.d("ORDERSRESPONCE11",f_name);
        Log.d("ORDERSRESPONCE12", String.valueOf(response));


        if(response!=null)
        {
            try {


                if(f_name.equals(AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY))
                {
                    get_order_list_json(response.toString());
                }

            }catch (Exception e)
            {
                e.toString();
            }
        }




    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }



    private void get_order_list_json(String jsondata) {

        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();
            Log.e("ORDERSRESPONCE13", jsondata);
            posts = new ArrayList<m_orderhistory>();
            posts = Arrays.asList(mGson.fromJson(jsondata, m_orderhistory[].class));

            //
            //Collections.sort(posts, new CustomComparator());

            try {
                Collections.sort(posts, new CustomComparator());
            } catch (Exception e) {

            }




          //  ad_orderhistory adapter;
           // adapter = new ad_orderhistory(posts, this);
            rv_orderhistory.setLayoutManager(new LinearLayoutManager(this));
            //rv_orderhistory.setAdapter(adapter);
            //json_update(posts);

            fill_orderlistHistory(posts);
            if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
                try {
                    Collections.sort(posts, new CustomComparator());
                } catch (Exception e) {

                }


            }


        }
    }

    private void fill_orderlistHistory(final List<m_orderhistory> posts) {

        if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
            //_total_count.setText("10 Orders.");
        } else {
           // _total_count.setText(posts.size() + " Orders.");
        }


        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {
        //    private List<m_orderhistory> _datalist;

            private ViewDataBinding binding;
            ad_childorderhistory adapter;
            private Context context;
            private RecyclerView rv_orderlistitems;
            private CustomLinearLayoutManager layoutManager;
            private TextView btn_show_items;



            *//*public ad_orderhistory(List<m_orderhistory> _datalistModal ,Context context) {

                if (_datalistModal == null) {
                    throw new IllegalArgumentException(
                            "_datalist must not be null");
                }
                this._datalist = _datalistModal;
                this.context=context;

            }*//*

            @Override
            public BindingViewHolder onCreateViewHolder(
                    ViewGroup viewGroup, int viewType) {

                BindingViewHolder itemView = null;
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                binding = DataBindingUtil.inflate(inflater, R.layout.adapter_order_history, viewGroup, false);
                itemView = new BindingViewHolder(binding.getRoot());

                layoutManager = new CustomLinearLayoutManager(viewGroup.getContext(), LinearLayoutManager.VERTICAL,false);

                rv_orderlistitems=(RecyclerView)binding.getRoot().findViewById(R.id.rv_orderlistitems);

                btn_show_items=(TextView) binding.getRoot().findViewById(R.id.btn_show_items);

                btn_show_items.setTag(rv_orderlistitems);


                return  itemView;
            }

            @Override
            public void onBindViewHolder(
                    final BindingViewHolder viewHolder,final int position) {

                m_orderhistory pendingbills =posts.get(position);
                viewHolder.getBinding().setVariable(BR.v_orderhistory, pendingbills);
                viewHolder.getBinding().executePendingBindings();
                ad_childorderhistory adapter = new ad_childorderhistory(pendingbills.getline_items());
                rv_orderlistitems.setAdapter(adapter);
                rv_orderlistitems.setHasFixedSize(false);
                rv_orderlistitems.setLayoutManager(new LinearLayoutManager(context));


                rv_orderlistitems.setTag(position);


                // COMMENT 1 MAy

              *//*  btn_show_items.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if ( ((RecyclerView)view.getTag()).getVisibility() == View.GONE) {
                            ((RecyclerView)view.getTag()).setVisibility(View.VISIBLE);
                            ((TextView)view).setText("Hide Items");
                            notifyDataSetChanged();

                        } else {
                            ((RecyclerView)view.getTag()).setVisibility(View.GONE);
                            ((TextView)view).setText("Show Items");
                            notifyDataSetChanged();
                        }
                    }
                });*//*

            }

            @Override
            public int getItemCount() {

                if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
                    if (posts.size() > 10) {
                        return 10;
                    } else {
                        return posts.size();
                    }
                } else {
                    return posts.size();
                }


            }
            @Override
            public int getItemViewType(int position) {

                return position;
            }


        };

        rv_orderhistory.setLayoutManager(new LinearLayoutManager(this));
        rv_orderhistory.setLayoutManager(new LinearLayoutManager(this));
        rv_orderhistory.setAdapter(adapter);



    }

    private void show_filter_dialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_stockist_order_history_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);

        txt_start_date=(TextView) dialogview.findViewById(R.id.txt_start_date);
        txt_end_date=(TextView) dialogview.findViewById(R.id.txt_end_date);




        if(filter_start_date!=null)
        {
            txt_start_date.setText(sdf.format(filter_start_date));
        }
        if(filter_end_date!=null)
        {
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

                    nYear=Integer.parseInt(sdYear.format(current_date));
                    nMonth=Integer.parseInt(sdMonth.format(current_date))-1;
                    Nday=Integer.parseInt(sdDay.format(current_date));

                }catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }


                dpd_start_date = DatePickerDialog.newInstance(
                        OrderHistoryActivity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_start_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_start_date.setMaxDate(Calendar.getInstance());;
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

                    nYear=Integer.parseInt(sdYear.format(current_date));
                    nMonth=Integer.parseInt(sdMonth.format(current_date))-1;
                    Nday=Integer.parseInt(sdDay.format(current_date));

                }catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }


                dpd_end_date = DatePickerDialog.newInstance(
                        OrderHistoryActivity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_end_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_end_date.setMaxDate(Calendar.getInstance());;
            }


        });

        dialogview.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter_dialog_conditions(filter_start_date,filter_end_date);
                infoDialog.dismiss();
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                json_update(posts);
                infoDialog.dismiss();
            }
        });


        set_attributes(infoDialog);
        infoDialog.show();


    }

    private void set_attributes(Dialog dlg)
    {

        Window window = dlg.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);

        int[] textSizeAttr = new int[] { android.R.attr.actionBarSize };
        int indexOfAttrTextSize = 0;
        TypedValue typedValue = new TypedValue();
        TypedArray a = this.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionbarsize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        int maxX = mdispSize.x;
        wlp.gravity = Gravity.TOP|Gravity.LEFT;
        wlp.x = maxX;   //x position
        wlp.y =actionbarsize-20;   //y position
        window.setAttributes(wlp);

    }


    private void filter_dialog_conditions(Date startDate, Date endDate) {
        try {

        Date convertedDate = null;
        final List<m_orderhistory> filteredModelList = new ArrayList<>();
        for (m_orderhistory model : posts) {

            if (startDate != null && endDate != null) {
                final String date = model.getInvoice_Date();
                try {
                    convertedDate = dateFormat.parse(date);
                    String c_date = sdf.format(convertedDate);
                   // convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                    convertedDate = dateFormat.parse(c_date);
                } catch (Exception e) {
                }

                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                {

                    filteredModelList.add(model);


                }
            }
            json_update(filteredModelList);
        }
        } catch (Exception e) {
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if(dpd_start_date!=null) {
            if (view.getId() == dpd_start_date.getId()) {
                try {
                    filter_start_date = dateFormat.parse(year + "-" + (monthOfYear+1) + "-" + dayOfMonth + "T00:00:00.000Z");
                    txt_start_date.setText(year + "-" +( monthOfYear+1) + "-" + dayOfMonth);
                } catch (Exception e) {
                    e.toString();
                }
            }
            dpd_start_date=null;
        }

        if(dpd_end_date!=null) {
            if (view.getId() == dpd_end_date.getId()) {
                try {
                    filter_end_date = dateFormat.parse(year + "-" + (monthOfYear+1) + "-" + dayOfMonth + "T00:00:00.000Z");
                    txt_end_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                } catch (Exception e) {
                }
                dpd_end_date=null;
            }

        }
    }

    public class CustomComparator implements Comparator<m_orderhistory> {
        @Override
        public int compare(m_orderhistory o1, m_orderhistory o2) {

            return o2.getInvoice_Date().compareTo(o1.getInvoice_Date());
            //  return o1.getOrderDate().compareTo(o2.getOrderDate());
        }
    }*/
    }
