package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.CustomerlistActivity;
import com.synergy.keimed_ordergenie.activity.CustomerlistActivityNew;
import com.synergy.keimed_ordergenie.databinding.FragmentCustomermenuBinding;
import com.synergy.keimed_ordergenie.model.m_menuitems;

/**
 * Created by prakash on 06/07/16.
 */
public class DashboardcustomermenuFragment extends Fragment {

	ImageButton btn;

	private static Context mContext;
	private m_menuitems om_menuitems;
	private FragmentCustomermenuBinding mBinding;

	public DashboardcustomermenuFragment() {
	}

	public static DashboardcustomermenuFragment newInstance(Context mContext) {

		mContext = mContext;
		DashboardcustomermenuFragment fragment = new DashboardcustomermenuFragment();
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		om_menuitems = new m_menuitems();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_customermenu, container, false);

		View o_view = mBinding.getRoot();

		mBinding.ibCustomer.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//create_customerlist();
					}
				}
		);
		return o_view;
	}


	private void create_customerlist() {

		Fragment fragment = null;
		fragment = CustomerlistFragment.newInstance(this.getContext());
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_centerscreen, fragment, "right").commit();
		//fragmentTransaction.add(R.id.getid(), fragment, "right").commit();
		fragmentTransaction.commit();

//		final CustomerlistFragment detailsFragment =
//				CustomerlistFragment.newInstance(mContext);
//
//		getSupportFragmentManager()
//				.beginTransaction()
//				.replace(R.id.main_centerscreen, detailsFragment, "rageComicDetails")
//				.addToBackStack(null)
//				.commit();

	}

	private void create_customerActvity() {

		Intent myIntent = new Intent(getActivity(), CustomerlistActivity.class);
		//Intent myIntent = new Intent(getActivity(), CustomerlistActivityNew.class);
		getActivity().startActivity(myIntent);

	}

}
