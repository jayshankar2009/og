package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 06-09-2016.
 */
public class m_distibutor_customer extends BaseObservable {


    Integer Stockist_Id;
    String Stokist_Name;
    Boolean Status;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    String location;
    @Bindable
    public String getStokist_Name() {
        return Stokist_Name;
    }

    public void setStokist_Name(String stokist_Name) {
        Stokist_Name = stokist_Name;
    }

    @Bindable
    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    @Bindable
    public Integer getStockist_Id() {
        return Stockist_Id;
    }

    public void setStockist_Id(Integer stockist_Id) {
        Stockist_Id = stockist_Id;
    }


}
