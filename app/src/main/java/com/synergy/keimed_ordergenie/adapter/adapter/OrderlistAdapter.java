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
import com.synergy.keimed_ordergenie.activity.StockistOrderDetails;
import com.synergy.keimed_ordergenie.model.m_orderlist_distributor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Admin on 29-11-2017.
 */

public class OrderlistAdapter extends RecyclerView.Adapter<OrderlistAdapter.MyViewHolder> {

    private List<m_orderlist_distributor> orderList;
    private Context mcontext;
    private List<String> dateList=new ArrayList<String>();
    String time;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_textview, circular_textview, chemistname_textview,timing_textview,items_textview,prize_textview;

        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            //date_textview,circular_textview,chemistname_textview,timing_textview,items_textview,prize_textview
            date_textview = (TextView) view.findViewById(R.id.date_textview);
            chemistname_textview = (TextView) view.findViewById(R.id.chemistname_textview);
            circular_textview = (TextView) view.findViewById(R.id.circular_textview);
            imageView=(ImageView)view.findViewById(R.id.image_icon);

            timing_textview = (TextView) view.findViewById(R.id.timing_textview);
            items_textview = (TextView) view.findViewById(R.id.items_textview);
            prize_textview = (TextView) view.findViewById(R.id.prize_textview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, StockistOrderDetails.class);
                    Log.d("order_id","itemclicked");
                    intent.putExtra("order_id",orderList.get(getAdapterPosition()).getTransaction_No());
                    intent.putExtra("custom_id",orderList.get(getAdapterPosition()).getClient_Code());
                    intent.putExtra("item_count",String.valueOf(orderList.get(getAdapterPosition()).getItems()));
                    intent.putExtra("cust_name",orderList.get(getAdapterPosition()).getClient_LegalName());
                    intent.putExtra("cust_amount",String.valueOf(orderList.get(getAdapterPosition()).getAmount()));
                    intent.putExtra("doc_date",orderList.get(getAdapterPosition()).getDoc_Date());
                    intent.putExtra("status",String.valueOf(orderList.get(getAdapterPosition()).getStatus()));
                    mcontext.startActivity(intent);
                }
            });
        }
    }


    public OrderlistAdapter(Context context,List<m_orderlist_distributor> orderList) {
        this.orderList = orderList;
        this.mcontext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            m_orderlist_distributor orderlistmodal = orderList.get(position);
            holder.chemistname_textview.setText(orderlistmodal.getClient_LegalName());
            String customer_name=orderlistmodal.getClient_LegalName();
            String parts[]=customer_name.split(" ");
           // String part1=parts[0];
            // String part2=parts[1];
            if(parts.length == 1) {
                // holder.textView.setText((String.valueOf(part1.charAt(0) + (String.valueOf(part2.charAt(0))))));
                String part1 = parts[0];
                holder.circular_textview.setText(String.valueOf(part1.charAt(0)));

            }else if(parts.length>1){

                String part1 = parts[0];
                String part2 = parts[1];
                holder.circular_textview.setText(String.valueOf(part1.charAt(0)) + String.valueOf(part2.charAt(0)));
            }
            if (orderlistmodal.getStatus()==1)
            {
                //processing
            holder.imageView.setImageResource(R.drawable.circular_textview_drawable);
            }
            else if (orderlistmodal.getStatus()==3||orderlistmodal.getStatus()==4)
            {
                //completed
                holder.imageView.setImageResource(R.drawable.greencircle);
            }
            else if (orderlistmodal.getStatus()==0)
            {
                //submited
                holder.imageView.setImageResource(R.drawable.plentycircle);
            }
            else if (orderlistmodal.getStatus()==5)
            {
                //delivered
                holder.imageView.setImageResource(R.drawable.moderatecircle);
            }
            Double amount=(orderlistmodal.getAmount()== null) ? 0.0 : orderlistmodal.getAmount();
            int count=(orderlistmodal.getItems()<=0) ? 0 :orderlistmodal.getItems();
            holder.items_textview.setText(String.valueOf(count)+" Items");
            holder.prize_textview.setText("Rs."+String.valueOf(amount));
          //  holder.circular_textview.setText(String.valueOf(part1.charAt(0))+String.valueOf(part2.charAt(0)));
           if (orderlistmodal.getDoc_Date()!=null)
           {
               SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
               Date d1 = sdf.parse(orderlistmodal.getDoc_Date());
               final String OLD_FORMAT = "yyyyMMdd";;
               sdf.applyPattern(OLD_FORMAT);
               String newdate=sdf.format(d1);
               Log.d("datee",d1.toString());
               Log.d("newdate",newdate);
               Log.d("time", String.valueOf(d1.getTime()));
               SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
               time=formatter.format(new Date(d1.getTime()));
               Log.d("time11", time);
               if (dateList.contains(newdate))
               {
                   Log.d("datee1",d1.toString());
                   holder.date_textview.setVisibility(View.GONE);
               }
               else
               {
                   Log.d("datee2",d1.toString());
                  // dateList.clear();
                   dateList.add(newdate);
                   holder.date_textview.setText(orderlistmodal.getDoc_Date());

               }
           }

            Random r = new Random();
            int red=r.nextInt(255 - 0 + 1)+0;
            int green=r.nextInt(255 - 0 + 1)+0;
            int blue=r.nextInt(255 - 0 + 1)+0;
            GradientDrawable draw = new GradientDrawable();
            draw.setShape(GradientDrawable.OVAL);
            draw.setColor(Color.rgb(red,green,blue));
            holder.circular_textview.setBackground(draw);
           holder.timing_textview.setText(time);
        }
        catch (Exception e)
        {

        }

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

