package com.synergy.keimed_ordergenie.model;

/**
 * Created by admin on 9/7/2018.
 */

public class Che_inventory_itemss {

    String IC;
    Float Mrp;
    Float Rate;
    String MfgC;
    String MfgN;
    String Img_path;
    String HSche;
    String PSche;

    public Che_inventory_itemss(String Ic, Float MRP, Float Rate,
                                String Mfgc,
                                String MfgN, String Img_path, String HalfScheme, String PercentScheme) {

        IC = Ic;
        Mrp = MRP;
        this.Rate = Rate;
        MfgC = Mfgc;
        this.MfgN = MfgN;
        this.Img_path = Img_path;
        HSche = HalfScheme;
        PSche = PercentScheme;
    }
    public String getIC() {
        return IC;
    }

    public void setIC(String IC) {
        this.IC = IC;
    }

    public Float getMrp() {
        return Mrp;
    }

    public void setMrp(Float mrp) {
        Mrp = mrp;
    }

    public Float getRate() {
        return Rate;
    }

    public void setRate(Float rate) {
        Rate = rate;
    }

    public String getMfgC() {
        return MfgC;
    }

    public void setMfgC(String mfgC) {
        MfgC = mfgC;
    }

    public String getMfgN() {
        return MfgN;
    }

    public void setMfgN(String mfgN) {
        MfgN = mfgN;
    }

    public String getImg_path() {
        return Img_path;
    }

    public void setImg_path(String img_path) {
        Img_path = img_path;
    }

    public String getHSche() {
        return HSche;
    }

    public void setHSche(String HSche) {
        this.HSche = HSche;
    }

    public String getPSche() {
        return PSche;
    }

    public void setPSche(String PSche) {
        this.PSche = PSche;
    }






   /* String Product_Code;
    Float Mrp;
    Float Rate;
    String MfgCode;
    String MfgName;
    String Image_path;
    String HalfScheme;
    String PercentScheme;*/


}
