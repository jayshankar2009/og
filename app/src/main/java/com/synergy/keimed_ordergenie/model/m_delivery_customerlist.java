package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;

/**
 * Created by apurva on 23-05-2018.
 */
public class m_delivery_customerlist extends BaseObservable {
    String ClientID;
    String Client_Code;
    String Client_Name;
    String Client_Address;
    ArrayList<m_delivery_invoice_list> ivoices;

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

    public String getClient_Name() {
        return Client_Name;
    }

    public void setClient_Name(String client_Name) {
        Client_Name = client_Name;
    }

    public String getClient_Address() {
        return Client_Address;
    }

    public void setClient_Address(String client_Address) {
        Client_Address = client_Address;
    }

    public ArrayList<m_delivery_invoice_list> getIvoices() {
        return ivoices;
    }

    public void setIvoices(ArrayList<m_delivery_invoice_list> ivoices) {
        this.ivoices = ivoices;
    }
}
