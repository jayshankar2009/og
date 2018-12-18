package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 06-09-2016.
 */
public class m_CartData extends BaseObservable {



    String Doc_item_No;
    String Itemname;
    String Packsize;
    String MRP;
    String Qty;
    String Createdon;
    String Rate;

    public String getDoc_item_No() {
        return Doc_item_No;
    }

    @Bindable
    public  String getCreatedon()
    {
        return Createdon;
    }
    public void setDoc_item_No(String doc_item_No) {
        this.Doc_item_No = doc_item_No;
    }

    @Bindable
    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String Itemname) {
        Itemname = Itemname;
    }

    @Bindable
    public String getPacksize() {
        return Packsize;
    }

    public void setPacksize(String Packsize) {
        Packsize = Packsize;
    }

    @Bindable
    public String getQty() {
        return Qty;
    }

    public void setQty(Integer Quantity) {
        Quantity = Quantity;
    }
    @Bindable
    public String getMRP() {
        return MRP;
    }

    public void setMRP(Float Mrp) {
        Mrp = Mrp;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }
}
