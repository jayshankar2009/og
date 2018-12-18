package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by prakash on 17/07/16.
 */
public class m_inventory extends BaseObservable {

//	"itemcode": "1001”
//			"itemname" : "Ace Drops Ear Drops”,
//			"packsize": "5ml",
//			"mrp": 150.00,
//			"rate scheme" : 120,
//			"stock": 1658,
//			"mfgcode":1001 ,
//			"mfgname": "Ajant Pharma"
//   Itemname


	String Itemcode;
	String Itemname ;
	String Stock ;
	String Product_ID;
	String Packsize;
	String DoseForm;
	String Scheme;
	String LegendName;
	String LegendColor;
	public List<m_inventory_items> line_items;


	public List<m_inventory_items> getline_items() {
		return line_items;
	}

	public void setline_items(List<m_inventory_items> line_items) {
		this.line_items = line_items;
	}





	public m_inventory(String itemcode, String Itemname, String stock,String Product_ID1,String packsize,String doseform, String scheme,String legendName, String legendColor) {
		Itemcode = itemcode;
		this.Itemname = Itemname;
		Stock = stock;
		Product_ID=Product_ID1;
		Packsize = packsize;
		DoseForm=doseform;
		Scheme = scheme;
		LegendName = legendName;
		LegendColor=legendColor;
	}


	@Bindable
	public String getItemcode() {
		return Itemcode;
	}
	
	public String getItemname() {
		return Itemname;
	}


	@Nullable
	@Bindable
	public String getStock() {
		return Stock;
	}
	public String getProduct_ID() {
		return Product_ID;
	}


	@Bindable
	public String getPacksize() {
		return Packsize;
	}

	public void setPacksize(String packsize) {
		Packsize = packsize;
	}


	@Bindable
	public String getDoseForm() {
		return DoseForm;
	}

	public void setDoseForm(String doseForm) {
		DoseForm = doseForm;
	}

	@Bindable
	public String getScheme() {
		return Scheme;
	}

	public void setScheme(String scheme) {
		Scheme = scheme;
	}


	public String getLegendName() {
		return LegendName;
	}

	public void setLegendName(String legendName) {
		LegendName = legendName;
	}

	public String getLegendColor() {
		return LegendColor;
	}

	public void setLegendColor(String legendColor) {
		LegendColor = legendColor;
	}

}
