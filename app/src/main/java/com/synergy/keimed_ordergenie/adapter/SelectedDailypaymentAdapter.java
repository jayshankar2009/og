package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_pendingbills;
import com.synergy.keimed_ordergenie.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SelectedDailypaymentAdapter extends RecyclerView.Adapter<SelectedDailypaymentAdapter.MyViewHolder> {

    private Context context;
    private List<m_pendingbills> paymentsList;
    private String time,cash="Cash",cheque="Cheque";


    public class MyViewHolder extends RecyclerView.ViewHolder {
       // private LinearLayout linearLayoutItems;
        public TextView textview_invoice,textview_chemistname,textview_flagstatus,label_chequeno,label_chequedate,label_bankname,textview_paymentdate,textview_chequedate,textview_chequeno,textview_bankname, textview_amountreceived, text_discount_per, textview_paymentmode;
        public ImageView payment_modecolor;
        public MyViewHolder(View view) {
            super(view);
            //linearLayoutItems = (LinearLayout) view.findViewById(R.id.linear_items_payment_list);
            textview_bankname = (TextView) view.findViewById(R.id.row_bankname);//row_bankname
            textview_flagstatus = (TextView) view.findViewById(R.id.textView7);//row_bankname
            textview_paymentdate = (TextView) view.findViewById(R.id.orderDatee); //payment date
            textview_paymentmode = (TextView) view.findViewById(R.id.orderamount); //payment mode
            //textview_chemistid = (TextView) view.findViewById(R.id.h_Cust_Name); //chemist id
            textview_chequeno = (TextView) view.findViewById(R.id.row_chequeno); //cheque no
            textview_chequedate = (TextView) view.findViewById(R.id.row_chequedate); //cheque date
            textview_amountreceived = (TextView) view.findViewById(R.id.order_Id); //amount received
            label_chequeno = (TextView) view.findViewById(R.id.h_invoicedate); //amount received
            label_chequedate = (TextView) view.findViewById(R.id.h_invoiceamount); //amount received
            label_bankname = (TextView) view.findViewById(R.id.h_invoice_Id); //amount received
            label_bankname = (TextView) view.findViewById(R.id.h_invoice_Id); //amount received
            textview_chemistname = (TextView) view.findViewById(R.id.Cust_Name); //chemist name
            textview_invoice = (TextView) view.findViewById(R.id.invoicedata); //chemist name
            payment_modecolor = (ImageView) view.findViewById(R.id.billStatus); //amount received
//            rupees_text = (TextView) view.findViewById(R.id.rupees_text);
//            text_invoicetime = (TextView) view.findViewById(R.id.text_invoicetime);
//            text_no_of_days = (TextView) view.findViewById(R.id.txt_days);
//            text_discount_amt = (TextView) view.findViewById(R.id.tx_discount);
//            text_discount_per = (TextView) view.findViewById(R.id.tx_discount_per);
//************



            //**********

        }
    }

    public SelectedDailypaymentAdapter(Context context, List<m_pendingbills> paymentsList) {
        this.context = context;
        this.paymentsList = paymentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dailycollectionreport, parent, false);
       // View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_items_dailypayment_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        m_pendingbills mm_pendingbills = paymentsList.get(position);

//        if (Integer.valueOf(mm_pendingbills.getBalanceamt()) == 0) {
//            holder.invoiceStatus.setText("Completely Paid");
//            holder.invoiceStatus.setVisibility(View.VISIBLE);
//            holder.invoiceStatus.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
//            holder.linearLayoutItems.setBackgroundColor(context.getResources().getColor(R.color.green_dshade));
//        } else if (Integer.valueOf(mm_pendingbills.getBalanceamt()) < Integer.valueOf(mm_pendingbills.getBillamount()) &&
//                Integer.valueOf(mm_pendingbills.getBalanceamt()) > 0) {
//            holder.invoiceStatus.setText("Partially Paid");
//            holder.invoiceStatus.setVisibility(View.VISIBLE);
//            holder.invoiceStatus.setTextColor(context.getResources().getColor(R.color.dark_orang));
//            holder.linearLayoutItems.setBackgroundColor(Color.WHITE);
//        } else {
//            holder.linearLayoutItems.setBackgroundColor(Color.WHITE);
//            holder.invoiceStatus.setVisibility(View.GONE);
        // }
        holder.textview_paymentmode.setText(mm_pendingbills.getInvoiceno());
        holder.textview_paymentdate.setText(mm_pendingbills.getInvoicedate());
       // holder.textview_chemistid.setText("Chemist Id: "+String.valueOf(mm_pendingbills.getTotalitems())+"-");
      //  holder.textview_flagstatus.setText("Status: "+String.valueOf(mm_pendingbills.getBalanceamt()));
        holder.textview_chemistname.setText(mm_pendingbills.getLedgerBalance());
        holder.textview_invoice.setText(mm_pendingbills.getGrandTotal());
        holder.textview_amountreceived.setText(mm_pendingbills.getPaymentreceived());
        if (mm_pendingbills.getInvoiceno().equalsIgnoreCase(cash)|| mm_pendingbills.getInvoiceno() == "CASH")
        {
            holder.textview_bankname.setVisibility(View.GONE);
            holder.textview_chequeno.setVisibility(View.GONE);
            holder.textview_chequedate.setVisibility(View.GONE);
            holder.label_bankname.setVisibility(View.GONE);
            holder.label_chequedate.setVisibility(View.GONE);
            holder.label_chequeno.setVisibility(View.GONE);
            //holder.payment_modecolor.setImageDrawable("#2334344");
        }
        else{

            holder.textview_bankname.setVisibility(View.VISIBLE);
            holder.textview_chequeno.setVisibility(View.VISIBLE);
            holder.textview_chequedate.setVisibility(View.VISIBLE);
            holder.label_bankname.setVisibility(View.VISIBLE);
            holder.label_chequedate.setVisibility(View.VISIBLE);
            holder.label_chequeno.setVisibility(View.VISIBLE);
            holder.textview_bankname.setText(mm_pendingbills.getTotalDiscount());
            holder.textview_chequeno.setText(mm_pendingbills.getInvoiceID());
            if(mm_pendingbills.getBillamount()!=null&&mm_pendingbills.getBillamount().length()>0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                String str = null;

                try {

                    date = sdf.parse(mm_pendingbills.getBillamount());
                    str = dateFormat.format(date);
                    holder.textview_chequedate.setText(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            }

//        if (order_list.getType() == -99) {
//            /*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));*/
//
//
//            if (posts_s.get(position).getStatus() == 0) {
//
//                order_Id.setTextColor(Color.parseColor("#4caf50"));
//            }
       // holder.rupees_text.setText("Balance: Rs." + mm_pendingbills.getBalanceamt());


        /* Get Day Count */
//        if (Utility.convertStringIntoDate(Constant.currentDate) != null && Utility.convertStringIntoDate(mm_pendingbills.getInvoicedate()) != null) {
//            long dayCount = Utility.convertStringIntoDate(Constant.currentDate).getTime() - Utility.convertStringIntoDate(mm_pendingbills.getInvoicedate()).getTime();
//            holder.text_no_of_days.setText("Days: " + TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS));
//        } else {
//            holder.text_no_of_days.setText("Days: 0");
//        }


        if (mm_pendingbills.getInvoicedate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date d1 = sdf.parse(mm_pendingbills.getInvoicedate());
                final String OLD_FORMAT = "yyyyMMdd";
                sdf.applyPattern(OLD_FORMAT);
                String newdate = sdf.format(d1);
                Log.d("timepayment11", d1.toString());
                Log.d("timepayment12", newdate);
                Log.d("timepayment13", String.valueOf(d1.getTime()));
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                time = formatter.format(new Date(d1.getTime()));
                Log.d("timepayment14", time);
              } catch (ParseException e) {
                e.printStackTrace();
            }
        }
       // holder.text_invoicetime.setText("Coleection Time: "+time);
    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }
}
