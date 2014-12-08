package com.melbournestore.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.melbournestore.models.Order_user;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;

public class SharedPreferenceUtils {

    // The SharedPrefrence file to know if the application is the first time used
    public static boolean getFirstTimeLaunch(Context context) {
        Boolean isFirstIn = false;
        SharedPreferences pref = context.getSharedPreferences("yabbyhouse",
                0);
        isFirstIn = pref.getBoolean("isFirstIn", true);
        return isFirstIn;
    }

    public static boolean saveFirstTimeLaunch(Context context){
        SharedPreferences pref = context.getSharedPreferences("yabbyhouse", 0);
        return pref.edit().putBoolean("isFirstIn", false).commit();
    }


    // The SharedPrefrence file to store current local items
    public static String getLocalItems(Context context, int shopId) {
        SharedPreferences pref = context.getSharedPreferences("local_items"+String.valueOf(shopId),
                0);
        return pref.getString("local_items"+String.valueOf(shopId), "");
    }

    public static boolean saveLocalItems(Context context, String info, int shopId) {
        SharedPreferences pref = context.getSharedPreferences("local_items"+String.valueOf(shopId),
                0);
        return pref.edit().putString("local_items"+String.valueOf(shopId), info).commit();
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

    // The SharedPrefrence file to store current choosed flates, which are not
    // added to Shopping cart.
    public static String getCurrentChoice(Context context) {
        SharedPreferences pref = context.getSharedPreferences("current_choice",
                0);
        return pref.getString("current_choice", "");
    }

    public static boolean saveCurrentChoice(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("current_choice",
                0);
        return pref.edit().putString("current_choice", info).commit();
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
    public static String getUserPassword(Context context){
        SharedPreferences pref = context.getSharedPreferences("user_password", 0);
        return pref.getString("user_password", "");
    }

    public static boolean saveUserPassword(Context context, String info){
        SharedPreferences pref = context.getSharedPreferences("user_password", 0);
        return pref.edit().putString("user_password", info).commit();
    }

    // The SharedPrefrence file to store User Phone Number
    public static String getUserNumber(Context context){
        SharedPreferences pref = context.getSharedPreferences("user_number", 0);
        return pref.getString("user_number", "");
    }

    public static boolean saveUserNumber(Context context, String info){
        SharedPreferences pref = context.getSharedPreferences("user_number", 0);
        return pref.edit().putString("user_number", info).commit();
    }

    // The SharedPrefrence file to store Areas
    public static String getAreas(Context context){
        SharedPreferences pref = context.getSharedPreferences("areas", 0);
        return pref.getString("areas", "");
    }

    public static boolean saveAreas(Context context, String info){
        SharedPreferences pref = context.getSharedPreferences("areas", 0);
        return pref.edit().putString("areas", info).commit();
    }


    // The SharedPrefrence file to store Csrf
    public static String getCsrf(Context context){
        SharedPreferences pref = context.getSharedPreferences("csrf", 0);
        return pref.getString("csrf", "");
    }

    public static boolean saveCsrf(Context context, String info){
        SharedPreferences pref = context.getSharedPreferences("csrf", 0);
        return pref.edit().putString("csrf", info).commit();
    }



    // The SharedPrefrence file to store Orders.
    public static String getCurrentOrder(Context context) {
        SharedPreferences pref = context.getSharedPreferences("current_order",
                0);
        return pref.getString("current_order", "");
    }

    public static boolean saveCurrentOrder(Context context, String info) {
        SharedPreferences pref = context.getSharedPreferences("current_order",
                0);
        return pref.edit().putString("current_order", info).commit();
    }


    // The SharedPrefrence file to store Coupons
    public static String getUserCoupons(Context context){
        SharedPreferences pref = context.getSharedPreferences("coupons", 0);
        return pref.getString("coupons", "");
    }

    public static boolean saveUserCoupons(Context context, String info){
        SharedPreferences pref = context.getSharedPreferences("coupons", 0);
        return pref.edit().putString("coupons", info).commit();
    }


    public static void SharedPreferenceClearCurrentChoice(Context context) {
        SharedPreferences current_choice = context.getSharedPreferences(
                "current_choice", 0);
        SharedPreferences.Editor current_choice_editor = current_choice.edit();
        current_choice_editor.clear();
        current_choice_editor.commit();

    }

    public static void SharedPreferenceClearCurrentOrder(Context context) {
        SharedPreferences current_order = context.getSharedPreferences(
                "current_order", 0);
        SharedPreferences.Editor current_order_editor = current_order.edit();
        current_order_editor.clear();
        current_order_editor.commit();

    }

    public static void SharedPreferenceLoginUser(Context context) {
        SharedPreferences login_user = context.getSharedPreferences(
                "user_login", 0);
        SharedPreferences.Editor login_user_editor = login_user.edit();
        login_user_editor.clear();
        login_user_editor.commit();

    }

    public static void setUpCurrentOrder(Context context) {

        Gson gson = new Gson();
        Order_user order = new Order_user();

        order.setDeliveryTime("");
        order.setRemark("");

        SharedPreferenceUtils.saveCurrentOrder(context, gson.toJson(order));

    }

    public static void setUpCurrentChoice(Context context) {
        Shop[] shops = new Shop[DataResourceUtils.shopItems.length];

        for (int i = 0; i < shops.length; i++) {

            Shop shop = new Shop();

            Plate[] plates = new Plate[DataResourceUtils.plateNames[i].length];

            for (int j = 0; j < plates.length; j++) {
                Plate plate = new Plate();

                plate.setName(DataResourceUtils.plateNames[i][j]);
                plate.setPrice(DataResourceUtils.platePrices[i][j]);
                plate.setNumber(0);
                plate.setStockMax(DataResourceUtils.plateStockMax[i][j]);
                plate.setLikeNum(DataResourceUtils.plateLikeNumbers[i][j]);
                plate.setImageId(DataResourceUtils.plateImages[i][j]);
                plate.setShopId(i);
                plate.setPlateId(j);

                plates[j] = plate;
            }
            shop.setPlates(plates);
            shops[i] = shop;
        }

        Gson gson = new Gson();
        String shopsJson = gson.toJson(shops);

        SharedPreferenceUtils.saveCurrentChoice(context, shopsJson);

    }





}
