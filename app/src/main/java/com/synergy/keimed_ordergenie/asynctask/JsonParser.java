package com.synergy.keimed_ordergenie.asynctask;

import android.content.Context;
import android.util.Log;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lenovo on 3/17/2018.
 */

public class JsonParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    JSONArray jsonArray, result;
    int timeout1 = 1000*2;
    int timeout2 = 1000*2;
    // constructor
    Context context;

    public JsonParser() {

    }

    public JSONArray getJSONFromUrl(JSONArray data, String requesturl,
                                     String get, String accessKey) {

        // Making HTTP request
        String result = "";

        Log.d("requesturl = ", requesturl);

        Log.d("pOST_METHOD = ", get);


        try {
            // defaultHttpClient
            URL url = new URL(requesturl);
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout1);
            HttpConnectionParams.setSoTimeout(httpParameters, timeout2);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(20000);
            connection.setRequestMethod(get);
            connection.setRequestProperty("content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + accessKey);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream dom = new DataOutputStream(
                    connection.getOutputStream());
            dom.writeBytes(data.toString());
            dom.flush();
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            dom.close();
            reader.close();
            Log.d("SERVICE RESPONSE: ", answer.toString());
            result = answer.toString();

            try {
                JSONArray jsonArray = new JSONArray(result);
                Log.i("JSONresponseText", jsonArray.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Log.d("JSON RESPONSE = ", jsonArray.toString());
        return jsonArray;

    }
}
