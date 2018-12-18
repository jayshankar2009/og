package com.synergy.keimed_ordergenie.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.SalesDataCustomAdapter;
import com.synergy.keimed_ordergenie.adapter.Summary_Adapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.Sales;
import com.synergy.keimed_ordergenie.model.m_product_list_chemist;
import com.synergy.keimed_ordergenie.model.summary_order;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_FULL_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_LOGIN_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.STOCKIST_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class SalesSummaryReportActivity extends AppCompatActivity implements
        View.OnClickListener, MakeWebRequest.OnResponseSuccess{

    Toolbar mToolbar;
    private static RecyclerView.Adapter adapter_sales_details;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recycler_view_all_sales_data;
    private static ArrayList<Sales> allSalesData = new ArrayList<Sales>();

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView from_date_View,to_date_View,sales_summary_salesman_name,sales_summary_total_target,sales_summary_grand_total;
    private int mYear, mMonth, mDay;
    LinearLayout from_layout, to_layout;
    String date = null;
    String login_type;
    String strSnalesmanId,strStokistId;
    SharedPreferences pref;
    //int grand_total=0;
    String cFullName;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_summary_layout);

        //Date Picker function
        from_layout = (LinearLayout) findViewById(R.id.from_layout);
        from_layout.setOnClickListener(this);
        to_layout = (LinearLayout) findViewById(R.id.to_layout);
        to_layout.setOnClickListener(this);

        from_date_View = (TextView) findViewById(R.id.text_from_date);
        to_date_View = (TextView) findViewById(R.id.text_to_date);

        sales_summary_salesman_name = (TextView) findViewById(R.id.sales_summary_salesman_name);

        sales_summary_total_target = (TextView) findViewById(R.id.sales_summary_total_target);
        sales_summary_grand_total = (TextView) findViewById(R.id.sales_summary_grand_total);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(mYear, mMonth+1, mDay);

        // sales report list
        recycler_view_all_sales_data = (RecyclerView) findViewById(R.id.recycler_view_all_sales_data);
        recycler_view_all_sales_data.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_view_all_sales_data.setLayoutManager(layoutManager);
        recycler_view_all_sales_data.setItemAnimator(new DefaultItemAnimator());

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        strSnalesmanId=pref.getString(USER_ID,"");
        strStokistId=pref.getString(CLIENT_ID,"");
        cFullName = pref.getString(CLIENT_FULL_NAME, "0");
        sales_summary_salesman_name.setText(cFullName);
        ButterKnife.bind(this);

        fetchDataFromServer();

    }

    private void fetchDataFromServer() {
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(strSnalesmanId); //stockist
            jsonArray.put(from_date_View.getText());
            jsonArray.put(to_date_View.getText()); //search key

            jsonArray.put(strStokistId);
            jsonArray.put("0");
            jsonArray.put("0");
            jsonArray.put("0");

            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_SALES_SUMMARY_LIST,
                    AppConfig.GET_SALES_SUMMARY_LIST + jsonArray, this, true);



        } catch (Exception e) {

        }
    }

    private void showDate(int year, int month, int day) {
        from_date_View.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
        to_date_View.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.from_layout:
                date = "from";
                // grand_total=0;
                pickUpDate();
                break;
            case R.id.to_layout:
                date = "to";
                //    grand_total=0;
                pickUpDate();
                break;
        }
    }

    private void pickUpDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if(date.equalsIgnoreCase("from")) {
                            from_date_View.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            fetchDataFromServer();

                        }else if(date.equalsIgnoreCase("to")) {
                            to_date_View.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            fetchDataFromServer();

                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        Log.d("Report", String.valueOf(response));
        if (response!=null) {
            //   Log.i("Json Response13",""+response.toString());
            String strResponse = response.toString();
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            allSalesData = new ArrayList<>(Arrays.asList(mGson.fromJson(strResponse, Sales[].class)));
            adapter_sales_details = new SalesDataCustomAdapter(allSalesData);
            recycler_view_all_sales_data.setAdapter(adapter_sales_details);
            if (allSalesData.size()!=0) {
                sales_summary_total_target.setText("Target : Rs." + String.valueOf(allSalesData.get(0).getS_targaet()));
                int grand_total=0;
                for (int i = 0; i < allSalesData.size(); i++) {
                    grand_total = grand_total + Integer.parseInt(allSalesData.get(i).getT_amt());
                }

                if (grand_total != 0) {
                    sales_summary_grand_total.setText("Rs. " + String.valueOf(grand_total));
                } else {
                    sales_summary_grand_total.setText("Rs. 0");
                }
         /* }catch (Exception e) {
              Log.i("Catch",""+e);
          }*/
            }
        }

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        Log.d("objectReport", String.valueOf(response));
    }
}
