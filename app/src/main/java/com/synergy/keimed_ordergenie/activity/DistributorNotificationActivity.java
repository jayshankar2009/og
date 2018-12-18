package com.synergy.keimed_ordergenie.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.Distrinotificationadapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.Distributornotificationmodal;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.get_current_location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by admin on 12/4/2017.
 */

public class DistributorNotificationActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {


    Distrinotificationadapter distrinotificationadapter;
    List<Distributornotificationmodal> notificationList = new ArrayList<Distributornotificationmodal>();
    @BindView(R.id.rv_message)
    RecyclerView rv_message;

    @BindView(R.id.empty_view)
    TextView empty_view;
    SharedPreferences pref;
    //List<m_message_list> posts;
    private String Chemist_Id;
    String login_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_notification);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        ButterKnife.bind(this);
        Chemist_Id = pref.getString(CLIENT_ID, "0");


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_message.getContext(),
                DividerItemDecoration.VERTICAL);
        rv_message.addItemDecoration(dividerItemDecoration);

        distrinotificationadapter = new Distrinotificationadapter(notificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_message.setLayoutManager(mLayoutManager);
        rv_message.setItemAnimator(new DefaultItemAnimator());
        rv_message.setAdapter(distrinotificationadapter);

        get_notificationMsg();



        notificationListing();

    }

    private void get_notificationMsg() {

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_DISTRIBUTOR_NOTIFICATIONS,
                AppConfig.GET_DISTRIBUTOR_NOTIFICATIONS + Chemist_Id, this, true);

    }

    private void notificationListing() {



        Distributornotificationmodal notificationlistmodal = new Distributornotificationmodal("PramodChemist","09:30 PM","Rs. 250");
        notificationList.add(notificationlistmodal);

        notificationlistmodal = new Distributornotificationmodal("PramodChemist001","10:30 PM","Rs. 350");
        notificationList.add(notificationlistmodal);

        distrinotificationadapter.notifyDataSetChanged();


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

                new get_current_location(DistributorNotificationActivity.this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void get_message_list_json(String jsondata) {



        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            //posts = new ArrayList<m_message_list>();

            //posts = Arrays.asList(mGson.fromJson(jsondata, m_message_list[].class));


          //  fill_message_list(posts);

        }
    }


    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {


        if (response != null) {

            Log.d("notificationResp",response.toString());
            if (f_name.equals(AppConfig.GET_DISTRIBUTOR_NOTIFICATIONS)) {

                //GET_USER_NOTIFICATIONS
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
