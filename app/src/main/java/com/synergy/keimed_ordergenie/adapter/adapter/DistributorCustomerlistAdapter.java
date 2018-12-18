package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.DistributorCustomerDetails;
import com.synergy.keimed_ordergenie.model.m_distributor_customer;

import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 02-12-2017.
 */

public class DistributorCustomerlistAdapter extends RecyclerView.Adapter<DistributorCustomerlistAdapter.MyViewHolder> {

    private List<m_distributor_customer> pendinglist;

    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_stockist_name,txt_address;
        public TextView textView;
        ImageView pendingbill_yes;
        public MyViewHolder(View view) {
            super(view);

            //chemistname_text,circular_textview,chemistname_textview,timing_textview,items_textview,prize_textview
            pendingbill_yes = (ImageView)view.findViewById(R.id.pendingbill_yes);

            textView = (TextView) view.findViewById(R.id.stockist_logo);
            txt_stockist_name = (TextView) view.findViewById(R.id.stockist_name);
            txt_address = (TextView) view.findViewById(R.id.stickist_add);


        /*    timing_textview = (TextView) view.findViewById(R.id.timing_textview);
            items_textview = (TextView) view.findViewById(R.id.items_textview);
            prize_textview = (TextView) view.findViewById(R.id.prize_textview);*/

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("position",String.valueOf(getAdapterPosition()));
                    Intent intent = new Intent(context,DistributorCustomerDetails.class);
                    intent.putExtra("CustomerCode",pendinglist.get(getAdapterPosition()).getClient_Code());
                    intent.putExtra("CustomerAddress",pendinglist.get(getAdapterPosition()).getClient_Address());
                    intent.putExtra("CustomerPhone",pendinglist.get(getAdapterPosition()).getClient_Contact());
                    intent.putExtra("CustomerEmail",pendinglist.get(getAdapterPosition()).getClient_Email());
                    intent.putExtra("CustomerName",pendinglist.get(getAdapterPosition()).getClient_LegalName());

                    intent.putExtra("orderStatus",pendinglist.get(getAdapterPosition()).getOrderStatus());
                    context.startActivity(intent);
                }
            });

        }
    }


    public DistributorCustomerlistAdapter(List<m_distributor_customer> pendinglist, Context context) {
        this.context = context;
        this.pendinglist = pendinglist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adpter_distributor_customer_list, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            m_distributor_customer pendingListModal = pendinglist.get(position);
            String customer_name = pendingListModal.getClient_LegalName();
            String parts[] = customer_name.split(" ");

            Random r = new Random();
            int red=r.nextInt(255 - 0 + 1)+0;
            int green=r.nextInt(255 - 0 + 1)+0;
            int blue=r.nextInt(255 - 0 + 1)+0;
            GradientDrawable draw = new GradientDrawable();
            draw.setShape(GradientDrawable.OVAL);
            draw.setColor(Color.rgb(red,green,blue));
            holder.textView.setBackground(draw);
            holder.txt_address.setText((pendingListModal.getCityName() == null) ? "NA" : pendingListModal.getCityName());
            holder.txt_stockist_name.setText((pendingListModal.getClient_LegalName() == null||pendingListModal.getClient_LegalName().equals("")) ? "NA" : pendingListModal.getClient_LegalName());




            if(pendingListModal.getPayment_Pending_status().equals("Yes"))
            {
                holder.pendingbill_yes.setVisibility(View.VISIBLE);

            }else {

                holder.pendingbill_yes.setVisibility(View.GONE);

            }

            if(parts.length == 1) {

               // holder.textView.setText((String.valueOf(part1.charAt(0) + (String.valueOf(part2.charAt(0))))));

                String part1 = parts[0];
                holder.textView.setText(String.valueOf(part1.charAt(0)));

            }else if(parts.length>1){

                String part1 = parts[0];
                String part2 = parts[1];


                holder.textView.setText(String.valueOf(part1.charAt(0)) + String.valueOf(part2.charAt(0)));

            }


        }
        catch (Exception e)
        {
            Log.e("exception",e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return pendinglist.size();
    }

}

