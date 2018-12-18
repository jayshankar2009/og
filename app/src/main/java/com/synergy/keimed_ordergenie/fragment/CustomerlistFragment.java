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
import com.synergy.keimed_ordergenie.model.m_customerlist;
import com.synergy.keimed_ordergenie.utils.OGtoast;

public class CustomerlistFragment extends Fragment {

	@BindView(R.id.rv_datalist)
	RecyclerView rvCustomerlist;

	@BindView(R.id.fab)
	FloatingActionButton fab;

	RecyclerView.Adapter<BindingViewHolder> o_adapter;

	//private RecyclerView rvCustomerlist;
	private static Context mContext;

	public static CustomerlistFragment newInstance(Context mContext) {

		mContext = mContext;
		CustomerlistFragment fragment = new CustomerlistFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_customerlist, container, false);

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
		//update_recycleview();
	}


	private void json_update(){

		String jsondata = loadJSONFromAsset();
		GsonBuilder builder = new GsonBuilder();
		ad_customerlist adapter;
		Gson mGson = builder.create();
		//List<m_customerlist> posts_drug_interaction = new ArrayList<m_customerlist>();

		List<?> posts = new ArrayList<m_customerlist>();
		posts = Arrays.asList(mGson.fromJson(jsondata, m_customerlist[].class));
	//	Log.e("post",posts.toString());
		final int CUSTOMEROUTSTANDING = 0;
		adapter = new ad_customerlist(this.getActivity(), posts,CUSTOMEROUTSTANDING,
				R.layout.fragement_customerlist_items,BR.v_customerlist);

		//Observable<Pair<List<TourStepEntity>
		rvCustomerlist.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		rvCustomerlist.setAdapter(adapter);



	}
/*	private void update_recycleview() {

		*//*int count = 50;
		double f_amount = 200.89;
		final m_customerlist[] mo_customerlist = new m_customerlist[count];
		for (int i = 0; i < mo_customerlist.length; i++) {
			mo_customerlist[i] = new m_customerlist(getString(R.string.app_name) + i, "", "", i, f_amount,"");
		}
*//*

		//RecyclerView.Adapter<BindingViewHolder> o_adapter = new RecyclerView.Adapter<BindingViewHolder>() {
		o_adapter = new RecyclerView.Adapter<BindingViewHolder>() {
			@Override
			public BindingViewHolder onCreateViewHolder(ViewGroup parent,
														int viewType) {
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				ViewDataBinding binding =
						DataBindingUtil.inflate(inflater, R.layout.fragement_customerlist_items, parent, false);

				return new BindingViewHolder(binding.getRoot());
			}

			@Override
			public void onBindViewHolder(BindingViewHolder holder, int position) {
				m_customerlist customerlist = mo_customerlist[position];
				holder.getBinding().setVariable(BR.v_customerlist, customerlist);
				holder.getBinding().executePendingBindings();
			}

			@Override
			public int getItemCount() {
				return mo_customerlist.length;
			}
		};
		rvCustomerlist.setLayoutManager(new LinearLayoutManager(getActivity()));
		rvCustomerlist.setAdapter(o_adapter);

	}*/

	@OnClick(R.id.fab)
	public void submit(View view) {


		OGtoast.OGtoast( "You win!",getActivity());
	}



	public String loadJSONFromAsset() {
		String json = null;
		try {
			InputStream is = getActivity().getAssets().open("customer_outstanding.json");
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
