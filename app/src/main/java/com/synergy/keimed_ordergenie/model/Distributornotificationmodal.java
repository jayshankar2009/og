package com.synergy.keimed_ordergenie.model;

/**
 * Created by Admin on 05-02-2018.
 */

public class Distributornotificationmodal {

    String chemistName;
    String notificationTiming;
    String getNotificationPrize;

    public Distributornotificationmodal(String chemistName, String notificationTiming, String getNotificationPrize) {

    this.chemistName = chemistName;
    this.notificationTiming = notificationTiming;
    this.getNotificationPrize = getNotificationPrize;


    }

    public String getChemistName() {
        return chemistName;
    }

    public void setChemistName(String chemistName) {
        this.chemistName = chemistName;
    }

    public String getNotificationTiming() {
        return notificationTiming;
    }

    public void setNotificationTiming(String notificationTiming) {
        this.notificationTiming = notificationTiming;
    }

    public String getGetNotificationPrize() {
        return getNotificationPrize;
    }

    public void setGetNotificationPrize(String getNotificationPrize) {
        this.getNotificationPrize = getNotificationPrize;
    }
}
