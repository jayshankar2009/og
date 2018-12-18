package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Admin on 02-12-2017.
 */

public class PendingListModal extends BaseObservable {

   String Cust_code;
    String Cust_name;
    String Cust_outstanding;
    public List<d_pending_line_items> line_items;

    public List<d_pending_line_items> getLine_items() {
        return line_items;
    }

    public void setLine_items(List<d_pending_line_items> line_items) {
        this.line_items = line_items;
    }

    public String getCust_code() {
        return Cust_code;
    }

    public void setCust_code(String cust_code) {
        Cust_code = cust_code;
    }

    public String getCust_name() {
        return Cust_name;
    }

    public void setCust_name(String cust_name) {
        Cust_name = cust_name;
    }

    public String getCust_outstanding() {
        return Cust_outstanding;
    }

    public void setCust_outstanding(String cust_outstanding) {
        Cust_outstanding = cust_outstanding;
    }

    public PendingListModal(String Cust_code, String Cust_name, String Cust_outstanding) {

        this.Cust_code = Cust_code;
        this.Cust_name = Cust_name;
        this.Cust_outstanding = Cust_outstanding;

    }



}
