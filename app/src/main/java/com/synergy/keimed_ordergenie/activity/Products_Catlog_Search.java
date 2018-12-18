package com.synergy.keimed_ordergenie.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.ManufactureListAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.ChemistInventorymodel;
import com.synergy.keimed_ordergenie.model.Model_Prdct_Ctlg;
import com.synergy.keimed_ordergenie.utils.Utility;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;
public class Products_Catlog_Search extends AppCompatActivity implements ProductListAdapter.ContactsAdapterListener, ManufactureListAdapter.ContactsAdapterListener {

    private RadioGroup radioGroup;
    RecyclerView recyclerView;
    private RadioButton sound, vibration, silent;
    Boolean flag = false;
    RelativeLayout rltSearch;
    String Customer_type, stokiest_id;
    String  _ID;
    private List<ChemistInventorymodel> postss = new ArrayList<ChemistInventorymodel>();
    private String accessKey;
    ProductListAdapter mAdapter;
    ManufactureListAdapter mAdapterM;
    SearchView searchView, searchvieww_mname;
    String stockflag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products__catlog__search);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        stokiest_id = getIntent().getStringExtra("Stokist_ID");
        _ID = pref.getString(CLIENT_ID,"");


        searchView = (SearchView) findViewById(R.id.searchview_pname);
        searchvieww_mname = (SearchView) findViewById(R.id.searchview_mname);
        searchView.setIconifiedByDefault(false);
        searchvieww_mname.setIconifiedByDefault(false);

        searchvieww_mname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_LONG).show();
                searchvieww_mname.setIconifiedByDefault(false);
                //   searchView.requestFocusFromTouch();
                onSearchRequested();
                //  showInputMethod(v.findFocus());
            }
        });
        initViews();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

//        Log.d("click_productradio10",stokiest_id);
        accessKey = pref.getString("key", "");
        //  Stockist_productsAPI(stokiest_id);
        // @drawable/rounded_corner"
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup1);

        sound = (RadioButton) findViewById(R.id.rd_searchproduct);
        vibration = (RadioButton) findViewById(R.id.rd_searchmanu_Name);
        //silent = (RadioButton) findViewById(R.id.silent);

        flag = true;

        Log.d("click_productradio111", "click product btn");


        stockflag = "1";

        //   mAdapterM.notifyDataSetChanged();
        search_productswithDetails(stokiest_id);
                    /*Toast.makeText(getApplicationContext(), "choice: Silent",

                            Toast.LENGTH_SHORT).show();*/
/*rltSearch.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        searchView.setFocusable(true);
    }
});*/

        searchvieww_mname.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
      //  searchView.setFocusableInTouchMode(true);
        product_searchview();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if (checkedId == R.id.rd_searchproduct) {

                    flag = true;

                    Log.d("click_productradio111", "click product btn");


                    stockflag = "1";


                    search_productswithDetails(stokiest_id);


                    searchvieww_mname.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    product_searchview();


                } else if (checkedId == R.id.rd_searchmanu_Name) {


                    searchView.setVisibility(View.GONE);
                    searchvieww_mname.setVisibility(View.VISIBLE);
                    product_searchviewMName();
                    Log.d("click_productradio1111", "click manufacture btn");
                    stockflag = "2";
                 /*   if (stokiest_id.equals("0") && postss.isEmpty()){

                        Log.d("click_productradio1112", "click manufacture button");
                        search_productswithDetails(stokiest_id);
                    }*/

                    search_productswithDetails(stokiest_id);

                } else {

                   /* Toast.makeText(getApplicationContext(), "choice: Vibration",

                            Toast.LENGTH_SHORT).show();*/

                }

            }

        });


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//               if (stockflag.equals("1")) {
//                   mAdapter.getFilter().filter(query);
//               }else if(stockflag.equals("2")){
//                   mAdapterM.getFilter().filter(query);
//               }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//
//               // mAdapter.getFilter().filter(newText);
//
//                if (stockflag.equals("1")) {
//                    mAdapter.getFilter().filter(newText);
//                }else if(stockflag.equals("2")){
//                    mAdapterM.getFilter().filter(newText);
//                }
//
//
//                return false;
//            }
//
//        });
//
//
//        searchView.setOnCloseListener(new  SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//               // _toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //_toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.GONE);
//            }
//        });


    }

    private void product_searchviewMName() {
        searchvieww_mname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (stockflag.equals("1")) {
                    mAdapter.getFilter().filter(query);
                } else if (stockflag.equals("2")) {
                    mAdapterM.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                // mAdapter.getFilter().filter(newText);

                if (stockflag.equals("1")) {
                    mAdapter.getFilter().filter(newText);
                } else if (stockflag.equals("2")) {
                    mAdapterM.getFilter().filter(newText);
                }


                return false;
            }

        });


        searchvieww_mname.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // _toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchvieww_mname.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //_toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    private void product_searchview() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (stockflag.equals("1")) {
                    mAdapter.getFilter().filter(query);
                } else if (stockflag.equals("2")) {
                    mAdapterM.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                // mAdapter.getFilter().filter(newText);

                if (stockflag.equals("1")) {
                    mAdapter.getFilter().filter(newText);
                } else if (stockflag.equals("2")) {
                    mAdapterM.getFilter().filter(newText);
                }


                return false;
            }

        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // _toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //_toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.GONE);
            }
        });

    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_productdatalist);


    }

    private void search_productswithDetails(String stockist_id) {
        Utility.showProgress(this);
        Log.d("click_productradio11", "click radio buttn");

        //  Utility.showSyncDialog(this);
        // String url = AppConfig.GET_STOCKIST_INVENTORY + clientId;
        String _key = "M"; //Multiple or list
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(stockist_id); //stockist
        jsonArray.put(1); //salesman flag
        jsonArray.put(""); //search key
        jsonArray.put(-99);//
        jsonArray.put(_key);//for multiple
        jsonArray.put(_ID); //user id

        //Log.d("jsonArray111", jsonArray.toString());
        // String url = AppConfig.GET_SALESMAN_INVENTORY + jsonArray;
        String url = AppConfig.GET_SALESMAN_INVENTORY + jsonArray;
        Log.d("click_productradio12", url);
        //   final JSONArray j_arr1= legenddata;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    Log.d("click_productradio13", response.toString());


                    postss = new ArrayList<>();
                    String jsondata = response.toString();
                    if (!jsondata.isEmpty()) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        Type listType = new TypeToken<List<ChemistInventorymodel>>() {
                        }.getType();
                        postss = mGson.fromJson(jsondata, listType);

                        //ChemistInventorymodel
                    }
                    Utility.hideProgress();
                    fill_Searchproducts_List(postss, stockflag);
                    // fill_exapndableList(postss, legend_color_data);


                    //   Utility.hideSyncDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress();
                //  Utility.hideSyncDialog();
                // Log.d("productListResp", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessKey);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void fill_Searchproducts_List(List<ChemistInventorymodel> postss, String stockflag) {
        //  txt_cust_count.setText(posts_s.size() + " Customers");
