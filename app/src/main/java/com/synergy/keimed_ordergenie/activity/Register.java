package com.synergy.keimed_ordergenie.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.CustomAutoCompleteView;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;

import static com.synergy.keimed_ordergenie.app.AppConfig.POST_SIGN_UP;

public class Register extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {


    @BindView(R.id.input_name)
    EditText input_name;
    @BindView(R.id.input_email)
    EditText input_email;
    @BindView(R.id.input_password)
    EditText input_password;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    //TextView _loginLink;
    Button _loginLink;

    @BindView(R.id.input_druglicence)
    EditText input_druglicence;
    @BindView(R.id.input_Mobile_number)
    EditText input_Mobile_number;
    @BindView(R.id.input_user_name)
    EditText input_user_name;

    @BindView(R.id.input_confirm_password)
    EditText input_confirm_password;
    @BindView(R.id.input_tin_cst_vat_number)
    EditText input_tin_cst_vat_number;

    @BindView(R.id.btn_upload)
    ImageView btn_upload;
    @BindView(R.id.chk_accept_terms)
    CheckBox chk_accept_terms;



    @BindView(R.id.input_address)
    EditText input_address;
    @BindView(R.id.input_fax)
    EditText input_fax;
    @BindView(R.id.input_contactname)
    EditText input_contactname;



    @BindView(R.id.input_state)
    TextView input_state;
    @BindView(R.id.sel_image)
    ImageView sel_image;


    @BindView(R.id.input_city)
    TextView input_city;


