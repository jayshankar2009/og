package com.synergy.keimed_ordergenie.activity;

import android.databinding.BaseObservable;

/**
 * Created by Admin on 30-01-2018.
 */

public class DistributorUsersModal extends BaseObservable {


    String _id;
    String Login_Name;
    String Full_Name;
    String Mobile_No;
    String email;
    String Role_ID;
    String Role_Name;
    String Active;
    String UserTasks;
    String profileImg;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLogin_Name() {
        return Login_Name;
    }

    public void setLogin_Name(String login_Name) {
        Login_Name = login_Name;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole_ID() {
        return Role_ID;
    }

    public void setRole_ID(String role_ID) {
        Role_ID = role_ID;
    }

    public String getRole_Name() {
        return Role_Name;
    }

    public void setRole_Name(String role_Name) {
        Role_Name = role_Name;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getUserTasks() {
        return UserTasks;
    }

    public void setUserTasks(String userTasks) {
        UserTasks = userTasks;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
