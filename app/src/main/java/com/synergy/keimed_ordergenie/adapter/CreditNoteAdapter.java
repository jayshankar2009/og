package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CreditNoteAdapter extends RecyclerView.Adapter<CreditNoteAdapter.MyViewHolder> {

    private Context context;
    private List<m_pendingbills> paymentsList;
    private String time;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayoutItems;
        public TextView text_ledger_balance,text_discount_amt, text_grant_total, text_discount_per, text_no_of_days, invoiceStatus, invoicenumber_text, date_text, textview_items, rupees_text, text_invoicetime;

        public MyViewHolder(View view) {
            super(view);
            linearLayoutItems = (LinearLayout) view.findViewById(R.id.linear_items_payment_list);
            invoiceStatus = (TextView) view.findViewById(R.id.txt_invoice_status_payment_list);
            date_text = (TextView) view.findViewById(R.id.date_text);
            invoicenumber_text = (TextView) view.findViewById(R.id.invoicenumber_text);
            textview_items = (TextView) view.findViewById(R.id.textview_items);
            rupees_text = (TextView) view.findViewById(R.id.rupees_text);
            text_invoicetime = (TextView) view.findViewById(R.id.text_invoicetime);
            text_no_of_days = (TextView) view.findViewById(R.id.txt_days);
            text_discount_amt = (TextView) view.findViewById(R.id.tx_discount);
            text_discount_per = (TextView) view.findViewById(R.id.tx_discount_per);
            text_grant_total = (TextView) view.findViewById(R.id.tx_grant);
            text_ledger_balance = (TextView) view.findViewById(R.id.ledgerBalance_text);

        }
    }

    public CreditNoteAdapter(Context context, List<m_pendingbills> paymentsList) {
        this.context = context;
        this.paymentsList = paymentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.creditnote_list_row, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        m_pendingbills mm_pendingbills = paymentsList.get(position);

        if (Integer.valueOf(mm_pendingbills.getBalanceamt()) == 0) {
            holder.invoiceStatus.setText("Payment Collected");
            // holder.invoiceStatus.setText("Completely Paid");
            holder.invoiceStatus.setVisibility(View.VISIBLE);
            holder.invoiceStatus.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.linearLayoutItems.setBackgroundColor(context.getResources().getColor(R.color.green_dshade));
        } else if (Integer.valueOf(mm_pendingbills.getBalanceamt()) < Integer.valueOf(mm_pendingbills.getBillamount()) &&
                Integer.valueOf(mm_pendingbills.getBalanceamt()) > 0) {
            holder.invoiceStatus.setText("Partially Paid");
            holder.invoiceStatus.setVisibility(View.VISIBLE);
            holder.invoiceStatus.setTextColor(context.getResources().getColor(R.color.dark_orang));
            holder.linearLayoutItems.setBackgroundColor(Color.WHITE);
        } else {
            holder.linearLayoutItems.setBackgroundColor(Color.WHITE);
            holder.invoiceStatus.setVisibility(View.GONE);
        }

        holder.invoicenumber_text.setText(mm_pendingbills.getInvoiceno());
        holder.rupees_text.setText("Balance: Rs." + mm_pendingbills.getBalanceamt());
        holder.date_text.setText(mm_pendingbills.getInvoicedate());
        holder.textview_items.setText("Items: " + String.valueOf(mm_pendingbills.getTotalitems()));
        holder.text_discount_amt.setText(mm_pendingbills.getBillamount());
        holder.text_discount_per.setText(mm_pendingbills.getTotalDiscount());
        holder.text_grant_total.setText(mm_pendingbills.getGrandTotal());
        holder.text_ledger_balance.setText("Ledger Balance: Rs."+ mm_pendingbills.getLedgerBalance());

        /* Get Day Count */
        if (Utility.convertStringIntoDate(Constant.currentDate) != null && Utility.convertStringIntoDate(mm_pendingbills.getInvoicedate()) != null) {
            long dayCount = Utility.convertStringIntoDate(Constant.currentDate).getTime() - Utility.convertStringIntoDate(mm_pendingbills.getInvoicedate()).getTime();
            holder.text_no_of_days.setText("Days: " + TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS));
        } else {
            holder.text_no_of_days.setText("Days: 0");
        }


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
        holder.text_invoicetime.setText(time);
    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }
}
