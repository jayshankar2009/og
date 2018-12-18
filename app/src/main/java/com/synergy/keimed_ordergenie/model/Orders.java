package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

public class Orders extends BaseObservable {


    public String getDoc_Date() {
        return Doc_Date;
    }

    public void setDoc_Date(String doc_Date) {
        Doc_Date = doc_Date;
    }

    public String getDOC_NO() {
        return DOC_NO;
    }

    public void setDOC_NO(String DOC_NO) {
        this.DOC_NO = DOC_NO;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    private String Doc_Date;

    private String DOC_NO;

    private int Amount;
}
