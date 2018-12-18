package com.synergy.keimed_ordergenie.activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.database.MasterPlacedOrder;
import com.synergy.keimed_ordergenie.model.m_callplans;
import com.synergy.keimed_ordergenie.model.m_delivery_invoicelist;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.LocationAddress;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.RefreshData;
import com.synergy.keimed_ordergenie.utils.Utility;
import com.synergy.keimed_ordergenie.utils.get_current_location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.synergy.keimed_ordergenie.Constants.Constant.selectedChemistCartItemList;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

/**
 * Created by Apurva on 23-05-2018.
 */
public class PendingOrderCustomerList extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess , View.OnClickListener {

    private LocationManager locationManager;
    private String locationProvider,call_plan_customer_id;
    private double currentLocLat = 0;
    private double currentLocLong = 0;
    @BindView(R.id.rv_callPlanlist)
    RecyclerView rv_callPlanlist;
    String login_type,Doc_Id;
    float orderAmount;
    //  Boolean isSelectedAll=true;
    RecyclerView.Adapter<BindingViewHolder> adapter;
    private String User_id, Stockist_id;
    Date filter_start_date = Calendar.getInstance().getTime();
    List<ChemistCart> chemistCartList=new ArrayList<>();
    AppController globalVariable;
    AppCompatButton btConfirm,btDelete;
    LinearLayout layoutCheckbox,layoutBottom;
    CheckBox checkBoxSelectAll;
    String checkOption="";
    private  int count;
    private Boolean Cart_Id_available = false;
    private Date current_date = Calendar.getInstance().getTime();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,dd LLL yyyy");
    long init;
    Boolean isTakOrder, isPayment, isDelivery;
    SimpleDateFormat sql_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    // SimpleDateFormat sql_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    List<m_callplans> posts;
    List<m_delivery_invoicelist> posts_completed;
    List<m_delivery_invoicelist> posts_pending;
    List<m_delivery_invoicelist> posts_final;
    private static final String FORMAT = "%02d:%02d:%02d";
    ImageView img_take_order;
    ImageView img_payment;
    ImageView img_delivery;
    private LinearLayout linearLayoutCart,linearLayoutCustomer;
    private TextView textViewCartCount;
    private CheckBox checkBoxx;
    private String s_response_array;
    private SearchView searchView;
    SharedPreferences pref;
    private Menu oMenu;
    private SQLiteHandler db;
    String   Client_id;
    private LinearLayout lnr_start_call;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

//    @BindView(R.id.btn_show_route)
//    TextView btn_show_route;
//
//    @BindView(R.id.txt_sel_date)
//    TextView txt_sel_date;


    /* Cart Dao */
    private DaoSession daoSession;
    private ChemistCartDao chemistCartDao;


    /* Post SQLite Data */
    private int postDeliverySQLite = 0;
    private int postPaymentSQLite = 0;
    private JSONArray jsonArray;
    //private String invoiceArray = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderspending_show_itemcart);
        // setContentView(R.layout.activity_pending_orderscustomer);

        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        /* Initialize SQLite Class */
        db = new SQLiteHandler(this);
        /* Initialize Dao Session Class */
        daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();

        btConfirm=(AppCompatButton)findViewById(R.id.button_confirm);
        btDelete=(AppCompatButton)findViewById(R.id.button_delete);
        layoutCheckbox=(LinearLayout)findViewById(R.id.layout_checkbox);
        layoutBottom=(LinearLayout)findViewById(R.id.bottomLayout);
        checkBoxSelectAll=(CheckBox) findViewById(R.id.checkAll);
        //checkBoxSelectAll.setChecked(false);
        btConfirm.setOnClickListener(this);
        checkBoxSelectAll.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        //new get_current_location(CallPlan.this);
        //txt_sel_date.setText(dateFormat.format(filter_start_date));
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        globalVariable = (AppController) getApplicationContext();
        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");
        setTitle("Pending Orders");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
        }

//        checkBoxSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                final RecyclerView listView = getListView();
//                for(int i=0; i < adapter.getItemCount(); i++){
//
//                }
//            }
//        });

    }


