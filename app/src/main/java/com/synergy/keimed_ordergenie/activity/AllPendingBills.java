package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.get_current_location;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.Exad_pending_all_bill;
import com.synergy.keimed_ordergenie.model.m_all_pending_list;
import com.synergy.keimed_ordergenie.model.m_all_pending_list_line_items;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_STOCKIST_PENDING_BILLS;
import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_PENDING_BILLS;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class AllPendingBills extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, MakeWebRequest.OnResponseSuccess {

    private SearchView searchView;
    @BindView(R.id.exp_pending_bill)
    ExpandableListView _exp_pending_bill;
    List<m_all_pending_list> posts = new ArrayList<m_all_pending_list>();
    m_all_pending_list_line_items m_all_pending;
    List<m_all_pending_list_line_items> posttt;
    List<m_all_pending_list> posttt1;
    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    String client_id;
    @BindView(R.id.txt_total_count)
    TextView _txt_total_count;
    SharedPreferences pref;
    private TextView txt_start_date, txt_end_date;

    @BindView(R.id.fab_options)
    FloatingActionMenu fab_options;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    String login_type;
    DatePickerDialog dpd_start_date, dpd_end_date;
    private Date current_date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    private String User_id, Stockist_id;
    private Date filter_start_date, filter_end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_all_pending_bills);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
            }

        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        client_id=pref.getString(CLIENT_ID,"");
        _exp_pending_bill.setEmptyView(findViewById(R.id.empty_view));
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");
        // Log.d("userid_stockid",Stockist_id+" "+User_id);
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Pending Bill");
        if (login_type.equals(CHEMIST)) {
            fab_options.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_PEDNING_BILLS,
                    AppConfig.GET_CHEMIST_PEDNING_BILLS + client_id, this, true);

        } else {
            fab_options.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            Log.d("userDetails",Stockist_id+" "+User_id);
            MakeWebRequest.MakeWebRequest("get", GET_STOCKIST_PENDING_BILLS,
                    GET_STOCKIST_PENDING_BILLS + "[" + Stockist_id + "," + User_id + "]", this, true);
            Log.d("API",GET_STOCKIST_PENDING_BILLS + "[" + Stockist_id + "," + User_id + "]");
            }

        _exp_pending_bill.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                _exp_pending_bill.smoothScrollToPosition(groupPosition);
                return false;
            }
        });

    }

    @OnClick(R.id.fab_order)
    void show_order() {
        if (login_type.equals(CHEMIST)) {
            Intent intent = new Intent(AllPendingBills.this, Order_list.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(AllPendingBills.this, OrderHistoryActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fab_sales)
    void show_sales() {
        Intent intent = new Intent(AllPendingBills.this, SalesReturnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab_stockist_list)
    void show_Stockist() {
        Intent intent = new Intent(AllPendingBills.this, StockistList.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    void fab_click() {
        new get_current_location(AllPendingBills.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all_pending_list, menu);

        if (login_type.equals(CHEMIST)) {
            menu.findItem(R.id.action_location).setVisible(true);
        } else {
            menu.findItem(R.id.action_location).setVisible(false);
        }

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Stockiest Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<m_all_pending_list> filteredModelList = new ArrayList<>();
                for (m_all_pending_list model : posts) {
                    final String text = model.getCust_name().toLowerCase();
                    if (text.contains(newText)) {
                        filteredModelList.add(model);
                    }
                }
                json_update(filteredModelList);

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
            case R.id.action_filter:
                show_dialog();
                return true;
            case R.id.action_location:
                new get_current_location(AllPendingBills.this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void json_update(List<m_all_pending_list> posts)
    {

        _txt_total_count.setText(posts.size() + " Orders");
        Exad_pending_all_bill adapter = new Exad_pending_all_bill(posts, this);
        _exp_pending_bill.setAdapter(adapter);
        Log.d("List",posts.toString());
    }

    private void show_dialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_chemist_all_pending_bill_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);

        txt_start_date = (TextView) dialogview.findViewById(R.id.txt_start_date);
        txt_end_date = (TextView) dialogview.findViewById(R.id.txt_end_date);

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
                        AllPendingBills.this,
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
                        AllPendingBills.this,
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

                filter_dialog_conditions(filter_start_date, filter_end_date);
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

    private void filter_dialog_conditions(Date startDate, Date endDate) {

        Date convertedDate = null;

        final List<m_all_pending_list> filteredModelList = new ArrayList<>();
        // int i=0;
        for (m_all_pending_list model : posts) {

            if (startDate != null && endDate != null) {
                for (int i = 0; i < model.getLine_items().size(); i++) {
                    final String date = model.getLine_items().get(i).getInvoicedate();

                    try {
                        convertedDate = dateFormat.parse(date);
                        String c_date = sdf.format(convertedDate);
                        convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                    } catch (Exception e) {
                    }
                }
                try {
                    if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                    {
                        filteredModelList.add(model);
                    }
                } catch (Exception e) {

                }

            }
            // i++;
        }
        json_update(filteredModelList);
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {


    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if (response != null) {
            try {
                Log.d("SHOWPENDINTRESPONCE11",f_name);
                Log.d("SHOWPENDINTRESPONCE12", String.valueOf(response));
                if (f_name.equals(GET_STOCKIST_PENDING_BILLS)) {

                    posts = new ArrayList<m_all_pending_list>();
                    String jsondata = response.toString();
                    //  Log.d("SHOWPENDINTRESPONCE14", String.valueOf(jsondata));

                    if (!jsondata.isEmpty()) {

//                        try {
//                            get_Pending_list_json(jsondata);
//
//                        }catch (Exception exx)
//                        {
//
//                        }
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        Type listType = new TypeToken<List<m_all_pending_list>>() {
                        }.getType();
                        posts = mGson.fromJson(jsondata, listType);
                        json_update(posts);
                    }
                }
                else if(f_name.equals(AppConfig.GET_CHEMIST_PEDNING_BILLS)) {
                    posts = new ArrayList<m_all_pending_list>();
                    String jsondata = response.toString();

                    Log.e("jsondata", jsondata.toString());
                    if (!jsondata.isEmpty()) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        Type listType = new TypeToken<List<m_all_pending_list>>() {
                        }.getType();
                        posts = mGson.fromJson(jsondata, listType);
                        json_update(posts);
                    }
                }

            } catch (Exception e) {
                e.toString();
            }
        }


    }

//    private void get_Pending_list_json(String jsondata) {
//
//        if(!jsondata.isEmpty())
//        {
//            Log.e("jsondata",jsondata.toString());
//
//            JSONArray json= null ;
//            try {
//            json = new JSONArray(jsondata);
//                for (int i=0;i<json.length();i++)
//                {
//                 //   posttt1=new ArrayList<>();
//                    posttt = new ArrayList<>();
//                JSONArray j_arr = json.getJSONObject(i).getJSONArray("line_items");
//
//                    for (int j=0;j<j_arr.length();j++)
//                    {
//                        String  Id=j_arr.getJSONObject(j).getString("Order_ID");
//                        m_all_pending = new m_all_pending_list_line_items();
//                        m_all_pending.setOrderId(Id);
//
//
//                        Log.e("orderidiijj",Id);
//                        Log.e("orderid",m_all_pending.getOrderId());
//
//                    }
//                    posttt.add(m_all_pending);
//                   // posttt1.add(posttt);
//                }
////                Exad_pending_all_bill adapter = new Exad_pending_all_bill(posttt, this);
////                _exp_pending_bill.setAdapter(adapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        }
//    }
}


























