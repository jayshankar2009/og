package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 08-09-2016.
 */
public class m_invoice_item extends BaseObservable
{

    String Product_Desc;
    String Packsize;
    String Qty;
    String MRP;

    @Bindable
    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    @Bindable
    public String getProduct_Desc() {
        return Product_Desc;
    }

    public void setProduct_Desc(String product_Desc) {
        Product_Desc = product_Desc;
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




}
