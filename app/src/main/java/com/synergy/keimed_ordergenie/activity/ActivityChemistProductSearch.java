package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;


import com.synergy.keimed_ordergenie.BR;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.synergy.ordergenie.BR;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_all_full_product_chemist_search;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

public class ActivityChemistProductSearch extends AppCompatActivity implements SearchView.OnQueryTextListener
        , MakeWebRequest.OnResponseSuccess {

    SearchView mysearchView;
//Checking
    @BindView(R.id.rv_orderlist)
    RecyclerView rv_orderlist;

    @BindView(R.id.txt_no_product)
    TextView empty_view;

    @BindView(R.id.txt_product_count)
    TextView txt_product_count;


    private String searched_text;

    List<m_all_full_product_chemist_search> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_product_search);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            // recycleradapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.search_medicine, menu);

        MenuItem item_search = menu.findItem(R.id.action_search);
        mysearchView = (SearchView) MenuItemCompat.getActionView(item_search);
        mysearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        mysearchView.setFocusable(true);
        mysearchView.setIconified(false);
        mysearchView.setQueryHint("Search your medicines");

        int searchPlateId = mysearchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = mysearchView.findViewById(searchPlateId);
        //if (searchPlate != null) {
        searchPlate.setBackgroundColor(Color.WHITE);
        int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        final TextView searchText = (TextView) searchPlate.findViewById(searchTextId);

        //  Typeface type = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        //searchText.setTypeface(type);

        if (searchText != null) {
            searchText.setTextColor(Color.parseColor("#4e5572"));
            searchText.setHintTextColor(Color.LTGRAY);
        }


        int searchCloseID = searchPlate.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView iv_searchClose = (ImageView) searchPlate.findViewById(searchCloseID);

        iv_searchClose.setImageResource(R.drawable.close_blue);

        iv_searchClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (searchText.getText().toString().isEmpty()) {
                    finish();
                } else {
                    searchText.setText("");

                }

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.length() > 1) {
            searched_text = newText.replace(" ", "").replace("-", "");
            MakeWebRequest.MakeWebRequest("get", AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DATA,
                    AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DATA + newText, this, false);
        } else {
            fill_search_adapter(new ArrayList<m_all_full_product_chemist_search>());
        }
