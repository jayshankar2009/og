package com.synergy.keimed_ordergenie.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.synergy.keimed_ordergenie.app.AppController;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by 1132 on 05-11-2016.
 */

public class MakeWebRequest {

    static Context cntx;
    static ProgressDialog pDialog;
    static JSONObject O_response_data;
    static JSONArray A_response_data;

    static OnResponseSuccess mListener;
    static AppController globalVariable;
    static SharedPreferences pref;


    public static void MakeWebRequest(String Method_type, String f_name, String Url, Context ctx, Boolean isProgreedialog) {
        try {


            cntx = ctx;
            globalVariable = (AppController) ctx.getApplicationContext();
            pref = ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            mListener = (OnResponseSuccess) cntx;
            if (pDialog != null) {
                pDialog.dismiss();
            }
            pDialog = new ProgressDialog(cntx);
            pDialog.setCancelable(false);
            //Log.d("aldjaldfjUrl", ""+Url);
            Log.i("URL CHECk",""+Url);
            get_method(Url, f_name, isProgreedialog, mListener);
        }catch (Exception e)
        {
            if (pDialog != null) {
                pDialog.dismiss();
            }
            Log.d("MakeWebRequest",e.getMessage());
        }
    }

    public static void MakeJsonObjectRequest(String Method_type, String f_name, String Url, Context ctx, Boolean isProgreedialog) {
        cntx = ctx;
        globalVariable = (AppController) ctx.getApplicationContext();
        pref = ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        mListener = (OnResponseSuccess) cntx;
        if (pDialog != null) {
            pDialog.dismiss();
        }
        pDialog = new ProgressDialog(cntx);
        pDialog.setCancelable(false);
        get_method_json_object(Url, f_name, isProgreedialog);

    }


    public static void MakeWebRequest(String Method_type, String f_name, String Url, JSONObject jsonParams, Context ctx, Boolean isProgreedialog) {
        cntx = ctx;
        globalVariable = (AppController) ctx.getApplicationContext();
        pref = ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        mListener = (OnResponseSuccess) cntx;
        if (pDialog != null) {
            pDialog.dismiss();
        }
        pDialog = new ProgressDialog(cntx);
        pDialog.setCancelable(false);
       // Log.i("Location---"+f_name,"URL---"+jsonParams);
        if (Method_type.equals("Post")) {
            post_method(jsonParams, Url, f_name, isProgreedialog, mListener);
        } else {
            post_method_jsonobject(jsonParams, Url, f_name, isProgreedialog, mListener);
        }


    }


    public static void MakeWebRequest(String Method_type, String f_name, String Url, JSONArray jarray, Context ctx, Boolean isProgreedialog, String array_string) {
        cntx = ctx;
        globalVariable = (AppController) ctx.getApplicationContext();
        mListener = (OnResponseSuccess) ctx;
        if (pDialog != null) {
            pDialog.dismiss();
        }
        pDialog = new ProgressDialog(ctx);
        pDialog.setCancelable(false);
        /*Log.d("hitUrl", Url);
        Log.d("hitParams", jarray.toString());*/
        if (Method_type.equals("out_array")) {
            post_method_json_array_out_jsonarray(jarray, Url, f_name, isProgreedialog, array_string);
            //Log.d("findUrl", Url);
        } else {
            post_method_json_array(jarray, Url, f_name, isProgreedialog);
            //Log.d("elseReq", "else");
        }
    }


