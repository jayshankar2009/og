package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Point;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.synergy.keimed_ordergenie.Constants.Constant;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.model.m_stockist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.synergy.keimed_ordergenie.utils.ConstData.data_refreshing.CHEMIST_STOCKISTLIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class All_ProductsCatlog extends AppCompatActivity {

    String perstock,color_code,legendName;
    private String clientId;
    private String accessKey;
    @BindView(R.id.rv_datalist)
    RecyclerView rv_datalist;
    List<m_stockist> posts;
    SharedPreferences pref;
    private SQLiteHandler db;
    private LinearLayout linearLayoutCart;
    TextView textViewCartCount,textview_take_orderList,textview_inventoryList;
    private DaoSession daoSession;
    private ChemistCartDao chemistCartDao;
    public static final String CHEMIST_STOCKIST_ID = "chemist_stockist_id";
    public static final String CHEMIST_STOCKIST_NAME = "chemist_stockist_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__products_catlog);

        db = new SQLiteHandler(this);
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        accessKey = pref.getString("key", "");
        get_stockist_json(pref.getString(CHEMIST_STOCKISTLIST, "[]"));
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stockist_product_catlog, menu);

        return true;
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
    }*/


    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
        get_stockist_json(pref.getString(CHEMIST_STOCKISTLIST, "[]"));
    }



    private void get_stockist_json(String jsondata) {

        if (!jsondata.isEmpty()) {
            //  Log.e("Stockist",jsondata.toString());
            GsonBuilder builder = new GsonBuilder();

            Gson mGson = builder.create();

            posts = new ArrayList<m_stockist>();
            posts = Arrays.asList(mGson.fromJson(jsondata, m_stockist[].class));

            Collections.sort(posts, comparator);
            Collections.reverse(posts);

            fill_stockist(posts);

        }

    }

    Comparator<m_stockist> comparator = new Comparator<m_stockist>() {
        @Override
        public int compare(m_stockist o1, m_stockist o2) {
            boolean b1 = o1.getStatus();
            boolean b2 = o2.getStatus();
            if (b1 && !b2) {
                return +1;
            }
            if (!b1 && b2) {
                return -1;
            }
            return 0;
        }
    };




    /* Stockist List Adapter */
    private void fill_stockist(final List<m_stockist> posts_s) {
        final RecyclerView.Adapter<StockistList.BindingViewHolder> adapter = new RecyclerView.Adapter<StockistList.BindingViewHolder>() {
            @Override
            public StockistList.BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                LayoutInflater inflater = LayoutInflater.from(All_ProductsCatlog.this);
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.adpter_stockist_product, parent, false);

                return new StockistList.BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(final StockistList.BindingViewHolder holder, final int position) {
                final m_stockist stockist_list = posts_s.get(position);
                holder.getBinding().setVariable(BR.v_stockistlist, stockist_list);
                holder.getBinding().executePendingBindings();

                if (stockist_list.getStockist_Id() != null) {
                    holder.getBinding().getRoot().setTag(stockist_list.getStockist_Id());
                }

                if (stockist_list.getStatus() != true) {
                    holder.getBinding().getRoot().findViewById(R.id.status).setVisibility(View.VISIBLE);
                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.arrow)).setImageResource(R.drawable.lock);

                } else {
                    holder.getBinding().getRoot().findViewById(R.id.status).setVisibility(View.GONE);
                    ((ImageView) holder.getBinding().getRoot().findViewById(R.id.arrow)).setImageResource(R.drawable.right_arrow_grey);
                }



                textViewCartCount = (TextView) holder.getBinding().getRoot().findViewById(R.id.txt_cart_count_call_plan);

                daoSession = ((AppController) getApplication()).getDaoSession();
                chemistCartDao = daoSession.getChemistCartDao();

                List<ChemistCart> chemistCartList = chemistCartDao.getCartItem(posts_s.get(position).getStockist_Id().toString(), false);
                int cartSize = chemistCartList.size();
                if (cartSize != 0) {
                    // linearLayoutCart.setVisibility(View.GONE);
                    textViewCartCount.setText(String.valueOf(cartSize));

                } else {
                    textViewCartCount.setVisibility(View.GONE);

                }




                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stockist_list.getStatus() != true) {
                            request_stock_access(posts_s.get(position).getStockist_Id().toString(), posts_s.get(position).getStokist_Name());
                        } else {
                            Create_new_order(posts_s.get(position).getStockist_Id().toString(), posts_s.get(position).getStokist_Name().toString());
                        }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return posts_s.size();
            }
        };

        rv_datalist.setLayoutManager(new LinearLayoutManager(this));
        rv_datalist.setAdapter(adapter);
    }


    private class BindingViewHolder extends RecyclerView.ViewHolder {

        public BindingViewHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }
    }

    private void request_stock_access(String stockist_id, String stockist_name) {
        Intent intent = new Intent(this, RequestStockistAccess.class);
        intent.putExtra(CHEMIST_STOCKIST_ID, stockist_id);
        intent.putExtra(CHEMIST_STOCKIST_NAME, stockist_name);
        startActivity(intent);
    }

    private void Create_new_order(String stockist_id, String stockist_name) {
       // Intent intent = new Intent(this, Order_Search.class);
        Intent intent = new Intent(this, Products_Catlog_Search.class);
        //Intent intent = new Intent(this, Create_Order.class);
        intent.putExtra("Stokist_ID", stockist_id);
        //intent.putExtra(CHEMIST_STOCKIST_NAME, stockist_name);

        Log.d("show_inventory10",stockist_id);
        startActivity(intent);
        //getLegendChemistapi(stockist_id);
    }
    private void getLegendChemistapi(final String stockist_id) {

        Log.d("chesmodeId11",stockist_id);
        JSONArray j_arr = new JSONArray();
        j_arr.put(stockist_id);
        j_arr.put(2);

        Log.d("jarrayprint",j_arr.toString());
        String url = AppConfig.POST_GET_STOCKIST_LEGENDS;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, j_arr, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("chesmodeId12",response.toString());
                if (response != null) {
                    JSONArray legenddata = response;
                    Log.d("chesmodeId13", response.toString());

                    get_FullproductsAPI(legenddata,stockist_id);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("legendResp", error.getMessage());
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
        Volley.newRequestQueue(All_ProductsCatlog.this).add(jsonArrayRequest);

    }

    private void get_FullproductsAPI(JSONArray legenddata, String stockist_id) {

        Log.d("chesmodeId14", legenddata.toString());

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        clientId = pref.getString(CLIENT_ID, "0");
        Log.d("chesmodeId15", clientId);

        //String url = AppConfig.GET_CHEMIST_PRODUCT_DATA + "[" + clientId + "," + userId + "]";

        String url = AppConfig.GET_CHEMIST_PRODUCT_DATA + "[" + clientId + "," + stockist_id + "]";
        Log.d("chesmodeId16",url);
        final JSONArray j_arr1= legenddata;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //  Log.d("chesmodeId16", response.toString());
                if (response != null) {
                    Log.d("chesmodeId17", response.toString());

                    Constant.posts = new ArrayList<>();
                    //sqLiteHandler.deleteRecordFromSalesmanProduct();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject j_obj = response.getJSONObject(i);
                            perstock = j_obj.getString("stock");
                            //*****condition to assign color code and legend name as per stock******//

                            for (int j = 0; j < j_arr1.length(); j++) {
                                JSONObject j_ob1 = j_arr1.getJSONObject(j);

                                if (j_ob1.has("StartRange")){

                                    if(perstock.equals("")||perstock.isEmpty()||perstock.equals(null)){

                                        color_code = "#ffffff";
                                        // legend_mode = j_ob1.getString("Legend_Mode");
                                        legendName = "NA";
                                    }   else if(perstock.contains(".")){
                                        if (Double.parseDouble(perstock) >= j_ob1.getInt("StartRange") &&
                                                Double.parseDouble(perstock) <= j_ob1.getInt("EndRange")) {
                                            color_code = j_ob1.getString("ColorCode");
                                            //  legend_mode = j_ob1.getString("Legend_Mode");
                                            legendName = j_ob1.getString("LegendName");
                                        }
                                    } else {
                                        if (Integer.parseInt(perstock) >= j_ob1.getInt("StartRange") &&
                                                Integer.parseInt(perstock) <= j_ob1.getInt("EndRange")) {
                                            color_code = j_ob1.getString("ColorCode");
                                            //legend_mode = j_ob1.getString("Legend_Mode");
                                            legendName = j_ob1.getString("LegendName");
                                        }
                                    }
                                }
                            }
                            //*****condition******//

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideSyncDialog();

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
        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequest);
    }

    private void show_dialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_chemist_stockist_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        final TextView med_name = (TextView) dialogview.findViewById(R.id.med_name);
        final TextView location_txt = (TextView) dialogview.findViewById(R.id.location_txt);
        dialogview.findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!med_name.getText().toString().isEmpty()) {
                    try {


                        filter_data_on_stockist(med_name.getText().toString());
                        infoDialog.dismiss();
                    }
                    catch (Exception e)
                    {

                    }
                } else if (!location_txt.getText().toString().isEmpty()) {
                    filter_on_location(location_txt.getText().toString());
                }

            }
        });

        dialogview.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
                fill_stockist(posts);
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

        window.setAttributes(wlp);

    }

    void filter_data_on_stockist(String medicine_name) {
        Cursor crs = db.get_stockist_id_on_medicine_name(medicine_name);

        final List<m_stockist> filteredModelList = new ArrayList<>();


        if (crs.moveToFirst()) {
            do {


                for (m_stockist model : posts) {
                    if (model.getStockist_Id() == crs.getInt(crs.getColumnIndex("Stockist_id")))
                        filteredModelList.add(model);

                }

            } while (crs.moveToNext());
        }


        fill_stockist(filteredModelList);

    }

    void filter_on_location(String location) {

        try {
            final List<m_stockist> filteredModelList = new ArrayList<m_stockist>();
            for (m_stockist model : posts) {
                if (model.getLocation().toLowerCase().equals(location.toLowerCase())) {
                    filteredModelList.add(model);
                }

            }
            fill_stockist(filteredModelList);
        } catch (Exception e) {
            OGtoast.OGtoast("Location not updated", this);
        }
    }

}
