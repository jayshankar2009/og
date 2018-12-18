package com.synergy.keimed_ordergenie.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.R;

import butterknife.ButterKnife;

/**
 * Created by admin on 12/1/2017.
 */

public class DistributorOrderStatusActivity extends AppCompatActivity {
TextView order_received, order_processing,order_submitted,order_completed,order_delivered,order_id,update_status;
String OrderIdNo = "8971";
    AlertDialog alertDialog1;
    CharSequence[] values = {" ORDER RECEIVED "," ORDER PROCESSING "," ORDER SUBMITTED "," ORDER COMPLETED "," ORDER DELIVERED "};
    TextView order_receiveddate, order_processingdate,order_submitteddate,order_completeddate,order_delivereddate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_order_status);
        ButterKnife.bind(this);
        order_received = (TextView)findViewById(R.id.order_received) ;
        order_processing = (TextView)findViewById(R.id.order_processing) ;
        order_submitted = (TextView)findViewById(R.id.order_submitted) ;
        order_completed = (TextView)findViewById(R.id.order_completed) ;
        order_delivered = (TextView)findViewById(R.id.order_delivered) ;
        order_receiveddate = (TextView)findViewById(R.id.order_received_date) ;
        order_processingdate = (TextView)findViewById(R.id.order_processing_date) ;
        order_submitteddate = (TextView)findViewById(R.id.order_submitted_date) ;
        order_completeddate = (TextView)findViewById(R.id.order_completed_date) ;
        order_delivereddate = (TextView)findViewById(R.id.order_delivered_date) ;
        order_id = (TextView)findViewById(R.id.orderid) ;
        update_status = (TextView)findViewById(R.id.btn_update_status) ;
      //  initImageLoader();

     //   getSupportActionBar().setDisplayShowHomeEnabled(true);


        //pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

       // Chemist_id=pref.getString(CLIENT_ID,"");

      //  product_id=String.valueOf(getIntent().getIntExtra("product_id",0));

       // MakeWebRequest.MakeWebRequest("get", AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL,
        //        AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL + "["+product_id+","+Chemist_id+"]", this, true);
        updatestatus();

        update_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialogWithRadioButtonGroup() ;
            }
        });
    }

    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(DistributorOrderStatusActivity.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        Toast.makeText(DistributorOrderStatusActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:

                        Toast.makeText(DistributorOrderStatusActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 2:

                        Toast.makeText(DistributorOrderStatusActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 3:

                        Toast.makeText(DistributorOrderStatusActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 4:

                        Toast.makeText(DistributorOrderStatusActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    private void updatestatus() {
    order_id.setText("ORDER ID "+OrderIdNo);

    }




}
