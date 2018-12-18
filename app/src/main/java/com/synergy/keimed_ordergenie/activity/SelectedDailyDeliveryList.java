package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.text.DateFormatSymbols;
import java.util.Locale;
import java.text.ParseException;

import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.ReportDeliveryInvoiceListAdapter;
import com.synergy.keimed_ordergenie.model.m_report_delivery_invoice_list;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class SelectedDailyDeliveryList extends AppCompatActivity implements View.OnClickListener,
        SelectedInvoiceDelivery,DatePickerDialog.OnDateSetListener {

    /* Find Views */

    private LinearLayout linearLayoutMakePayment;
    private ReportDeliveryInvoiceListAdapter paymentListAdapter;
    private RecyclerView rec_payment;
    private int mYear, mMonth, mDay;
    private Boolean IsCallPlanTask;
    DatePickerDialog dpd_start_date, dpd_end_date;
    RadioGroup radioGroup;
    String filterflag="0";
    SharedPreferences pref;
    List<m_report_delivery_invoice_list> posts;
    TextView txt_start_date,txt_end_date,txt_delivery;
    private String accessKey, userId, stockistId, chemistId, chemistName, User_id,Stockist_id;
    private TextView textViewTotalBalance;
    private Date current_date = Calendar.getInstance().getTime();
    private TextView empty_view;
    TextView title_report,textView_date;
    AutoCompleteTextView autoCompleteTextView;
    //private EditText editTextAmount;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    private Date filter_start_date, filter_end_date;

    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;

    String[] comp_names;

    /*@BindView(Cheque_date)
    TextView _Cheque_date;

    @BindView(R.id.Cheque_number)
    EditText _Cheque_number;

    @BindView(R.id.micr_number)
    EditText _micr_number;

    @BindView(R.id.account_number)
    EditText _account_number;

    @BindView(R.id.bank_name)
    AutoCompleteTextView bank_name;*/

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_invoice_list);

        /* Initialize Class */
        sqLiteHandler = new SQLiteHandler(this);

        linearLayoutMakePayment = (LinearLayout) findViewById(R.id.bottom_layout);
        // textViewTotalBalance = (TextView) findViewById(R.id.txt_total_balance_amount_payment);
        empty_view = (TextView) findViewById(R.id.empty_view);
        title_report = (TextView) findViewById(R.id.title_report);

        comp_names = getResources().getStringArray(R.array.Company_names_list);
        setTitle("Delivery Report");
        title_report.setText("INVOICE LIST");
        /* Make Payment Click */
        findViewById(R.id.complete_delivery).setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rec_payment = (RecyclerView) findViewById(R.id.recycler_payment);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rec_payment.getContext(), DividerItemDecoration.VERTICAL);
        rec_payment.addItemDecoration(dividerItemDecoration);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        IsCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);

//        accessKey = pref.getString("key", "");
//        userId = pref.getString(USER_ID, "0");
//        stockistId = pref.getString(CLIENT_ID, "0");
//        chemistId = "53019";
//        chemistName = "53019";
//        chemistId = getIntent().getStringExtra("chemist_id");
//        chemistName = getIntent().getStringExtra("chemist_name");

//        Log.d("chemistId",chemistId);
//        Log.d("chemistName",chemistName);

