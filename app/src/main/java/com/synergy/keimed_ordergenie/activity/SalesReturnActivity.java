package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.OnClick;
import com.synergy.keimed_ordergenie.BR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.ad_customerlist;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_salesreturn;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.get_current_location;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by prakash on 08/07/16.
 */
public class SalesReturnActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    @BindView(R.id.rv_datalist)
    RecyclerView rvCustomerlist;
    String login_type;
    SharedPreferences pref;
    @BindView(R.id.fab_options)
    FloatingActionMenu fab_options;

    @BindView(R.id.empty_view)
    TextView empty_view;


/*
    @BindView(R.id.fab)
	FloatingActionButton fab;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return);


        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
      /*  getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);*/
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        ButterKnife.bind(this);

        if (login_type.equals(CHEMIST)) {
            fab_options.setVisibility(View.VISIBLE);


            try {
                JSONObject j_obj = new JSONObject();
                j_obj.put("ClientID", pref.getString(ConstData.user_info.CLIENT_ID, ""));
               // j_obj.put("ClientID", "1");
                j_obj.put("ClientType", "c");
                JSONArray j_arr = new JSONArray();
                j_arr.put(j_obj);

                MakeWebRequest.MakeWebRequest("out_array", AppConfig.GET_SALES_RETURNS, AppConfig.GET_SALES_RETURNS,
                        null, this, true, j_obj.toString());


            } catch (Exception e) {

            }

        } else {
            fab_options.setVisibility(View.GONE);


            try {

                JSONObject j_obj = new JSONObject();
                j_obj.put("ClientID", pref.getString(ConstData.user_info.CLIENT_ID, ""));
                j_obj.put("ClientType", "s");
                JSONArray j_arr = new JSONArray();
                j_arr.put(j_obj);

                MakeWebRequest.MakeWebRequest("out_array", AppConfig.GET_SALES_RETURNS, AppConfig.GET_SALES_RETURNS,
                        null, this, true, j_obj.toString());

            } catch (Exception e) {

            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_location:

                new get_current_location(SalesReturnActivity.this);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @OnClick(R.id.fab_stockist_list)
    void show_Stockist() {
        Intent intent = new Intent(SalesReturnActivity.this, StockistList.class);
        startActivity(intent);
    }


    @OnClick(R.id.fab_bills)
    void show_pendingbills() {

        if (login_type.equals(CHEMIST)) {

            Intent intent = new Intent(SalesReturnActivity.this, AllPendingBills.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(SalesReturnActivity.this, IndividualPendingBillsActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fab_order)
    void show_order() {
        if (login_type.equals(CHEMIST)) {

            Intent intent = new Intent(SalesReturnActivity.this, Order_list.class);
            startActivity(intent);
        } else {


            Intent intent = new Intent(SalesReturnActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void json_update(List<?> posts_s) {

        if(posts_s.size()==0)
        {
            empty_view.setVisibility(View.VISIBLE);
        }else
        {
            empty_view.setVisibility(View.GONE);
        }


        final int SALESRETURN = 3;
        ad_customerlist adapter = new ad_customerlist(this, posts_s, SALESRETURN,
                R.layout.fragement_salesreturn_items, BR.v_salesreturn);


        rvCustomerlist.setLayoutManager(new LinearLayoutManager(this));
        rvCustomerlist.setAdapter(adapter);
    }


    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {


        if (response != null) {
            if (f_name.equals(AppConfig.GET_SALES_RETURNS)) {

                GsonBuilder builder = new GsonBuilder();
                ad_customerlist adapter;
                Gson mGson = builder.create();
                final int SALESRETURN = 3;

                List<?> posts = new ArrayList<m_salesreturn>();
                posts = Arrays.asList(mGson.fromJson(response.toString(), m_salesreturn[].class));

                json_update(posts);
            }
        }

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        if (response != null) {

        }
    }

}
