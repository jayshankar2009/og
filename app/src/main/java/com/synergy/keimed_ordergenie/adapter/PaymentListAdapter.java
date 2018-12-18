package com.synergy.keimed_ordergenie.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.DistributorpaymentListmodal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 04-12-2017.
 */

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.MyViewHolder> {

    private List<DistributorpaymentListmodal> paymentList;
    String time;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_textview,mediconame_textview, textview_invoicenum, textview_time,ruppees_textview;
        ImageView img_paymentmode;

        public MyViewHolder(View view) {
            super(view);


            img_paymentmode = (ImageView) view.findViewById(R.id.img_paymentmode);
            date_textview = (TextView) view.findViewById(R.id.date_textview);
            mediconame_textview = (TextView) view.findViewById(R.id.mediconame_textview);
            textview_invoicenum = (TextView) view.findViewById(R.id.textview_invoicenum);
            textview_time = (TextView) view.findViewById(R.id.textview_time);
            ruppees_textview = (TextView) view.findViewById(R.id.ruppees_textview);




        }
    }


    public PaymentListAdapter(List<DistributorpaymentListmodal> paymentList) {
        this.paymentList = paymentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paymentlist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DistributorpaymentListmodal dpaymentListmodal = paymentList.get(position);

        holder.date_textview.setText(dpaymentListmodal.getPaymentDate());
        holder.mediconame_textview.setText(dpaymentListmodal.getCustName());
        holder.textview_invoicenum.setText(dpaymentListmodal.getInvoiceNo());

        holder.ruppees_textview.setText(String.valueOf(dpaymentListmodal.getInvoiceAmount()));

        if(dpaymentListmodal.getPaymentDate()!= null){

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
            try {
                Date d1 = sdf.parse(dpaymentListmodal.getPaymentDate());
                final String OLD_FORMAT = "yyyyMMdd";;
                sdf.applyPattern(OLD_FORMAT);
                String newdate=sdf.format(d1);
                Log.d("timepayment11",d1.toString());
                Log.d("timepayment12",newdate);
                Log.d("timepayment13", String.valueOf(d1.getTime()));
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                time=formatter.format(new Date(d1.getTime()));
                Log.d("timepayment14", time);


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        holder.textview_time.setText(time);


        if(dpaymentListmodal.getPaymentModeID().equals("3") )
        {
            // check payment--orange color
            holder.img_paymentmode.setImageResource(R.drawable.circle);

        }else if(dpaymentListmodal.getPaymentModeID().equals("0") || dpaymentListmodal.getPaymentModeID().equals("2") || dpaymentListmodal.getPaymentModeID().equals("5")){

            // cash payment---blue color

            holder.img_paymentmode.setImageResource(R.drawable.plentycircle);

        }

    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }
}

