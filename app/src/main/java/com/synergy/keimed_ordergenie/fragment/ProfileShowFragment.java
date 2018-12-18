package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.utils.ConstData;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ADRESS;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class ProfileShowFragment extends Fragment {
    private Context context;



    String login_type;
    SharedPreferences pref;

    public ProfileShowFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileShowFragment newInstance() {
        ProfileShowFragment fragment = new ProfileShowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_show, container, false);
        pref = getActivity().getApplicationContext().getSharedPreferences(PREF_NAME,  getActivity().MODE_PRIVATE);
        login_type=pref.getString(ConstData.user_info.CLIENT_ROLE,"");

        if (login_type.equals(CHEMIST)) {

            /*view.findViewById(R.id.img_sixth).setVisibility(View.VISIBLE);*/
           /* view.findViewById(R.id.txt_cus_outstanding_heading).setVisibility(View.VISIBLE);
            view.findViewById(R.id.txt_cus_outstanding).setVisibility(View.VISIBLE);*/

        } else {

          /*  view.findViewById(R.id.img_sixth).setVisibility(View.GONE);*/
         /*   view.findViewById(R.id.txt_cus_outstanding_heading).setVisibility(View.GONE);
            view.findViewById(R.id.txt_cus_outstanding).setVisibility(View.GONE);*/



        }

        TextView txt_name =(TextView) view.findViewById(R.id.txt_name);
        TextView txt_cus_code =(TextView) view.findViewById(R.id.txt_cus_code);
        TextView txt_cus_address =(TextView) view.findViewById(R.id.txt_cus_address);
        TextView txt_cus_email =(TextView) view.findViewById(R.id.txt_cus_email);
        TextView txt_cus_phone =(TextView) view.findViewById(R.id.txt_cus_phone);


       // txt_name.setText(pref.getString(ConstData.user_info.CLIENT_NAME,""));

        txt_name.setText(pref.getString(ConstData.user_info.CLIENT_FULL_NAME,""));
        txt_cus_code.setText(pref.getString(ConstData.user_info.CLIENT_ID,""));
        txt_cus_address.setText(pref.getString(CLIENT_ADRESS, ""));
        txt_cus_email.setText(pref.getString(ConstData.user_info.EMAIL_ID,""));
        txt_cus_phone.setText(pref.getString(ConstData.user_info.CLIENT_MOBILE,""));


        ButterKnife.bind(this, view);

        return view;
    }

}
