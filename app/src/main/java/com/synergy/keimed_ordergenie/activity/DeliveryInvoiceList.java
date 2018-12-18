package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.DeliveryInvoiceListAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.synergy.keimed_ordergenie.model.m_delivery_invoice_list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class DeliveryInvoiceList extends AppCompatActivity
        implements View.OnClickListener,
        SelectedInvoiceDelivery, DatePickerDialog.OnDateSetListener {

    /* Find Views */
    String filterflag = "1";
    private LinearLayout linearLayoutMakePayment;
    private DeliveryInvoiceListAdapter paymentListAdapter;
    private RecyclerView rec_payment;
    private int mYear, mMonth, mDay;
    private SearchView searchView;
    private Boolean IsCallPlanTask;
    DatePickerDialog dpd_start_date, dpd_end_date;
    RadioGroup radioGroup;
    SharedPreferences pref;
    private int REQUEST_FORM = 1;
    List<m_delivery_invoice_list> posts;
    TextView txt_start_date, txt_end_date;
    private String accessKey, userId, stockistId, chemistId, chemistName, User_id, Stockist_id;
    private TextView textViewTotalBalance;
    private Date current_date = Calendar.getInstance().getTime();
    private TextView empty_view;
    TextView textView_date;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_invoice_list);

        /* Initialize Class */
        sqLiteHandler = new SQLiteHandler(this);

        linearLayoutMakePayment = (LinearLayout) findViewById(R.id.bottom_layout);
        // textViewTotalBalance = (TextView) findViewById(R.id.txt_total_balance_amount_payment);
        empty_view = (TextView) findViewById(R.id.empty_view);
        comp_names = getResources().getStringArray(R.array.Company_names_list);
        setTitle("Delivery List");
        /* Make Payment Click */
        findViewById(R.id.complete_delivery).setOnClickListener(this);

        // added by prakash
        findViewById(R.id.export_data).setOnClickListener(this);

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
        chemistId = getIntent().getStringExtra("chemist_id");
        chemistName = getIntent().getStringExtra("chemist_name");

      //  Log.d("chemistId", chemistId);
      //  Log.d("chemistName", chemistId);
        getDeliveryInvoiceListSQLite();

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

        if (paymentListAdapter != null) {
            getDeliveryInvoiceListSQLite();
            paymentListAdapter.notifyDataSetChanged();
            rec_payment.invalidate();
        }
        //   getDeliveryInvoiceListSQLite();
    }


    /* onCreate Option Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delivery_invoice, menu);
        searchView = (SearchView) menu.findItem(R.id.action_invoicesearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<m_delivery_invoice_list> mList = new ArrayList<>();

                for(m_delivery_invoice_list modelinvoiceList : posts){

                    final String pInvoiceno = modelinvoiceList.getInvoiceNo().toLowerCase();

                    if(pInvoiceno.contains(newText)){

                        mList.add(modelinvoiceList);

                    }

                }

                fill_payment_list(mList);

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
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
        final View dialogview = inflater.inflate(R.layout.dialog_delivery_invoice_list_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        txt_start_date = (TextView) dialogview.findViewById(R.id.txt_start_date);
        txt_end_date = (TextView) dialogview.findViewById(R.id.txt_end_date);
        final CheckBox chk_pending = (CheckBox) dialogview.findViewById(R.id.chk_pending);
        final CheckBox chk_delivered = (CheckBox) dialogview.findViewById(R.id.chk_completed);

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
                  //  Log.d("date12", txt_start_date.getText().toString());
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
                        DeliveryInvoiceList.this,
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
                        DeliveryInvoiceList.this,
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

                filter_dialog_conditions(filter_start_date, filter_end_date, chk_pending.isChecked(), chk_delivered.isChecked());
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

    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isPendingChecked, Boolean isDeliveredChecked) {

        Date convertedDate = null;

        final List<m_delivery_invoice_list> filteredModelList = new ArrayList<>();
        if (posts != null) {
            for (m_delivery_invoice_list model : posts) {
              //  Log.d("Status12", model.getDeliveryFlag());
                if (startDate != null && endDate != null) {
                    final String date = model.getDeliveryDate();
                    // final String date = model.getInvoiceDate();
                    try {
                        convertedDate = dateFormat.parse(date);
                        String c_date = sdf.format(convertedDate);
                        convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                     //   Log.d("date123", convertedDate.toString());
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
                        } else {
                            filteredModelList.add(model);
                        }
                    }

                } else {
                    if (isPendingChecked) {
                        if (model.getDeliveryFlag().equals("0")) {
                            filteredModelList.add(model);
                        }
                    }

                    if (isDeliveredChecked) {
                        if (model.getDeliveryFlag().equals("1")) {
                            filteredModelList.add(model);
                        }
                    }
                }
            }
            fill_payment_list(filteredModelList);
        } else {
            Toast.makeText(getApplicationContext(), "No Records Found", Toast.LENGTH_SHORT).show();
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
    public void fill_payment_list(final List<m_delivery_invoice_list> posts_s) {

        if (posts != null) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
        paymentListAdapter = new DeliveryInvoiceListAdapter(this, posts_s);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rec_payment.setLayoutManager(mLayoutManager);
        rec_payment.setItemAnimator(new DefaultItemAnimator());
        rec_payment.setAdapter(paymentListAdapter);
    }

    protected void exportDb() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File dataDirectory = Environment.getDataDirectory();

        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = "/data/" + getApplicationContext().getApplicationInfo().packageName + "/databases/SampleDB";
        String backupDBPath = "SampleDB.sqlite";
        File currentDB = new File(dataDirectory, currentDBPath);
        File backupDB = new File(dataDirectory, backupDBPath);
        //File backupDB = new File(externalStorageDirectory, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());

            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (source != null) source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (destination != null) destination.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /* onClick */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.export_data:
                exportDb();

            case R.id.Cheque_date:
                show_Date_Picker();
                break;
            case R.id.complete_delivery:
                //  validations();
             //   Log.d("Size", "" + String.valueOf(Constant.selectedDeliveryInvoice));
                if (Constant.selectedDeliveryInvoice.size() > 0) {
                    call_delivery_activity();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select invoices", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void call_delivery_activity() {
        Intent i = new Intent(getApplicationContext(), SaveDeliveryActivity.class);
        //  i.putExtra("customer_name","NA");
        i.putExtra("chemist_name", chemistName);
        i.putExtra("chemist_id", chemistId);
        startActivityForResult(i, REQUEST_FORM);
        // startActivity(i);
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
    private void getDeliveryInvoiceListSQLite() {
        Cursor cursor = sqLiteHandler.getDeliveryInvoiceByChemistId(chemistId, filterflag);
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            empty_view.setVisibility(View.VISIBLE);
            linearLayoutMakePayment.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            linearLayoutMakePayment.setVisibility(View.VISIBLE);
            posts = new ArrayList<>();
                while (cursor.moveToNext()) {

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


                    m_delivery_invoice_list m_pendingbills =
                            new m_delivery_invoice_list(ErpsalesmanID, DeliveryDate,
                                    DeliveryStatus, OrderID, InvoiceDate, BoxCount, InvoiceNo,
                                    TotalAmount, TotalItems, Package_count, DeliveryId,
                                    DeliveryFlag);
                    posts.add(m_pendingbills);
                 //   Log.d("posts", posts.toString());

                    if (cursorCount == posts.size()) {
                        fill_payment_list(posts);
                    }
                }
          //  }
        }
    }

    // Added by prakash
    private void getDeliveryInvoiceListSQLite_resume(String filterflag) {
        Cursor cursor = sqLiteHandler.getDeliveryInvoiceByChemistId(chemistId, filterflag);
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


                    m_delivery_invoice_list m_pendingbills =
                            new m_delivery_invoice_list(ErpsalesmanID, DeliveryDate,
                                    DeliveryStatus, OrderID, InvoiceDate,
                                    BoxCount, InvoiceNo,
                                    TotalAmount, TotalItems,
                                    Package_count, DeliveryId,
                                    DeliveryFlag);
                    posts.add(m_pendingbills);
                //    Log.d("posts", posts.toString());

                    if (cursorCount == posts.size()) {
                        fill_payment_list(posts);
                    }
                } while (cursor.moveToNext());
            }
        }
    }


    //
    /* Total Balance Interface */
    @Override
    public void totalBalance(int balance) {
        // textViewTotalBalance.setText("Total Balance: " + String.valueOf(balance));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("strEditText", "apurva");
//        if (requestCode == 1) {
//            if(resultCode == RESULT_OK)
//            {
//                String strEditText = data.getStringExtra("Save");
//                Log.d("strEditText",strEditText);
//                if (strEditText.equals("SAVE_DELIVERY"))
//                {
//                    if (paymentListAdapter!=null)
//                    {
//
//                        getDeliveryInvoiceListSQLite();
//                        paymentListAdapter.notifyDataSetChanged();
//                        rec_payment.invalidate();
//                    }
//                }
//          //  }
        if (requestCode == REQUEST_FORM && resultCode == RESULT_OK) {
            Log.d("strEditText", "apurva");
            String strEditText = data.getStringExtra("Save");
         //   Log.d("strEditText", strEditText);
            if (strEditText.equals("SAVE_DELIVERY")) {
                if (paymentListAdapter != null) {

                    getDeliveryInvoiceListSQLite();
                    paymentListAdapter.notifyDataSetChanged();
                    rec_payment.invalidate();
                }
            }

    }
}


}
