package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 08-09-2016.
 */
public class m_Product_list extends BaseObservable
{

    String Itemcode;
    String Itemname;
    String Packsize;
    String MRP;
    String Rate;
    String Stock;
    String MfgCode;
    String type;
    String MfgName;
    String DoseForm;
    String Scheme;
    String Product_ID;


    @Bindable
    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }




    @Bindable
    public String getItemcode() {
        return Itemcode;
    }

    public void setItemcode(String itemcode) {
        Itemcode = itemcode;
    }

    @Bindable
    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String itemname) {
        Itemname = itemname;
    }

    @Bindable
    public String getPacksize() {
        return Packsize;
    }

    public void setPacksize(String packsize) {
        Packsize = packsize;
    }

    @Bindable
    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    @Bindable
    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    @Bindable
    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    @Bindable
    public String getMfgCode() {
        return MfgCode;
    }

    public void setMfgCode(String mfgCode) {
        MfgCode = mfgCode;
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getMfgName() {
        return MfgName;
    }

    public void setMfgName(String mfgName) {
        MfgName = mfgName;
    }

    @Bindable
    public String getDoseForm() {
        return DoseForm;
    }

    public void setDoseForm(String doseForm) {
        DoseForm = doseForm;
    }

    @Bindable
    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }


}
