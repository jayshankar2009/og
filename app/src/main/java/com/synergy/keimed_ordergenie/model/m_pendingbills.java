package com.synergy.keimed_ordergenie.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by prakash on 17/07/16.
 */
public class m_pendingbills implements Serializable{

    String Invoiceno;
    String Invoicedate;
    Integer Totalitems;
    String Billamount;
    String Paymentreceived;
    String Balanceamt;
    String InvoiceID;
    String GrandTotal;
    String TotalDiscount;



    String ledgerBalance;
    private boolean isSelected;

    /* Set Balance For Payload */
    private String balanceAmountPayload;




    public m_pendingbills(String invoiceno, String invoicedate, Integer totalitems,
                          String billamount, String paymentreceived, String balanceamt, String invoiceID, String grandTotal, String totalDiscount, String ledgerBalancee) {
        Invoiceno = invoiceno;
        Invoicedate = invoicedate;
        Totalitems = totalitems;
        Billamount = billamount;
        Paymentreceived = paymentreceived;
        Balanceamt = balanceamt;
        InvoiceID = invoiceID;
        GrandTotal = grandTotal;
        TotalDiscount = totalDiscount;
        ledgerBalance = ledgerBalancee;
    }


    public String getInvoiceno() {
        return Invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        Invoiceno = invoiceno;
    }

    public String getInvoicedate() {
        return Invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        Invoicedate = invoicedate;
    }

    public Integer getTotalitems() {
        return Totalitems;
    }

    public void setTotalitems(Integer totalitems) {
        Totalitems = totalitems;
    }

    public String getBillamount() {
        return Billamount;
    }

    public void setBillamount(String billamount) {
        Billamount = billamount;
    }

    public String getPaymentreceived() {
        return Paymentreceived;
    }

    public void setPaymentreceived(String paymentreceived) {
        Paymentreceived = paymentreceived;
    }

    public String getBalanceamt() {
        return Balanceamt;
    }

    public void setBalanceamt(String balanceamt) {
        Balanceamt = balanceamt;
    }

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
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

    public String getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        GrandTotal = grandTotal;
    }

    public String getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        TotalDiscount = totalDiscount;
    }


    public String getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(String ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }
}
