package com.synergy.keimed_ordergenie.utils;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.database.MasterPlacedOrder;
import com.synergy.keimed_ordergenie.database.MasterPlacedOrderDao;

import java.util.List;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_LAST_DATA_SYNC;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_ORDERLIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_PENDING_BILLS;
import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_STOCKISTLIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.decrypt;
import static com.synergy.keimed_ordergenie.utils.ConstData.encrypt;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_CITY_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_PASSWORD;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_USERNAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.STOCKIST_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by 1132 on 14-11-2016.
 */

public class RefreshData extends IntentService implements MakeWebRequest.OnResponseSuccess {
    private SQLiteHandler sqLiteHandler;
    SharedPreferences pref;
    String currentsyncdate;
    private SQLiteHandler db;
    private String login_type;
    AppController globalVariable;
    public static final String Key_USERID = "user_Id";
    public static final String Key_CUSTID = "call_plan_customer_id";
    public static final String Key_TASK = "task";
    public static final String Key_Address = "address";
    public static final String F_KEY_CC_DOC_NO = "DOC_NO";
    public static final String Key_TRANSACTION_ID = "Tran_No";
    public static final String Key_LATITUDE = "Latitude";
    public static final String Key_LONGITUDE = "Longitude";
    public static final String Key_UNIQ_ID = "unqid";
    String camefrom="";
    ConnectionDetector connectiondetector;
    public static final String ACTION_CONFIRM_ORDER = "android.ordergenie.intent.action.CONFIRM_ORDER";
    public static final String ACTION_CONFIRM_ORDER_CHEMIST = "android.ordergenie.intent.action.CONFIRM_ORDER_CHEMIST";
   // public static final String ACTION_CONFIRM_ORDER_SALESMAN = "android.ordergenie.intent.action.CONFIRM_ORDER_SALESMAN";

    public RefreshData() {
        super(RefreshData.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       // camefrom=intent.getStringExtra("camefrom");
        if (intent.getAction().equals(ACTION_CONFIRM_ORDER)) {
            if (ConnectionDetector.isConnectingToInternet(this)) {
                sqLiteHandler = new SQLiteHandler(this);
                confirm_order();
            }
        }  else if (intent.getAction().equals(ACTION_CONFIRM_ORDER_CHEMIST)) {
            if (ConnectionDetector.isConnectingToInternet(this)) {
               //**jyott 3/8
                sqLiteHandler = new SQLiteHandler(this);
                confirm_order_chemist();
            }
        }
        else {
            pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            // connectiondetector = new ConnectionDetector(this);
            globalVariable = (AppController) getApplicationContext();
            db = new SQLiteHandler(this);

            if (ConnectionDetector.isConnectingToInternet(this)) {
                get_user_credentials();
            } else {
                // OGtoast.OGtoast( "Network not available",this);
            }

        }
    }

    void get_refreshed_data(String client_id, String city_id, String Stockist_id, String user_id) {
        String syncdate  = pref.getString(CHEMIST_LAST_DATA_SYNC, "");
        if(syncdate==null||syncdate==""||syncdate.isEmpty()){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


            currentsyncdate = String.valueOf(simpleDateFormat.format(calendar.getTime()));

        }
        else{
            currentsyncdate = pref.getString(CHEMIST_LAST_DATA_SYNC, "");
        }


        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_STOCKIST,
                AppConfig.GET_CHEMIST_STOCKIST + "[" + client_id + "," + city_id + "]", this, false);
        Log.d("stokc",AppConfig.GET_CHEMIST_STOCKIST + "[" + client_id + "," + city_id + "]");

      /*  MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY,
                AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY+"["+Stockist_id+","+user_id+"]" , this, false);
*/

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_ORDERLIST,
                AppConfig.GET_CHEMIST_ORDERLIST + client_id, this, false);

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_CHEMIST_PEDNING_BILLS,
                AppConfig.GET_CHEMIST_PEDNING_BILLS + client_id, this, false);

