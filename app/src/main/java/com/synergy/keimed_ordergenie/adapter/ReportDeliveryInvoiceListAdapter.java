package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.SelectedInvoiceDelivery;
import com.synergy.keimed_ordergenie.model.m_report_delivery_invoice_list;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportDeliveryInvoiceListAdapter extends RecyclerView.Adapter<ReportDeliveryInvoiceListAdapter.MyViewHolder> {

    private Context context;
    private List<m_report_delivery_invoice_list> paymentList;
    private String time;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_chemist_id,text_del_status,text_discount_amt,text_statuss,text_grant_total,text_total_boxes,text_status,text_total_items,date_textview, textview_invoicenum, textview_time, ruppees_textview,text_total_packets,tx_invoice_amount;
        LinearLayout linearLayout;
        LinearLayout linearLayout_row;
        TextView textView_delivery_amt,textview_clientname;
       // CheckBox txtcheckBox;
        ImageView img_status;
        public MyViewHolder(View view) {
            super(view);
           // txtcheckBox = (CheckBox) view.findViewById(R.id.checkBox);
            date_textview = (TextView) view.findViewById(R.id.date_textview);
            textview_invoicenum = (TextView) view.findViewById(R.id.txt_invoice);
            linearLayout=(LinearLayout)view.findViewById(R.id.layout_checkbox);
            linearLayout_row=(LinearLayout)view.findViewById(R.id.row);
            //  textview_time = (TextView) view.findViewById(R.id.textview_time);
            ruppees_textview = (TextView) view.findViewById(R.id.ruppees_textview);
            text_chemist_id = (TextView) view.findViewById(R.id.tx_chemist_id);
            img_status=(ImageView)view.findViewById(R.id.billStatus);
            tx_invoice_amount=(TextView)view.findViewById(R.id.text_invoice_amt);
            text_total_items=(TextView)view.findViewById(R.id.tx_no_of_items);
            text_total_packets=(TextView)view.findViewById(R.id.tx_no_of_packets);
            text_total_boxes=(TextView)view.findViewById(R.id.tx_no_of_box);
            text_del_status=(TextView)view.findViewById(R.id.txt_status);
            text_statuss=(TextView)view.findViewById(R.id.txt_statuss);
            textView_delivery_amt = (TextView) view.findViewById(R.id.text_delivery_amt);
            textview_clientname = (TextView) view.findViewById(R.id.txt_clientname);
           // txtcheckBox.setVisibility(View.GONE);
        }
    }

    public ReportDeliveryInvoiceListAdapter(Context context, List<m_report_delivery_invoice_list> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
        Constant.selectedDeliveryInvoice = new ArrayList<>();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deliveryinvoice_row, parent, false);
       // View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_invoicelist_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final m_report_delivery_invoice_list dpaymentListmodal = paymentList.get(position);
        final SelectedInvoiceDelivery selectedInvoice = (SelectedInvoiceDelivery) context;
        final int itemPosition = holder.getAdapterPosition();
        Log.d("DATA",dpaymentListmodal.getInvoiceNo()+"--"+dpaymentListmodal.getTotalAmount()+"--"+dpaymentListmodal.getInvoiceNo()+"--"+dpaymentListmodal.getPackage_count());
       // holder.date_textview.setText(dpaymentListmodal.getDeliveryDate());
        holder.date_textview.setText(dpaymentListmodal.getInvoiceDate());
        holder.textview_invoicenum.setText("INVOICE NO: " + dpaymentListmodal.getInvoiceNo());
        Log.i("Delivery Date"," "+dpaymentListmodal.getDeliveryDate());
        // holder.ruppees_textview.setText("Balance: Rs. " + String.valueOf(dpaymentListmodal.getBalanceamt()));
        holder.text_total_packets.setText(String.valueOf(dpaymentListmodal.getPackage_count()));
        holder.text_total_items.setText(String.valueOf(dpaymentListmodal.getTotalItems()));
        holder.text_total_boxes.setText(String.valueOf(dpaymentListmodal.getBoxCount()));
        holder.text_chemist_id.setText(String.valueOf(dpaymentListmodal.getChemistId()));
        holder.text_statuss.setText(String.valueOf(dpaymentListmodal.getDescription()));
        holder.textView_delivery_amt.setText(dpaymentListmodal.getDeliveryDate());
        holder.textview_clientname.setText(dpaymentListmodal.getClient_Name());
       // holder.text_total_boxes.setText(String.valueOf(dpaymentListmodal.getBoxCount()));


      //  String name = dpaymentListmodal.getClient_Name();

     //   holder.textview_clientname.setText(name);

