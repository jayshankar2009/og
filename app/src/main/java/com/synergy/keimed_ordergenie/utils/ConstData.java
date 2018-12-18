package com.synergy.keimed_ordergenie.utils;


import android.database.Cursor;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConstData {

    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }


    public static String getValueOrDefault(String value, String defaultValue) {
        return isNotNullOrEmpty(value) ? value : defaultValue;
    }

    private static boolean isNotNullOrEmpty(String str) {
        return (str != null && !str.isEmpty());
    }

    public static JSONArray get_jsonArray_from_cursor(Cursor cursor) {
        JSONArray array = new JSONArray();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                JSONObject object = new JSONObject();
                try {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        object.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return array;
    }


    public class user_info {
        public static final String CLIENT_USERNAME = "sdadaswe";
        public static final String CLIENT_PASSWORD = "adadadaewtry";
        public static final String CLIENT_LONGITUDE = "Longitude";
        public static final String CLIENT_LATITUDE = "Latitude";
        public static final String CHEMIST_ID = "Chemist_id";
        public static final String CLIENT_ID = "client_id";
        public static final String STOCKIST_ID = "Stockist_id";
        public static final String USER_ID = "user_id";
        public static final String EMAIL_ID = "email_id";
        public static final String CLIENT_ROLE = "client_role";
        public static final String LEGEND_MODE ="legend_mode";
        public static final String CLIENT_MOBILE = "client_mobile";
        public static final String CLIENT_PROFILE_IMAGE = "client_profile_image";
        public static final String CLIENT_NAME = "client_name";

        public static final String CLIENT_FULL_NAME = "client_full_name";
        public static final String CLIENT_CITY_ID = "client_city_id";
        public static final String CLIENT_ADRESS = "client_address";
        public static final String CLIENT_ROLE_ID = "client_role_id";
        public static final String CLIENT_LOGIN_NAME = "login_name";
        public static final String CLIENT_IS_DUMMY_PASS = "is_dummy_pass";


        public static final String IS_ORDER_DELIVERY_ASSIGNED = "IS_ORDER_DELIVERY_ASSIGNED";
        public static final String IS_PAYMENT_COLLECTION_ASSIGNED = "IS_PAYMENT_COLLECTION_ASSIGNED";
        public static final String IS_TAKE_ORDER_ASSIGNED = "IS_TAKE_ORDER_ASSIGNED";

        public static final String IS_ORDER_DELIVERY_ASSIGN = "IS_DELIVERY";
        public static final String IS_PAYMENT_COLLECTION_ASSIGN = "IS_PAYMENT";
        public static final String IS_TAKE_ORDER_ASSIGN = "IS_ORDER";

        public static final String IS_USER_ORDER = "USER_TAKE_ORDER";
        public static final String IS_USER_PAYMENT = "USER_PAYMENT_ORDER";
        public static final String IS_USER_DELIVERY = "USER_DELIVERY_ORDER";

    }

    public class Login_type {
        public static final String STOCKIST = "stockist";
        public static final String CHEMIST = "chemist";
        public static final String Salesname = "user";
    }

    public class data_refreshing {
        public static final String CHEMIST_STOCKISTLIST = "chemist_stockistlist";

        public static final String CHEMIST_ORDERLIST = "chemist_orderlist";
        public static final String CHEMIST_PENDING_BILLS = "chemist_pending_bills";

        public static final String CHEMIST_LAST_DATA_SYNC = "chemist_last_data_sync";
    }


    public static String encrypt(String input) {
        // Simple encryption, not very strong!
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}


