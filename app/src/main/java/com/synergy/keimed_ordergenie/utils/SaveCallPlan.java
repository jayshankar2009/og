package com.synergy.keimed_ordergenie.utils;

import android.app.Activity;
import android.util.Log;

/**
 * Created by 1132 on 07-02-2017.
 */
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import com.synergy.keimed_ordergenie.app.AppConfig;

public class SaveCallPlan {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public SaveCallPlan(Activity act,String call_date,String StockistCallPlanID,String ChemistID,String user_id,String InTime,String OutTime,String CallPlanStatus,
                     String task_json)
    {
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("Date", call_date);
            jsonParams.put("StockistCallPlanID", StockistCallPlanID);
            jsonParams.put("ChemistID", ChemistID);
            jsonParams.put("UserID", user_id);
            jsonParams.put("InTime", InTime);
            jsonParams.put("OutTime", OutTime);
            jsonParams.put("Status", CallPlanStatus);
            jsonParams.put("TaskStatus", task_json);
       //   Log.e("jsonParams",jsonParams.toString());
            //globalVariable.setToken(null);
            MakeWebRequest.MakeWebRequest("Post",AppConfig.POST_SAVE_CALL_PLAN, AppConfig.POST_SAVE_CALL_PLAN, jsonParams, act, true);
        }catch (Exception e)
        {
            e.toString();
        }
    }
}
