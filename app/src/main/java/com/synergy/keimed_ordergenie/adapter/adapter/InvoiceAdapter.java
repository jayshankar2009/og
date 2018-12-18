package com.synergy.keimed_ordergenie.adapter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_invoice_item;

import java.util.List;

/**
 * Created by admin on 5/20/2017.
 */
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    private List<m_invoice_item> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, pack, qty, mrp;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.H_Item_Name);
            pack = (TextView) view.findViewById(R.id.H_Pack);
            qty = (TextView) view.findViewById(R.id.H_Qty);
            mrp = (TextView) view.findViewById(R.id.H_mrp);
        }
    }


    public InvoiceAdapter(List<m_invoice_item> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adpter_invoice_details_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        m_invoice_item movie = moviesList.get(position);
        holder.title.setText(movie.getProduct_Desc());
       // holder.pack.setText(movie.getPacksize());
        holder.pack.setText((movie.getPacksize() == null) ? "NA" : movie.getPacksize());
        holder.qty.setText(movie.getQty());
        holder.mrp.setText(movie.getMRP());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
