package com.synergy.keimed_ordergenie.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.Sales;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.Sales;

import java.util.ArrayList;

public class SalesDataCustomAdapter extends RecyclerView.Adapter<SalesDataCustomAdapter.MyViewHolder> {

    private ArrayList<Sales> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_customer_sales_name;
        TextView text_customer_sales_qty;
        TextView text_customer_sales_amount;
        TextView text_sales_return_qty;
        TextView text_sales_return_amount;
        TextView text_sales_total_amount;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.text_customer_sales_name = (TextView) itemView.findViewById(R.id.text_customer_sales_name);
            this.text_customer_sales_qty = (TextView) itemView.findViewById(R.id.text_customer_sales_qty);
            this.text_customer_sales_amount = (TextView) itemView.findViewById(R.id.text_customer_sales_amount);
            this.text_sales_return_qty = (TextView) itemView.findViewById(R.id.text_sales_return_qty);
            this.text_sales_return_amount = (TextView) itemView.findViewById(R.id.text_sales_return_amount);
            this.text_sales_total_amount = (TextView) itemView.findViewById(R.id.text_sales_total_amount);

        }
    }

    public SalesDataCustomAdapter(ArrayList<Sales> allSalesData) {
        this.dataSet = allSalesData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartview_sales_summary_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        holder.text_customer_sales_name.setText(dataSet.get(listPosition).get_chName());
        //Log.d("name",dataSet.get(listPosition).get_chName());

        if(dataSet.get(listPosition).getS_totalbills()!=null){
            holder.text_customer_sales_qty.setText(dataSet.get(listPosition).getS_totalbills());
        }else {
            holder.text_customer_sales_qty.setText("0");
        }
        if(dataSet.get(listPosition).getS_salesAmount()!=null){
            holder.text_customer_sales_amount.setText("Rs. " + dataSet.get(listPosition).getS_salesAmount());
        }else{
            holder.text_customer_sales_amount.setText("Rs. 0");
        }
        if(dataSet.get(listPosition).getR_salesreturnbills()!=null){
            holder.text_sales_return_qty.setText(dataSet.get(listPosition).getR_salesreturnbills());
        }else{
            holder.text_sales_return_qty.setText("0");
        }
        if(dataSet.get(listPosition).getR_salesreturn()!=null){
            holder.text_sales_return_amount.setText("Rs. " + dataSet.get(listPosition).getR_salesreturn());
        }else{
            holder.text_sales_return_amount.setText("Rs. 0");
        }
        if(dataSet.get(listPosition).getT_amt()!=null){
            holder.text_sales_total_amount.setText("Rs. " + dataSet.get(listPosition).getT_amt());
        }else{
            holder.text_sales_total_amount.setText("Rs. 0");
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
        //return 3;
    }
}
