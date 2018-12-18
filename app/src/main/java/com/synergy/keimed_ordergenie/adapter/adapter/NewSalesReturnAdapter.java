package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_return_add_product;

import java.util.List;

/**
 * Created by Admin on 30-01-2018.
 */

public class NewSalesReturnAdapter extends RecyclerView.Adapter<NewSalesReturnAdapter.MyViewHolder> {


    List<m_return_add_product> salesReturnList;
    Context context;

    public NewSalesReturnAdapter(List<m_return_add_product> salesReturnList, Context context) {

        this.salesReturnList = salesReturnList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,product_qty,product_batchno,product_mfg_date;


        LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);

            product_name = (TextView) view.findViewById(R.id.tx_product_name);
            product_qty = (TextView) view.findViewById(R.id.tx_qty);
            product_batchno = (TextView) view.findViewById(R.id.tx_batchno);
            product_mfg_date = (TextView) view.findViewById(R.id.mfg_date);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.return_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {

            m_return_add_product distributorUsersModal = salesReturnList.get(position);

            holder.product_name.setText(distributorUsersModal.getProduct_Desc());
            holder.product_batchno.setText(distributorUsersModal.getProduct_batchno());
            holder.product_mfg_date.setText(distributorUsersModal.getProduct_expiry());
            holder.product_qty.setText(distributorUsersModal.getProduct_qty());

        }catch (Exception e)
        {

        }

    }

    @Override
    public int getItemCount() {
        return salesReturnList.size();
    }
}
