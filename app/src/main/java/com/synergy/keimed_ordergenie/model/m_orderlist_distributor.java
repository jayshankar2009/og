package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

/**
 * Created by 1144 on 13-09-2016.
 */
public class m_orderlist_distributor extends BaseObservable {

    String Transaction_No;
    String Doc_Date;
    String DOC_NO;
    String ClientID;
    String Client_Code;
    int Status;
    String Client_LegalName;
    Double Amount;
    int IsDownloaded;
    int Items;
    String Client_Address;

 public m_orderlist_distributor(String client_LegalName)
 {
     this.Client_LegalName=client_LegalName;
 }
    public String getTransaction_No() {
        return Transaction_No;
    }

    public void setTransaction_No(String transaction_No) {
        Transaction_No = transaction_No;
    }

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

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getClient_Code() {
        return Client_Code;
    }

    public void setClient_Code(String client_Code) {
        Client_Code = client_Code;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getClient_LegalName() {
        return Client_LegalName;
    }

    public void setClient_LegalName(String client_LegalName) {
        Client_LegalName = client_LegalName;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public int getIsDownloaded() {
        return IsDownloaded;
    }

    public void setIsDownloaded(int isDownloaded) {
        IsDownloaded = isDownloaded;
    }

    public int getItems() {
        return Items;
    }

    public void setItems(int items) {
        Items = items;
    }

    public String getClient_Address() {
        return Client_Address;
    }

    public void setClient_Address(String client_Address) {
        Client_Address = client_Address;
    }
}
