package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.synergy.keimed_ordergenie.R;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout collectReport,deliveryReport,lnrSummary,lnrSalesSummary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        collectReport=(LinearLayout)findViewById(R.id.lnr_all_collection_report);
        deliveryReport=(LinearLayout)findViewById(R.id.lnr_all_delivery_report);
        lnrSummary=(LinearLayout)findViewById(R.id.lnr_all_Summary);
        lnrSalesSummary=(LinearLayout)findViewById(R.id.lnr_all_sales_Summary);
        lnrSalesSummary.setOnClickListener(this);
        lnrSummary.setOnClickListener(this);
        collectReport.setOnClickListener(this);
        deliveryReport.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
        }
         setTitle("Report");
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.lnr_all_collection_report)
        {
        Intent intent = new Intent(this, SelectedDailypaymentList.class);
        intent.putExtra("flag","Dashboard");
        startActivity(intent);
        }else  if (v.getId()==R.id.lnr_all_delivery_report)
        {
            Intent intent = new Intent(this, SelectedDailyDeliveryList.class);
            intent.putExtra("flag","Dashboard");
            startActivity(intent);
        }else if(v.getId()==R.id.lnr_all_Summary) {
            Intent intent = new Intent(this, SummaryOrder_Activity.class);
            intent.putExtra("flag","Dashboard");
            startActivity(intent);
        }else if(v.getId()==R.id.lnr_all_sales_Summary) {
            Intent intent = new Intent(this, SalesSummaryReportActivity.class);
            intent.putExtra("flag","Dashboard");
            startActivity(intent);
        }

    }

}
