package com.synergy.keimed_ordergenie.model;

/**
 * Created by prakash on 04/07/16.
 */
public class m_dashboard {

	Integer totalsales ;
	Integer customercount ;
	Integer pendingorder;
	Integer salesreturns ;
	Integer salesorders;

	public Integer getTotalsales() {
		return totalsales;
	}

	public void setTotalsales(Integer totalsales) {
		this.totalsales = totalsales;
	}

	public Integer getCustomercount() {
		return customercount;
	}

	public void setCustomercount(Integer customercount) {
		this.customercount = customercount;
	}

	public Integer getPendingorder() {
		return pendingorder;
	}

	public void setPendingorder(Integer pendingorder) {
		this.pendingorder = pendingorder;
	}

	public Integer getSalesreturns() {
		return salesreturns;
	}

	public void setSalesreturns(Integer salesreturns) {
		this.salesreturns = salesreturns;
	}

	public Integer getSalesorders() {
		return salesorders;
	}

	public void setSalesorders(Integer salesorders) {
		this.salesorders = salesorders;
	}

}
