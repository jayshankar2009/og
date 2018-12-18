package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1132 on 12-09-2016.
 */
public class m_all_pending_list_line_items  extends BaseObservable {

    String Invoiceno;

    String Invoiceid;
    String  Invoicedate;
    Integer Totalitems;
    Double Billamount;
    Double Paymentreceived;
    Double Balanceamt;

    String InvoiceID;
  //  String Order_ID;
    String Due_date;
    String OrderId;

    public String getInvoiceid() {
        return Invoiceid;
    }

    public void setInvoiceid(String invoiceid) {
        Invoiceid = invoiceid;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

//    public String getOrder_ID() {
//        return Order_ID;
//    }
//
//    public void setOrder_ID(String order_ID) {
//        Order_ID = order_ID;
//    }

    //
//    public m_all_pending_list_line_items(   String Invoiceno,
//                                            String  Invoicedate,
//                                            Integer Totalitems,
//                                            Double Billamount,
//                                            Double Paymentreceived,
//                                            Double Balanceamt,
//                                            String Due_date,
//                                            String OrderId
//    )
//    {
//        this.Invoiceno=Invoiceno;
//        this.Invoicedate=Invoicedate;
//        this.Totalitems=Totalitems;
//        this.Billamount=Billamount;
//        this.Paymentreceived=Paymentreceived;
//        this.Balanceamt=Balanceamt;
//        this.Due_date=Due_date;
//        this.OrderId=OrderId;
//    }
//




    @Bindable
    public String getInvoiceno() {
        return Invoiceno;
    }

    @Bindable
    public String getInvoicedate() {
        return Invoicedate;
    }

    @Bindable
    public Integer getTotalitems() {
        return Totalitems;
    }

    @Bindable
    public Double getBillamount() {
        return Billamount;
    }

    @Bindable
    public Double getPaymentreceived() {
        return Paymentreceived;
    }

    @Bindable
    public Double getBalanceamt() {
        return Balanceamt;
    }

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
    }
}
