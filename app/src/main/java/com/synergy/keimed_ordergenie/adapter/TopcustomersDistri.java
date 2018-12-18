package com.synergy.keimed_ordergenie.adapter;

/**
 * Created by Admin on 23-12-2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.TopSkuInfo;

import java.util.List;

/**
 * Created by admin on 11/29/2017.
 */

public class TopcustomersDistri extends RecyclerView.Adapter<TopcustomersDistri.MyViewHolder> {

    private List<TopSkuInfo> topcustomersList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView custt_txtproduct_name, product_comp, product_pack,custt_txtproduct_ptr,product_mrp;

        public MyViewHolder(View view) {
            super(view);
            custt_txtproduct_name = (TextView) view.findViewById(R.id.custt_txtproduct_name);
            custt_txtproduct_ptr = (TextView) view.findViewById(R.id.custt_txtproduct_ptr);
        }
    }


    public TopcustomersDistri(List<TopSkuInfo> topcustomersList) {
        this.topcustomersList = topcustomersList;
    }

    @Override
    public TopcustomersDistri.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topcustomers_row_products, parent, false);



        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(),"click",Toast.LENGTH_SHORT).show();


            }
        });

        //movie_list_row

        return new TopcustomersDistri.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopcustomersDistri.MyViewHolder holder, int position) {
        TopSkuInfo doctors = topcustomersList.get(position);

        holder.custt_txtproduct_name.setText(doctors.getProduct_name());
        holder.custt_txtproduct_ptr.setText(String.valueOf(doctors.getPtr()));

    }

    @Override
    public int getItemCount() {
        return topcustomersList.size();
    }
}

