package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Admin on 18-12-2017.
 */

public class Productcatolg_modal extends BaseObservable {



    String Itemcode;
    String Itemname ;
    String Stock ;
    String Product_ID;
    String Packsize;
    String DoseForm;



    public List<Productcatlog_inventory> getline_items() {
        return line_items;
    }

    public void setline_items(List<Productcatlog_inventory> line_items) {
        this.line_items = line_items;
    }

    public List<Productcatlog_inventory> line_items;


    public String getDoseForm() {
        return DoseForm;
    }

    public List<Productcatlog_inventory> getLine_items() {
        return line_items;
    }

//    public Productcatolg_modal(String itemcode, String Itemname,
//                               String stock, String Product_ID1, String packsize, String DoseForm) {
//
//        Itemcode = itemcode;
//        this.Itemname = Itemname;
//        Stock = stock;
//        Product_ID=Product_ID1;
//        Packsize = packsize;
//        this.DoseForm = DoseForm;
//
//    }


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

}



