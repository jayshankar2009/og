package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class DistributorCustomerDetails extends AppCompatActivity implements View.OnClickListener,MakeWebRequest.OnResponseSuccess {
    BarChart chart;
    private Image Graph_bar;
    ImageView call,email;
    TextView txtcall,reviewStatus,txtCode,txtType,txtAddress,txtPhone,txtEmail,txt_icon,txt_name;
    ImageView imageView;
    String cust_code,cust_type,cust_address,cust_phone,cust_email,cust_name,orderStatus;
    TextView txt_order,txt_sales,txt_bills,txt_orderstatus;
    private SharedPreferences preferences;
    private Date current_date = Calendar.getInstance().getTime();
    String Stockist_id;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_customer_details);
        setTitle("Customer Details");
        call=(ImageView)findViewById(R.id.btn_call);
        email=(ImageView)findViewById(R.id.btn_email);
        txtEmail=(TextView)findViewById(R.id.textview_email);
        txtPhone=(TextView)findViewById(R.id.txt_phone);
        txtAddress=(TextView)findViewById(R.id.customer_address);
        txtType=(TextView)findViewById(R.id.customer_type);
        txtCode=(TextView)findViewById(R.id.customer_code);
        txt_icon=(TextView)findViewById(R.id.cust_icon);
        txt_name=(TextView)findViewById(R.id.customer);
        txt_order=(TextView)findViewById(R.id.customer_order);
        txt_sales=(TextView)findViewById(R.id.customer_salenreturn);
        txt_bills=(TextView)findViewById(R.id.cust_pending_bill);
        txt_orderstatus = (TextView) findViewById(R.id.txt_statusorder);
        txt_order.setOnClickListener(this);
        txt_bills.setOnClickListener(this);
        txt_sales.setOnClickListener(this);


        imageView=(ImageView)findViewById(R.id.back_button);
       // reviewStatus=(TextView)findViewById(R.id.review_status);
        imageView.setOnClickListener(this);
        cust_code=getIntent().getStringExtra("CustomerCode");
        cust_address=getIntent().getStringExtra("CustomerAddress");
        cust_email=getIntent().getStringExtra("CustomerEmail");
        cust_phone=getIntent().getStringExtra("CustomerPhone");
        cust_name=getIntent().getStringExtra("CustomerName");

        orderStatus = getIntent().getStringExtra("orderStatus");

        txt_orderstatus.setText(orderStatus);

       // Log.d("orderStatus",orderStatus);


        set_character(cust_name);
        txtCode.setText((cust_code == null) ? "NA" : cust_code);
       // txtType.setText(cust_code);
        txtAddress.setText( (cust_address == null) ? "NA" : cust_address);
        txtPhone.setText((cust_phone == null) ? "NA" : cust_phone);
        txtEmail.setText((cust_email == null||cust_email.equals("")) ? "NA" : cust_email);
        txt_name.setText((cust_name == null) ? "NA" : cust_name);

        call.setOnClickListener(this);
      //  reviewStatus.setOnClickListener(this);
        chart = (BarChart)findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(20f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(9f, 6));

        BarDataSet dataset = new BarDataSet(entries, "# Sales");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Mon");
        labels.add("Tues");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");
        labels.add("Sun");

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.animateY(5000);
        chart.setDrawValueAboveBar(false);
        chart.setDrawGridBackground(false);

        XAxis bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        preferences = getApplicationContext().getSharedPreferences(PREF_NAME, getApplicationContext().MODE_PRIVATE);
        Stockist_id = preferences.getString(CLIENT_ID, "0");
      //  customer_sale_yearly();
    }


public  void  set_character(String Cust_Name)
{

    try {


        String parts[] = Cust_Name.split(" ");
       // String part1 = parts[0];
       // String part2 = parts[1];
       // txt_icon.setText(String.valueOf(part1.charAt(0))+String.valueOf(part2.charAt(0)));

        if(parts.length == 1) {

            String part1 = parts[0];
            txt_icon.setText(String.valueOf(part1.charAt(0)));

        }else if(parts.length>1){

            String part1 = parts[0];
            String part2 = parts[1];
            txt_icon.setText(String.valueOf(part1.charAt(0)) + String.valueOf(part2.charAt(0)));

        }


    }
    catch (Exception e)
    {

    }

}

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_call:
                call_function();
                break;
            case R.id.btn_email:
                email_function();
                break;
            case R.id.back_button:
               super.onBackPressed();
                break;
            case R.id.customer_order:
                Intent i=new Intent(this,DistributorsOrderList.class);
                startActivity(i);
                break;
            case R.id.customer_salenreturn:
                Intent i1=new Intent(this,DistributorReturnsActivity.class);
                startActivity(i1);
                break;
                case R.id.cust_pending_bill:
                    Intent i2=new Intent(this,DistributorPendingBills.class);
                    startActivity(i2);
            break;
        }
    }

    public void  call_function()
    {
        String number=txtPhone.getText().toString();
        Log.d("telephone",number);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        startActivity(callIntent);
    }

    public  void customer_sale_yearly()
    {

//        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_DISTRIBUTOR_CUSTOMER_SALECHART,
//                AppConfig.GET_DISTRIBUTOR_CUSTOMER_SALECHART +"["+Stockist_id+",\""+sdf.format(current_date)+",\"]" ,getApplicationContext(), true);

    }

    public void  email_function()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, "apurvahodawadekar.1040@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response!=null)
        {
            if (f_name.equals(AppConfig.GET_DISTRIBUTOR_CUSTOMER_SALECHART))
            {

            }
        }

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }
}
