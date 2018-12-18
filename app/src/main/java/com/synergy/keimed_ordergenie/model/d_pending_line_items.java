package com.synergy.keimed_ordergenie.model;

import java.io.Serializable;

/**
 * Created by prakash on 17/07/16.
 */
public class d_pending_line_items implements Serializable {

 String Invoiceno;
 Double Invoiceid;
 String Invoicedate;
 String Due_date;
 int Totalitems;
 Double Billamount;
 int Paymentreceived;
 Double Balanceamt;

	public String getInvoiceno() {
		return Invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		Invoiceno = invoiceno;
	}

	public Double getInvoiceid() {
		return Invoiceid;
	}

	public void setInvoiceid(Double invoiceid) {
		Invoiceid = invoiceid;
	}

	public String getInvoicedate() {
		return Invoicedate;
	}

	public void setInvoicedate(String invoicedate) {
		Invoicedate = invoicedate;
	}

	public String getDue_date() {
		return Due_date;
	}

	public void setDue_date(String due_date) {
		Due_date = due_date;
	}

	public int getTotalitems() {
		return Totalitems;
	}

	public void setTotalitems(int totalitems) {
		Totalitems = totalitems;
	}

	public Double getBillamount() {
		return Billamount;
	}

	public void setBillamount(Double billamount) {
		Billamount = billamount;
	}

	public int getPaymentreceived() {
		return Paymentreceived;
	}

	public void setPaymentreceived(int paymentreceived) {
		Paymentreceived = paymentreceived;
	}

	public Double getBalanceamt() {
		return Balanceamt;
	}

	public void setBalanceamt(Double balanceamt) {
		Balanceamt = balanceamt;
	}
}
