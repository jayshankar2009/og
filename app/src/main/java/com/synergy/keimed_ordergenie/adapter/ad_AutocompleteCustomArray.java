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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.database.StockistProducts;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.LEGEND_MODE;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by 1144 on 09-09-2016.
 */
public class ad_AutocompleteCustomArray extends ArrayAdapter<StockistProducts> {

    public ArrayList<StockistProducts> suggestion_array = new ArrayList<>();
    public ArrayList<StockistProducts> temp_array;
    public List<StockistProducts> orginal_array;
    SharedPreferences pref;
    String legend_data, color_code, legend_name, legend_mode;
    String str;
    String login_type;
    Context mContext;
    String search_item;

    @SuppressWarnings("unchecked")
    public ad_AutocompleteCustomArray(Context mContext, ArrayList<StockistProducts> med_data, String legend_data) {
        super(mContext, 0, med_data);
        this.orginal_array = med_data;
        this.temp_array = new ArrayList<>(orginal_array);
        this.mContext = mContext;
        this.legend_data = legend_data;
      //  this.search_item=search_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adpter_create_order_chemist, parent, false);
            }
            StockistProducts med_data = getItem(position);
            final RelativeLayout lnr_med_main = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            final TextView productName = (TextView) convertView.findViewById(R.id.productName);
            final TextView manufactureName = (TextView) convertView.findViewById(R.id.txt_gsk);
            final TextView productStatus = (TextView) convertView.findViewById(R.id.productStatus);
            final TextView packSize = (TextView) convertView.findViewById(R.id.Stock);
            final TextView Scheme = (TextView) convertView.findViewById(R.id.pro_scheme);
           final TextView half_scheme=(TextView) convertView.findViewById(R.id.txt_half_sche);;

            convertView.setTag(R.id.key_product_ItemCode, med_data.getItemcode());
            convertView.setTag(R.id.key_product_MfgCode, med_data.getMfgCode());
            convertView.setTag(R.id.key_product_Type, med_data.getType());
            convertView.setTag(R.id.key_product_Name, med_data.getItemname());
            convertView.setTag(R.id.key_product_Mfg, med_data.getMfgName());
            convertView.setTag(R.id.key_product_Dose, med_data.getDoseForm());
            convertView.setTag(R.id.key_product_Pack, med_data.getPacksize());
            convertView.setTag(R.id.key_product_PTR, med_data.getRate());
            convertView.setTag(R.id.key_product_MRP, med_data.getMRP());
            convertView.setTag(R.id.key_product_Scheme, med_data.getScheme());
            convertView.setTag(R.id.key_product_stock, med_data.getStock());
            convertView.setTag(R.id.key_product_code, med_data.getProduct_ID());
            convertView.setTag(R.id.key_product_halfscheme, med_data.getHalfScheme());
            convertView.setTag(R.id.key_product_percentscheme, med_data.getPercentScheme());
            productName.setText(med_data.getItemname());
           half_scheme.setText(med_data.getHalfScheme());
           if(manufactureName!=null)
           {
               manufactureName.setText(med_data.getMfgName());
           }
           else
           {
               manufactureName.setText("NA");
           }
            packSize.setText(!med_data.getPacksize().equals("null") ? med_data.getPacksize().toString() : "NA");
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
            if (med_data.getScheme().equalsIgnoreCase("null")) {
                Scheme.setText("No Scheme");
            } else if (med_data.getScheme().equalsIgnoreCase("")) {
                Scheme.setText("No Scheme");
            } else {
                Scheme.setText("Scheme: " + med_data.getScheme());
            }

