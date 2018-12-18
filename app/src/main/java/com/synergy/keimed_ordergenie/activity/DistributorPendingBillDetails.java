package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.PendinglistDetailsAdapter;
import com.synergy.keimed_ordergenie.model.d_pending_line_items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 12/4/2017.
 */

public class DistributorPendingBillDetails extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    RecyclerView expandableListView;
    private MenuItem textView;
    private Menu oMenu;
    PendinglistDetailsAdapter expandlistAdapter;
    List<String> listProductHeader;
    HashMap<String,List<String>> listProductChilds;
    List<d_pending_line_items> line_items=new ArrayList<d_pending_line_items>();
    ImageView back;
    TextView txt_custid,txt_custname,txt_count;
    public static final String STOCKIST_INVOICE_No = "invoice_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_distributor_productcatlog);
        setContentView(R.layout.activity_distributor_pendingbill_details);
        txt_custid=(TextView)findViewById(R.id.txt_custid);
        txt_custname=(TextView)findViewById(R.id.txt_title);
        txt_count=(TextView)findViewById(R.id.txt_count);
        back=(ImageView) findViewById(R.id.back_btn);

       /* setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Product Dialog");*/
        // LinearLayout textView = (LinearLayout) findViewById(R.id.heading_layout);
        line_items=(ArrayList<d_pending_line_items>)getIntent().getSerializableExtra("ArrayList");
        Log.d("line_items", String.valueOf(line_items));
        Log.d("line_itemss",String.valueOf(line_items.size()));
        String Cust_Id=getIntent().getStringExtra("cust_id");
        String Cust_Name=getIntent().getStringExtra("cust_name");
        txt_custid.setText(Cust_Id);
        txt_custname.setText(Cust_Name);
        txt_count.setText(String.valueOf(line_items.size()));
        prepareDataList();
        expandableListView = (RecyclerView) findViewById(R.id.expand_productcatlog);
        expandlistAdapter = new PendinglistDetailsAdapter(line_items,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        expandableListView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(expandableListView.getContext(),
                DividerItemDecoration.VERTICAL);
        expandableListView.addItemDecoration(dividerItemDecoration);
        expandableListView.setItemAnimator(new DefaultItemAnimator());
        // setting list adapter
        expandableListView.setAdapter(expandlistAdapter);
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
//        {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
//            {
//                Log.d(">>>", "" + groupPosition);
//                Intent intent = new Intent(getApplicationContext(), Invoice_details.class);
//                //put invoice id
//                intent.putExtra(STOCKIST_INVOICE_No, String.valueOf(line_items.get(groupPosition).getInvoiceid()));
//                intent.putExtra("invoice_no",String.valueOf(line_items.get(groupPosition).getInvoiceno()));
//                intent.putExtra("chemist_order_date",String.valueOf(line_items.get(groupPosition).getInvoicedate()));
//                startActivity(intent);
//                return false;
//            }
//        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), DistributorPendingBills.class);
                startActivity(intent);
                finish();
            }
        });
        // passing_catlogData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pendingbill_details, menu);
        oMenu = menu;
//        textView = (MenuItem) menu.findItem(R.id.action_billcount).getActionView();
//        textView.setTitle(line_items.size());
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//
//                // newText = newText.toLowerCase();
//                // filter_on_text(newText);
//
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//        });
//
//
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                //_toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
//
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // _toolbar.findViewById(R.id.sp_customer_type).setVisibility(View.GONE);
//            }
//        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareDataList() {

        listProductHeader = new ArrayList<String>();
        listProductChilds = new HashMap<String, List<String>>();


        listProductHeader.add("29 SEP, 2017");
        listProductHeader.add("19 AUG, 2017");
        listProductHeader.add("02 SEP, 2017");


        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Lucky Pharma");
       /* top250.add("Rxmedikart");
        top250.add("Smartpractix");
        top250.add("Ordergenie");
        top250.add("Tcs");*/

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Pramod Pharma");
        /*nowShowing.add("medico");
        nowShowing.add("1000");
        nowShowing.add("4444");
        nowShowing.add("viroopa");*/


        List<String> listd = new ArrayList<String>();
        listd.add("Harish Pharma");

        listProductChilds.put(listProductHeader.get(0), top250); // Header, Child data
        listProductChilds.put(listProductHeader.get(1), nowShowing);
        listProductChilds.put(listProductHeader.get(2), listd);
    }

    private void passing_catlogData() {

    /*    expand_productcatlogItems = new ExpandlistAdapter(this, listProductHeader, listProductChilds);
        expand_productcatlog.setAdapter(expand_productcatlogItems);
    */

    }
}
