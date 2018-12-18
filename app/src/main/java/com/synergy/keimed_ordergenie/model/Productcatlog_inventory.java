package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Admin on 18-12-2017.
 */
public class Productcatlog_inventory extends BaseObservable {

    String Product_Code;
    String Packsize;
    Double Mrp;
    Double Rate;
    String MfgCode;
    String MfgName;
    String Image_path;


    public Productcatlog_inventory(String Product_Code, String Packsize,
                             Double Mrp,
                             Double Rate,
                             String MfgCode,
                             String MfgName,
                             String Image_path) {
        this.Product_Code = Product_Code;
        this.Packsize = Packsize;
        this.Mrp = Mrp;
        this.Rate = Rate;
        this.MfgCode = MfgCode;
        this.MfgName = MfgName;
        this.Image_path = Image_path;
    }

    @Bindable
    public String getProduct_Code() {
        return Product_Code;
    }

    @Bindable
    public String getImage_path() {
        return Image_path;
    }

    @Bindable
    public String getPacksize() {
        return Packsize;
    }

    @Bindable
    public Double getMrp() {
        return Mrp;
    }

    @Bindable
    public Double getRate() {
        return Rate;
    }

    @Bindable
    public String getMfgCode() {
        return MfgCode;
    }

    @Bindable
    public String getMfgName() {
        return MfgName;
    }
}


