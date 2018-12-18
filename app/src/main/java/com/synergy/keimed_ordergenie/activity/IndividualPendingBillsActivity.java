package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.model.PaymentGetSet;
import com.synergy.keimed_ordergenie.utils.Utility;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.ad_customerlist;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_pendingbills;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.RecyclerItemClickListener;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by prakash on 08/07/16.
 */
public class IndividualPendingBillsActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess,
        DatePickerDialog.OnDateSetListener {

    private static final String CHEMIST_ID = "Chemist_id";
    private String Chemist_id, Stockist_id;
    private Float Total_paid = 0f, Total_balance = 0f;
    private TextView txt_start_date, txt_end_date;
    private Date filter_start_date, filter_end_date;
    private int nYear, nMonth, Nday;
    DatePickerDialog dpd_start_date, dpd_end_date;
    List<m_pendingbills> posts;
    SharedPreferences pref;
    private String invoice_flag="0";
    private Boolean IsCallPlanTask;
    private  String chemist_Name;
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private Date current_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");

    public static final String CHEMIST_ORDER_ID = "chemist_order_id";
    public static final String CHEMIST_ORDER_DATE = "chemist_order_date";
    public static final String STOCKIST_INVOICE_No = "invoice_id";

    @BindView(R.id.rv_datalist)
    RecyclerView rvCustomerlist;

    @BindView(R.id.txt_paid)
    TextView txt_paid;

    @BindView(R.id.empty_view)
    TextView empty_view;

    @BindView(R.id.txt_balance)
    TextView txt_balance;


    /*@BindView(R.id.fab)
    FloatingActionButton fab;*/
    String getChemistId;


    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private ArrayList<PaymentGetSet> arrayListPaymentSQLite;
    private PaymentGetSet paymentGetSet;
    private int exportPosition = 0;
    private JSONObject jsonObjectPayment;
    private int postSQLitePayment = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_bill);

        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        IsCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);
        chemist_Name=getIntent().getStringExtra(CLIENT_NAME);
        Chemist_id = pref.getString(CLIENT_ID, "0");
        Stockist_id = pref.getString(USER_ID, "0");
        //getChemistId = getIntent().getStringExtra(CHEMIST_ID);
        getChemistId = getIntent().getStringExtra("chemist_id");
               Log.i("Pending Bills","Pending");

        //Log.d("getChemistId",getChemistId);
        //1245

        /* Initialize SQLite Class */
        sqLiteHandler = new SQLiteHandler(this);


        setTitle(chemist_Name);
        rvCustomerlist.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (IsCallPlanTask) {
                            Intent i = new Intent(IndividualPendingBillsActivity.this, Payment_Option.class);
                            i.putExtra("Invoiceno", posts.get(position).getInvoiceno());
                            i.putExtra("Invoicedate", posts.get(position).getInvoicedate());
                            i.putExtra("Totalitems", posts.get(position).getTotalitems());
                            i.putExtra("Billamount", posts.get(position).getBillamount());
                            i.putExtra("Balanceamt", posts.get(position).getBalanceamt());
                            i.putExtra("receiver", getIntent().getParcelableExtra("receiver"));
                            i.putExtra("chemist_id", getChemistId);
                            startActivity(i);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Invoice_details.class);
                            //put invoice id
                            intent.putExtra(STOCKIST_INVOICE_No, posts.get(position).getInvoiceID());
                            intent.putExtra("invoice_no", posts.get(position).getInvoiceno());
                            intent.putExtra(CHEMIST_ORDER_DATE, posts.get(position).getInvoicedate().toString());
                            startActivity(intent);
                        }
                    }
                })
        );


    }



    /* onResume */
    @Override
    public void onResume() {
        if (IsCallPlanTask) {
            if (Utility.internetCheck(this)) {
                MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST,
                        AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST + "[" + getIntent().getStringExtra("chemist_id") + "," + Chemist_id + "]", this, true);
            } else {
                getInvoiceListSQLite();
            }
        } else {
            /*MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST,
                    AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST +"["+Stockist_id+","+Chemist_id+"]" , this, true);*/

            if (Utility.internetCheck(this)) {
                MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST,
                        AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST + "[" + getChemistId + "," + Chemist_id + "]", this, true);
                Log.d("pending",AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST + "[" + getChemistId + "," + Chemist_id + "]");

            } else {
                getInvoiceListSQLite();
            }
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pendingbill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter:
                show_filter_dialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void json_update(List<?> posts) {
        if (posts.size() > 0) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }

        get_total_paid_balance(posts);
        final int PENDINGBILLS = 4;
        ad_customerlist adapter = new ad_customerlist(this, posts, PENDINGBILLS, R.layout.fragement_pendingbills_items, BR.v_pendingbills);
        rvCustomerlist.setLayoutManager(new LinearLayoutManager(this));
        rvCustomerlist.setAdapter(adapter);
    }



    /* APIs Success Response */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        if (response != null) {
            if (f_name.equals(AppConfig.SAVE_PAYMENT)) {
                if (postSQLitePayment == 1) {
                    updateStatusPayment();
                }
            }
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            try {
                if (f_name.equals(AppConfig.GET_STOCKIST_INDIVIDUAL_PENDINGLIST)) {
                    String jsondata = response.toString();
                    Log.d("jsondata",jsondata);
                    if (!jsondata.isEmpty()) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        posts = new ArrayList<m_pendingbills>();
                        posts = Arrays.asList(mGson.fromJson(jsondata, m_pendingbills[].class));
                        json_update(posts);
                        insertInvoiceSQLite(jsondata);
                    }
                } else if (f_name.equals(AppConfig.SAVE_ORDER_INVOICE)) {
                    if (postSQLitePayment == 1) {
                        sqLiteHandler.deletePaymentRecord(paymentGetSet.getInvoiceNo());
                        exportPosition = exportPosition + 1;
                        if (arrayListPaymentSQLite.size() != exportPosition) {
                            addDataInJsonFormat();
                        }
                    }
                }
            } catch (Exception e) {
                e.toString();
            }
        }
    }





    /* Get SQLite Invoice List */
    private void getInvoiceListSQLite() {
        Cursor cursor = sqLiteHandler.getPendingInvoiceList(getChemistId);
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            Toast.makeText(this, "Invoice Not Found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    String invoiceNo = cursor.getString(4);
                    String invoiceId = cursor.getString(5);
                    String invoiceDate = cursor.getString(6);
                    String totalItems = cursor.getString(7);
                    String billAmount = cursor.getString(8);
                    String paymentReceived = cursor.getString(9);
                    String balanceAmount = cursor.getString(10);
                    String grandTotal ="0";
                    String totalDiscount = "0";
                    String ledgerBalance = "0";

                    Log.d("InvoiceNo", invoiceNo);
                    Log.d("InvoiceId", invoiceId);
                    Log.d("InvoiceDate", invoiceDate);
                    Log.d("TotalItems", totalItems);
                    Log.d("BillAmount", billAmount);
                    Log.d("PaymentReceived", paymentReceived);
                    Log.d("BalanceAmount", balanceAmount);

                    m_pendingbills m_pendingbills = new m_pendingbills(invoiceNo, invoiceDate, Integer.valueOf(totalItems), billAmount,
                            paymentReceived, balanceAmount, invoiceId,grandTotal,totalDiscount,ledgerBalance);
                    posts.add(m_pendingbills);

                    if (cursorCount == posts.size()) {
                        json_update(posts);
                    }
                } while (cursor.moveToNext());
            }
        }
    }





    /* Insert Invoice List In SQLite */
    private void insertInvoiceSQLite(String responseData) {
        Cursor cursor = sqLiteHandler.getPendingInvoiceList(getChemistId);
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            sqLiteHandler.deleteInvoiceList(getChemistId);
        }

        try {
            JSONArray jsonArray = new JSONArray(responseData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String invoiceNo = jsonObject.getString("Invoiceno");
                String invoiceId = jsonObject.getString("InvoiceID");
                String invoiceDate = jsonObject.getString("Invoicedate");
                String totalItems = jsonObject.getString("Totalitems");
                String billAmount = jsonObject.getString("Billamount");
                String paymentReceived = jsonObject.getString("Paymentreceived");
                String balanceAmount = jsonObject.getString("Balanceamt");

                if (invoiceId.equalsIgnoreCase("null")) {
                    invoiceId = "0";
                }
                if (invoiceDate.equalsIgnoreCase("null")) {
                    invoiceDate = "";
                }
                if (totalItems.equalsIgnoreCase("null")) {
                    totalItems = "0";
                }
                if (billAmount.equalsIgnoreCase("null")) {
                    billAmount = "0";
                }
                if (paymentReceived.equalsIgnoreCase("null")) {
                    paymentReceived = "0";
                }
                if (balanceAmount.equalsIgnoreCase("null")) {
                    balanceAmount = "0";
                }

                /* Insert Values */     // Here "Chemist_id"  is  UserId    &   "getChemistId"  is  RealChemistId
                /*sqLiteHandler.insertInvoiceListPayment(Chemist_id, Stockist_id, getChemistId, invoiceNo, invoiceId, invoiceDate, totalItems,
                        billAmount, paymentReceived, balanceAmount);*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    void get_total_paid_balance(List<?> arraY) {
        Total_paid = 0f;
        Total_balance = 0f;
        try {
            for (int i = 0; i < arraY.size(); i++) {
                m_pendingbills o_m_pendingbills = (m_pendingbills) arraY.get(i);
                Total_paid = Total_paid + Float.parseFloat(o_m_pendingbills.getPaymentreceived());
                Total_balance = Total_balance + Float.parseFloat(o_m_pendingbills.getBalanceamt());

                txt_paid.setText("Rs. " + Total_paid);
                txt_balance.setText("Rs. " + Total_balance);
            }
        } catch (Exception e) {

        }
    }

    private void show_filter_dialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_individual_pending_bill_filter, null);
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
                        IndividualPendingBillsActivity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_start_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_start_date.setMaxDate(Calendar.getInstance());
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
                        IndividualPendingBillsActivity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_end_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_end_date.setMaxDate(Calendar.getInstance());
            }


        });

        dialogview.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filter_start_date != null && filter_end_date != null) {
                    filter_dialog_conditions(filter_start_date, filter_end_date);
                    infoDialog.dismiss();
                } else {
                    OGtoast.OGtoast("Please select start date and date  to filter", IndividualPendingBillsActivity.this);
                }
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json_update(posts);
                txt_end_date.setText("");
                txt_end_date.setText("");
                filter_start_date = null;
                filter_end_date = null;
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

        final List<m_pendingbills> filteredModelList = new ArrayList<>();
        for (int q = 0; q < posts.size(); q++) {
            m_pendingbills o_m_pendingbills = (m_pendingbills) posts.get(q);

            if (startDate != null && endDate != null) {
                final String date = o_m_pendingbills.getInvoicedate();
                try {
                    convertedDate = dateFormat.parse(date);
                    String c_date = sdf.format(convertedDate);
                    convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                } catch (Exception e) {
                }

                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                {
                    filteredModelList.add(o_m_pendingbills);
                }
            }
        }
        json_update(filteredModelList);
    }



    public Intent getSupportParentActivityIntent() {
        Intent newIntent = null;
        try {
            if (!IsCallPlanTask) {
                newIntent = new Intent(IndividualPendingBillsActivity.this, MainActivity.class);
            }
            //you need t o define the class with package name
            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIntent;
    }



    /* onStart */
    @Override
    protected void onStart() {
        super.onStart();
        new checkInternetConn().execute(0);
    }



    /* onPause */
    @Override
    protected void onPause() {
        super.onPause();
        new checkInternetConn().execute(0);
    }



    /* Check Internet Connection Class */
    private class checkInternetConn extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update your layout here
            if (InternetConnection.checkConnection(getApplication())) {
                //uploadSQLiteData();
            }
            super.onPostExecute(result);

            //Do it again!
            new checkInternetConn().execute(8000);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
    }




    /* Check Internet Connection on Current State */
    public static class InternetConnection {
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
            return false;
        }
    }



    ////----    Post Payment SQLite Data    ----////

    /* Upload SQLite Data To Server */
    /*private void uploadSQLiteData() {
        arrayListPaymentSQLite = new ArrayList<>();
        Cursor cursor = sqLiteHandler.getPaymentRecord(getChemistId);
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            //Log.d("RecordNotFound", ""+cursorCount);
        } else {
            if (cursor.moveToFirst()) {
                do {
                    PaymentGetSet paymentGetSet = new PaymentGetSet();
                    paymentGetSet.setUserId(cursor.getString(1));
                    paymentGetSet.setChemistId(cursor.getString(2));
                    paymentGetSet.setInvoiceNo(cursor.getString(3));
                    paymentGetSet.setInvoiceDate(cursor.getString(4));
                    paymentGetSet.setSavedDate(cursor.getString(5));
                    paymentGetSet.setTotalItems(cursor.getString(6));
                    paymentGetSet.setCashReceived(cursor.getString(7));
                    paymentGetSet.setTotalAmount(cursor.getString(8));
                    paymentGetSet.setBalanceAmount(cursor.getString(9));
                    paymentGetSet.setPaymentModeId(cursor.getString(10));
                    paymentGetSet.setPaymentMode(cursor.getString(11));
                    paymentGetSet.setChequeDate(cursor.getString(12));
                    paymentGetSet.setChequeNo(cursor.getString(13));
                    paymentGetSet.setBankId(cursor.getString(14));
                    paymentGetSet.setBankName(cursor.getString(15));
                    paymentGetSet.setPaymentId(cursor.getString(16));
                    paymentGetSet.setNarration(cursor.getString(17));
                    paymentGetSet.setStatus(cursor.getString(18));
                    paymentGetSet.setStockistId(cursor.getString(19));
                    paymentGetSet.setMicrNo(cursor.getString(20));

                    arrayListPaymentSQLite.add(paymentGetSet);

                    if (cursorCount == arrayListPaymentSQLite.size()) {
                        addDataInJsonFormat();
                    }
                } while (cursor.moveToNext());
            }
        }
    }*/


    /* Add Data in Json */
    private void addDataInJsonFormat() {
        paymentGetSet = arrayListPaymentSQLite.get(exportPosition);
        jsonObjectPayment = new JSONObject();
        try {
            jsonObjectPayment.put("Invoice_No", paymentGetSet.getInvoiceNo());
            jsonObjectPayment.put("Doc_No", "");
            jsonObjectPayment.put("Doc_Date", paymentGetSet.getInvoiceDate());
            jsonObjectPayment.put("PaymodeId", paymentGetSet.getPaymentModeId());
            jsonObjectPayment.put("PaymodeDesc", paymentGetSet.getPaymentMode());
            jsonObjectPayment.put("Chq_No", paymentGetSet.getChequeNo());
            jsonObjectPayment.put("Chq_Date", paymentGetSet.getChequeDate());
            jsonObjectPayment.put("BankAccountID", "");
            jsonObjectPayment.put("Bank_Id", paymentGetSet.getBankId());
            jsonObjectPayment.put("BankName", paymentGetSet.getBankName());
            jsonObjectPayment.put("Amount", paymentGetSet.getCashReceived());
            jsonObjectPayment.put("Narration", paymentGetSet.getNarration());
            jsonObjectPayment.put("PaymentID", paymentGetSet.getPaymentId());
            jsonObjectPayment.put("Stockist_Client_ID", paymentGetSet.getStockistId());
            jsonObjectPayment.put("Chemist_ID", paymentGetSet.getChemistId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("postParamsJson", jsonObjectPayment.toString());
        if (Utility.internetCheck(this)) {
            postSQLitePayment = 1;
            MakeWebRequest.MakeWebRequest("Post", AppConfig.SAVE_PAYMENT, AppConfig.SAVE_PAYMENT, jsonObjectPayment, this, false);
        }
    }



    /* Get Payment Data From SQLite */
    private void updateStatusPayment() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("InvoiceNo", paymentGetSet.getInvoiceNo());
            jsonObject1.put("StockistID", paymentGetSet.getStockistId());
            jsonObject1.put("Status", "4");

            jsonArray.put(jsonObject1);

            jsonObject.put("invoices", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }  MakeWebRequest.MakeWebRequest("out_array", AppConfig.SAVE_ORDER_INVOICE, AppConfig.SAVE_ORDER_INVOICE,
                null, this, false, jsonObject.toString());
        //Log.d("updatedInvoiceStatus", jsonObject.toString());
        if (Utility.internetCheck(this)) {

        }
    }


}