           // Scheme.setText(!med_data.getScheme().equals("null") ? "Scheme:"+med_data.getScheme().toString() : "Scheme:NA");
            //productStatus.setText(!med_data.getStock().equals("null") ? med_data.getStock() : "0");
            pref = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String clilent_role = pref.getString(CLIENT_ROLE, "0");
            //Log.e("clilent_role",clilent_role);
            try {
                JSONArray j_arr = new JSONArray(legend_data);
                //Log.d("legendDataAdapter", j_arr.toString());
                for (int i = 0; i < j_arr.length(); i++) {
                    JSONObject j_ob = j_arr.getJSONObject(i);
                    if (Integer.parseInt(med_data.getStock()) >= j_ob.getInt("StartRange") &&
                            Integer.parseInt(med_data.getStock()) <= j_ob.getInt("EndRange")) {
                         color_code = j_ob.getString("ColorCode");
                         legend_name=j_ob.getString("LegendName");
                        legend_mode = j_ob.getString("Legend_Mode");
                      if (legend_mode.equals("3"))

                        {
                            //    Log.d("legend_mode3","print three ");
                            productStatus.setBackgroundColor(Color.parseColor(color_code));
                            productStatus.setText(legend_name);
                            //code and name
                        }
                        else if(legend_mode.equals("2"))
                        {
                            //  Log.d("legend_mode2","print two");
                            //code n stock
                            productStatus.setBackgroundColor(Color.parseColor(color_code));
                            productStatus.setText(String.valueOf(med_data.getStock()));
                            // productStatus.setTextColor(getResources().getColor(R.color.white));
                        }

                        else if (legend_mode.equals("1"))
                        {
                            //Log.d("legend_mode1","print one");
                            //only colorcode
                            productStatus.setBackgroundColor(Color.parseColor(color_code));
                            //**

                        }
//                        if (clilent_role.equals("user")) {
//                            legend_mode = pref.getString(LEGEND_MODE, "0");
//                            //Toast.makeText(mContext, "iff", Toast.LENGTH_SHORT).show();
//                          //  Log.e("legend_mode",legend_mode);
//                        }
//                        else {
//                            //Toast.makeText(mContext, "else", Toast.LENGTH_SHORT).show();
//                           // JSONObject j_ob2  = j_arr.getJSONObject(1);
//                            legend_mode = j_ob.getString("Legend_Mode");
//                            //Log.d("legendModeAdapter",legend_mode);
//
////                            legend_mode=j_ob.getString("LegendMode");
//                           // Log.e("legend_mode11",legend_mode);
//                        }
                        //  productStatus.getBackground().setColorFilter(Color.parseColor(color_code), PorterDuff.Mode.MULTIPLY);

//                        Log.d("reply",legend_mode);
//                        Log.d("reply1",legend_name);
//                        Log.d("reply2",color_code);
                    }
                }

            } catch (Exception e) {
                e.toString();
            }
//            if (legend_mode.equals("1")) {
//                if (med_data.getStock().equals("null")) {
//                    productStatus.setVisibility(View.GONE);
//                } else {
//                    productStatus.setVisibility(View.VISIBLE);
//                    productStatus.setText(med_data.getStock());
//                    productStatus.setBackgroundColor(Color.parseColor(color_code));
//                }
//            } else if (legend_mode.equals("2")) {
//                if (med_data.getStock().equals("null")) {
//                    productStatus.setVisibility(View.GONE);
//                } else {
//                    productStatus.setVisibility(View.VISIBLE);
//                    productStatus.setText(med_data.getStock());
//                    productStatus.setBackgroundColor(Color.parseColor(color_code));
//                }
//            } else if (legend_mode.equals("3")) {
//                if (med_data.getStock().equals("null")) {
//                    productStatus.setVisibility(View.GONE);
//                } else {
//                    productStatus.setVisibility(View.VISIBLE);
//                    productStatus.setText(legend_name);
//                    productStatus.setBackgroundColor(Color.parseColor(color_code));
//                }
//            }
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
            if(((StockistProducts) (resultValue))==null){
                          str = "CAFIRATE SOLUTION 3ML";
            }
           // ((StockistProducts)(resultValue)).getItemname().toString().isEmpty();
              else {
                str = ((StockistProducts) (resultValue)).getItemname();
            }
//            if(str.isEmpty()){
//                 str = "na";
//            }
            //}
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestion_array.clear();
                for (StockistProducts product : temp_array) {
                    if (product.getItemname() != null && product.getItemname().replace("-","").replace(" ","").replace("(","").replace(")","")
                            .replace("/","").replace("*","").replace(":","").replace("_","").toLowerCase()
                           // .matches(constraint.toString().replace(" ","").replace("-","")
                                    .startsWith(constraint.toString().replace(" ","").replace("-","")
                                    //.contains(constraint.toString().replace(" ","").replace("-","")
                                   .replace("(","").replace(")","")
                                    .replace("/","").replace("*","").replace(":","").replace("_","")
                                    .toLowerCase()))

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
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<StockistProducts> filteredList = (ArrayList<StockistProducts>) results.values;
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
