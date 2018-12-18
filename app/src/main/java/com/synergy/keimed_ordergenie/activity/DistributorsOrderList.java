package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.OrderlistAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_orderlist_distributor;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class DistributorsOrderList extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess,DatePickerDialog.OnDateSetListener {

    private RecyclerView recyclerView;
    private OrderlistAdapter orderlistAdapter;
    // List<Orderlistmodal> orderlist = new ArrayList<>();
    List<m_orderlist_distributor> postss = new ArrayList<m_orderlist_distributor>();
    List<m_orderlist_distributor> postss_new = new ArrayList<m_orderlist_distributor>();
    private Date current_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialog dpd_start_date, dpd_end_date;
    TextView startdate,enddate;
    private Date filter_start_date, filter_end_date;
    private int nYear, nMonth, Nday;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributors_order_list);
        setTitle("Orders");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
      //  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
       // recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        sendRequestdata();
        // orderListing();

//        List<m_orderlist_distributor> childsList = new ArrayList<>();
//        childsList.add(new m_orderlist_distributor("April"));
//        childsList.add(new m_orderlist_distributor("Austin"));
//        childsList.add(new m_orderlist_distributor("Alex"));
//        childsList.add(new m_orderlist_distributor("Aakash"));
//
//        //Create a List of Section DataModel implements SectionItem
//        List<Sections> sections = new ArrayList<>();
//        sections.add(new Sections(childsList, "A"));
//
//        childsList = new ArrayList<>();
//        childsList.add(new m_orderlist_distributor("Bill Gates"));
//        childsList.add(new m_orderlist_distributor("Bob Proctor"));
//        childsList.add(new m_orderlist_distributor("Bryan Tracy"));
//        sections.add(new Sections(childsList, "B"));
//
//        childsList = new ArrayList<>();
//        childsList.add(new m_orderlist_distributor("Intruder Shanky"));
//        childsList.add(new m_orderlist_distributor("Invincible Vinod"));
//        sections.add(new Sections(childsList, "I"));
//
//        childsList = new ArrayList<>();
//        childsList.add(new m_orderlist_distributor("Jim Carry"));
//        sections.add(new Sections(childsList, "J"));
//
//        childsList = new ArrayList<>();
//        childsList.add(new m_orderlist_distributor("Neil Patrick Harris"));
//        sections.add(new Sections(childsList, "N"));
//
//        childsList = new ArrayList<>();
//        childsList.add(new m_orderlist_distributor("Orange"));
//        childsList.add(new m_orderlist_distributor("Olive"));
//        sections.add(new Sections(childsList, "O"));
//
//
//        AdapterSectionRecycler adapterRecycler = new AdapterSectionRecycler(this, sections, recyclerView, R.layout.section_layout, R.layout.item_layout);
//        recyclerView.setAdapter(adapterRecycler);
//        adapterRecycler.setOnItemClickListener(new SectionRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int sectionPosition, int itemPosition) {
//                Log.d(sectionPosition + "", itemPosition + "");
//            }
//        });
//
//        adapterRecycler.setOnSectionClickListener(new SectionRecyclerViewAdapter.OnSectionClickListener() {
//            @Override
//            public void onSectionClick(int position) {
//                Log.d("sectionPosition", position + "");
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Customer Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                List<m_orderlist_distributor> filteredModelList = new ArrayList<>();
                for (m_orderlist_distributor model : postss)
                {
                    final String text = model.getClient_LegalName().toLowerCase();
                    if (text.contains(newText))
                    {
                        filteredModelList.add(model);
                    }
                }
                fill_order_list(filteredModelList);
                recyclerView.scrollToPosition(0);

                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
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
    private void show_dialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        //  final View dialogview = inflater.inflate(R.layout.dialog_product_catalog_stockist_filter, null);
        final View dialogview = inflater.inflate(R.layout.dialog_distributor_orderlist_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        startdate = (TextView) dialogview.findViewById(R.id.start_date);
        enddate = (TextView) dialogview.findViewById(R.id.end_date);

        final RadioButton chk_process = (RadioButton) dialogview.findViewById(R.id.chk_processing);
        final RadioButton chk_complete = (RadioButton) dialogview.findViewById(R.id.chk_completed);
        final RadioButton chk_submited = (RadioButton) dialogview.findViewById(R.id.chk_submited);
        final RadioButton chk_deliver = (RadioButton) dialogview.findViewById(R.id.chk_delivered);

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(startdate.getText().toString());
                    Log.d("current_date",String.valueOf(current_date));

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    nYear = Integer.parseInt(sdYear.format(current_date));
                    nMonth = Integer.parseInt(sdMonth.format(current_date)) - 1;
                    Nday = Integer.parseInt(sdDay.format(current_date));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                dpd_start_date = DatePickerDialog.newInstance(
                        DistributorsOrderList.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_start_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_start_date.setMaxDate(Calendar.getInstance());

            }


        });

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(enddate.getText().toString());
                    Log.d("enddate",String.valueOf(current_date));

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {

                    nYear = Integer.parseInt(sdYear.format(current_date));
                    nMonth = Integer.parseInt(sdMonth.format(current_date)) - 1;
                    Nday = Integer.parseInt(sdDay.format(current_date));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                dpd_end_date = DatePickerDialog.newInstance(
                        DistributorsOrderList.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_end_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_end_date.setMaxDate(Calendar.getInstance());
            }


        });

        dialogview.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter_dialog_conditions(filter_start_date, filter_end_date, chk_process.isChecked(), chk_complete.isChecked(),chk_submited.isChecked(),chk_deliver.isChecked());
                infoDialog.dismiss();
            }
        });
        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               fill_order_list(postss);
                infoDialog.dismiss();
            }
        });

        set_attributes(infoDialog);
        infoDialog.show();
    }
    private void sendRequestdata() {
        try {

            String Stockist_id = pref.getString(CLIENT_ID, "0");
            String date = sdf.format(current_date);
            Calendar c= Calendar.getInstance();
            c.add(Calendar.MONTH, -5);
            String lastsixDate=c.get(Calendar.YEAR)+ "-" + (c.get(Calendar.MONTH) + 1) + "-" +c.get(Calendar.DATE) ;
//            Log.d("date6",lastsixDate);
//            Log.d("date",date);
//            Log.d("date11",current_date.toString());
//        MakeWebRequest.MakeWebRequest("get", AppConfig.DISTRIBUTOR_ORDERS,
//                AppConfig.DISTRIBUTOR_ORDERS +"["+Stockist_id+","+0+","+1+",\""+sdf.format(current_date)+",\""+sdf.format(current_date)+"]" , this, true);
            MakeWebRequest.MakeWebRequest("get", AppConfig.DISTRIBUTOR_ORDERS,
                    AppConfig.DISTRIBUTOR_ORDERS +"["+Stockist_id+","+0+","+0+",\""+lastsixDate+",\""+",\""+sdf.format(current_date)+",\"]" , this, true);

            //   " http://www.ordergenie.co.in/api/orders/getOrdersDist/[1240,0,0,\"2017-10-11\",\"2018-01-11\"]"
        }catch (Exception e)
        {
            Log.d("dateexcep",e.getMessage());
        }

    }

    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isProcessing, Boolean isCompletedChecked,Boolean isSubmited,Boolean isDelivered) {

        Date convertedDate = null;
        final List<m_orderlist_distributor> filteredModelList = new ArrayList<>();
        for (m_orderlist_distributor model : postss) {

            if (startDate != null && endDate != null) {
                final String date = model.getDoc_Date();
                try {
                    convertedDate = dateFormat.parse(date);
                    String c_date = sdf.format(convertedDate);
                    convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                } catch (Exception e) {
                    Log.d("Excep",e.getMessage());
                }

                if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                {
                    if (isProcessing) {
                        if (model.getStatus()==1) {
                            filteredModelList.add(model);
                        }
                    } else if (isCompletedChecked) {
                        if (model.getStatus()==3 || model.getStatus()==4 || model.getStatus()==5) {
                            filteredModelList.add(model);
                        }
                    }else if (isDelivered)
                    {
                        if (model.getStatus()==5) {
                            filteredModelList.add(model);
                        }
                    }
                    else if (isSubmited)
                    {
                        if (model.getStatus()==0) {
                            filteredModelList.add(model);
                        }
                    }
                    else {
                        filteredModelList.add(model);
                    }
                }

            } else {
                if (isProcessing) {
                    if (model.getStatus()==1) {
                        filteredModelList.add(model);
                    }
                }

                if (isCompletedChecked) {
                    if (model.getStatus() == 3 || model.getStatus()== 4 || model.getStatus()== 5) {
                        filteredModelList.add(model);
                    }
                }
                else if (isDelivered)
                {
                    if (model.getStatus()==5) {
                        filteredModelList.add(model);
                    }
                }
                else if (isSubmited)
                {
                    if (model.getStatus()==0) {
                        filteredModelList.add(model);
                    }
                }
            }
        }
       fill_order_list(filteredModelList);
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

    //    private void orderListing() {

//
//        Orderlistmodal orderlistmodal = new Orderlistmodal("25 Nov,2017","Pramod Chemist","09:30","1 Items","Rs. 250","PC");
//        orderlist.add(orderlistmodal);
//        orderlistmodal = new Orderlistmodal("26 Nov,2017","Appolo Chemist","10:30","2 Items","Rs. 350","AC");
//        orderlist.add(orderlistmodal);
//
//        orderlistmodal = new Orderlistmodal("28 Nov,2017","New Life Chemist","11:00","5 Items","Rs. 550","NC");
//        orderlist.add(orderlistmodal);
//
//        orderlistmodal = new Orderlistmodal("2 DEC,2017","Novo Care Medicos","09:30","8 Items","Rs. 650","NM");
//        orderlist.add(orderlistmodal);
//
//        orderlistmodal = new Orderlistmodal("6 DEC,2017","Maruti Medicos","1:30","9 Items","Rs. 800","MM");
//        orderlist.add(orderlistmodal);
//        orderlistAdapter.notifyDataSetChanged();
//    }
    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if(response!=null)
        {
            Log.d("PRINT_RESPHERE11", f_name);
            Log.d("PRINT_RESPHERE12", String.valueOf(response));

            try {
                if(f_name.equals(AppConfig.DISTRIBUTOR_ORDERS))
                {
                    get_distributorDatalist(response.toString());
                }

            }catch (Exception e)
            {
                Log.d("PRINT_RESPHERE13", e.getMessage());
            }
        }
    }

    private void get_distributorDatalist(String jsonData) {
        if(!jsonData.isEmpty())
        {
            Log.d("DOrderlist",jsonData);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            postss = new ArrayList<m_orderlist_distributor>();
            postss_new=new ArrayList<m_orderlist_distributor>();
            postss = Arrays.asList(gson.fromJson(jsonData, m_orderlist_distributor[].class));
            //postss = new LinkedList<m_orderlist_distributor>(Arrays.asList(gson.fromJson(jsonData, m_orderlist_distributor[].class)));
//            for (int i=0;i<postss.size();i++)
//            {
//             if (postss.get(i).getDoc_Date().equals(postss.get(i+1).getDoc_Date()))
//             {
//                 postss_new.add(postss.get(i));
//             }
//
//                List<m_orderlist_distributor> childsList = new ArrayList<>();
//                childsList.add(new m_orderlist_distributor(postss.get(i).getClient_LegalName()));
//
//                //Create a List of Section DataModel implements SectionItem
//                List<Sections> sections = new ArrayList<>();
//                sections.add(new Sections(childsList, postss.get(i).getDoc_Date()));
//
//                AdapterSectionRecycler adapterRecycler = new AdapterSectionRecycler(this, sections, recyclerView, R.layout.section_layout, R.layout.item_layout);
//                recyclerView.setAdapter(adapterRecycler);
//                adapterRecycler.setOnItemClickListener(new SectionRecyclerViewAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int sectionPosition, int itemPosition) {
//                        Log.d(sectionPosition + "", itemPosition + "");
//                    }
//                });
//
//                adapterRecycler.setOnSectionClickListener(new SectionRecyclerViewAdapter.OnSectionClickListener() {
//                    @Override
//                    public void onSectionClick(int position) {
//                        Log.d("sectionPosition", position + "");
//                    }
//                });
//            }
               Collections.sort(postss, new Comparator<m_orderlist_distributor>() {
                   @Override
                   public int compare(m_orderlist_distributor o1, m_orderlist_distributor o2) {
                       return o2.getDoc_Date().compareTo(o1.getDoc_Date());
                   }
               });
                fill_order_list(postss);
        }
    }

    public void fill_order_list(final List<m_orderlist_distributor> posts_s)
    {
        orderlistAdapter = new OrderlistAdapter(this,posts_s);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderlistAdapter);

    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (dpd_start_date != null) {
            if (view.getId() == dpd_start_date.getId()) {
                try {
                    filter_start_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    startdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (Exception e) {
                    Log.d("Excep",e.getMessage());
                }
            }
            dpd_start_date = null;
        }

        if (dpd_end_date != null) {
            if (view.getId() == dpd_end_date.getId()) {
                try {
                    filter_end_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "T00:00:00.000Z");
                    enddate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (Exception e) {
                    Log.d("Excepp",e.getMessage());
                }
                dpd_end_date = null;
            }

        }

    }
}