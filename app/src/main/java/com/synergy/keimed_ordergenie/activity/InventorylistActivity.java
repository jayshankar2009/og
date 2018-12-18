package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.Exad_inventoryItems;
import com.synergy.keimed_ordergenie.adapter.ad_AutocompleteCustomArray;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.database.StockistProducts;
import com.synergy.keimed_ordergenie.model.m_inventory;
import com.synergy.keimed_ordergenie.model.m_legends;

import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.SharedPref;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by prakash on 08/07/16.
 */
public class InventorylistActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    private SearchView searchView;
    Exad_inventoryItems adapter;
    private Menu oMenu;
    private SharedPreferences pref;
    private String Login_type;
    private String Client_id;
    private String Doc_Id;
    private Integer n_product_cart_count;
    private List<m_inventory> posts = new ArrayList<m_inventory>();
    private List<m_legends> color_legends = new ArrayList<m_legends>();
    ;
    private String call_plan_customer_id;

    Double orderAmount;

    private String legend_color_data;
    private SQLiteHandler db;

    AppController globalVariable;//= (AppController)getApplicationContext();

    @BindView(R.id.exp_inventory)
    ExpandableListView _exp_inventory;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @Nullable
    @BindView(R.id.empty_view)
    TextView empty_view;

    @BindView(R.id.txt_item_count)
    TextView txt_item_count;


    @BindView(R.id.cart_count)
    TextView cart_count;

    @BindView(R.id.cart_view)
    FrameLayout cart_with_img;

    private DaoSession daoSession;
    private ChemistCartDao chemistCartDao;
    TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        // txtyour.setTypeface(type);

        // textView.setTypeface(typeface);

        ButterKnife.bind(this);
        db = new SQLiteHandler(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        globalVariable = (AppController) getApplicationContext();
        Login_type = pref.getString(CLIENT_ROLE, "");
        Client_id = pref.getString(CLIENT_ID, "");
        title = (TextView) findViewById(R.id.myTitle);
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        title.setText("Item List");
        title.setTypeface(type);

        //title.setTextSize(18f);

        cart_with_img.setVisibility(View.GONE);
        //setTitle("Inventory");
        call_plan_customer_id = getIntent().getStringExtra("client_id");
        orderAmount = (double) getIntent().getFloatExtra("order_amount", 0);

        if (Login_type != null) {
            if (Login_type.equals(CHEMIST)) {

            } else {
                get_stockist_inventory(Client_id);
                //getOfflineProductList();
            }
        }

        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();

        get_cart_id();


        _exp_inventory.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

    }



    /* Get Offline Data */
    /*private void getOfflineProductList() {
        posts = new ArrayList<m_inventory>();
        Cursor cursor = db.getSalesmanProductList();
        int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            Toast.makeText(this, "Inventory list not available", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    String itemCode = cursor.getString(1);
                    String itemName = cursor.getString(4);
                    String stock = cursor.getString(8);
                    String productId = cursor.getString(3);
                    String packSize = cursor.getString(5);
                    String doseForm = cursor.getString(12);
                    String scheme = cursor.getString(13);

                    m_inventory m_inventory = new m_inventory(itemCode, itemName, stock, productId, packSize, doseForm, scheme);

                    posts.add(m_inventory);
                } while (cursor.moveToNext());
            }
            fill_exapndable(posts, SharedPref.getLegendData(this));
        }
    }*/


    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
    }



    /* OptionItemSelected */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inventory, menu);
        oMenu = menu;

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (globalVariable.getCall_plan_Started()) {

       //     Log.d("go_for_customer", "callplan");
            searchView.setIconifiedByDefault(false);
            searchView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);


            // date 13/9/2017

            cart_with_img.setVisibility(View.VISIBLE);
        }

        // create 13/9/2017
        if (globalVariable.getFromCustomerList()) {
            cart_with_img.setVisibility(View.VISIBLE);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {


                filter_on_text(newText);


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

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                show_filter_dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* Inventory List API */
    private void get_stockist_inventory(String Client_id) {
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_STOCKIST_INVENTORY,
                AppConfig.GET_STOCKIST_INVENTORY + Client_id, this, true);
        //Log.d("Client_id",Client_id);
    }


    /*public String loadJSONFromAsset()
    {
        String json = null;
        try {
            InputStream is = this.getAssets().open("inventory.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }*/

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            try {
                if (f_name.equals(AppConfig.GET_STOCKIST_INVENTORY)) {
                    //Log.d("inventoryResp", response.toString());
                    posts = new ArrayList<>();
                    String jsondata = response.toString();
                    if (!jsondata.isEmpty()) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        Type listType = new TypeToken<List<m_inventory>>() {}.getType();
                        posts = mGson.fromJson(jsondata, listType);
                    }
                    get_stockist_legends();
                }
                if (f_name.equals(AppConfig.POST_GET_STOCKIST_LEGENDS)) {
                    legend_color_data = response.toString();
                    //fill_exapndable(posts, legend_color_data);
                }

                fill_exapndable(posts, legend_color_data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    void filter_on_text(String newText) {
        newText = newText.toLowerCase();
        List<m_inventory> filteredModelList = new ArrayList<>();
        for (m_inventory model : posts) {
            try {
                final String text = model.getItemname().toLowerCase();

            // fill_exapndable(posts, legend_color_data);
                //    Log.d("print_names_inventory", text);
                if (text.contains(newText)) {
                    filteredModelList.add(model);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        fill_exapndable(filteredModelList, legend_color_data);
    }


    private void show_filter_dialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_inventory_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);

        final TextView edt_search_word = (TextView) dialogview.findViewById(R.id.edt_search_word);
        dialogview.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edt_search_word.getText().toString().isEmpty()) {
                    try {
                        filter_on_text(edt_search_word.getText().toString());
                        infoDialog.dismiss();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                    OGtoast.OGtoast("Please enter a name to filter", InventorylistActivity.this);

                }

            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fill_exapndable(posts, legend_color_data);
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
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = maxX;   //x position
        wlp.y = actionbarsize - 20;   //y position
        window.setAttributes(wlp);
    }



    void fill_exapndable(List<m_inventory> filteredModelList, String legend_data) {
        if (filteredModelList != null) {
            txt_item_count.setText(filteredModelList.size() + " Items.");
            empty_view.setVisibility(View.GONE);
            if (filteredModelList.size() > 0) {
                empty_view.setVisibility(View.GONE);
                adapter = new Exad_inventoryItems(filteredModelList, InventorylistActivity.this, call_plan_customer_id, orderAmount, legend_data);
                _exp_inventory.setAdapter(adapter);
            } else {
                empty_view.setVisibility(View.VISIBLE);
                adapter = new Exad_inventoryItems(filteredModelList, InventorylistActivity.this, call_plan_customer_id, orderAmount, legend_data);
                _exp_inventory.setAdapter(adapter);
            }
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
    }



    public Intent getSupportParentActivityIntent() {
        Intent newIntent = null;
        try {
            if (globalVariable.getCall_plan_Started()) {
                finish();
            } else {
                finish();
                //newIntent = new Intent(InventorylistActivity.this, MainActivity.class);
            }
            //you need to define the class with package name

            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIntent;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // finish();
        //Intent newIntent = null;
        try {
            if (globalVariable.getCall_plan_Started()) {
                finish();
            } else {
                finish();
                //newIntent = new Intent(InventorylistActivity.this, MainActivity.class);
            }
            //you need to define the class with package name

            /*startActivity(newIntent);
            finish();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void get_cart_id()
    {

      /*  if (call_plan_customer_id != null) {
            Cursor crs_cart = db.get_chemist_cart(call_plan_customer_id);

            if (crs_cart != null && crs_cart.getCount() > 0) {
                while (crs_cart.moveToNext()) {
                    Doc_Id = crs_cart.getString(crs_cart.getColumnIndex("DOC_NO"));
                    n_product_cart_count = (db.get_total_order_item_count(Doc_Id));
                    createCartBadge(n_product_cart_count);
                }
            }


        }*/
        if (call_plan_customer_id != null) {
            n_product_cart_count = chemistCartDao.getCartItem(call_plan_customer_id, true).size();
            createCartBadge(n_product_cart_count);
        }


    }


    public void createCartBadge(int paramInt) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fab_scale_up);
        a.reset();
        cart_count.clearAnimation();


        if (paramInt == 0) {
            cart_count.setVisibility(View.GONE);
        } else
            cart_count.setVisibility(View.VISIBLE);

        cart_count.startAnimation(a);
        cart_count.setText("" + paramInt);

    }

    void get_stockist_legends() {
        try {

            JSONArray j_arr = new JSONArray();
            j_arr.put(Client_id);

            MakeWebRequest.MakeWebRequest("out_array", AppConfig.POST_GET_STOCKIST_LEGENDS, AppConfig.POST_GET_STOCKIST_LEGENDS,
                    j_arr, this, true, "");

        } catch (Exception e) {

        }
    }

}
