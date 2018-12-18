package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1132 on 25-07-2016.
 */
public class m_orderhistoryitems extends BaseObservable {

    String Item_Name;
    String Packsize; 
    Integer Qty;
    //Double Rate;
    Double MRP;
    Integer Order_no;
    Integer ItemSrNo;

    public m_orderhistoryitems( String Item_Name,
                                String Packsize,
                                Integer Qty,
                                //Double rate,
                                Double MRP,
                                Integer Order_no,
                                Integer ItemSrNo) {

        m_orderhistoryitems.this.Item_Name = Item_Name;
        m_orderhistoryitems.this.Packsize = Packsize;
        m_orderhistoryitems.this.Qty = Qty;
        //m_orderhistoryitems.this.Rate = rate;
        m_orderhistoryitems.this.MRP = MRP;
        m_orderhistoryitems.this.Order_no = Order_no;
        m_orderhistoryitems.this.ItemSrNo = ItemSrNo;
    }


    @Bindable
    public String getItem_Name() {
        return Item_Name;
    }

    @Bindable
    public String getPacksize() {
        return Packsize;
    }

    @Bindable
    public Integer getQty() {
        return Qty;
    }

    /*@Bindable
    public Double getRate() {
        return Rate;
    }*/

    @Bindable
    public Double getMRP() {
        return MRP;
    }

    @Bindable
    public Integer getOrder_no() {
        return Order_no;
    }
    @Bindable
    public Integer getItemSrNo() {
        return ItemSrNo;
    }

}