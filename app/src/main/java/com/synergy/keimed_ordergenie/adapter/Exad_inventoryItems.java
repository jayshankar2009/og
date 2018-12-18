package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.InventorylistOfflineActivity;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.model.m_inventory;
import com.synergy.keimed_ordergenie.model.m_inventory_items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by anandbose on 09/06/15.
 */
public class Exad_inventoryItems extends BaseExpandableListAdapter {

    private List<m_inventory> catList;
    private int itemLayoutId;
    private int groupLayoutId;
    private Context ctx;
    private Boolean Cart_Id_available = false;
    private SQLiteHandler db;
    Float vMrp, vRate;
    private String Itemcode, Itemname,ProductId, UOM;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private String Doc_Id;
    private Integer n_product_cart_count;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    AppController globalVariable;
    private String call_plan_chemist_id;
    Double orderAmount;
    private int nQty = 1;
    private String legend_data;
    String color_code,legend_name,legend_mode;

    public Exad_inventoryItems(List<m_inventory> catList, Context ctx, String call_plan_chemist_id, Double orderAmount, String legend_data) {
        this.itemLayoutId = R.layout.exad_inventory_line_items;
        this.groupLayoutId = R.layout.fragement_inventory_items;
        this.legend_data = legend_data;
        this.orderAmount = orderAmount;

        if (call_plan_chemist_id != null) {
            this.call_plan_chemist_id = call_plan_chemist_id;
        }

        this.catList = catList;
        this.ctx = ctx;
        db = new SQLiteHandler(ctx);
        globalVariable = (AppController) ctx.getApplicationContext();
        initImageLoader();
        //get_cart_id();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getline_items().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getline_items().get(childPosition).hashCode();
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // View v = convertView;
        View v = null;
        ViewDataBinding binding = null;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding = DataBindingUtil.inflate(inflater, R.layout.exad_inventory_line_items, parent, false);
        }
        final m_inventory_items det = catList.get(groupPosition).getline_items().get(childPosition);


        TextView invtory_packsize = (TextView) binding.getRoot().findViewById(R.id.invtory_packsize);
        TextView invtory_productcode = (TextView) binding.getRoot().findViewById(R.id.invtory_itemcode);
        ImageView img_product = (ImageView) binding.getRoot().findViewById(R.id.img_product);


//        Log.d("print_packsize", catList.get(groupPosition).getPacksize());

        invtory_packsize.setText(catList.get(groupPosition).getPacksize());
        invtory_productcode.setText(catList.get(groupPosition).getItemcode());

        imageLoader.displayImage(det.getImage_path(), img_product);

        final TextView Qty = (TextView) binding.getRoot().findViewById(R.id.Qty);

        if (binding != null) {
            binding.setVariable(BR.v_inventory_items, det);
        }

        if (globalVariable.getCall_plan_Started()) {
            binding.getRoot().findViewById(R.id.add_product_lnr).setVisibility(View.VISIBLE);
        } else if (globalVariable.getFromCustomerList()) {
            /*binding.getRoot().findViewById(R.id.add_product_lnr).setVisibility(View.GONE);*/
            binding.getRoot().findViewById(R.id.add_product_lnr).setVisibility(View.VISIBLE);
        } else {
            binding.getRoot().findViewById(R.id.add_product_lnr).setVisibility(View.GONE);
        }

