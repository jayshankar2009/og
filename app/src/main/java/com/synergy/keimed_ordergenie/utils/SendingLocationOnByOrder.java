package com.synergy.keimed_ordergenie.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendingLocationOnByOrder extends IntentService {
    SQLiteHandler sqLiteHandler;
    AppController globalVariable;
    JSONObject jsonParams=null;
    String Doc_id="";
    RequestQueue queue;
    public SendingLocationOnByOrder() {

        super(SendingLocationOnByOrder.class.getName());
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        globalVariable = (AppController) getApplicationContext();
        jsonParams = new JSONObject();
        Doc_id = intent.getStringExtra("doc_id");
        Log.e("SendingLocationOrder","started");
        if (ConnectionDetector.isConnectingToInternet(this)) {
            if (Doc_id!=null){
                try{
                    jsonParams =new JSONObject(Doc_id);
                }catch (Exception e){

                }

            }

          //  Log.e("SendingLocation",""+jsonParams);
            if(jsonParams!=null) {
                try {

                    if(queue==null){
                        queue= Volley.newRequestQueue(this);
                    }

                    VolleyLog.DEBUG = true;
                    JsonObjectRequest jsObjRequest_post = new JsonObjectRequest(
                            //Method.POST,
                            AppConfig.POST_UPDATE_CURRENT_LOCATION,
                            jsonParams,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("SendingLocatiResponse", response.toString());
                                    //hidePDialog();
                                    try{
                                        sqLiteHandler.deleteLocationDatabyCustomerId(jsonParams.getString("DOC_NO"));
                                    }catch (Exception e){

                                    }



                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.d("SendingLocationerror", "error:f_name " + error.toString());
                                    if (error instanceof TimeoutError) {

                                    } else if (error instanceof ServerError) {

                                    }
                                    // hidePDialog();

                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            if (globalVariable.getToken() != null) {
                                headers.put("Authorization", "Bearer " + globalVariable.getToken());
                            }

                            return headers;
                        }

                    };


                    jsObjRequest_post.setRetryPolicy(new DefaultRetryPolicy(
                            15000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(jsObjRequest_post);


                } catch (Exception e) {
                   // Log.e("SendData", e.toString());
                }
            }

        }


    }


}
