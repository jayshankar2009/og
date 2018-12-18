package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 05-01-2017.
 */

public class m_message_list extends BaseObservable {



    private Integer NotificationID;
    private String Title;
    private String Description;
    private String Message_time;


    @Bindable
    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
    @Bindable
    public String getShortSummary() {
        return ShortSummary;
    }

    public void setShortSummary(String shortSummary) {
        ShortSummary = shortSummary;
    }

    private Integer Status;
    private String ShortSummary;


    @Bindable
    public Integer getNotificationID() {
        return NotificationID;
    }

    @Bindable
    public void setNotificationID(Integer notificationID) {
        NotificationID = notificationID;
    }

    @Bindable
    public String getTitle() {
        return Title;
    }

    @Bindable
    public void setTitle(String title) {
        Title = title;
    }

    @Bindable
    public String getDescription() {
        return Description;
    }

    @Bindable
    public void setDescription(String description) {
        Description = description;
    }

    @Bindable
    public String getMessage_time() {
        return Message_time;
    }

    @Bindable
    public void setMessage_time(String message_time) {
        Message_time = message_time;
    }



}
