package com.synergy.keimed_ordergenie.activity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.model.m_callplans;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.synergy.keimed_ordergenie.BR;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_ORDER_DELIVERY_ASSIGN;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_PAYMENT_COLLECTION_ASSIGN;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_TAKE_ORDER_ASSIGN;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

;

public class DistributorCallPlan extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.rv_callPlanlist)
    RecyclerView rv_callPlanlist;
    String login_type;
    private String User_id, Stockist_id,User_Task;
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
    Boolean isTakOrder,isPayment,isDelivery;
    SimpleDateFormat sql_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
   // SimpleDateFormat sql_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    List<m_callplans> posts;
    List<m_callplans> posts_completed;
    List<m_callplans> posts_pending;
    List<m_callplans> posts_final;
    private static final String FORMAT = "%02d:%02d:%02d";
    ImageView img_take_order;
    ImageView img_payment;
    ImageView img_delivery;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_plan_distributor);

        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(this);

        //new get_current_location(CallPlan.this);
        txt_sel_date.setText(dateFormat.format(filter_start_date));
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        globalVariable = (AppController) getApplicationContext();
       // User_id = pref.getString(USER_ID, "0");
        User_id=getIntent().getStringExtra("user_id");
        User_Task=getIntent().getStringExtra("user_task");
       // Log.d("User_id",User_id);
        //Log.d("task","task:"+User_Task);
        editor.remove(IS_TAKE_ORDER_ASSIGN);
        editor.remove(IS_PAYMENT_COLLECTION_ASSIGN);
        editor.remove(IS_ORDER_DELIVERY_ASSIGN);
        if (User_Task.contains("1")) {
            editor.putBoolean(IS_TAKE_ORDER_ASSIGN, true);
            //editor.commit();
        }
        if (User_Task.contains("2")) {
            editor.putBoolean(IS_PAYMENT_COLLECTION_ASSIGN, true);
            //editor.commit();
        }
        if (User_Task.contains("3")) {
            editor.putBoolean(IS_ORDER_DELIVERY_ASSIGN, true);
            //editor.commit();
        }

        editor.commit();
        Stockist_id = pref.getString(CLIENT_ID, "0");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
        }

        if (login_type.equals(CHEMIST)) {


        } else {


          /*  MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                    AppConfig.GET_STOCKIST_CALL_PLAN +"["+User_id+","+get_day_number(filter_start_date)+",\""+ sdf.format(filter_start_date)+"\"]" , this, true);
*/
        }

     /*   http://www.ordergenie.co.in/api/stockistcallplandetails/getUserCallPlanDetails/[527,1,"2016-12-27"]*/

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
                DistributorCallPlan.this,
                nYear,
                nMonth,
                Nday
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setMaxDate(Calendar.getInstance());

    }

