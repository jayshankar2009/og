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
import android.view.MenuItem;
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
import com.synergy.keimed_ordergenie.adapter.CreditNoteAdapter;
import com.synergy.keimed_ordergenie.model.m_pendingbills;
import java.util.ArrayList;
import java.util.List;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CHEMIST_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class CreditNoteList extends AppCompatActivity implements View.OnClickListener {
    /* Find Views */
    private TextView title_list,textViewChemistName;
    private Button button_pdc,button_creditnote;
    private TextView textViewTotalAmount, text_amount_collect;
    private RecyclerView recyclerview;
    private Button buttonMakePayment;
    private String chemistName,invoice_flag="0";
    private String userId, stockistId, chemistId, getChemistId, Check;
    private boolean isCallPlanTask;
    String Name = "";
    private SharedPreferences pref;
    private TextView empty_view;
    List<m_pendingbills> posts;
    CallPlanDetails.MyResultReceiver resultReceiver;
    /* SQLite Database */
    private SQLiteHandler sqLiteHandler;
    private LinearLayout linearLayout,txt_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditnote_list);

        setTitle("Credit Note List");
        Log.d("Ciclik_creditnotes11","Activity start Credit Notes");
        /* Initialize Class Object */
        sqLiteHandler = new SQLiteHandler(this);

        /* FindViews */
        textViewChemistName = (TextView) findViewById(R.id.txt_chemist_name_payment_list);
        textViewTotalAmount = (TextView) findViewById(R.id.txt_total_amount_payment_list);
        txt_header = (LinearLayout) findViewById(R.id.txt_header);
        title_list = (TextView) findViewById(R.id.title_list);
        text_amount_collect = (TextView) findViewById(R.id.total_amount);
        button_pdc = (Button) findViewById(R.id.button_pdc);
        button_creditnote = (Button) findViewById(R.id.button_creditnote);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        empty_view = (TextView) findViewById(R.id.empty_view);
        buttonMakePayment = (Button) findViewById(R.id.make_payment);
        linearLayout = (LinearLayout) findViewById(R.id.layout1);

        /* Get Intent or SharedPref */
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isCallPlanTask = getIntent().getBooleanExtra("call_plan_task", false);
        chemistName = getIntent().getStringExtra(CLIENT_NAME);
        userId = pref.getString(USER_ID, "0");
        stockistId = pref.getString(CLIENT_ID, "0");
        chemistId = getIntent().getStringExtra("chemist_id");
        getChemistId = getIntent().getStringExtra(CHEMIST_ID);

        if (chemistId!=null) {
            Log.d("chemist", chemistId);
        }
        /* Set Values */
        startpage();
        /* RecyclerView Item Decoration */
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(), DividerItemDecoration.VERTICAL);
        recyclerview.addItemDecoration(dividerItemDecoration);


    }

    private void startpage() {
        /* Set Values */
        //  textViewChemistName.setText("AK Pharma Davangere");
      //  Toast.makeText(getApplicationContext(),"pdc1"+chemistName,Toast.LENGTH_SHORT).show();
        textViewChemistName.setText(chemistName);
        title_list.setText("Credit/Debit Notes");
        txt_header.setVisibility(View.GONE);
        textViewTotalAmount.setVisibility(View.GONE);
        button_creditnote.setVisibility(View.GONE);
        button_pdc.setVisibility(View.GONE);
        buttonMakePayment.setVisibility(View.GONE);
    }


    /* onResume */
    @Override
    protected void onResume() {
        super.onResume();
        //   if (Check.equals("isFromCallPlan")) {
        getInvoiceListSQLite();

    }



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
        recyclerview.setAdapter(new CreditNoteAdapter(this, posts_s));
    }





    /* onClick */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.make_payment:
                Cursor cursor = sqLiteHandler.getPendingInvoiceList(chemistId);
                if (cursor.getCount() == 0) {
                    cursor.close();
                  //  Toast.makeText(this, "Payment not pending", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreditNoteList.this, "Enter amount", Toast.LENGTH_SHORT).show();
                }
                else if((Integer.parseInt(editTextAmount.getText().toString()))>tot_outstanding){
                    Toast.makeText(CreditNoteList.this, "Can not enter amount > "+String.valueOf(tot_outstanding), Toast.LENGTH_SHORT).show();
                }
                else {
                    Constant.enteredAmount = Integer.valueOf(editTextAmount.getText().toString());
                    Constant.balanceAmount = Constant.enteredAmount;
                    Constant.ableToSelectInvoice = Constant.balanceAmount;
                    Intent intent = new Intent(CreditNoteList.this, SalesmanMakePayment.class);
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
        Cursor cursor = sqLiteHandler.getCreditNoteFlag(chemistId,invoice_flag);
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            empty_view.setVisibility(View.VISIBLE);
            // buttonMakePayment.setVisibility(View.GONE);
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
                    String invoice_flag = cursor.getString(15);

                    // if(invoice_flag=="0"||invoice_flag.equalsIgnoreCase("0")||invoice_flag.equals("0")){
                    m_pendingbills m_pendingbills = new m_pendingbills(invoiceNo, invoiceDate,
                            Integer.valueOf(totalItems), billAmount, paymentReceived, balanceAmount, invoiceId,
                            grandTotal, totalDiscount, ledgerBalance);

                    posts.add(m_pendingbills);

                    //  }

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
            totalAmount = totalAmount + Integer.valueOf(post.get(i).getBalanceamt());

        }
        return totalAmount;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}