//        chemist_Name = getIntent().getStringExtra("chemistName");
//        getChemistId = getIntent().getStringExtra(CHEMIST_ID);
//
//        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    autoCompleteTextView.showDropDown();
//                }
//            }
//        });
//
//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
//            }
//        });
    }




    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
        getDeliveryInvoiceListSQLite();
    }


    /* onCreate Option Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_make_payment, menu);

        searchView = (SearchView) menu.findItem(R.id.invoicenosearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                  newText = newText.toLowerCase();

                List<m_report_delivery_invoice_list> nList = new ArrayList<>();

                for(m_report_delivery_invoice_list model : posts){

                    String customerName = model.getClient_Name().toLowerCase();
                    String invoice_no = model.getInvoiceNo().toLowerCase();
                    if(customerName.contains(newText) || invoice_no.contains(newText) ){

                    nList.add(model);

                    }


                }

                fill_payment_list(nList);

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

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_search:
                return true;
            case R.id.action_filter:
                show_dialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void show_dialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_delivery_invoicereport, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        txt_start_date = (TextView) dialogview.findViewById(R.id.txt_start_date);
        txt_end_date = (TextView) dialogview.findViewById(R.id.txt_end_date);
        //txt_delivery = (TextView) dialogview.findViewById(R.id.complete_delivery);
        final CheckBox chk_pending = (CheckBox) dialogview.findViewById(R.id.chk_pending);
        final CheckBox chk_delivered = (CheckBox) dialogview.findViewById(R.id.chk_completed);
        final CheckBox chk_undelivered = (CheckBox) dialogview.findViewById(R.id.chk_notdelivered);
       // txt_delivery.setVisibility(View.GONE);
//        chk_pending.setVisibility(View.GONE);
//        chk_delivered.setVisibility(View.GONE);
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
                        SelectedDailyDeliveryList.this,
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
                        SelectedDailyDeliveryList.this,
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

                filter_dialog_conditions(filter_start_date, filter_end_date, chk_pending.isChecked(), chk_delivered.isChecked(),chk_undelivered.isChecked());
                infoDialog.dismiss();
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fill_payment_list(posts);
                infoDialog.dismiss();
            }
        });

        set_attributes(infoDialog);
        infoDialog.show();

    }

    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isPendingChecked, Boolean isDeliveredChecked,Boolean isUndeiveredCheck) {

        Date convertedDate = null;

        final List<m_report_delivery_invoice_list> filteredModelList = new ArrayList<>();
//      if(posts==null||posts.toString().equals(null)||posts.isEmpty())
//      {
//          Toast.makeText(getApplicationContext(),"No Records Found",Toast.LENGTH_SHORT).show();
//      }
      if(posts!=null)
      {
        for (m_report_delivery_invoice_list model : posts) {

            Log.d("Status12",model.getInvoiceDate());
            Log.d("Status12",model.getInvoiceDate());
            if (startDate != null && endDate != null) {
                final String date = model.getDeliveryDate();
                try {
                    convertedDate = dateFormat.parse(date);
                    String c_date = sdf.format(convertedDate);
                    convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                } catch (Exception e) {
                }

                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                {
                    if (isPendingChecked) {
                        if (model.getDeliveryFlag().equals("0")) {
                            filteredModelList.add(model);
                        }
                    } else if (isDeliveredChecked) {
                        if (model.getDeliveryFlag().equals("1")) {
                            filteredModelList.add(model);
                        }
                    }
                    else if(isUndeiveredCheck)
                    {
                        if (model.getDeliveryFlag().equals("2")) {
                            filteredModelList.add(model);
                        }
                    }
                    else {
                        filteredModelList.add(model);
                    }
                }

            } else {
                if (isPendingChecked)
                {
                    if (model.getDeliveryFlag().equals("0"))
                    {
                        filteredModelList.add(model);
                    }
                }

                if (isDeliveredChecked)
                {
                    if (model.getDeliveryFlag().equals("1"))
                    {
                        filteredModelList.add(model);
                    }
                }
                if (isUndeiveredCheck)
                {
                    if (model.getDeliveryFlag().equals("2"))
                    {
                        filteredModelList.add(model);
                    }
                }
            }
        }
        fill_payment_list(filteredModelList);

    }
        else        {
            Toast.makeText(getApplicationContext(),"No Records Found",Toast.LENGTH_SHORT).show();
        }
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


    /* RecyclerView Adapter */
    public void fill_payment_list(final List<m_report_delivery_invoice_list> posts_s) {
        if (posts!=null) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
        paymentListAdapter = new ReportDeliveryInvoiceListAdapter(this, posts_s);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rec_payment.setLayoutManager(mLayoutManager);
        rec_payment.setItemAnimator(new DefaultItemAnimator());
        rec_payment.setAdapter(paymentListAdapter);
    }


    /* onClick */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Cheque_date:
                show_Date_Picker();
                break;
