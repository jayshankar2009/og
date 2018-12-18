package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by apurva on 23-05-2018.
 */
public class m_delivery_invoicelist extends BaseObservable {
    String ErpsalesmanID;
    String DeliveryDate;
    String OrderID;
    String InvoiceDate;
    String BoxCount;
    String InvoiceNo;
    String TotalAmount;
    String TotalItems;
    String Package_count;
    String DeliveryId;
    String DeliveryStatus;

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public String getErpsalesmanID() {
        return ErpsalesmanID;
    }

    public void setErpsalesmanID(String erpsalesmanID) {
        ErpsalesmanID = erpsalesmanID;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getBoxCount() {
        return BoxCount;
    }

    public void setBoxCount(String boxCount) {
        BoxCount = boxCount;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(String totalItems) {
        TotalItems = totalItems;
    }

    public String getPackage_count() {
        return Package_count;
    }

    public void setPackage_count(String package_count) {
        Package_count = package_count;
    }

    public String getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        DeliveryId = deliveryId;
    }
}
