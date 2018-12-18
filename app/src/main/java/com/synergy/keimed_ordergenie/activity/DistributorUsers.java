package com.synergy.keimed_ordergenie.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.DistributorUsersAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class DistributorUsers extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    RecyclerView recycler_view;
    DistributorUsersAdapter distributorUsersAdapter;
    List<DistributorUsersModal> distributorUsersList;
    SharedPreferences pref;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_users);

        setTitle("Users");

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_view.getContext(),
                DividerItemDecoration.VERTICAL);
        recycler_view.addItemDecoration(dividerItemDecoration);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        get_userData();
    }

    private void get_userData() {

        String Stockist_id = pref.getString(CLIENT_ID, "0");
        Log.d("Stockist_id",Stockist_id);
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_DISTRIBUTOR_USERS_LIST,
                AppConfig.GET_DISTRIBUTOR_USERS_LIST + Stockist_id, this, true);

        //GET_DISTRIBUTOR_USERS_LIST
        //http://www.ordergenie.co.in/api/AppApis/userlistNcallplan/1240
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {


        if(response!=null)
        {
            get_DistributorUsersList(response.toString());
        }
    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        // Log.d("printUsers12",response.toString());

    }

    private void get_DistributorUsersList(String jsondata) {

        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            distributorUsersList = new ArrayList<DistributorUsersModal>();
            //  pendinglist_old=new ArrayList<PendingListModal>();
            // pendinglist = new LinkedList<PendingListModal>(Arrays.asList(mGson.fromJson(jsondata, PendingListModal[].class)));
            distributorUsersList = Arrays.asList(mGson.fromJson(jsondata, DistributorUsersModal[].class));
        
            fill_usersListData(distributorUsersList);
        }
    }

    private void fill_usersListData(List<DistributorUsersModal> distributorUsersList) {
        distributorUsersAdapter = new DistributorUsersAdapter(distributorUsersList,DistributorUsers.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(distributorUsersAdapter);
    }
}