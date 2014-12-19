package com.melbournestore.db;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    // The SharedPrefrence file to know if the application is the first time used
    public static boolean getFirstTimeLaunch(Context context) {
        Boolean isFirstIn = false;
        SharedPreferences pref = context.getSharedPreferences("yabbyhouse",
                0);
        isFirstIn = pref.getBoolean("isFirstIn", true);
        return isFirstIn;
    }

    public static boolean saveFirstTimeLaunch(Context context) {
        SharedPreferences pref = context.getSharedPreferences("yabbyhouse", 0);
        return pref.edit().putBoolean("isFirstIn", false).commit();
    }


    // The SharedPrefrence file to store current local items
    public static String getLocalItems(Context context, int shopId) {
        SharedPreferences pref = context.getSharedPreferences("local_items" + String.valueOf(shopId),
                0);
        return pref.getString("local_items" + String.valueOf(shopId), "");
    }

    public static boolean saveLocalItems(Context context, String info, int shopId) {
        SharedPreferences pref = context.getSharedPreferences("local_items" + String.valueOf(shopId),
                0);
        return pref.edit().putString("local_items" + String.valueOf(shopId), info).commit();
    }


    // The SharedPrefrence file to store current local shops
    public static String getLocalShops(Context context) {
        SharedPreferences pref = context.getSharedPreferences("local_shops",
                0);
        return pref.getString("local_shops", "");
    }

    public static boolean saveLocalShops(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("local_shops",
                0);
        return pref.edit().putString("local_shops", info).commit();
    }


    // The SharedPrefrence file to store User login info
    public static String getLoginUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences("user_login", 0);
        return pref.getString("user_login", "");
    }

    public static boolean saveLoginUser(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("user_login", 0);
        return pref.edit().putString("user_login", info).commit();
    }

    // The SharedPrefrence file to store User Password
    public static String getUserPassword(Context context) {
        SharedPreferences pref = context.getSharedPreferences("user_password", 0);
        return pref.getString("user_password", "");
    }

    public static boolean saveUserPassword(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("user_password", 0);
        return pref.edit().putString("user_password", info).commit();
    }

    // The SharedPrefrence file to store User Phone Number
    public static String getUserNumber(Context context) {
        SharedPreferences pref = context.getSharedPreferences("user_number", 0);
        return pref.getString("user_number", "");
    }

    public static boolean saveUserNumber(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("user_number", 0);
        return pref.edit().putString("user_number", info).commit();
    }

    // The SharedPrefrence file to store Areas
    public static String getAreas(Context context) {
        SharedPreferences pref = context.getSharedPreferences("areas", 0);
        return pref.getString("areas", "");
    }

    public static boolean saveAreas(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("areas", 0);
        return pref.edit().putString("areas", info).commit();
    }



    // The SharedPrefrence file to store Coupons
    public static String getUserCoupons(Context context) {
        SharedPreferences pref = context.getSharedPreferences("coupons", 0);
        return pref.getString("coupons", "");
    }

    public static boolean saveUserCoupons(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("coupons", 0);
        return pref.edit().putString("coupons", info).commit();
    }

    // The SharedPrefrence file to store DeliveryTime
    public static String getDeliveryTime(Context context) {
        SharedPreferences pref = context.getSharedPreferences("deliveryTime", 0);
        return pref.getString("deliveryTime", "");
    }

    public static boolean saveDeliveryTime(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("deliveryTime", 0);
        return pref.edit().putString("deliveryTime", info).commit();
    }


    // The SharedPrefrence file to store Remark
    public static String getRemark(Context context) {
        SharedPreferences pref = context.getSharedPreferences("remark", 0);
        return pref.getString("remark", "");
    }

    public static boolean saveRemark(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("remark", 0);
        return pref.edit().putString("remark", info).commit();
    }

    // The SharedPrefrence file to store Contact Number
    public static String getContactNumber(Context context) {
        SharedPreferences pref = context.getSharedPreferences("contactNumber", 0);
        return pref.getString("contactNumber", "");
    }

    public static boolean saveContactNumber(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("contactNumber", 0);
        return pref.edit().putString("contactNumber", info).commit();
    }




}
