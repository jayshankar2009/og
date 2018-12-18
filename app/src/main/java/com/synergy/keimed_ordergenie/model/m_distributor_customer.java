package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;

/**
 * Created by 1144 on 06-09-2016.
 */
public class m_distributor_customer extends BaseObservable {


    Integer ClientID;
    String Client_Code;
    String Client_LegalName;
    String CityName;
    String Client_Contact;
    String Client_Address;
    String StateID;
    String StateName;
    String Active;
    String Client_Email;
    String outstandingBal;
    String Createdon;
    String Payment_Pending_status;
    String orderStatus;

    public String getPayment_Pending_status() {
        return Payment_Pending_status;
    }

    public void setPayment_Pending_status(String payment_Pending_status) {
        Payment_Pending_status = payment_Pending_status;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getClientID() {
        return ClientID;
    }

    public void setClientID(Integer clientID) {
        ClientID = clientID;
    }

    public String getClient_Code() {
        return Client_Code;
    }

    public void setClient_Code(String client_Code) {
        Client_Code = client_Code;
    }

    public String getClient_LegalName() {
        return Client_LegalName;
    }

    public void setClient_LegalName(String client_LegalName) {
        Client_LegalName = client_LegalName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getClient_Contact() {
        return Client_Contact;
    }

    public void setClient_Contact(String client_Contact) {
        Client_Contact = client_Contact;
    }

    public String getClient_Address() {
        return Client_Address;
    }

    public void setClient_Address(String client_Address) {
        Client_Address = client_Address;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getClient_Email() {
        return Client_Email;
    }

    public void setClient_Email(String client_Email) {
        Client_Email = client_Email;
    }

    public String getOutstandingBal() {
        return outstandingBal;
    }

    public void setOutstandingBal(String outstandingBal) {
        this.outstandingBal = outstandingBal;
    }

    public String getCreatedon() {
        return Createdon;
    }

    public void setCreatedon(String createdon) {
        Createdon = createdon;
    }
}
