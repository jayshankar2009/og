package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by admin on 9/7/2018.
 */

public class ChemistInventorymodel extends BaseObservable {
    /*String P_ID;
    String IC;
    String IM;
    String Stk;
    String PZ;
    String Sche;
    String DF;
    String legendName;
    String legendColor;

    public String getMrp() {
        return Mrp;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
    }

    String Mrp;

    public String get_ptr() {
        return _ptr;
    }

    public void set_ptr(String _ptr) {
        this._ptr = _ptr;
    }

    String _ptr;

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    String Rate;

    public String getMfgN() {
        return MfgN;
    }

    public void setMfgN(String mfgN) {
        MfgN = mfgN;
    }

    String MfgN;
    public List<Che_inventory_itemss> line_items;

    public ChemistInventorymodel(String itemcode, String Itemname, String stock,String Product_ID1,String packsize,String doseform, String scheme,String legendName, String legendColor) {
        IC = itemcode;
        IM = Itemname;
        Stk = stock;
        P_ID=Product_ID1;
        PZ = packsize;
        DF=doseform;
        Sche = scheme;
        this.legendName = legendName;
        this.legendColor=legendColor;
    }


    public List<Che_inventory_itemss> getLine_items() {
        return line_items;
    }

    public void setLine_items(List<Che_inventory_itemss> line_items) {
        this.line_items = line_items;
    }




    *//*String Itemcode; //IC
    String Itemname ;  //IM
    String Stock ;     //Stk
    String Product_ID;  //P_ID
    String Packsize;  //PZ
    String DoseForm;  //DF
    String Scheme;  //Sche
    String LegendName;  //IC
    String LegendColor;*//*  //IC




    public String getLegendName() {
        return legendName;
    }

    public void setLegendName(String legendName) {
        this.legendName = legendName;
    }

    public String getLegendColor() {
        return legendColor;
    }

    public void setLegendColor(String legendColor) {
        this.legendColor = legendColor;
    }



    public String getP_ID() {
        return P_ID;
    }

    public void setP_ID(String p_ID) {
        P_ID = p_ID;
    }

    public String getIC() {
        return IC;
    }

    public void setIC(String IC) {
        this.IC = IC;
    }

    public String getIM() {
        return IM;
    }

    public void setIM(String IM) {
        this.IM = IM;
    }





    public String getStk() {
        return Stk;
    }

    public void setStk(String stk) {
        Stk = stk;
    }



    public String getPZ() {
        return PZ;
    }

    public void setPZ(String PZ) {
        this.PZ = PZ;
    }

    public String getSche() {
        return Sche;
    }

    public void setSche(String sche) {
        Sche = sche;
    }

    public String getDF() {
        return DF;
    }

    public void setDF(String DF) {
        this.DF = DF;
    }
*/

    private String _id;

    public String get_in() {
        return _in;
    }

    public void set_in(String _in) {
        this._in = _in;
    }

    public String get_s() {
        return _s;
    }

    public void set_s(String _s) {
        this._s = _s;
    }

    public String get_sc() {
        return _sc;
    }

    public void set_sc(String _sc) {
        this._sc = _sc;
    }

    public String get_pz() {
        return _pz;
    }

    public void set_pz(String _pz) {
        this._pz = _pz;
    }

    public String get_mn() {
        return _mn;
    }

    public void set_mn(String _mn) {
        this._mn = _mn;
    }

    public String get_lm() {
        return _lm;
    }

    public void set_lm(String _lm) {
        this._lm = _lm;
    }

    public String get_sid() {
        return _sid;
    }

    public void set_sid(String _sid) {
        this._sid = _sid;
    }

    public String get_snm() {
        return _snm;
    }

    public void set_snm(String _snm) {
        this._snm = _snm;
    }

    public String get_ptr() {
        return _ptr;
    }

    public void set_ptr(String _ptr) {
        this._ptr = _ptr;
    }

    public String get_sche() {
        return _sche;
    }

    public void set_sche(String _sche) {
        this._sche = _sche;
    }

    public String getBoxSize() {
        return BoxSize;
    }

    public void setBoxSize(String boxSize) {
        BoxSize = boxSize;
    }

    private String _in;
    private String _s;
    private String _sc;
    private String _pz;
    private String _mn;
    private String _lm;
    private String _sid;
    private String _snm;
    private String _ptr;
    private String _sche;
    private String _mrp;
    private String BoxSize;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_mrp() {
        return _mrp;
    }

    public void set_mrp(String _mrp) {
        this._mrp = _mrp;
    }
}
