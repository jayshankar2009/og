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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.PendinglistAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.PendingListModal;
import com.synergy.keimed_ordergenie.model.d_pending_line_items;
import com.synergy.keimed_ordergenie.utils.ConstData;
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

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_STOCKIST_PENDING_BILLS;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class DistributorPendingBills extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess,DatePickerDialog.OnDateSetListener {

    String login_type,User_id;
    private SearchView searchView;
    private Menu oMenu;
    RecyclerView recycler_view;
    private PendinglistAdapter pendinglistAdapter;
    List<PendingListModal> pendinglist;
    List<PendingListModal> pendinglist_old;
    SharedPreferences pref;
    String Stockist_id;
    private TextView startdate,enddate;
    private Spinner sp1,sp2;
    private Date filter_start_date, filter_end_date;
    private Date current_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialog dpd_start_date, dpd_end_date;
    private int nYear, nMonth, Nday;
     Double start_range,end_range;
     TextView totalAmount;
     Double ToatlAmount=0.0;
    List<d_pending_line_items> line_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_pending_bills);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Stockist_id = pref.getString(CLIENT_ID, "0");
        User_id = pref.getString(USER_ID, "0");
        setTitle("Pending Bills");
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        totalAmount=(TextView)findViewById(R.id.total_amount);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_view.getContext(),
                DividerItemDecoration.VERTICAL);
        recycler_view.addItemDecoration(dividerItemDecoration);

        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        /*if(login_type.equals(STOCKIST)){

            Log.d("Login11","Distributor");
            get_distPendingBills();

        }else {
            Log.d("Login12","Salesman");
            get_SalesmanPendingBills();
        }*/

        get_distPendingBills();
    }

    private void get_SalesmanPendingBills() {

        MakeWebRequest.MakeWebRequest("get", GET_STOCKIST_PENDING_BILLS,
                GET_STOCKIST_PENDING_BILLS + "[" + Stockist_id + "," + User_id + "]", this, true);

    }

    private void get_distPendingBills()
    {
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_DISTRIBUTOR_PENDING_BILL_LIST,
                AppConfig.GET_DISTRIBUTOR_PENDING_BILL_LIST +Stockist_id,this,true );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customer_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Customer Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                List<PendingListModal> filteredModelList = new ArrayList<>();
                for (PendingListModal model : pendinglist)
                {
                    final String text = model.getCust_name().toLowerCase();
                    if (text.contains(newText))
                    {
                        filteredModelList.add(model);
                    }
                }
                fill_pending_list(filteredModelList);
                recycler_view.scrollToPosition(0);
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose()
            {
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

    public void fill_pending_list(final List<PendingListModal> posts_s)
    {
        pendinglistAdapter = new PendinglistAdapter(posts_s,DistributorPendingBills.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(pendinglistAdapter);
    }

    private void show_dialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        //  final View dialogview = inflater.inflate(R.layout.dialog_product_catalog_stockist_filter, null);
        final View dialogview = inflater.inflate(R.layout.dialog_pendingbill_stockist_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        infoDialog.setCancelable(true);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        startdate = (TextView) dialogview.findViewById(R.id.sp_start_date);
        enddate = (TextView) dialogview.findViewById(R.id.sp_end_date);
        sp1 = (Spinner) dialogview.findViewById(R.id.sp_start_amt);
        sp2 = (Spinner) dialogview.findViewById(R.id.sp_end_amt);
        final RadioButton chk_asc = (RadioButton) dialogview.findViewById(R.id.chk_ascending);
        final RadioButton chk_dsc = (RadioButton) dialogview.findViewById(R.id.chk_descending);
        final RadioButton chk_due_bills = (RadioButton) dialogview.findViewById(R.id.due_bills);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(startdate.getText().toString());
                    Log.d("current_date", String.valueOf(current_date));

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
                        DistributorPendingBills.this,
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
                    Log.d("enddate", String.valueOf(current_date));

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
                        DistributorPendingBills.this,
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
               filter_dialog_conditions(filter_start_date, filter_end_date, chk_asc.isChecked(), chk_dsc.isChecked(),start_range,end_range,chk_due_bills.isChecked());
                infoDialog.dismiss();
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fill_pending_list(pendinglist_old);
                infoDialog.dismiss();
            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if (position>0) {
                  start_range = Double.parseDouble(parent.getItemAtPosition(position).toString()) ;
              }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    end_range =Double.parseDouble(parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        set_attributes(infoDialog);
        infoDialog.show();
    }

    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isAscCheck, Boolean isDescCheck,Double Start_Range,Double End_Range,Boolean isDueCheck) {

        Date convertedDate = null;
        final List<PendingListModal> filteredModelList = new ArrayList<>();
        if (isAscCheck)
        {
            Collections.sort(pendinglist, new Comparator<PendingListModal>() {
                @Override
                public int compare(PendingListModal lhs, PendingListModal rhs) {
                    return lhs.getCust_outstanding().compareTo(rhs.getCust_outstanding());
                }
            });
        }
        else if (isDescCheck)
        {
            Collections.sort(pendinglist, new Comparator<PendingListModal>() {
                @Override
                public int compare(PendingListModal lhs, PendingListModal rhs) {
                    return rhs.getCust_outstanding().compareTo(lhs.getCust_outstanding());
                }
            });
        }
        for (PendingListModal model : pendinglist) {

            if (startDate != null && endDate != null) {

                line_item=new ArrayList<d_pending_line_items>();
                line_item=model.getLine_items();
                for (d_pending_line_items d_pending_line : line_item)
                {
                    final String date = d_pending_line.getInvoicedate();
                    try {
                        convertedDate = dateFormat.parse(date);
                        String c_date = sdf.format(convertedDate);
                        convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                    } catch (Exception e) {
                        Log.d("Excep",e.getMessage());
                    }

                    if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                    {
                        filteredModelList.add(model);
                    }

                    fill_pending_list(filteredModelList);
                }

            }
            else
            {
                fill_pending_list(pendinglist_old);
            }

            if(start_range!=null&&end_range!=null)
            {
                if (Double.parseDouble(model.getCust_outstanding())>=start_range&&Double.parseDouble(model.getCust_outstanding())<=end_range)
                {
                    filteredModelList.add(model);
                }
                fill_pending_list(filteredModelList);
            }

//            if (isDueCheck)
//            {
//                if (model.getDays_To_Pay()>0)
//                {
//
//                }
//            }


        }

    }
//    public static class CustomComparator implements Comparator<PendingListModal> {
//        @Override
//        public int compare(PendingListModal o1, PendingListModal o2) {
//
//            return o1.getTotalAmount().compareTo(o2.getTotalAmount());
//            //  return o1.getOrderDate().compareTo(o2.getOrderDate());
//        }
//
//    }

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

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if (response!=null)
        {
            Log.d("Distpendingbills11", String.valueOf(response));
            if (f_name.equals(AppConfig.GET_DISTRIBUTOR_PENDING_BILL_LIST)) {
                get_order_list_json(response.toString());
            }/*else if(f_name.equals(AppConfig.GET_STOCKIST_PENDING_BILLS)){
                get_order_list_json(response.toString());

            }*/
        }
    }

    private void get_order_list_json(String jsondata) {

        if (!jsondata.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            pendinglist = new ArrayList<PendingListModal>();
            pendinglist_old=new ArrayList<PendingListModal>();
           // pendinglist = new LinkedList<PendingListModal>(Arrays.asList(mGson.fromJson(jsondata, PendingListModal[].class)));
            pendinglist = Arrays.asList(mGson.fromJson(jsondata, PendingListModal[].class));
            pendinglist_old=pendinglist;
            Log.d("pendingList", String.valueOf(pendinglist));
            for (int i=0;i<pendinglist.size();i++)
            {
                if (pendinglist.get(i).getCust_outstanding()!=null)
                ToatlAmount=ToatlAmount+Double.parseDouble(pendinglist.get(i).getCust_outstanding());

            }
            totalAmount.setText("Rs."+String.valueOf(ToatlAmount));
            fill_pending_list(pendinglist_old);
        }

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
