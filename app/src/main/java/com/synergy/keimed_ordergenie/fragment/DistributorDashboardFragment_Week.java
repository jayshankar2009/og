package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.ActivityChemistProductSearch;
import com.synergy.keimed_ordergenie.activity.AllPendingBills;
import com.synergy.keimed_ordergenie.activity.CustomerlistActivity;
import com.synergy.keimed_ordergenie.activity.DistributorCustomerList;
import com.synergy.keimed_ordergenie.activity.DistributorsOrderList;
import com.synergy.keimed_ordergenie.activity.InventorylistActivity;
import com.synergy.keimed_ordergenie.activity.SalesReturnActivity;
import com.synergy.keimed_ordergenie.adapter.TopSkuListAdapter;
import com.synergy.keimed_ordergenie.model.TopSkuInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_FULL_NAME;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by admin on 11/30/2017.
 */

public class DistributorDashboardFragment_Week extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView,recyclerView1;

    //NovoProductListAdapter doctorsAdapter;
    TopSkuListAdapter TopSkuAdapter;
    //    List<TopSkuInfo> productList;
    // List<NovoProductInfo> productList;
    List<TopSkuInfo> productList;
    ImageButton btn;
    String task_complete = "40",task_total = "100",count = "7";
    // private ad_offersliding ad_view_pager;
    private TextView tx_completedtask,tx_totaltask;
    static int i = 0;
    private final Handler handler = new Handler();
    SharedPreferences preferences;
    ProgressBar simpleProgressBar;
    //ViewPager vp;
    BarChart chart;
    private Image Graph_bar;
    // PageIndicator _indicator;


    private DistributorDashboardFragment.OnmenuitemSelected mListener;

    public static DistributorDashboardFragment newInstance() {
        DistributorDashboardFragment fragment = new DistributorDashboardFragment();
        return fragment;

    }

