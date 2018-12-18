package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

/**
 * Created by 1132 on 12-09-2016.
 */
public class m_all_pending_list extends BaseObservable {

    String Cust_code;
    String   Cust_name;
    Double Cust_outstanding;
    public List<m_all_pending_list_line_items> line_items;

    public void setLine_items(List<m_all_pending_list_line_items> line_items) {
        this.line_items = line_items;
    }



    public m_all_pending_list( String Cust_code,
                               String   Cust_name,
                               Double Cust_outstanding,List<m_all_pending_list_line_items> line_items
                               ) {

        this.Cust_code = Cust_code;
        this.Cust_name = Cust_name;
        this.Cust_outstanding = Cust_outstanding;
        this.line_items=line_items;

    }


    @Bindable

    public String getCust_code() {
        return Cust_code;
    }

    @Bindable
    public String getCust_name() {
        return Cust_name;
    }

    @Bindable
    public Double getCust_outstanding() {
        return Cust_outstanding;
    }

    @Bindable
    public List<m_all_pending_list_line_items> getLine_items() {
        return line_items;
    }


}
