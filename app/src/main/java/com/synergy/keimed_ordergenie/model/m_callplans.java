package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 06-09-2016.
 */
public class m_callplans extends BaseObservable {
    Integer StockistCallPlanID;
    String Client_LegalName;
    String ClientLocation;
    String Call_duration;
    String Call_for;
    Integer BillAmount;
    Integer Order_Id;
    Double Latitude;
    Double Longitude;
    String TaskStatus;
    Integer Sequence;
    String isLocked;
    String isLocationCheck;

    public String getIsLocationCheck() {
        return isLocationCheck;
    }

    public void setIsLocationCheck(String isLocationCheck) {
        this.isLocationCheck = isLocationCheck;
    }

    public String getBlock_Reason() {
        return Block_Reason;
    }

    public void setBlock_Reason(String block_Reason) {
        Block_Reason = block_Reason;
    }

    String Block_Reason;
    public String getDLExpiry() {
        return DLExpiry;
    }

    public void setDLExpiry(String DLExpiry) {
        this.DLExpiry = DLExpiry;
    }

    String DLExpiry;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Bindable
    public Integer getSequence() {
        return Sequence;
    }

    public void setSequence(Integer sequence) {
        Sequence = sequence;
    }

    @Bindable
    public String getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        TaskStatus = taskStatus;
    }



    public String getChemistID() {
        return ChemistID;
    }

    public void setChemistID(String chemistID) {
        ChemistID = chemistID;
    }

    String ChemistID;

    public Integer getOrderRecevied() {
        return OrderRecevied;
    }

    public void setOrderRecevied(Integer orderRecevied) {
        OrderRecevied = orderRecevied;
    }

    Integer OrderRecevied;

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }




    @Bindable
    public Integer getStockistCallPlanID() {
        return StockistCallPlanID;
    }

    public void setStockistCallPlanID(Integer stockistCallPlanID) {
        StockistCallPlanID = stockistCallPlanID;
    }

    @Bindable
    public String getClient_LegalName() {
        return Client_LegalName;
    }

    public void setClient_LegalName(String client_LegalName) {
        Client_LegalName = client_LegalName;
    }

    @Bindable
    public String getClientLocation() {
        return ClientLocation;
    }

    public void setClientLocation(String clientLocation) {
        ClientLocation = clientLocation;
    }

    @Bindable
    public String getCall_duration() {
        return Call_duration;
    }

    public void setCall_duration(String call_duration) {
        Call_duration = call_duration;
    }

    @Bindable
    public String getCall_for() {
        return Call_for;
    }

    public void setCall_for(String call_for) {
        Call_for = call_for;
    }

    @Bindable
    public Integer getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(Integer billAmount) {
        BillAmount = billAmount;
    }

    @Bindable
    public Integer getOrder_Id() {
        return Order_Id;
    }

    public void setOrder_Id(Integer order_Id) {
        Order_Id = order_Id;
    }


    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

}



//package com.synergy.ordergenie.model;
//
//import android.databinding.BaseObservable;
//import android.databinding.Bindable;
//
///**
// * Created by 1144 on 06-09-2016.
// */
//public class m_callplans extends BaseObservable
//{
//
//    Integer StockistCallPlanID;
//    String Client_LegalName;
//    String ClientLocation;
//    String Call_duration;
//    String Call_for;
//    Integer BillAmount;
//    Integer Order_Id;
//    Double Latitude;
//    Double Longitude;
//    String TaskStatus;
//
//    @Bindable
//    public String getTaskStatus() {
//        return TaskStatus;
//    }
//
//    public void setTaskStatus(String taskStatus) {
//        TaskStatus = taskStatus;
//    }
//
//
//
//    public String getChemistID() {
//        return ChemistID;
//    }
//
//    public void setChemistID(String chemistID) {
//        ChemistID = chemistID;
//    }
//
//    String ChemistID;
//
//    public Integer getOrderRecevied() {
//        return OrderRecevied;
//    }
//
//    public void setOrderRecevied(Integer orderRecevied) {
//        OrderRecevied = orderRecevied;
//    }
//
//    Integer OrderRecevied;
//
//    public Double getLatitude() {
//        return Latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        Latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return Longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        Longitude = longitude;
//    }
//
//
//
//
//    @Bindable
//    public Integer getStockistCallPlanID() {
//        return StockistCallPlanID;
//    }
//
//    public void setStockistCallPlanID(Integer stockistCallPlanID) {
//        StockistCallPlanID = stockistCallPlanID;
//    }
//
//    @Bindable
//    public String getClient_LegalName() {
//        return Client_LegalName;
//    }
//
//    public void setClient_LegalName(String client_LegalName) {
//        Client_LegalName = client_LegalName;
//    }
//
//    @Bindable
//    public String getClientLocation() {
//        return ClientLocation;
//    }
//
//    public void setClientLocation(String clientLocation) {
//        ClientLocation = clientLocation;
//    }
//
//    @Bindable
//    public String getCall_duration() {
//        return Call_duration;
//    }
//
//    public void setCall_duration(String call_duration) {
//        Call_duration = call_duration;
//    }
//
//    @Bindable
//    public String getCall_for() {
//        return Call_for;
//    }
//
//    public void setCall_for(String call_for) {
//        Call_for = call_for;
//    }
//
//    @Bindable
//    public Integer getBillAmount() {
//        return BillAmount;
//    }
//
//    public void setBillAmount(Integer billAmount) {
//        BillAmount = billAmount;
//    }
//
//    @Bindable
//    public Integer getOrder_Id() {
//        return Order_Id;
//    }
//
//    public void setOrder_Id(Integer order_Id) {
//        Order_Id = order_Id;
//    }
//
//
//}
