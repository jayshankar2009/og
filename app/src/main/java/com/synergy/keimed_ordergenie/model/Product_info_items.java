package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

/**
 * Created by Mauli on 9/15/2017.
 */

public class Product_info_items extends BaseObservable{

    String MfgName;
    Double Rate;
    String Scheme;
    int Status;

//    public Product_info_items(String MfgName,Double Rate,String Scheme, int Status )
//    {
//        this.MfgName=MfgName;
//        this.Rate= Rate;
//        this.Scheme=Scheme;
//        this.Status=Status;
//
//    }

    public void setMfgName(String mfgName) {
        MfgName = mfgName;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }




    public String getMfgName() {
        return MfgName;
    }






}
