package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_delivery_invoice_list;

import java.util.List;

/**
 * Created by Admin on 21-02-2018.
 */

public class SelectedDeliveryListAdapter extends RecyclerView.Adapter<SelectedDeliveryListAdapter.MyViewHolder> {

    private Context context;
    private List<m_delivery_invoice_list> paymentsList;
    private String time;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayoutItems;
        public TextView invoice_amt,invoicenumber_text;

        public MyViewHolder(View view) {
            super(view);
            invoicenumber_text = (TextView) view.findViewById(R.id.txt_invoice_no);
            invoice_amt = (TextView) view.findViewById(R.id.text_invoice_amt);
        }
    }

    public SelectedDeliveryListAdapter(Context context, List<m_delivery_invoice_list> paymentsList) {
        this.context = context;
        this.paymentsList = paymentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_invoicelist_selected_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        m_delivery_invoice_list mm_pendingbills = paymentsList.get(position);
        holder.invoicenumber_text.setText("INVOICE NO:"+mm_pendingbills.getInvoiceNo());
        holder.invoice_amt.setText("Invoice Amt: Rs." + mm_pendingbills.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }
}
