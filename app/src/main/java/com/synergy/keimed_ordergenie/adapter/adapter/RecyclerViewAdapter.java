package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.ItemObject;

import java.util.List;

/**
 * Created by Admin on 06-12-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> {
    private List<ItemObject> itemList;
    private Context context;


    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView date_textview,chemistname_textview,prize_textview,timing_textview,items_textview;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            date_textview = (TextView)itemView.findViewById(R.id.date_textview);
            chemistname_textview = (TextView)itemView.findViewById(R.id.chemistname_textview);
            prize_textview = (TextView)itemView.findViewById(R.id.prize_textview);
            timing_textview = (TextView)itemView.findViewById(R.id.timing_textview);
            items_textview = (TextView)itemView.findViewById(R.id.items_textview);
        }
        @Override
        public void onClick(View view) {

            Log.d("ClickToView","clk");
        }
    }


    public RecyclerViewAdapter(Context context, List<ItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }
    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_row, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.date_textview.setText(itemList.get(position).getInvoice_Date());
        holder.chemistname_textview.setText(itemList.get(position).getCustomer());
        holder.prize_textview.setText( itemList.get(position).getInvoice_Amt());
        holder.timing_textview.setText(itemList.get(position).getOrderNo());
    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
