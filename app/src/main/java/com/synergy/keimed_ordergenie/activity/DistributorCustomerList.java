package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.DistributorCustomerlistAdapter;
import com.synergy.keimed_ordergenie.model.m_distributor_customer;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_CUSTOMERLIST_DISTRIBUTORLOGIN;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class DistributorCustomerList extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {
    RecyclerView rv_datalist;
    List<m_distributor_customer> posts;
    SharedPreferences pref;
    private SQLiteHandler db;
    String Stockist_id;
    private SearchView searchView;
    private DistributorCustomerlistAdapter customerlistAdapter;
    Toolbar toolbar;
    TextView txt_total,showpendinglist;
    RadioGroup radioGroup_customers, rdgroup_filtercustomers;
    RadioButton rd_top_customer, rd_alphabetic, rd_recent_activity, rd_recent_order, rd_pending_bill, rd_newcustomer;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_customer_list);


        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        rv_datalist = (RecyclerView) findViewById(R.id.rv_datalist);
        txt_total = (TextView) findViewById(R.id.total_customer);

        showpendinglist = (TextView) findViewById(R.id.showpendinglist);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        rv_datalist.addItemDecoration(dividerItemDecoration);
        setTitle("Customers");
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        Stockist_id = pref.getString(CLIENT_ID, "0");
//        MakeWebRequest.MakeWebRequest("get", GET_STOCKIST_CUSTOMERLIST_STOCKISTLOGIN,
//                GET_STOCKIST_CUSTOMERLIST_STOCKISTLOGIN + Stockist_id, this, true);

        //GET_CUSTOMERLIST_DISTRIBUTORLOGIN

        MakeWebRequest.MakeWebRequest("get", GET_CUSTOMERLIST_DISTRIBUTORLOGIN,
                GET_CUSTOMERLIST_DISTRIBUTORLOGIN + Stockist_id, this, true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customer_list, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Customer Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                List<m_distributor_customer> filteredModelList = new ArrayList<>();
                for (m_distributor_customer model : posts) {
                    final String text = model.getClient_LegalName().toLowerCase();
                    if (text.contains(newText)) {
                        filteredModelList.add(model);
                    }
                }
                fill_stockist(filteredModelList);
                rv_datalist.scrollToPosition(0);

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        return true;
    }


    public void fill_stockist(final List<m_distributor_customer> posts_s) {
        txt_total.setText("TOTAL CUSTOMERS :" + String.valueOf(posts_s.size()));
        customerlistAdapter = new DistributorCustomerlistAdapter(posts_s, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_datalist.setLayoutManager(mLayoutManager);
        rv_datalist.setItemAnimator(new DefaultItemAnimator());
        rv_datalist.setAdapter(customerlistAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter:
                show_dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //get_stockist_json(pref.getString(CHEMIST_STOCKISTLIST, "[]"));
    }

    private void show_dialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        //  final View dialogview = inflater.inflate(R.layout.dialog_product_catalog_stockist_filter, null);
        final View dialogview = inflater.inflate(R.layout.dialog_customer_stockist_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        radioGroup_customers = (RadioGroup) dialogview.findViewById(R.id.radioGroup_customers);
        rdgroup_filtercustomers = (RadioGroup) dialogview.findViewById(R.id.rdgroup_filtercustomers);
        rd_top_customer = (RadioButton) dialogview.findViewById(R.id.rd_top_customer);
        rd_alphabetic = (RadioButton) dialogview.findViewById(R.id.rd_alphabetic);
        rd_recent_activity = (RadioButton) dialogview.findViewById(R.id.rd_recent_activity);

        rd_newcustomer = (RadioButton) dialogview.findViewById(R.id.rd_newcustomer);
        rd_recent_order = (RadioButton) dialogview.findViewById(R.id.rd_recent_order);
        rd_pending_bill = (RadioButton) dialogview.findViewById(R.id.rd_pending_bill);


        TextView textview_clear = (TextView) dialogview.findViewById(R.id.textview_clear);
        TextView textview_filter = (TextView) dialogview.findViewById(R.id.textview_filter);


        rdgroup_filtercustomers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


            }
        });


        radioGroup_customers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {


            /*    if(checkedId == R.id.rd_top_customer)
                {
                    Log.d("Youprint11","Top cusomers");
                }else if(checkedId == R.id.rd_alphabetic)
                {
                    Log.d("Youprint12","Alphabetic");

                }else if(checkedId == R.id.rd_recent_activity){

                    Log.d("Youprint13","Recent Activity");
                }*/

            }


        });


        textview_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = radioGroup_customers.getCheckedRadioButtonId();
                int idd = rdgroup_filtercustomers.getCheckedRadioButtonId();


                if (selectedId == rd_top_customer.getId()) {
                    Log.d("you_select11", "Top customers");
                } else if (selectedId == rd_alphabetic.getId()) {

                    Collections.sort(posts, new CustomComparator());
                    fill_stockist(posts);
                    infoDialog.dismiss();


                    Log.d("you_select12", "Alphabetic");

                } else if (selectedId == rd_recent_activity.getId()) {

                    Log.d("you_select13", "Recent Activity");

                } else if (idd == rd_recent_order.getId()) {

                    Log.d("you_select14", "Recent Orders");
                    final List<m_distributor_customer> filteredModelList = new ArrayList<>();

                    for (m_distributor_customer model : posts) {

                        if (model.getOrderStatus().equals("Processing")) {

                            filteredModelList.add(model);

                        }
                        fill_stockist(filteredModelList);

                    }

                } else if (idd == rd_pending_bill.getId()) {

                    final List<m_distributor_customer> filteredModelList = new ArrayList<>();

                    for (m_distributor_customer model : posts) {

                        if (model.getPayment_Pending_status().equals("Yes")) {

                            filteredModelList.add(model);

                        }
                        fill_stockist(filteredModelList);

                    }
                    Log.d("you_select15", "Pending Bills");

                }

                infoDialog.dismiss();
            }
        });

        textview_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fill_stockist(posts);
                infoDialog.dismiss();

            }
        });


        set_attributes(infoDialog);
        infoDialog.show();
    }

    private void set_attributes(Dialog dlg) {

        Window window = dlg.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);

        int[] textSizeAttr = new int[]{android.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedValue typedValue = new TypedValue();
        TypedArray a = this.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionbarsize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

        int maxX = mdispSize.x;
        int maxY = mdispSize.y;

        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = maxX;   //x position
        wlp.y = actionbarsize - 20;   //y position
        // wlp.width = 50;
        // wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

    }

    private void get_stockist_json(String jsondata) {
        // LoadJsonFromAssets _LoadJsonFromAssets = new LoadJsonFromAssets("Chemist_Stokist.json", StockistList.this);
        //  String jsondata = //_LoadJsonFromAssets.getJson();
        if (!jsondata.isEmpty()) {
            Log.e("Stockist", jsondata.toString());
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            posts = new ArrayList<m_distributor_customer>();
            posts = Arrays.asList(mGson.fromJson(jsondata, m_distributor_customer[].class));


            for(int i = 0 ;i<posts.size();i++){

                if(posts.get(i).getPayment_Pending_status().equals("Yes")){

                    count++;

                    showpendinglist.setText("PENDING BILLS : " + String.valueOf(count));

                }

            }


            // Collections.sort(posts, comparator);
            // Collections.reverse(posts);

            fill_stockist(posts);

        }

    }

