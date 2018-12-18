package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_return_add_product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1144 on 09-09-2016.
 */
public class ad_AutocompletSalesReturn extends ArrayAdapter<m_return_add_product> {
    public ArrayList<m_return_add_product> suggestion_array = new ArrayList<>();
    public ArrayList<m_return_add_product> temp_array;
    public List<m_return_add_product> orginal_array;
    SharedPreferences pref;
    String legend_data,color_code,legend_name,legend_mode;
    String str;
    String login_type;
    Context mContext;
    public ad_AutocompletSalesReturn(Context mContext, List<m_return_add_product> med_data)
    {
        super(mContext, 0, med_data);
        this.orginal_array = med_data;
        this.temp_array = new ArrayList<m_return_add_product>(orginal_array);
        this.mContext = mContext;
        this.legend_data = legend_data;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adpter_return_product, parent, false);

            }
            m_return_add_product med_data = getItem(position);
            final RelativeLayout lnr_med_main = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            final TextView productName = (TextView) convertView.findViewById(R.id.productName);
            Log.d("productname",med_data.getProduct_Desc());
            productName.setText(med_data.getProduct_Desc());
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {

            String str = ((m_return_add_product) resultValue).getProduct_Desc();
            return str;

        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestion_array.clear();
                for (m_return_add_product product : temp_array) {
                    if (product.getProduct_Desc() != null && product.getProduct_Desc().replace("-","").replace(" ","").replace("(","").replace(")","")
                            .replace("/","").replace("*","").replace(":","").replace("_","").toLowerCase()
                            .contains(constraint.toString().replace(" ","").replace("-","")
                                   .replace("(","").replace(")","")
                                    .replace("/","").replace("*","").replace(":","").replace("_","")
                                    .toLowerCase()))

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
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<m_return_add_product> filterList = (ArrayList<m_return_add_product>) results.values;
            if (results != null && results.count > 0) {
                filterList.clear();
                for (m_return_add_product people : filterList)
                {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
//        @Override
//        protected void publishResults(CharSequence constraint,
//                                      FilterResults results) {
//            ArrayList<m_return_add_product> filteredList = (ArrayList<m_return_add_product>) results.values;
//            if (results != null && results.count > 0) {
//                clear();
//                // orginal_array=filteredList;
//               /* for (m_Product_list c : filteredList) {
//                    add(c);
//                }*/
//                // new ArrayList<m_Product_list>(filteredList);
//                addAll(filteredList);
//               /* synchronized (this) {
//
//                for (m_Product_list c : filteredList) {
//                    add(c);
//                }
//            }*/
//                notifyDataSetChanged();
//            }
//        }
    };
}
