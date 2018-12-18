package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.d_pending_line_items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 12/4/2017.
 */

public class Expandlist_pendingbill_Adapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    List<d_pending_line_items> line_items;

    public Expandlist_pendingbill_Adapter(Context context, List<d_pending_line_items> line_items) {
        this._context = context;
        this.line_items = line_items;
       // this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.line_items.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

       // final String childText = (String) getChild(groupPosition, childPosition);
      //  final d_pending_line_items childText = line_items.get(groupPosition);
      //  Log.d("printText",childText);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_pendingbill_distributor_childitems, null);
        }

//        TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.textview_productcode);

       // txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                .size();
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.line_items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.line_items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
       // String headerTitle = (String) getGroup(groupPosition);
        final d_pending_line_items headerTitle = line_items.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_pendingbillheadertems, null);
        }

        TextView txt_date = (TextView) convertView
                .findViewById(R.id.tx_date);
        TextView txt_itemcount = (TextView) convertView
                .findViewById(R.id.tx_itemcount);
        TextView tx_pending_amt = (TextView) convertView
                .findViewById(R.id.tx_pending_amount);

        ImageView img_icon=(ImageView)convertView.findViewById(R.id.image_icon);

        if (headerTitle.getPaymentreceived()>0&&(headerTitle.getPaymentreceived()<headerTitle.getBalanceamt()))
        {
            img_icon.setVisibility(View.VISIBLE);
        }
        else
        {
            img_icon.setVisibility(View.INVISIBLE);
        }

        txt_date.setTypeface(null, Typeface.BOLD);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
        SimpleDateFormat sdf1 = new SimpleDateFormat( "dd-MMM-yyyy'T'HH:mm:ss.SSS'Z'" );
       // String oldFormat= "dd-MMM-yy HH:mm:ss";
        try {
            if (headerTitle.getInvoicedate()!=null) {
                Date d1 = sdf.parse(headerTitle.getInvoicedate());
                String d2=sdf1.format(d1);
                Log.d("date", d1.toString());
                Log.d("d2", d2);
                Log.d("datee", headerTitle.getInvoicedate());
                txt_date.setText(d2);
            }
            txt_itemcount.setText(headerTitle.getTotalitems()+"Items");
            tx_pending_amt.setText("Rs."+String.valueOf(headerTitle.getBillamount()));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
