package com.synergy.keimed_ordergenie.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.StockistExpandableListAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.Product_info_items;
import com.synergy.keimed_ordergenie.model.m_stockist_orderdetails;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_DISTRIBUTOR_ORDER_DETAILS;

public class StockistOrderDetails extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {
    ExpandableListView expandableListView;
    StockistExpandableListAdapter listAdapter;
    private List<m_stockist_orderdetails> distorderDetailsList = new ArrayList<m_stockist_orderdetails>();
    Product_info_items productcatolg_modal;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    TextView reviewStatus,orderId,chemistname,customer_id,order_date,order_amount,item_count;
    List<Product_info_items> line_items;
    m_stockist_orderdetails m_stockist_orderdetails;
    Double OrderAmount=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockist_order_details);
        setTitle("Order Details");
        expandableListView = (ExpandableListView) findViewById(R.id.expand_distorders);
        reviewStatus=(TextView)findViewById(R.id.process_status);
        orderId=(TextView)findViewById(R.id.order_id);
        chemistname=(TextView)findViewById(R.id.chemist_name);
        customer_id=(TextView)findViewById(R.id.cust_id);
        order_date=(TextView)findViewById(R.id.date);
        order_amount=(TextView)findViewById(R.id.order_amount);
        item_count=(TextView)findViewById(R.id.txt_count);

        String orderid=getIntent().getStringExtra("order_id");
        String cust_id=getIntent().getStringExtra("custom_id");
        String itemcount=getIntent().getStringExtra("item_count");
        String cust_name=getIntent().getStringExtra("cust_name");
       // String cust_amount=getIntent().getStringExtra("cust_amount");
        String doc_date=getIntent().getStringExtra("doc_date");
        String order_status=getIntent().getStringExtra("status");

       // Log.d("orderid",orderid+cust_id+itemcount+cust_name+doc_date);
        orderId.setText("OrderId"+"\n"+orderid);
        chemistname.setText(cust_name);
        customer_id.setText("Customer ID:"+cust_id);
        order_date.setText(doc_date);
      //  order_amount.setText("Rs."+cust_amoeunt);
        item_count.setText(itemcount+"ITEMS  ");
        if (order_status.equals("1"))
        {
            //processing
            reviewStatus.setText("processing");
        }
        else if (order_status.equals("3")||order_status.equals("4"))
        {
            //completed
            reviewStatus.setText("completed");
        }
        else if (order_status.equals("0"))
        {
            //submited
            reviewStatus.setText("submited");

        }
        else if (order_status.equals("5"))
        {
            //delivered
            reviewStatus.setText("delivered");
        }

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_DISTRIBUTOR_ORDER_DETAILS,
                GET_DISTRIBUTOR_ORDER_DETAILS +orderid, this, true);
        /*expandableListDetail = getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        listAdapter = new StockistExpandableListAdapter(getApplicationContext(), expandableListTitle, getData());
        expandableListView.setAdapter(listAdapter);*/
//        reviewStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(getApplicationContext(), DistributorOrderStatusActivity.class);
//                startActivity(intent);
//            }
//        });
       // get_distOrderdetailsapi();
    }

    private void get_distOrderdetailsapi() {
       // http://www.ordergenie.co.in/api/orderdetails/getOrdersDetails/10760

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INVENTORY,
                AppConfig.GET_STOCKIST_INVENTORY + "1240", this, true);

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if(response!=null)
        {
            Log.d("DistOrderdetails", String.valueOf(response));
            try {

                if(f_name.equals(AppConfig.GET_STOCKIST_INVENTORY)){

                    distorderDetailsList = new ArrayList<m_stockist_orderdetails>();
                    String jsonData = response.toString();
                    if(!jsonData.isEmpty()){

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Type type = new TypeToken<List<m_stockist_orderdetails>>(){}.getType();
                        distorderDetailsList = gson.fromJson(jsonData,type);

                    }
                   // Log.d("orderDlist", String.valueOf(distorderDetailsList));
                    //expand_orderAdpt(distorderDetailsList);
                }

                if(f_name.equals(AppConfig.GET_DISTRIBUTOR_ORDER_DETAILS))
                {
                    distorderDetailsList = new ArrayList<m_stockist_orderdetails>();
                    line_items=new ArrayList<Product_info_items>();
                    String jsonData = response.toString();
                    if(!jsonData.isEmpty()){

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Type type = new TypeToken<List<m_stockist_orderdetails>>(){}.getType();
                        distorderDetailsList = gson.fromJson(jsonData,type);

                        for (int i=0;i<distorderDetailsList.size();i++)
                        {
                            productcatolg_modal=new Product_info_items();
                            //line_items=new ArrayList<Product_info_items>();
                            productcatolg_modal.setMfgName(distorderDetailsList.get(i).getManufacturer_Name());
                            productcatolg_modal.setRate(distorderDetailsList.get(i).getRate());
                            productcatolg_modal.setScheme(distorderDetailsList.get(i).getScheme());
                            productcatolg_modal.setStatus(distorderDetailsList.get(i).getStatus());
                            line_items.add(productcatolg_modal);
                        //    Log.d("line_items",String.valueOf(line_items.size()));
                         //   Log.d("line_items",productcatolg_modal.toString());
                            OrderAmount += Double.valueOf(distorderDetailsList.get(i).getPrice());

                        }
                        order_amount.setText(String.format("%.1f", OrderAmount));
                        m_stockist_orderdetails=new m_stockist_orderdetails();
                        m_stockist_orderdetails.setLine_items(line_items);
                        //Log.d("line_itemss", String.valueOf(m_stockist_orderdetails.getLine_items()));
                    }
                    expand_orderAdpt(distorderDetailsList,line_items);
                }

            }catch (Exception e)
            {

            }
        }
    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    private void expand_orderAdpt(List<m_stockist_orderdetails> distorderDetailsList,List<Product_info_items> line_items) {

        listAdapter = new StockistExpandableListAdapter(getApplicationContext(),distorderDetailsList,line_items);
        expandableListView.setAdapter(listAdapter);
    }

}
