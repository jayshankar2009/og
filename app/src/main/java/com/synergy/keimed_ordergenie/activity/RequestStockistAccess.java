package com.synergy.keimed_ordergenie.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class RequestStockistAccess extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {


    @BindView(R.id.txt_user_selection)
    TextView _txt_user_selection;
    @BindView(R.id.txt_distrubutor_code)
    TextView txt_distrubutor_code;
    @BindView(R.id.edt_message)
    EditText edt_message;


    @OnClick(R.id.btn_cancel)
    public void btncancel() {
        finish();
    }

    SharedPreferences pref;
    private String stockist_id, stockist_name;
    static final String CHEMIST_STOCKIST_ID = "chemist_stockist_id";
    public static final String CHEMIST_STOCKIST_NAME = "chemist_stockist_name";
    private String accessKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_stockist_access);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        accessKey = pref.getString("key", "");



        ButterKnife.bind(this);
        get_intent();


    }


    @OnClick(R.id.save_btn)
    public void send_request() {


        // THIS METHOD COMMENTED 16 AUGUST 2018

        //   post_access_request(edt_message.getText().toString());

        get_access_request(edt_message.getText().toString());

        finish();
    }


    void get_intent() {
        stockist_id = getIntent().getStringExtra(CHEMIST_STOCKIST_ID);
        stockist_name = getIntent().getStringExtra(CHEMIST_STOCKIST_NAME);

        txt_distrubutor_code.setText("Distributor Code : " + stockist_id);
        _txt_user_selection.setText(stockist_name);
    }

    private void get_access_request(String s) {

        String client_id = pref.getString(CLIENT_ID, "");
        String accessrequest = "accessrequest";

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(stockist_id);
        jsonArray.put(client_id);
        jsonArray.put(accessrequest);
        jsonArray.put(s);
        jsonArray.put("true");
        String url = AppConfig.GET_CHEMIST_TO_STOCKIST_INVENTORY_ACESS + jsonArray;

        Log.d("send_Request11",url);
        Log.d("jsonArray",jsonArray.toString());

        //  http://192.168.31.109:9000/api/notifications/StocistSendRequest/[42104,4875,"accessrequest","Prasad","true"]


        // String url = AppConfig.GET_CHEMIST_TO_STOCKIST_INVENTORY_ACESS + "[" + stockist_id + "," + pref.getString(CLIENT_ID, "") + "accessrequest" + s + "true" + "]";

       // Log.d("Click_RequestAccess22", url);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

             //  Log.d("send_Request11",response.toString());

                if (response != null) {

                //    Log.d("send_Request12",response.toString());

                    try {

                        JSONObject jsonObject = response.getJSONObject(0);
                        String show_requestMsg = jsonObject.getString("flag");
                        String show_Msg = jsonObject.getString("msg");
                      //  Log.d("send_Request13",show_requestMsg);

                      //  Log.d("send_Request14",show_Msg);

                        if (show_requestMsg.equals("0")) {

                            Toast.makeText(RequestStockistAccess.this, show_Msg, Toast.LENGTH_LONG).show();

                        }else if(show_requestMsg.equals("1")){

                            Toast.makeText(RequestStockistAccess.this, show_Msg, Toast.LENGTH_LONG).show();

                        } /*else {

                            Toast.makeText(RequestStockistAccess.this, "Your request not sent", Toast.LENGTH_LONG).show();

                        }*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*try {

                        JSONObject jsonObject = response.getJSONObject(0);
                        String show_requestMsg = jsonObject.getString("ok");

                        if (show_requestMsg.equals("ok")) {

                            Toast.makeText(RequestStockistAccess.this, "Your request successfully sent", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(RequestStockistAccess.this, "Your request not sent", Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.d("Resp", error.getMessage());
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
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }


    void post_access_request(String messsage) {
        String client_id = pref.getString(CLIENT_ID, "");

        JSONObject jsonParams = new JSONObject();

        // JSONArray jsonParams = new JSONArray();
        try {
            jsonParams.put("StockistID", stockist_id);
            jsonParams.put("ClientID", pref.getString(CLIENT_ID, ""));
            jsonParams.put("Type", "accessrequest");
            jsonParams.put("Info", messsage);
            jsonParams.put("active", "true");

        //    Log.d("Click_RequestAccess11", jsonParams.toString());
            // Log.d("Click_RequestAccess12",AppConfig.POST_CHEMIST_TO_STOCKIST_INVENTORY_ACESS);

            // MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_CHEMIST_TO_STOCKIST_INVENTORY_ACESS, AppConfig.POST_CHEMIST_TO_STOCKIST_INVENTORY_ACESS, jsonParams, this, true);
        } catch (Exception e) {

        }
    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        if (response != null) {


          /*  if(f_name.equals(AppConfig.POST_CHEMIST_TO_STOCKIST_INVENTORY_ACESS))
            {
                OGtoast.OGtoast("Request sent succesfully",RequestStockistAccess.this);
                finish();
            }*/
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {


    }
}
