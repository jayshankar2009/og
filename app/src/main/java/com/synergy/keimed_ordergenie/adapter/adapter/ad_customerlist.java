package com.synergy.keimed_ordergenie.adapter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.synergy.keimed_ordergenie.model.m_customerlist;
import com.synergy.keimed_ordergenie.model.m_inventory;
import com.synergy.keimed_ordergenie.model.m_pendingbills;
import com.synergy.keimed_ordergenie.model.m_salesreturn;

import java.util.List;

/**
 * Created by prakash on 13/07/16.
 */
public class ad_customerlist extends RecyclerView.Adapter<BindingViewHolder> {

    public static final int CUSTOMEROUTSTANDING = 0;
    public static final int INVENTORY = 1;
    public static final int ORDERS = 2;
    public static final int SALESRETURN = 3;
    public static final int PENDINGBILLS = 4;

    //List<m_customerlist>
    private List<?> _datalist = null;
    private Context _mContext;
    private Integer _layout_variable;
    private int _mDataSetTypes = -99;
    private int layoutresource = -99;
    private int variableId = -99;

    private ViewDataBinding binding;

    public ad_customerlist(Context _mContext, List<?> _datalist, int dataSetTypes,
                           @LayoutRes int resource, Integer variableId) {
        this._datalist = _datalist;
        this._mContext = _mContext;
        this._mDataSetTypes = dataSetTypes;
        this.layoutresource = resource;
        this.variableId = variableId;
    }


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BindingViewHolder itemView = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(inflater, layoutresource, parent, false);
        itemView = new BindingViewHolder(binding.getRoot());
        return itemView;
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (_mDataSetTypes == CUSTOMEROUTSTANDING) {
            m_customerlist customerlist = (m_customerlist) _datalist.get(position);
            holder.getBinding().setVariable(variableId, customerlist);
        }
        if (_mDataSetTypes == SALESRETURN) {
            m_salesreturn salesreturnlist = (m_salesreturn) _datalist.get(position);
            holder.getBinding().setVariable(variableId, salesreturnlist);
        }
        if (_mDataSetTypes == PENDINGBILLS) {
            m_pendingbills pendingbills = (m_pendingbills) _datalist.get(position);
            holder.getBinding().setVariable(variableId, pendingbills);
        }
        if (_mDataSetTypes == INVENTORY) {
            m_inventory inventory = (m_inventory) _datalist.get(position);
            holder.getBinding().setVariable(variableId, inventory);
        }
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


