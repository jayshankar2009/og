package com.synergy.keimed_ordergenie.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
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
import com.synergy.keimed_ordergenie.utils.ViewPagerAdapter;


public class MoleculesTab extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess{

    private String sMemberId;

    private static final String TAG = MainActivity.class.getSimpleName();
    //pDialog = new ProgressDialog(this);
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    ViewPagerAdapter vm;
    static Button notifCount;
    static int mNotifCount = 0;

    private Button btncheckout;

    private ListView mDrawerList;
    ViewPager pager;
    //private String titles[] = new String[]{"Details", "Molecule", "Info", "Pack Size","Alternative"};
    private String titles[] = new String[]{"Details", "Info", "Molecule Intraction"};


    private ProgressDialog pDialog;

    //private Toolbar toolbar;

    SlidingTabLayout slidingTabLayout;


    private String iProductId, productname;
    //private JSONArray ja_objproductsubdetails, ja_objMoleculedetails,ja_objOtherProductdetails, ja_objPackSizedetails,ja_objMedicinedetails = null;

    private JSONArray ja_cart, ja_summary = null;

    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;

    List<m_all_full_product_chemist_search> posts_product=new ArrayList<m_all_full_product_chemist_search>();
    List<m_drug_intraction> posts_drug_intraction=new ArrayList<m_drug_intraction>();
    List<m_molecule_interaction> posts_drug_molecule_intraction=new ArrayList<m_molecule_interaction>();
    List<m_General_Info> posts_molecule_general_info=new ArrayList<m_General_Info>();

    private  String OGMolecule_id ="990";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_molecules_tab);

        getIntenet();





        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);






        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });


        MakeWebRequest.MakeJsonObjectRequest("get", AppConfig.GET_MOLECULE_DATA,
                AppConfig.GET_MOLECULE_DATA + OGMolecule_id, this, true);

    }

    private void getIntenet() {
        Intent intent_productdetails = getIntent();
        iProductId = intent_productdetails.getStringExtra("productid");
        OGMolecule_id=intent_productdetails.getStringExtra("Molecule_id");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        productname = pref.getString("product_name", "");

        /*String s_objproductsubdetails = intent_productdetails.getStringExtra("objproductsubdetails");
        String s_objMoleculedetails = intent_productdetails.getStringExtra("objMoleculedetails");
        String s_objPackSizedetails = intent_productdetails.getStringExtra("objPackSizedetails");
        String s_objMedicinedetails = intent_productdetails.getStringExtra("objMedicinedetails");
        String s_objOtherProductdetails = intent_productdetails.getStringExtra("objOtherProductdetails");

        try
        {
            //ja_productdetails, ja_molecules, ja_alternative, ja_info

            ja_objproductsubdetails = new JSONArray(s_objproductsubdetails);
            ja_objMoleculedetails = new JSONArray(s_objMoleculedetails);
            ja_objPackSizedetails = new JSONArray(s_objPackSizedetails);
            ja_objMedicinedetails = new JSONArray(s_objMedicinedetails);
            ja_objOtherProductdetails = new JSONArray(s_objOtherProductdetails);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {


      //  Log.d("informationResponce", String.valueOf(response));

        if(response!=null)
        {
            try {


                if (f_name.equals(AppConfig.GET_MOLECULE_DATA)) {

                    if (!response.toString().isEmpty()) {
                        GsonBuilder builder = new GsonBuilder();

                        Gson mGson = builder.create();

                        posts_product = Arrays.asList(mGson.fromJson(response.getJSONArray("prodsByMolecule").toString(), m_all_full_product_chemist_search[].class));
                        posts_drug_intraction= Arrays.asList(mGson.fromJson(response.getJSONArray("drugDrugData").toString(), m_drug_intraction[].class));
                        posts_drug_molecule_intraction = Arrays.asList(mGson.fromJson(response.getJSONArray("drugMoleculeData").toString(), m_molecule_interaction[].class));
                        posts_molecule_general_info = Arrays.asList(mGson.fromJson(response.getJSONArray("moleculeInfo").toString(), m_General_Info[].class));

                        set_fragment();


                        slidingTabLayout.setViewPager(pager);


                        slidingTabLayout.setDistributeEvenly(true);
                    }

                }
            }catch (Exception e)
            {
                e.toString();
            }
        }


    }
    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {



    }

    void refresh()
    {
        Fragment page = null;
        int i=  pager.getCurrentItem();

            page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + pager.getCurrentItem());
           ((moleculesProductFragment)page).fill_search_adapter_product(posts_product);

    }

    void refresh_Molecule_gerenal_info()
    {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1);
        // ((moleculesIntractionFragment)page).fill_search_adapter_drugIntraction(posts_drug_intraction);
        ((moleculesInfoFragment)page).fill_search_adapter_info(posts_molecule_general_info);

    }

   void refresh_drugIntraction()
    {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 2);
        ((moleculesIntractionFragment)page).fill_search_adapter_drugIntraction(posts_drug_intraction);

        ((moleculesIntractionFragment)page).fill_search_adapter_drugMoleculeIntraction(posts_drug_intraction);

    }



    void set_fragment()
    {
        Bundle bundle = null;

        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getSupportFragmentManager());


        if(posts_product!=null) {
            bundle = new Bundle();
            bundle.putParcelableArrayList("product_detail", new ArrayList(posts_product));
            bundle.putString("heading","Medicine containing this molecule");
        }
        moleculesProductFragment f15 = new moleculesProductFragment();
        f15.setArguments(bundle);



        if(posts_molecule_general_info!=null) {
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

            bundle.putParcelableArrayList("mol_mol_interaction", new ArrayList(posts_drug_molecule_intraction));
        }
        moleculesIntractionFragment f13 = new moleculesIntractionFragment();
        f13.setArguments(bundle);




        adapter.addFragment(f15, titles[0]);
        adapter.addFragment(f14, titles[1]);
        adapter.addFragment(f13, titles[2]);
       // pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);



    }
}
