package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.Invoice_details;
import com.synergy.keimed_ordergenie.model.m_all_pending_list;
import com.synergy.keimed_ordergenie.model.m_all_pending_list_line_items;
import com.synergy.keimed_ordergenie.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by anandbose on 09/06/15.
 */
public class Exad_pending_all_bill extends BaseExpandableListAdapter {

    private List<m_all_pending_list> catList;
    private int itemLayoutId;
    private int groupLayoutId;
    private Context ctx;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    /*public static final String  CHEMIST_ORDER_ID = "chemist_order_id";
    public static final String  CHEMIST_ORDER_DATE = "chemist_order_date";
*/

    public static final String CHEMIST_ORDER_ID = "chemist_order_id";
    public static final String CHEMIST_ORDER_DATE = "chemist_order_date";
    public static final String STOCKIST_INVOICE_No = "invoice_id";
    public static final String CHEMIST_INVOICE_No = "invoiceid";


    public Exad_pending_all_bill(List<m_all_pending_list> catList, Context ctx) {

        this.itemLayoutId =  R.layout.exad_all_pending_bill_lineitem;
        this.groupLayoutId = R.layout.exad_pending_all_bill_heading;
        this.catList = catList;
        this.ctx = ctx;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getLine_items().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getLine_items().get(childPosition).hashCode();
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

       // View v = convertView;
        View v = null;
        ViewDataBinding binding=null;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            binding = DataBindingUtil.inflate(inflater, R.layout.exad_all_pending_bill_lineitem, parent, false);
        }
       final  m_all_pending_list_line_items det = catList.get(groupPosition).getLine_items().get(childPosition);
        if(binding!=null) {
            binding.setVariable(BR.v_pendingbills_line_item, det);
        }

      TextView tx_invoicedate= (TextView)   binding.getRoot().findViewById(R.id.tx_invoicedate);
     TextView txtDays= (TextView)binding.getRoot().findViewById(R.id.tx_day_du);

        if (Utility.convertStringIntoDate(Constant.currentDate) != null && Utility.convertStringIntoDate(catList.get(groupPosition).getLine_items().get(childPosition).getInvoicedate()) != null) {
            long dayCount = Utility.convertStringIntoDate(Constant.currentDate).getTime() - Utility.convertStringIntoDate(catList.get(groupPosition).getLine_items().get(childPosition).getInvoicedate()).getTime();
            txtDays.setText(""+TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS));
        } else {
            txtDays.setText("0");

        }
        try{
            tx_invoicedate.setText(sdf.format(dateFormat.parse(det.getInvoicedate())));


        }catch (Exception e)
        {

        }

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //ctx  = v.getContext();
                Intent intent=new Intent(ctx,Invoice_details.class);
               intent.putExtra(CHEMIST_ORDER_ID,det.getOrderId());
                //put invoice id
                intent.putExtra(CHEMIST_INVOICE_No,det.getInvoiceID());
                intent.putExtra(STOCKIST_INVOICE_No,det.getInvoiceid());
                intent.putExtra("invoice_no",det.getInvoiceno());
                intent.putExtra(CHEMIST_ORDER_DATE,det.getInvoicedate().toString());
                ctx.startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = catList.get(groupPosition).getLine_items().size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return catList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return catList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return catList.get(groupPosition).hashCode();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        //View v = convertView;
        View v = null;
        ViewDataBinding binding=null;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService    (Context.LAYOUT_INFLATER_SERVICE);
            binding = DataBindingUtil.inflate(inflater, R.layout.exad_pending_all_bill_heading, parent, false);
        }
        m_all_pending_list cat = catList.get(groupPosition);

        if(cat.getCust_outstanding()>0)
        {
            ((ImageView)binding.getRoot().findViewById(R.id.due_indicator)).setVisibility(View.VISIBLE);
        }else
        {
            ((ImageView)binding.getRoot().findViewById(R.id.due_indicator)).setVisibility(View.INVISIBLE);
        }

      /*  if ((groupPosition+1)/2==0) {
            ((ImageView)binding.getRoot().findViewById(R.id.due_indicator)).setVisibility(View.GONE);
        } else {
            ((ImageView)binding.getRoot().findViewById(R.id.due_indicator)).setImageResource(View.VISIBLE);
        }*/


     if(binding!=null) {
      //   if (cat.getCust_outstanding()>0)
         binding.setVariable(BR.v_pendingbillheading, cat);
     }
         return binding.getRoot();

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}
