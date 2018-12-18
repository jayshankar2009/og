package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_product_list_chemist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1144 on 09-09-2016.
 */
public class ad_AutocompleteCustomArray_chemist extends ArrayAdapter<m_product_list_chemist> {

    public List<m_product_list_chemist> suggestion_array = new ArrayList<>();
    public List<m_product_list_chemist> temp_array;
    public List<m_product_list_chemist> orginal_array;
    SharedPreferences pref;
    String legend_data, color_code, legend_name, legend_mode;
    String str,constraint1,constraint2;
    String login_type;
    Context mContext;
    String search_item;

    @SuppressWarnings("unchecked")
    public ad_AutocompleteCustomArray_chemist(Context mContext, List<m_product_list_chemist> med_data) {
        super(mContext, 0);
        this.orginal_array = med_data;
        this.temp_array = new ArrayList<>(orginal_array);
        this.mContext = mContext;
        // this.legend_data = legend_data;
        //  this.search_item=search_item;
    }

    /*@Override
    public int getCount() {
        return orginal_array.size();
    }*/



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adpter_create_order_chemist, parent, false);
            }
            m_product_list_chemist med_data = getItem(position);
            final RelativeLayout lnr_med_main = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            final TextView productName = (TextView) convertView.findViewById(R.id.productNamee);
            // final TextView productName = (TextView) convertView.findViewById(R.id.productNamee);
            final TextView manufactureName = (TextView) convertView.findViewById(R.id.txt_gsk);
            final TextView productStatus = (TextView) convertView.findViewById(R.id.productStatus);
            final TextView packSize = (TextView) convertView.findViewById(R.id.Stock);
            final TextView Scheme = (TextView) convertView.findViewById(R.id.pro_scheme);
            final TextView half_scheme=(TextView) convertView.findViewById(R.id.txt_half_sche);;
            final TextView sub_stockist=(TextView) convertView.findViewById(R.id.txt_sub_stockist);;

            convertView.setTag(R.id.key_product_ItemCode, med_data.get_id());
            convertView.setTag(R.id.key_product_MfgCode, med_data.get_mn());
            convertView.setTag(R.id.key_product_Type, "NA");
            convertView.setTag(R.id.key_product_Name, med_data.get_in());
            convertView.setTag(R.id.key_product_Mfg, med_data.get_mn());

            convertView.setTag(R.id.key_product_Dose,"NA");
            convertView.setTag(R.id.key_product_Pack, med_data.get_pz());
            convertView.setTag(R.id.key_product_PTR, "NA");
            convertView.setTag(R.id.key_product_MRP, "NA");
            convertView.setTag(R.id.key_product_Scheme,"NA");
            convertView.setTag(R.id.key_product_stock, med_data.get_s());
            convertView.setTag(R.id.key_product_code, "NA");
            convertView.setTag(R.id.key_product_halfscheme, "NA");
            convertView.setTag(R.id.key_product_percentscheme, "NA");
            convertView.setTag(R.id.key_product_stockscheme, med_data.get_sc());
            convertView.setTag(R.id.key_product_SubStockist, med_data.get_snm());
            convertView.setTag(R.id.key_product_minQ,med_data.getMinQ());
            convertView.setTag(R.id.key_product_maxQ,med_data.getMaxQ());
            // productName.setText(med_data.get_in());
            if(med_data.getHSche()!=null){
                half_scheme.setText(med_data.getHSche());
            }else {
                half_scheme.setText("NA");
            }
            if(manufactureName!=null)
            {
                manufactureName.setText(med_data.get_mn());
            }
            else
            {
                manufactureName.setText("NA");
            }

            if(sub_stockist!=null)
            {
                sub_stockist.setText(med_data.get_snm());
            }
            else
            {
                sub_stockist.setText("NA");
            }
            if(productName!=null)
            {
                productName.setText(med_data.get_in());
            }
            else
            {
                productName.setText("NA");
            }
            packSize.setText(!med_data.get_pz().equals("null") ? med_data.get_pz().toString() : "NA");
            if(med_data.get_sc()!=null) {
                String stockColour=med_data.get_sc();
                if(stockColour.contains(","))
                {
                    String parts[] = stockColour.split(",");
                    String legend_color=parts[0];
                    String legend_name=parts[1];
                    if(legend_name.equalsIgnoreCase("NA"))
                    {
                        productStatus.setBackgroundColor(Color.parseColor(legend_color));
                        productStatus.setText("");
                    }
                    else{
                        productStatus.setBackgroundColor(Color.parseColor(legend_color));
                        productStatus.setText(legend_name);
                    }
                }
                else{
                    productStatus.setBackgroundColor(Color.parseColor(stockColour));
                    productStatus.setText("");
                }
            }

            //     login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
            //      Log.d("login_type",login_type);
