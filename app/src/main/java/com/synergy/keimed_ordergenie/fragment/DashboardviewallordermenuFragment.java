package com.synergy.keimed_ordergenie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synergy.keimed_ordergenie.R;

/**
 * Created by prakash on 07/07/16.
 */
public class DashboardviewallordermenuFragment extends Fragment {

	public DashboardviewallordermenuFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v_view = inflater.inflate(R.layout.fragment_updated_showallorder, container, false);
		return v_view;
	}

}