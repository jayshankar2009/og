package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

/**
 * Created by 1144 on 05-01-2017.
 */

public class m_General_Info extends BaseObservable {

    private Integer Molecule_ID;
    private String Pharmacology_Option;
    private String Pharmacology_Desc;
    private Integer Pharmacology_ID;

    public List<Product_info_items> product_infor_items;


    @Bindable
    public Integer getMolecule_ID() {
        return Molecule_ID;
    }

    @Bindable
    public void setMolecule_ID(Integer molecule_ID) {
        Molecule_ID = molecule_ID;
    }

    @Bindable
    public String getPharmacology_Option() {
        return Pharmacology_Option;
    }

    @Bindable
    public void setPharmacology_Option(String pharmacology_Option) {
        Pharmacology_Option = pharmacology_Option;
    }

    @Bindable
    public String getPharmacology_Desc() {
        return Pharmacology_Desc;
    }

    @Bindable
    public void setPharmacology_Desc(String pharmacology_Desc) {
        Pharmacology_Desc = pharmacology_Desc;
    }

    @Bindable
    public Integer getPharmacology_ID() {
        return Pharmacology_ID;
    }

    @Bindable
    public void setPharmacology_ID(Integer pharmacology_ID) {
        Pharmacology_ID = pharmacology_ID;
    }



/*    @Bindable
    public List<Product_info_items> getLine_items() {
        return product_infor_items;
    }*/
}