//    @OnClick(R.id.txt_sel_date)
//    void select_date() {
//        try {
//            current_date = sdf.parse(txt_sel_date.getText().toString());
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        try {
//            nYear = Integer.parseInt(sdYear.format(current_date));
//            nMonth = Integer.parseInt(sdMonth.format(current_date)) - 1;
//            Nday = Integer.parseInt(sdDay.format(current_date));
//
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//        DatePickerDialog dpd = DatePickerDialog.newInstance(
//                PendingOrderCustomerList.this,
//                nYear,
//                nMonth,
//                Nday
//        );
//        dpd.show(getFragmentManager(), "Datepickerdialog");
//        dpd.setMaxDate(Calendar.getInstance());
//    }

//    @OnClick(R.id.btn_show_route)
//    void onclcik_route() {
//    //    Log.e("insidelocation", "hiii");
//        Intent i = new Intent(PendingOrderCustomerList.this, MapsActivity.class);
//        i.putExtra("response", s_response_array);
//        startActivity(i);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_call_plan, menu);


        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<m_callplans> filteredModelList = new ArrayList<>();
                for (m_callplans model : posts) {
                    final String text = model.getClient_LegalName().toLowerCase();
                    if (text.contains(newText))
                    {
                        filteredModelList.add(model);
                    }
                }
                fill_callplan(filteredModelList);

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
            case R.id.action_location:
                globalVariable.setFromMenuItemClick(true);
                new get_current_location(PendingOrderCustomerList.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    /* onResume */
    @Override
    public void onResume() {
        super.onResume();
        getChemistList();
    }


    /* Get Chemist List */
    private void getChemistList() {
        SharedPreferences share = getSharedPreferences("OGlogin", MODE_PRIVATE);
        String userId = share.getString("user_id", "");
        Cursor cursor = db.getSalesmanChemistList(userId);
        int cursorCount = cursor.getCount();
        posts = new ArrayList<>();
        if (cursorCount == 0) {
            Toast.makeText(this, "Stockist Call Plan List Not Found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    m_callplans m_callplans = new m_callplans();
                    String chemistId = cursor.getString(2);
                    String stockistCallPlanId = cursor.getString(3);
                    String clientLegalName = cursor.getString(5);
                    String latitude = cursor.getString(6);
                    String longitude = cursor.getString(7);
                    String address = cursor.getString(8);
                    String billAmount = cursor.getString(13);
                    String sequence = cursor.getString(14);
                    String taskStatus = cursor.getString(15);
                    String orderReceived = cursor.getString(16);
                    String isLocked = cursor.getString(18);
                    String DLExpiry = cursor.getString(19);
                    String Block_Reason = cursor.getString(20);

                    m_callplans.setChemistID(chemistId);
                    if (stockistCallPlanId.equalsIgnoreCase("null")) {
                        m_callplans.setStockistCallPlanID(0);
                    } else {
                        m_callplans.setStockistCallPlanID(Integer.valueOf(stockistCallPlanId));
                    }
                    m_callplans.setClient_LegalName(clientLegalName);
                    if (latitude.equalsIgnoreCase("null")) {
                        m_callplans.setLatitude(0.0);
                    } else {
                        m_callplans.setLatitude(Double.valueOf(latitude));
                    }
                    if (longitude.equalsIgnoreCase("null")) {
                        m_callplans.setLongitude(0.0);
                    } else {
                        m_callplans.setLongitude(Double.valueOf(longitude));
                    }
                    if (address.equalsIgnoreCase("null")) {
                        m_callplans.setClientLocation("");
                    } else {
                        m_callplans.setClientLocation(address);
                    }
                    if (billAmount.equalsIgnoreCase("null")) {
                        m_callplans.setBillAmount(0);
                    } else {
                        m_callplans.setBillAmount(Integer.valueOf(billAmount));
                    }
                    if (sequence.equalsIgnoreCase("null")) {
                        m_callplans.setSequence(0);
                    } else {
                        m_callplans.setSequence(Integer.valueOf(sequence));
                    }
                    if(DLExpiry.equalsIgnoreCase("null")){
                        m_callplans.setDLExpiry("NA");
                    }else {
                        m_callplans.setDLExpiry(DLExpiry);
                    }
                    if (Block_Reason.equalsIgnoreCase("null")){
                        m_callplans.setBlock_Reason("NA");
                    }else {
                        m_callplans.setBlock_Reason(Block_Reason);
                    }
                    m_callplans.setTaskStatus(taskStatus);
                    if (orderReceived.equalsIgnoreCase("null")) {
                        m_callplans.setOrderRecevied(0);
                    } else {
                        m_callplans.setOrderRecevied(Integer.valueOf(orderReceived));
                    }
//                    if (isLocked.equalsIgnoreCase("1")) {
//                        String lockstatus = "locked";
//                        m_callplans.setIsLocked(lockstatus);
//                    } else {
//                        String lockstatus = "unlocked";
//                        m_callplans.setIsLocked(lockstatus);
//                    }
                    m_callplans.setIsLocked(isLocked);
                    posts.add(m_callplans);

                    if (cursorCount == posts.size()) {
                        fill_callplan(posts);
                    }
                } while (cursor.moveToNext());
            }
        }
    }

    private void fill_callplan(final List<m_callplans> posts) {
        selectedChemistCartItemList = new ArrayList<>();
        count=0;
        Log.d("count11", String.valueOf(count));
        if (posts.size() > 0) {

            findViewById(R.id.emptyview).setVisibility(View.GONE);
        } else
        {
            findViewById(R.id.emptyview).setVisibility(View.VISIBLE);
        }
        adapter = new RecyclerView.Adapter<BindingViewHolder>() {
            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                ViewDataBinding binding = null;
                LayoutInflater inflater = LayoutInflater.from(PendingOrderCustomerList.this);
                binding = DataBindingUtil.inflate(inflater, R.layout.adpter_pending_customerlist, parent, false);
                img_take_order = (ImageView) binding.getRoot().findViewById(R.id.img_take_order);
                img_payment = (ImageView) binding.getRoot().findViewById(R.id.img_payment);
                img_delivery = (ImageView) binding.getRoot().findViewById(R.id.img_delivery);
                linearLayoutCart = (LinearLayout) binding.getRoot().findViewById(R.id.linear_cart_call_plan);
                linearLayoutCustomer = (LinearLayout) binding.getRoot().findViewById(R.id.layout_customer);
                textViewCartCount = (TextView) binding.getRoot().findViewById(R.id.txt_cart_count_call_plan);
                checkBoxx = (CheckBox) binding.getRoot().findViewById(R.id.checkbox);
                lnr_start_call = (LinearLayout) binding.getRoot().findViewById(R.id.call_plan_start);

                return new BindingViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(final BindingViewHolder holder, final int position) {
                final m_callplans callplan_list = posts.get(position);
                holder.getBinding().setVariable(BR.v_callplanlist, callplan_list);
                // Constant.selectedChemistCart = new ArrayList<>();

                List<ChemistCart> chemistCartListt = chemistCartDao.getCartItem(String.valueOf(callplan_list.getChemistID()), true);
                final List<List<ChemistCart>> chemistMasterCartList = new ArrayList<>();
                // Constant.selectedChemistCart.clear();
                //  Constant.selectedChemistCartItemList.clear();
                chemistCartList.clear();
                // chemistMasterCartList.add(chemistCartList);
                //  chemistMasterCartList.add(chemistCartList)
                // Log.d("count", String.valueOf(count) + isSelectedAll + ":" + callplan_list.isSelected());
                final int itemPosition = holder.getAdapterPosition();
                checkBoxx.setChecked(callplan_list.isSelected());
                // checkBoxx.setChecked(false);
                int cartSize = chemistCartListt.size();
                if (cartSize == 0) {

                    // btConfirm.setVisibility(View.GONE);
                    linearLayoutCart.setVisibility(View.GONE);
                    linearLayoutCustomer.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                } else {
                    // Log.d("demo", "hiiii:" + callplan_list.isSelected()+isSelectedAll);
                    count++;
                    //btConfirm.setVisibility(View.VISIBLE);
                    linearLayoutCustomer.setVisibility(View.VISIBLE);
                    linearLayoutCart.setVisibility(View.VISIBLE);
                    textViewCartCount.setText(String.valueOf(cartSize));
//                    if (checkOption.equals("check")) {
//                        if (!isSelectedAll) {
//                            callplan_list.setSelected(true);
//                            selectedChemistCartItemList.add(posts.get(itemPosition));
//                            Log.d("itemPosition11", String.valueOf(itemPosition));
//                            Log.d("RemovedItems14", String.valueOf(itemPosition) + posts.get(itemPosition).getClient_LegalName());
//                            Log.d("itemist12", String.valueOf(selectedChemistCartItemList));
//
//
//                        } else {
//
//                            callplan_list.setSelected(false);
//                            selectedChemistCartItemList.remove(posts.get(itemPosition));
//                            Log.d("itemPosition", String.valueOf(itemPosition));
//                            Log.d("itemist11", String.valueOf(selectedChemistCartItemList));
//                            Log.d("RemovedItems11", String.valueOf(itemPosition) + posts.get(itemPosition).getClient_LegalName());
//
//                        }
//                        checkBoxx.setChecked(callplan_list.isSelected());
//                        Log.d("checkBoxx", String.valueOf(callplan_list.isSelected()) + "xx" + checkBoxx.isChecked());
//                        //  adapter.notifyDataSetChanged();
//                    }

                }
                // int visible=rv_callPlanlist.getVi
                // if (count>0)
                //  {
                // btConfirm.setVisibility(View.VISIBLE);
                // layoutBottom.setVisibility(View.VISIBLE);
                //   layoutCheckbox.setVisibility(View.VISIBLE);

                // }

                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(PendingOrderCustomerList.this, Create_Order_Salesman.class);
                        i.putExtra("client_id", callplan_list.getChemistID());
                        i.putExtra("flag", "pending");
                        i.putExtra("chemist_stockist_name",callplan_list.getClient_LegalName());
                        startActivity(i);
                    }

                });

                checkBoxx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("RemovedItems10", String.valueOf(callplan_list.isSelected()));
                        if (callplan_list.isSelected()) {
                            callplan_list.setSelected(false);
                            // chemistCartList = chemistCartDao.getCartItem(String.valueOf(callplan_list.getChemistID()), true);
                            // Constant.selectedChemistCart.remove(posts.get(itemPosition));
//                            if (chemistCartList.size()>0) {
//                               // Constant.selectedChemistCart.remove(posts.get(itemPosition));
//                            }
                            selectedChemistCartItemList.remove(posts.get(itemPosition));
                            Log.d("RemovedItems11", String.valueOf(selectedChemistCartItemList));
                            Log.d("RemovedItems12", String.valueOf(itemPosition)+posts.get(itemPosition).getClient_LegalName());
                        } else {
//
                            callplan_list.setSelected(true);
                            //String ChemistID=posts.get(itemPosition).getChemistID();
                            //chemistCartList = chemistCartDao.getCartItem(ChemistID, true);
                            //chemistMasterCartList.add(chemistCartList);
                            // Constant.selectedChemistCart.add(posts.get(itemPosition));
//                            if (chemistCartList.size()>0) {
//                                //Constant.selectedChemistCart.add(chemistCartList);
//
//                            }
                            selectedChemistCartItemList.add(posts.get(itemPosition));
                            Log.d("RemovedItems13", String.valueOf(selectedChemistCartItemList));

                        }

                        notifyDataSetChanged();
                        checkBoxx.setChecked(callplan_list.isSelected());
                    }
                });


                holder.getBinding().executePendingBindings();
            }

            @Override
            public int getItemCount() {
                return posts.size();
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };

        rv_callPlanlist.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //rv_callPlanlist.setLayoutManager(new LinearLayoutManager(this),LinearLayoutManager.VERTICAL,false);
        //rv_callPlanlist.setItemAnimator(new DefaultItemAnimator());
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_callPlanlist.getContext(), DividerItemDecoration.VERTICAL);
//        rv_callPlanlist.addItemDecoration(dividerItemDecoration);
        //rv_callPlanlist.scrollToPosition(0);
        rv_callPlanlist.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_confirm)
        {
            if (selectedChemistCartItemList.size()>0)
            {
                confirm_dialog("No comment");
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please select Item",Toast.LENGTH_SHORT).show();
            }
        }
