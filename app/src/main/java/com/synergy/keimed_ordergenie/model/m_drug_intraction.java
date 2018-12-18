package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 03-01-2017.
 */

public class m_drug_intraction extends BaseObservable {

    private Integer Molecule_Id;
    private String DrugDrugInteractionDesc;
    private String DrugInteractionMoleculeDetail;

    private String DrugInteractionName;
    private String DrugInteractionDesc;

    @Bindable
    public String getDrugInteractionName() {
        return DrugInteractionName;
    }

    @Bindable
    public void setDrugInteractionName(String drugInteractionName) {
        DrugInteractionName = drugInteractionName;
    }

    @Bindable
    public String getDrugInteractionDesc() {
        return DrugInteractionDesc;
    }

    @Bindable
    public void setDrugInteractionDesc(String drugInteractionDesc) {
        DrugInteractionDesc = drugInteractionDesc;
    }

    @Bindable
    public Integer getMolecule_Id() {
        return Molecule_Id;
    }

    @Bindable
    public void setMolecule_Id(Integer molecule_Id) {
        Molecule_Id = molecule_Id;
    }

    @Bindable
    public String getDrugDrugInteractionDesc() {
        return DrugDrugInteractionDesc;
    }

    @Bindable
    public void setDrugDrugInteractionDesc(String drugDrugInteractionDesc) {
        DrugDrugInteractionDesc = drugDrugInteractionDesc;
    }

    @Bindable
    public String getDrugInteractionMoleculeDetail() {
        return DrugInteractionMoleculeDetail;
    }

    @Bindable
    public void setDrugInteractionMoleculeDetail(String drugInteractionMoleculeDetail) {
        DrugInteractionMoleculeDetail = drugInteractionMoleculeDetail;
    }



}