//    Comparator<m_distributor_customer> comparator = new Comparator<m_distributor_customer>() {
//        @Override
//        public int compare(m_distributor_customer o1, m_distributor_customer o2) {
//            boolean b1 = o1.getStatus();
//            boolean b2 = o2.getStatus();
//            if (b1 && !b2) {
//                return +1;
//            }
//            if (!b1 && b2) {
//                return -1;
//            }
//            return 0;
//        }
//    };

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {


        if (response != null)
        {
            Log.d("printResponced11", String.valueOf(response));
            if (f_name.equals(GET_CUSTOMERLIST_DISTRIBUTORLOGIN))
            {

                get_stockist_json(response.toString());

            }
        }

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }
//    private void fill_stockist(final List<m_stockist> posts_s) {
//
//        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>() {
//            @Override
//            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
//                LayoutInflater inflater = LayoutInflater.from(DistributorCustomerList.this);
//                ViewDataBinding binding = DataBindingUtil
//                        .inflate(inflater, R.layout.adpter_distributor_customer_list, parent, false);
//
//                return new BindingViewHolder(binding.getRoot());
//            }
//
//            @Override
//            public void onBindViewHolder(final DistributorCustomerList.BindingViewHolder holder, final int position) {
//                final m_stockist stockist_list = posts_s.get(position);
//                holder.getBinding().setVariable(BR.v_stockistlist, stockist_list);
//                holder.getBinding().executePendingBindings();
//
//                if (stockist_list.getStockist_Id() != null) {
//                    holder.getBinding().getRoot().setTag(stockist_list.getStockist_Id());
//                }
////
////                if (stockist_list.getStatus() != true) {
////                    holder.getBinding().getRoot().findViewById(R.id.status).setVisibility(View.VISIBLE);
////                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.arrow)).setImageResource(R.drawable.lock);
////
////                } else {
////                    holder.getBinding().getRoot().findViewById(R.id.status).setVisibility(View.GONE);
////                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.arrow)).setImageResource(R.drawable.right_arrow_grey);
////                }
////
////
//                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(), DistributorCustomerDetails.class);
//                        startActivity(intent);
//                    }
//                });
//            }
//            @Override
//            public int getItemCount() {
//                return posts_s.size();
//            }
//        };
//
//        rv_datalist.setLayoutManager(new LinearLayoutManager(this));
//        rv_datalist.setAdapter(adapter);
//    }

    private class BindingViewHolder extends RecyclerView.ViewHolder {

        public BindingViewHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }
    }


    public static class CustomComparator implements Comparator<m_distributor_customer> {
        @Override
        public int compare(m_distributor_customer o1, m_distributor_customer o2) {


            return o1.getClient_LegalName().compareTo(o2.getClient_LegalName());
        }

    }
}