//        Log.d("size", String.valueOf(postss.size()));
//        mAdapter = new ProductListAdapter(getApplicationContext(), postss, this);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.scrollToPosition(0);

        if (stockflag.equals("1")) {

            Log.d("click_productradio14", "Stock flag 1");
            mAdapter = new ProductListAdapter(getApplicationContext(), postss, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(0);
        } else if (stockflag.equals("2")) {

            Log.d("click_productradio15", "Stock flag 2");

       mAdapterM = new ManufactureListAdapter(getApplicationContext(), postss, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapterM);
            recyclerView.scrollToPosition(0);


        }

        //CustomerListAdapter
    }

    @Override
    public void onContactSelected(ChemistInventorymodel contact, View v, int position) {

   //     Log.d("click12", "Name:" + contact.getIM());
        Log.d("click13", "Position:" + String.valueOf(position));

    }



    /*private void fill_Searchproducts_List(final List<ChemistInventorymodel> postss) {
        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {
//adapter_historyy_orderss
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(Products_Catlog_Search.this);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.adapter_product_catlog, parent, false);

                return new BindingViewHolder(binding.getRoot());
            }


            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position) {
                ChemistInventorymodel order_list = postss.get(position);
                holder.getBinding().setVariable(BR.v_ordertlist, order_list);
                holder.getBinding().executePendingBindings();

                TextView p_Itemname = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_productitemname);
                TextView tx_psizee = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_psize);
                TextView tx_pmanufaturename = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_manufacturname);
                TextView tx_prate = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_rate);
              *//*  TextView orderDate = (TextView) holder.getBinding().getRoot().findViewById(R.id.orderDatee);
                TextView invoice_Idd = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoice_Id);
                TextView h_invoicedate = (TextView) holder.getBinding().getRoot().findViewById(R.id.h_invoicedate);
                TextView invoicedate = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoicedate);
                TextView h_invoiceamount = (TextView) holder.getBinding().getRoot().findViewById(R.id.h_invoiceamount);
                TextView invoiceamount = (TextView) holder.getBinding().getRoot().findViewById(R.id.invoiceamount);
                TextView Orderamountt = (TextView) holder.getBinding().getRoot().findViewById(R.id.orderamount);
                *//*


                p_Itemname.setText(postss.get(position).getIM());
                tx_psizee.setText(postss.get(position).getPZ());
                tx_pmanufaturename.setText(postss.get(position).getMfgN());
                tx_prate.setText(postss.get(position).getRate());









                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {




                    }
                });


                *//*if (order_list.getType() == -99) {
     *//**//*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));*//**//*

                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));

                } else {
                    if (order_list.getStatus() < 4) {

                        *//**//*((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_orange));*//**//*

                        ((ImageView) holder.getBinding().getRoot().findViewById(R.id.billStatus)).setImageDrawable(getResources().getDrawable(R.drawable.square_red));
                    }
                }*//*

            }

            @Override
            public int getItemCount() {
               *//* if (getIntent().getBooleanExtra(CHEMIST_IS_LAST_10_ORDER, false)) {
                    if (posts_s.size() > 10) {
                        return 10;
                    } else {
                        return posts_s.size();
                    }
                } else {
                    return posts_s.size();
                }*//*

              return postss.size();
            }
            @Override
            public LayoutInflater.Filter getFilter() {
                return new LayoutInflater.Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence) {
                        String charString = charSequence.toString();
                        if (charString.isEmpty()) {
                            contactListFiltered = contactList;
                        } else {
                            List<Contact> filteredList = new ArrayList<>();
                            for (Contact row : contactList) {

                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                                    filteredList.add(row);
                                }
                            }

                            contactListFiltered = filteredList;
                        }

                        FilterResults filterResults = new FilterResults();
                        filterResults.values = contactListFiltered;
                        return filterResults;
                    }

                    @Override
                    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                        contactListFiltered = (ArrayList<Contact>) filterResults.values;
                        notifyDataSetChanged();
                    }
                };
            }

            public interface ContactsAdapterListener {
                void onContactSelected(Contact contact);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //    rv_orderlist.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     //   searchView.clearFocus();
        finish();
    }
}


