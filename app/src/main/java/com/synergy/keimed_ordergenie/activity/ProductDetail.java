package com.synergy.keimed_ordergenie.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.fragment.ProductDetailFragment;
import com.synergy.keimed_ordergenie.fragment.moleculesInfoFragment;
import com.synergy.keimed_ordergenie.fragment.moleculesIntractionFragment;
import com.synergy.keimed_ordergenie.fragment.moleculesProductFragment;
import com.synergy.keimed_ordergenie.model.m_General_Info;
import com.synergy.keimed_ordergenie.model.m_all_full_product_chemist_search;
import com.synergy.keimed_ordergenie.model.m_drug_intraction;
import com.synergy.keimed_ordergenie.model.m_molecule_interaction;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.ProductViewPagerAdapter;
import com.synergy.keimed_ordergenie.utils.SlidingTabLayout;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_PRODUCT_DETAIL;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class ProductDetail extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    private String sMemberId;
    ViewPager pager;
    private String titles[] = new String[]{"Details", "Info", "Interaction","Substitute"};
    private ProgressDialog pDialog;
    SlidingTabLayout slidingTabLayout;
    private String iProductId, productname;
    private String product_id;
    List<m_drug_intraction> posts_drug_intraction;
    List<m_molecule_interaction> posts_drug_molecule_intraction;
    List<m_General_Info>    posts_molecule_general_info;
    ActionBar actionBar;
    private String j_son_data,Chemist_id;
    private SharedPreferences pref;
    List<m_all_full_product_chemist_search> sub_products=new ArrayList<m_all_full_product_chemist_search>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_tab);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        getIntenet();


         actionBar = getSupportActionBar();


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pager = (ViewPager) findViewById(R.id.viewpager);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        //  inflater.inflate(R.menu.menu_shoppingcart, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);

    }


    private void getIntenet() {


        product_id = String.valueOf(getIntent().getIntExtra("product_id", 0));
        Chemist_id=pref.getString(CLIENT_ID,"");

        Log.d("print_DATA", product_id +" "+Chemist_id);

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL,
                AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL + "["+product_id+","+Chemist_id+"]", this, true);


    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

       // Log.d("informationResponce11", String.valueOf(response));
        if (response != null) {
            try {
           //     Log.e("ProductDetails",response.toString());
                if(f_name.equals(GET_PRODUCT_DETAIL)) {


                    GsonBuilder builder = new GsonBuilder();

                    Gson mGson = builder.create();
                    posts_molecule_general_info = Arrays.asList(mGson.fromJson(response.getJSONArray("medicineDetails").toString(), m_General_Info[].class));
                    posts_drug_intraction = Arrays.asList(mGson.fromJson(response.getJSONArray("drugDrugData").toString(), m_drug_intraction[].class));
                    sub_products= Arrays.asList(mGson.fromJson(response.getJSONArray("SuggestedProducts").toString(), m_all_full_product_chemist_search[].class));

                    set_fragment(j_son_data.toString());

                    slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
                    slidingTabLayout.setViewPager(pager);


                    slidingTabLayout.setDistributeEvenly(true);
                    slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                        @Override
                        public int getIndicatorColor(int position) {
                            return getResources().getColor(R.color.white);
                        }
                    });

                }
            } catch (Exception e) {
               e.toString();
            }
        }

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        try {
            if (response != null) {
                if (f_name.equals(AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL)) {
                    j_son_data = response.getJSONObject(0).toString();

                    actionBar.setTitle(response.getJSONObject(0).getString("Itemname"));
                    MakeWebRequest.MakeJsonObjectRequest("get", AppConfig.GET_PRODUCT_DETAIL,
                            GET_PRODUCT_DETAIL + product_id, this, true);

                }
            }
        }catch (Exception e)
        {

        }
    }

    void set_fragment(String j_obj)
    {
        Bundle bundle = null;

        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getSupportFragmentManager());


        if(j_obj!=null) {
            bundle = new Bundle();
            bundle.putString("j_obj", j_obj);
        }
        ProductDetailFragment f15 = new ProductDetailFragment();
        f15.setArguments(bundle);



        if(posts_molecule_general_info!=null) {

         //   Log.d("informationResponce12", String.valueOf(posts_molecule_general_info));

            bundle = new Bundle();
            bundle.putParcelableArrayList("info", new ArrayList(posts_molecule_general_info));
        }
        moleculesInfoFragment f14 = new moleculesInfoFragment();
        f14.setArguments(bundle);



        if(posts_drug_intraction!=null) {
            bundle = new Bundle();
            bundle.putParcelableArrayList("drug_interaction", new ArrayList(posts_drug_intraction));
        }
        if(posts_drug_molecule_intraction!=null) {
            bundle = new Bundle();
            bundle.putParcelableArrayList("mol_mol_interaction", new ArrayList(posts_drug_molecule_intraction));
        }
        moleculesIntractionFragment f13 = new moleculesIntractionFragment();
        f13.setArguments(bundle);


        if(sub_products!=null) {
            bundle = new Bundle();
            bundle.putParcelableArrayList("product_detail", new ArrayList(sub_products));
            bundle.putString("heading","Suggested Products");
        }
        // bundle.putParcelableArrayList("mol_mol_interaction", new ArrayList(posts_drug_molecule_intraction));
        moleculesProductFragment f12 = new moleculesProductFragment();
        f12.setArguments(bundle);



        adapter.addFragment(f15, titles[0]);
        adapter.addFragment(f14, titles[1]);
        adapter.addFragment(f13, titles[2]);
        adapter.addFragment(f12, titles[3]);
        pager.setAdapter(adapter);



        }
    }