        binding.getRoot().findViewById(R.id.btn_add_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vRate = det.getRate();
                vMrp = det.getMrp();
                Itemcode = catList.get(groupPosition).getItemcode();
                //   Log.e("Productid", catList.get(groupPosition).getProduct_ID());
                ProductId = catList.get(groupPosition).getProduct_ID();//det.get();

                Itemname = catList.get(groupPosition).getItemname();
                UOM = "Tablet";
                //   add_product(view, catList.get(groupPosition).getItemname());
                if(vMrp == null || vRate == null|| Itemname==null || Itemcode == null || ProductId == null || Itemname.equals("") || Itemcode.equals("") || ProductId.equals("") || vRate.equals("") || vMrp.equals(""))
                {
                    Toast.makeText(ctx, "Null Values cannot be added into the cart", Toast.LENGTH_LONG).show();
                }else{
//                    addItemCart(catList.get(groupPosition));

                    addItemCart(catList.get(groupPosition), catList.get(groupPosition).getline_items().get(childPosition));
                }
                //addItemCart(catList.get(groupPosition));
            }
        });

        binding.getRoot().findViewById(R.id.btnminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                // int num1;
                String value = Qty.getText().toString();
                int num1 = Integer.parseInt(value);
                if (num1 > 1) {
                    nQty = num1 - 1;
                }

                Qty.setText(String.valueOf(nQty));

            }
        });

        //btnplus Button click Events

        binding.getRoot().findViewById(R.id.btnplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = Qty.getText().toString();
                int num1 = Integer.parseInt(value);
                nQty = num1 + 1;
                Qty.setText(String.valueOf(nQty));

            }
        });
        return binding.getRoot();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = catList.get(groupPosition).getline_items().size();

        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return catList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return catList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return catList.get(groupPosition).hashCode();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //View v = convertView;
        View v = null;
        ViewDataBinding binding = null;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding = DataBindingUtil.inflate(inflater, R.layout.fragement_inventory_items, parent, false);
        }
        if (isExpanded) {
            ((ImageView) binding.getRoot().findViewById(R.id.groupindicator)).setImageResource(R.drawable.up_arrow);
        } else {
            ((ImageView) binding.getRoot().findViewById(R.id.groupindicator)).setImageResource(R.drawable.down_arrow);
        }
        TextView tx_stock = (TextView) binding.getRoot().findViewById(R.id.tx_stock);

        TextView tx_itemname = (TextView) binding.getRoot().findViewById(R.id.tx_itemname);

        m_inventory cat = catList.get(groupPosition);

        try {

            if (cat.getItemname()!= null)
            {
                tx_itemname.setText(cat.getItemname());

            }else
            {
                tx_itemname.setText("NA");

            }
        }catch (Exception e)
        {

        }

        tx_stock.setBackgroundColor(Color.parseColor(cat.getLegendColor()));
        if (cat.getLegendName().equalsIgnoreCase("NA"))
        {
            tx_stock.setText("");
        }
        else
        {
            tx_stock.setText(cat.getLegendName());
        }


