package com.melbournestore.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.melbournestore.models.Order_user;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;

public class SharedPreferenceUtils {

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
