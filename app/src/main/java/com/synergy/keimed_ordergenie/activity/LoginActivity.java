package com.synergy.keimed_ordergenie.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.app.Config;
import com.synergy.keimed_ordergenie.utils.CompatibilityUtil;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.NotificationUtils;
import com.synergy.keimed_ordergenie.utils.OGtoast;
import com.synergy.keimed_ordergenie.utils.SessionManager;
import com.synergy.keimed_ordergenie.utils.get_imie_number;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.STOCKIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.encrypt;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ADRESS;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_CITY_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_FULL_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_LATITUDE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_LOGIN_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_LONGITUDE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_MOBILE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_PASSWORD;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_PROFILE_IMAGE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_USERNAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.EMAIL_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_ORDER_DELIVERY_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_PAYMENT_COLLECTION_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_TAKE_ORDER_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.LEGEND_MODE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

//import com.synergy.ordergenie.utils.RandomUtil;

public class LoginActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    String regId;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = "LoginActivity";
    private String login_type = STOCKIST;
    private static final int REQUEST_SIGNUP = 0;
    public static final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 125;
    private SessionManager session;

    private String IMEI = "", isDummypass = "";
    //String forgotpass_activity = "forgotpassword", pageid = "qwerty";
    //String changepass_activity = "changepassword";
    // String register_activity = "register";
    AppController globalVariable;
    @BindView(R.id.input_email)
    EditText _emailText;

    @BindView(R.id.input_password)
    EditText _passwordText;

    @BindView(R.id.btn_login)
    Button _loginButton;

    @BindView(R.id.link_signup)
    Button _signupLink;

    @BindView(R.id.input_iemi)
    EditText _iemi;

    @BindView(R.id.txt_forgotpass)
    TextView _forgotpass;

    @OnClick({R.id.txt_forgotpass})
    void onforgotpassclick() {

        forgotPassword();
    }

    @OnClick({R.id.link_signup})
    void onregisterclick() {
        showregistrationscreen();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        session = new SessionManager(getApplicationContext());
        globalVariable = (AppController) getApplicationContext();
        IMEI = new get_imie_number().check_imie_permission(this);
        _iemi.setText(IMEI);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
//        Intent intent = getIntent();
//        pageid = intent.getStringExtra("pageid");
        //   Log.d("MID" , Build.SERIAL);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

		/*_signupLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start the Signup activity
				Intent intent = new Intent(getApplicationContext(), SignupActivity.class);

				startActivityForResult(intent, REQUEST_SIGNUP);
			}
		});*/


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    // Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    // txtMessage.setText(message);
                }
            }


        };

        displayFirebaseRegId();

    }

    private void displayFirebaseRegId() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
        if (regId != null) {
            Log.d("Firebase_NotificationId", regId);
        } else {

            regId = "NA";
        }
        //Toast.makeText(getApplicationContext(),"Firebase Token :- " + regId,Toast.LENGTH_LONG).show();
    }

    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed();
            return;
        }



	/*	final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Authenticating...");
		progressDialog.show();
*/
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        check_login(email, password);
        // TODO: Implement your own authentication logic here.

	/*	new android.os.Handler().postDelayed(
                new Runnable() {
					public void run() {
						// On complete call either onLoginSuccess or onLoginFailed

						// onLoginFailed();
						progressDialog.dismiss();
					}
				}, 3000);*/
    }


    /* onActivity Result */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }


    /* onBack Pressed */
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String Token) {
        globalVariable.setToken(Token);
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("key", Token);
        editor.commit();
        //MakeWebRequest.MakeWebRequest("get","",AppConfig.GET_UNMAPPED_CHEMIST+"11",this);
        /*	Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("imei", IMEI);
		JSONArray j_arraY=new JSONArray();
		j_arraY.put(jsonParams);*/
        String s = "{\"imei\":" + IMEI + "}";
        //String s = "{\"imei\":" + "352342070323613" + "}";
        //globalVariable.setToken(null);
        MakeWebRequest.MakeWebRequest("out_array", "GetMe", AppConfig.GET_ME,
                null, this, true, s);

        //	MakeWebRequest.MakeWebRequest("Post","GetMe",AppConfig.GET_ME,jsonParams,this,true);
        //MakeWebRequest.MakeWebRequest("get","GetMe",AppConfig.GET_ME,this,true);
    }

    public void onLoginFailed() {
        OGtoast.OGtoast("Login failed", getBaseContext());
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("enter a valid email user name");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            //***     if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("password strength should be greater than 4");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }

    private void showmainscreen(JSONArray response) {

        Log.d("print_login", String.valueOf(response));


        //
//        if (pageid.equals(forgotpass_activity)){
//            Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
//            startActivity(intent);
//        }
//
////
////
//else{

        try {
            if (response.getJSONObject(0).getString("isDummyPwd") != null) {
                isDummypass = response.getJSONObject(0).getString("isDummyPwd");
                //      Log.d("isDummypass",isDummypass);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isDummypass.equals("1")) {
            Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        } else {
            //ShownotificationFcm
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            try {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(CLIENT_ID, response.getJSONObject(0).getString("ClientID"));
                editor.putString(USER_ID, response.getJSONObject(0).getString("_id"));
                editor.putString(CLIENT_NAME, response.getJSONObject(0).getString("Client_LegalName"));
                editor.putString(CLIENT_FULL_NAME, response.getJSONObject(0).getString("Full_Name"));
                editor.putString(EMAIL_ID, response.getJSONObject(0).getString("email"));
                editor.putString(CLIENT_ROLE, response.getJSONObject(0).getString("role"));
                editor.putString(LEGEND_MODE, response.getJSONObject(0).getString("legend_mode"));
                editor.putString(CLIENT_MOBILE, response.getJSONObject(0).getString("Mobile_No"));
                editor.putString(CLIENT_LATITUDE, response.getJSONObject(0).getString("Latitude"));
               Log.i("Client Image","---"+response.getJSONObject(0).getString("ProfileImage"));
                editor.putString(CLIENT_LONGITUDE, response.getJSONObject(0).getString("Longitude"));
                editor.putString(CLIENT_PROFILE_IMAGE, response.getJSONObject(0).getString("ProfileImage"));
                editor.putString(CLIENT_CITY_ID, response.getJSONObject(0).getString("CityID"));
                editor.putString(CLIENT_ADRESS, response.getJSONObject(0).getString("Location"));
                editor.putString(CLIENT_ROLE_ID, response.getJSONObject(0).getString("Role_ID"));
                editor.putString(CLIENT_LOGIN_NAME, response.getJSONObject(0).getString("Login_Name"));

                //editor.putString(CLIENT_IS_DUMMY_PASS, response.getJSONObject(0).getString("isDummyPwd"));
                if (response.getJSONObject(0).getString("UserTasks").contains("1")) {
                    editor.putBoolean(IS_TAKE_ORDER_ASSIGNED, true);
                    //editor.commit();
                }
                if (response.getJSONObject(0).getString("UserTasks").contains("2")) {
                    editor.putBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, true);
                    //editor.commit();
                }
                if (response.getJSONObject(0).getString("UserTasks").contains("3")) {
                    editor.putBoolean(IS_ORDER_DELIVERY_ASSIGNED, true);
                    //editor.commit();
                }

                editor.commit();

                session.setLogin(true);
                startActivity(intent);
                  finish();

            } catch (Exception e) {

            }
        }


    }

    //  }

    private void showregistrationscreen() {
        Intent intent = new Intent(LoginActivity.this, Register.class);
        intent.putExtra("login_type", login_type);
        startActivity(intent);
    }

    private void forgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(intent);

    }

    private void check_login(String email, String password) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CLIENT_USERNAME, encrypt(email));
        editor.putString(CLIENT_PASSWORD, encrypt(password));
        editor.apply();
        editor.commit();
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("firebaseID", regId);
            jsonParams.put("email", email);
            jsonParams.put("password", password);

            MakeWebRequest.MakeWebRequest("Post", "Login", AppConfig.checklogin, jsonParams, this, true);
        } catch (Exception e) {

        }
    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        try {
            if (response != null) {
                if (response.getString("token") != null) {
                    onLoginSuccess(response.getString("token"));
                } else if (response.getString("message") != null) {
                    OGtoast.OGtoast(response.getString("message"), getBaseContext());
                }
            } else {
                if (isNetworkConnectionAvailable()) {
                    OGtoast.OGtoast("Not a valid user.Please check your Username Or Password.", getBaseContext());
                } else {
                    OGtoast.OGtoast("Please check your Internet connection", getBaseContext());
                }
            }
        } catch (Exception e) {

        }
    }


    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            // checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        //Log.d("Login", f_name);
        if (response != null) {
            Log.d("Login", response.toString());
            if (f_name.equals("GetMe")) {
                showmainscreen(response);
            } else {
                OGtoast.OGtoast("Unable to contact the server", getBaseContext());
            }
        }
    }


  /*  *//* Get IMEI Number *//*
    private String getMyPhoneNO() {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) getSystemService
                (TELEPHONY_SERVICE);

        String yourNumber = mTelephonyMgr.getLine1Number();
        if (yourNumber.equals("")) {
            yourNumber = mTelephonyMgr.getSubscriberId();
        }

        return yourNumber;
    }*/


    /* Runtime Permissions */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    IMEI = CompatibilityUtil.get_iemi(LoginActivity.this);
                    _iemi.setText(IMEI);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new get_imie_number().check_imie_permission(this);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
