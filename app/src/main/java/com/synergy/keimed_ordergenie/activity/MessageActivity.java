package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.BR;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_message_list;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.get_current_location;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class MessageActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    @BindView(R.id.rv_message)
    RecyclerView rv_message;

    @BindView(R.id.empty_view)
    TextView empty_view;
    SharedPreferences pref;
    List<m_message_list> posts;
    private String Chemist_Id;
    String login_type;

    @BindView(R.id.fab_options)
    FloatingActionMenu fab_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        ButterKnife.bind(this);
        Chemist_Id = pref.getString(CLIENT_ID, "0");

        if (login_type.equals(CHEMIST)) {
            fab_options.setVisibility(View.VISIBLE);
        } else {
            fab_options.setVisibility(View.GONE);
        }


        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_USER_NOTIFICATIONS,
                AppConfig.GET_USER_NOTIFICATIONS + "[" + Chemist_Id + ",1,\"" + pref.getString(CLIENT_ROLE, "") + "\"]", this, true);


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

                new get_current_location(MessageActivity.this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab_stockist_list)
    void show_Stockist() {
        Intent intent = new Intent(MessageActivity.this, StockistList.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab_bills)
    void show_pendingbills() {

        if (login_type.equals(CHEMIST)) {

            Intent intent = new Intent(MessageActivity.this, AllPendingBills.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(MessageActivity.this, IndividualPendingBillsActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fab_order)
    void show_order() {
        if (login_type.equals(CHEMIST)) {

            Intent intent = new Intent(MessageActivity.this, Order_list.class);
            startActivity(intent);
        } else {


            Intent intent = new Intent(MessageActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fab_sales)
    void show_sales() {
        Intent intent = new Intent(MessageActivity.this, SalesReturnActivity.class);
        startActivity(intent);
    }

    private void get_message_list_json(String jsondata) {


        // LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Message.json", MessageActivity.this);
        // String jsondata = _LoadJsonFromAssets.getJson();

        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            posts = new ArrayList<m_message_list>();

            posts = Arrays.asList(mGson.fromJson(jsondata, m_message_list[].class));


            fill_message_list(posts);

        }
    }

    private void fill_message_list(final List<m_message_list> posts_s) {

        // _total_count.setText(posts_s.size()+" Orders.");

        if (posts_s.isEmpty()) {
            empty_view.setVisibility(View.VISIBLE);
            rv_message.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            rv_message.setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {

            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(MessageActivity.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.adapter_message, parent, false);


                return new BindingViewHolder(binding.getRoot());
            }


            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                m_message_list order_list = posts_s.get(position);
                holder.getBinding().setVariable(BR.v_message_list, order_list);
                holder.getBinding().executePendingBindings();
                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });


            }


            @Override
            public int getItemCount() {
                return posts_s.size();
            }
        };

        rv_message.setLayoutManager(new LinearLayoutManager(this));
        rv_message.setAdapter(adapter);
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            if (f_name.equals(AppConfig.GET_USER_NOTIFICATIONS)) {
                get_message_list_json(response.toString());
            }
        }
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        if (response != null) {

        }
    }


}
