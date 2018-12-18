package com.synergy.keimed_ordergenie.model;

/**
 * Created by Admin on 29-11-2017.
 */

public class Orderlistmodal {

    String date;
    String ChemistName;
    String timing;
    String no_of_Items;
    String detail_Prized;
    String circular_Name;

    public Orderlistmodal(String date, String ChemistName, String timing, String no_of_Items, String detail_Prized, String circular_Name )
    {

        this.date = date;
        this.ChemistName = ChemistName;
        this.timing = timing;
        this.no_of_Items = no_of_Items;
        this.detail_Prized = detail_Prized;
        this.circular_Name = circular_Name;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChemistName() {
        return ChemistName;
    }

    public void setChemistName(String chemistName) {
        ChemistName = chemistName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getNo_of_Items() {
        return no_of_Items;
    }

    public void setNo_of_Items(String no_of_Items) {
        this.no_of_Items = no_of_Items;
    }

    public String getDetail_Prized() {
        return detail_Prized;
    }

    public void setDetail_Prized(String detail_Prized) {
        this.detail_Prized = detail_Prized;
    }

    public String getCircular_Name() {
        return circular_Name;
    }

    public void setCircular_Name(String circular_Name) {
        this.circular_Name = circular_Name;
    }
}

