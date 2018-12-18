package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 13-09-2016.
 */
public class m_order extends BaseObservable {

    Integer order_Id;
    String Customer;
    String OrderNo;
    String OrderDate;
    String Customer_Name;
    int Status;
    String Order_Status;
    String Invoice_Date;
    String line_Item_Count;
    String Balance_Amt;
    String OrderAmount;


    public String getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getInvoice_Amt() {
        return Invoice_Amt;
    }

    public void setInvoice_Amt(String invoice_Amt) {
        Invoice_Amt = invoice_Amt;
    }

    String Invoice_Amt;
    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    String InvoiceDate;

    public int getItems() {
        return Items;
    }

    public void setItems(int items) {
        Items = items;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    int Items;
    String InvoiceNo;

    public String getLine_Item_Count() {
        return line_Item_Count;
    }

    public void setLine_Item_Count(String line_Item_Count) {
        this.line_Item_Count = line_Item_Count;
    }


    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    String Amount;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    int type;


//Balance_Amt
//Order_Status

    @Bindable
    public String getOrdersStatus() {
        return Order_Status;
    }

    public void setOrdersStatus(String orderOrdeStatus) {
        Order_Status = orderOrdeStatus;
    }



    @Bindable
    public String getBalenceAmount() {
        return Balance_Amt;
    }

    public void setBalenceAmount(String orderBalance_Amt) {
        Balance_Amt = orderBalance_Amt;
    }


    @Bindable
    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }



    @Bindable
    public Integer getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(Integer order_Id) {
        this.order_Id = order_Id;
    }

    @Bindable
    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    @Bindable
    public String getOrderInvoiceDate() {
        return Invoice_Date;
    }

    public void setOrderInvoiceDate(String orderDate) {
        Invoice_Date = orderDate;
    }






//line_Item_Count

    @Bindable
    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    @Bindable
    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }


}
