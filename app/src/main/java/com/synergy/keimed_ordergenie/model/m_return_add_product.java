package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

/**
 * Created by 1144 on 06-09-2016.
 */
public class m_return_add_product extends BaseObservable {

  String Product_Desc;
  String product_expiry;
  String product_batchno;
  String product_qty;
  String invoice_no;
  String Product_ID;
  String Itemname;
  String MRP;

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getProduct_Desc() {
        return Product_Desc;
    }

    public void setProduct_Desc(String product_Desc) {
        Product_Desc = product_Desc;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String itemname) {
        Itemname = itemname;
    }

    public m_return_add_product(String product_name, String product_expiry, String product_batchno, String product_qty, String invoice_no)
  {
      this.product_batchno=product_batchno;
      this.product_expiry=product_expiry;
      this.product_qty=product_qty;
      this.invoice_no=invoice_no;
//      this.Product_ID=Product_ID;
//      this.Itemname=Itemname;

  }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getProduct_expiry() {
        return product_expiry;
    }

    public void setProduct_expiry(String product_expiry) {
        this.product_expiry = product_expiry;
    }

    public String getProduct_batchno() {
        return product_batchno;
    }

    public void setProduct_batchno(String product_batchno) {
        this.product_batchno = product_batchno;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }
}
