package com.synergy.keimed_ordergenie.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.OGtoast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {
  AppCompatButton btn_submit,btn_login;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btn_submit=(AppCompatButton)findViewById(R.id.btSubmit);
        btn_login=(AppCompatButton)findViewById(R.id.btLogin);
        input=(EditText) findViewById(R.id.input_id);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    forgotPassword(input.getText().toString());
                }

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }


    public boolean validate() {
        boolean valid = true;
        String id = input.getText().toString();
        if (id.isEmpty()) {
            input.setError("enter a valid user id");
            valid = false;
        } else {
            input.setError(null);
        }
        return valid;
    }
    public  void  forgotPassword(String userid)
    {
        MakeWebRequest.MakeJsonObjectRequest("get", "forgotPassword", AppConfig.FORGOT_PASSWORD + userid, this, true);
    }


    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        //Log.d("f_name",f_name);

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

        if (response!=null) {
          //  Log.d("f_name11", f_name);
            //Log.d("response", response.toString());
            if (f_name.equals("forgotPassword")) {
                try {
                    String result=response.getString("success");
                //    Log.d("result", result);
                    if (result.equals("New Password"))
                    {
                        OGtoast.OGtoast("Success", getBaseContext());
                        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                        intent.putExtra("pageid","forgotpassword");
                        startActivity(intent);
                    }
                    else
                    {
                        OGtoast.OGtoast("Invalid  Username", getBaseContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
        startActivity(intent);
    }
}