/*
        if (newText.length() > 1 ) {
            // get_db_search(newText);
        }*/
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private void fill_search_adapter(final List<m_all_full_product_chemist_search> posts_s) {

        txt_product_count.setText(posts_s.size() + " Results for '" + searched_text + "'");
        if (posts_s.isEmpty()) {
            empty_view.setVisibility(View.VISIBLE);
            rv_orderlist.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            rv_orderlist.setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {

            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(ActivityChemistProductSearch.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.invoice_header, parent, false);
                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                m_all_full_product_chemist_search order_list = posts_s.get(position);
                holder.getBinding().setVariable(BR.v_serchedproduct, order_list);
                holder.getBinding().executePendingBindings();

                if (posts_s.get(position).getHeading_type() == 1) {
                    holder.getBinding().getRoot().findViewById(R.id.heading_molecule).setVisibility(View.VISIBLE);
                    holder.getBinding().getRoot().findViewById(R.id.heading_products).setVisibility(View.GONE);
                    holder.getBinding().getRoot().findViewById(R.id.molecule_rl).setVisibility(View.GONE);
                    holder.getBinding().getRoot().findViewById(R.id.product_lnr).setVisibility(View.GONE);
                } else if (posts_s.get(position).getHeading_type() == 2) {
                    holder.getBinding().getRoot().findViewById(R.id.heading_molecule).setVisibility(View.GONE);
                    holder.getBinding().getRoot().findViewById(R.id.heading_products).setVisibility(View.VISIBLE);
                    holder.getBinding().getRoot().findViewById(R.id.molecule_rl).setVisibility(View.GONE);
                    holder.getBinding().getRoot().findViewById(R.id.product_lnr).setVisibility(View.GONE);
                } else {

                    holder.getBinding().getRoot().findViewById(R.id.heading_molecule).setVisibility(View.GONE);
                    holder.getBinding().getRoot().findViewById(R.id.heading_products).setVisibility(View.GONE);


                    if (posts_s.get(position).getIsMolecule() == 1) {
                        holder.getBinding().getRoot().findViewById(R.id.molecule_rl).setVisibility(View.VISIBLE);
                        holder.getBinding().getRoot().findViewById(R.id.product_lnr).setVisibility(View.GONE);
                        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(ActivityChemistProductSearch.this, MoleculesTab.class);
                                i.putExtra("Molecule_id", posts_s.get(position).getMolecule_ID());
                                startActivity(i);
                            }
                        });

                    } else {
                        holder.getBinding().getRoot().findViewById(R.id.molecule_rl).setVisibility(View.GONE);
                        holder.getBinding().getRoot().findViewById(R.id.product_lnr).setVisibility(View.VISIBLE);
                        try {

                            ((TextView) holder.getBinding().getRoot().findViewById(R.id.txt_count)).setText(posts_s.get(position).getPacksize() + " " + "Tablet");
                            // ((TextView) holder.getBinding().getRoot().findViewById(R.id.txt_count)).setText(posts_s.get(position).getPacksize() + " " + posts_s.get(position).getProductFormImage().substring(0, posts_s.get(position).getProductFormImage().length() - 4));
                        } catch (Exception e) {
                        }
                        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(ActivityChemistProductSearch.this, ProductDetail.class);
                                i.putExtra("product_id", posts_s.get(position).getProduct_ID());
                                startActivity(i);

                            }
                        });
                    }
                }
            }


            @Override
            public int getItemCount() {
                return posts_s.size();
            }
        };
        rv_orderlist.setLayoutManager(new LinearLayoutManager(this));
        rv_orderlist.setAdapter(adapter);
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if (response != null) {
            try {

                if (f_name.equals(AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DATA)) {

                    if (!response.toString().isEmpty()) {
                        GsonBuilder builder = new GsonBuilder();

                        Gson mGson = builder.create();

                        posts = new ArrayList<m_all_full_product_chemist_search>();

                        m_all_full_product_chemist_search o_products = new m_all_full_product_chemist_search();
                        o_products.setHeading_type(1);
                        posts.add(o_products);

                        for (int i = 0; i < response.length(); i++) {

                            if (response.getJSONObject(i).getInt("isMolecule") == 1) {
                                o_products = new m_all_full_product_chemist_search();
                                o_products.setHeading_type(0);
                                o_products.setIsMolecule(1);

                                o_products.setMolecule_ID(response.getJSONObject(i).getString("Molecule_ID"));
                                o_products.setOGMolecule_id(response.getJSONObject(i).getString("OGMolecule_id"));
                                o_products.setMolecule_Code(response.getJSONObject(i).getString("Molecule_Code"));
                                o_products.setMolecule_Desc(response.getJSONObject(i).getString("Molecule_Desc"));


                                posts.add(o_products);
                            }
                        }

                        if (posts.size() == 1) {
                            posts = new ArrayList<m_all_full_product_chemist_search>();
                        }

                        o_products = new m_all_full_product_chemist_search();
                        o_products.setHeading_type(2);

                        for (int i = 0; i < response.length(); i++) {

                            if (response.getJSONObject(i).getInt("isMolecule") == 0) {
                                posts.add(o_products);
                                o_products = new m_all_full_product_chemist_search();
                                o_products.setHeading_type(0);
                                o_products.setIsMolecule(0);
                                o_products.setProduct_Code(response.getJSONObject(i).getString("Product_Code"));
                                o_products.setProduct_Desc(response.getJSONObject(i).getString("Product_Desc"));
                                o_products.setProduct_ID(response.getJSONObject(i).getInt("Product_ID"));
                                o_products.setMainImagePath(response.getJSONObject(i).getString("MainImagePath"));
                                o_products.setManufacturer_Name(response.getJSONObject(i).getString("Manufacturer_Name"));
                                o_products.setMRP(Float.parseFloat(response.getJSONObject(i).getString("MRP")));
                                o_products.setPacksize(response.getJSONObject(i).getString("Packsize"));
                                o_products.setPTR(Float.parseFloat(response.getJSONObject(i).getString("PTR")));
                                o_products.setRecordcount(response.getJSONObject(i).getInt("recordcount"));

                                if (i == (response.length() - 1)) {
                                    posts.add(o_products);
                                }

                            }
                        }

                        //     posts = Arrays.asList(mGson.fromJson(response.toString(), m_all_full_product_chemist_search[].class));

                        if (posts != null) {
                            fill_search_adapter(posts);
                        }
                    }
                }
            } catch (Exception e) {

            }
        }


    }

}
