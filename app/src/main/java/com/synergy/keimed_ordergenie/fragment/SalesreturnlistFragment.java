package com.synergy.keimed_ordergenie.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.adapter.ad_customerlist;
import com.synergy.keimed_ordergenie.model.m_salesreturn;
import com.synergy.keimed_ordergenie.utils.OGtoast;

public class SalesreturnlistFragment extends Fragment {

	@BindView(R.id.rv_datalist)
	RecyclerView rvCustomerlist;

	@BindView(R.id.fab)
	FloatingActionButton fab;

	RecyclerView.Adapter<BindingViewHolder> o_adapter;

	//private RecyclerView rvCustomerlist;
	private static Context mContext;

	public static SalesreturnlistFragment newInstance(Context mContext) {

		mContext = mContext;
		SalesreturnlistFragment fragment = new SalesreturnlistFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_salesreturnmenu, container, false);

		ButterKnife.bind(this, view);
		return view;

	}

	public void onButtonPressed(Uri uri) {

		getActivity().finish();
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
		json_update();
	}


	private void json_update(){

		String jsondata = loadJSONFromAsset();
		GsonBuilder builder = new GsonBuilder();
		ad_customerlist adapter;
		Gson mGson = builder.create();
		List<m_salesreturn> posts = new ArrayList<m_salesreturn>();
		posts = Arrays.asList(mGson.fromJson(jsondata, m_salesreturn[].class));

		final int SALESRETURN = 3;
		adapter = new ad_customerlist(this.getActivity(), posts,SALESRETURN,
				R.layout.fragement_salesreturn_items, BR.v_salesreturn);

		rvCustomerlist.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		rvCustomerlist.setAdapter(adapter);

	}


	@OnClick(R.id.fab)
	public void submit(View view) {


		OGtoast.OGtoast("You win!",getActivity());
	}



	public String loadJSONFromAsset() {
		String json = null;
		try {
			InputStream is = getActivity().getAssets().open("sales_return.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}
}
