package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.synergy.keimed_ordergenie.BuildConfig;
import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.adapter.MakePaymentListAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.interfaces.SelectedInvoice;
import com.synergy.keimed_ordergenie.model.m_pendingbills;
import com.synergy.keimed_ordergenie.utils.LocationActivity;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CHEMIST_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class SalesmanMakePayment extends AppCompatActivity implements LocationListener, View.OnClickListener, MakeWebRequest.OnResponseSuccess,
        SelectedInvoice {
    List<String> list = new ArrayList<String>();
    List<String> list1 = new ArrayList<String>();
    String invoicejoined, invoicejoinedoffline;
    /* Find Views */
    SearchView searchView;
    private LinearLayout linearLayoutMakePayment;
    private MakePaymentListAdapter paymentListAdapter;
    private RecyclerView rec_payment;
    private int mYear, mMonth, mDay;
    private Boolean IsCallPlanTask;
    RadioGroup radioGroup;
    SharedPreferences pref;
    private String invoice_flag = "0";
    List<m_pendingbills> posts;
    String selfie_url = "";
    String paymentsId;
    Bitmap bitmap1;
    TextView txt_start_date, txt_end_date, txt_delivery;
    float dayNum;
    private LocationManager locationManager;
    private String locationProvider;
    private double currentLocLat = 0;
    private double currentLocLong = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "OG_PaymentSelfie";
    private Uri fileUri;
    float num, numDivide;
    private String accessKey, userId, stockistId, chemistId, chemist_Name, getChemistId;
    private TextView textViewTotalBalance;

    private TextView empty_view;
    RadioButton radiobutton_cash, radiobutton_check;
    EditText textView_date;
    TextView edit_amount, text_average;
    EditText editText_Cheque_number, editText_micrNumber, editText_accnumber;
    AutoCompleteTextView autoCompleteTextView;
    //private EditText editTextAmount;
    String chemist_1;

    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private static final int PERMISSION_REQUEST_CODE = 200;

    /* Save Payment */
    private String paymentModeId = "1";
    private String paymentMode = "Cash";
    private String chequeDate = "";
    private String chequeNo = "";
    private String accountNo = "";
    private String bankId = "";
    private String bankName = "";
    private String micrNo = "";
    private String paymentId = "";       // Some Random Numbers
    private String narration = "";
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
    Button btn_captureImage;
    ArrayList<String> dateArray;
    ArrayList<Float> sumOfArrayList;
    String date2;
    String Remarks="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesman_make_payment);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        /* Initialize Class */
        sqLiteHandler = new SQLiteHandler(this);

        linearLayoutMakePayment = (LinearLayout) findViewById(R.id.bottom_layout);
        textViewTotalBalance = (TextView) findViewById(R.id.txt_total_balance_amount_payment);
        textView_date = (EditText) findViewById(R.id.Cheque_date);
        edit_amount = (TextView) findViewById(R.id.txt_total_balance_edit);
        editText_Cheque_number = (EditText) findViewById(R.id.Cheque_number);
        editText_micrNumber = (EditText) findViewById(R.id.micr_number);
        editText_accnumber = (EditText) findViewById(R.id.account_number);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.bank_name);
        text_average = (TextView) findViewById(R.id.Cheque_average);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);=
        radioGroup = (RadioGroup) findViewById(R.id.radio_payment);
        radiobutton_cash = (RadioButton) findViewById(R.id.radiobutton_cash);
        radiobutton_check = (RadioButton) findViewById(R.id.radiobutton_check);
        empty_view = (TextView) findViewById(R.id.empty_view);
        // txt_balanceamt = (TextView) findViewById(R.id.txt_balance);
        comp_names = getResources().getStringArray(R.array.Company_names_list);

        /* Make Payment Click */
        findViewById(R.id.complete_payment).setOnClickListener(this);

        textViewTotalBalance.setText("Total Balance: " + Constant.balanceAmount);

        /* Date Picker Click */
        textView_date.setOnClickListener(this);
        edit_amount.setOnClickListener(this);
        ArrayAdapter<String> compny_names_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comp_names);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(compny_names_adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rec_payment = (RecyclerView) findViewById(R.id.recycler_payment);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rec_payment.getContext(), DividerItemDecoration.VERTICAL);
        rec_payment.addItemDecoration(dividerItemDecoration);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        IsCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);

        accessKey = pref.getString("key", "");
        userId = pref.getString(USER_ID, "0");
        stockistId = pref.getString(CLIENT_ID, "0");
        chemistId = getIntent().getStringExtra("chemist_id");
        chemist_Name = getIntent().getStringExtra("chemistName");
        // Log.d("chemist", chemist_Name);
        btn_captureImage = (Button) findViewById(R.id.capture_image);
        getChemistId = getIntent().getStringExtra(CHEMIST_ID);
        setTitle(chemist_Name);

        /* Radio Buttons */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radiobutton_cash.isChecked()) {
                    paymentModeId = "1";
                    paymentMode = "Cash";
                    textView_date.setVisibility(View.GONE);
                    text_average.setVisibility(View.GONE);
                    editText_Cheque_number.setVisibility(View.GONE);
                    editText_micrNumber.setVisibility(View.GONE);
                    autoCompleteTextView.setVisibility(View.GONE);
                    btn_captureImage.setVisibility(View.GONE);
                } else if (radiobutton_check.isChecked()) {
                    paymentModeId = "2";
                    paymentMode = "Cheque";
                    textView_date.setVisibility(View.VISIBLE);
                    text_average.setVisibility(View.VISIBLE);
                    editText_Cheque_number.setVisibility(View.VISIBLE);
                    editText_micrNumber.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                    btn_captureImage.setVisibility(View.VISIBLE);
                }
            }
        });

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoCompleteTextView.showDropDown();
                }
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
            }
        });

        btn_captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//Log.d("click_captureImage","btn onclick image");

                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
