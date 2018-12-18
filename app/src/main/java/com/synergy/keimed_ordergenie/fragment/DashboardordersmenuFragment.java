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
public class DashboardordersmenuFragment extends Fragment {

	public DashboardordersmenuFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v_view = inflater.inflate(R.layout.fragment_ordersmenu, container, false);
		return v_view;
	}

}
