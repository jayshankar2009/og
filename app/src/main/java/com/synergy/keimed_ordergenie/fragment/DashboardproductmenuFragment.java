package com.synergy.keimed_ordergenie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synergy.keimed_ordergenie.R;

public class DashboardproductmenuFragment extends Fragment {

    public DashboardproductmenuFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v_view = inflater.inflate(R.layout.fragment_productsmenu, container, false);
        return v_view;
    }

}