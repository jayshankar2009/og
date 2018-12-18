package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by 1144 on 05-01-2017.
 */

public class m_stockist_orderdetails extends BaseObservable {

    private Integer OrderNo;
    private String Scheme;
    private String Packsize;
    private Integer Status;
    String Manufacturer_Name ;
    private Double Rate;
    private Double Price;
    private Integer InvQty;
    private  String Product_Desc;
    private Integer Qty;

    public Integer getQty() {
        return Qty;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public List<Product_info_items> getLine_items() {
        return line_items;
    }

    public void setLine_items(List<Product_info_items> line_items) {
        this.line_items = line_items;
    }

    public String getProduct_Desc() {
        return Product_Desc;
    }

    public void setProduct_Desc(String product_Desc) {
        Product_Desc = product_Desc;
    }

    public List<Product_info_items> line_items;


    public Integer getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(Integer orderNo) {
        OrderNo = orderNo;
    }

    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }

    public String getPacksize() {
        return Packsize;
    }

    public void setPacksize(String packsize) {
        Packsize = packsize;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getManufacturer_Name() {
        return Manufacturer_Name;
    }

    public void setManufacturer_Name(String manufacturer_Name) {
        Manufacturer_Name = manufacturer_Name;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Integer getInvQty() {
        return InvQty;
    }

    public void setInvQty(Integer invQty) {
        InvQty = invQty;
    }

//    public List<Product_info_items> getLine_items() {
//        return line_items;
//    }
//
//    public void setLine_items(List<Product_info_items> line_items)
//    {
//        this.line_items = line_items;
//    }
}
