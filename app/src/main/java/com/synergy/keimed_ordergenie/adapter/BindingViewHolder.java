package com.synergy.keimed_ordergenie.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by prakash on 13/07/16.
 */
public class BindingViewHolder extends RecyclerView.ViewHolder {

    public BindingViewHolder(View itemView) {
        super(itemView);
    }

    public ViewDataBinding getBinding() {
        return DataBindingUtil.getBinding(itemView);
    }
}
