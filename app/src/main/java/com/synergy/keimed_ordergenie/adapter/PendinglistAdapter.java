package com.synergy.keimed_ordergenie.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.DistributorPendingBillDetails;
import com.synergy.keimed_ordergenie.model.PendingListModal;
import com.synergy.keimed_ordergenie.model.d_pending_line_items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 02-12-2017.
 */

public class PendinglistAdapter extends RecyclerView.Adapter<PendinglistAdapter.MyViewHolder> {

    private List<PendingListModal> pendinglist;
    char part1,part2;
    public List<d_pending_line_items> line_items=new ArrayList<d_pending_line_items>();

    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView chemistname_text,pending_textview, rs_textview, item_textview,date_textview,items_textview,prize_textview;

        LinearLayout linearLayout;
        private ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            //chemistname_text,circular_textview,chemistname_textview,timing_textview,items_textview,prize_textview

            linearLayout = (LinearLayout) view.findViewById(R.id.rowpendingbill);
            chemistname_text = (TextView) view.findViewById(R.id.chemistname_text);
            pending_textview = (TextView) view.findViewById(R.id.pending_textview);
            imageView = (ImageView) view.findViewById(R.id.image_icon);
            rs_textview = (TextView) view.findViewById(R.id.rs_textview);
            item_textview = (TextView) view.findViewById(R.id.item_textview);
            date_textview=(TextView)view.findViewById(R.id.item_date);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    line_items= pendinglist.get(getAdapterPosition()).getLine_items();
                    if (line_items.size()>0)
                    {
                        Intent intent = new Intent(context, DistributorPendingBillDetails.class);
                        intent.putExtra("ArrayList", (ArrayList<d_pending_line_items>) line_items);
                        intent.putExtra("cust_id", pendinglist.get(getAdapterPosition()).getCust_code());
                        intent.putExtra("cust_name", pendinglist.get(getAdapterPosition()).getCust_name());
                        context.startActivity(intent);
                    }
                }
            });
        /*    timing_textview = (TextView) view.findViewById(R.id.timing_textview);
            items_textview = (TextView) view.findViewById(R.id.items_textview);
            prize_textview = (TextView) view.findViewById(R.id.prize_textview);*/

        }
    }

    public PendinglistAdapter(List<PendingListModal> pendinglist,Context context) {
        this.context = context;
        this.pendinglist = pendinglist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pendinglist_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            //Log.d("pendinglistSize",String.valueOf(pendinglist.size()));
            PendingListModal pendingListModal = pendinglist.get(position);
          //  Log.d("pendinglist",String.valueOf(pendinglist.get(position).getCust_outstanding()));
            if (Integer.parseInt(pendinglist.get(position).getCust_outstanding())>0) {
                try {

                    String customer_name = pendingListModal.getCust_name();
                    String parts[] = customer_name.split(" ");

                    if (parts.length == 1) {
                        part1 = parts[0].charAt(0);
                        holder.pending_textview.setText(String.valueOf(part1));
                    } else {
                        if (parts.length > 1) {
                            part1 = parts[0].charAt(0);
                            part2 = parts[1].charAt(0);
                            holder.pending_textview.setText(String.valueOf(part1) + String.valueOf(part2));
                        }
                    }
                    // Log.d("outstanding", String.valueOf(pendingListModal.getCust_outstanding()));
//                    if (pendingListModal.getLine_items().size() == 0) {
//                        holder.imageView.setVisibility(View.GONE);
//                    } else {
//                        holder.imageView.setVisibility(View.VISIBLE);
//                    }
                    Random r = new Random();
                    int red = r.nextInt(255 - 0 + 1) + 0;
                    int green = r.nextInt(255 - 0 + 1) + 0;
                    int blue = r.nextInt(255 - 0 + 1) + 0;
                    GradientDrawable draw = new GradientDrawable();
                    draw.setShape(GradientDrawable.OVAL);
                    draw.setColor(Color.rgb(red, green, blue));
                    holder.pending_textview.setBackground(draw);
                    holder.chemistname_text.setText((pendingListModal.getCust_name() == null) ? "NA" : pendingListModal.getCust_name());
                    holder.rs_textview.setText((String.valueOf(pendingListModal.getCust_outstanding()) == null) ? "NA" : String.valueOf(pendingListModal.getCust_outstanding()));
                    holder.item_textview.setText((String.valueOf(pendingListModal.getLine_items().size()) == null) ? "NA" : String.valueOf(pendingListModal.getLine_items().size() + " Pending Bills"));


                    // holder.date_textview.setText((String.valueOf(pendingListModal.getInvoiceDate()) == null) ? "NA" : String.valueOf(pendingListModal.getInvoiceDate()));

                    //Log.d("sizependingbils", String.valueOf(pendingListModal.getLine_items().size()));

//            if (String.valueOf(pendingListModal.getLine_items().size()).equals("0")){
//
//                holder.linearLayout.setVisibility(View.GONE);
//
//            }else {
//
//                holder.linearLayout.setVisibility(View.VISIBLE);
//
//            }
                }catch (Exception e)
                {
                    Log.d("Exceppp",e.getMessage());
                }
            }
            else
            {
                Log.d("print","else block");
            }

        }
        catch (Exception e)
        {
        Log.d("Excep",e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return pendinglist.size();
    }
}