//        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_PARTIAL_CHEMIST_DATA,
//                AppConfig.GET_PARTIAL_CHEMIST_DATA + "[" + client_id + ",\"" + pref.getString(CHEMIST_LAST_DATA_SYNC, "") + "\"]", this, false);
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_PARTIAL_CHEMIST_DATA,
                AppConfig.GET_PARTIAL_CHEMIST_DATA + "[" + client_id + ",\"" + currentsyncdate + "\"]", this, false);
      //  String ip =  AppConfig.GET_PARTIAL_CHEMIST_DATA+"[" + client_id + ",\"" + currentsyncdate + "\"]";

       // System.out.println("checkip"+ip);
        try {

            JSONObject j_obj = new JSONObject();
            j_obj.put("chemistId", client_id);

            JSONArray j_arr = new JSONArray();
            j_arr.put(j_obj);

            MakeWebRequest.MakeWebRequest("out_array", AppConfig.GET_ALL_LEGEND_DATA, AppConfig.GET_ALL_LEGEND_DATA,
                    null, this, false, j_obj.toString());
         //   Log.d("leged",j_arr.toString());
         //   Log.d("leged11",AppConfig.GET_ALL_LEGEND_DATA);


        } catch (Exception e) {

        }

    }

    /* API Success Response */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        try {
            if (response != null ) {

              // if (j_ob1.has("StartRange"))
               Log.d("Response","DATA:"+response.toString());
                if (f_name.equals(AppConfig.POST_CHEMIST_CONFIRM_ORDER)) {

                   // Log.d("Show_Transaction11",response.toString());
                    //Log.d("Show_Transaction12",response.getString("Transaction_No"));
                    SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Transaction_No", response.getString("Transaction_No"));
                    editor.commit();
                    if(response.has("DOC_NO")) {

                        sqLiteHandler.deleteOrderByDocNo(response.getString("DOC_NO"));
                      //  Log.d("DocNo", response.getString("DOC_NO"));
                        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
                        MasterPlacedOrderDao masterPlacedOrderDao = daoSession.getMasterPlacedOrderDao();
                        masterPlacedOrderDao.deleteOrder(response.getString("DOC_NO"));
                       // if(camefrom!=null&&camefrom.length()>0&&camefrom.equalsIgnoreCase("pending")) {
                            Cursor cursor= sqLiteHandler.getLocationDatabyDocId(response.getString("DOC_NO"));

                            JSONObject jsonParams = new JSONObject();


                            if (cursor.getCount() > 0) {


                                if (cursor.moveToFirst()) {
                                    do {

                                        try {
                                            jsonParams.put("UserID", cursor.getString(cursor.getColumnIndex(Key_USERID)));
                                            jsonParams.put("CustID", Integer.valueOf(cursor.getString(cursor.getColumnIndex(Key_CUSTID))));
                                            jsonParams.put("task", cursor.getString(cursor.getColumnIndex(Key_TASK)));
                                            jsonParams.put("CurrentLocation", cursor.getString(cursor.getColumnIndex(Key_Address)));
                                            jsonParams.put("DOC_NO", cursor.getString(cursor.getColumnIndex(F_KEY_CC_DOC_NO)));
                                            jsonParams.put("Tran_No", cursor.getString(cursor.getColumnIndex(Key_TRANSACTION_ID)));
                                            jsonParams.put("Latitude", cursor.getString(cursor.getColumnIndex(Key_LATITUDE)));
                                            jsonParams.put("Longitude", cursor.getString(cursor.getColumnIndex(Key_LONGITUDE)));
                                            jsonParams.put("unqid", cursor.getString(cursor.getColumnIndex(Key_UNIQ_ID)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } while (cursor.moveToNext());
                                }
                                Intent download_intent = new Intent(this, SendingLocationOnByOrder.class);
                                download_intent.putExtra("doc_id", jsonParams.toString());
                                startService(download_intent);
                            }


                        //}
                        //sqLiteHandler.deleteLocationDatabyCustomerId(response.getString("DOC_NO"));

                    }


                    else{

                      //  OGtoast.OGtoast( "Server error while pushing products",getBaseContext());
                      //  Log.d("Error Text",response.toString());
                    }
                }
//**JYOTT 2/8
                else if (f_name.equals(AppConfig.POST_CHEMIST_CONFIRM_ORDER_APPWEB)) {

                   //** if(response.has("DOC_NO")) {
                    if(response.has("Uniqkid")) {
                        String og = "OG";
                        String og1 = og+response.getString("Uniqkid");
                        sqLiteHandler.deleteOrderByDocNo(og1);
                     //   Log.d("DocNochem", response.getString("DOC_NO"));
                     //   Log.d("og1", response.getString("Uniqkid"));
                      //  Log.d("og11", response.toString());
                      //  Log.d("og111", response.getString("Uniqkid"));
                        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
                        MasterPlacedOrderDao masterPlacedOrderDao = daoSession.getMasterPlacedOrderDao();
                        masterPlacedOrderDao.deleteOrder(og1);

                    }


                    else{

                      //  OGtoast.OGtoast( "Server error while pushing products",getBaseContext());
                       //  Log.d("Error Text",response.toString());
                    }
                }




//**JYOTT 2/8



                if (f_name.equals(AppConfig.checklogin)) {
                    login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");

                    if (login_type != null) {
                        if (login_type.equals(CHEMIST)) {
                            globalVariable.setToken(response.getString("token"));
                            SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            //editor.putString(encrypt("key"), encrypt(Token));
                            editor.putString("key", response.getString("token"));
                            Log.d("token",response.getString("token"));
                            editor.commit();
                           confirm_order_chemist(); //**jyott
                            get_refreshed_data(pref.getString(CLIENT_ID, ""), pref.getString(CLIENT_CITY_ID, ""), pref.getString(STOCKIST_ID, ""), pref.getString(USER_ID, ""));


                        }else{

                        globalVariable.setToken(response.getString("token"));
                        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        //editor.putString(encrypt("key"), encrypt(Token));
                        editor.putString("key", response.getString("token"));
                       // Log.d("token",response.getString("token"));
                        editor.commit();
                        confirm_order();
                        get_refreshed_data(pref.getString(CLIENT_ID, ""), pref.getString(CLIENT_CITY_ID, ""), pref.getString(STOCKIST_ID, ""), pref.getString(USER_ID, ""));
                    }

                    }
                }

            } else {
                // OGtoast.OGtoast( "Unable to connect to the server",getBaseContext());
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
         //   Log.d("qwerty",response.toString());
            if (f_name.equals(AppConfig.GET_PARTIAL_CHEMIST_DATA))
            {
                response.toString();
            }

            if (f_name.equals(AppConfig.GET_CHEMIST_STOCKIST))
            {
                SharedPreferences.Editor edt = pref.edit();
                edt.putString(CHEMIST_STOCKISTLIST, response.toString());
                edt.commit();
            }

            if (f_name.equals(AppConfig.GET_CHEMIST_ORDERLIST)) {

              //  Log.e("Response333", response.toString());
                SharedPreferences.Editor edt = pref.edit();
                edt.putString(CHEMIST_ORDERLIST, response.toString());
                edt.commit();
            }

            if (f_name.equals(AppConfig.GET_CHEMIST_PEDNING_BILLS)) {
                SharedPreferences.Editor edt = pref.edit();
                edt.putString(CHEMIST_PENDING_BILLS, response.toString());
                edt.commit();
            }

            //************jyott*************//
            if (f_name.equals(AppConfig.POST_CHEMIST_CONFIRM_ORDER_APPWEB)) {
                String jsondata = response.toString();
                try {
                if (!jsondata.isEmpty()) {

                    JSONObject j_obj = null;

                        j_obj = response.getJSONObject(0);

                    String og2 = "OG";
                    String Uniqkid = og2+j_obj.getString("Uniqkid");
                  //  Log.d("Uniqkid", Uniqkid);
                    sqLiteHandler.deleteOrderByDocNo(Uniqkid);
                   // Log.d("DocNochem", "success");

                    DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
                    MasterPlacedOrderDao masterPlacedOrderDao = daoSession.getMasterPlacedOrderDao();
                    masterPlacedOrderDao.deleteOrder(Uniqkid);



                }
                } catch (Exception e)
                {

                   // Log.d("Message",e.getMessage());
                }

                //PPPPPPPPP
//                Log.d("resp",response.toString());
//                Log.d("resp1", String.valueOf(response.length()));
//                Log.d("resp1", String.valueOf(response.length()));
//                for (int i = 0; i < response.length(); i++) {
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        String og2 = "OG";
//                        String Uniqkid = response.getJSONObject(0).getString("Uniqkid");
//                        Toast.makeText(getApplicationContext(), "abc"+Uniqkid, Toast.LENGTH_SHORT).show();
//                        Log.d("resp1", Uniqkid);
//                        Log.d("resp12", og2+Uniqkid);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                if(response.has("Uniqkid")) {
//                    String og = "OG";
//                    String og1 = og+response.getString("Uniqkid");
//                    sqLiteHandler.deleteOrderByDocNo(og1);
//                    //   Log.d("DocNochem", response.getString("DOC_NO"));
//                    Log.d("og1", response.getString("Uniqkid"));
//                    Log.d("og11", response.toString());
//                    Log.d("og111", response.getString("Uniqkid"));
//                    DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
//                    MasterPlacedOrderDao masterPlacedOrderDao = daoSession.getMasterPlacedOrderDao();
//                    masterPlacedOrderDao.deleteOrder(og1);
//
//                }
            }



            //************jyott*************//


            if (f_name.equals(AppConfig.GET_ALL_LEGEND_DATA)) {

                try {
                 //   Log.d("Legend", response.toString());

                    for (int j = 0; j < response.length(); j++) {
                        Boolean isDataavailable = false;
                        Cursor crs = db.get_legend_data(response.getJSONObject(j).getString("ClientID"));

                        if (crs != null && crs.getCount() > 0) {
                            while (crs.moveToNext()) {
                                isDataavailable = true;
                            }
                        }

                        if (!isDataavailable) {
                            for (int i = 0; i < response.getJSONObject(0).getJSONArray("stk_legends").length(); i++) {
                                db.insert_into_stockist_legend(response.getJSONObject(j).getString("ClientID"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("LegendName"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("ColorCode"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("StartRange"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("EndRange"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("StockLegendID"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("LegendMode"));
                            }
                        } else {
                            db.delete_legend_data(response.getJSONObject(j).getString("ClientID"));

                            for (int i = 0; i < response.getJSONObject(0).getJSONArray("stk_legends").length(); i++) {

                                db.insert_into_stockist_legend(response.getJSONObject(j).getString("ClientID"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("LegendName"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("ColorCode"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("StartRange"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("EndRange"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("StockLegendID"),
                                        response.getJSONObject(j).getJSONArray("stk_legends").getJSONObject(i).getString("LegendMode"));
                            }

                        }

                    }
                } catch (Exception e) {

                }

            }


        } else {
          /*  OGtoast.OGtoast( "Unable to connect to the server",
                    getBaseContext());*/
        }

    }

    void confirm_order()
    {
/*
       Cursor crs_order_json = db.get_order_json();


        if (crs_order_json != null && crs_order_json.getCount() > 0) {
            try {
                if (crs_order_json.moveToFirst()) {
                    do {

                       String SjArray=crs_order_json.getString(crs_order_json.getColumnIndex("json"));
                        Log.e("SjArrayyy",SjArray);

                        MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_CHEMIST_CONFIRM_ORDER, AppConfig.POST_CHEMIST_CONFIRM_ORDER,
                                new JSONArray(SjArray), this,false,"");

                    } while (crs_order_json.moveToNext());
                }

            }catch (Exception e)
            {
               e.toString();
            }
        }
*/

        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        MasterPlacedOrderDao masterPlacedOrderDao = daoSession.getMasterPlacedOrderDao();
        List<MasterPlacedOrder> masterPlacedOrderList = masterPlacedOrderDao.loadAll();

        for (MasterPlacedOrder masterPlacedOrder : masterPlacedOrderList)
        {
            String SjArray = masterPlacedOrder.getJson();
           // Log.e("orderArraySALESMAN", SjArray);
            try
            {
                MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_CHEMIST_CONFIRM_ORDER, AppConfig.POST_CHEMIST_CONFIRM_ORDER,
                        new JSONArray(SjArray), this, false, "");
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    void confirm_order_chemist()
    {

        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        MasterPlacedOrderDao masterPlacedOrderDao = daoSession.getMasterPlacedOrderDao();

        List<MasterPlacedOrder> masterPlacedOrderList = masterPlacedOrderDao.loadAll();

        for (MasterPlacedOrder masterPlacedOrder : masterPlacedOrderList)
        {
            String SjArray = masterPlacedOrder.getJson();


              //  Log.d("jsonArray114", SjArray);
              //  Log.d("orderArrayCHEMIST", SjArray);
            try
            {
//                MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_CHEMIST_CONFIRM_ORDER_APPWEB, AppConfig.POST_CHEMIST_CONFIRM_ORDER_APPWEB,
//                        new JSONArray(SjArray), this, false, "");

                MakeWebRequest.MakeWebRequest("out_array", AppConfig.POST_CHEMIST_CONFIRM_ORDER_APPWEB, AppConfig.POST_CHEMIST_CONFIRM_ORDER_APPWEB,
                        new JSONArray(SjArray), this, false, "");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    void get_user_credentials() {
        String userEncrypted = pref.getString(CLIENT_USERNAME, encrypt(""));
        String passEncrypted = pref.getString(CLIENT_PASSWORD, encrypt(""));
        String pass = decrypt(passEncrypted);
        String user = decrypt(userEncrypted);

        try {
            JSONObject jsonParams = new JSONObject();

            jsonParams.put("email", user);
            jsonParams.put("password", pass);


            globalVariable.setToken(null);
            MakeWebRequest.MakeWebRequest("Post", AppConfig.checklogin, AppConfig.checklogin, jsonParams, this, false);
        } catch (Exception E) {

        }
    }

}





