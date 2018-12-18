package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 08-09-2016.
 */
public class m_product_list_chemist extends BaseObservable
{
    //product id
    String _id;
    //product name
    String _in;
    //product Stock
    String _s;
    //product stock color
    String _sc;
    //product pack
    String _pz;
    //mfg name
    String _mn;
    //item code
    String _icode;
    //dose form
    String _dform;
    //PTR
    Float _ptr;
    //MRP
    Float _mrp;
    //Scheme
    String _schm;
    //half scheme
    String HSche;
    //perdent scheme
    String _pschm;
    //Product Name
    String _pname;
    //Qty
    String _qty;
    //Min Quantity
    String _mq;



    String _snm;

    private String MinQ;
    private String MaxQ;

    public String getMinQ() {
        return MinQ;
    }

    public void setMinQ(String minQ) {
        MinQ = minQ;
    }

    public String getMaxQ() {
        return MaxQ;
    }

    public void setMaxQ(String maxQ) {
        MaxQ = maxQ;
    }

    public String getSche() {
        return Sche;
    }

    public void setSche(String sche) {
        Sche = sche;
    }

    String Sche;
    public String get_icode() {
        return _icode;
    }

    public void set_icode(String _icode) {
        this._icode = _icode;
    }

    public String get_dform() {
        return _dform;
    }

    public void set_dform(String _dform) {
        this._dform = _dform;
    }

    public Float get_ptr() {
        return _ptr;
    }

    public void set_ptr(Float _ptr) {
        this._ptr = _ptr;
    }

    public Float get_mrp() {
        return _mrp;
    }

    public void set_mrp(Float _mrp) {
        this._mrp = _mrp;
    }

    public String get_schm() {
        return _schm;
    }

    public void set_schm(String _schm) {
        this._schm = _schm;
    }

    public String getHSche() {
        return HSche;
    }

    public void setHSche(String _hschm) {
        this.HSche = _hschm;
    }
    public String get_pschm() {
        return _pschm;
    }

    public void set_pschm(String _pschm) {
        this._pschm = _pschm;
    }

    public String get_pname() {
        return _pname;
    }

    public void set_pname(String _pname) {
        this._pname = _pname;
    }

    public String get_qty() {
        return _qty;
    }

    public void set_qty(String _qty) {
        this._qty = _qty;
    }


    public String get_mq() {
        return _mq;
    }

    public void set_mq(String _mq) {
        this._mq = _mq;
    }

    @Bindable
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    @Bindable
    public String get_in() {
        return _in;
    }

    public void set_in(String _in) {
        this._in = _in;
    }
    @Bindable
    public String get_s() {
        return _s;
    }

    public void set_s(String _s) {
        this._s = _s;
    }
    @Bindable
    public String get_sc() {
        return _sc;
    }

    public void set_sc(String _sc) {
        this._sc = _sc;
    }
    @Bindable
    public String get_pz() {
        return _pz;
    }

    public void set_pz(String _pz) {
        this._pz = _pz;
    }
    @Bindable
    public String get_mn() {
        return _mn;
    }

    public void set_mn(String _mn) {
        this._mn = _mn;
    }

    public String get_snm() {
        return _snm;
    }

    public void set_snm(String _snm) {
        this._snm = _snm;
    }
}