    @BindView(R.id.input_pincode)
    CustomAutoCompleteView input_pincode;
    private String State_id,City_id,Client_Code="";
    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    ArrayList<String> Pincode_list = new ArrayList<String>();
   // String register_token = "register";
    ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent i = new Intent(Register.this,LoginActivity.class);
               // i.putExtra("pageid",register_token);
                startActivity(i);
                finish();
            }
        });

        input_state.setEnabled(false);
        input_city.setEnabled(false);
        input_pincode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (input_pincode.getText().toString().length() >= 2) {
                    pincode_search(input_pincode.getText().toString());

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //input_pincode.seto

        input_pincode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                input_state.setEnabled(true);
                input_city.setEnabled(true);
                f_get_city_state_on_pincode(input_pincode.getAdapter().getItem(i).toString());
            }


        });


        input_state.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        input_city.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {




            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();





    }

    @OnClick(R.id.btn_upload)
    void onbBTNUploadClick()
    {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    @OnClick(R.id.btn_capture)
    void onbBTNCaptureClick()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void signup() {


        if (!validate()) {
            return;
        }

        onValidationSuccess();


        // TODO: Implement your own signup logic here.


    }


    public void onValidationSuccess() {
        JSONArray j_array=new JSONArray();
       JSONObject jsonParams_first = new JSONObject();
        JSONObject jsonParams_second = new JSONObject();

        try {

            int randomPIN = (int)(Math.random()*9000)+1000;
            //Store integer in a string
            //randomPIN.toString(PINString);
            if(!input_name.getText().toString().isEmpty()) {
                String name=input_name.getText().toString();
                Client_Code=name.substring(0,2)+""+String.valueOf(randomPIN);
                Client_Code=Client_Code.toUpperCase();
            }

            String curr_date=dateFormat.format(Calendar.getInstance().getTime());
            jsonParams_first.put("Active", "true");
            jsonParams_first.put("CityID", City_id);
            jsonParams_first.put("CityName", input_city.getText().toString());
            jsonParams_first.put("ClientTypeID", "3");
            jsonParams_first.put("Client_Address", input_address.getText().toString());
            jsonParams_first.put("Client_Code",Client_Code);
            jsonParams_first.put("Client_Contact", input_contactname.getText().toString());
            jsonParams_first.put("Client_LegalName", input_name.getText().toString());
            jsonParams_first.put("Createdon",curr_date );
            jsonParams_first.put("Druglicence", input_druglicence.getText().toString());
            jsonParams_first.put("Fax", input_fax.getText().toString());
            jsonParams_first.put("MobileNo", input_Mobile_number.getText().toString());
            jsonParams_first.put("PinCode", input_pincode.getText().toString());
            jsonParams_first.put("StateID", State_id);
            jsonParams_first.put("StateName", input_state.getText().toString());
            jsonParams_first.put("tin_cst_vat", input_tin_cst_vat_number.getText().toString());
            jsonParams_first.put("Client_Name", input_name.getText().toString());
            jsonParams_first.put("Client_Email", input_email.getText().toString());

            j_array.put(jsonParams_first);

            jsonParams_second.put("Role_ID", "2");
            jsonParams_second.put("UserName", input_user_name.getText().toString());
            jsonParams_second.put("password", input_password.getText().toString());
            jsonParams_second.put("Login_Name", input_name.getText().toString());
            jsonParams_second.put("Full_Name", input_name.getText().toString());
            jsonParams_second.put("email",  input_email.getText().toString());
            jsonParams_second.put("Mobile_No", input_Mobile_number.getText().toString());
            jsonParams_second.put("Active", "true");
            jsonParams_second.put("Createdon",curr_date);
            jsonParams_second.put("ProfileImage", "");
            jsonParams_second.put("role", "chemist");
            jsonParams_second.put("isDummyPwd", "false");

            j_array.put(jsonParams_second);
       //     Log.d("print_registerdata", String.valueOf(j_array));

        }catch (Exception e)
        {
           e.printStackTrace();
        }
       // MakeWebRequest.MakeWebRequest("Post","SignUp",AppConfig.POST_SIGN_UP,jsonParams,this);
        MakeWebRequest.MakeWebRequest("Post", POST_SIGN_UP, POST_SIGN_UP,
               j_array, this,true,"");
        //  finish();
    }

    public void onSignupFailed() {


    }

    public boolean validate() {
        boolean valid = true;




        if (input_name.getText().toString().isEmpty()) {
            input_name.setError("Please enter a name");
            input_name.requestFocus();
            valid = false;
        } else {
            input_name.setError(null);
        }

        if (  input_email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(  input_email.getText().toString()).matches()) {
            input_email.setError("enter a valid email address");
            input_email.requestFocus();
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (input_Mobile_number.getText().toString().isEmpty()) {
            input_Mobile_number.setError("Please enter a mobile number");
            input_Mobile_number.requestFocus();
            valid = false;
        } else {
            input_Mobile_number.setError(null);
        }

        if (input_pincode.getText().toString().isEmpty()||input_pincode.getText().toString().length()<6) {
            input_pincode.setError("Please enter a valid pincode");
            input_pincode.requestFocus();
            valid = false;
        } else {
            input_pincode.setError(null);
        }

        if (input_druglicence.getText().toString().isEmpty()) {
            input_druglicence.setError("Please enter drug licence number");
            input_druglicence.requestFocus();
            valid = false;
        } else {
            input_druglicence.setError(null);
        }

        if (input_user_name.getText().toString().isEmpty()) {
            input_user_name.setError("Please enter a username");
            input_user_name.requestFocus();
            valid = false;
        } else {
            input_user_name.setError(null);
        }


        if (input_confirm_password.getText().toString().isEmpty()|| input_confirm_password.length()<4 ||!input_confirm_password.getText().toString().equals(input_password.getText().toString())) {
            input_confirm_password.setError("Confirm password is not matching");
            input_confirm_password.requestFocus();
            valid = false;
        } else {
            input_confirm_password.setError(null);
        }




        if (input_password.getText().toString().isEmpty()) {
            input_password.setError("please enter a password");
            input_password.requestFocus();
            valid = false;
        } else {
            input_password.setError(null);
        }

        if (!chk_accept_terms.isChecked()) {
            chk_accept_terms.setError("please accept the Terms and Conditions");
            show_snack_bar("please accept the Terms and Conditions");
            valid = false;
        } else {
            chk_accept_terms.setError(null);
        }

     /*   if(!input_tin_cst_vat_number.getText().toString().isEmpty())
        {
            if (input_tin_cst_vat_number.getText().length()<15) {
                input_tin_cst_vat_number.setError("please enter valid GST no");
                valid = false;
            }
        }*/
        if(input_state.getText().toString().isEmpty())
        {
            input_state.setError("please select ");
                valid = false;
        }

        if(input_city.getText().toString().isEmpty())
        {
            input_city.setError("please enter a state");
            valid = false;
        }

        return valid;
    }


    private boolean isValidVATNO(String vat){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(vat).matches();
    }

    void pincode_search(String pincode) {

        MakeWebRequest.MakeWebRequest("get", "pincode_search", AppConfig.GET_PINCODE_SEARCH + pincode, this, false);
    }
    void State_search(String pincode) {
        MakeWebRequest.MakeWebRequest("get", "pincode_search", AppConfig.GET_PINCODE_SEARCH + pincode, this, false);
    }
    void City_search(String pincode) {
        MakeWebRequest.MakeWebRequest("get", "pincode_search", AppConfig.GET_PINCODE_SEARCH + pincode, this, false);
    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        if (response != null) {

            if (f_name.equals(POST_SIGN_UP)) {

                try {
                    String key = response.names().getString(0);
              //      Log.e("key", key.toString());
                    if (key.equals("token")) {
                        Intent i = new Intent(Register.this, LoginActivity.class);
                        startActivity(i);
                        OGtoast.OGtoast("Please login with your new credentials", getBaseContext());
                        finish();

                    } else {
                        String error = response.getString("error_msg");//       Log.e("res", "Exists");
                        //   OGtoast.OGtoast(error, getBaseContext());
                        showpopup(error);
                        // input_user_name.setError("Enter new username");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        else
        {
            if (isNetworkConnectionAvailable()) {
                //OGtoast.OGtoast("Not a valid user.Please check your Username Or Password.", getBaseContext());
            }
            else
            {
                OGtoast.OGtoast("Please check your Internet connection", getBaseContext());
            }
        }
    }

    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
        //    Log.d("Network", "Connected");
            return true;
        }
        else{
            // checkNetworkConnection();
       //     Log.d("Network","Not Connected");
            return false;
        }
    }

    public  void showpopup(String Error)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
        //Setting message manually and performing action on button click
        builder.setMessage(Error)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error Message");
        alert.show();
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        try {
            if (f_name.equals("pincode_search")) {

                if (response!=null) {

                //    Log.e("picode",response.toString());
                    Pincode_list.clear();

                    for (int i = 0; i < response.length(); i++) {
                        Pincode_list.add(response.getJSONObject(i).getString("PinCode"));
                    }
                    show_list_pincode();
                }
                else
                {
                    if (isNetworkConnectionAvailable())
                    {
                        OGtoast.OGtoast("Pincode is not available in list", getBaseContext());
                    }
                    else
                    {
                        OGtoast.OGtoast("Please check your internet connection", getBaseContext());
                    }

                }
            }

            if (f_name.equals("CityStatePincode")) {
                input_state.setText((response.getJSONObject(0).getString("StateName")));
                input_state.setEnabled(false);
                input_city.setText((response.getJSONObject(0).getString("CityName")));
                input_city.setEnabled(false);
                State_id=response.getJSONObject(0).getString("StateID");
                City_id=response.getJSONObject(0).getString("CityID");
            }


            if (f_name.equals("SignUp")) {

               finish();

            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    void show_list_pincode() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Register.this,
                R.layout.rxspinner_simple_text_layout, Pincode_list);

        input_pincode.setAdapter(dataAdapter);
    }

    void f_get_city_state_on_pincode(String pincode) {
        MakeWebRequest.MakeWebRequest("get", "CityStatePincode", AppConfig.GET_CITY_STATE_ON_PINCODE + pincode, this, true);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                sel_image.setImageBitmap(bitmap);
                sel_image.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {




                try {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    sel_image.setImageBitmap(image);
                    sel_image.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void show_snack_bar(String mesaage)
    {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.lnr_main), mesaage, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Register.this, LoginActivity.class);
        startActivity(intent);
    }

}
