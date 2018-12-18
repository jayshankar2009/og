package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

 public class Sales extends BaseObservable {

    private String _chId;
    private String _chName;

    private String s_inv_type;
    private String s_salesAmount;
    private String s_totalbills;

    private String r_inv_type;
    private String r_salesreturn;
    private String r_salesreturnbills;

    private String t_amt;

    private String dummy_1;
    private String dummy_2;
    private String dummy_3;
    private String dummy_4;
    private String s_targaet;
    public String get_chId() {
        return _chId;
    }

    public void set_chId(String _chId) {
        this._chId = _chId;
    }

    public String get_chName() {
        return _chName;
    }

    public void set_chName(String _chName) {
        this._chName = _chName;
    }

    public String getS_inv_type() {
        return s_inv_type;
    }

    public void setS_inv_type(String s_inv_type) {
        this.s_inv_type = s_inv_type;
    }

    public String getS_salesAmount() {
        return s_salesAmount;
    }

    public void setS_salesAmount(String s_salesAmount) {
        this.s_salesAmount = s_salesAmount;
    }

    public String getS_totalbills() {
        return s_totalbills;
    }

    public void setS_totalbills(String s_totalbills) {
        this.s_totalbills = s_totalbills;
    }

    public String getR_inv_type() {
        return r_inv_type;
    }

    public void setR_inv_type(String r_inv_type) {
        this.r_inv_type = r_inv_type;
    }

    public String getR_salesreturn() {
        return r_salesreturn;
    }

    public void setR_salesreturn(String r_salesreturn) {
        this.r_salesreturn = r_salesreturn;
    }

    public String getR_salesreturnbills() {
        return r_salesreturnbills;
    }

    public void setR_salesreturnbills(String r_salesreturnbills) {
        this.r_salesreturnbills = r_salesreturnbills;
    }

    public String getT_amt() {
        return t_amt;
    }

    public void setT_amt(String t_amt) {
        this.t_amt = t_amt;
    }

    public String getDummy_1() {
        return dummy_1;
    }

    public void setDummy_1(String dummy_1) {
        this.dummy_1 = dummy_1;
    }

    public String getDummy_2() {
        return dummy_2;
    }

    public void setDummy_2(String dummy_2) {
        this.dummy_2 = dummy_2;
    }

    public String getDummy_3() {
        return dummy_3;
    }

    public void setDummy_3(String dummy_3) {
        this.dummy_3 = dummy_3;
    }

    public String getDummy_4() {
        return dummy_4;
    }

    public void setDummy_4(String dummy_4) {
        this.dummy_4 = dummy_4;
    }


    public String getS_targaet() {
        return s_targaet;
    }

    public void setS_targaet(String s_targaet) {
        this.s_targaet = s_targaet;
    }
}