//        legend_mode = SharedPref.getLegendMode(ctx);
//        if (legend_mode.equals("3"))
//
//        {
//            //    Log.d("legend_mode3","print three ");
//            tx_stock.setBackgroundColor(Color.parseColor(cat.getLegendColor()));
//            tx_stock.setText(cat.getLegendName());
//
//        }
//        else if(legend_mode.equals("2"))
//        {
//
//           tx_stock.setBackgroundColor(Color.parseColor(cat.getLegendColor()));
//            tx_stock.setText(cat.getStock());
//
//        }
//
//        else if (legend_mode.equals("1"))
//        {
//
//            tx_stock.setBackgroundColor(Color.parseColor(cat.getLegendColor()));
//        }

        if (binding != null) {
            binding.setVariable(BR.v_inventory, cat);
        }
        return binding.getRoot();

    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                ctx).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }

    void add_product(View v, String itemname) {
        Float price = vRate * nQty;

       /* if(!db.check_saleman_product_availabity(ProductId)) {
            db.inset_into_salesman_products(ProductId, Itemcode, itemname, "", String.valueOf(vMrp), "", "", "", "", "", "", "");
        }*/

        if (Cart_Id_available) {


            db.insert_into_chemist_cart_details(Doc_Id, Itemcode, ProductId, nQty, UOM, vRate.toString(), price.toString(),
                    vMrp.toString(),
                    dateFormat.format(Calendar.getInstance().getTime()));


            price = price + db.get_total_order_amount(Doc_Id);
            Integer item_count = (db.get_total_order_item_count(Doc_Id)) + 1;
            //_OrderAmt.setText(" Rs." +price.toString());
            db.update_into_chemist_cart(Doc_Id, item_count, price.toString());

            ((InventorylistOfflineActivity) ctx).createCartBadge(item_count);
            //((InventorylistActivity) ctx).createCartBadge(item_count);

        } else {

            db.insert_into_chemist_cart(Doc_Id, call_plan_chemist_id, price.toString(),
                    dateFormat.format(Calendar.getInstance().getTime()),
                    1, "cart", 0, 0);

            db.insert_into_chemist_cart_details(Doc_Id, Itemcode, ProductId, nQty, UOM, vRate.toString(), price.toString(),
                    vMrp.toString(),
                    dateFormat.format(Calendar.getInstance().getTime()));
            // _OrderAmt.setText(" Rs." +price.toString());


            ((InventorylistOfflineActivity) ctx).createCartBadge(1);
            //((InventorylistActivity) ctx).createCartBadge(1);

            Cart_Id_available = true;

        }

        Snackbar snackbar = Snackbar
                .make(v, "Product added to cart succesfully", Snackbar.LENGTH_LONG);

        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        snackbar.show();


    }


    private void addItemCart(m_inventory m_inventory, m_inventory_items m_inventory_item) {

        Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;

        Float price = vRate * nQty;
        //  Double price = vRate * nQty;


        ChemistCart chemistCart = new ChemistCart();

        chemistCart.setDOC_ID(Doc_Id);
        chemistCart.setPACK(m_inventory.getPacksize());
        chemistCart.setItems(m_inventory.getItemcode());
        chemistCart.setItemname(m_inventory.getItemname());
        chemistCart.setProduct_ID(m_inventory.getProduct_ID());
        chemistCart.setQty(String.valueOf(nQty));
        chemistCart.setUOM(UOM);
        chemistCart.setRate(vRate.toString());
        chemistCart.setPrice(price.toString());
        chemistCart.setMRP(String.valueOf(vMrp));
        chemistCart.setCreatedon(dateFormat.format(Calendar.getInstance().getTime()));
        //chemistCart.setSalesman(true);
        chemistCart.setStockist_Client_id(call_plan_chemist_id);
        chemistCart.setRemarks("cart");
        chemistCart.setOrder_sync(false);
        chemistCart.setSalesman(true);
        chemistCart.setScheme(m_inventory.getScheme());
        chemistCart.setHalfScheme(m_inventory_item.getHalfScheme());
        //chemistCart.setPercentScheme(m_inventory.getline_items().get(position).getPercentScheme());
        // chemistCart.setDoc_Date(dateFormat.format(Calendar.getInstance().getTime()));
        chemistCart.setAmount(price.toString());
        chemistCart.setSub_stkID("");

        //chemistCart.setStatus(false);
        //chemistCart.
        //chemistCart.


        DaoSession daoSession = ((AppController) ctx.getApplicationContext()).getDaoSession();

        ChemistCartDao chemistCartDao = daoSession.getChemistCartDao();

        chemistCartDao.insertOrUpdateCart(chemistCart, true);

        ((InventorylistOfflineActivity) ctx).createCartBadge(chemistCartDao.getCartItem(call_plan_chemist_id, true).size());

        // createCartBadge(chemistCartDao.ge);

        //Log.v("GreenDao", chemistCart.getId().toString());

        //   OGtoast.OGtoast("Product added to cart succesfully", Create_Order_Salesman.this);


    }


    void get_cart_id() {

        if (call_plan_chemist_id != null) {
            Cursor crs_cart = db.get_chemist_cart(call_plan_chemist_id);

            if (crs_cart != null && crs_cart.getCount() > 0) {
                while (crs_cart.moveToNext()) {
                    Doc_Id = crs_cart.getString(crs_cart.getColumnIndex("DOC_NO"));
                    Cart_Id_available = true;
                    n_product_cart_count = (db.get_total_order_item_count(Doc_Id));
                }
            }


            if (Doc_Id == null) {
                Doc_Id = "OG" + sdf.format(Calendar.getInstance().getTime()) + (int) (Math.random() * 90) + 100;
                // ;
                Cart_Id_available = false;

            }
        }

    }


}
