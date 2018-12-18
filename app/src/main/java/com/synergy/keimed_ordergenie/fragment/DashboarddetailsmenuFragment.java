package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;


/**
 * Created by prakash on 06/07/16.
 */
public class DashboarddetailsmenuFragment extends Fragment {

	private OnFragmentInteractionListener mListener;



	public DashboarddetailsmenuFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		LayoutInflater lf = getActivity().getLayoutInflater();

		View v_view =lf.inflate(R.layout.fragment_detailmenu, container, false);




		return v_view;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name


		void onFragmentLoaded(TextView  tx_customercount_value,TextView  tx_pendingbills_value,TextView  tx_orders_value,
							  TextView  tx_sales_value,TextView  tx_returns_value,TextView  tx_inv_value);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mListener!=null)
		{
			mListener.onFragmentLoaded((TextView)getView().findViewById(R.id.tx_customercount_value),(TextView)getView().findViewById(R.id.tx_pendingbills_value),
			(TextView)getView().findViewById(R.id.tx_orders_value),(TextView)getView().findViewById(R.id.tx_sales_value),
			(TextView)getView().findViewById(R.id.tx_returns_value),(TextView)getView().findViewById(R.id.tx_inv_value));
		}
	}

}

