package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.DistributorSalesExpanListAdapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_salesreturn_distributor;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DistributorReturnsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,MakeWebRequest.OnResponseSuccess {
    private SearchView searchView;
    private  RecyclerView rv_datalist;
    List<m_salesreturn_distributor> posts;
    ExpandableListView expandableListView;
    DistributorSalesExpanListAdapter listAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private TextView startdate,enddate;
    private Date filter_start_date, filter_end_date;
    private Date current_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialog dpd_start_date, dpd_end_date;
    private int nYear, nMonth, Nday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_returns);
       // rv_datalist=(RecyclerView)findViewById(R.id.rv_datalist);
        setTitle("Returns");
        expandableListView=(ExpandableListView)findViewById(R.id.rv_datalist);
        expandableListDetail = getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        listAdapter = new DistributorSalesExpanListAdapter(getApplicationContext(),expandableListTitle,getData());
        expandableListView.setAdapter(listAdapter);


        get_SalesandReturns();
    }

    private void get_SalesandReturns() {

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_DISTRIBUTOR_SALESRETURNS,
                AppConfig.GET_DISTRIBUTOR_SALESRETURNS + 1240, this, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_list, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Customer Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                List<m_salesreturn_distributor> filteredModelList = new ArrayList<>();
                for (m_salesreturn_distributor model : posts)
                {
                    final String text = model.getMonth_name().toLowerCase();
                    if (text.contains(newText))
                    {
                        filteredModelList.add(model);
                    }
                }
              //  fill_stockist(filteredModelList);
                rv_datalist.scrollToPosition(0);
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

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        //HashMap<String, List<String>> expandableListDetail;
        List<String> cricket = new ArrayList<String>();
        cricket.add("ACILOC 300 MG");
//        cricket.add("Pakistan");
//        cricket.add("Australia");
//        cricket.add("England");
//        cricket.add("South Africa");

        List<String> football = new ArrayList<String>();
        football.add("CROCIN 500 MG");
//        football.add("Spain");
//        football.add("Germany");
//        football.add("Netherlands");
//        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("BIOSPOAS  500 MG");
//        basketball.add("Spain");
//        basketball.add("Argentina");
//        basketball.add("France");
//        basketball.add("Russia");

        expandableListDetail.put("AMAR MEDICAL STORE", cricket);

        expandableListDetail.put("MEDICOS PHARMA", football);

        expandableListDetail.put("LIFE CARE PHARMA", basketball);
        return expandableListDetail;

    }

    private void show_dialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        //  final View dialogview = inflater.inflate(R.layout.dialog_product_catalog_stockist_filter, null);
        final View dialogview = inflater.inflate(R.layout.dialog_returns_stockist_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        startdate = (TextView) dialogview.findViewById(R.id.start_date);
        enddate = (TextView) dialogview.findViewById(R.id.end_date);
        final RadioButton radio_breakage=(RadioButton)findViewById(R.id.chk_breakage);
        final RadioButton radio_expiry=(RadioButton)findViewById(R.id.chk_expiry);
        RadioButton radio_others=(RadioButton)findViewById(R.id.chk_others);
        RadioButton radio_non_moving=(RadioButton)findViewById(R.id.chk_non_moving);

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
                        DistributorReturnsActivity.this,
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
                        DistributorReturnsActivity.this,
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
                 filter_dialog_conditions(filter_start_date, filter_end_date, radio_breakage.isChecked(), radio_expiry.isChecked());
                infoDialog.dismiss();
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // fill_payment_list(expandableListDetail);
                infoDialog.dismiss();
            }
        });
        set_attributes(infoDialog);
        infoDialog.show();

    }


    private void filter_dialog_conditions(Date startDate, Date endDate, Boolean isCheckPayment, Boolean isCashPayment) {




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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
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

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if(response != null){

            if (f_name.equals(AppConfig.GET_DISTRIBUTOR_SALESRETURNS)){

                Log.d("salesreturn_responce",response.toString());

                get_salesreturn_json(response.toString());

            }
        }

    }

    private void get_salesreturn_json(String jsondata) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        //posts = new ArrayList<m_salesreturn_distributor>();
        //posts = new LinkedList<m_salesreturn_distributor>(Arrays.asList(gson.fromJson(jsondata,m_salesreturn_distributor[].class)));



    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }
}