    static void post_method(JSONObject jsonParams, String Url, final String f_name, Boolean isProgreedialog, final OnResponseSuccess o_mListener) {

       // Log.e("Url88888888888888888888", Url);
        try {

            if (isProgreedialog) {
                showPdialog("Loading....");
            }
            RequestQueue queue = Volley.newRequestQueue(cntx);
            VolleyLog.DEBUG = true;
            JsonObjectRequest jsObjRequest_post = new JsonObjectRequest(
                    //Method.POST,
                    Url,
                    jsonParams,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            //hidePDialog();
                            if (o_mListener != null) {
                                o_mListener.onSuccess_json_object(f_name, response);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {

                         //   Log.d("Volley","error:"+error.getMessage());
                           // hidePDialog();
                            if (o_mListener != null) {
                                o_mListener.onSuccess_json_object(f_name, O_response_data);
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    if (globalVariable.getToken() != null) {
                        headers.put("Authorization", "Bearer " + globalVariable.getToken());
                    }else{

                        String key = pref.getString("key", "");
                        headers.put("Authorization", "Bearer " + key);
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
            e.toString();
        }
        finally {
            hidePDialog();
        }
    }

    static void post_method_jsonobject(JSONObject jsonParams, String Url, final String f_name, Boolean isProgreedialog, final OnResponseSuccess o_mListener) {

      //  Log.e("Url99999999999999999", Url);
        try {

            if (isProgreedialog) {
                showPdialog("Loading....");
            }
            RequestQueue queue = Volley.newRequestQueue(cntx);

            JsonArrayRequest jsObjRequest_post = new JsonArrayRequest(
                    //Method.POST,
                    Url,
                    jsonParams,

                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            hidePDialog();
                            if (o_mListener != null) {
                                o_mListener.onSuccess_json_array(f_name, response);
                            }
                            ;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidePDialog();
                            if (o_mListener != null) {
                                o_mListener.onSuccess_json_object(f_name, O_response_data);
                            }
                            ;

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // headers.put("Content-Type", "application/json");
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
            e.toString();
        }
    }


    static void post_method_json_array(JSONArray jsonParams, String Url, final String f_name, Boolean isProgreedialog) {
        try {

            if (isProgreedialog) {
                showPdialog("Loading. . .");
            }
            // showPdialog("Loading....");
            RequestQueue queue = Volley.newRequestQueue(cntx);

         /* Map<String, String> j_obj = new HashMap<String, String>();
          //  JSONObject j_obj = new JSONObject();
            j_obj.put("ClientID", "608");
            j_obj.put("ClientType", "c");
            JSONArray j_arr = new JSONArray();
            j_arr.put(j_obj);
*/

            //  String s= "{\"ClientID\":\"608\",\"ClientType\":\"c\"}";

            JsonObjectRequest jsObjRequest_post = new JsonObjectRequest(
                    1,
                    Url,
                    jsonParams.toString(),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hidePDialog();
                          //  Log.d("checkResp", response.toString());
                            if (mListener != null) {
                                mListener.onSuccess_json_object(f_name, response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidePDialog();
                            if (mListener != null) {
                                mListener.onSuccess_json_object(f_name, O_response_data);
                            }
                        }
                    })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // headers.put("Content-Type", "application/json");
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
        }
    }


    static void post_method_json_array_out_jsonarray(JSONArray jsonParams, String Url, final String f_name, Boolean isProgreedialog, String array_string) {
        String req_params = "";
        //Log.d("Params1", jsonParams.toString());
        //Log.d("Params2", array_string);
        try {
            if (array_string.equals("")) {
                req_params = jsonParams.toString();
            } else {
                req_params = array_string;
            }
            //Log.d("urlWithParams", ""+Url+ "" + req_params);

            if (isProgreedialog) {
                showPdialog("Loading. . .");
            }
            // showPdialog("Loading....");
            RequestQueue queue = Volley.newRequestQueue(cntx);

            JsonArrayRequest jsObjRequest_post = new JsonArrayRequest(1, Url, req_params,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            hidePDialog();
                        //    Log.d("checkResp", response.toString());
                            if (mListener != null) {
                                mListener.onSuccess_json_array(f_name, response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidePDialog();
                            if (mListener != null) {
                                mListener.onSuccess_json_object(f_name, O_response_data);
                            }
                        }
                    })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // headers.put("Content-Type", "application/json");
                    if (globalVariable.getToken() != null) {
                        headers.put("Authorization", "Bearer " + globalVariable.getToken());
                    }

                    return headers;
                }

            };

            jsObjRequest_post.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest_post);

        } catch (Exception e) {
        }
    }


    static void get_method(String url, final String f_name, Boolean isProgreedialog,
                           final OnResponseSuccess o_mListener) {

        if (isProgreedialog) {
            showPdialog("Loading. . .");
        }

        RequestQueue queue = Volley.newRequestQueue(cntx);

        JsonArrayRequest jsonObjReq_state = new JsonArrayRequest(Request.Method.GET,
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hidePDialog();
                if (response!=null) {
                  //  Log.d("reponseCheck", response.toString());
                    //Log.d("RESPONS",response.toString());
                }
                if (o_mListener != null) {
                    o_mListener.onSuccess_json_array(f_name, response);
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Log.d("error_volley",error.toString());
                        hidePDialog();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//This indicates that the reuest has either time out or there is no connection
                     //      Log.d("Error","TimeoutError");
                        } else if (error instanceof AuthFailureError) {
// Error indicating that there was an Authentication Failure while performing the request
                     //       Log.d("Error","Authentication Failure");
                        } else if (error instanceof ServerError) {
//Indicates that the server responded with a error response
                     //       Log.d("Error","ServerError");

                        } else if (error instanceof NetworkError) {
//Indicates that there was network error while performing the request
                      //      Log.d("Error","NetworkError");

                        } else if (error instanceof ParseError) {
// Indicates that the server response could not be parsed
                        //    Log.d("Error","ParseError");
                        }
                        if (o_mListener != null) {
                            o_mListener.onSuccess_json_array(f_name, A_response_data);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (globalVariable.getToken() != null) {
                    headers.put("Authorization", "Bearer " + globalVariable.getToken());
//                    String xyz="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOjksInJvbGUiOiJzdG9ja2lzdCIsImlhdCI6MTUyODg4Mjg5NSwiZXhwIjoxNTI4OTE4ODk1fQ.n0txaN-hyuBe68feazKWDe182BBQWTRsZ3Ob5N0EqPY";
//                    headers.put("Authorization", "Bearer " + xyz);
                }
                return headers;
            }
        };
        // jsonObjReq_state.setShouldCache(false);
        jsonObjReq_state.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq_state);

    }

    static void get_method_json_object(String url, final String f_name, Boolean isProgreedialog) {
        if (isProgreedialog) {
            showPdialog("Loading. . .");
        }

        RequestQueue queue = Volley.newRequestQueue(cntx);

        JsonObjectRequest jsonObjReq_state = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();
              //  Log.d("checkResp", response.toString());
                if (mListener != null) {
                    mListener.onSuccess_json_object(f_name, response);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        if (mListener != null) {
                            mListener.onSuccess_json_array(f_name, A_response_data);
                        }
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

        queue.add(jsonObjReq_state);

    }


    private static void hidePDialog() {
        if (pDialog != null & pDialog.isShowing()) {
            pDialog.dismiss();
            //pDialog = null;
        }
    }

    private static void showPdialog(String sMessage) {
        if (pDialog != null) {
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }


    /* Interface */
    public interface OnResponseSuccess {
        public void onSuccess_json_array(String f_name, JSONArray response);

        public void onSuccess_json_object(String f_name, JSONObject response);
    }


    public static void put_request(Activity ctx, final String url, final Map<String, String> params) {
     //   if (pDialog == null) {
         if (pDialog != null) {
            pDialog.dismiss();
        }
        mListener = (OnResponseSuccess) ctx;
        pDialog = new ProgressDialog(ctx);
        showPdialog("Loading. . .");
        final SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(ctx);


        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                   Log.d("checkResp", response.toString());
                        Log.d("URL", "---"+url.toString());
                        try {


                            if (mListener != null) {
                                mListener.onSuccess_json_object("profile", new JSONObject(response));
                            }

                        } catch (Exception e) {

                        }
                        // response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        if (mListener != null) {
                            mListener.onSuccess_json_array("profile", A_response_data);
                        }
                        ;

                    }
                }
        )
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (globalVariable.getToken() != null) {
                    headers.put("Authorization", "Bearer " + globalVariable.getToken());
                }

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {


                return params;
            }

        };
        queue.add(putRequest);
    }

    public static void put_request1(Activity ctx, final String url, JSONObject jsonObject) {
        if (pDialog != null) {
            pDialog.dismiss();
        }
        mListener = (OnResponseSuccess) ctx;
        pDialog = new ProgressDialog(ctx);

        showPdialog("Loading. . .");
        final SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(ctx);


        JsonObjectRequest jsObjRequest_post = new JsonObjectRequest(
                //Method.POST,
                Request.Method.PUT,
                url,
                jsonObject,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();
                        if (mListener != null) {
                            mListener.onSuccess_json_object(url, response);
                        }
                        ;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        if (mListener != null) {
                            mListener.onSuccess_json_object(url, O_response_data);
                        }
                        ;

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // headers.put("Content-Type", "application/json");
                if (globalVariable.getToken() != null) {
                    headers.put("Authorization", "Bearer " + globalVariable.getToken());
                }

                return headers;
            }

        };

        queue.add(jsObjRequest_post);
    }

    public static void put_request2( Activity context, String url, JSONObject jsonObject) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,url,jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  hidePDialog();


                    }
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String> ();
               // params.put("name", "Alif");
               // params.put("domain", "http://itsalif.info");

                if (globalVariable.getToken() != null) {
//                    params.put("Authorization", "Bearer " + globalVariable.getToken());
                }

                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);

    }



}
