package com.synergy.keimed_ordergenie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.synergy.keimed_ordergenie.R;

/**
 * Created by prakash on 06/07/16.
 */
public class DashboardsalesmanStaticsFragment extends Fragment {
	private ProgressBar pbr_total_target,pbr_total_achieved;

	public DashboardsalesmanStaticsFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v_view =inflater.inflate(R.layout.fragment_sales_statics_menu, container, false);

		pbr_total_target=(ProgressBar)v_view.findViewById(R.id.pbr_total_target);
		pbr_total_achieved=(ProgressBar)v_view.findViewById(R.id.pbr_total_achieved);

		pbr_total_target.setProgress(100);
		pbr_total_achieved.setProgress(70);

		return v_view;
	}



}

