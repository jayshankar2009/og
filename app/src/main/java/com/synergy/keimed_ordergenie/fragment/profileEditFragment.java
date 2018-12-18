package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.synergy.keimed_ordergenie.utils.ConstData.decrypt;
import static com.synergy.keimed_ordergenie.utils.ConstData.encrypt;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ADRESS;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_FULL_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_MOBILE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_USERNAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.EMAIL_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class profileEditFragment extends Fragment implements MakeWebRequest.OnResponseSuccess {

    SharedPreferences pref;

    EditText input_name;
    EditText input_email_id;
    EditText input_shop_name;
    EditText input_adress;
    EditText input_phone;
    EditText input_city;
    EditText input_state;


    public static profileEditFragment newInstance() {

        profileEditFragment fragment = new profileEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getActivity().getSharedPreferences(PREF_NAME, getActivity().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        ButterKnife.bind(this, view);

        input_name = (EditText) view.findViewById(R.id.input_name);
        input_email_id = (EditText) view.findViewById(R.id.input_email_id);
        input_shop_name = (EditText) view.findViewById(R.id.input_shop_name);
        input_adress = (EditText) view.findViewById(R.id.input_adress);
        input_phone = (EditText) view.findViewById(R.id.input_phone);
        input_city = (EditText) view.findViewById(R.id.input_city);
        input_state = (EditText) view.findViewById(R.id.input_state);




        input_name.setText(pref.getString(CLIENT_FULL_NAME, ""));
        input_email_id.setText(pref.getString(EMAIL_ID, ""));
        input_shop_name.setText(pref.getString(CLIENT_FULL_NAME, ""));
        input_adress.setText(pref.getString(CLIENT_ADRESS, ""));
        input_phone.setText(pref.getString(CLIENT_MOBILE, ""));
        //    input_city.setText(pref.getString(CLIENT_CITY_ID,""));
        //  input_state.setText(pref.getString(CLIENT_MOBILE,""));




        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public  void save_profile(String Image_name) {
        try {

            Map<String, String> j_obj=new HashMap<>();

            j_obj.put("_id",pref.getString(USER_ID, ""));
            j_obj.put("Login_Name", pref.getString(CLIENT_NAME, ""));
            j_obj.put("Full_Name", input_name.getText().toString());
            j_obj.put("Mobile_No",input_phone.getText().toString());
            j_obj.put("email",input_email_id.getText().toString());
            j_obj.put("ClientID",pref.getString(CLIENT_ID, ""));
            j_obj.put("Client_LegalName", input_name.getText().toString());
            j_obj.put("Role_ID",pref.getString(CLIENT_ROLE_ID, ""));
            j_obj.put("Role_Name",pref.getString(CLIENT_ROLE, ""));
            j_obj.put("Latitude", "28.631421");
            j_obj.put("Longitude", "77.220517");

           // j_obj.put("Location", Image_name);
            j_obj.put("Client_Mobile", input_phone.getText().toString());

            j_obj.put("ProfileImage", Image_name);
            j_obj.put("UserTasks", "");
            j_obj.put("Active", "true");
            j_obj.put("Createdon", "2017-01-27T10:39:14.391Z");


        //    Log.e("Editprofile", String.valueOf(j_obj));
            // http://www.ordergenie.co.in/api/UserMasters/[UserID,ClientID,UserName]
            String userEncrypted = pref.getString(CLIENT_USERNAME, encrypt(""));
            String user_name = decrypt(userEncrypted);

            MakeWebRequest.put_request(getActivity(),AppConfig.EDIT_PROFILE+"["+pref.getString(USER_ID, "")+","+
                            pref.getString(CLIENT_ID, "")+","+ user_name+"]"
                    ,j_obj);

        } catch (Exception E) {
            E.toString();
        }
    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
  //    Log.e("profiles",response.toString());
    }
}
