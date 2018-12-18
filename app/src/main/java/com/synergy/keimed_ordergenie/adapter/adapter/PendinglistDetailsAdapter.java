package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.Invoice_details;
import com.synergy.keimed_ordergenie.model.PendingListModal;
import com.synergy.keimed_ordergenie.model.d_pending_line_items;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 02-12-2017.
 */

public class PendinglistDetailsAdapter extends RecyclerView.Adapter<PendinglistDetailsAdapter.MyViewHolder> {

    private List<PendingListModal> pendinglist;
    char part1,part2;
    List<d_pending_line_items> line_items=new ArrayList<d_pending_line_items>();
    public static final String STOCKIST_INVOICE_No = "invoice_id";
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_date,txt_itemcount,tx_pending_amt;
        private   ImageView img_icon;
        public MyViewHolder(View view) {
            super(view);

            txt_date = (TextView) view
                    .findViewById(R.id.tx_date);
            txt_itemcount = (TextView) view
                    .findViewById(R.id.tx_itemcount);
            tx_pending_amt = (TextView) view
                    .findViewById(R.id.tx_pending_amount);
            img_icon=(ImageView)view.findViewById(R.id.image_icon);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context, Invoice_details.class);
                //put invoice id
                intent.putExtra(STOCKIST_INVOICE_No, String.valueOf(line_items.get(getAdapterPosition()).getInvoiceid()));
                intent.putExtra("invoice_no",String.valueOf(line_items.get(getAdapterPosition()).getInvoiceno()));
                intent.putExtra("chemist_order_date",String.valueOf(line_items.get(getAdapterPosition()).getInvoicedate()));
                context.startActivity(intent);
                }
            });
        /*    timing_textview = (TextView) view.findViewById(R.id.timing_textview);
            items_textview = (TextView) view.findViewById(R.id.items_textview);
            prize_textview = (TextView) view.findViewById(R.id.prize_textview);*/

        }
    }

    public PendinglistDetailsAdapter(List<d_pending_line_items> line_items, Context context) {
        this.context = context;
        this.line_items = line_items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expand_pendingbillheadertems, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        try
        {
            final d_pending_line_items headerTitle = line_items.get(position);
            //  holder.pending_textview.setText(String.valueOf(part1));
            if (headerTitle.getPaymentreceived() > 0 && (headerTitle.getPaymentreceived() < headerTitle.getBalanceamt())) {
                holder.img_icon.setVisibility(View.VISIBLE);
            } else {
                holder.img_icon.setVisibility(View.INVISIBLE);
            }
            holder.txt_date.setTypeface(null, Typeface.BOLD);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy'T'HH:mm:ss.SSS'Z'");
            // String oldFormat= "dd-MMM-yy HH:mm:ss";
                if (headerTitle.getInvoicedate() != null)
                {
                    Date d1 = sdf.parse(headerTitle.getInvoicedate());
                    String d2 = sdf1.format(d1);
                    Log.d("date", d1.toString());
                    Log.d("d2", d2);
                    Log.d("datee", headerTitle.getInvoicedate());
                    holder.txt_date.setText(d2);
                }
                holder.txt_itemcount.setText(headerTitle.getTotalitems() + "Items");
                holder.tx_pending_amt.setText("Rs." + String.valueOf(headerTitle.getPaymentreceived()));
        }
        catch (Exception e)
        {
            Log.d("Excep", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return line_items.size();
    }
}