//          if (login_type.equals(CHEMIST))
//          {
//              if (med_data.getSalesman().equals("1"))
//              {
//                  Scheme.setVisibility(View.VISIBLE);
//              }
//              else
//              {
//                  Scheme.setVisibility(View.INVISIBLE);
//              }
//          }
//           if (login_type.equals("user"))
//          {
//              if (med_data.getSalesman().equals("1"))
//              {
//                  Scheme.setVisibility(View.VISIBLE);
//              }
//              else
//              {
//                  Scheme.setVisibility(View.INVISIBLE);
//              }
//          }


//          if (!med_data.getScheme().equals("null")&&!med_data.getScheme().equals("")) {
//            Scheme.setText("Scheme:"+med_data.getScheme());
//           }
            /*  edits by Sumit on 9th April 2018       */
//            if (med_data.getScheme().equalsIgnoreCase("null")) {
//                Scheme.setText("No Scheme");
//            } else if (med_data.getScheme().equalsIgnoreCase("")) {
//                Scheme.setText("No Scheme");
//            } else {
//                Scheme.setText("Scheme: " + med_data.getScheme());
//            }

            // Scheme.setText(!med_data.getScheme().equals("null") ? "Scheme:"+med_data.getScheme().toString() : "Scheme:NA");
            //productStatus.setText(!med_data.getStock().equals("null") ? med_data.getStock() : "0");

            // productStatus.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //productStatus.setText("Yes");

            if (med_data.getSche().equalsIgnoreCase("null")) {
                Scheme.setText("No Scheme");
            } else if (med_data.getSche().equalsIgnoreCase("")) {
                Scheme.setText("No Scheme");
            } else {
                Scheme.setText("Scheme : " + med_data.getSche());
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        return nameFilter;
    }
    Filter nameFilter = new Filter()
    {
        // if (isNull(Object resultValue)) {
//            Toast.makeText(getContext(), "novalue" + resultValue.toString(), Toast.LENGTH_SHORT).show();
//            str = "CAFIRATE SOLUTION 3ML";
//        }
//StockistProducts resultValue = new StockistProducts();
        public String convertResultToString(Object resultValue) {
//if(resultValue!=null){}
            // sreach_product_list = daoSession.getStockistProductsDao().getStockistProducts(Itemname);
//            if(resultValue.getI)
//            {
            // else {
            if(((m_product_list_chemist) (resultValue))==null){
                str = "CAFIRATE SOLUTION 3ML";
            }
            // ((StockistProducts)(resultValue)).getItemname().toString().isEmpty();
            else {
                str = ((m_product_list_chemist) (resultValue)).get_in();
            }
//            if(str.isEmpty()){
//                 str = "na";
//            }
            //}
            return str;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if(constraint.toString().contains(" ")){
                String [] arrOfStr = constraint.toString().split(" ");
                constraint1= arrOfStr[0];
                constraint2=arrOfStr[1];
            }
            else{
                constraint1= constraint.toString();
                constraint2=" ";
            }
//            Log.d("constraint1",constraint1);
//            Log.d("constraint2",constraint2);

            if (constraint1 != null) {
                suggestion_array.clear();
                for (m_product_list_chemist product : temp_array) {
                    if ((product.get_in() != null && product.get_in().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                            .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                            // .matches(constraint.toString().replace(" ","").replace("-","")
                            .startsWith(constraint1.replace(" ", "").replace("-", "")
                                    //.contains(constraint.toString().replace(" ","").replace("-","")
                                    .replace("(", "").replace(")", "")
                                    .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                    .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                    )&&(product.get_in() != null && product.get_in().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                            .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                            .contains(constraint2.replace(" ","").replace("-","")
                                    .replace("(", "").replace(")", "")
                                    .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                    .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                    ))



                    {
                        synchronized (this) {
                            suggestion_array.add(product);

                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestion_array;
                filterResults.count = suggestion_array.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        /*@Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestion_array.clear();
                for (m_product_list_chemist product : temp_array) {
                    if (product.get_in() != null && product.get_in().replace("-","").replace(" ","").replace("(","").replace(")","")
                            .replace("/","").replace("*","").replace(":","").replace("_","").toLowerCase()
                           // .matches(constraint.toString().replace(" ","").replace("-","")
                                    .startsWith(constraint.toString().replace(" ","").replace("-","")
                                    //.contains(constraint.toString().replace(" ","").replace("-","")
                                   .replace("(","").replace(")","")
                                    .replace("/","").replace("*","").replace(":","").replace("_","")
                                    .toLowerCase()))
                    if (product.get_in() != null)
                    {
                        synchronized (this)
                        {
                            suggestion_array.add(product);

                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestion_array;
                filterResults.count = suggestion_array.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }*/
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            List<m_product_list_chemist> filteredList = (List<m_product_list_chemist>) results.values;
            if (results != null && results.count > 0) {
                clear();
                // orginal_array=filteredList;
               /* for (m_Product_list c : filteredList) {
                    add(c);
                }*/
                // new ArrayList<m_Product_list>(filteredList);
                addAll(filteredList);
               /* synchronized (this) {

                for (m_Product_list c : filteredList) {
                    add(c);
                }
            }*/
                notifyDataSetChanged();
            }
        }
    };
}
