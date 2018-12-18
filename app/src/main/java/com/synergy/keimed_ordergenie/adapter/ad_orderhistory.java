package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_orderhistory;
import com.synergy.keimed_ordergenie.utils.CustomLinearLayoutManager;

/**
 * Created by prakash on 13/07/16.
 */
public class ad_orderhistory extends RecyclerView.Adapter<BindingViewHolder>  {
	private List<m_orderhistory> _datalist;

	private ViewDataBinding binding;
	ad_childorderhistory adapter;
	private Context context;
	private RecyclerView rv_orderlistitems;
	private CustomLinearLayoutManager layoutManager;
	private TextView btn_show_items;



	public ad_orderhistory(List<m_orderhistory> _datalistModal ,Context context) {

		if (_datalistModal == null) {
			throw new IllegalArgumentException(
					"_datalist must not be null");
		}
		this._datalist = _datalistModal;
		this.context=context;

	}

	@Override
	public BindingViewHolder onCreateViewHolder(
			ViewGroup viewGroup, int viewType) {

		BindingViewHolder itemView = null;
		LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

		binding = DataBindingUtil.inflate(inflater, R.layout.adapter_order_history, viewGroup, false);
		itemView = new BindingViewHolder(binding.getRoot());

		layoutManager = new CustomLinearLayoutManager(viewGroup.getContext(), LinearLayoutManager.VERTICAL,false);

		rv_orderlistitems=(RecyclerView)binding.getRoot().findViewById(R.id.rv_orderlistitems);

		btn_show_items=(TextView) binding.getRoot().findViewById(R.id.btn_show_items);

		btn_show_items.setTag(rv_orderlistitems);


		return  itemView;
	}

	@Override
	public void onBindViewHolder(
			final BindingViewHolder viewHolder,final int position) {

		m_orderhistory pendingbills =_datalist.get(position);
		viewHolder.getBinding().setVariable(BR.v_orderhistory, pendingbills);
		viewHolder.getBinding().executePendingBindings();
		ad_childorderhistory adapter = new ad_childorderhistory(pendingbills.getline_items());
		rv_orderlistitems.setAdapter(adapter);
		rv_orderlistitems.setHasFixedSize(false);
		rv_orderlistitems.setLayoutManager(new LinearLayoutManager(context));


		rv_orderlistitems.setTag(position);


		btn_show_items.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if ( ((RecyclerView)view.getTag()).getVisibility() == View.GONE) {
					((RecyclerView)view.getTag()).setVisibility(View.VISIBLE);
					((TextView)view).setText("Hide Items");
					notifyDataSetChanged();

				} else {
					((RecyclerView)view.getTag()).setVisibility(View.GONE);
					((TextView)view).setText("Show Items");
					notifyDataSetChanged();
				}
			}
		});

	}

	@Override
	public int getItemCount() {




		return _datalist.size();
	}
	@Override
	public int getItemViewType(int position) {

		return position;
	}


}

