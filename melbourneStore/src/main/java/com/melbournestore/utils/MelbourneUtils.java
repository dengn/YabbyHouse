package com.melbournestore.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Area;
import com.melbournestore.models.Order;
import com.melbournestore.models.OrderItem;
import com.melbournestore.models.Shop_iPhone;
import com.melbournestore.models.Suburb;
import com.melbournestore.models.item_iphone;
import com.melbournestore.models.number_price;
import com.melbournestore.models.user_iphone;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MelbourneUtils {


    //sum the number and price of items
    public static final number_price sum_item_number_price(Context context) {
        Gson gson = new Gson();
        String shopsString = SharedPreferenceUtils.getLocalShops(context);
        Type type = new TypeToken<ArrayList<Shop_iPhone>>() {
        }.getType();
        ArrayList<Shop_iPhone> shops = gson.fromJson(shopsString, type);

        int item_numbers = 0;
        float item_prices = 0;
        for (int i = 0; i < shops.size(); i++) {
            String itemsString = SharedPreferenceUtils.getLocalItems(context, shops.get(i).getId());
            type = new TypeToken<ArrayList<item_iphone>>() {
            }.getType();
            ArrayList<item_iphone> items = gson.fromJson(itemsString, type);
            for (int j = 0; j < items.size(); j++) {

                item_numbers += items.get(j).getUnit();
                item_prices += items.get(j).getUnit() * Float.parseFloat(items.get(j).getPrice());
            }
        }
        number_price sumNumberPrice = new number_price(item_numbers, item_prices);
        return sumNumberPrice;
    }

    //Find all chosenItems
    public static final ArrayList<item_iphone> getAllChosenItems(Context context) {

        Gson gson = new Gson();
        String shopsString = SharedPreferenceUtils.getLocalShops(context);
        Type type = new TypeToken<ArrayList<Shop_iPhone>>() {
        }.getType();
        ArrayList<Shop_iPhone> shops = gson.fromJson(shopsString, type);

        ArrayList<item_iphone> chosenItems = new ArrayList<item_iphone>();
        for (int i = 0; i < shops.size(); i++) {
            String itemsString = SharedPreferenceUtils.getLocalItems(context, shops.get(i).getId());
            type = new TypeToken<ArrayList<item_iphone>>() {
            }.getType();
            ArrayList<item_iphone> items = gson.fromJson(itemsString, type);

            for (int j = 0; j < items.size(); j++) {
                if (items.get(j).getUnit() > 0) {
                    chosenItems.add(items.get(j));
                }
            }
        }
        return chosenItems;
    }

    //Update the local item units to fetched ones
    public static final item_iphone updateItemUnits(Context context, item_iphone item) {
        Gson gson = new Gson();
        String itemsString = SharedPreferenceUtils.getLocalItems(context, item.getShopId());
        Type type = new TypeToken<ArrayList<item_iphone>>() {
        }.getType();
        ArrayList<item_iphone> items = gson.fromJson(itemsString, type);
        for (int i = 0; i < items.size(); i++) {
            if (item.getId() == items.get(i).getId()) {
                item.setUnit(items.get(i).getUnit());
                break;
            }
        }
        return item;
    }

    public static final ArrayList<Integer> getLocalItemsId(ArrayList<item_iphone> items) {
        ArrayList<Integer> itemIds = new ArrayList<Integer>();
        for (int i = 0; i < items.size(); i++) {
            itemIds.add(items.get(i).getId());
        }
        return itemIds;
    }




    public static final float sum_price_items(OrderItem[] orders) {
        float price_all = 0;
        for (int i = 0; i < orders.length; i++) {
            price_all += orders[i].getPrice() * Float.parseFloat(orders[i].getCount());
        }
        return price_all;
    }





    public static final String getCompleteAddress(user_iphone user) {
        String address = "";
        if (!user.getUnitNo().equals("") || !user.getStreet().equals("")
                || !user.getSuburb().getName().equals("")) {
            address = user.getUnitNo() + " " + user.getStreet() + ","
                    + user.getSuburb().getName();
        }
        return address;
    }

    public static final String getCompleteAddress(Order order) {
        String address = "";
        if (!order.getUnitNo().equals("") || !order.getStreet().equals("")
                || !order.getSuburb().getName().equals("")) {
            address = order.getUnitNo() + " " + order.getStreet() + ","
                    + order.getSuburb().getName();
        }
        return address;
    }



    public static final String getStatusString(int status) {
        if (status == 0) {
            return new String("待确认");
        } else if (status == 1) {
            return new String("待配送");
        } else if (status == 2) {
            return new String("准备中");
        } else if (status == 3) {
            return new String("配送中");
        } else if (status == 4) {
            return new String("已完成");
        } else {
            return new String("");
        }
    }

    public static final String getCurrentOrderStatusString(int status) {
        String string = "";
        switch (status) {
            case 0:
                string = new String("订单已提交");
                break;
            case 1:
                string = new String("订单已确认");
                break;
            case 2:
                string = new String("订单准备中");
                break;
            case 3:
                string = new String("订单配送中");
                break;
            case 4:
                string = new String("订单已完成");
                break;

        }
        return string;
    }







    public static final String getAllItemsNames(OrderItem[] items) {
        String names = "";
        if (items.length == 1) {
            names = items[0].getName();

        } else if (items.length > 1) {
            for (int i = 0; i < items.length - 1; i++) {
                names += items[i].getName() + "、";
            }
            names += items[items.length - 1].getName();
        }

        if (names.length() > 15) {
            names = names.substring(0, 15) + "...";
        }

        return names;
    }

    public static final Area getAreaFromSuburb(Suburb suburb, Context context) {
        String mAreaString = SharedPreferenceUtils.getAreas(context);

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Area>>() {
        }.getType();
        ArrayList<Area> areas = gson.fromJson(mAreaString, listType);
        Area mArea = new Area();
        for (int i = 0; i < areas.size(); i++) {
            for (int j = 0; j < areas.get(i).getSuburbs().size(); j++) {


                if (suburb.getName().equals(areas.get(i).getSuburbs().get(j).getName())) {
                    mArea = areas.get(i);
                }
            }
        }

        return mArea;
    }

    public static final String getShopNameFromItemId(int itemId, Context context) {
        String shopsString = SharedPreferenceUtils.getLocalShops(context);
        Type type_shop = new TypeToken<ArrayList<Shop_iPhone>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<Shop_iPhone> shops = gson.fromJson(shopsString, type_shop);
        int index = 0;
        LOOP:
        for (int i = 0; i < shops.size(); i++) {
            String itemsString = SharedPreferenceUtils.getLocalItems(context, shops.get(i).getId());
            Type type_item = new TypeToken<ArrayList<item_iphone>>() {
            }.getType();
            ArrayList<item_iphone> items = gson.fromJson(itemsString, type_item);
            for (int j = 0; j < items.size(); j++) {
                if (itemId == items.get(j).getId()) {
                    index = i;
                    break LOOP;
                }
            }
        }
        return shops.get(index).getName();
    }



}
