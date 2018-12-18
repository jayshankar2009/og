package com.synergy.keimed_ordergenie.adapter;

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

public class TopSkuListAdapter extends RecyclerView.Adapter<TopSkuListAdapter.MyViewHolder> {

    private List<TopSkuInfo> doctorList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_title, product_comp, product_pack,product_ptr,product_mrp;

        public MyViewHolder(View view) {
            super(view);
            product_title = (TextView) view.findViewById(R.id.search_product_name);
            //   product_comp = (TextView) view.findViewById(R.id.product_company);
            //   product_pack = (TextView) view.findViewById(R.id.search_pack);
            product_ptr = (TextView) view.findViewById(R.id.product_ptr);
            //    product_mrp = (TextView) view.findViewById(R.id.search_product_mrp);

            // year = (TextView) view.findViewById(R.id.year);
        }
    }


    public TopSkuListAdapter(List<TopSkuInfo> doctorList) {
        this.doctorList = doctorList;
    }

    @Override
    public TopSkuListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topsku_row_products, parent, false);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(),"click",Toast.LENGTH_SHORT).show();


            }
        });

        //movie_list_row

        return new TopSkuListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopSkuListAdapter.MyViewHolder holder, int position) {
        TopSkuInfo doctors = doctorList.get(position);

        holder.product_title.setText(doctors.getProduct_name());
        //  holder.product_comp.setText(doctors.getCompany_name());
        //  holder.product_pack.setText(doctors.getPack_size());
        holder.product_ptr.setText(String.valueOf(doctors.getPtr()));
        // holder.product_mrp.setText(String.valueOf(doctors.getMrp()));
        //holder.genre.setText(doctors.getGenre());

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
}
