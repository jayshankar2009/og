package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.Exad_pending_all_bill;
import com.synergy.keimed_ordergenie.adapter.Summary_Adapter;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.Orders;
import com.synergy.keimed_ordergenie.model.m_all_pending_list;
import com.synergy.keimed_ordergenie.model.m_delivery_invoice_list;
import com.synergy.keimed_ordergenie.model.summary_order;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class SummaryOrder_Activity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess, DatePickerDialog.OnDateSetListener {

    JSONObject obj;
    Toolbar _toolbar;
    private SearchView searchView;
    private SharedPreferences pref;
    String userId;
    TextView textView_date;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private int nYear, nMonth, Nday;
    private Date filter_start_date, filter_end_date;
    DatePickerDialog dpd_start_date, dpd_end_date;
    TextView txt_start_date, txt_end_date;
    private Date current_date = Calendar.getInstance().getTime();
    List<summary_order> posts = new ArrayList<summary_order>();
    ExpandableListView _exp_pending_bill;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summayorder);
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _exp_pending_bill = (ExpandableListView) findViewById(R.id.ex_summary);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        /*try {
            obj = new JSONObject(readJSONFromAsset());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = pref.getString(USER_ID, "");
        //   Toast.makeText(getApplicationContext(),"----"+userId,Toast.LENGTH_LONG).show();

        MakeWebRequest.MakeWebRequest("get", AppConfig.getOrderReportSalesmanWithDate,
                AppConfig.getOrderReportSalesmanWithDate + "[" + userId + ",\"" + String.valueOf(sdf.format(current_date)) + "\"," + "\"" + String.valueOf(sdf.format(current_date)) + "\"" + "]",
                this, true);
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("Summary_Report");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            ResponseData(json);
            //   Log.i("Json Read",""+json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void ResponseData(String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson mGson = builder.create();
        Type listType = new TypeToken<List<summary_order>>() {
        }.getType();
        posts = mGson.fromJson(json, listType);
        Summary_Adapter adapter = new Summary_Adapter(posts, this);
        _exp_pending_bill.setAdapter(adapter);
        //  _exp_pending_bill.setChildIndicator(null);
        //_exp_pending_bill

        Log.i("Json Response", "" + posts.get(0).getOrders().get(0).getAmount());

        /*try {

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String delivery_ClientID = jsonObject.getString("ClientID");
                String delivery_Client_Code = jsonObject.getString("Client_Code");
                String delivery_Client_Name = jsonObject.getString("Client_Name");
                String toatal_amount = jsonObject.getString("total amount");
                JSONArray jsonArray1 = jsonObject.getJSONArray("Orders");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                    String delivery_ErpsalesmanID = jsonObject1.getString("date");
                    String delivery_DeliveryDate = jsonObject1.getString("order_no");
                    String delivery_DeliveryStatus = jsonObject1.getString("amount");


                }
                }
            } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:

                return true;
            case R.id.action_filter:
                show_dialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delivery_invoice, menu);
        //  oMenu = menu;
        searchView = (SearchView) menu.findItem(R.id.action_invoicesearch).getActionView();
        // searchView.setQueryHint("Stockiest Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                List<summary_order> filteredModelList = new ArrayList<>();
                for (summary_order model : posts) {
                    final String text = model.getClientName().toLowerCase();
                    if (text.contains(newText)) {
                        filteredModelList.add(model);
                    }
                }
                json_update(filteredModelList);

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

    private void json_update(List<summary_order> posts) {

        // _txt_total_count.setText(posts.size() + " Orders");
        Summary_Adapter adapter = new Summary_Adapter(posts, this);
        _exp_pending_bill.setAdapter(adapter);
        Log.d("List", posts.toString());
    }


    private void show_dialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.dialog_delivery_invoice_list_filter, null);
        final Dialog infoDialog = new Dialog(this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        txt_start_date = (TextView) dialogview.findViewById(R.id.txt_start_date);
        txt_end_date = (TextView) dialogview.findViewById(R.id.txt_end_date);
        final CheckBox chk_pending = (CheckBox) dialogview.findViewById(R.id.chk_pending);
        final CheckBox chk_delivered = (CheckBox) dialogview.findViewById(R.id.chk_completed);

        if (filter_start_date != null) {
            txt_start_date.setText(sdf.format(filter_start_date));
        }
        if (filter_end_date != null) {
            txt_end_date.setText(sdf.format(filter_end_date));
        }
        txt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //  Log.d("date12", txt_start_date.getText().toString());
                    current_date = sdf.parse(txt_start_date.getText().toString());

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
                        SummaryOrder_Activity.this,
                        nYear,
                        nMonth,
                        Nday
                );
                dpd_start_date.show(getFragmentManager(), "Datepickerdialog");
                dpd_start_date.setMaxDate(Calendar.getInstance());

            }


        });


        txt_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    current_date = sdf.parse(txt_end_date.getText().toString());

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
                        SummaryOrder_Activity.this,
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

                filter_dialog_conditions(filter_start_date, filter_end_date);
                infoDialog.dismiss();
            }
        });

        dialogview.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // filter_summary_report(posts);
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


    private void filter_dialog_conditions(Date startDate, Date endDate) {

        MakeWebRequest.MakeWebRequest("get", AppConfig.getOrderReportSalesmanWithDate,
                AppConfig.getOrderReportSalesmanWithDate + "[" + userId + ",\"" + String.valueOf(sdf.format(startDate)) + "\"," + "\"" + String.valueOf(sdf.format(endDate)) + "\"" + "]",
                this, true);
    }

       /* Date convertedDate = null;

        final List<summary_order> filteredModelList = new ArrayList<>();

        if (posts != null) {
            for (summary_order model : posts) {
                final List<Orders> filterorder = new ArrayList<>();
                summary_order summary_order = new summary_order();
                summary_order.setClientCode(model.getClientCode());
                summary_order.setClientID(model.getClientID());
                summary_order.setClientName(model.getClientName());
                summary_order.setTotalAmount(model.getTotalAmount());
                for (int i=0;i<model.getOrders().size();i++) {

                    if (startDate != null && endDate != null) {
                        final String date = model.getOrders().get(i).getDoc_Date();
                        // final String date = model.getInvoiceDate();
                        try {
                            convertedDate = dateFormat.parse(date);
                            String c_date = sdf.format(convertedDate);
                            convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                            //   Log.d("date123", convertedDate.toString());
                        } catch (Exception e) {
                        }

                        if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                        {

                            filterorder.add(model.getOrders().get(i));



                        }

                    }else{
                        final String date = model.getOrders().get(i).getDoc_Date();
                        // final String date = model.getInvoiceDate();
                        try {
                            convertedDate = dateFormat.parse(date);
                            String c_date = sdf.format(convertedDate);
                            convertedDate = dateFormat.parse(c_date + "T00:00:00.000Z");
                            int  nYear = Integer.parseInt(sdYear.format(current_date));
                            int   nMonth = Integer.parseInt(sdMonth.format(current_date)) - 1;
                            int   Nday = Integer.parseInt(sdDay.format(current_date));
                            startDate = dateFormat.parse(nYear + "-" + (nMonth + 1) + "-" + Nday + "T00:00:00.000Z");
                            endDate = dateFormat.parse(nYear + "-" + (nMonth + 1) + "-" + Nday + "T00:00:00.000Z");
                            //   Log.d("date123", convertedDate.toString());
                        } catch (Exception e) {
                        }

                        if (((convertedDate.after(startDate)) || (convertedDate.equals(startDate))) && ((convertedDate.before(endDate))) || (convertedDate.equals(endDate))) //here "date2" and "date1" must be converted to dateFormat
                        {

                            filterorder.add(model.getOrders().get(i));



                        }

                    }

                }

                 if(filterorder.size()>0){
                    summary_order.setOrders(filterorder);
                    filteredModelList.add(summary_order);
                 }
                //  Log.d("Status12", model.getDeliveryFlag());

            }
            filter_summary_report(filteredModelList);
        } else {
            Toast.makeText(getApplicationContext(), "No Records Found", Toast.LENGTH_SHORT).show();
        }*/




    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
         //   Log.i("Json Response",""+response.toString());
        if (response!=null) {
         //   Log.i("Json Response13",""+response.toString());
            String strResponse = response.toString();
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            Type listType = new TypeToken<List<summary_order>>() {
            }.getType();
            posts = mGson.fromJson(strResponse, listType);
            Log.i("Json Response12",""+posts.toString());
            filter_summary_report(posts);
        }


}
   private void filter_summary_report(List<summary_order> list){
       Summary_Adapter adapter = new Summary_Adapter(list, this);
       _exp_pending_bill.setAdapter(adapter);
   }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {




    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (dpd_start_date != null) {
            if (view.getId() == dpd_start_date.getId()) {
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(0);
                    cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    if(DateValidation(cal.getTimeInMillis())){
                        filter_start_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "T00:00:00.000Z");
                        txt_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }else{
                        Toast.makeText(this,"you can not select date befor 3 month",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.toString();
                }
            }
            dpd_start_date = null;
        }

        if (dpd_end_date != null) {
            if (view.getId() == dpd_end_date.getId()) {
                try {
                    filter_end_date = dateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "T00:00:00.000Z");
                    txt_end_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (Exception e) {
                }
                dpd_end_date = null;
            }

        }
    }



    private boolean DateValidation(long selectedMilli) {
        Date dateOfBirth = new Date(selectedMilli);
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        if (age>3) {
           return false;
        } else {
            return true;
        }


    }
}
