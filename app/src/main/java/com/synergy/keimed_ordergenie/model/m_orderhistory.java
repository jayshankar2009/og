package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

/**
 * Created by prakash on 17/07/16.
 */
public class m_orderhistory extends BaseObservable {


    Double Invoice_Amt;
    String Invoice_Date; 
    Integer OrderNo; 
    String line_Item_Count;
    String Order_Status;

    public List<m_orderhistoryitems> getline_items() {
        return line_items;
    }

    public void setline_items(List<m_orderhistoryitems> line_items) {
        line_items = line_items;
    }

    List<m_orderhistoryitems> line_items;



    
    public m_orderhistory( Double Invoice_Amt,String Invoice_Date,Integer OrderNo,String line_Item_Count,String Order_Status) {

        this.Invoice_Amt = Invoice_Amt;
        this.Invoice_Date = Invoice_Date;
        this.OrderNo = OrderNo;
        this.line_Item_Count = line_Item_Count;
        this.Order_Status = Order_Status;
    }
    @Bindable
    public Double getInvoice_Amt() {
        return Invoice_Amt;
    }

    @Bindable
    public String getInvoice_Date() {
        return Invoice_Date;
    }

    @Bindable
    public Integer getOrderNo() {
        return OrderNo;
    }

    @Bindable
    public String getLine_Item_Count() {
        return line_Item_Count;
    }

    @Bindable
    public String getOrder_Stauts() {
        return Order_Status;
    }

 /*   public List<m_orderhistoryitems> getChildList() {
        return ChildList;
    }*/

  /*  public void setChildList(List<m_orderhistoryitems> childList) {
        ChildList = childList;
    }*/


   /* public class m_orderhistoryitem extends BaseObservable {

        String Item_Name;
        String Packsize;
        Integer Qty;
        Double MRP;

        public m_orderhistoryitem( String Item_Name,
                                    String Packsize,
                                    Integer Qty,
                                    Double MRP) {

            m_orderhistoryitem.this.Item_Name = Item_Name;
            m_orderhistoryitem.this.Packsize = Packsize;
            m_orderhistoryitem.this.Qty = Qty;
            m_orderhistoryitem.this.MRP = MRP;
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

        @Bindable
        public Double getMRP() {
            return MRP;
        }


    }*/


}
