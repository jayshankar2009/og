package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.ActivityChemistProductSearch;
import com.synergy.keimed_ordergenie.activity.AllPendingBills;
import com.synergy.keimed_ordergenie.activity.All_ProductsCatlog;
import com.synergy.keimed_ordergenie.activity.CustomerlistActivity;
import com.synergy.keimed_ordergenie.activity.CustomerlistActivityNew;
import com.synergy.keimed_ordergenie.activity.InventorylistActivity;
import com.synergy.keimed_ordergenie.activity.Order_list;
import com.synergy.keimed_ordergenie.activity.SalesReturnActivity;
import com.synergy.keimed_ordergenie.activity.StockistList;
import com.synergy.keimed_ordergenie.adapter.ad_offersliding;
import com.synergy.keimed_ordergenie.adapter.ad_offersliding_new;
import com.synergy.keimed_ordergenie.model.m_offerlist;
import com.synergy.keimed_ordergenie.utils.PageIndicator;
import com.synergy.keimed_ordergenie.utils.ZoomOutPageTransformer;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_FULL_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class ChemistDashboardFragment extends Fragment implements View.OnClickListener
{
    ImageButton btn;
    private ad_offersliding ad_view_pager;
    private ad_offersliding_new ad_view_pagerr;
    private TextView tx_username;
    static int i = 0;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private final Handler handler = new Handler();
    SharedPreferences preferences;
    ViewPager _view_pager;
    PageIndicator _indicator;
    private OnmenuitemSelected mListener;
    private static final Integer[] IMAGES= {R.drawable.medicines_inventory_temp,R.drawable.medicines_inventory_temp,R.drawable.medicines_inventory_temp,R.drawable.medicines_inventory_temp};
    /* Get Intent */
    private static final String SELECTED_CHEMIST_ID = "selected_chemist_id";
    private String chemistId;


    public static ChemistDashboardFragment newInstance() {
        ChemistDashboardFragment fragment = new ChemistDashboardFragment();
        return fragment;
    }

    Runnable ViewPagerVisibleScroll = new Runnable() {
        @Override
        public void run() {
            try {

                i = _view_pager.getCurrentItem() + 1;

                if (i <= ad_view_pagerr.getCount() - 1) {
                    _view_pager.setCurrentItem(i, true);

                    handler.postDelayed(ViewPagerVisibleScroll, 7500);
                    i++;
                } else {
                    i = 0;
                    _view_pager.setCurrentItem(i, true);
                    handler.postDelayed(ViewPagerVisibleScroll, 6000);
                }
            } catch (Exception e) {

            }
        }
    };

    public ChemistDashboardFragment() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(PREF_NAME, getActivity().MODE_PRIVATE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v_view = inflater.inflate(R.layout.chemist_fragment_dashboard, container, false);
        _view_pager = (ViewPager) v_view.findViewById(R.id.view_pager);
        _indicator = (PageIndicator) v_view.findViewById(R.id.indicator);
        tx_username = (TextView) v_view.findViewById(R.id.tx_username);
        //tx_username.setText(preferences.getString(CLIENT_NAME, ""));

        String name_chem = preferences.getString(CLIENT_FULL_NAME, "");
        chemistId = preferences.getString(CLIENT_ID, "");
        // tx_username.setText(preferences.getString(CLIENT_FULL_NAME, ""));

        if(name_chem.equalsIgnoreCase("")) {
            tx_username.setText("");
        } else if (name_chem.equalsIgnoreCase("null")) {
            tx_username.setText("");
        } else {
            tx_username.setText(name_chem);
        }
        create_offer_sliding_images();
        ButterKnife.bind(this, v_view);
        return v_view;
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
@OnClick(R.id.product_catlog_fragment)
public void create_products_catlogactivity(View view) {
    create_productsCatlogactivity();
}
    @Nullable
    @OnClick(R.id.stockist_fragment)
    public void showstockistlist(View view) {
        create_Stockistlist();
    }

    @Nullable
    @OnClick(R.id.order_fragement)
    public void showorders(View view) {
        Show_all_order();
    }

    @Nullable
    @OnClick(R.id.pending_fragment)
    public void create_pendingbillsactivity(View view) {
        create_pendingbillsactivity();
    }

//    @Nullable
//    @OnClick(R.id.img_search)
//    public void search_click() {
//        Intent i = new Intent(getActivity(), ActivityChemistProductSearch.class);
//        startActivity(i);
//    }


	/*@Nullable
    @OnClick(R.id.ib_orders)
	public void orderhistory(View view){
		create_Orderhistory();
	}
*/


    @Override
    public void onClick(View v) {
        // implements your things
        mListener.OnmenuitemSelected(1, "", "", "");

        //create_customeractivity();
    }


    public interface OnmenuitemSelected {
        void OnmenuitemSelected(int imageResId, String name, String description, String url);
    }


    private void create_customeractivity() {
        Intent intent = new Intent(getActivity(), CustomerlistActivity.class);
       // Intent intent = new Intent(getActivity(), CustomerlistActivityNew.class);
        startActivity(intent);

    }

    private void create_salesreturnactivity() {
        Intent intent = new Intent(getActivity(), SalesReturnActivity.class);
        startActivity(intent);

    }

    private void create_pendingbillsactivity()
    {
        Intent intent = new Intent(getActivity(), AllPendingBills.class);
        startActivity(intent);

    }

    private void create_Inventorysactivity() {
        Intent intent = new Intent(getActivity(), InventorylistActivity.class);
        startActivity(intent);

    }

    private void Show_all_order() {
        Intent intent = new Intent(getActivity(), Order_list.class);
        intent.putExtra(SELECTED_CHEMIST_ID, chemistId);
        startActivity(intent);
    }
    private void create_productsCatlogactivity() {

        Intent intent = new Intent(getActivity(), All_ProductsCatlog.class);
        startActivity(intent);

    }
    private void create_Stockistlist() {
        Intent intent = new Intent(getActivity(), StockistList.class);
        startActivity(intent);
    }

    public void create_offer_sliding_images() {

        for(int i=0;i<IMAGES.length;i++) {
            ImagesArray.add(IMAGES[i]);
        }
        ad_view_pagerr = new ad_offersliding_new(getActivity(),ImagesArray);
        _view_pager.setAdapter(ad_view_pagerr);
        _indicator.setViewPager(_view_pager);
        // _view_pager.setPageTransformer(true, new ZoomOutPageTransformer());
        _view_pager.setPageTransformer(true, new ZoomOutPageTransformer());

        if (ImagesArray.size() != 0) {
            handler.post(ViewPagerVisibleScroll);
        }

    }
    public void create_offer_sliding(String jsondata) {
        Log.d("chemistResp", jsondata);
        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            List<m_offerlist> posts = new ArrayList<m_offerlist>();
            posts = Arrays.asList(mGson.fromJson(jsondata, m_offerlist[].class));
            ad_view_pager = new ad_offersliding(getActivity(), posts);
            _view_pager.setAdapter(ad_view_pager);
            _indicator.setViewPager(_view_pager);
            //  _view_pager.setPageTransformer(true, new ZoomOutPageTransformer());
            _view_pager.setPageTransformer(true, new ZoomOutPageTransformer());

            if (posts.size() != 0) {
                handler.post(ViewPagerVisibleScroll);
            }
            //ad_view_pager.setScrollDurationFactor(4);
            //  ad_view_pager.setScrollDurationFactor(2);

        }
        else {

        }
    }

}
