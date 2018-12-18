package com.synergy.keimed_ordergenie.model;

/**
 * Created by admin on 11/29/2017.
 */

public class TopSkuInfo {
    public String product_name;
    //   public String company_name;
    public String ptr;
    //  public String mrp;
    //  public String pack_size;


    public TopSkuInfo(String product_name,  String ptr)
    {
        this.product_name = product_name;
        //      this.company_name = company_name;
        this.ptr = ptr;
        //      this.mrp = mrp;
        //       this.pack_size = pack_size;

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

//    public String getCompany_name() {
//        return company_name;
//    }
//
//    public void setCompany_name(String company_name) {
//        this.company_name = company_name;
//    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

//    public String getMrp() {
//        return mrp;
//    }
//
//    public void setMrp(String mrp) {
//        this.mrp = mrp;
//    }
//
//    public String getPack_size() {
//        return pack_size;
//    }
//
//    public void setPack_size(String pack_size) {
//        this.pack_size = pack_size;
//    }

}




