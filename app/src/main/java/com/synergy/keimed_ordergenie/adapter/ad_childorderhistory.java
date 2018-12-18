package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_orderhistoryitems;

/**
 * Created by prakash on 13/07/16.
 */
public class ad_childorderhistory extends RecyclerView.Adapter<BindingViewHolder> {


    //List<m_customerlist>
    private List<m_orderhistoryitems> _datalist = null;
    private Context _mContext;

    private ViewDataBinding binding;

    public ad_childorderhistory(List<m_orderhistoryitems> _datalist) {
        this._datalist = _datalist;
    }


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {


        BindingViewHolder itemView = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_childorderhistory, parent, false);
        itemView = new BindingViewHolder(binding.getRoot());


        return itemView;
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {

        m_orderhistoryitems pendingbills = _datalist.get(position);
        holder.getBinding().setVariable(BR.v_orderhistory_child, pendingbills);

        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        //return al_customerlist.length;
        return this._datalist.size();
    }

    public Object getItem(int position) {
        return _datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}


