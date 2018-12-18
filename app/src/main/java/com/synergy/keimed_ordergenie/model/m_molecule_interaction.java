package com.synergy.keimed_ordergenie.model;

/**
 * Created by 1132 on 31-01-2017.
 */

public class m_molecule_interaction {

    public Integer getMolecule_Id() {
        return Molecule_Id;
    }

    public void setMolecule_Id(Integer molecule_Id) {
        Molecule_Id = molecule_Id;
    }

    public String getDrugInteractionName() {
        return DrugInteractionName;
    }

    public void setDrugInteractionName(String drugInteractionName) {
        DrugInteractionName = drugInteractionName;
    }

    public String getDrugInteractionDesc() {
        return DrugInteractionDesc;
    }

    public void setDrugInteractionDesc(String drugInteractionDesc) {
        DrugInteractionDesc = drugInteractionDesc;
    }

    Integer   Molecule_Id;
         String DrugInteractionName;
            String DrugInteractionDesc;
}



