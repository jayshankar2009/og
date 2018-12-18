package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Mauli on 9/22/2017.
 */

public class m_stokiest_distributor_list extends BaseObservable {

    String ClientID;
    String Client_Code;
    String Client_LegalName;
    String CityID;
    String CityName;
    String StateName;
    String StateID;
    String Client_Contact;
    String Client_Address;
    String Active;
    String Client_Name;
    String Client_Email;
    String Createdon;


    @Bindable
    public String getClient_Code() {
        return Client_Code;
    }

    public void setClient_Code(String client_Code) {
        Client_Code = client_Code;
    }


    @Bindable
    public String getClient_LegalName() {
        return Client_LegalName;
    }

    public void setClient_LegalName(String client_LegalName) {
        Client_LegalName = client_LegalName;
    }


    @Bindable
    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }



    @Bindable
    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }


    @Bindable
    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }


    @Bindable
    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }


    @Bindable
    public String getClient_Contact() {
        return Client_Contact;
    }

    public void setClient_Contact(String client_Contact) {
        Client_Contact = client_Contact;
    }



    @Bindable
    public String getClient_Address() {
        return Client_Address;
    }

    public void setClient_Address(String client_Address) {
        Client_Address = client_Address;
    }


    @Bindable
    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    @Bindable
    public String getClient_Name() {
        return Client_Name;
    }

    public void setClient_Name(String client_Name) {
        Client_Name = client_Name;
    }


    @Bindable
    public String getClient_Email() {
        return Client_Email;
    }

    public void setClient_Email(String client_Email) {
        Client_Email = client_Email;
    }


    @Bindable
    public String getCreatedon() {
        return Createdon;
    }

    public void setCreatedon(String createdon) {
        Createdon = createdon;
    }


    @Bindable
    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public m_stokiest_distributor_list(String Client_Legalname) {
        this.Client_LegalName = Client_Legalname;
    }

    public m_stokiest_distributor_list(String ClientId,String Client_code,String Client_LegalName,String CityId,String CityName,String Statename,String Stateid,String Client_contact,String Client_address,String Active,String Client_name,String Client_email,String Create_date)
    {
        this.ClientID = ClientId;
        this.Client_Code = Client_code;
        this.Client_LegalName = Client_LegalName;
        this.CityID = CityId;
        this.CityName = CityName;
        this.StateName = Statename;
        this.StateID = Stateid;
        this.Client_Contact = Client_contact;
        this.Client_Address = Client_address;
        this.Active = Active;
        this.Client_Name = Client_name;
        this.Client_Email = Client_email;
        this.Createdon = Create_date;
    }

    public m_stokiest_distributor_list newInstance(){
        m_stokiest_distributor_list stock_customerlist = new m_stokiest_distributor_list("");
        return stock_customerlist;

    }
}
