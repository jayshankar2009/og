package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by prakash on 30/06/16.
 */
public class m_salesreturn extends BaseObservable {




	String Month_name ;
	String return_ratio;
	Integer Fin_year;
	Double TotalSales;
	Double TotalReturn;


	public m_salesreturn(String month_name, String returnratio,
						 Integer Fin_year, Double sales_value, Double totalReturn) {

		this.Month_name = month_name;
		this.return_ratio = returnratio;
		this.Fin_year = Fin_year;
		this.TotalSales = sales_value;
		this.TotalReturn = totalReturn;
	}

	@Bindable
	public String getMonth_name() {
		return Month_name;
	}
	@Bindable
	public String getReturn_ratio() {
		return return_ratio;
	}
	@Bindable
	public Integer getFin_year() {
		return Fin_year;
	}
	@Bindable
	public Double getTotalSales() {
		return TotalSales;
	}
	@Bindable
	public Double getTotalReturn() {
		return TotalReturn;
	}




}