//        else if(v.getId()==R.id.checkAll)
//        {
//            checkOption="check";
//            Log.d("isCheck", String.valueOf(checkBoxSelectAll.isChecked()));
//         if (checkBoxSelectAll.isChecked())
//         {
//          //   checkBoxSelectAll.setChecked(false);
//            isSelectedAll=false;
////             Log.d("select11", String.valueOf(isSelectedAll));
////             for (m_callplans model : posts) {
////                 model.setSelected(true);
////             }
//         // selectAllItem(true);
//
//         }
//        else
//         {
//          isSelectedAll=true;
////             Log.d("select12", String.valueOf(isSelectedAll));
//           //  checkBoxSelectAll.setChecked(true);
//           //  selectAllItem(false);
////             for (m_callplans model : posts) {
////                 model.setSelected(false);
////             }
//         }
//
//         adapter.notifyDataSetChanged();
//       //  rv_callPlanlist.getAdapter().notifyDataSetChanged();
        //  }
        else if (v.getId()==R.id.button_delete)
        {
            if (selectedChemistCartItemList.size()>0)
            {
                delete_dialog();
            }
            else
            {
                Toast.makeText(this,"Please Select Item",Toast.LENGTH_SHORT).show();
            }
        }

    }
    // selectAllCheckBox = (CheckBox) rootView.findViewById(R.id.select_all_check_box);


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  isSelectedAll=false;
    }

    void selectAllItem(boolean isSelectedAll)
    {
        try {
            if (posts != null) {

                for (int index = 0; index < posts.size(); index++) {
                    // posts.get(index).setSelected(isSelectedAll);
                    posts.get(index).setSelected(isSelectedAll);
                    checkBoxx.setChecked(posts.get(index).isSelected());
                    if (isSelectedAll)
                    {
                        selectedChemistCartItemList.add(posts.get(index));
                    }
                    else
                    {
                        selectedChemistCartItemList.remove(posts.get(index));
                    }

                    Log.d("index", String.valueOf(index));
                    Log.d("isSelected", String.valueOf(isSelectedAll)+"::"+selectedChemistCartItemList);
                }



            }
            adapter.notifyDataSetChanged();
            // rv_callPlanlist.getAdapter().notifyDataSetChanged();

        } catch (Exception e) {

        }
    }
    void confirm_dialog(final String Comment) {
        new AlertDialog.Builder(PendingOrderCustomerList.this)
                .setTitle("Pending Orders")
                .setMessage("Do you wish to confirm your order?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        confirm_order(Comment);

                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();


    }

    void delete_dialog() {
        new AlertDialog.Builder(PendingOrderCustomerList.this)
                .setTitle("Pending Orders")
                .setMessage("Delete order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        for (int i=0;i<selectedChemistCartItemList.size();i++)
                        {
                            chemistCartList = chemistCartDao.getCartItem(selectedChemistCartItemList.get(i).getChemistID(), true);
                            chemistCartDao.deleteInTx(chemistCartList);
                            Log.d("Delete",selectedChemistCartItemList.get(i).getChemistID());
                            Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                        }
                        // finish();
                        dialog.dismiss();
                        rv_callPlanlist.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();
    }




    private class BindingViewHolder extends RecyclerView.ViewHolder {
        public BindingViewHolder(View itemView) {
            super(itemView);
        }
        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }
    }





//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        try {
//            filter_start_date = sdf.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//            txt_sel_date.setText(dateFormat.format(filter_start_date));
//            //Log.d("todayDates11", dateFormat.format(filter_start_date));
//            //Log.d("todayDates11", get_day_number(filter_start_date));
//        } catch (Exception e) {
//            e.toString();
//        }
//}

//    private String get_day_number(Date d) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(d);
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//
//        String daynumber = "1";
//
//        switch (day) {
//
//            case Calendar.MONDAY:
//                daynumber = "1";
//                break;
//            case Calendar.TUESDAY:
//                daynumber = "2";
//                break;
//
//            case Calendar.WEDNESDAY:
//                daynumber = "3";
//                break;
//            case Calendar.THURSDAY:
//                daynumber = "4";
//                break;
//            case Calendar.FRIDAY:
//                daynumber = "5";
//                break;
//            case Calendar.SATURDAY:
//                daynumber = "6";
//                break;
//
//            case Calendar.SUNDAY:
//                daynumber = "0";
//                // daynumber = "7";
//                // Current day is Sunday
//                break;
//        }
//        return daynumber;
//    }

    /* onStart */
    @Override
    protected void onStart() {
        super.onStart();
        // new checkInternetConn().execute(0);
        //getCollectedPayment();
        // new checkCollectedPayments().execute(0);
    }


    /* onPause */
    @Override
    protected void onPause() {
        super.onPause();
        // new checkInternetConn().execute(0)
    }

    /* Order Placed */
    private void confirm_order(String Comment) {
        try {

            for (m_callplans m_callplans : selectedChemistCartItemList) {
                Log.d("Chemist", m_callplans.getChemistID());


                JSONArray main_j_array = new JSONArray();
                JSONObject main_j_object = new JSONObject();

                JSONArray line_j_array = new JSONArray();
                JSONArray productarray = new JSONArray();

                // main_j_object.put("Transaction_No","");
                // main_j_object.put("CreatedBy", Integer.parseInt(Client_id));
                main_j_object.put("CreatedBy", pref.getString(USER_ID, ""));

                // Cursor crs_cart = db.get_chemist_cart(call_plan_customer_id);
                //##############

                chemistCartList = chemistCartDao.getCartItem(m_callplans.getChemistID(), true);
                if (chemistCartList != null && chemistCartList.size() > 0) {
                    ChemistCart chemistCart = chemistCartList.get(0);
                    //count toral order Amount
                    countTotalOrderAmount(chemistCartList);
                    if (chemistCartList.size() > 0) {

                        main_j_object.put("Client_ID", Integer.parseInt(chemistCart.getStockist_Client_id()));
                        main_j_object.put("DOC_NO", chemistCart.getDOC_ID());
                        Log.d("DOC", chemistCart.getDOC_ID());
                        main_j_object.put("Doc_Date", chemistCart.getCreatedon());
                        //main_j_object.put("Stockist_Client_id",Client_id+"  "+crs_cart.getString(crs_cart.getColumnIndex("Stockist_Client_id")));
                        main_j_object.put("Stockist_Client_id", Integer.parseInt(Stockist_id));
                        //    Log.d("stockisttID", client_id);
                        main_j_object.put("Remarks", chemistCart.getRemarks());
                        main_j_object.put("Items", chemistCartList.size());
                        main_j_object.put("Amount", orderAmount);
                        main_j_object.put("Status", 0);
                        main_j_object.put("Createdon", chemistCart.getCreatedon());
                        // main_j_object.put("Comments", editText.getText().toString());
                        // main_j_object.put("LoginName",pref.getString(CLIENT_LOGIN_NAME,""));
                        //main_j_object.put("USER_ID",pref.getString(USER_ID,""));

                        //   Log.d("asdfdff", String.valueOf(Integer.parseInt(crs_cart.getString(crs_cart.getColumnIndex("status")))));
                        Doc_Id = chemistCart.getDOC_ID();
                        Cart_Id_available = true;
                        //    }
                        // Cursor crs_cart_details = db.get_chemist_cart_detail(Doc_Id);

                        // if (crs_cart_details != null && crs_cart_details.getCount() > 0) {

                        for (int i = 0; i < chemistCartList.size(); i++) {
                            chemistCart = chemistCartList.get(i);
                            JSONObject line_j_object = new JSONObject();
                            line_j_object.put("Transaction_No", "");
                            //line_j_object.put("Doc_item_No", crs_cart_details.getString(crs_cart_details.getColumnIndex("Doc_item_No")));
                            line_j_object.put("Doc_item_No", i + 1);
                            line_j_object.put("Product_ID", Integer.parseInt(chemistCart.getProduct_ID()));
                            line_j_object.put("Qty", Integer.parseInt(chemistCart.getQty()));
                            line_j_object.put("UOM", chemistCart.getUOM());
                            line_j_object.put("Rate", Double.valueOf(chemistCart.getRate()));
                            line_j_object.put("Price", Double.valueOf(chemistCart.getPrice()));

                            if (Utility.internetCheck(this)) {

                            } else {
                                line_j_object.put("itemName", chemistCart.getItemname());
                                line_j_object.put("packSize", chemistCart.getPACK());
                            }
                            //  String.format("%.2f", crs_cart_details.getDouble(crs_cart_details.getColumnIndex("Price")));
                            line_j_object.put("MRP", Float.valueOf(chemistCart.getMRP()));
                            line_j_object.put("Createdon", chemistCart.getCreatedon());
                            line_j_object.put("CreatedBy", Integer.parseInt(m_callplans.getChemistID()));
                            line_j_array.put(line_j_object);
                            productarray.put(Integer.parseInt(chemistCart.getProduct_ID()));
                            //   Log.e("line_j_object", line_j_object.toString());
                        }

                        //  }

                        main_j_array.put(main_j_object);
                        main_j_array.put(line_j_array);
                        main_j_array.put(productarray);


                        Log.d("orderPlaceData", String.valueOf(main_j_array));
                /*Log.d("orderCustomerId", call_plan_customer_id);
                Log.d("orderDocId", Doc_Id);*/

                        MasterPlacedOrder masterPlacedOrder = new MasterPlacedOrder();
                        masterPlacedOrder.setJson(main_j_array.toString());
                        masterPlacedOrder.setCustomerID(m_callplans.getChemistID());
                        masterPlacedOrder.setDoc_ID(Doc_Id);

                        long confirm = daoSession.getMasterPlacedOrderDao().insert(masterPlacedOrder);

                        if (confirm > 0) {
                            chemistCartDao.deleteInTx(chemistCartList);
                            // adpter.notifyDataSetChanged();
                            Intent download_intent = new Intent(RefreshData.ACTION_CONFIRM_ORDER, null, this, RefreshData.class);
                            download_intent.putExtra("camefrom","pending");
                            startService(download_intent);
                        }

                        if (Utility.internetCheck(this)) {

                        } else {
                            /* For Insert Offline Orders */
                            db.insertOrder(User_id, User_id, Client_id, m_callplans.getChemistID(), m_callplans.getClient_LegalName(), Doc_Id,
                                    chemistCart.getCreatedon(), chemistCart.getRemarks(), String.valueOf(chemistCartList.size()),
                                    String.valueOf(orderAmount), "0", chemistCart.getCreatedon(), "", line_j_array.toString(),
                                    String.valueOf(productarray));
                        }
                        //db.insert_into_chemist_order_sync(Doc_Id, main_j_array.toString(), 0);
                        // db.delete_chemist_Cart(Doc_Id);
                        // db.delete_chemist_Cart_Details(Doc_Id);

                    }
                }
            }
            order_confirmed_dialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void order_confirmed_dialog() {
        new AlertDialog.Builder(PendingOrderCustomerList.this)
                .setTitle("Order")
                .setMessage("Order placed successfully.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //  finish();

                        // NEW ADDING LOCATION UPDATE 9 OCTOBER 2018

                        rv_callPlanlist.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
    }

    private void countTotalOrderAmount(List<ChemistCart> list) {
        orderAmount = 0;
        for (int i = 0; i < list.size(); i++) {
            orderAmount += Double.valueOf(list.get(i).getAmount());
        }
        // _OrderAmt.setText(" Rs." + String.format("%.2f", orderAmount));

        //cartAdapter.notifyDataSetChanged();
    }




    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Please enable the Location");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OGtoast.OGtoast("Location Services not enabled !. Unable to get the location", getApplicationContext());
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        if (response != null) {
            try {
                if (f_name.equals(AppConfig.POST_UPDATE_CURRENT_LOCATION)) {
                    String status = response.getString("Status");
                    Log.d("location_orderpend15", response.toString());
                    if (status.equalsIgnoreCase("success")) {
                        //     Toast.makeText(getBaseContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {

            }
        }

    }

}


