package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.interfaces.SelectedInvoice;
import com.synergy.keimed_ordergenie.model.m_pendingbills;
import com.synergy.keimed_ordergenie.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 04-12-2017.
 */

public class MakePaymentListAdapter extends RecyclerView.Adapter<MakePaymentListAdapter.MyViewHolder> {

    private Context context;
    private List<m_pendingbills> paymentList;
    private String time;
    float dayNum,numDivide;

    int mchekedSum =0;
    String restoredText;
    boolean chkClickable=false;
    SharedPreferences prefs;
    // ArrayList<String> dateArray;
    String txtChecqDate,strChqAvrg;
    TextView txtAverage;
    ArrayList<String> dateArray;
    ArrayList<Float> sumOfArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_ledger_balance,text_discount_amt,text_grant_total,text_discount_per,text_no_of_days,date_textview, textview_invoicenum, textview_time, ruppees_textview,balance_textview;
        CheckBox txtcheckBox;
        public MyViewHolder(View view) {
            super(view);
            prefs = context.getSharedPreferences("OrderGenieSha",context.MODE_PRIVATE);

            txtcheckBox = (CheckBox) view.findViewById(R.id.checkBox);
            date_textview = (TextView) view.findViewById(R.id.date_textview);
            textview_invoicenum = (TextView) view.findViewById(R.id.txt_invoice);
            textview_time = (TextView) view.findViewById(R.id.textview_time);
            ruppees_textview = (TextView) view.findViewById(R.id.ruppees_textview);
            balance_textview = (TextView) view.findViewById(R.id.ruppees_textview1);
            text_no_of_days=(TextView)view.findViewById(R.id.txt_days);
            text_discount_amt=(TextView)view.findViewById(R.id.tx_discount);
            text_discount_per=(TextView)view.findViewById(R.id.tx_discount_per);
            text_grant_total=(TextView)view.findViewById(R.id.tx_grant);
            text_ledger_balance=(TextView)view.findViewById(R.id.txt_ledgerBalance);
        }
    }

    public MakePaymentListAdapter(Context context, List<m_pendingbills> paymentList, TextView txtAverage) {
        this.context = context;
        this.paymentList = paymentList;
        Constant.selectedInvoice = new ArrayList<>();
        dateArray = new ArrayList<>();
        sumOfArrayList = new ArrayList<>();
        // this.dateArray = dateArray;
      /*  this.txtChecqDate = txtChecqDate;*/
        this.txtAverage=txtAverage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.make_paymentlist_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final m_pendingbills dpaymentListmodal = paymentList.get(position);
        final SelectedInvoice selectedInvoice = (SelectedInvoice) context;
        final int itemPosition = holder.getAdapterPosition();

        restoredText = prefs.getString("sharedDate", null);
        holder.date_textview.setText(parseDateToddMMyyyy(dpaymentListmodal.getInvoicedate()));
        holder.textview_invoicenum.setText("INVOICE NO: " + dpaymentListmodal.getInvoiceno());
        holder.ruppees_textview.setText("Balance: Rs. " + String.valueOf(dpaymentListmodal.getBalanceamt()));
        holder.text_discount_amt.setText("Rs. " + String.valueOf(dpaymentListmodal.getBillamount()));
        holder.text_discount_per.setText("Rs. " + String.valueOf(dpaymentListmodal.getTotalDiscount()));
        holder.text_grant_total.setText("Rs. " + String.valueOf(dpaymentListmodal.getGrandTotal()));
        holder.text_ledger_balance.setText("Ledger Balance Rs. " + String.valueOf(dpaymentListmodal.getLedgerBalance()));

        /* Get Day Count */
        if (Utility.convertStringIntoDate(Constant.currentDate) != null && Utility.convertStringIntoDate(dpaymentListmodal.getInvoicedate()) != null) {
            long dayCount = Utility.convertStringIntoDate(Constant.currentDate).getTime() - Utility.convertStringIntoDate(dpaymentListmodal.getInvoicedate()).getTime();
            holder.text_no_of_days.setText("Days: " + TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS));
        } else {
            holder.text_no_of_days.setText("Days: 0");
        }

        if (dpaymentListmodal.getInvoicedate() != null) {
            //commented by apurva parse error date 28/8/2018
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Log.d("date","date:"+dpaymentListmodal.getInvoicedate());
            try {

                Date d1 = sdf.parse(dpaymentListmodal.getInvoicedate());
                final String OLD_FORMAT = "yyyyMMdd";
                sdf.applyPattern(OLD_FORMAT);
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                time = formatter.format(new Date(d1.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // holder.textview_time.setText(time);
        holder.txtcheckBox.setChecked(dpaymentListmodal.isSelected());
        dateArray.clear();
        sumOfArrayList.clear();
        if(chkClickable==true) {
            chkClickable=false;
            if(restoredText!=null) {

                for (int i = 0; i < paymentList.size(); i++) {
                    m_pendingbills m_pend = paymentList.get(i);
                    if (m_pend.isSelected()) {
                        dateArray.add(m_pend.getInvoicedate());
                      //  System.out.println("Number of Days between dates 2321: " + dateArray);
                     //   System.out.println("Number of Days between dates 2321:Size " + dateArray.size());
                        //  dateArray.add();
                        //     Toast.makeText(this, "Select invoice second--"+m_pend.getInvoicedate().length(), Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(this, "Select invoice first"+m_pend.getInvoicedate().length(), Toast.LENGTH_SHORT).show();


                    }
                }

                for (int j = 0; j < dateArray.size(); j++) {
                    //   saveSize = new int[dateArray.size()];
                    //     sumOfArray = new String[dateArray.size()];
                    System.out.println("Number of Days between dates 23: " + dateArray);
                    try {

                        long dayCount = Utility.convertStringIntoDate(restoredText).getTime() - Utility.convertStringIntoDate(dateArray.get(j)).getTime();

                        dayNum = TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS);
                        sumOfArrayList.add(dayNum);


                        //       System.out.println("Number of Days between dayNum232-j: " + sumOfArrayList);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                float sumOfDate= 0.0f;
               try {
                   for (int k = 0; k < sumOfArrayList.size(); k++)
                   {
                       sumOfDate += sumOfArrayList.get(k);
                   }
                   numDivide = sumOfDate / sumOfArrayList.size();
                   String strDate = String.valueOf(numDivide);
                   String strReplace = strDate.substring(0, strDate.indexOf("."));
                   txtAverage.setText("Avg Days :"+String.valueOf(strReplace));
               }
               catch (Exception e)
               {

               }


            }

        }
        /* ItemView Click */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    chkClickable = true;
                    Log.d("position", String.valueOf(itemPosition));
                    if (dpaymentListmodal.isSelected()) {
                        //    Log.i("Check Balance","Check Balance1"+dpaymentListmodal.getInvoicedate());
                        //     dateArray.add(dpaymentListmodal.getInvoicedate());
                        Constant.balanceAmount = Constant.balanceAmount + Integer.valueOf(dpaymentListmodal.getBalanceamt());
                        if (Constant.balanceAmount <= 0) {
                            selectedInvoice.totalBalance(0);
                         //   Log.d("loop", "first if");
                        } else
                        {
                          //  Log.d("loop", "first else");
                            selectedInvoice.totalBalance(Constant.balanceAmount);
                            holder.balance_textview.setVisibility(View.GONE);
                            dpaymentListmodal.setBalanceAmountPayload(String.valueOf(Constant.balanceAmount));
                        }
                        dpaymentListmodal.setSelected(false);
                        Constant.selectedInvoice.remove(paymentList.get(itemPosition));
                    } else {
                        //   Log.i("Check Balance","Check Balance2");
                        if (Constant.ableToSelectInvoice <= 0) {
                         //   Log.i("Check Balance","Check Balance3");
                            Toast.makeText(context, "Balance not available", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Constant.balanceAmount < Integer.valueOf(dpaymentListmodal.getBalanceamt())) {
                                //    Log.i("Check Balance","Check Balance4");
                                Constant.balanceAmount = Constant.balanceAmount - Integer.valueOf(dpaymentListmodal.getBalanceamt());
                                if (Constant.balanceAmount <= 0) {
                                //    Log.i("Check Balance","Check Balance4");
                                  //  Log.d("loop", "second if");
                                    selectedInvoice.totalBalance(0);
                                    holder.balance_textview.setText("Balance: " + String.valueOf(Constant.balanceAmount).replace("-", ""));
                                    holder.balance_textview.setVisibility(View.VISIBLE);
                                    dpaymentListmodal.setBalanceAmountPayload(String.valueOf(Constant.balanceAmount).replace("-", ""));
                                }
                            } else {

                              //  Log.d("loop", "second else");
                                //holder.balance_textview.setVisibility(View.GONE);
                                Constant.balanceAmount = Constant.balanceAmount - Integer.valueOf(dpaymentListmodal.getBalanceamt());
                                selectedInvoice.totalBalance(Constant.balanceAmount);
                                dpaymentListmodal.setBalanceAmountPayload("0");
                            }
                            dpaymentListmodal.setSelected(true);
                            Constant.selectedInvoice.add(paymentList.get(itemPosition));
                            }
                    }

                    notifyDataSetChanged();
                    holder.txtcheckBox.setChecked(dpaymentListmodal.isSelected());
                    Constant.ableToSelectInvoice = Constant.balanceAmount;
                }catch (Exception e)
                {
                    Log.d("Catch",e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return paymentList.size();
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}

