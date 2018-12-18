package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1144 on 06-09-2016.
 */
public class m_call_activities extends BaseObservable
{

    Integer Call_activity_id;
    String Call_activity_time;
    String Call_activity_description;



    @Bindable
    public Integer getCall_activity_id() {
        return Call_activity_id;
    }

    public void setCall_activity_id(Integer Call_activity_id) {
        Call_activity_id = Call_activity_id;
    }

    @Bindable
    public String getCall_activity_time() {
        return Call_activity_time;
    }

    public void setCall_activity_time(String Call_activity_time) {
        Call_activity_time = Call_activity_time;
    }

    @Bindable
    public String getCall_activity_description() {
        return Call_activity_description;
    }

    public void setCall_activity_description(String Call_activity_description) {
        Call_activity_description = Call_activity_description;
    }






}
