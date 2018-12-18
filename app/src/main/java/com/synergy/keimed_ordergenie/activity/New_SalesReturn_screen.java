package com.synergy.keimed_ordergenie.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.NewSalesReturnAdapter;
import com.synergy.keimed_ordergenie.adapter.ad_AutocompletSalesReturn;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.model.m_return_add_product;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.CustomAutoCompleteView;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.synergy.keimed_ordergenie.activity.CallPlanDetails.CHEMIST_STOCKIST_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class New_SalesReturn_screen extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess   {

    AutoCompleteTextView autoCompleteTextView;
    String client_id;
    private SharedPreferences pref;
    private TextView add_button;
    private EditText txt_product_name,txt_QTY,txt_bachnumber,txt_invoice_no;
    private int mYear, mMonth, mDay;
    private TextView _txt_customer_name,txt_mfgdate_text;
    private RecyclerView recyclerView;
    private NewSalesReturnAdapter newSalesReturnAdapter;
    private List<m_return_add_product>  returnListt = new ArrayList<m_return_add_product>();
    private String legend_data,stokistName,login_type,sealable,nonSealable,return_Fromdate,return_Todate;
    private ad_AutocompletSalesReturn adpter;
   // private PeopleAdapter adpterr;
    ArrayList<m_return_add_product> posts=new ArrayList<>();
    private RadioButton  radioSaleable,radioNonsaleable;

    private RadioGroup radioGroup;
    private String returnType,ReturnType,Chemist_ID;
    private Button button_request;
    private Date current_date = Calendar.getInstance().getTime();
    private CustomAutoCompleteView _autoCompleteTextView;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    Date date = new Date(); // to get the date
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = df.format(date.getTime());

    private List<m_return_add_product> returnListResponse= new ArrayList<m_return_add_product>();;
    private List<m_return_add_product> returnListRespon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_salesreturns_functionality);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        client_id = pref.getString(CLIENT_ID, "0");
        add_button=(TextView)findViewById(R.id.add_row);
        _txt_customer_name=(TextView)findViewById(R.id.txt_customer);
        radioSaleable=(RadioButton)findViewById(R.id.chk_saleable);
        radioNonsaleable=(RadioButton)findViewById(R.id.chk_nonsaleable);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        button_request=(Button)findViewById(R.id.request_return);
        setTitle("CREATE SALES RETURN");
        _txt_customer_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));
        Chemist_ID=getIntent().getStringExtra("Chemist_Id");
        stokistName = getIntent().getStringExtra("Stokist_Name");
        sealable = getIntent().getStringExtra("Salable_return");
        nonSealable = getIntent().getStringExtra("NonSalable_return");
        return_Fromdate = getIntent().getStringExtra("product_return_from");
        return_Todate = getIntent().getStringExtra("product_return_to");
        Log.d("Chemist_ID",Chemist_ID);
        Log.d("Date",current_date.toString());
        Log.d("Dateee", dateFormat.format(current_date));
        // autoCompleteTextView  = (CustomAutoCompleteView) findViewById(R.id.autoCompleteTextView);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE,"");

        if(login_type.equals(CHEMIST)){

            Log.d("login_type11","CHEMIST LOGIN");
            _txt_customer_name.setText(stokistName);


        }else
        {
            Log.d("login_type12","SALESMAN LOGIN");
            _txt_customer_name.setText(getIntent().getStringExtra(CHEMIST_STOCKIST_NAME));

        }


        button_request.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            Log.d("request","request created");
            post_sales_return_request();
            }

        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                returnType=rb.getText().toString();
                Log.d("SALE",returnType);
                if (returnType.contains("non"))
                {
                 ReturnType="nonmoving";
                    Log.d("SALE11",ReturnType);
                }
                else
                {
                 ReturnType="moving";
                    Log.d("SALE12",ReturnType);
                }
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                get_products_search();
               // Log.d("returnListRes",returnListResponse.toString());

            }
        });

        if(sealable != null) {
            if (sealable.equals("1")) {

                radioSaleable.setVisibility(View.VISIBLE);
                Log.d("printDatastatus11", "Sealable");

            } else {
                radioSaleable.setVisibility(View.GONE);
            }
        }

        if(nonSealable != null) {
            if (nonSealable.equals("1")) {

                radioNonsaleable.setVisibility(View.VISIBLE);
                Log.d("printDatastatus11", "Sealable");

                Log.d("Date_compare11", formattedDate);
                Log.d("Date_compare12", return_Fromdate + " " + return_Todate);

                Date currentDate = null;
                try {
                    currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(formattedDate);

                    Log.d("Date_compare13", String.valueOf(currentDate));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void get_products_search()

    {
//        JSONArray j_arr = new JSONArray();
//        j_arr.put(0,(1245));
//        j_arr.put(1,(1240));
//        Log.d("Array",j_arr.toString());
        MakeWebRequest.MakeWebRequest("get", AppConfig.SALES_RETURN_PRODUCT_LIST,
                    AppConfig.SALES_RETURN_PRODUCT_LIST + "[" + Chemist_ID + ","+client_id +"]", this, true);

//            MakeWebRequest.MakeWebRequest("out_array", AppConfig.SALES_RETURN_PRODUCT_LIST, AppConfig.SALES_RETURN_PRODUCT_LIST,
//                    null, this, false, j_arr.toString());
        //JSONArray jsonArray=new JSONArray();
    }

    private void post_sales_return_request()
    {
        try {
            Random rand = new Random();
            int randomNumber = rand.nextInt(1000) + 1;
            Log.d("random",String.valueOf(randomNumber));
            Log.d("id",returnListt.get(0).getProduct_ID()+"hii");
            Log.d("id12",returnListt.get(0).getMRP()+"hello");
            JSONArray jsonArray=new JSONArray();
            if (returnListt.size()>0)
            {
                for (int i=0;i<returnListt.size();i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Product_ID",1010);
                    jsonObject.put("mrp", 300);
                    jsonObject.put("Chemist_ID", Integer.parseInt(Chemist_ID));
                    jsonObject.put("Stockist_ID", Integer.parseInt(client_id));
                    jsonObject.put("ReturnType", ReturnType);
                    jsonObject.put("CreatedBy", Integer.parseInt(Chemist_ID));
                    jsonObject.put("expDate", "2018-3");
                    jsonObject.put("BatchID", 1500);
                    jsonObject.put("invoiceNo", "1210");
                    jsonObject.put("Createdon", dateFormat.format(current_date));
                    jsonObject.put("ref_number", String.valueOf(randomNumber));
                    jsonObject.put("Qty", 70);
                    jsonArray.put(jsonObject);
                    Log.d("json", jsonObject.toString());
                    Log.d("json", jsonArray.toString());
                }
            }
   //   MakeWebRequest.MakeWebRequest("Post", AppConfig.SALES_RETURNS_REQUEST, AppConfig.SALES_RETURNS_REQUEST, j, this, true);

            MakeWebRequest.MakeWebRequest("Post", AppConfig.SALES_RETURNS_REQUEST, AppConfig.SALES_RETURNS_REQUEST,
                    jsonArray, this, true, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void add_sales_return_dialog(List<m_return_add_product> list)

    {
        final LayoutInflater inflater = LayoutInflater.from(New_SalesReturn_screen.this);
        final View dialogview = inflater.inflate(R.layout.fragment_return_add_product, null);
        final Dialog infoDialog = new Dialog(New_SalesReturn_screen.this);//builder.create();
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(dialogview);
        //txt_deliveryoption.setText(delivery_option);
        txt_product_name=(EditText) dialogview.findViewById(R.id.product_name);
        txt_QTY=(EditText)dialogview.findViewById(R.id.QTY);
        txt_mfgdate_text=(TextView)dialogview.findViewById(R.id.mfgdate_text);
        txt_bachnumber=(EditText)dialogview.findViewById(R.id.bachnumber_text);
        txt_invoice_no=(EditText)dialogview.findViewById(R.id.invoice_text);
        autoCompleteTextView  = (AutoCompleteTextView) dialogview.findViewById(R.id.autoCompleteText);

       dialogview.findViewById(R.id.mfgdate_text).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               show_Date_Picker();
           }
       });
        dialogview.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (txt_product_name.getText().toString().equals("")||txt_QTY.getText().toString().equals(""))
                {
                    //txt_product_name.setError("Please enter product name");
                    Toast.makeText(getApplicationContext(),"Please enter all details",Toast.LENGTH_SHORT).show();
                }
                else {
                    String productName=txt_product_name.getText().toString();
                    String productExpiry=txt_mfgdate_text.getText().toString();
                    String productBatchNo=txt_bachnumber.getText().toString();
                    String productQty=txt_QTY.getText().toString();
                    String product_invoice=txt_invoice_no.getText().toString();
                    Log.d("Sales",productName+"---"+productExpiry+"---"+productBatchNo);
                   // returnListt = new ArrayList<m_return_add_product>();
                    m_return_add_product m_return_add=new  m_return_add_product(productName, productExpiry, productBatchNo, productQty,product_invoice);
                    returnListt.add(m_return_add);
                    Log.d("returnListt", returnListt.toString());
                    if (returnListt.size() > 0)
                    {
                        fill_sales_and_return(returnListt);
                    }

//                else if (txt_QTY.getText().toString().equals(""))
//                {
//                    txt_QTY.setError("Please enter product qty");
//                }
//                else  if (txt_mfgdate_text.getText().toString().equals(""))
//                {
//                    txt_mfgdate_text.setError("Please select date");
//                }
//                else if (txt_bachnumber.getText().toString().equals(""))
//                {
//                    txt_bachnumber.setError("Please enter batch no");
//                }
                 infoDialog.dismiss();
                }
            }
        });

        dialogview.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public                                                                                                                                                                                                                                   void onClick(View view)
            {
                // confirm_dialog(txt_comment.getText().toString());
                infoDialog.dismiss();
            }
        });
        infoDialog.show();

        Log.d("returnListResponse11", String.valueOf(list));
        fill_product_dropdown(list);
    }

    public void fill_product_dropdown(List<m_return_add_product> posts) {

        Log.d("Post",posts.toString());
        // mSearchAutoComplete.setDropDownBackgroundResource(R.drawable.background_white);
        autoCompleteTextView.setDropDownAnchor(R.id.action_search);
       autoCompleteTextView.setThreshold(1);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchItems);
        adpter = new ad_AutocompletSalesReturn(this, posts);
        //adpterr=new PeopleAdapter(this,posts);
        autoCompleteTextView.setAdapter(adpter);
    }
    private void fill_sales_and_return(List<m_return_add_product> returnList) {

        newSalesReturnAdapter = new NewSalesReturnAdapter(returnList,New_SalesReturn_screen.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(newSalesReturnAdapter);
    }

    private void show_Date_Picker() {
        //    Log.d("shw","yaya");
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this,
                new android.app.DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {

                        txt_mfgdate_text.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        try {
            if (response != null)
            {
                Log.d("response11", String.valueOf(response));

                if (f_name.equals(AppConfig.SALES_RETURN_PRODUCT_LIST))
                {
                    legend_data=response.toString();
                    Log.d("inventory",response.toString());
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
                    //returnListRespon=new ArrayList<m_return_add_product>();
            returnListResponse = Arrays.asList(mGson.fromJson(response.toString(), m_return_add_product[].class));
                    Log.d("returnListResponse",returnListResponse.toString());

                    add_sales_return_dialog(returnListResponse);
                    //returnListRespon.addAll(returnListResponse);
                }
            }
        }catch (Exception e)
        {
         Log.d("Message",e.getMessage());
        }

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {


        if (f_name.equals(AppConfig.SALES_RETURNS_REQUEST))
        {
            Log.d("RETURNSS", "RES:"+String.valueOf(response));
            try {
                String message=response.getString("message");
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            GsonBuilder builder = new GsonBuilder();
//            Gson mGson = builder.create();
//            List<m_salesreturn_request> returnListResponse= new ArrayList<m_salesreturn_request>();
//            returnListResponse = Arrays.asList(mGson.fromJson(response.toString(), m_salesreturn_request.class));
        }
    }
}
