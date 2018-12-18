package com.synergy.keimed_ordergenie.model;

import java.io.Serializable;

/**
 * Created by prakash on 17/07/16.
 */
public class d_month_wise_graph implements Serializable {

 String count;
 String year;
 String week;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
}