//        Log.d("prt_chemist_name12",name);
      /*  String deliveryDate = dpaymentListmodal.getDescription();

        if(deliveryDate.contains(" ")){
            String[] arrOfStr = deliveryDate.split(" ",0);

            String getDeliverydate = arrOfStr[0];

          //  Log.d("DeliveryDateinvoice",getDeliverydate);
            holder.textView_delivery_amt.setText(getDeliverydate);
        }*/


       // Log.d("descriptiondate12",);


       // Log.d("Flag",dpaymentListmodal.getDeliveryFlag());
        if (dpaymentListmodal.getDeliveryFlag().equals("1"))
        //   if (dpaymentListmodal.getDeliveryStatus().equals("1"))
        {
            holder.text_del_status.setText("Delivered");
            holder.text_del_status.setTextColor(Color.parseColor("#ec173e"));
        }
        else if(dpaymentListmodal.getDeliveryFlag().equals("2"))
        {
            holder.text_del_status.setText("Undelivered");
            holder.text_del_status.setTextColor(Color.parseColor("#4caf50"));
        }
        else if ((dpaymentListmodal.getDeliveryFlag().equals("0")))
        {
            holder.text_del_status.setText("Pending");
            holder.text_del_status.setTextColor(Color.parseColor("#757575"));
        }

        // holder.text_del_status.setText(String.valueOf(dpaymentListmodal.getDeliveryFlag()));
        holder.tx_invoice_amount.setText("Rs."+dpaymentListmodal.getTotalAmount());
        // holder.text_status.setText("Pending");

        /* Get Day Count */
//        if (Utility.convertStringIntoDate(Constant.currentDate) != null && Utility.convertStringIntoDate(dpaymentListmodal.getInvoicedate()) != null) {
//            long dayCount = Utility.convertStringIntoDate(Constant.currentDate).getTime() - Utility.convertStringIntoDate(dpaymentListmodal.getInvoicedate()).getTime();
//            holder.text_no_of_days.setText("Days: " + TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS));
//        } else {
//            holder.text_no_of_days.setText("Days: 0");
//        }

        if (dpaymentListmodal.getInvoiceDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date d1 = sdf.parse(dpaymentListmodal.getInvoiceDate());
                final String OLD_FORMAT = "yyyyMMdd";
                sdf.applyPattern(OLD_FORMAT);
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                time = formatter.format(new Date(d1.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //   holder.textview_time.setText(time);
      //  holder.txtcheckBox.setChecked(dpaymentListmodal.isSelected());
//        Log.d("DeliveryStatus",dpaymentListmodal.getDeliveryStatus());
//        Log.d("DeliveryFlag",dpaymentListmodal.getDeliveryFlag());
//        if (dpaymentListmodal.getDeliveryFlag().equals("1"))
//        // if (dpaymentListmodal.getDeliveryFlag().equals("Delivered"))
//        {
//            holder.txtcheckBox.setVisibility(View.GONE);
//            holder.linearLayout.setVisibility(View.GONE);
//        }

        /* ItemView Click */
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (dpaymentListmodal.isSelected()) {
//                    Constant.balanceAmountDelivery = Constant.balanceAmountDelivery + Integer.valueOf(dpaymentListmodal.getTotalAmount());
//                    if (Constant.balanceAmountDelivery <= 0) {
//                        selectedInvoice.totalBalance(0);
//                    } else {
//                        selectedInvoice.totalBalance(Constant.balanceAmountDelivery);
//                        //   holder.balance_textview.setVisibility(View.GONE);
//                        //  dpaymentListmodal.setBalanceAmountPayload(String.valueOf(Constant.balanceAmount));
//                    }
//                    dpaymentListmodal.setSelected(false);
//                    Constant.selectedDeliveryInvoice.remove(paymentList.get(itemPosition));
//                    Log.d("RemovedItems", String.valueOf(Constant.selectedDeliveryInvoice));
//                } else {
////                    if (Constant.ableToSelectInvoice <= 0) {
////                        Toast.makeText(context, "Balance not available", Toast.LENGTH_SHORT).show();
////                    }
//                    //else {
//                    if (Constant.balanceAmountDelivery < Integer.valueOf(dpaymentListmodal.getTotalAmount())) {
//                        Constant.balanceAmountDelivery = Constant.balanceAmountDelivery - Integer.valueOf(dpaymentListmodal.getTotalAmount());
//                        if (Constant.balanceAmountDelivery <= 0) {
//                            selectedInvoice.totalBalance(0);
//                            //  holder.balance_textview.setText("Balance: " + String.valueOf(Constant.balanceAmount).replace("-", ""));
//                            // holder.balance_textview.setVisibility(View.VISIBLE);
//
//                            // dpaymentListmodal.setBalanceAmountPayload(String.valueOf(Constant.balanceAmount).replace("-", ""));
//                        }
//                    } else {
//                        Constant.balanceAmountDelivery = Constant.balanceAmountDelivery - Integer.valueOf(dpaymentListmodal.getTotalAmount());
//                        selectedInvoice.totalBalance(Constant.balanceAmountDelivery);
//
//                        dpaymentListmodal.setBalanceAmountPayload("0");
//                    }
//                    dpaymentListmodal.setSelected(true);
//                    Constant.selectedDeliveryInvoice.add(paymentList.get(itemPosition));
//                    Log.d("SelectedItems", String.valueOf(Constant.selectedDeliveryInvoice));
//
//                    //  }
//                }
//
//                notifyDataSetChanged();
//                holder.txtcheckBox.setChecked(dpaymentListmodal.isSelected());
//                //Constant.ableToSelectInvoice = Constant.balanceAmountDelivery;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(paymentList!=null)
        {
            return paymentList.size();
        }
        else {
            return 0;

        }
    }

//    @Override
//    public int getItemCount() {
//        return paymentList.size();
//    }
}

