package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.SelectedDailypaymentAdapter;
import com.synergy.keimed_ordergenie.model.m_pendingbills;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CHEMIST_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class SelectedDailypaymentList extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    /* Find Views */
    private TextView text_title, txt_start_date,txt_end_date, textViewChemistName;
    private TextView textViewTotalAmount, text_amount_collect;
    private RecyclerView recyclerview;
    private Button buttonMakePayment;
    Cursor cursor;
    private int nYear, nMonth, Nday;
    private String chemistName;
    private String userId, stockistId, chemistId, getChemistId, Check, flag = "PDC";
    private boolean isCallPlanTask;
    DatePickerDialog dpd_start_date, dpd_end_date;
    private SharedPreferences pref;
    private TextView empty_view;
    List<m_pendingbills> posts;

    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private LinearLayout linearLayout;
    private Date filter_start_date,filter_end_date;
    private Date current_date = Calendar.getInstance().getTime();
    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecteddailypayment_list);

        Log.d("DailypaymentList", "collecion List");


        /* Initialize Class Object */
        sqLiteHandler = new SQLiteHandler(this);

        /* FindViews */
        textViewChemistName = (TextView) findViewById(R.id.txt_chemist_name_payment_list);
        textViewTotalAmount = (TextView) findViewById(R.id.txt_total_amount_payment_list);
        text_amount_collect = (TextView) findViewById(R.id.total_amount);
        text_title = (TextView) findViewById(R.id.text_title);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        empty_view = (TextView) findViewById(R.id.empty_view);
        buttonMakePayment = (Button) findViewById(R.id.make_payment);
        linearLayout = (LinearLayout) findViewById(R.id.layout1);
        buttonMakePayment.setOnClickListener(this);

        /* Get Intent or SharedPref */
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);
        chemistName = getIntent().getStringExtra(CLIENT_NAME);
        userId = pref.getString(USER_ID, "0");
        stockistId = pref.getString(CLIENT_ID, "0");
        chemistId = getIntent().getStringExtra("chemist_id");
        getChemistId = getIntent().getStringExtra(CHEMIST_ID);

        if (chemistId != null) {
            Log.d("chemist", chemistId);
        }
        Check = getIntent().getStringExtra("flag");
        // Toast.makeText(getApplicationContext(),"flag"+Check,Toast.LENGTH_SHORT).show();
        if (Check.equalsIgnoreCase(flag)) {
            setTitle("PDC Report");
            text_title.setText("PDC Report");
        } else {
            setTitle("Daily Collection Report");
            text_title.setText("Daily Collection Report");
        }

        /* Set Values */
        textViewChemistName.setText(chemistName);

        /* RecyclerView Item Decoration */
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(), DividerItemDecoration.VERTICAL);
        recyclerview.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collection_reports, menu);

        searchView = (SearchView) menu.findItem(R.id.customersname_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                List<m_pendingbills> mBills = new ArrayList<>();

                for(m_pendingbills m_bills_modal : posts){

                    final String getCustomerName = m_bills_modal.getLedgerBalance().toLowerCase();

                    if(getCustomerName.contains(newText)){

                        mBills.add(m_bills_modal);
                    }

                }

                fill_payment_list(mBills);

                return false;

            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
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
        final View dialogview = inflater.inflate(R.layout.dialog_collection_reports_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);

        txt_start_date = (TextView) dialogview.findViewById(R.id.txt_start_date);
         txt_end_date = (TextView) dialogview.findViewById(R.id.txt_end_datee);
        final CheckBox chk_pending = (CheckBox) dialogview.findViewById(R.id.cash_report);
        final CheckBox chk_completed = (CheckBox) dialogview.findViewById(R.id.cheque_report);


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
                        SelectedDailypaymentList.this,
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
                        SelectedDailypaymentList.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_end_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_end_date.setMaxDate(Calendar.getInstance());

            }


        });

        dialogview.findViewById(R.id.btn_filterr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter_dialog_conditions(filter_start_date,filter_end_date, chk_pending.isChecked(), chk_completed.isChecked());
                infoDialog.dismiss();
            }


        });

        dialogview.findViewById(R.id.btn_clearr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  fill_orderlist(posts);
                text_amount_collect.setText("Amount :Rs. " + totalAmountCount(posts));
                fill_payment_list(posts);
                infoDialog.dismiss();
            }
        });


        set_attributes(infoDialog);
        infoDialog.show();
        //   infoDialog.show();


    }

    private void filter_dialog_conditions(Date startDate, Date endDate, boolean iscashchecked, boolean ischequechecked) {


        Date convertedDate = null;

        final List<m_pendingbills> filteredModelList = new ArrayList<>();
        for (m_pendingbills model : posts) {

            Log.d("print11", model.getInvoicedate());
            Log.d("print12", model.getInvoiceno());
            Log.d("print19", model.getPaymentreceived());
            //Log.d("print13",model.getInvoiceno());
            //Log.d("print14",model.getInvoiceno());

            if (startDate != null && endDate != null) {
                final String date = model.getInvoicedate();
                Log.d("printdate11", date);
                Log.d("printdate12", startDate.toString());
                try {
                    convertedDate = dateFormat.parse(date);
                    Log.d("printdate13", convertedDate.toString());
                    String c_date = sdf.format(convertedDate);
                    // convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                } catch (Exception e) {
                }

                //if (((convertedDate.equals(startDate)))) //here "date2" and "date1" must be converted to dateFormat
                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                {
                    if (iscashchecked) {

                        Log.d("print13", model.getInvoiceno());
                        if (model.getInvoiceno().equals("Cash")) {
                            filteredModelList.add(model);
                        }
                    } else if (ischequechecked) {
//Cheque
                        Log.d("print14", model.getInvoiceno());
                        if (model.getInvoiceno().equals("Cheque")) {
                            filteredModelList.add(model);
                        }
                    } else {
                        filteredModelList.add(model);
                    }
                }

            } else {
                if (iscashchecked) {
                    if (model.getInvoiceno().equals("Cash")) {
                        filteredModelList.add(model);
                    }
                }

                if (ischequechecked) {
                    if (model.getInvoiceno().equals("Cheque")) {
                        filteredModelList.add(model);
                    }
                }

            }
        }
        //fill_orderlist(filteredModelList);


        int amount = totalAmountCount(filteredModelList);
        Log.d("amount_show", String.valueOf(amount));

        // textViewTotalAmount.setText(String.valueOf(amount));
        text_amount_collect.setText("Amount :Rs. " + String.valueOf(amount));

        fill_payment_list(filteredModelList);
        // textViewTotalAmount.setText("Rs. " + totalAmountCount(filteredModelList));


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

    /* onResume */
    @Override
    protected void onResume() {
        super.onResume();
        //  if (Check.equalsIgnoreCase(flag)) {
        //      getInvoiceListSQLiteDailyCollection();
        //  } else {

        // }
        buttonMakePayment.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        getInvoiceListSQLiteDailyCollection();

        //  }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == android.R.id.home) {
//          Intent i=new Intent(getApplicationContext(),CallPlanDetails.class);
//          startActivity(i);
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }

    /* RecyclerView Adapter */
    public void fill_payment_list(final List<m_pendingbills> posts_s) {
        if (posts_s.size() > 0) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(new SelectedDailypaymentAdapter(this, posts_s));
    }


    /* Get Invoice with Order By DESC Date and Equal to current Date */
    private void getInvoiceListSQLiteDailyCollection() {
        //jyott 27/6
        if (Check.equalsIgnoreCase(flag)) {
            cursor = sqLiteHandler.getTableCollectPDCPaymentSalesman(chemistId);
        } else {
            cursor = sqLiteHandler.getTableCollectPaymentSalesman();
        }

        //  Cursor cursor = sqLiteHandler.getTableCollectPaymentSalesman();
        // Cursor cursor = sqLiteHandler.getAllInvoiceList();
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            empty_view.setVisibility(View.VISIBLE);
            buttonMakePayment.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            //buttonMakePayment.setVisibility(View.VISIBLE);
            if (cursor.moveToFirst()) {
                do {

//                    Key_Id_PaymentSalesman 0
//                    Key_StockistId_PaymentSalesman 1
//                    Key_UserId_PaymentSalesman 2
//                    Key_ChemistId_PaymentSalesman 3
//                    Key_PaymentId_PaymentSalesman 4
//                    Key_CreatedBy_PaymentSalesman 5
//                    Key_Amount_PaymentSalesman 6
//                    Key_PaymentDate_PaymentSalesman 7
//                    Key_Remarks_PaymentSalesman 8
//                    Key_Status_PaymentSalesman 9
//                    Key_CreatedDate_PaymentSalesman 10
//                    Key_PaymentMode_PaymentSalesman 11
//                    Key_BankName_PaymentSalesman 12
//                    Key_ChequeNo_PaymentSalesman 13
//                    Key_ChequeAmount_PaymentSalesman 14
//                    Key_ChequeDate_PaymentSalesman 15
//                    Key_MicrNo_PaymentSalesman 16
//                    Key_Narration_PaymentSalesman 17
//
//                 Key_Comments_PaymentSalesman = "Comments"; 18
//                   Key_Flag_PaymentSalesman = "Flag"; 19
//                   Key_InvoiceList_PaymentSalesman = "InvoiceList"; 20
//                    Key_ChemistName_PaymentSalesman = "ChemistName"; 21

                    String invoiceNo = cursor.getString(11);// payment mode: cash /cheque
                    String invoiceDate = cursor.getString(7);// payment collection date
                    String paymentReceived = cursor.getString(6);//payment Received
                    String totalItems = cursor.getString(3);//* chemist ID
                    String totalDiscount = cursor.getString(12);//bank name
                    // String totalItems = cursor.getString(7);
                    String invoiceId = cursor.getString(13);//* chequeno

                    String billAmount = cursor.getString(15);//* chequedate

                    String balanceAmount = cursor.getString(19);//*flagstatus
                    //String paymentStatus = "confirmm";//*
                    String grandTotal = cursor.getString(20);//*InvoiceList
                    String ledgerBalance = cursor.getString(21);//*ChemistName

                   /* m_pendingbills m_pendingbills = new m_pendingbills(invoiceNo, invoiceDate,
                            Integer.valueOf(totalItems), billAmount, paymentReceived, balanceAmount,
                            invoiceId, grandTotal, totalDiscount, ledgerBalance);

                    posts.add(m_pendingbills);

                    Log.d("qwerty", String.valueOf(posts.size()));

                    if (cursorCount == posts.size()) {
                        text_amount_collect.setVisibility(View.VISIBLE);
                        text_amount_collect.setText("Amount :Rs. " + totalAmountCount(posts));
                        fill_payment_list(posts);
                    }*/

                    try {
                        sdf.setDateFormatSymbols(DateFormatSymbols.getInstance(Locale.ENGLISH));
                        final String filterdate = sdf.format(Calendar.getInstance().getTime()).toString();

                        SelectedDailypaymentList.Predicate<m_pendingbills> validPersonPredicate = new SelectedDailypaymentList.Predicate<m_pendingbills>() {

                            Date date;

                            public boolean apply(m_pendingbills m_order) {
                                try {
                                    String s = sdf.format(getFormattedDate(m_order.getInvoicedate()));
                                    // String data = m_order.getDescription();
                                    // String data1 = m_order.getDeliveryDate();
                                    // Log.d("data", data);
                                    // Log.d("data11", data1);
                                    // String s2=sdf.format(sdf.parse(m_order.getDescription()));
                                    return s.equals(filterdate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        };

                        // m_report_delivery_invoice_list m_pendingbills = new m_report_delivery_invoice_list(ErpsalesmanID,DeliveryDate,DeliveryStatus,OrderID,InvoiceDate,BoxCount,InvoiceNo,TotalAmount,TotalItems,Package_count,DeliveryId,DeliveryFlag,ChemistId,DeliveryUpdatedStatus);
                        //  m_report_delivery_invoice_list m_pendingbills = new m_report_delivery_invoice_list(ErpsalesmanID, DeliveryDate, DeliveryStatus, OrderID, InvoiceDate, BoxCount, InvoiceNo, TotalAmount, TotalItems, Package_count, DeliveryId, DeliveryFlag, ChemistId, DeliveryUpdatedStatus, Deliverychemist_name); // 28 sep

                        m_pendingbills m_pendingbills = new m_pendingbills(invoiceNo, invoiceDate,
                                Integer.valueOf(totalItems), billAmount, paymentReceived, balanceAmount,
                                invoiceId, grandTotal, totalDiscount, ledgerBalance);
                        posts.add(m_pendingbills);

                        Collections.sort(posts, new SelectedDailypaymentList.CustomComparator());
                        Collection<m_pendingbills> result = SelectedDailypaymentList.filter(posts, validPersonPredicate);



                        text_amount_collect.setVisibility(View.VISIBLE);
                        text_amount_collect.setText("Amount :Rs. " + totalAmountCount((List)result));
                        fill_payment_list((List) result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
        }
    }


    /* onClick */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.make_payment:
                Cursor cursor = sqLiteHandler.getPendingInvoiceList(chemistId);
                if (cursor.getCount() == 0) {
                    cursor.close();
                    Toast.makeText(this, "Payment not pending", Toast.LENGTH_SHORT).show();
                } else {
                    cursor.close();
                    amountDialog();
                }
                break;
        }
    }


    /* Amount Dialog */
    private void amountDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.amount_dialog);
        dialog.show();

        ImageButton imageButtonClose = (ImageButton) dialog.findViewById(R.id.img_close_dialog);
        final EditText editTextAmount = (EditText) dialog.findViewById(R.id.edt_amount_dialog);
        TextView textViewDone = (TextView) dialog.findViewById(R.id.txt_done_dialog);

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editTextAmount.getText().clear();
        Constant.enteredAmount = 0;
        Constant.balanceAmount = 0;
        Constant.ableToSelectInvoice = 0;
        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextAmount.getText().toString().isEmpty()) {
                    Toast.makeText(SelectedDailypaymentList.this, "Enter amount", Toast.LENGTH_SHORT).show();
                } else {
                    Constant.enteredAmount = Integer.valueOf(editTextAmount.getText().toString());
                    Constant.balanceAmount = Constant.enteredAmount;
                    Constant.ableToSelectInvoice = Constant.balanceAmount;
                    Intent intent = new Intent(SelectedDailypaymentList.this, SalesmanMakePayment.class);
                    intent.putExtra("chemistName", chemistName);
                    intent.putExtra("chemist_id", chemistId);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
    }
    /* Get SQLite Invoice List */
//    private void getInvoiceListSQLite() {
//        Cursor cursor = sqLiteHandler.getAllInvoice(chemistId,invoic);
//        int cursorCount = cursor.getCount();
//        posts = new ArrayList<>();
//        if (cursorCount == 0) {
//            empty_view.setVisibility(View.VISIBLE);
//            buttonMakePayment.setVisibility(View.GONE);
//        } else {
//            empty_view.setVisibility(View.GONE);
//            buttonMakePayment.setVisibility(View.VISIBLE);
//            if (cursor.moveToFirst()) {
//                do {
//                    String invoiceNo = cursor.getString(4);
//                    String invoiceId = cursor.getString(5);
//                    String invoiceDate = cursor.getString(6);
//                    String totalItems = cursor.getString(7);
//                    String billAmount = cursor.getString(8);
//                    String paymentReceived = cursor.getString(9);
//                    String balanceAmount = cursor.getString(10);
//                    String paymentStatus = cursor.getString(11);
//                    String grandTotal = cursor.getString(12);
//                    String totalDiscount = cursor.getString(13);
//                    String ledgerBalance = "0";
//
//                    m_pendingbills m_pendingbills = new m_pendingbills(invoiceNo, invoiceDate,
//                            Integer.valueOf(totalItems), billAmount, paymentReceived, balanceAmount, invoiceId,
//                            grandTotal, totalDiscount,ledgerBalance);
//
//                    posts.add(m_pendingbills);
//
//                    if (cursorCount == posts.size()) {
//                        textViewTotalAmount.setText("Rs. " + totalAmountCount(posts));
//                        fill_payment_list(posts);
//                    }
//                } while (cursor.moveToNext());
//            }
//        }
//    }


    /* Total Amount Count */
    private int totalAmountCount(List<m_pendingbills> post) {
        int totalAmount = 0;
        for (int i = 0; i < post.size(); i++) {
//            if (Check.equals("isFromCallPlan")) {
//                totalAmount = totalAmount + Integer.valueOf(post.get(i).getBalanceamt());
//            } else {
            totalAmount = totalAmount + Integer.valueOf(post.get(i).getPaymentreceived());
            //  }
        }
        return totalAmount;
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
    public interface Predicate<T> {
        boolean apply(T type);
    }

    public static <T> Collection<T> filter(Collection<T> col, SelectedDailypaymentList.Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }


    public static class CustomComparator implements Comparator<m_pendingbills> {
        @Override
        public int compare(m_pendingbills o1, m_pendingbills o2) {
            return getFormattedDate(o2.getInvoicedate()).compareTo(getFormattedDate(o1.getInvoicedate()));
                    }

    }

    public static Date getFormattedDate(String time){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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

    public static Date getChangeFormateDate(String time){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateFormat read = new SimpleDateFormat( "dd-MM-yyyy");
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