// will close the app if the device does't have camera
                    finish();
                }
                if (checkPermission()) {
                    captureImage();
                } else {
                    requestPermission();
                }
            }
        });

        getInvoiceListSQLite();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[2] == PackageManager.PERMISSION_GRANTED;


                    if (cameraPermission && readExternalFile && writeExternalFile) {
                        // write your logic here
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                showMessageOKCancel("You need to allow access permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermission();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SalesmanMakePayment.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
// this device has a camera
            return true;
        } else {
// no camera on this device
            return false;
        }
    }

    private void captureImage() {
// Requesting camera app to capture image using ACTION_IMAGE_CAPTURE
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//Specifying a path where the image has to be stored using EXTRA_OUTPUT
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

// start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        //return Uri.fromFile(getOutputMediaFile(type));
        return FileProvider.getUriForFile(SalesmanMakePayment.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

// External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

// Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
// Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

// Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }




    /* Edit Text Change Listener */
    /*private void editTextChangeListener() {
        editTextAmount.getText().clear();
        Constant.enteredAmount = 0;
        Constant.balanceAmount = 0;
        Constant.ableToSelectInvoice = 0;
        textViewTotalBalance.setText("Total Balance: 0");

        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    Constant.enteredAmount = Integer.valueOf(editTextAmount.getText().toString());
                    Constant.balanceAmount = Constant.enteredAmount;
                    Constant.ableToSelectInvoice = Constant.balanceAmount;
                    textViewTotalBalance.setText("Total Balance: " + editTextAmount.getText().toString());
                } else {
                    textViewTotalBalance.setText("Total Balance: 0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }*/


    /* onResume */
    @Override
    public void onResume() {
        super.onResume();

    }


    /* onCreate Option Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customerlist, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<m_pendingbills> nList = new ArrayList<>();

                for (m_pendingbills model : posts) {

                    //  String customerName = model.getClient_Name().toLowerCase();
                    String invoice_no = model.getInvoiceno().toLowerCase();
                    if (invoice_no.contains(newText)) {

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* RecyclerView Adapter */
    public void fill_payment_list(final List<m_pendingbills> posts_s) {
        if (posts.size() > 0) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
        dateArray = new ArrayList<>();
        sumOfArrayList = new ArrayList<>();

        //   paymentListAdapter = new MakePaymentListAdapter(this, posts_s);
        paymentListAdapter = new MakePaymentListAdapter(this, posts_s, text_average);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rec_payment.setLayoutManager(mLayoutManager);
        rec_payment.setItemAnimator(new DefaultItemAnimator());
        rec_payment.setAdapter(paymentListAdapter);
        rec_payment.setNestedScrollingEnabled(false);
    }


    /* onClick */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Cheque_date:
                if (Constant.selectedInvoice.size() == 0) {
                    Toast.makeText(this, "Select invoice first", Toast.LENGTH_SHORT).show();
                } else {
                    show_Date_Picker();
                }
                break;
            case R.id.complete_payment:
                if (get_location()) {
                    validations();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enable GPS Location", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.Cheque_number:
                Log.d("click_paymentDone", "click to Payment cheque no");
                if (Constant.selectedInvoice.size() == 0) {
                    Toast.makeText(this, "Select invoice first", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txt_total_balance_edit:
                Log.d("edit ", "click edit balance");
                if (Constant.selectedInvoice.size() == 0) {
                    amountDialog();

                } else {
                    Toast.makeText(getApplicationContext(), "Can't Edit Balance", Toast.LENGTH_SHORT).show();
                    showAlert();
                }

                break;

            case R.id.micr_number:

                Log.d("click_paymentDone", "click to Payment MICR no");
                if (Constant.selectedInvoice.size() == 0) {
                    Toast.makeText(this, "Select invoice first", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showAlert() {

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        alertDialog.setTitle("Balance");

        alertDialog.setMessage("Selected invoices will be cleared. Are you sure want to continue and Edit??");

        alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                amountDialog();
//             Constant.enteredAmount = 0;
//             Constant.balanceAmount = 0;
//             Constant.ableToSelectInvoice = 0;
//             for(int i = 0; i<posts.size();i++)
//             {
//                 m_pendingbills m_pend= posts.get(i);
//                 if(m_pend.isSelected())
//                 {
//                     Log.d("position11",String.valueOf(m_pend.isSelected()));
//                     Log.d("position12",String.valueOf(m_pend.getBalanceamt()));
//                     // dateArray.add(m_pend.getInvoicedate());btn
//                     m_pend.setSelected(false);
//                     Constant.selectedInvoice.remove(posts.get(i));
//                    paymentListAdapter.notifyDataSetChanged();
//                 }
//             }
            }
        });


        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //OGtoast.OGtoast("Location Services not enabled !. Unable to get the location", getApplicationContext());
                dialog.cancel();
            }
        });
        alertDialog.show();


    }

    public boolean validate() {

        if (textView_date.getText().toString().length() == 0) {
            textView_date.setError("Please Fill Cheque Date");
            textView_date.requestFocus();
            return false;
        }


        if (editText_Cheque_number.getText().toString().length() == 0) {
            editText_Cheque_number.setError("Please Fill Cheque number");
            editText_Cheque_number.requestFocus();
            return false;
        }


        if (autoCompleteTextView.getText().toString().length() == 0) {
            autoCompleteTextView.setError("Please Fill Bank Name");
            autoCompleteTextView.requestFocus();
            return false;
        }

        return true;
    }


    /* Validations */
    private void validations() {
        if (Constant.selectedInvoice.size() == 0) {
            Toast.makeText(this, "Select invoice first", Toast.LENGTH_SHORT).show();
        } else {

            randomPaymentId();
            if (paymentModeId.equalsIgnoreCase("2")) {
                if (validate()) {
                    chequeDate = textView_date.getText().toString().trim();
                    chequeNo = editText_Cheque_number.getText().toString().trim();
                    accountNo = editText_accnumber.getText().toString().trim();
                    bankName = autoCompleteTextView.getText().toString().trim();
                    micrNo = editText_micrNumber.getText().toString().trim();
                    narration = "Payment make on " + currentDate() + " with amount " + Constant.enteredAmount;
//Toast.makeText(getApplicationContext(),"Complete"+autoCompleteTextView.getText().toString(),Toast.LENGTH_LONG).show();
                    RemarkDialog();
                    /*if (Utility.internetCheck(this)) {
                        savePaymentOnline();
                    } else {
                        savePaymentOffline();
                    }*/
                }
            } else if (paymentModeId.equalsIgnoreCase("1")) {
                narration = "Payment make on " + currentDate() + " with amount " + Constant.enteredAmount;

                RemarkDialog();
                /*if (Utility.internetCheck(this)) {
                    savePaymentOnline();
                } else {
                    savePaymentOffline();
                }*/
            }
        }

    }


    /* Random Payment Id */
    private void randomPaymentId() {
        Random rand = new Random();
        int randomId = rand.nextInt(1000) + 1;
        paymentId = "OG" + paymentIdDate() + randomId;
    }


    /* Current Date */
    private String currentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.US);
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
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String date1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        String dateString = sdf.format(calendar.getTime());
                        chequeDate = dateString;
                        textView_date.setError(null);
                        textView_date.setText(date1);
                        dateArray.clear();
                        sumOfArrayList.clear();
                        for (int i = 0; i < posts.size(); i++) {
                            m_pendingbills m_pend = posts.get(i);
                            if (m_pend.isSelected()) {

                                dateArray.add(m_pend.getInvoicedate());

                            }
                        }
                        for (int j = 0; j < dateArray.size(); j++) {
                            System.out.println("Number of Days between dates: " + dateArray);
                            try {
                                date2 = Utility.convertDDMMYYYYtoYYYYMMDD(date1);
                                System.out.println("Number of Days between dates: " + date2 + "-----" + date2);
                                long dayCount = Utility.convertStringIntoDate(date2).getTime() - Utility.convertStringIntoDate(dateArray.get(j)).getTime();
                                dayNum = TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS);
                                sumOfArrayList.add(dayNum);


                                System.out.println("Number of Days between dayNum2-j: " + sumOfArrayList);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        float sumOfDate = 0.0f;
                        for (int k = 0; k < sumOfArrayList.size(); k++) {
                            sumOfDate += sumOfArrayList.get(k);
                        }

                        SharedPreferences.Editor editor = getSharedPreferences("OrderGenieSha", MODE_PRIVATE).edit();
                        editor.putString("sharedDate", date2);
                        //   editor.putInt("idName", 12);
                        editor.commit();
                        numDivide = sumOfDate / sumOfArrayList.size();
                        String strDate = String.valueOf(numDivide);
                        String strReplace = strDate.substring(0, strDate.indexOf("."));
                        text_average.setText("Avg Days :" + String.valueOf(strReplace));
                        System.out.println("Number of Days between dates: dayNum---" + dayNum);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    /* Get SQLite Invoice List */
    private void getInvoiceListSQLite() {
        Cursor cursor = sqLiteHandler.getPendingInvoiceList1(chemistId, invoice_flag);
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
                    String invoiceNo = cursor.getString(4);
                    String invoiceId = cursor.getString(5);
                    String invoiceDate = cursor.getString(6);
                    String totalItems = cursor.getString(7);
                    String billAmount = cursor.getString(8);
                    String paymentReceived = cursor.getString(9);
                    String balanceAmount = cursor.getString(10);
                    String paymentStatus = cursor.getString(11);
                    String grandTotal = cursor.getString(12);
                    String totalDiscount = cursor.getString(13);
                    String ledgerBalance = cursor.getString(14);

                    m_pendingbills m_pendingbills = new m_pendingbills(invoiceNo, invoiceDate,
                            Integer.valueOf(totalItems), billAmount, paymentReceived, balanceAmount, invoiceId, grandTotal, totalDiscount, ledgerBalance);
                    posts.add(m_pendingbills);

                    if (cursorCount == posts.size()) {
                        fill_payment_list(posts);
                    }
                } while (cursor.moveToNext());
            }
        }
    }


    /* Total Balance Interface */
    @Override
    public void totalBalance(int balance) {
        textViewTotalBalance.setText("Total Balance: " + String.valueOf(balance));
    }

    /* Save Payment Online */
    private void savePaymentOnline() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PaymnetAppID", paymentId);
            jsonObject.put("CreatedBy", userId);
            jsonObject.put("ChemistID", chemistId);
            jsonObject.put("Amount", Constant.enteredAmount);
            jsonObject.put("PaymentDate", currentDate());
            jsonObject.put("StockiestID", stockistId);
            jsonObject.put("Remarks", Remarks);
            jsonObject.put("Status", "Submitted");
            jsonObject.put("CreatedDate", currentDate());
            jsonObject.put("PaymentMode", paymentMode);
            jsonObject.put("Bank", bankName);
            jsonObject.put("ChequeNo", chequeNo);
            if (paymentModeId.equalsIgnoreCase("2")) {
                jsonObject.put("ChequeAmt", Constant.enteredAmount);
            } else {
                jsonObject.put("ChequeAmt", "");
            }
            jsonObject.put("ChequeDate", chequeDate);
            jsonObject.put("MicrCode", micrNo);
            jsonObject.put("Narration", narration);
            jsonObject.put("Comments", "");
            jsonObject.put("CameraImage", selfie_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        JSONArray jsonArray1 = new JSONArray();
        for (m_pendingbills m_pendingbills : Constant.selectedInvoice) {
            JSONObject jsonObject1 = new JSONObject();
            try {
                list1.add(m_pendingbills.getInvoiceno());
                jsonObject1.put("InvoiceNo", m_pendingbills.getInvoiceno());
                jsonObject1.put("InvoiceAmt", String.valueOf(Integer.valueOf(m_pendingbills.getBalanceamt()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())));
                jsonObject1.put("PaymentStatus", "Submitted");
                jsonObject1.put("CreatedDate", currentDate());
                jsonObject1.put("Status", "completed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray1.put(jsonObject1);
        }

        invoicejoinedoffline = TextUtils.join(", ", list1);
        //   Log.e("list2", invoicejoinedoffline);
        //   Log.d("list22", invoicejoinedoffline);
        jsonArray.put(jsonArray1);
        onlinePaymentAPI(jsonArray);
        String Flag = "online";
        // String collection_InvoiceList = "123,234,345,456,567,678";
        // String collection_ChemistName = "AVM Chemist";

        /* Insert Payment Collection */
        /* jyott */
        sqLiteHandler.create_Write_db_object();
        sqLiteHandler.mydb.beginTransaction();
        /* Insert Payment Collection */
        if (paymentModeId.equalsIgnoreCase("2")) {
            sqLiteHandler.insertPaymentCollection(stockistId, userId, chemistId, paymentId, userId, String.valueOf(Constant.enteredAmount),
                    currentDate(), Remarks, "Submitted", currentDate(), paymentMode, bankName, chequeNo,
                    String.valueOf(Constant.enteredAmount), chequeDate, micrNo, narration, "", Flag, invoicejoinedoffline, chemist_Name);
        } else {
            sqLiteHandler.insertPaymentCollection(stockistId, userId, chemistId, paymentId, userId, String.valueOf(Constant.enteredAmount),
                    currentDate(), Remarks, "Submitted", currentDate(), paymentMode, bankName, chequeNo, "", chequeDate,
                    micrNo, narration, "", Flag, invoicejoinedoffline, chemist_Name);
        }
        sqLiteHandler.mydb.setTransactionSuccessful();
        sqLiteHandler.mydb.endTransaction();
        sqLiteHandler.mydb.close();

//        for (m_pendingbills m_pendingbills : Constant.selectedInvoice) {
//
//            /* Insert Selected Invoices */
//            sqLiteHandler.insertSelectedInvoices(stockistId, userId, chemistId, paymentId, m_pendingbills.getInvoiceno(),
//                    String.valueOf(Integer.valueOf(m_pendingbills.getBalanceamt()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())),
//                    "Submitted", currentDate(), "completed");
//
//            /* Update Invoice List Table With Invoice Balance */
//            /*sqLiteHandler.updateInvoice(m_pendingbills.getInvoiceno(),
//                    String.valueOf(Integer.valueOf(m_pendingbills.getBillamount()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())),
//                    m_pendingbills.getBalanceAmountPayload());*/
//            sqLiteHandler.updateInvoiceReceivedAmount(m_pendingbills.getInvoiceno(),
//                    String.valueOf(Integer.valueOf(m_pendingbills.getBalanceamt()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())));
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
// successfully captured the image
// display it in image view

                previewCapturedImage();
                Toast.makeText(getApplicationContext(),
                        "Image Captured Successfully", Toast.LENGTH_SHORT)
                        .show();
//bitmap1=decodeFile(fileUri.getPath());

//////////////
                if (bitmap1 != null) {
/*selfie.setAdjustViewBounds(true);
selfie.setImageBitmap(bitmap1);*/
                }

            } else if (resultCode == RESULT_CANCELED) {
// user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
// failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    private void previewCapturedImage() {
        try {

// selfie.setVisibility(View.VISIBLE);

// bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

// downsizing image as it throws OutOfMemory Exception for large images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
// selfie.setImageBitmap(bitmap);

            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream); //compress to which format you want.
                byte[] byte_arr = stream.toByteArray();


                selfie_url = Base64.encodeToString(byte_arr, Base64.NO_WRAP);

                Log.d("show_selfie_url11", "show image url for camera: " + selfie_url + "Complete_URL");

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /* Save Payment Offline */
    private void savePaymentOffline() {
        String Flag = "offline";
//add chemist name and invoice
        /* Insert Payment Collection */


        for (m_pendingbills m_pendingbills : Constant.selectedInvoice) {


            list.add(m_pendingbills.getInvoiceno());
            // list.add("Item 2");
//            String joined = TextUtils.join(", ", list);
//            Log.e("list",joined);
//            Log.d("list1",joined);
            /* Insert Selected Invoices */
            sqLiteHandler.insertSelectedInvoices(stockistId, userId, chemistId, paymentId, m_pendingbills.getInvoiceno(),
                    String.valueOf(Integer.valueOf(m_pendingbills.getBalanceamt()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())),
                    "Submitted", currentDate(), "completed");

            /* Update Invoice List Table With Invoice Balance */
            /*sqLiteHandler.updateInvoice(m_pendingbills.getInvoiceno(),
                    String.valueOf(Integer.valueOf(m_pendingbills.getBillamount()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())),
                    m_pendingbills.getBalanceAmountPayload());*/
            sqLiteHandler.updateInvoiceReceivedAmount1(m_pendingbills.getInvoiceno(),
                    String.valueOf(Integer.valueOf(m_pendingbills.getBalanceamt()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())));
        }
        invoicejoined = TextUtils.join(", ", list);
        //  Log.e("list111", invoicejoined);
        //  Log.d("list11", invoicejoined);

        sqLiteHandler.create_Write_db_object();
        sqLiteHandler.mydb.beginTransaction();

        if (paymentModeId.equalsIgnoreCase("2")) {
            sqLiteHandler.insertPaymentCollection(stockistId, userId, chemistId, paymentId, userId, String.valueOf(Constant.enteredAmount),
                    currentDate(), Remarks, "Submitted", currentDate(), paymentMode, bankName, chequeNo,
                    String.valueOf(Constant.enteredAmount), chequeDate, micrNo, narration, "", Flag, invoicejoined, chemist_Name);
        } else {
            sqLiteHandler.insertPaymentCollection(stockistId, userId, chemistId, paymentId, userId, String.valueOf(Constant.enteredAmount),
                    currentDate(), Remarks, "Submitted", currentDate(), paymentMode, bankName, chequeNo, "", chequeDate,
                    micrNo, narration, "", Flag, invoicejoined, chemist_Name);
        }
        sqLiteHandler.mydb.setTransactionSuccessful();
        sqLiteHandler.mydb.endTransaction();
        sqLiteHandler.mydb.close();

        paymentSuccessDialog("Offline Mode", "Due to the internet connection absence payment stored in offline" +
                " mode.\n\nIn the presence of internet it will be sync automatically to the server");
    }


    /* Online Payment API */
    private void onlinePaymentAPI(JSONArray jsonArray) {
        String url = AppConfig.CollectPayment;
        //String url = AppConfig.DailyCollections;
        Log.d("urlll", url);
        Log.d("array", jsonArray.toString());
        Utility.showProgress(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Utility.hideProgress();
                if (response != null) {
                    //   Log.d("selectedInvoice", Constant.selectedInvoice.toString());
                    try {
                        paymentsId = response.getJSONObject(0).getString("PaymentID");
                        // Log.d("payments_responce12", paymentsId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (m_pendingbills m_pendingbills : Constant.selectedInvoice) {
                        /*sqLiteHandler.updateInvoice(m_pendingbills.getInvoiceno(),
                                String.valueOf(Integer.valueOf(m_pendingbills.getBillamount()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())),
                                m_pendingbills.getBalanceAmountPayload());*/
                        sqLiteHandler.updateInvoiceReceivedAmount1(m_pendingbills.getInvoiceno(),
                                String.valueOf(Integer.valueOf(m_pendingbills.getBalanceamt()) - Integer.valueOf(m_pendingbills.getBalanceAmountPayload())));
                    }
                    paymentSuccessDialog("Success", "Payment done! Thanks");
                    //updateonlinePaymentAPI();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress();
                //Toast.makeText(SalesmanMakePayment.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                finish();
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

    /* Payment SQLite */
    private void paymentSuccessDialog(String title, String msg) {
        new AlertDialog.Builder(this).setCancelable(false).setTitle(title).setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    private boolean get_location() {
        boolean check =false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //  mLocationManager.getLastKnownLocation();
        Boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled ||isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true;
            }

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1, this);


            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {

                String adress = LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), this);
            }
         check =true;
        } else {
           // showSettingsAlert();
            Intent intent = new Intent(SalesmanMakePayment.this, LocationActivity.class);
            startActivity(intent);
            check =false;
            //return false;
        }
        return check;
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Please enable the Location");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OGtoast.OGtoast("Location Services not enabled !. Unable to get the location", getApplicationContext());
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void update_Current_locationn() {
        Log.d("GPS", "inside location");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        locationProvider = locationManager.getBestProvider(criteria, false);

        if (locationProvider != null && !locationProvider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //  locationManager.requestLocationUpdates(locationProvider, 10000, 1, this);
            Location location = locationManager.getLastKnownLocation(locationProvider);
            locationManager.requestLocationUpdates(locationProvider, 10000, 1, this);

            if (location != null) {
                Log.d("GPS", "location not null");
                onLocationChanged(location);
            } else {
                Log.d("GPS", "location null");
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onLocationChanged(Location location) {


        if (location != null) {
            currentLocLat = location.getLatitude();
            currentLocLong = location.getLongitude();
            //  locationManager.removeUpdates(CustomerlistActivity.this);
            //   Log.d("order_salesman11", currentLocLat + "  " + currentLocLong);
            String adress = LocationAddress.getAddressFromLocation(currentLocLat, currentLocLong, this);
            // Log.d("onLocationupdate12",adress);

            JSONObject jsonParams = new JSONObject();
            try {
                SharedPreferences pref = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                String uID = pref.getString("UUID", "0");
                Log.d("location_updatePaymen2", uID);
                String trasaction_No = pref.getString("Transaction_No", "");
                // Log.d("Show_Transaction13", trasaction_No);
                // jsonParams.put("UserID", pref.getString(USER_ID, "0"));
                //jsonParams.put("UserID", Selected_chemist_id);
                jsonParams.put("Latitude", String.valueOf(location.getLatitude()));
                jsonParams.put("Longitude", String.valueOf(location.getLongitude()));
                jsonParams.put("CurrentLocation", adress);
                jsonParams.put("UserID", userId);
                jsonParams.put("CustID", chemistId);
                jsonParams.put("task", "Payment");
                jsonParams.put("Tran_No", paymentsId);
                jsonParams.put("unqid", uID);
                // jsonParams.put("Tran_No","");


                Log.d("callplan_Location12", AppConfig.POST_UPDATE_CURRENT_LOCATION);
                Log.d("callplan_Location13", String.valueOf(jsonParams));
                MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CURRENT_LOCATION, AppConfig.POST_UPDATE_CURRENT_LOCATION, jsonParams, this, false);

                locationManager.removeUpdates(SalesmanMakePayment.this);

                Log.d("order_salesman14", "Stop service location");
                // USE THIS API
                // MakeWebRequest.MakeWebRequest("Post", AppConfig.POST_UPDATE_CHEMIST_LOCATION, AppConfig.POST_UPDATE_CHEMIST_LOCATION, jsonParams, this, false);

            } catch (Exception e) {
            }
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Gps_is9", "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {

        Log.d("Gps_is10", "GPS Disable");
    }

    @Override
    public void onProviderDisabled(String s) {

        Log.d("Gps_is11", "GPS Disable");

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        if (response != null) {

            if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                try {
                    String status = response.getString("Status");
                    Log.d("callplan_Location14", response.toString());
                    if (status.equalsIgnoreCase("success")) {
                        // Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(getBaseContext(), "Location not Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /* Amount Dialog */
    private void amountDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.amount_dialog_new);
        dialog.show();

        ImageButton imageButtonClose = (ImageButton) dialog.findViewById(R.id.img_close_dialog);
        final EditText editTextAmount = (EditText) dialog.findViewById(R.id.edt_amount_dialog);
        TextView textViewDone = (TextView) dialog.findViewById(R.id.txt_done_dialog);
        ImageButton textViewClear = (ImageButton) dialog.findViewById(R.id.txt_clear_amt);

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // editTextAmount.getText().clear();
        final Integer tot_outstanding = totalAmountCount(posts);
        editTextAmount.setText(String.valueOf(Constant.enteredAmount));
//        Constant.enteredAmount = 0;
//        Constant.balanceAmount = 0;
//        Constant.ableToSelectInvoice = 0;
        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextAmount.getText().toString().isEmpty()) {
                    Toast.makeText(SalesmanMakePayment.this, "Enter amount", Toast.LENGTH_SHORT).show();
                } else if ((Integer.parseInt(editTextAmount.getText().toString())) > tot_outstanding) {
                    Toast.makeText(SalesmanMakePayment.this, "Can not enter amount > " + String.valueOf(tot_outstanding), Toast.LENGTH_SHORT).show();
                } else {

                    Constant.enteredAmount = 0;
                    Constant.balanceAmount = 0;
                    Constant.ableToSelectInvoice = 0;
                    for (int i = 0; i < posts.size(); i++) {
                        m_pendingbills m_pend = posts.get(i);
                        if (m_pend.isSelected()) {
                            Log.d("position13", String.valueOf(posts.size()) + "--" + i);
                            Log.d("position14", String.valueOf(m_pend.getBalanceamt()));
                            Log.d("position16", String.valueOf(m_pend.getInvoiceno()));
                            Log.d("position15", String.valueOf(m_pend.isSelected()));
                            // dateArray.add(m_pend.getInvoicedate());
                            m_pend.setSelected(false);
                            paymentListAdapter.notifyDataSetChanged();
                            // posts.remove(i);
                        }
                    }
                    //[113,741,424,747,336,456] [1,2,4,5,6,7]
                    // paymentListAdapter.notifyDataSetChanged();
                    Constant.selectedInvoice.clear();
                    Constant.enteredAmount = Integer.valueOf(editTextAmount.getText().toString());
                    Constant.balanceAmount = Constant.enteredAmount;
                    Constant.ableToSelectInvoice = Constant.balanceAmount;
                    textViewTotalBalance.setText("Total Balance: " + Constant.balanceAmount);
                    Log.d("position11", String.valueOf(Constant.balanceAmount));
                    Log.d("position12", editTextAmount.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        textViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextAmount.getText().clear();
            }
        });
    }

    /* Total Amount Count */
    private int totalAmountCount(List<m_pendingbills> post) {
        int totalAmount = 0;
        for (int i = 0; i < post.size(); i++) {
            totalAmount = totalAmount + Integer.valueOf(post.get(i).getBalanceamt());
        }
        Log.d("Total", String.valueOf(totalAmount));
        return totalAmount;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = getSharedPreferences("OrderGenieSha", MODE_PRIVATE).edit();
        editor.clear();
        //   editor.putInt("idName", 12);
        editor.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("OrderGenieSha", MODE_PRIVATE).edit();
        editor.clear();
        //   editor.putInt("idName", 12);
        editor.commit();
//.i("SharedPreferences","onPause");
    }

    private void RemarkDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.remark_dialog);
        dialog.show();

        ImageButton imageButtonClose = (ImageButton) dialog.findViewById(R.id.img_close_dialog);
        final EditText editTextAmount = (EditText) dialog.findViewById(R.id.edt_amount_dialog);
        TextView textViewDone = (TextView) dialog.findViewById(R.id.txt_done_dialog);
        TextView heading = (TextView)dialog.findViewById(R.id.heading);


        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editTextAmount.getText().clear();

        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextAmount.getText().toString().isEmpty()&&
                        editTextAmount.getText().toString().trim().length()==0) {
                    //Toast.makeText(SalesmanMakePayment.this, "Enter Remark", Toast.LENGTH_SHORT).show();
                } else {
                    Remarks = editTextAmount.getText().toString().trim();


                }
                if (Utility.internetCheck(SalesmanMakePayment.this)) {
                    savePaymentOnline();
                } else {
                    savePaymentOffline();
                }
                dialog.dismiss();
            }
        });
    }

}