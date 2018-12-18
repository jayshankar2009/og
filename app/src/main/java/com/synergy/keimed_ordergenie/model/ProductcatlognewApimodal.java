package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

/**
 * Created by Admin on 01-02-2018.
 */

public class ProductcatlognewApimodal extends BaseObservable {

String Product_ID;
String Product_Code;
String Product_Desc;
String Category_Name;
String Stk_Prod_Code;
String Stk_Prod_Desc;
String MRP;
String PTR;
String Stock;
String Manufacturer_Name;
String Scheme;

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_Code() {
        return Product_Code;
    }

    public void setProduct_Code(String product_Code) {
        Product_Code = product_Code;
    }

    public String getProduct_Desc() {
        return Product_Desc;
    }

    public void setProduct_Desc(String product_Desc) {
        Product_Desc = product_Desc;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public String getStk_Prod_Code() {
        return Stk_Prod_Code;
    }

    public void setStk_Prod_Code(String stk_Prod_Code) {
        Stk_Prod_Code = stk_Prod_Code;
    }

    public String getStk_Prod_Desc() {
        return Stk_Prod_Desc;
    }

    public void setStk_Prod_Desc(String stk_Prod_Desc) {
        Stk_Prod_Desc = stk_Prod_Desc;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getPTR() {
        return PTR;
    }

    public void setPTR(String PTR) {
        this.PTR = PTR;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getManufacturer_Name() {
        return Manufacturer_Name;
    }

    public void setManufacturer_Name(String manufacturer_Name) {
        Manufacturer_Name = manufacturer_Name;
    }

    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    String popularity;



}
