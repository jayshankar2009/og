package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.SelectedpaymentAdapter;
import com.synergy.keimed_ordergenie.model.m_pendingbills;

import java.util.ArrayList;
import java.util.List;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CHEMIST_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class SelectedpaymentList extends AppCompatActivity implements View.OnClickListener {

    /* Find Views */
    private TextView textViewChemistName;
    private Button button_pdc,button_creditnote;
    private TextView textViewTotalAmount, text_amount_collect;
    private RecyclerView recyclerview;
    private Button buttonMakePayment;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String chemistName,invoice_flag="0";
    private String userId, stockistId, chemistId, getChemistId, Flag;
    private boolean isCallPlanTask;
    String Name = "";
    private SharedPreferences pref;
    private TextView empty_view;
    List<m_pendingbills> posts;

    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedpayment_list);

        setTitle("Pending Bills");

        /* Initialize Class Object */
        sqLiteHandler = new SQLiteHandler(this);

        /* FindViews */
        textViewChemistName = (TextView) findViewById(R.id.txt_chemist_name_payment_list);
        textViewTotalAmount = (TextView) findViewById(R.id.txt_total_amount_payment_list);
        text_amount_collect = (TextView) findViewById(R.id.total_amount);
        button_pdc = (Button) findViewById(R.id.button_pdc);
        button_creditnote = (Button) findViewById(R.id.button_creditnote);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        empty_view = (TextView) findViewById(R.id.empty_view);
        buttonMakePayment = (Button) findViewById(R.id.make_payment);
        linearLayout = (LinearLayout) findViewById(R.id.layout1);
        buttonMakePayment.setOnClickListener(this);

        /* Get Intent or SharedPref */
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);
       // chemistName = getIntent().getStringExtra(CLIENT_NAME);
        userId = pref.getString(USER_ID, "0");
        stockistId = pref.getString(CLIENT_ID, "0");
       // Log.i("Pending Bills","Pending");

       // chemistId = getIntent().getStringExtra("chemist_id");

      /*  if(chemistId!=null){
          //  Log.d("chem12",chemistId);
        }*/
        getChemistId = getIntent().getStringExtra(CHEMIST_ID);
        //Flag = getIntent().getStringExtra("flag");
       // Log.d("Flag",Flag);
        /* Set Values */
        SharedPreferences prefsn = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        chemistId = prefsn.getString("chemist_id", null);//"No name defined" is the default value.
        Flag = prefsn.getString("flag", null);
        chemistName = prefsn.getString(CLIENT_NAME, null);
        textViewChemistName.setText(chemistName);
        /* RecyclerView Item Decoration */
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(), DividerItemDecoration.VERTICAL);
        recyclerview.addItemDecoration(dividerItemDecoration);

        button_pdc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(),"pdc",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectedpaymentList.this, SelectedDailypaymentList.class);
                intent.putExtra("flag","PDC");
                intent.putExtra("chemist_id", chemistId);
                startActivity(intent);
            }
        });
        button_creditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(getApplicationContext(),"pdc"+chemistName,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectedpaymentList.this, CreditNoteList.class);
                intent.putExtra("flag","Credit Note");
                intent.putExtra("chemist_id", chemistId);
                intent.putExtra("chemistName", chemistName);
                startActivity(intent);
            }
        });
        //getInvoiceListSQLite();

    }



    /* onResume */
    @Override
    protected void onResume() {
        super.onResume();
        //   if (Check.equals("isFromCallPlan")) {
        getInvoiceListSQLite();
        // Log.d("ifCheck", "if");
//        } else {
//            Log.d("elseCheck", "else");
//            buttonMakePayment.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.GONE);
//            getInvoiceListSQLiteDailyCollection();
//        }
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
        recyclerview.setAdapter(new SelectedpaymentAdapter(this, posts_s));
    }


    /* Get Invoice with Order By DESC Date and Equal to current Date */
    private void getInvoiceListSQLiteDailyCollection() {
        Cursor cursor = sqLiteHandler.getAllInvoiceList();
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
                            Integer.valueOf(totalItems), billAmount, paymentReceived, balanceAmount,
                            invoiceId, grandTotal, totalDiscount,ledgerBalance);

                    posts.add(m_pendingbills);

                    if (cursorCount == posts.size()) {
                        text_amount_collect.setVisibility(View.VISIBLE);
                        text_amount_collect.setText("Amount :Rs. " + totalAmountCount(posts));
                        fill_payment_list(posts);
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
                Cursor cursor = sqLiteHandler.getPendingInvoiceList1(chemistId,invoice_flag);
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
        final Integer tot_outstanding = totalAmountCount(posts);
        editTextAmount.getText().clear();
        Constant.enteredAmount = 0;
        Constant.balanceAmount = 0;
        Constant.ableToSelectInvoice = 0;
        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextAmount.getText().toString().isEmpty()) {
                    Toast.makeText(SelectedpaymentList.this, "Enter amount", Toast.LENGTH_SHORT).show();
                }
                else if((Integer.parseInt(editTextAmount.getText().toString()))>tot_outstanding){
                    Toast.makeText(SelectedpaymentList.this, "Can not enter amount > "+String.valueOf(tot_outstanding), Toast.LENGTH_SHORT).show();
                }
                else {
                    Constant.enteredAmount = Integer.valueOf(editTextAmount.getText().toString());
                    Constant.balanceAmount = Constant.enteredAmount;
                    Constant.ableToSelectInvoice = Constant.balanceAmount;
                    Intent intent = new Intent(SelectedpaymentList.this, SalesmanMakePayment.class);
                    intent.putExtra("chemistName", chemistName);
                    intent.putExtra("chemist_id", chemistId);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
    }


    /* Get SQLite Invoice List */
    private void getInvoiceListSQLite() {
     //   chemistId = getIntent().getStringExtra("chemist_id");
      //  Toast.makeText(getApplicationContext(),"Cursor Count==="+chemistId+"---"+invoice_flag,Toast.LENGTH_SHORT).show();
        Cursor cursor = sqLiteHandler.getAllInvoice(chemistId,invoice_flag);
        int cursorCount = cursor.getCount();
  //      Toast.makeText(getApplicationContext(),"Cursor Count"+cursorCount,Toast.LENGTH_SHORT).show();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            empty_view.setVisibility(View.VISIBLE);
            buttonMakePayment.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            if (Flag.equals("customer"))
            {
                buttonMakePayment.setVisibility(View.GONE);
            }
            else {
                buttonMakePayment.setVisibility(View.VISIBLE);
            }
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
                    String invoice_flag = cursor.getString(15);

                    // if(invoice_flag=="0"||invoice_flag.equalsIgnoreCase("0")||invoice_flag.equals("0")){
                    m_pendingbills m_pendingbills = new m_pendingbills(invoiceNo, invoiceDate,
                            Integer.valueOf(totalItems), billAmount, paymentReceived, balanceAmount, invoiceId,
                            grandTotal, totalDiscount, ledgerBalance);

                    posts.add(m_pendingbills);

                    if (cursorCount == posts.size()) {
                        textViewTotalAmount.setText("Rs. " + totalAmountCount(posts));
                        fill_payment_list(posts);
                    }
                } while (cursor.moveToNext());
            }
        }
    }


    /* Total Amount Count */
    private int totalAmountCount(List<m_pendingbills> post) {
        int totalAmount = 0;
        for (int i = 0; i < post.size(); i++) {
            // if (Check.equals("isFromCallPlan")) {
            totalAmount = totalAmount + Integer.valueOf(post.get(i).getBalanceamt());
//            } else {
//                totalAmount = totalAmount + Integer.valueOf(post.get(i).getPaymentreceived());
//            }
        }
        return totalAmount;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }
}
