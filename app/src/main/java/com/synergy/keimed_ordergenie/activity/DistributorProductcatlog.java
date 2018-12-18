package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.Expand_productcatlogs;
import com.synergy.keimed_ordergenie.adapter.ExpandlistAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.Productcatolg_modal;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class DistributorProductcatlog extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    String Stockist_id;
    SharedPreferences pref;
    TextView textView_clear, textView_filter;
    Spinner manufacture_name, product_category, product_form;
    RadioGroup radioGroup;
    RadioButton no_stock, low_stock, moderate_stock, available_stock, plenty;
    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    Expand_productcatlogs adapter;
    private List<Productcatolg_modal> d_productcatogs = new ArrayList<>();
    private String Client_id;
    ExpandableListView expandableListView;
    private SearchView searchView;
    private Menu oMenu;
    TextView textview_totalproducts;

/*    @BindView(R.id.textview_totalproducts)
    TextView textview_totalproducts;*/

    ExpandlistAdapter expandlistAdapter;
    List<String> listProductHeader;
    HashMap<String, List<String>> listProductChilds;
    String distributor_legendData;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_productcatlog);
        setTitle("Product Catalog");


        textview_totalproducts = (TextView) findViewById(R.id.totalproducts);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Stockist_id = pref.getString(CLIENT_ID, "0");
        list = new ArrayList<String>();
        list.add("Manufature Name");

      /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup radioGroup, int i) {
              if(i == R.id.no_stock){
                  Toast.makeText(getApplicationContext(), "choice: No stock",
                          Toast.LENGTH_SHORT).show();
              }else if(i == R.id.low_stock){
                  Toast.makeText(getApplicationContext(), "choice: Lo stock",
                          Toast.LENGTH_SHORT).show();
              }
              else if(i == R.id.moderate_stock){
                  Toast.makeText(getApplicationContext(), "choice: moderate_stock",
                          Toast.LENGTH_SHORT).show();
              }
              else if(i == R.id.available_stock){
                  Toast.makeText(getApplicationContext(), "choice: available_stock",
                          Toast.LENGTH_SHORT).show();
              }
              else if(i == R.id.plenty){
                  Toast.makeText(getApplicationContext(), "choice: plenty",
                          Toast.LENGTH_SHORT).show();
              }
          }
      });*/
        //  setContentView(R.layout.activity_distributor_pendingbill_details);

       /* setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Product Dialog");*/

        // LinearLayout textView = (LinearLayout) findViewById(R.id.heading_layout);

        // prepareDataList();
        expandableListView = (ExpandableListView) findViewById(R.id.expand_productcatlog);


        //  Log.d("Listprint", String.valueOf(listProductHeader));
        // Log.d("Listprint11", String.valueOf(listProductChilds));


