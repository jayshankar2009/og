package com.synergy.keimed_ordergenie.model;

import java.io.Serializable;

public class m_report_delivery_invoice_list implements Serializable {

    String ErpsalesmanID;
    String DeliveryDate;
    String DeliveryStatus;
    String OrderID;
    String InvoiceDate;
    String BoxCount;
    String InvoiceNo;
    String TotalAmount;
    String TotalItems;
    String Package_count;
    String DeliveryId;
    String DeliveryFlag;
    String ChemistId;
    String Description;
    String Client_Name;

    public String getClient_Name() {
        return Client_Name;
    }

    public void setClient_Name(String client_Name) {
        Client_Name = client_Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDeliveryFlag() {
        return DeliveryFlag;
    }

    public void setDeliveryFlag(String deliveryFlag) {
        DeliveryFlag = deliveryFlag;
    }



    public String getBoxCount() {
        return BoxCount;
    }

    public void setBoxCount(String boxCount) {
        BoxCount = boxCount;
    }

    private boolean isSelected;

    /* Set Balance For Payload */
    private String balanceAmountPayload;


    public String getChemistId() {
        return ChemistId;
    }

    public void setChemistId(String chemistId) {
        ChemistId = chemistId;
    }

    public m_report_delivery_invoice_list(String erpsalesmanID, String deliveryDate, String deliveryStatus, String orderID, String invoiceDate, String boxCount, String invoiceNo, String totalAmount, String totalItems, String package_count, String deliveryId, String deliveryFlag, String chemistId ,String description,String client_name) {
        ErpsalesmanID = erpsalesmanID;
        DeliveryDate = deliveryDate;
        DeliveryStatus = deliveryStatus;
        OrderID = orderID;
        InvoiceDate = invoiceDate;
        BoxCount=boxCount;
        InvoiceNo = invoiceNo;
        TotalAmount = totalAmount;
        TotalItems = totalItems;
        Package_count = package_count;
        DeliveryId=deliveryId;
        DeliveryFlag=deliveryFlag;
        ChemistId=chemistId;
        Description=description;
        Client_Name = client_name;
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

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getBalanceAmountPayload() {
        return balanceAmountPayload;
    }

    public void setBalanceAmountPayload(String balanceAmountPayload) {
        this.balanceAmountPayload = balanceAmountPayload;
    }
}
