package com.synergy.keimed_ordergenie.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.PaymentGetSet;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import static com.synergy.keimed_ordergenie.R.id.Cheque_date;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class Payment_Option extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess, View.OnClickListener {

    /*   private SearchView searchView;
       private Menu oMenu;*/
    private Boolean IsPaymentoptionSelected = false;
    private Boolean IsChequeSelected = false;

    String chemist_Id;
    StringBuffer date_buff;
    //private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;


    private int mYear, mMonth, mDay;
    ResultReceiver resultReceiver;
    private SharedPreferences pref;
    private Integer payment_id;

    LinearLayout PartPaymentOption;
    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.lblinvoiceid)
    TextView lblinvoiceid;

    @BindView(R.id.invoiceId)
    TextView invoiceId;

    @BindView(R.id.lblorderDate)
    TextView _lblorderDate;

    @BindView(R.id.orderDate)
    TextView _orderDate;

    @BindView(R.id.lblTotalItem)
    TextView _lblTotalItem;

    @BindView(R.id.TotalItem)
    TextView _TotalItem;

    @BindView(R.id.lblInvAmt)
    TextView _lblInvAmt;

    @BindView(R.id.InvAmt)
    TextView _InvAmt;

    @BindView(R.id.lblBalAmt)
    TextView _lblBalAmt;

    @BindView(R.id.BalAmt)
    TextView _BalAmt;

    @BindView(R.id.checkPartpay)
    CheckBox _checkPartpay;

    @BindView(R.id.note)
    TextView _note;

    @BindView(R.id.desireAmt)
    EditText _desireAmt;

    @BindView(R.id.spinner)
    Spinner _SelectPayOption;

    @BindView(R.id.ComPay)
    Button _ComPay;


    @BindView(Cheque_date)
    TextView _Cheque_date;

    @BindView(R.id.Cheque_number)
    EditText _Cheque_number;

    @BindView(R.id.micr_number)
    EditText _micr_number;


    @BindView(R.id.account_number)
    EditText _account_number;

    @BindView(R.id.bank_name)
    AutoCompleteTextView bank_name;

    String[] comp_names;

    private Date current_date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    // private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    String chemist_id;

    @OnItemSelected(R.id.spinner)
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }



    /* Get Intent Strings */
    private String invoiceNo = "";
    private String invoiceDate = "";
    private String totalItems = "";
    private String totalAmount = "";
    private String balanceAmount = "";
    private String chemistId = "";


    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private ArrayList<PaymentGetSet> arrayListPaymentSQLite;
    private PaymentGetSet paymentGetSet;
    private int exportPosition = 0;
    private JSONObject jsonObjectPayment;
    private int postSQLitePayment = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__option);

        ButterKnife.bind(this);
        SharedPreferences prefs = getSharedPreferences("MY PREF", MODE_PRIVATE);
        chemist_id = prefs.getString("chemist_id", null);
        // Log.d("printpaymentId",chemist_id);

        comp_names = getResources().getStringArray(R.array.Company_names_list);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Payment Details");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        ArrayAdapter<String> compny_names_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comp_names);
        bank_name.setThreshold(1);
        bank_name.setAdapter(compny_names_adapter);

        PartPaymentOption = (LinearLayout) findViewById(R.id.PartPayment);
        PartPaymentOption.setVisibility(View.GONE);
        resultReceiver = getIntent().getParcelableExtra("receiver");

        invoiceId.setText(getIntent().getStringExtra("Invoiceno"));


        _Cheque_date.setOnClickListener(this);

        bank_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bank_name.showDropDown();
                }
            }
        });

        bank_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(bank_name.getWindowToken(), 0);
            }
        });


        try {
            String date_Length = sdf.format(dateFormat.parse(getIntent().getStringExtra("Invoicedate")));
           /* _orderDate.setText(sdf.format(dateFormat.parse(getIntent().getStringExtra("Invoicedate"))));*/
            date_buff = new StringBuffer(date_Length);
            date_buff.setLength(10);
            //  Log.d("date_length", String.valueOf(date_buff));
            _orderDate.setText(date_buff);

        } catch (Exception e) {

        }

        _TotalItem.setText("" + getIntent().getIntExtra("Totalitems", 0));
        _InvAmt.setText("Rs. " + getIntent().getStringExtra("Billamount"));
        _BalAmt.setText("" + getIntent().getStringExtra("Balanceamt"));

        if (Integer.parseInt(getIntent().getStringExtra("Billamount")) >= 500) {
            PartPaymentOption.setVisibility(View.VISIBLE);
        }
        _checkPartpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    _desireAmt.setVisibility(View.VISIBLE);
                } else {
                    _desireAmt.setVisibility(View.GONE);
                }
            }
        });

        _SelectPayOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    IsPaymentoptionSelected = false;
                    _Cheque_date.setVisibility(View.GONE);
                    _Cheque_number.setVisibility(View.GONE);
                    _account_number.setVisibility(View.GONE);
                    bank_name.setVisibility(View.GONE);
                    _micr_number.setVisibility(View.GONE);
                } else {
                    IsPaymentoptionSelected = true;
                    _Cheque_date.setVisibility(View.GONE);
                    _Cheque_number.setVisibility(View.GONE);
                    _account_number.setVisibility(View.GONE);
                    bank_name.setVisibility(View.GONE);
                    _micr_number.setVisibility(View.GONE);
                }
                if (position == 2) {
                    _Cheque_date.setVisibility(View.VISIBLE);
                    _Cheque_number.setVisibility(View.VISIBLE);
                    /*comment Account num visiblity gone not need for check payment for said Rashid sir*/
                    _account_number.setVisibility(View.GONE);
                    bank_name.setVisibility(View.VISIBLE);
                    _micr_number.setVisibility(View.VISIBLE);

                    payment_id = 3;
                    IsChequeSelected = true;
                } else {
                    payment_id = 2;
                    IsChequeSelected = false;
                }

                //Toast.makeText(Payment_Option.this, ""+payment_id+"\n"+IsChequeSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        /* Get Intent Strings */
        invoiceNo = getIntent().getStringExtra("Invoiceno");
        invoiceDate = getIntent().getStringExtra("Invoicedate");
        totalItems = String.valueOf(getIntent().getIntExtra("Totalitems", 0));
        totalAmount = getIntent().getStringExtra("Billamount");
        balanceAmount = getIntent().getStringExtra("Balanceamt");
        chemistId = getIntent().getStringExtra("chemist_id");


        /* Initialize SQLite Database */
        sqLiteHandler = new SQLiteHandler(this);
        //uploadSQLiteData();

        /* Check Internet Connectivity */
        new checkInternetConn().execute(0);
    }


    /* Complete Payment Click */
    @OnClick(R.id.ComPay)
    void onclick_submit() {
        if (Validate()) {
            save_payment();
        }
    }



    /* Validations */
    Boolean Validate() {
        Boolean success = true;
        if (_checkPartpay.isChecked()) {
            if (_desireAmt.getText().toString().isEmpty()) {
                success = false;
                OGtoast.OGtoast("Please enter the amount for part payment", Payment_Option.this);
            }
        }
        if (!IsPaymentoptionSelected) {
            success = false;
            OGtoast.OGtoast("Please select a payment option", Payment_Option.this);
        }
        if (IsChequeSelected) {
            if (_Cheque_date.getText().toString().isEmpty()) {
                success = false;
                OGtoast.OGtoast("Please enter Cheque date", Payment_Option.this);
            } else if (_Cheque_number.getText().toString().isEmpty()) {
                success = false;
                OGtoast.OGtoast("Please enter Cheque Number", Payment_Option.this);
            } /*else if (_account_number.getText().toString().isEmpty()) {
                success = false;
                OGtoast.OGtoast("Please enter Account Number", Payment_Option.this);
            } */ else if (bank_name.getText().toString().isEmpty()) {
                success = false;
                OGtoast.OGtoast("Please enter a Bank Name", Payment_Option.this);
            }/*else if (_micr_number.getText().toString().isEmpty()) {
                success = false;
                OGtoast.OGtoast("Please enter MICR Number", Payment_Option.this);
            }*/
           /* if(_account_number.getText().toString().isEmpty())
            {
                success=false;
                OGtoast.OGtoast("Please enter Account Number",Payment_Option.this);
            }*/
        }
        return success;
    }





    /* Option Item Selected */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_payment_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    /* Save Payment */
    void save_payment() {
        try {
            String amount = "";
            JSONObject j_obj = new JSONObject();
            String Narration_Text = "";

            j_obj.put("Invoice_No", invoiceId.getText().toString());
            j_obj.put("Doc_No", "");
            j_obj.put("Doc_Date", dateFormat.format(Calendar.getInstance().getTime()));
            j_obj.put("PaymodeId", payment_id);
            j_obj.put("PaymodeDesc", _SelectPayOption.getSelectedItem().toString());

            if (_checkPartpay.isChecked()) {
                amount = _desireAmt.getText().toString();
            } else {
                amount = _BalAmt.getText().toString();
            }


            if (IsChequeSelected) {
                j_obj.put("Chq_No", _Cheque_number.getText().toString());
                j_obj.put("Chq_Date", _Cheque_date.getText().toString());
                j_obj.put("BankAccountID", _account_number.getText().toString());
                j_obj.put("Bank_Id", "");
                j_obj.put("BankName", bank_name.getText().toString());

                Narration_Text = " Cheque Payment- Ref No- " + invoiceId.getText().toString() + " and Cheque No " + " Dated - " + sdf.format(Calendar.getInstance().getTime())
                        + " of RS." + amount + "";

            } else {
                j_obj.put("Chq_No", "");
                j_obj.put("Chq_Date", "");
                j_obj.put("BankAccountID", "0");
                j_obj.put("Bank_Id", "");
                j_obj.put("BankName", "");

                Narration_Text = "Cash Payment- Ref No- " + invoiceId.getText().toString() + " Dated - " + sdf.format(Calendar.getInstance().getTime())
                        + " of RS." + amount + "";
            }

            j_obj.put("Amount", amount);
            j_obj.put("Narration", Narration_Text);

            Random rand = new Random();
            int randomNumber = rand.nextInt(1000) + 1;
            //   Log.d("randomnumer", String.valueOf(randomNumber));
            j_obj.put("PaymentID", randomNumber);
            j_obj.put("Stockist_Client_ID", pref.getString(CLIENT_ID, "0"));

            j_obj.put("Chemist_ID", chemist_id);

            //Log.d("savePayment3", j_obj.toString());

            //MakeWebRequest.MakeWebRequest("Post", AppConfig.SAVE_PAYMENT, AppConfig.SAVE_PAYMENT, j_obj, this, true);

            if (Utility.internetCheck(this)) {
                MakeWebRequest.MakeWebRequest("Post", AppConfig.SAVE_PAYMENT, AppConfig.SAVE_PAYMENT, j_obj, this, true);
            } else {
                /* Insert In SQLite */
                if (Integer.valueOf(totalAmount) >= 500) {
                    if (Integer.valueOf(amount) > Integer.valueOf(totalAmount)) {
                        Toast.makeText(this, "Paid amount more than Bill Amount", Toast.LENGTH_SHORT).show();
                    } else {
                        int balanceAmt = Integer.valueOf(totalAmount) - Integer.valueOf(amount);
                        balanceAmountDialog(totalAmount, amount, balanceAmt, randomNumber, Narration_Text);
                    }
                } else {
                    /*if (IsChequeSelected) {
                        sqLiteHandler.insertPaymentCollection(pref.getString(CLIENT_ID, "0"), chemistId, invoiceNo, invoiceDate,
                                dateFormat.format(Calendar.getInstance().getTime()), totalItems, amount, totalAmount, balanceAmount,
                                String.valueOf(payment_id), _SelectPayOption.getSelectedItem().toString(), _Cheque_date.getText().toString(),
                                _Cheque_number.getText().toString(), "", bank_name.getText().toString(), String.valueOf(randomNumber),
                                Narration_Text,"4", pref.getString(CLIENT_ID, "0"), "");
                    } else {
                        sqLiteHandler.insertPaymentCollection(pref.getString(CLIENT_ID, "0"), chemistId, invoiceNo, invoiceDate,
                                dateFormat.format(Calendar.getInstance().getTime()), totalItems, amount, totalAmount, balanceAmount,
                                String.valueOf(payment_id), _SelectPayOption.getSelectedItem().toString(), "","", "",
                                "", String.valueOf(randomNumber), Narration_Text,"4", pref.getString(CLIENT_ID, "0"),
                                "");
                    }*/
                    paymentMadeSuccessDialog(amount, Integer.valueOf(balanceAmount));
                }
            }

        } catch (Exception e) {
            e.toString();
        }
    }



    /* Balance Dialog */
    private void balanceAmountDialog(final String invoiceAmt, final String paidAmt, final int balanceAmt, final int randomNo,
                                     final String narrationText) {
        new AlertDialog.Builder(this).setTitle("Total Amt: " + invoiceAmt)
                .setMessage("Paid Amount: " + paidAmt + "\nBalance Amount: " + balanceAmt)
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        /*if (IsChequeSelected) {
                            sqLiteHandler.insertPaymentCollection(pref.getString(CLIENT_ID, "0"), chemistId, invoiceNo, invoiceDate,
                                    dateFormat.format(Calendar.getInstance().getTime()), totalItems, paidAmt, invoiceAmt, String.valueOf(balanceAmt),
                                    String.valueOf(payment_id), _SelectPayOption.getSelectedItem().toString(), _Cheque_date.getText().toString(),
                                    _Cheque_number.getText().toString(), "", bank_name.getText().toString(), String.valueOf(randomNo),
                                    narrationText,"4", pref.getString(CLIENT_ID, "0"), "");
                        } else {
                            sqLiteHandler.insertPaymentCollection(pref.getString(CLIENT_ID, "0"), chemistId, invoiceNo, invoiceDate,
                                    dateFormat.format(Calendar.getInstance().getTime()), totalItems, paidAmt, invoiceAmt, String.valueOf(balanceAmt),
                                    String.valueOf(payment_id), _SelectPayOption.getSelectedItem().toString(), "","", "",
                                    "", String.valueOf(randomNo), narrationText,"4", pref.getString(CLIENT_ID, "0"),
                                    "");
                        }*/
                        paymentMadeSuccessDialog(paidAmt, balanceAmt);
                    }
                }).show();
    }




    /* Payment Made Successfully Dialog */
    private void paymentMadeSuccessDialog(String paidAmount, int balanceAmount) {
        //sqLiteHandler.updateSingleInvoice(invoiceNo, paidAmount, String.valueOf(balanceAmount));
        new AlertDialog.Builder(Payment_Option.this)
                .setTitle("Payment")
                .setCancelable(false)
                .setMessage("Payment made successfully.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        /*Bundle bundle = new Bundle();
                        bundle.putString("Payment", "Done");
                        if (resultReceiver != null) {
                            resultReceiver.send(300, bundle);
                        }*/
                        finish();
                    }
                }).show();
    }





    /* API Response */
    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        // Log.d("RESPONCEPAYMENT11", f_name);
        // Log.d("RESPONCEPAYMENT12", String.valueOf(response));
        if (response != null) {

            if (f_name.equals(AppConfig.SAVE_ORDER_INVOICE)) {
                if (postSQLitePayment == 1) {
                    sqLiteHandler.deletePaymentRecord(paymentGetSet.getInvoiceNo());
                    exportPosition = exportPosition + 1;
                    if (arrayListPaymentSQLite.size() != exportPosition) {
                        addDataInJsonFormat();
                    }
                } else {
                    new AlertDialog.Builder(Payment_Option.this)
                            .setTitle("Payment")
                            .setCancelable(false)
                            .setMessage("Payment made successfully.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("Payment", "Done");
                                    if (resultReceiver != null) {
                                        resultReceiver.send(300, bundle);
                                    }

                                    finish();
                                }
                            }).show();
                }
            }
        }
    }





    /* Save Response */
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        try {
            if (response != null) {
                //Log.e("response", response.toString());
                if (f_name.equals(AppConfig.SAVE_PAYMENT)) {
                    if (postSQLitePayment == 1) {
                        updateStatusPayment();
                    } else {
                        save_invoice(invoiceId.getText().toString(), "4");
                    }
                }
            }
        } catch (Exception e) {
            e.toString();
        }

    }





    /* Save Invoice */
    void save_invoice(String Invoice_id, String status) {
        try {
            JSONObject main_j_obj = new JSONObject();
            JSONArray j_array = new JSONArray();

            JSONObject j_obj = new JSONObject();

            j_obj.put("InvoiceNo", Invoice_id);
            j_obj.put("StockistID", pref.getString(CLIENT_ID, "0"));
            j_obj.put("Status", status);

            j_array.put(j_obj);

            main_j_obj.put("invoices", j_array);

            Log.d("SavedInvoice", j_array.toString());

            // MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_CHEMIST_CONFIRM_ORDER, AppConfig.POST_CHEMIST_CONFIRM_ORDER,
            //       new JSONArray().put(main_j_obj), this,false,"");


            MakeWebRequest.MakeWebRequest("out_array", AppConfig.SAVE_ORDER_INVOICE, AppConfig.SAVE_ORDER_INVOICE,
                    null, this, true, main_j_obj.toString());

            //  MakeWebRequest.MakeWebRequest("Post", AppConfig.SAVE_ORDER_INVOICE, AppConfig.SAVE_ORDER_INVOICE, main_j_obj, this, true);

        } catch (Exception e) {

        }

    }






    /* DatePicker Click */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Cheque_date:
                show_Date_Picker();
                break;
        }
    }





    /* Show DatePicker */
    private void show_Date_Picker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this,
                new android.app.DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        _Cheque_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }







    /* Upload SQLite Data To Server */
    /*private void uploadSQLiteData() {
        arrayListPaymentSQLite = new ArrayList<>();
        Cursor cursor = sqLiteHandler.getPaymentRecord(chemistId);
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
        }
        //Log.d("updatedInvoiceStatus", jsonObject.toString());
        if (Utility.internetCheck(this)) {
            MakeWebRequest.MakeWebRequest("out_array", AppConfig.SAVE_ORDER_INVOICE, AppConfig.SAVE_ORDER_INVOICE,
                    null, this, false, jsonObject.toString());
        }
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
                //getSavedDelivery();
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
        /**
         * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
         */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                //  Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }
    }

}
