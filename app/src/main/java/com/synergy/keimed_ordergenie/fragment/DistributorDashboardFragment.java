package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.activity.DistributorPendingBills;
import com.synergy.keimed_ordergenie.activity.DistributorProductcatlog;
import com.synergy.keimed_ordergenie.activity.DistributorReturnsActivity;
import com.synergy.keimed_ordergenie.activity.DistributorsOrderList;
import com.synergy.keimed_ordergenie.adapter.TopSkuListAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.InventorylistActivity;
import com.synergy.keimed_ordergenie.adapter.TopcustomersDistri;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.TopSkuInfo;
import com.synergy.keimed_ordergenie.model.d_month_wise_graph;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_DISTRIBUTOR_MONTH_GRAPH;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class DistributorDashboardFragment extends Fragment implements View.OnClickListener, MakeWebRequest.OnResponseSuccess {
    RecyclerView recyclerView, recyclerView1;
    //NovoProductListAdapter doctorsAdapter;
    TopSkuListAdapter TopSkuAdapter;
    private List<Map<String,String >> list_data=new ArrayList<Map<String,String >>();
    TextView textview_day,textview_week,textview_month;
    LinearLayout layout_Day,layout_Week,layout_Month;
    TopcustomersDistri topcustomersDistri;
    List<d_month_wise_graph> posts;
    Boolean isClicked = false;
    //    List<TopSkuInfo> productList;
    // List<NovoProductInfo> productList;
    List<TopSkuInfo> productList;
    List<TopSkuInfo> topcustomersList;
    private Context context;
    String[] country = {"India", "USA", "China", "Japan", "Other",};
    ImageButton btn;
    String task_complete = "40", task_total = "100", count = "7";
    // private ad_offersliding ad_view_pager;
    private TextView tx_completedtask, tx_totaltask;
    static int i = 0;
    private final Handler handler = new Handler();
    SharedPreferences preferences;
    ProgressBar simpleProgressBar;
    int countt;
    //ViewPager vp;
    BarChart chart;
    private Image Graph_bar;
    private Date current_date = Calendar.getInstance().getTime();
    String Stockist_id;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // PageIndicator _indicator;
    public static String avgOrder = "";
    private TextView txt_avgorder,txt_avgsales,txt_date,txt_orderreceive,txt_salesgenerated,txt_saleslnreturn,txt_pendingbill;
    private TextView txt_order_taken,txt_payment,txt_order_deliver,txt_orderreturn;
    private OnmenuitemSelected mListener;
    public static DistributorDashboardFragment newInstance() {
        DistributorDashboardFragment fragment = new DistributorDashboardFragment();
        return fragment;

    }

    public DistributorDashboardFragment()
    {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnmenuitemSelected) {
            mListener = (OnmenuitemSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnmenuitemSelected.");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        preferences = getContext().getSharedPreferences(PREF_NAME, getContext().MODE_PRIVATE);
        Stockist_id = preferences.getString(CLIENT_ID, "0");
        Log.d("Stockist_id",Stockist_id);
        Log.d("current_date",current_date.toString());
        distributor_mothwise_graph();
        distributor_weekwisedata();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v_view = inflater.inflate(R.layout.distributor_fragment_dashboard_new, container, false);
        //distributor_daywisedata();
        textview_day = (TextView) v_view.findViewById(R.id.textview_day);
        textview_week = (TextView) v_view.findViewById(R.id.textview_week);
        textview_month = (TextView) v_view.findViewById(R.id.textview_month);

        layout_Day = (LinearLayout) v_view.findViewById(R.id.layout_Day);
        layout_Week = (LinearLayout) v_view.findViewById(R.id.layout_Week);
        layout_Month = (LinearLayout) v_view.findViewById(R.id.layout_Month);

        layout_Day.setOnClickListener(this);
        layout_Week.setOnClickListener(this);
        layout_Month.setOnClickListener(this);


      //  textview_day.setTextColor(Color.WHITE);
       // layout_Day.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        txt_avgorder=(TextView)v_view.findViewById(R.id.tx_averageorder);
        txt_avgsales=(TextView)v_view.findViewById(R.id.tx_averagesales);
        txt_date=(TextView)v_view.findViewById(R.id.txt_time);
        txt_orderreceive=(TextView)v_view.findViewById(R.id.tx_order_received);
        txt_salesgenerated=(TextView)v_view.findViewById(R.id.tx_sales_genen);
        txt_saleslnreturn=(TextView)v_view.findViewById(R.id.tx_order_salesreturn);
        txt_pendingbill=(TextView)v_view.findViewById(R.id.tx_pending_bill);
        txt_order_taken=(TextView)v_view.findViewById(R.id.tx_order_taken);
        txt_payment=(TextView)v_view.findViewById(R.id.tx_paymentcollect);
        txt_order_deliver=(TextView)v_view.findViewById(R.id.tx_order_deliver);
        txt_orderreturn=(TextView)v_view.findViewById(R.id.tx_order_return);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy'T'HH:mm:ss.SSS'Z'");
        String Day=get_day_function(current_date);
        Log.d("Day",Day);
        txt_date.setText(Day+","+sdf1.format(current_date));


        simpleProgressBar = (ProgressBar) v_view.findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setMax(100); // 100 maximum value for the progress value
        simpleProgressBar.setProgress(50); // 50 default progress value for the progress bar
        //vp = (ViewPager) v_view.findViewById(R.id.viewPager);
        //   _indicator = (PageIndicator) v_view.findViewById(R.id.indicator);
        tx_completedtask = (TextView) v_view.findViewById(R.id.tx_completedtask);
        tx_totaltask = (TextView) v_view.findViewById(R.id.tx_totaltask);
        //tx_completedtask.setText(task_complete + " Task Completed");
        tx_totaltask.setText("Total " + task_total);
        chart = (BarChart) v_view.findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(9f, 6));

        BarDataSet dataset = new BarDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Mon");
        labels.add("Tues");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");
        labels.add("Sun");

        /* for create Grouped Bar chart
        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(4f, 0));
        group1.add(new BarEntry(8f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(12f, 3));
        group1.add(new BarEntry(18f, 4));
        group1.add(new BarEntry(9f, 5));

        ArrayList<BarEntry> group2 = new ArrayList<>();
        group2.add(new BarEntry(6f, 0));
        group2.add(new BarEntry(7f, 1));
        group2.add(new BarEntry(8f, 2));
        group2.add(new BarEntry(12f, 3));
        group2.add(new BarEntry(15f, 4));
        group2.add(new BarEntry(10f, 5));

        BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);
        */

        BarData data = new BarData(labels, dataset);
        //dataset.setBarSpacePercent(30f);
        //  String count1 = "100"
        // chart.getLayoutParams().width=100*7;
        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        chart.setData(data);
        chart.animateY(5000);
        chart.setDrawValueAboveBar(false);
        chart.setDrawGridBackground(false);

        XAxis bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        topcustomersList = new ArrayList<>();
        productList = new ArrayList<>();
        TopSkuAdapter = new TopSkuListAdapter(productList);
        topcustomersDistri = new TopcustomersDistri(topcustomersList);

        recyclerView = (RecyclerView) v_view.findViewById(R.id.recycler_topsku);
        recyclerView1 = (RecyclerView) v_view.findViewById(R.id.recycler_topcustomers);

      //  String name_chem = preferences.getString(CLIENT_FULL_NAME, "");
        if (!isClicked)
        {
            textview_week.setTextColor(Color.WHITE);
            layout_Week.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            countt=0;
        }

        ButterKnife.bind(this, v_view);
        return v_view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
     //   distributor_daywisedata();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //distributor_daywisedata();
    }
    private String get_day_function(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String daynumber = "";

        switch (day) {

            case Calendar.MONDAY:
                daynumber = "MON";
                break;
            case Calendar.TUESDAY:
                daynumber = "TUE";
                break;

            case Calendar.WEDNESDAY:
                daynumber = "WED";
                break;
            case Calendar.THURSDAY:
                daynumber = "THU";
                break;
            case Calendar.FRIDAY:
                daynumber = "FRI";
                break;
            case Calendar.SATURDAY:
                daynumber = "SAT";
                break;

            case Calendar.SUNDAY:
                daynumber = "SUN";
                // daynumber = "7";
                // Current day is Sunday
                break;
        }
        return daynumber;
    }




//	@OnClick(R.id.submit)
//	public void sayHi(Button button) {
//		button.setText("Hello!");
//	}

//	@OnClick(R.id.submit)
//	public void submit() {
//		// TODO submit data to server...
//	}


//    @Nullable
//    @OnClick(R.id.salesreturn_fragment)
//    public void salesreturn(View view) {
//        create_salesreturnactivity();
//    }


    @Nullable
    @OnClick(R.id.received_order_fragment)
    public void showstockistlist(View view) {
        //create_Stockistlist();
        show_orders_received();
    }

    @Nullable
    @OnClick(R.id.pending_bills_distributor_fragment)
    public void showorders(View view) {
        // Show_all_order();
        show_product_catlog_inventory();
    }

    @Nullable
    @OnClick(R.id.sales_generated_fragement)
    public void showstockistlist1(View view) {
//        create_Stockistlist();
        //show_sales_generated();
    }

    @Nullable
    @OnClick(R.id.sales_return_fragment)
    public void showorders1(View view) {
        show_pending_bill();
    }


    @Override
    public void onClick(View v) {
        // implements your things
        //mListener.OnmenuitemSelected(1, "", "", "");
        checkSelectedOption();
        if(textview_week.getCurrentTextColor()== Color.WHITE){

            textview_week.setTextColor(Color.BLACK);
            layout_Week.setBackgroundColor(getResources().getColor(R.color.white));
        }

        switch (v.getId()){

            case R.id.layout_Day:

                if(textview_day.getCurrentTextColor()== Color.BLACK){

                    textview_day.setTextColor(Color.WHITE);
                    layout_Day.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                countt = 1;
                isClicked = true;
                Log.d("clickwithFrgment11","click Day");
                distributor_daywisedata();
                break;

            case  R.id.layout_Week:

                if(textview_week.getCurrentTextColor()== Color.BLACK){

                    textview_week.setTextColor(Color.WHITE);
                    layout_Week.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                countt = 2;
                isClicked = true;
                Log.d("clickwithFrgment12","click Week");

                distributor_weekwisedata();
                break;

            case R.id.layout_Month:

                if(textview_month.getCurrentTextColor()== Color.BLACK){

                    textview_month.setTextColor(Color.WHITE);
                    layout_Month.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                countt = 3;
                isClicked = true;
                Log.d("clickwithFrgment13","click Month");

                distributor_monthwisedata();
                break;

        }


    }

    private void checkSelectedOption() {

        if (isClicked) {
            if (countt == 1) {

                textview_day.setTextColor(Color.BLACK);
                layout_Day.setBackgroundColor(getResources().getColor(R.color.white));

            } else if (countt == 2) {

                textview_week.setTextColor(Color.BLACK);
                layout_Week.setBackgroundColor(getResources().getColor(R.color.white));

            }
           else if (countt == 3) {

                textview_month.setTextColor(Color.BLACK);
                layout_Month.setBackgroundColor(getResources().getColor(R.color.white));


            }

        }

    }
    public  void distributor_daywisedata()
    {
        MakeWebRequest.MakeWebRequest("get", AppConfig.DISTRIBUTOR_DASHBOARD_DAYWISE_DATA,
                AppConfig.DISTRIBUTOR_DASHBOARD_DAYWISE_DATA +"["+Stockist_id+",\""+sdf.format(current_date)+",\"]" ,getContext(), true);

    }
    public  void distributor_weekwisedata()
    {
        MakeWebRequest.MakeWebRequest("get", AppConfig.DISTRIBUTOR_DASHBOARD_WEEKWISE_DATA,
                AppConfig.DISTRIBUTOR_DASHBOARD_WEEKWISE_DATA +"["+Stockist_id+",\""+sdf.format(current_date)+",\"]" ,getContext(), true);

    }

    public  void distributor_mothwise_graph()
    {
        MakeWebRequest.MakeWebRequest("get", GET_DISTRIBUTOR_MONTH_GRAPH,
                GET_DISTRIBUTOR_MONTH_GRAPH +"["+Stockist_id+",\""+sdf.format(current_date)+",\"]" ,getContext(), true);

    }
    public  void distributor_monthwisedata()
    {
        MakeWebRequest.MakeWebRequest("get", AppConfig.DISTRIBUTOR_DASHBOARD_MONTHWISE_DATA,
                AppConfig.DISTRIBUTOR_DASHBOARD_MONTHWISE_DATA +"["+Stockist_id+",\""+sdf.format(current_date)+",\"]" ,getContext(), true);

    }
    public void getRequest(String jsonData,String url_name)
    {
        Log.d("data111",jsonData);
        Log.d("url11",url_name);
        if (url_name.equals(GET_DISTRIBUTOR_MONTH_GRAPH))
        {
            get_graph_json(jsonData);
        }
        fill_dashboard_data(jsonData);
    }


    private void get_graph_json(String jsondata) {
        // LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Chemist_Stokist.json", StockistList.this);
        //  String jsondata = //_LoadJsonFromAssets.getJson();
        if (!jsondata.isEmpty())
        {
            Log.d("GRAPH", jsondata.toString());
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            posts = new ArrayList<d_month_wise_graph>();
            posts = Arrays.asList(mGson.fromJson(jsondata, d_month_wise_graph[].class));

            // Collections.sort(posts, comparator);
            // Collections.reverse(posts);
          //  fill_graph(posts);
        }

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response)
    {

    }
    private void fill_dashboard_data(String jsonData) {
        if(!jsonData.isEmpty())
        {
            try {

                JSONArray jsonResponse = new JSONArray(jsonData);
                HashMap<String, String> meMap = null;
                list_data.clear();
                productList.clear();
                topcustomersList.clear();
                for (int i=0;i<jsonResponse.length();i++)
                {
                 JSONArray jsonArray=jsonResponse.getJSONArray(i);
                   // Log.d("DOrderlist",jsonArray.toString());
                    for (int j=0;j<jsonArray.length();j++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        Log.d("jsonObject", jsonObject.toString());
                        String data = jsonObject.getString("data");
                        String per = jsonObject.getString("per");
                        String Product_Desc = jsonObject.getString("Product_Desc");
                        Log.d("dataa", data + "" + per + "" + Product_Desc);
                        if (data.equals("sku"))
                        {

                            TopSkuInfo item1 = new TopSkuInfo(Product_Desc,per+"%");
                            productList.add(item1);
                           // Log.d("data22", data + "" + per + "" + Product_Desc);
                        }
                        if (data.equals("cust"))
                        {
                            TopSkuInfo item2 = new TopSkuInfo(Product_Desc,"Rs."+per);
                            topcustomersList.add(item2);
                            //Log.d("data23", data + "" + per + "" + Product_Desc);
                        }
                        if (data.equals("avgOrder"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_avgorder.setText(jsonObject.getString("per"));
                            //txt_avgorder.setText(String.format("%.2f", jsonObject.getString("per")));
                        }
                        if (data.equals("avgSale"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_avgsales.setText(jsonObject.getString("per"));
                            //txt_avgsales.setText(String.format("%.2f",jsonObject.getString("per")));
                        }
                        if(data.equals("OrderReceived"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_orderreceive.setText(jsonObject.getString("per"));
                        }
                         if (data.equals("SalesGenerated"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_salesgenerated.setText(jsonObject.getString("per"));
                        }
                        if (data.equals("pendingbill"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_pendingbill.setText(jsonObject.getString("per"));
                        }
                         if (data.equals("inventory"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_saleslnreturn.setText(jsonObject.getString("per"));
                        }
                         if (data.equals("PaymentCollected"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_payment.setText(jsonObject.getString("per"));
                        }
                         if (data.equals("OrderTaken"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_order_taken.setText(jsonObject.getString("per"));
                        }
                        if (data.equals("OrderDelivered"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_order_deliver.setText(jsonObject.getString("per"));
                        }
                         if (data.equals("OrderReturned"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            txt_orderreturn.setText(jsonObject.getString("per"));
                        }
                         if (data.equals("userActivity"))
                        {
                            //Log.d("data24", data + "" + per + "" + Product_Desc);
                            tx_completedtask.setText(jsonObject.getString("per")+" Task Completed");
                        }
                    }
                    // fill_dashboard_data_list(postss);
                }

                call_topsku_adapter();
                call_top_customer_list();
            }catch (Exception e)
            {
                Log.d("Excep",e.getMessage());
            }
        }
    }


    public void call_topsku_adapter()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(TopSkuAdapter);

    }

    public void call_top_customer_list()
    {
        RecyclerView.LayoutManager mmLayoutManager = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(mmLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.setNestedScrollingEnabled(false);
        recyclerView1.setAdapter(topcustomersDistri);
    }
    @Override
    public void onSuccess_json_object(String f_name, JSONObject response)
    {

    }


    public interface OnmenuitemSelected {
        void OnmenuitemSelected(int imageResId, String name, String description, String url);
    }


    private void show_orders_received() {
        //  Intent intent = new Intent(getActivity(), CustomerlistActivity.class);
        //  Intent intent = new Intent(getActivity(), DistributorNotificationActivity.class);
        Intent intent = new Intent(getContext(), DistributorsOrderList.class);
        //  Intent intent = new Intent(getActivity(), DistributorsOrderList.class);
        startActivity(intent);
    }

    private void show_sales_generated() {
        Intent intent = new Intent(getContext(), DistributorReturnsActivity.class);
        startActivity(intent);
    }

    private void show_product_catlog_inventory() {
        Intent intent = new Intent(getContext(), DistributorProductcatlog.class);
        startActivity(intent);
    }

    private void create_Inventorysactivity() {
        Intent intent = new Intent(getContext(), InventorylistActivity.class);
        startActivity(intent);
    }

    private void Show_all_order() {
        Intent intent = new Intent(getContext(), DistributorsOrderList.class);
        startActivity(intent);
    }

    private void show_pending_bill() {
        Intent intent = new Intent(getContext(), DistributorPendingBills.class);
        //  Intent intent = new Intent(getActivity(), StockistOrderDetails.class);
        //  Intent intent = new Intent(getActivity(), DistributorOrderStatusActivity.class);
        //     Intent intent = new Intent(getActivity(), DistributorsOrderList.class);
        startActivity(intent);
    }


//    public void create_offer_sliding(String jsondata) {
//
//        // Log.d("print_img",jsondata);
//
//        //LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("offerlist.json", getActivity());
//        //String jsondata = _LoadJsonFromAssets.getJson();
//        if (!jsondata.isEmpty()) {
//
//
//            GsonBuilder builder = new GsonBuilder();
//            Gson mGson = builder.create();
//
//            List<m_offerlist> posts = new ArrayList<m_offerlist>();
//            posts = Arrays.asList(mGson.fromJson(jsondata, m_offerlist[].class));
//            ad_view_pager = new ad_offersliding(getActivity(), posts);
//            _view_pager.setAdapter(ad_view_pager);
//
//
//            _indicator.setViewPager(_view_pager);
//            //  _view_pager.setPageTransformer(true, new ZoomOutPageTransformer());
//            _view_pager.setPageTransformer(true, new ZoomOutPageTransformer());
//
//
//
//            if (posts.size() != 0) {
//                handler.post(ViewPagerVisibleScroll);
//            }
//
//            //ad_view_pager.setScrollDurationFactor(4);
//            //  ad_view_pager.setScrollDurationFactor(2);
//
//        }
//
//
//
//    }

}

