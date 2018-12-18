package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1132 on 07-09-2016.
 */
public class m_offerlist extends BaseObservable {





    Integer OfferID;
    String Product_Desc;
    String OfferDetails;
    String Validity;

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    String Product_ID;

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getMainImagePath() {
        return MainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        MainImagePath = mainImagePath;
    }

    String StartDate;
    String EndDate;

    String MainImagePath;

    @Bindable
    public Integer getOfferID() {
        return OfferID;
    }

    public void setOfferID(Integer offerID) {
        OfferID = offerID;
    }

    @Bindable
    public String getProduct_Desc() {
        return Product_Desc;
    }

    public void setProduct_Desc(String product_Desc) {
        Product_Desc = product_Desc;
    }

    @Bindable
    public String getOfferDetails() {
        return OfferDetails;
    }

    public void setOfferDetails(String offerDetails) {
        OfferDetails = offerDetails;
    }

    @Bindable
    public String getValidity() {
        return Validity;
    }

    public void setValidity(String validity) {
        Validity = validity;
    }



}
