package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.Product_info_items;
import com.synergy.keimed_ordergenie.model.m_stockist_orderdetails;

import java.util.List;

public class StockistExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;

    List<m_stockist_orderdetails> distorderDetailsList;
    // private List<String> _listDataHeader;
    // private HashMap<String, List<String>> _listDataChild;
    List<Product_info_items> line_items;

    public StockistExpandableListAdapter(Context context, List<m_stockist_orderdetails> distorderDetailsList,List<Product_info_items> line_items) {
        this._context = context;
        this.distorderDetailsList = distorderDetailsList;
        this.line_items=line_items;
        //  this._listDataHeader = listDataHeader;
        // this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        //return distorderDetailsList.get(groupPosition).getLine_items().get(childPosititon);
        return line_items.get(groupPosition);


    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
       // return distorderDetailsList.get(groupPosition).getLine_items().get(childPosition).hashCode();
        return line_items.get(childPosition).hashCode();
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        /*final String childText = (String) getChild(groupPosition, childPosition);*/

          try {
            //  final Product_info_items exdChilddata = distorderDetailsList.get(groupPosition).getLine_items().get(childPosition);
              final Product_info_items exdChilddata = line_items.get(groupPosition);

              if (convertView == null) {
                  LayoutInflater infalInflater = (LayoutInflater) this._context
                          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  convertView = infalInflater.inflate(R.layout.stockist_child_layout, null);
              }

              TextView txt_company_name = (TextView) convertView
                      .findViewById(R.id.txt_company_name);
              TextView txt_product_rate = (TextView) convertView
                      .findViewById(R.id.txt_rate);

              TextView txt_product_scheme = (TextView) convertView
                      .findViewById(R.id.txt_scheme);

              TextView txt_product_status = (TextView) convertView
                      .findViewById(R.id.txt_status);

//              txt_company_name.setText(exdChilddata.getMfgName());
//                      txt_product_rate.setText(String.valueOf(exdChilddata.getRate()) == null ? "NA" : String.valueOf(exdChilddata.getRate()));
//        txt_product_scheme.setText(exdChilddata.getScheme() == null ? "NA" : exdChilddata.getScheme());
//        txt_product_status.setText(String.valueOf(exdChilddata.getStatus()) == null ? "NA" : String.valueOf(exdChilddata.getStatus()));

              txt_company_name.setText(exdChilddata.getMfgName());
              txt_product_rate.setText(String.valueOf(exdChilddata.getRate()) == null ? "NA" : String.valueOf(exdChilddata.getRate()));
              txt_product_scheme.setText(exdChilddata.getScheme().equals("")? "NA" : exdChilddata.getScheme());
       // txt_product_status.setText(String.valueOf(exdChilddata.getStatus()) == null ? "NA" : String.valueOf(exdChilddata.getStatus()));
         if (exdChilddata.getStatus()==3||exdChilddata.getStatus()==4)
         {
            txt_product_status.setText("Invoiced");
         }
         else
         {
             txt_product_status.setText("Not Invoiced");
         }
          }catch (Exception e)
          {
              Log.d("Excep",e.getMessage());
          }
           return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        // size = distorderDetailsList.get(groupPosition).getProduct_infor_items().size();
       // int size = distorderDetailsList.get(groupPosition).getLine_items().size();
       // int size = line_items.size();
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return distorderDetailsList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return distorderDetailsList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return distorderDetailsList.get(groupPosition).hashCode();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //String headerTitle = (String) getGroup(groupPosition);

        m_stockist_orderdetails stockist_orderdetails = distorderDetailsList.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.stockist_header_layout, null);
        }


        TextView txt_productname = (TextView) convertView
                .findViewById(R.id.txt_productname);

        TextView textView_pack = (TextView) convertView
                .findViewById(R.id.txt_pack);

        TextView textView_qty = (TextView) convertView
                .findViewById(R.id.txt_qty);

        TextView textView_price = (TextView) convertView
                .findViewById(R.id.txt_price);

        txt_productname.setText((stockist_orderdetails.getProduct_Desc() == null) ? "NA" : stockist_orderdetails.getProduct_Desc());
        textView_pack.setText((stockist_orderdetails.getPacksize() == null) ? "NA" : stockist_orderdetails.getPacksize());
        textView_qty.setText((String.valueOf(stockist_orderdetails.getQty() )== null) ? "NA" : String.valueOf(stockist_orderdetails.getQty()));
        textView_price.setText((String.valueOf(stockist_orderdetails.getPrice() )== null) ? "NA" : String.valueOf(stockist_orderdetails.getPrice()));
        /*TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txt_productname);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);*/
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;

  /*  private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;

    public StockistExpandableListAdapter(Context context, List<String> listDataHeader,
                                         HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        //Log.d("Childdataprint",childText);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.stockist_child_layout, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.txt_company_name);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.stockist_header_layout, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txt_productname);

//        TextView textView_pack = (TextView) convertView
//                .findViewById(R.id.txt_pack);
//
//        TextView textView_qty = (TextView) convertView
//                .findViewById(R.id.txt_qty);
//
//        TextView textView_price = (TextView) convertView
//                .findViewById(R.id.txt_price);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
   */
    }

}