//            case R.id.complete_delivery:
//                //  validations();
//                Log.d("Size",""+String.valueOf(Constant.selectedDeliveryInvoice));
//                if (Constant.selectedDeliveryInvoice.size()>0)
//                {
//                    call_delivery_activity();
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Please Select invoices",Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
    }



//
//    /* Random Payment Id */
//    private void randomPaymentId() {
//        Random rand = new Random();
//        int randomId = rand.nextInt(1000) + 1;
//        paymentId = "OG" + paymentIdDate() + randomId;
//    }
//


    private void call_delivery_activity()
    {
        Intent i=new Intent(getApplicationContext(),SaveDeliveryActivity.class);
        //  i.putExtra("customer_name","NA");
        i.putExtra("chemist_name",chemistName);
        i.putExtra("chemist_id",chemistId);


        startActivity(i);
    }
    /* Current Date */
    private String currentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return sdf.format(Calendar.getInstance().getTime());
    }




    /* Date Add In Payment Id */
    private String paymentIdDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        return sdf.format(Calendar.getInstance().getTime());
    }





    /* Date Picker */
    private void show_Date_Picker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this,
                new android.app.DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        textView_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }




    /* Get SQLite Invoice List */
 /*   private void getDeliveryInvoiceListSQLite() {
        SharedPreferences share = getSharedPreferences("OGlogin", MODE_PRIVATE);
        String userId = share.getString("user_id", "");
        Log.d("userId",userId);
     //   Cursor cursor = sqLiteHandler.getDeliveryInvoiceBySalesmanId(userId);
        Cursor cursor = sqLiteHandler.getDeliveryInvoiceBySalesmanId(userId,filterflag);
        //Cursor cursor = sqLiteHandler.getDeliveryInvoiceByChemistId(chemistId);
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            empty_view.setVisibility(View.VISIBLE);
            linearLayoutMakePayment.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            linearLayoutMakePayment.setVisibility(View.VISIBLE);
            posts = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    //ErpsalesmanID   0
                    //DeliveryDate
                    //DeliveryStatus
                    //OrderID
                    //InvoiceDate
                    //BoxCount
                    //InvoiceNo
                    //TotalAmount
                    //TotalItems
                    //Package_count
                    //DeliveryId
                    String ErpsalesmanID = cursor.getString(3);
                    String DeliveryDate = cursor.getString(4);
                    String DeliveryStatus = cursor.getString(5);
                    String OrderID = cursor.getString(6);
                    String InvoiceDate = cursor.getString(7);
                    String BoxCount = cursor.getString(8);
                    String InvoiceNo = cursor.getString(9);
                    String TotalAmount = cursor.getString(10);
                    String TotalItems = cursor.getString(11);
                    String Package_count = cursor.getString(12);
                    String DeliveryId = cursor.getString(13);
                    String DeliveryFlag = cursor.getString(14);
                    String ChemistId = cursor.getString(2);
                    String DeliveryUpdatedStatus= cursor.getString(15);
                    String Deliverychemist_name= cursor.getString(16);

                  //  Log.d("prt_chemist_name11",Deliverychemist_name);

//                    try {
//                        sdf.setDateFormatSymbols(DateFormatSymbols.getInstance(Locale.ENGLISH));
//                        final String filterdate = sdf.format(Calendar.getInstance().getTime()).toString();
//
//                        SelectedDailyDeliveryList.Predicate<m_report_delivery_invoice_list> validPersonPredicate = new SelectedDailyDeliveryList.Predicate<m_report_delivery_invoice_list>() {
//
//                            Date date;
//
//                            public boolean apply(m_report_delivery_invoice_list m_order) {
//                                try {
//                                    String s = sdf.format(sdf.parse(m_order.getDeliveryDate()));
//                                    String data=m_order.getDescription();
//                                    String data1=m_order.getDeliveryDate();
//                                    Log.d("data",data);
//                                    Log.d("data11",data1);
//                                   // String s2=sdf.format(sdf.parse(m_order.getDescription()));
//                                    return s.equals(filterdate);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                return false;
//                            }
//                        };
//
//                        Collections.sort(posts, new SelectedDailyDeliveryList.CustomComparator());
//                        Collection<m_report_delivery_invoice_list> result = SelectedDailyDeliveryList.filter(posts, validPersonPredicate);
//                        m_report_delivery_invoice_list m_pendingbills = new m_report_delivery_invoice_list(ErpsalesmanID,DeliveryDate,DeliveryStatus,OrderID,InvoiceDate,BoxCount,InvoiceNo,TotalAmount,TotalItems,Package_count,DeliveryId,DeliveryFlag,ChemistId,DeliveryUpdatedStatus);
//
//                            posts.add(m_pendingbills);
//                            fill_payment_list((List) result);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
                    m_report_delivery_invoice_list m_pendingbills = new m_report_delivery_invoice_list(ErpsalesmanID,DeliveryDate,DeliveryStatus,OrderID,InvoiceDate,BoxCount,InvoiceNo,TotalAmount,TotalItems,Package_count,DeliveryId,DeliveryFlag,ChemistId,DeliveryUpdatedStatus,Deliverychemist_name);
                    posts.add(m_pendingbills);
                   // fill_payment_list((List) result);
                } while (cursor.moveToNext());
              *//*  Collections.sort(posts, new Comparator<m_report_delivery_invoice_list>() {
                    @Override
                    public int compare(m_report_delivery_invoice_list lhs, m_report_delivery_invoice_list rhs) {
                        return lhs.getInvoiceDate().compareTo(rhs.getInvoiceDate());
                    }
                });*//*
                fill_payment_list(posts);
            }
        }
    }*/

    //
    /* Get SQLite Invoice List */
    private void getDeliveryInvoiceListSQLite() {
        SharedPreferences share = getSharedPreferences("OGlogin", MODE_PRIVATE);
        String userId = share.getString("user_id", "");
        Log.d("userId",userId);
        //   Cursor cursor = sqLiteHandler.getDeliveryInvoiceBySalesmanId(userId);
        Cursor cursor = sqLiteHandler.getDeliveryInvoiceBySalesmanId(userId,filterflag);
        //Cursor cursor = sqLiteHandler.getDeliveryInvoiceByChemistId(chemistId);
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            empty_view.setVisibility(View.VISIBLE);
            linearLayoutMakePayment.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            linearLayoutMakePayment.setVisibility(View.VISIBLE);
            posts = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    //ErpsalesmanID   0
                    //DeliveryDate
                    //DeliveryStatus
                    //OrderID
                    //InvoiceDate
                    //BoxCount
                    //InvoiceNo
                    //TotalAmount
                    //TotalItems
                    //Package_count
                    //DeliveryId
                    String ErpsalesmanID = cursor.getString(3);
                    String DeliveryDate = cursor.getString(4);
                    String DeliveryStatus = cursor.getString(5);
                    String OrderID = cursor.getString(6);
                    String InvoiceDate = cursor.getString(7);
                    String BoxCount = cursor.getString(8);
                    String InvoiceNo = cursor.getString(9);
                    String TotalAmount = cursor.getString(10);
                    String TotalItems = cursor.getString(11);
                    String Package_count = cursor.getString(12);
                    String DeliveryId = cursor.getString(13);
                    String DeliveryFlag = cursor.getString(14);
                    String ChemistId = cursor.getString(2);
                    String DeliveryUpdatedStatus= cursor.getString(15);
                    String Deliverychemist_name= cursor.getString(16);

                    //  Log.d("prt_chemist_name11",Deliverychemist_name);

                    try {
                        sdf.setDateFormatSymbols(DateFormatSymbols.getInstance(Locale.ENGLISH));
                        final String filterdate = sdf.format(Calendar.getInstance().getTime()).toString();

                        SelectedDailyDeliveryList.Predicate<m_report_delivery_invoice_list> validPersonPredicate = new SelectedDailyDeliveryList.Predicate<m_report_delivery_invoice_list>() {

                            Date date;

                            public boolean apply(m_report_delivery_invoice_list m_order) {
                                try {
                                    String s = sdf.format(sdf.parse(m_order.getDeliveryDate()));
                                    String data=m_order.getDescription();
                                    String data1=m_order.getDeliveryDate();
                                    Log.d("data",data);
                                    Log.d("data11",data1);
                                    // String s2=sdf.format(sdf.parse(m_order.getDescription()));
                                    return s.equals(filterdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        };

                        Collections.sort(posts, new SelectedDailyDeliveryList.CustomComparator());
                        Collection<m_report_delivery_invoice_list> result = SelectedDailyDeliveryList.filter(posts, validPersonPredicate);
                        // m_report_delivery_invoice_list m_pendingbills = new m_report_delivery_invoice_list(ErpsalesmanID,DeliveryDate,DeliveryStatus,OrderID,InvoiceDate,BoxCount,InvoiceNo,TotalAmount,TotalItems,Package_count,DeliveryId,DeliveryFlag,ChemistId,DeliveryUpdatedStatus);
                        m_report_delivery_invoice_list m_pendingbills = new m_report_delivery_invoice_list(ErpsalesmanID,DeliveryDate,DeliveryStatus,OrderID,InvoiceDate,BoxCount,InvoiceNo,TotalAmount,TotalItems,Package_count,DeliveryId,DeliveryFlag,ChemistId,DeliveryUpdatedStatus,Deliverychemist_name); // 28 sep

                        posts.add(m_pendingbills);
                        fill_payment_list((List) result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //  m_report_delivery_invoice_list m_pendingbills = new m_report_delivery_invoice_list(ErpsalesmanID,DeliveryDate,DeliveryStatus,OrderID,InvoiceDate,BoxCount,InvoiceNo,TotalAmount,TotalItems,Package_count,DeliveryId,DeliveryFlag,ChemistId,DeliveryUpdatedStatus,Deliverychemist_name); // 28 sep
                    // posts.add(m_pendingbills);  // 28 sep
                    // fill_payment_list((List) result);
                } while (cursor.moveToNext());
          /*  Collections.sort(posts, new Comparator<m_report_delivery_invoice_list>() {
                @Override
                public int compare(m_report_delivery_invoice_list lhs, m_report_delivery_invoice_list rhs) {
                    return lhs.getInvoiceDate().compareTo(rhs.getInvoiceDate());
                }
            });*/
                //  fill_payment_list(posts); // 28 sep
            }
        }
    }
    /* Total Balance Interface */
    @Override
    public void totalBalance(int balance) {
        // textViewTotalBalance.setText("Total Balance: " + String.valueOf(balance));
    }


    public interface Predicate<T> {
        boolean apply(T type);
    }

    public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }


    /*public static class CustomComparator implements Comparator<m_report_delivery_invoice_list> {
        @Override
        public int compare(m_report_delivery_invoice_list o1, m_report_delivery_invoice_list o2) {

            return o2.getDeliveryDate().compareTo(o1.getDeliveryDate());
        }

    }*/

    public static class CustomComparator implements Comparator<m_report_delivery_invoice_list> {
        @Override
        public int compare(m_report_delivery_invoice_list o1, m_report_delivery_invoice_list o2) {


            if(!o1.getDescription().equals("") && !o2.getDescription().equals(""))

                return getFormattedDate(o2.getDescription().split(":")[0]).compareTo(getFormattedDate(o1.getDescription().split(":")[0]));
            else
                return o2.getDeliveryDate().compareTo(o1.getDeliveryDate());
// return o1.getOrderDate().compareTo(o2.getOrderDate());
        }

    }

    public static Date getFormattedDate(String time){
        DateFormat read = new SimpleDateFormat( "yyyy-MM-dd hh.mm aa");
        //DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = read.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