//        expandlistAdapter = new ExpandlistAdapter(this, listProductHeader, listProductChilds);

        // setting list adapter
        //      expandableListView.setAdapter(expandlistAdapter);


        //  Client_id = "1240";


        //  get_distributor_legends();


        get_distributor_catlog();

        // passing_catlogData();
    }

    private void get_distributor_legends() {

        try {

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(Stockist_id);


            MakeWebRequest.MakeWebRequest("out_array", AppConfig.POST_GET_STOCKIST_LEGENDS, AppConfig.POST_GET_STOCKIST_LEGENDS,
                    jsonArray, this, true, "");

        } catch (Exception e) {

        }
    }

    private void get_distributor_catlog() {


        //Client_id
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INVENTORY,
                AppConfig.GET_STOCKIST_INVENTORY + Stockist_id, this, true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customer_list, menu);
        oMenu = menu;
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Product Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<Productcatolg_modal> productnamesList = new ArrayList<>();
                for (Productcatolg_modal model : d_productcatogs) {

                    try {

                        final String text = model.getItemname().toLowerCase();

                        if (text.contains(newText)) {
                            productnamesList.add(model);
                        }
                    } catch (Exception e) {

                    }
                }
                fill_exapndable(productnamesList, distributor_legendData);
                return false;
                /*try {

                    newText = newText.toLowerCase();

                    List<Productcatolg_modal> productnameList = new ArrayList<>();

                    for (Productcatolg_modal modal : d_productcatogs) {
                        final String text = modal.getItemname().toLowerCase();

                        Log.d("printtext",text);
                        if (text.contains(newText)) {
                            productnameList.add(modal);
                        }
                    }

                    fill_exapndable(productnameList);

                }catch (Exception e)
                {
                    Log.d("msgforerr",e.getMessage());
                }


                return false;*/
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //_toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // _toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.GONE);
            }
        });


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
    }

    private void prepareDataList() {

        listProductHeader = new ArrayList<String>();
        listProductChilds = new HashMap<String, List<String>>();


        listProductHeader.add("ACAMPTAS333");
        listProductHeader.add("Crocin");
        listProductHeader.add("Combiflam");


        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Viroopa");
       /* top250.add("Rxmedikart");
        top250.add("Smartpractix");
        top250.add("Ordergenie");
        top250.add("Tcs");*/

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Rxmedikart");
        /*nowShowing.add("medico");
        nowShowing.add("1000");
        nowShowing.add("4444");
        nowShowing.add("viroopa");*/


        List<String> listd = new ArrayList<String>();
        listd.add("Smartpractix");

        listProductChilds.put(listProductHeader.get(0), top250); // Header, Child data
        listProductChilds.put(listProductHeader.get(1), nowShowing);
        listProductChilds.put(listProductHeader.get(2), listd);

    }

    private void passing_catlogData() {

    /*    expand_productcatlogItems = new ExpandlistAdapter(this, listProductHeader, listProductChilds);
        expand_productcatlog.setAdapter(expand_productcatlogItems);
    */

    }


    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {


        if (response != null) {
            Log.d("prtresponce", String.valueOf(response));
            try {
                if (f_name.equals(AppConfig.POST_GET_STOCKIST_LEGENDS)) {
                    distributor_legendData = response.toString();
                    Log.e("Distributor11", distributor_legendData);
                    fill_exapndable(d_productcatogs, distributor_legendData);
                    //  get_distributor_catlog();
                }

                if (f_name.equals(AppConfig.GET_STOCKIST_INVENTORY)) {
                    d_productcatogs = new ArrayList<Productcatolg_modal>();

                    String jsondata = response.toString();
                    if (!jsondata.isEmpty()) {
                        Log.d("Product", response.toString());
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
//                        Type listType = new TypeToken<List<Productcatolg_modal>>() {
//                        }.getType();
                        // d_productcatogs = mGson.fromJson(jsondata, listType);
//sorting json response alphabetically
                        ArrayList<JSONObject> array = new ArrayList<JSONObject>();
                        JSONArray jsonArray = new JSONArray(jsondata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                array.add(jsonArray.getJSONObject(i));
                                list.add(array.get(i).getString("Itemname"));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        Collections.sort(array, new Comparator<JSONObject>() {

                            @Override
                            public int compare(JSONObject lhs, JSONObject rhs) {
                                // TODO Auto-generated method stub

                                try {
                                    return (lhs.getString("Itemname").toLowerCase().compareTo(rhs.getString("Itemname").toLowerCase()));
                                } catch (JSONException e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });

                        d_productcatogs = Arrays.asList(mGson.fromJson(jsondata, Productcatolg_modal[].class));

                        //fill_exapndable(d_productcatogs, distributor_legendData);
                        //Log.d("print_array_json", String.valueOf(posts));
                    }
                    get_distributor_legends();

                } else {
                    if (isNetworkConnectionAvailable()) {

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (Exception e) {
                Log.d("Excp", e.getMessage());

            }

        }
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            //    Log.d("Network", "Connected");
            return true;
        } else {
            // checkNetworkConnection();
            //     Log.d("Network","Not Connected");
            return false;
        }
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    private void fill_exapndable(List<Productcatolg_modal> d_productcatogs, String distributor_legendData) {


        //Log.d("listproduct", String.valueOf(d_productcatogs.size()));

        textview_totalproducts.setText(String.valueOf(d_productcatogs.size()));

        adapter = new Expand_productcatlogs(d_productcatogs, DistributorProductcatlog.this, distributor_legendData);
        expandableListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void show_dialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        //  final View dialogview = inflater.inflate(R.layout.dialog_product_catalog_stockist_filter, null);
        final View dialogview = inflater.inflate(R.layout.dialog_product_catalog_stockist_filter, null);


        radioGroup = (RadioGroup) dialogview.findViewById(R.id.radiostockGroup);
        no_stock = (RadioButton) dialogview.findViewById(R.id.no_stock);
        low_stock = (RadioButton) dialogview.findViewById(R.id.low_stock);
        moderate_stock = (RadioButton) dialogview.findViewById(R.id.moderate_stock);
        available_stock = (RadioButton) dialogview.findViewById(R.id.available_stock);
        plenty = (RadioButton) dialogview.findViewById(R.id.plenty);

        textView_clear = (TextView) dialogview.findViewById(R.id.btn_clear);
        textView_filter = (TextView) dialogview.findViewById(R.id.btn_filter);

        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        set_attributes(infoDialog);
        infoDialog.show();
        Spinner sp_manufacture, sp_product_cat, sp_product_from;
        sp_manufacture = (Spinner) dialogview.findViewById(R.id.sp_manufacture);
        sp_product_cat = (Spinner) dialogview.findViewById(R.id.sp_product_category);
        sp_product_from = (Spinner) dialogview.findViewById(R.id.sp_product_form);

        try {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_manufacture.setAdapter(dataAdapter);
            sp_product_cat.setAdapter(dataAdapter);
            sp_product_from.setAdapter(dataAdapter);
        } catch (Exception e) {

        }

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fill_exapndable(d_productcatogs, distributor_legendData);
                infoDialog.dismiss();

            }
        });


        dialogview.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//no_stock,low_stock,moderate_stock,available_stock,plenty;
                filter_distributor_Stocks(no_stock.isChecked(), low_stock.isChecked(), moderate_stock.isChecked(), available_stock.isChecked(), plenty.isChecked());
                infoDialog.dismiss();

            }

            private void filter_distributor_Stocks(boolean no_Stockchecked, boolean low_stockChecked, boolean moderate_stockChecked, boolean available_stockChecked, boolean plentyChecked) {
                // Log.d("Filter_stock","Filter_stock");
                List<Productcatolg_modal> modal_product = new ArrayList<Productcatolg_modal>();


                for (Productcatolg_modal modal : d_productcatogs) {


                    if (no_Stockchecked) {

                        try {


                            if (Integer.parseInt(modal.getStock()) == 0 || modal.getStock() == null) {

                                modal_product.add(modal);
                            }
                        } catch (Exception e) {


                            Log.d("Filter_stock10", e.getMessage());
                        }

                        // Log.d("Filter_stock11","no_Stockchecked");


                    } else if (low_stockChecked) {

                        try {


                            if (Integer.parseInt(modal.getStock()) > 0 && Integer.parseInt(modal.getStock()) <= 100) {

                                modal_product.add(modal);
                            }
                        } catch (Exception e) {


                            Log.d("Filter_stock11", e.getMessage());
                        }

                    } else if (moderate_stockChecked) {


                        try {

                            if (Integer.parseInt(modal.getStock()) >= 101 && Integer.parseInt(modal.getStock()) <= 200) {

                                modal_product.add(modal);

                            }

                        } catch (Exception ex) {

                            Log.d("Filter_stock11", ex.getMessage());
                        }
                    } else if (available_stockChecked) {

                        try {

                            if (Integer.parseInt(modal.getStock()) >= 201 && Integer.parseInt(modal.getStock()) <= 300) {

                                modal_product.add(modal);

                            }

                        } catch (Exception ex) {

                            Log.d("Filter_stock11", ex.getMessage());
                        }


                    } else if (plentyChecked) {


                        try {

                            if (Integer.parseInt(modal.getStock()) >= 301 && Integer.parseInt(modal.getStock()) <= 10000) {

                                modal_product.add(modal);

                            }

                        } catch (Exception ex) {

                            Log.d("Filter_stock11", ex.getMessage());
                        }

                    }


                }

                fill_exapndable(modal_product, distributor_legendData);


            }
        });
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


    public static class CustomComparator implements Comparator<Productcatolg_modal> {
        @Override
        public int compare(Productcatolg_modal o1, Productcatolg_modal o2) {

            return o2.getItemname().compareTo(o1.getItemname());

            //  return o1.getOrderDate().compareTo(o2.getOrderDate());
        }
    }


}

