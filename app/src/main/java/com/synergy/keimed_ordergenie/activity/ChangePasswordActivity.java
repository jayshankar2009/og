package com.synergy.keimed_ordergenie.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by AS Infotech on 28-10-2017.
 */

public class ChangePasswordActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {
    EditText oldpass, newpass, confirmpass;
    String _oldpass, _newpass, _confirmpass;
    Boolean dummypass = false;
    Button Save;
    AppController globalVariable;
    private static final String TAG = "ChangePasswordActivity";
    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
        }



        oldpass = (EditText) findViewById(R.id.oldpassword);
        confirmpass = (EditText) findViewById(R.id.confirm_password);
        newpass = (EditText) findViewById(R.id.newpassword);
        Save = (Button) findViewById(R.id.btSubmit);
        globalVariable = (AppController) getApplicationContext();
        setSupportActionBar(_toolbar);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _oldpass = oldpass.getText().toString().trim();
                _newpass = newpass.getText().toString().trim();
                _confirmpass = confirmpass.getText().toString().trim();

                // changepasswordprocess();
                if (validate()) {
                    check_passwordchange(_oldpass, _newpass, _confirmpass);
                }


            }
        });



    }


    //   check_passwordchange(_oldpass, _newpass,_confirmpass);
    // TODO: Implement your own authentication logic here.

    public void onLoginFailed() {
        OGtoast.OGtoast("Please try again", getBaseContext());

        Save.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        _oldpass = oldpass.getText().toString();
        _confirmpass = confirmpass.getText().toString();
        _newpass = newpass.getText().toString();

        if (_oldpass.isEmpty()) {
            oldpass.setError("enter a valid password");
            valid = false;
        } else {
            oldpass.setError(null);
        }

        if (_newpass.isEmpty() || _newpass.length() < 4 ) {
            //***     if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            newpass.setError("password strength should be greater than 4");
            valid = false;
        } else {
            newpass.setError(null);
        }
        if (_confirmpass.isEmpty() || _confirmpass.length() < 4) {
            //***     if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            confirmpass.setError("password strength should be greater than 4");
            valid = false;
        } else {
            confirmpass.setError(null);
        }
        if (_confirmpass.equals(_newpass)) {
            //***     if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            confirmpass.setError(null);

            //  valid = true;
        } else {
            confirmpass.setError("Please enter valid password");
            valid = false;
        }

        return valid;
    }

    private void check_passwordchange(String _oldpass, String _newpass, String _confirmpass) {



        if (_confirmpass.equals(_newpass)&&_confirmpass.length()>4) {

            try {

                // JSONObject jsonParams = new JSONObject();
               /* Map<String, String> j_obj = new HashMap<>();

                j_obj.put("oldPassword", _oldpass);
                j_obj.put("newPassword", _newpass);
                j_obj.put("confPassword", _confirmpass);
                j_obj.put("isDummyPwd", dummypass.toString());*/

                JSONObject jsonParams = new JSONObject();
                jsonParams.put("oldPassword", _oldpass);
                jsonParams.put("newPassword", _newpass);
                jsonParams.put("confPassword", _confirmpass);
                jsonParams.put("isDummyPwd", dummypass.toString());

                // String url = "http://192.168.31.151:9000/api/UserMasters/2/password/";

                // Log.d("ChangePassword11", url);
                //  MakeWebRequest.put_request(this, AppConfig.checkpasswordchange, j_obj);
                // MakeWebRequest.put_request(this, AppConfig.checkpasswordchange, j_obj); // 25 SEP
                //  MakeWebRequest.MakeWebRequest("put", "Login", AppConfig.checkpasswordchange, jsonParams, this, true);

               // MakeWebRequest.put_request1(this, AppConfig.checkpasswordchange, jsonParams); // use 25
                MakeWebRequest.put_request1(this, AppConfig.newcheckpasswordchange, jsonParams); // use 25
            } catch (Exception e) {

                //   Log.d("ChangePassword111", e.getMessage());

            }

        }else{
            Toast.makeText(getApplicationContext(),"Please submit valid password of strength greater than 4",Toast.LENGTH_SHORT).show();


        }
    }

    @Override

    public void onSuccess_json_object(String f_name, JSONObject response) {

        try {
            if (response != null) {

                //  Log.d("ChangePassword12ob", response.toString());
                //Log.d("print_pass_status11",response.toString());
                Toast.makeText(getApplicationContext(),"Password changed successfully.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }else if(response == null||response.equals("")) {

                //Log.d("print_pass_status11","dunno");
                if (isNetworkConnectionAvailable()) {
                    oldpass.setText("");
                    newpass.setText("");
                    confirmpass.setText("");
                    Toast.makeText(getApplicationContext(),"Failed to change password",Toast.LENGTH_SHORT).show();

                    //  OGtoast.OGtoast("Failed to change password", getBaseContext());
                }
                else
                {
                    oldpass.setText("");
                    newpass.setText("");
                    confirmpass.setText("");
                    Toast.makeText(getApplicationContext(),"Please check your Internet connection",Toast.LENGTH_SHORT).show();
                    // OGtoast.OGtoast("Please check your Internet connection", getBaseContext());
                }

            }else if(response == null){

                Toast.makeText(getApplicationContext(),"Password changed successfully.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);

            }
            else if(response.equals("")){

                Toast.makeText(getApplicationContext(),"Password changed successfully.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);

            }
            else{

                oldpass.setText("");
                newpass.setText("");
                confirmpass.setText("");
                Toast.makeText(getApplicationContext(),"Old password is not valid. Please try again, later.",Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {

            // Log.d("ChangePassword13", e.getMessage());
            oldpass.setText("");
            newpass.setText("");
            confirmpass.setText("");
        }
    }


    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            //Log.d("Network", "Connected");
            return true;
        }
        else{
            // checkNetworkConnection();
            //Log.d("Network","Not Connected");
            return false;
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        try {
            if (response == null) {
                //  Log.d("ChangePassword13Ar", response.toString());
                //  Log.d("print_pass_status","null");
                oldpass.setText("");
                newpass.setText("");
                confirmpass.setText("");
                Toast.makeText(getApplicationContext(),"Old password is not valid. Please try again, later.",Toast.LENGTH_LONG).show();


            }

        } catch (Exception e) {
            oldpass.setText("");
            newpass.setText("");
            confirmpass.setText("");
            Toast.makeText(getApplicationContext(),"Old password is not valid. Please try again, later.",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if (globalVariable.getFromHomeActivity())
        {
            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            startActivity(intent);
            globalVariable.setFromHomeActivity(false);
        }
        else {
            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}
