package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.Orders;
import com.synergy.keimed_ordergenie.model.summary_order;

import java.util.List;

public class Summary_Adapter extends BaseExpandableListAdapter {
    private List<summary_order> posts;
    private Context ctx;
    private int itemLayoutId;
    private int groupLayoutId;
    public Summary_Adapter(List<summary_order> posts,  Context ctx) {
        this.itemLayoutId =  R.layout.adapter_summary_child;
        this.groupLayoutId = R.layout.adapter_summary_parent;
        this.posts = posts;
        this.ctx = ctx;

    }

    @Override
    public int getGroupCount() {
        return posts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = posts.get(groupPosition).getOrders().size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return posts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return posts.get(groupPosition).getOrders().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return posts.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return posts.get(groupPosition).getOrders().get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      //  View v = null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_summary_parent, null);     }
        summary_order cat = posts.get(groupPosition);
        TextView tx_cust_name = (TextView)convertView.findViewById(R.id.tx_cust_name);
        TextView txt_cust_amt= (TextView)convertView.findViewById(R.id.tx_stock);
        ImageView imgIndicator = (ImageView)convertView.findViewById(R.id.due_indicator);
        final LinearLayout linearLayout=(LinearLayout)convertView.findViewById(R.id.lnrVisible);

        if(isExpanded) {
            linearLayout.setVisibility(View.VISIBLE);
          //  imgIndicator.s
            imgIndicator.setImageResource(android.R.drawable.arrow_up_float);
        }else {
            linearLayout.setVisibility(View.GONE);
            imgIndicator.setImageResource(android.R.drawable.arrow_down_float);
        }
       /* RelativeLayout rltClick =(RelativeLayout)convertView.findViewById(R.id.rltMain);
        final LinearLayout linearLayout=(LinearLayout)convertView.findViewById(R.id.lnrVisible);
        rltClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });*/
        tx_cust_name.setText(cat.getClientName());
        txt_cust_amt.setText(String.valueOf(cat.getTotalAmount()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_summary_child, null);
        }
        Orders cat = posts.get(groupPosition).getOrders().get(childPosition);
        TextView tx_invoicedate = (TextView)convertView.findViewById(R.id.tx_date);
        TextView tx_orderNo = (TextView)convertView.findViewById(R.id.tx_orderNo);
        TextView tx_amt = (TextView)convertView.findViewById(R.id.tx_amt);
        tx_invoicedate.setText(cat.getDoc_Date());
        tx_orderNo.setText(cat.getDOC_NO());
        tx_amt.setText(String.valueOf(cat.getAmount()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
