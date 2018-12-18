package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by prakash on 30/06/16.
 */
public class m_customerlist extends BaseObservable {

	String Chemist_id;
	String Cust_Code;
	String CustomerName;
	String Email_id;
	String Mobile;
	String Location;
	double Latitude;
	double Longitude;
	String Outstanding_Bill;


	@Bindable
	public String getChemist_id() {
		return Chemist_id;
	}

	public void setChemist_id(String chemist_id) {
		Chemist_id = chemist_id;
	}



	@Bindable
	public String getCust_Code() {
		return Cust_Code;
	}

	public void setCust_Code(String cust_Code) {
		Cust_Code = cust_Code;
	}

	@Bindable
	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerNames) {
		CustomerName = customerNames;
	}

	@Bindable
	public String getEmail_id() {
		return Email_id;
	}

	public void setEmail_id(String email_id) {
		Email_id = email_id;
	}

	@Bindable
	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    @Bindable
	public String getOutstanding_Bill() {
		return Outstanding_Bill;
	}

	public void setOutstanding_Bill(String outstanding_Bill) {
        Outstanding_Bill = outstanding_Bill;
	}
	@Bindable
	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}


	public m_customerlist(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public m_customerlist(String CustomerName, String Location,
						  String Email_id, String Cust_code,
						  String Outstanding_Bill, String Mobile) {
		this.CustomerName = CustomerName;
		this.Cust_Code = Cust_code;
		this.Outstanding_Bill = Outstanding_Bill;
		this.Location = Location;
		this.Email_id = Email_id;
		this.Mobile = Mobile;
	}

	public m_customerlist newInstance(){
		m_customerlist o_customerlist = new m_customerlist("");
		return o_customerlist;

	}


	/* New Constructor */
	/*public m_customerlist(String chemistId, String custCode, String custName, String email, String mobile, String location, double latitude,
						  double longitude, String outStandingBill) {
		setChemist_id(chemistId);
		setCust_Code(custCode);
		setCustomerName(custName);
		setEmail_id(email);
		setMobile(mobile);
		setLocation(location);
		setLatitude(latitude);
		setLongitude(longitude);
		setOutstanding_Bill(outStandingBill);
	}*/


	public m_customerlist() {
	}
}
