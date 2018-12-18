package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.Productcatlog_inventory;
import com.synergy.keimed_ordergenie.model.Productcatolg_modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by anandbose on 09/06/15.
 */
public class Expand_productcatlogs extends BaseExpandableListAdapter {

    String legend_data,color_code,legend_name,legend_mode;
    public List<Productcatlog_inventory> items ;
    private Context context;
    List<Productcatolg_modal> d_productcatogs;
    String distributor_legendData;
    // child data in format of header title, child title
    // private HashMap<String, List<String>> _listDataChild;

    public Expand_productcatlogs(List<Productcatolg_modal> d_productcatogs,Context context,String distributor_legendData) {
        this.context = context;
        this.d_productcatogs = d_productcatogs;

        this.distributor_legendData = distributor_legendData;
        // this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return d_productcatogs.get(groupPosition);
    }
    //return catList.get(groupPosition).getline_items().get(childPosition);
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // return d_productcatogs.get(groupPosition).getline_items().get(childPosition).hashCode();

        return d_productcatogs.get(childPosition).hashCode();
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // final String childText = (String) getChild(groupPosition, childPosition);

        /*final Productcatlog_inventory childText = d_productcatogs.get(groupPosition).getline_items().get(childPosition);*/

        final Productcatolg_modal childText = d_productcatogs.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_productchilditems, null);
        }



        TextView textview_productcode = (TextView) convertView.findViewById(R.id.textview_productcode);
        TextView textview_pharma = (TextView) convertView.findViewById(R.id.textview_pharma);
        TextView textview_stock = (TextView) convertView.findViewById(R.id.textview_stock);
        TextView product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
        TextView txt_composition = (TextView) convertView.findViewById(R.id.txt_composition);

        // Log.d("groupStock",d_productcatogs.get(groupPosition).getStock());

        //   product_mrp.setText(String.valueOf(childText.getMRP()));
        textview_productcode.setText(childText.getItemcode());
        textview_pharma.setText(d_productcatogs.get(groupPosition).getDoseForm());
        textview_stock.setText(d_productcatogs.get(groupPosition).getStock());
        txt_composition.setText(d_productcatogs.get(groupPosition).getItemname());

        // Log.d("Doseform",);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       /* return this.d_productcatogs.get(this._listDataHeader.get(groupPosition))
                .size();*/

        // int size = d_productcatogs.get(groupPosition).getline_items().size();
        //return size;
        return  1;

    }

    @Override
    public Object getGroup(int groupPosition) {
        return d_productcatogs.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return d_productcatogs.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return d_productcatogs.get(groupPosition).hashCode();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //  String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_productheadertems, null);
        }

        Productcatolg_modal productcatolg = d_productcatogs.get(groupPosition);

        try {


            String customer_name = productcatolg.getItemname();
            ImageView due_indicator = (ImageView) convertView.findViewById(R.id.due_indicator);
            TextView circular_textview = (TextView) convertView.findViewById(R.id.circular_textview);
            TextView tx_distriname = (TextView) convertView.findViewById(R.id.tx_distriname);
            TextView pharmaname_textview = (TextView) convertView.findViewById(R.id.pharmaname_textview);
            TextView textview_ptrvalue = (TextView) convertView.findViewById(R.id.textview_ptrvalue);
            // circular_textview.setText(String.valueOf(part1.charAt(0) + String.valueOf(part2.charAt(0))));
            tx_distriname.setText(productcatolg.getItemname());



            try {


                JSONArray jsonArray = new JSONArray(distributor_legendData);
                for (int i = 0;i<jsonArray.length();i++)
                {

                    JSONObject j_ob = jsonArray.getJSONObject(i);
                    if (Integer.parseInt(productcatolg.getStock()) >= j_ob.getInt("StartRange") &&
                            Integer.parseInt(productcatolg.getStock()) <= j_ob.getInt("EndRange")) {
                        color_code = j_ob.getString("ColorCode");
                        legend_name=j_ob.getString("LegendName");

                        legend_mode=j_ob.getString("Legend_Mode");

                        Log.d("legend_name",legend_name);
                        Log.d("print_colorocode",color_code);

                        // due_indicator.setBackgroundColor(Color.parseColor(color_code));


                    }


                }


            }catch (Exception e){

                Log.d("Exception",e.getMessage());

            }


          /*  if(Integer.parseInt(productcatolg.getStock()) == 0 || productcatolg == null){

                due_indicator.setImageResource(R.drawable.nostockcircle);
            }*/

            if(legend_name.equals("Low"))
            {
                if(Integer.parseInt(productcatolg.getStock()) == 0 || productcatolg == null){

                    due_indicator.setImageResource(R.drawable.nostockcircle);
                }else {

                    due_indicator.setImageResource(R.drawable.circle);
                }
                //nostockcircle,circle
                //due_indicator.setImageResource(R.drawable.circle);

            }else if(legend_name.equals("In-Plenty")){

                due_indicator.setImageResource(R.drawable.plentycircle);

            }else if(legend_name.contains("Moderate")){

                due_indicator.setImageResource(R.drawable.moderatecircle);

            }else if(legend_name.equals("Available")){

                due_indicator.setImageResource(R.drawable.greencircle);

            }


            //Log.e("positionOnly", String.valueOf(groupPosition));

            //string = string.replaceAll("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+", "");

            // 1-2-2018
            // textview_ptrvalue.setText(String.valueOf(productcatolg.getline_items().get(0).getRate()));

            textview_ptrvalue.setText(String.valueOf(productcatolg.getPacksize()));

            // pharmaname_textview.setText(productcatolg.getManufacturer_Name());

        }catch (Exception e)
        {

        }

        return convertView;
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
