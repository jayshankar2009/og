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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.database.StockistProducts;
import com.synergy.keimed_ordergenie.utils.SharedPref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.LEGEND_MODE;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by 1144 on 09-09-2016.
 */
public class ad_AutocompleteCustomArray_Salesman extends ArrayAdapter<StockistProducts> {

    public ArrayList<StockistProducts> suggestion_array = new ArrayList<>();
    public ArrayList<StockistProducts> suggestion_array1 = new ArrayList<>();
    public ArrayList<StockistProducts> temp_array;
    public ArrayList<StockistProducts> temp_array1;
    public List<StockistProducts> orginal_array;
    SharedPreferences pref;
    String sales_legend_mode,legend_data, color_code, legend_name, legend_mode,final_legend_mode;
    String constraint1,  constraint2;
    String str;
    String login_type;
    Context mContext;
    String search_item;

    @SuppressWarnings("unchecked")
    public ad_AutocompleteCustomArray_Salesman(Context mContext, ArrayList<StockistProducts> med_data) {    super(mContext, 0, med_data);
        this.orginal_array = med_data;
        this.temp_array = new ArrayList<>(orginal_array);
        this.temp_array1 = new ArrayList<>(orginal_array);
        this.mContext = mContext;
        this.legend_data = legend_data;
       // this.sales_legend_mode = sales_legend_mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adpter_create_order_new, parent, false);
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
            convertView.setTag(R.id.key_product_minQ,med_data.getMinQ());
            convertView.setTag(R.id.key_product_maxQ,med_data.getMaxQ());

            if(AppConfig.AppCode == 1){
                convertView.setTag(R.id.key_product_BoxSize, med_data.getBoxSize());
            }else{
                convertView.setTag(R.id.key_product_halfscheme, med_data.getHalfScheme());
            }

            convertView.setTag(R.id.key_product_percentscheme, med_data.getPercentScheme());
            convertView.setTag(R.id.key_product_legendname, med_data.getLegendName());
            convertView.setTag(R.id.key_product_legendcolor, med_data.getLegendColor());

            if(AppConfig.AppCode == 1){
                half_scheme.setText("Box Size : "+med_data.getBoxSize());
            }else {
                half_scheme.setText(" "+med_data.getHalfScheme());
            }

//            Log.d("legendname1", med_data.getLegendName());
//            Log.d("legendcolor1", med_data.getLegendColor());
//            Log.d("legendname1",med_data.getLegendName());
//            Log.d("legendcolor1",med_data.getLegendColor());
            String  sales_legend_mode = SharedPref.getLegendMode(getContext());

            // new syncProductList(void,void,void).execute();
            productName.setText(med_data.getItemname());
            if (manufactureName != null) {
                manufactureName.setText(med_data.getMfgName());
            } else {
                manufactureName.setText("NA");
            }
            packSize.setText(!med_data.getPacksize().equals("null") ? med_data.getPacksize().toString() : "NA");

            /*  edits by Sumit on 9th April 2018       */
            if (med_data.getScheme().equalsIgnoreCase("null")) {
                Scheme.setText("No Scheme");
            } else if (med_data.getScheme().equalsIgnoreCase("")) {
                Scheme.setText("No Scheme");
            } else {
                Scheme.setText("Scheme: " + med_data.getScheme());
            }


                productStatus.setBackgroundColor(Color.parseColor(med_data.getLegendColor()));
              if (med_data.getLegendName().equalsIgnoreCase("NA"))
              {
                  productStatus.setText("");
              }
              else
              {
                  productStatus.setText(med_data.getLegendName());
              }

                //code and name





            // Scheme.setText(!med_data.getScheme().equals("null") ? "Scheme:"+med_data.getScheme().toString() : "Scheme:NA");
            //productStatus.setText(!med_data.getStock().equals("null") ? med_data.getStock() : "0");
            pref = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String clilent_role = pref.getString(CLIENT_ROLE, "0");

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

            if (((StockistProducts) (resultValue)) == null) {
                str = "CAFIRATE SOLUTION 3ML";
            }
            // ((StockistProducts)(resultValue)).getItemname().toString().isEmpty();
            else {
                str = ((StockistProducts) (resultValue)).getItemname();
            }

            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

        // String  str1 = constraint.toString();
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
                suggestion_array = getSuggestion_array(temp_array,constraint1,constraint2);

                /*for (StockistProducts product : temp_array) {
                    if ((product.getItemname() != null && product.getItemname().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                            .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                            // .matches(constraint.toString().replace(" ","").replace("-","")
                            .startsWith(constraint1.replace(" ", "").replace("-", "")
                                    //.contains(constraint.toString().replace(" ","").replace("-","")
                                    .replace("(", "").replace(")", "")
                                    .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                    .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                            )&&(product.getItemname() != null && product.getItemname().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
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
*/
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
                }
               */
                // new ArrayList<m_Product_list>(filteredList);
                addAll(filteredList);
               /* synchronized (this) {

                for (m_Product_list c : filteredList) {
                    add(c);
                    }
                }
                */
                notifyDataSetChanged();
            }
        }
    };


    public ArrayList<StockistProducts> getSuggestion_array(ArrayList<StockistProducts> temp_array, final String constraint1, final String constraint2) {
        Collection<StockistProducts> filterStrings = Collections2.filter(
                temp_array, new Predicate<StockistProducts>() {
                    @Override
                    public boolean apply(StockistProducts product) {

                        return ((product.getItemname() != null && product.getItemname().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                                .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                                // .matches(constraint.toString().replace(" ","").replace("-","")
                                .startsWith(constraint1.replace(" ", "").replace("-", "")
                                        //.contains(constraint.toString().replace(" ","").replace("-","")
                                        .replace("(", "").replace(")", "")
                                        .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                        .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                        )&&(product.getItemname() != null && product.getItemname().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                                .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                                .contains(constraint2.replace(" ","").replace("-","")
                                        .replace("(", "").replace(")", "")
                                        .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                        .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                        ));
                    }
                });
        return new ArrayList<StockistProducts>(filterStrings);
    }



    private static class customPredicate implements Predicate<StockistProducts> {

        @Override
        public boolean apply(StockistProducts products) {
            return products.getMfgName().equals("");
        }
    }

}
