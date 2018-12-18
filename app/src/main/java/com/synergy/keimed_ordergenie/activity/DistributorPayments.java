package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.PaymentListAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.DistributorpaymentListmodal;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class DistributorPayments extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess,DatePickerDialog.OnDateSetListener {

    TextView total_products;
    Double TotalPayment=0.0;
    String Stockist_id;
    private SearchView searchView;
    SharedPreferences pref;
    private Menu oMenu;
    RecyclerView recycler_view_payment;
    PaymentListAdapter paymentListAdapter;
    List<DistributorpaymentListmodal> paymentList = new ArrayList<>();
    private  TextView startdate,enddate;
    private RadioButton chk_cheq,chk_cash;
    private Date filter_start_date, filter_end_date;
    private Date current_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialog dpd_start_date, dpd_end_date;
    private int nYear, nMonth, Nday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_payments);

        total_products = (TextView) findViewById(R.id.total_products);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Stockist_id = pref.getString(CLIENT_ID, "0");

        setTitle("Payments");
        //paymentlist_row
        recycler_view_payment = (RecyclerView) findViewById(R.id.recycler_view_payment);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_view_payment.getContext(),
                DividerItemDecoration.VERTICAL);
        recycler_view_payment.addItemDecoration(dividerItemDecoration);
        fill_payment_list(paymentList);
        //paymentList();

        getPayments();
    }

    private void getPayments() {

//        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INVENTORY,
//                AppConfig.GET_STOCKIST_INVENTORY + Stockist_id, this, true);


        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_DISTRIBUTOR_PAYMENTS,
                AppConfig.GET_DISTRIBUTOR_PAYMENTS + Stockist_id, this, true);

    }

    @Override
    protected void onResume() {
//        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_SUMMARY,
//                AppConfig.GET_STOCKIST_INDIVIDUAL_ORDER_SUMMARY + "[" + Stockist_id + "," + User_id + "," + selected_chemist_id + "]", this, true);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Customer Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {



                    newText = newText.toLowerCase();
                    List<DistributorpaymentListmodal> filteredModelList = new ArrayList<>();
                    for (DistributorpaymentListmodal model : paymentList) {

                        try {

                            final String text = model.getCustName().toLowerCase();
                            if (text.contains(newText)) {
                                filteredModelList.add(model);
                            }
                        }catch (Exception e){

                        }

                    }
                    fill_payment_list(filteredModelList);
                    recycler_view_payment.scrollToPosition(0);



                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter:
                show_dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void show_dialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        //  final View dialogview = inflater.inflate(R.layout.dialog_product_catalog_stockist_filter, null);
        final View dialogview = inflater.inflate(R.layout.dialog_payment_stockist_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        startdate = (TextView) dialogview.findViewById(R.id.start_date);
        enddate = (TextView) dialogview.findViewById(R.id.end_date);
        chk_cheq = (RadioButton) dialogview.findViewById(R.id.cheque_payment);
        chk_cash = (RadioButton) dialogview.findViewById(R.id.cash_payment);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(startdate.getText().toString());
                    Log.d("current_date", String.valueOf(current_date));

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
                        DistributorPayments.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_start_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_start_date.setMaxDate(Calendar.getInstance());
            }

        });

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(enddate.getText().toString());
                    Log.d("enddate", String.valueOf(current_date));

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
                        DistributorPayments.this,
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
                filter_dialog_conditions(filter_start_date, filter_end_date, chk_cash.isChecked(), chk_cheq.isChecked());
                infoDialog.dismiss();
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fill_payment_list(paymentList);
                infoDialog.dismiss();
            }
        });
        set_attributes(infoDialog);
        infoDialog.show();
    }
    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isCheckPayment, Boolean isCashPayment) {

        Date convertedDate = null;
        final List<DistributorpaymentListmodal> filteredModelList = new ArrayList<>();

        for (DistributorpaymentListmodal model : paymentList) {

            if (startDate != null && endDate != null) {
                final String date = model.getPaymentDate();
                try {
                    convertedDate = dateFormat.parse(date);
                    String c_date = sdf.format(convertedDate);
                    convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                } catch (Exception e) {
                    Log.d("Excep",e.getMessage());
                }

                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                {

                    if (isCheckPayment)
                    {

                        if(model.getPaymentModeID().equals("3") )
                        {

                            filteredModelList.add(model);
                        }

                    }
                    else if (isCashPayment)
                    {
                        if(model.getPaymentModeID().equals("0") || model.getPaymentModeID().equals("2") || model.getPaymentModeID().equals("5")){

                            filteredModelList.add(model);
                        }

                    }
                 //   filteredModelList.add(model);
                }
              //  fill_payment_list(filteredModelList);
            }
            else
            {
                if (isCheckPayment)
                {

                    if(model.getPaymentModeID().equals("3")){

                        filteredModelList.add(model);
                    }


                }
                else if (isCashPayment)
                {
                    if(model.getPaymentModeID().equals("0") || model.getPaymentModeID().equals("2") || model.getPaymentModeID().equals("5")){

                        filteredModelList.add(model);
                    }

                }

                //fill_payment_list(filteredModelList);
            }
        }
        fill_payment_list(filteredModelList);
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
        int maxY = mdispSize.y;

        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = maxX;   //x position
        wlp.y = actionbarsize - 20;   //y position
        // wlp.width = 50;
        // wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

    }

    private void paymentList() {
//        DistributorpaymentListmodal distributorpaymentListmodal = new DistributorpaymentListmodal("18 AUG 2017", "Sarthak Medico", "SBI100", "9:30 am", "10000");
//        paymentList.add(distributorpaymentListmodal);
//
//        distributorpaymentListmodal = new DistributorpaymentListmodal("17 SEP 2018", "DBS Mediko", "SBI202", "5:30 pm", "2000");
//        paymentList.add(distributorpaymentListmodal);
//
//        distributorpaymentListmodal = new DistributorpaymentListmodal("20 SEP 2018", "Gupta Mediko", "SBI204", "2:30 pm", "3002");
//        paymentList.add(distributorpaymentListmodal);
//
//        distributorpaymentListmodal = new DistributorpaymentListmodal("22 SEP 2018", "Lifemate Mediko", "SBI206", "4:30 pm", "4000");
//        paymentList.add(distributorpaymentListmodal);
//
//        distributorpaymentListmodal = new DistributorpaymentListmodal("25 SEP 2018", "Akash Mediko", "SBI208", "1:30 pm", "1000");
//        paymentList.add(distributorpaymentListmodal);
       // paymentListAdapter.notifyDataSetChanged();
    }
    public  void  fill_payment_list(final List<DistributorpaymentListmodal> posts_s)
    {
        Log.d("size",String.valueOf(posts_s.size()));
        paymentListAdapter = new PaymentListAdapter(posts_s);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_payment.setLayoutManager(mLayoutManager);
        recycler_view_payment.setItemAnimator(new DefaultItemAnimator());
        recycler_view_payment.setAdapter(paymentListAdapter);
    }



    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response!=null)
        {
            if (f_name.equals(AppConfig.GET_DISTRIBUTOR_PAYMENTS)) {

                Log.d("Distributorpayment",response.toString());

                get_order_list_json(response.toString());

            }
        }


    }

    private void get_order_list_json(String jsondata) {

        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();
            paymentList = new ArrayList<DistributorpaymentListmodal>();
            paymentList = new LinkedList<DistributorpaymentListmodal>(Arrays.asList(mGson.fromJson(jsondata, DistributorpaymentListmodal[].class)));
            // posts = Arrays.asList(mGson.fromJson(jsondata, m_order[].class));

            for(int i = 0;i<paymentList.size();i++){

                if(paymentList.get(i).getInvoiceAmount()!=null);
                TotalPayment = TotalPayment+paymentList.get(i).getInvoiceAmount();
                // ToatlAmount=ToatlAmount+pendinglist.get(i).getCust_outstanding();

            }
            total_products.setText("Rs : " + String.valueOf(TotalPayment));
            Log.d("TotalAmount", String.valueOf(TotalPayment));

            fill_payment_list(paymentList);
        }


    }




    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        if (dpd_start_date != null) {
            if (view.getId() == dpd_start_date.getId()) {
                try {
                    filter_start_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    startdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (Exception e) {
                    Log.d("Excep",e.getMessage());
                }
            }
            dpd_start_date = null;
        }

        if (dpd_end_date != null) {
            if (view.getId() == dpd_end_date.getId()) {
                try {
                    filter_end_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "T00:00:00.000Z");
                    enddate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (Exception e) {
                    Log.d("Excepp",e.getMessage());
                }
                dpd_end_date = null;
            }

        }
    }
}
