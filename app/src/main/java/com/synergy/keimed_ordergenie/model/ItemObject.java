package com.synergy.keimed_ordergenie.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 06-12-2017.
 */

//Invoice_Date

public class ItemObject {
    @SerializedName("Invoice_Date")
    private String Invoice_Date;
    @SerializedName("Customer")
    private String Customer;
    @SerializedName("Order_Status")
    private String Order_Status;
    @SerializedName("OrderNo")
    private String OrderNo;
    @SerializedName("Invoice_Amt")
    private String Invoice_Amt;
    public ItemObject(String Invoice_Date,String Customer, String Order_Status, String OrderNo) {
        this.Invoice_Date = Invoice_Date;
        this.Customer = Customer;
        this.Order_Status = Order_Status;
        this.OrderNo = OrderNo;
    }

    public String getInvoice_Date() {
        return Invoice_Date;
    }

    public String getCustomer() {
        return Customer;
    }

    public String getOrder_Status() {
        return Order_Status;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public String getInvoice_Amt() {
        return Invoice_Amt;
    }
}