//    @OnClick(R.id.btn_show_route)
//    void onclcik_route() {
//    //    Log.e("insidelocation", "hiii");
//        Intent i = new Intent(DistributorCallPlan.this, MapsActivity.class);
//        i.putExtra("response", s_response_array);
//        startActivity(i);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_call_plan_distrib, menu);


        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<m_callplans> filteredModelList = new ArrayList<>();
                for (m_callplans model : posts) {
                    final String text = model.getClient_LegalName().toLowerCase();
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
//               globalVariable.setFromMenuItemClick(true);
//                new get_current_location(DistributorCallPlan.this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
     //   Log.d("todayDates12", get_day_number(filter_start_date));
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]", this, true);
//Stockist_id
        //User_id;

    }

    private void get_callist_json(String response) {

        //  LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Call_Plan.json", CallPlan.this);
        String jsondata = response.toString();

        if (!jsondata.isEmpty()) {
            Log.d("RES", jsondata);
            try {
           /*  JSONArray j_array=new JSONArray(jsondata);
             for(int i=0;i<j_array.length();i++)
             {
                 JSONObject j_object=j_array.getJSONObject(i);
                 db.insertInto_call_plan(j_object.getString("Call_Id"),j_object.getString("Client_id"),j_object.getString("Location"),
                        0,"",j_object.getInt("Delivery"),j_object.getInt("Payment"),j_object.getInt("Return"),0);
             }*/

            } catch (Exception e) {
            }


            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            posts = new ArrayList<m_callplans>();
            posts_final=new ArrayList<m_callplans>();
            posts_completed=new ArrayList<m_callplans>();
            posts_pending=new ArrayList<m_callplans>();
            posts = Arrays.asList(mGson.fromJson(jsondata, m_callplans[].class));
            posts_completed.clear();
            posts_pending.clear();
            posts_final.clear();
            for (int i=0;i<posts.size();i++)
            {
                if ( posts.get(i).getTaskStatus() != null) {
                    try {

                        JSONObject  taskStatusData = new JSONObject(posts.get(i).getTaskStatus());
                        if (taskStatusData != null && taskStatusData.getString("1").equals("yes") && taskStatusData.getString("2").equals("yes") && taskStatusData.getString("3").equals("yes"))
                        {
                            posts_completed.add(posts.get(i));
                            Log.d("posts_completed",String.valueOf(posts_completed.size()));
                        }
                        else
                        {
                            posts_pending.add(posts.get(i));
                            Log.d("posts_pending",String.valueOf(posts_pending.size()));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    posts_pending.add(posts.get(i));
                    Log.d("posts_pending",String.valueOf(posts_pending.size()));
                }

            }
            posts_final.clear();
            posts_final.addAll(posts_pending);
            posts_final.addAll(posts_completed);
            Log.d("posts_final",String.valueOf(posts_final.size()));
            fill_callplan(posts_final);

        }

    }


    private void fill_callplan(final List<m_callplans> posts) {
        if (posts.size() > 0) {
            findViewById(R.id.emptyview).setVisibility(View.GONE);
        } else {
            findViewById(R.id.emptyview).setVisibility(View.VISIBLE);
        }


        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                ViewDataBinding binding = null;
                LayoutInflater inflater = LayoutInflater.from(DistributorCallPlan.this);
                binding = DataBindingUtil.inflate(inflater, R.layout.adpter_callplan_list, parent, false);

                img_take_order = (ImageView) binding.getRoot().findViewById(R.id.img_take_order);
                img_payment = (ImageView) binding.getRoot().findViewById(R.id.img_payment);
                img_delivery = (ImageView) binding.getRoot().findViewById(R.id.img_delivery);


                lnr_start_call = (LinearLayout) binding.getRoot().findViewById(R.id.call_plan_start);
                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(final BindingViewHolder holder, final int position) {
                final m_callplans callplan_list = posts.get(position);
                holder.getBinding().setVariable(BR.v_callplanlist, callplan_list);
                String taskstatus = callplan_list.getTaskStatus();
               // Log.d("taskstatus",taskstatus);
                isDelivery=true;
                isPayment=true;
                isTakOrder=true;
                JSONObject taskStatusData = null;
                try {
                    if (taskstatus != null) {
                        taskStatusData = new JSONObject(taskstatus);

                    }
                    Resources res = getResources();
                    if (pref.getBoolean(IS_TAKE_ORDER_ASSIGN, false))
                    {
                        if (taskStatusData != null && taskStatusData.getString("1").equals("yes")) {
                            img_take_order.setImageDrawable(res.getDrawable(R.drawable.order_icon_done));
                        } else {

                            final Drawable drawable = res.getDrawable(R.drawable.order_icon);
                            img_take_order.setImageDrawable(drawable);
                        }
                    } else {

                        img_take_order.setBackgroundColor(getResources().getColor(R.color.white));
                        final Drawable drawable = res.getDrawable(R.drawable.order_inactive);
                        img_take_order.setImageDrawable(drawable);
                        isTakOrder=false;
                    }
                    if (pref.getBoolean(IS_PAYMENT_COLLECTION_ASSIGN, false)) {
               //         Log.e("IS_TAKE_ORDER_ASSIGNED2", pref.getBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, false) + "");
                        if (taskStatusData != null && taskStatusData.getString("2").equals("yes")) {
                            img_payment.setImageDrawable(res.getDrawable(R.drawable.rupee_icon_done));

                        } else {

                            final Drawable drawable = res.getDrawable(R.drawable.rupee_icon);
                            img_payment.setImageDrawable(drawable);
                        }
                    } else {

                   //   Log.e("IS_TAKE_ORDER_ASSIGNED2", pref.getBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, false) + "");
                        img_payment.setBackgroundColor(getResources().getColor(R.color.white));
                        final Drawable drawable = res.getDrawable(R.drawable.rupee_inactive);
                        img_payment.setImageDrawable(drawable);
                        isPayment=false;
                    }

                    if (pref.getBoolean(IS_ORDER_DELIVERY_ASSIGN, false)) {
                   //     Log.e("IS_TAKE_ORDER_ASSIGNED3", pref.getBoolean(IS_ORDER_DELIVERY_ASSIGNED, false) + "");

                        if (taskStatusData != null && taskStatusData.getString("3").equals("yes")) {
                            img_delivery.setImageDrawable(res.getDrawable(R.drawable.delivery_icon_done));
                        } else {

                            final Drawable drawable = res.getDrawable(R.drawable.delivery_icon);
                            img_delivery.setImageDrawable(drawable);
                        }
                    } else {
                   //     Log.e("IS_TAKE_ORDER_ASSIGNED3", pref.getBoolean(IS_ORDER_DELIVERY_ASSIGNED, false) + "");
                        img_delivery.setBackgroundColor(getResources().getColor(R.color.white));

                        final Drawable drawable = res.getDrawable(R.drawable.delivery_inactive);
                        img_delivery.setImageDrawable(drawable);
                        isDelivery=false;
                    }

                } catch (JSONException js) {
                    js.printStackTrace();
                }
                holder.getBinding().getRoot().setTag(R.id.key_call_plan_client_name, callplan_list.getClient_LegalName());
                holder.getBinding().getRoot().setTag(R.id.key_call_plan_location, callplan_list.getClientLocation());
//                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    //    Log.e("Click","item click");
//                         if (isDelivery||isTakOrder||isPayment) {
//                             JSONObject j_obj = new JSONObject();
//                             JSONArray j_array = new JSONArray();
//                             try {
//                                 j_obj.put("StockistCallPlanID", callplan_list.getStockistCallPlanID());
//                                 j_obj.put("Client_LegalName", callplan_list.getClient_LegalName());
//                                 j_obj.put("Latitude", callplan_list.getLatitude());
//                                 j_obj.put("Longitude", callplan_list.getLongitude());
//                                 j_obj.put("ClientLocation", callplan_list.getClientLocation());
//                                 j_array.put(j_obj);
//                             } catch (Exception e) {
//
//                             }
//
//                             Intent i = new Intent(DistributorCallPlan.this, CallPlanDetails.class);
//                             SharedPreferences.Editor editor = getSharedPreferences("MY PREF", MODE_PRIVATE).edit();
//                             editor.putString("client_name", holder.getBinding().getRoot().getTag(R.id.key_call_plan_client_name).toString());
//                             editor.putString("chemist_id", posts.get(position).getChemistID());
//                             editor.commit();
//                             // i.putExtra("client_name", holder.getBinding().getRoot().getTag(R.id.key_call_plan_client_name).toString());
//                             i.putExtra("Call_plan_id", posts.get(position).getStockistCallPlanID().toString());
//                             i.putExtra("response", j_array.toString());
//                             i.putExtra("chemist_id", posts.get(position).getChemistID());
//                             i.putExtra("chemist_id", posts.get(position).getChemistID());
//                             i.putExtra("task_status", posts.get(position).getTaskStatus());
//                             i.putExtra("filter_start_date", sql_dateFormat.format(filter_start_date));
//                             // globalVariable.setClick_on_menuitem(false);
//                             //  i.putExtra("client_location",  holder.getBinding().getRoot().getTag(R.id.key_call_plan_location).toString());
//                             globalVariable.setFromMenuItemClick(false);
//                             startActivity(i);
//                         }
//
//                    }
//
//                });

                try {

                    if (isDelivery||isPayment||isTakOrder) {
                        holder.getBinding().getRoot().findViewById(R.id.callstartImg).setVisibility(View.VISIBLE);
                        holder.getBinding().getRoot().findViewById(R.id.callstartText).setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lnr_start_call.setVisibility(View.INVISIBLE);
                    }
                    if (!callplan_list.getTaskStatus().equals("")) {

                        //  JSONObject taskStatusData = new JSONObject(callplan_list.getTaskStatus());

                        Boolean alltaskcompleted = false;
                        if (taskStatusData != null && taskStatusData.getString("1").equals("yes") && taskStatusData.getString("2").equals("yes") && taskStatusData.getString("3").equals("yes")) {
                            alltaskcompleted = true;
                        }

                        if (!alltaskcompleted) {
                            holder.getBinding().getRoot().findViewById(R.id.callstartImg).setVisibility(View.VISIBLE);
                            holder.getBinding().getRoot().findViewById(R.id.callstartText).setVisibility(View.VISIBLE);
                        } else {
                            holder.getBinding().getRoot().findViewById(R.id.callstartImg).setVisibility(View.GONE);
                            ((TextView) holder.getBinding().getRoot().findViewById(R.id.callstartText)).setText("Call Completed");
                        }

                    } else {

                    }
                } catch (Exception e) {

                }
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




    /* APIs Success Response */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            try {
         //Log.d("todayDates12", String.valueOf(response));
                if (f_name.equals(AppConfig.GET_STOCKIST_CALL_PLAN)) {

                    s_response_array = response.toString();
                    get_callist_json(response.toString());
                }

            } catch (Exception e) {
                e.toString();
            }
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

// Running API
        /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                AppConfig.GET_STOCKIST_CALL_PLAN +"["+User_id+","+get_day_number(filter_start_date)+",\""+ sdf.format(filter_start_date)+"\"]" , this, true);
*/
       /* MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                AppConfig.GET_STOCKIST_CALL_PLAN +"["+Stockist_id+","+"1"+",2016-12-27+]" , this, true);
*/


        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_CALL_PLAN,
                AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]", this, true);
      //  Log.d("callplan",AppConfig.GET_STOCKIST_CALL_PLAN + "[" + User_id + "," + get_day_number(filter_start_date) + ",\"" + sdf.format(filter_start_date) + "\"]");
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


    /*private String get_day_number(Date d)
    {
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String daynumber="1";

        switch (day) {

            case Calendar.SUNDAY:
                daynumber="1";
                break;

            case Calendar.MONDAY:
                daynumber="2";
                break;
            case Calendar.TUESDAY:
                daynumber="3";
                break;
            case Calendar.WEDNESDAY:
                daynumber="4";
                break;
            case Calendar.THURSDAY:
                daynumber="5";
                break;
            case Calendar.FRIDAY:
                daynumber="6";
                break;
            case Calendar.SATURDAY:
                daynumber="7";
                break;

            *//*case Calendar.SUNDAY:
                daynumber="7";
                // Current day is Sunday
                break;*//*
        }
        return daynumber;
    }*/




    /* onBackPressed */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