//    Runnable ViewPagerVisibleScroll = new Runnable() {
//        @Override
//        public void run() {
//            try {
//
//                i = _view_pager.getCurrentItem() + 1;
//
//                if (i <= ad_view_pager.getCount() - 1) {
//                    _view_pager.setCurrentItem(i, true);
//
//                    handler.postDelayed(ViewPagerVisibleScroll, 7500);
//                    i++;
//                } else {
//                    i = 0;
//                    _view_pager.setCurrentItem(i, true);
//                    handler.postDelayed(ViewPagerVisibleScroll, 6000);
//                }
//            } catch (Exception e) {
//
//            }
//        }
//    };

    public DistributorDashboardFragment_Week() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DistributorDashboardFragment.OnmenuitemSelected) {
            mListener = (DistributorDashboardFragment.OnmenuitemSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnmenuitemSelected.");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(PREF_NAME, getActivity().MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v_view = inflater.inflate(R.layout.distributor_fragment_dashboard, container, false);




        simpleProgressBar= (ProgressBar)v_view.findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setMax(100); // 100 maximum value for the progress value
        simpleProgressBar.setProgress(50); // 50 default progress value for the progress bar
        //vp = (ViewPager) v_view.findViewById(R.id.viewPager);
        //   _indicator = (PageIndicator) v_view.findViewById(R.id.indicator);
        tx_completedtask = (TextView) v_view.findViewById(R.id.tx_completedtask);
        tx_totaltask = (TextView) v_view.findViewById(R.id.tx_totaltask);
        tx_completedtask.setText(task_complete+" Task Completed");
        tx_totaltask.setText("Total "+task_total);
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

        productList = new ArrayList<>();
        TopSkuAdapter = new TopSkuListAdapter(productList);
        recyclerView=(RecyclerView)v_view.findViewById(R.id.recycler_topsku);
        recyclerView1=(RecyclerView)v_view.findViewById(R.id.recycler_topcustomers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(TopSkuAdapter);
//        recyclerView1.setLayoutManager(mLayoutManager);
//        recyclerView1.setItemAnimator(new DefaultItemAnimator());
//        recyclerView1.setAdapter(TopSkuAdapter);
        preparedoctorListData();

        //  vp = (ViewPager) findViewById(R.id.viewPager);
        //   vp.setOffscreenPageLimit(3);
        // vp.setAdapter(new MyPagesAdapter());


//        viewpager_previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int current_item= vp.getCurrentItem();
//                vp.setCurrentItem(getItem(+1), true);
//            }
//        });

        //tx_username.setText(preferences.getString(CLIENT_NAME, ""));

        String name_chem = preferences.getString(CLIENT_FULL_NAME, "");
        // tx_username.setText(preferences.getString(CLIENT_FULL_NAME, ""));

//        if(name_chem.equals("null"))
//        {
//            tx_username.setText("");
//        }
//        {
//            tx_username.setText(name_chem);
//        }

        ButterKnife.bind(this, v_view);
        return v_view;
    }

    private void preparedoctorListData() {
        TopSkuInfo item1 = new TopSkuInfo("PANKREOFLAT TABLET","7.41%");
        productList.add(item1);
        TopSkuInfo item2 = new TopSkuInfo("QUADRIDERM 10GM", "7.80%");
        productList.add(item2);

        TopSkuInfo item3 = new TopSkuInfo("ACILOC TABLET", "7.41%");
        productList.add(item3);

        TopSkuInfo item4 = new TopSkuInfo("ACAMPTAS TABLET", "7.98%");
        productList.add(item4);

        TopSkuInfo item5 = new TopSkuInfo("CARBOPLATIN 450MG", "7.41%");
        productList.add(item5);

        TopSkuAdapter.notifyDataSetChanged();

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
        create_Stockistlist();
    }

    @Nullable
    @OnClick(R.id.pending_bills_distributor_fragment)
    public void showorders(View view) {
        Show_all_order();
    }
    @Nullable
    @OnClick(R.id.sales_generated_fragement)
    public void showstockistlist1(View view) {
        create_Stockistlist();
    }

    @Nullable
    @OnClick(R.id.sales_return_fragment)
    public void showorders1(View view) {
        Show_all_order();
    }

//    @Nullable
//    @OnClick(R.id.pending_fragment)
//    public void create_pendingbillsactivity(View view) {
//        create_pendingbillsactivity();
//    }

    @Nullable
    @OnClick(R.id.img_search)
    public void search_click() {
        Intent i = new Intent(getActivity(), ActivityChemistProductSearch.class);
        startActivity(i);
    }


	/*@Nullable
    @OnClick(R.id.ib_orders)
	public void orderhistory(View view){
		create_Orderhistory();
	}
*/


    @Override
    public void onClick(View v) {

        switch (v.getId()){



        }
        // implements your things
        mListener.OnmenuitemSelected(1, "", "", "");




        //create_customeractivity();
    }


    public interface OnmenuitemSelected {
        void OnmenuitemSelected(int imageResId, String name, String description, String url);
    }


    private void create_customeractivity() {
        Intent intent = new Intent(getActivity(), CustomerlistActivity.class);
        startActivity(intent);

    }

    private void create_salesreturnactivity() {
        Intent intent = new Intent(getActivity(), SalesReturnActivity.class);
        startActivity(intent);

    }

    private void create_pendingbillsactivity() {
        Intent intent = new Intent(getActivity(), AllPendingBills.class);
        startActivity(intent);

    }

    private void create_Inventorysactivity() {
        Intent intent = new Intent(getActivity(), InventorylistActivity.class);
        startActivity(intent);

    }

    private void Show_all_order() {
        Intent intent = new Intent(getActivity(), DistributorCustomerList.class);
        startActivity(intent);
    }

    private void create_Stockistlist() {
        Intent intent = new Intent(getActivity(), DistributorsOrderList.class);
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
