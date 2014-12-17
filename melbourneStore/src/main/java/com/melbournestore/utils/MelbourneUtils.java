package com.melbournestore.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Area;
import com.melbournestore.models.Order;
import com.melbournestore.models.OrderItem;
import com.melbournestore.models.Order_user;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;
import com.melbournestore.models.Shop_iPhone;
import com.melbournestore.models.Suburb;
import com.melbournestore.models.User;
import com.melbournestore.models.item_iphone;
import com.melbournestore.models.number_price;
import com.melbournestore.models.user_iphone;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MelbourneUtils {

    public static final int sum(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static final int sum_price(int[] price, int[] number) {
        int sum = 0;

        for (int i = 0; i < price.length; i++) {
            sum += number[i] * price[i];
        }
        return sum;
    }

    public static final boolean checkExistSharedPrefrence(String name) {

        File f = new File(
                "/data/data/com.melbournestore.activities/shared_prefs/" + name
                        + ".xml"
        );
        if (f.exists())
            return true;
        else
            return false;

    }

    public static final int sum_number_all(Shop[] shops) {
        int num_all = 0;
        for (int i = 0; i < shops.length; i++) {
            for (int j = 0; j < shops[i].getPlates().length; j++) {
                num_all += shops[i].getPlates()[j].getNumber();
            }
        }
        return num_all;
    }

    public static final int sum_price_all(Shop[] shops) {
        int price_all = 0;
        for (int i = 0; i < shops.length; i++) {
            for (int j = 0; j < shops[i].getPlates().length; j++) {
                price_all += shops[i].getPlates()[j].getNumber()
                        * shops[i].getPlates()[j].getPrice();
            }
        }
        return price_all;
    }

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


    public static final void createLocalItems(Context context, ArrayList<item_iphone> items) {

        //HashMap<Integer, ArrayList<item_iphone>> shopItems = new HashMap<Integer, ArrayList<item_iphone>>();

        ArrayList<Integer> shopIds = new ArrayList<Integer>();

        for (int i = 0; i < items.size(); i++) {
            if (shopIds.contains(items.get(i).getShopId())) {
                shopIds.add(items.get(i).getShopId());

            }
        }


        Gson gson = new Gson();
        for (int i = 0; i < shopIds.size(); i++) {
            ArrayList<item_iphone> localItems = new ArrayList<item_iphone>();
            for (int j = 0; j < items.size(); j++) {
                if (shopIds.get(i) == items.get(j).getShopId()) {
                    localItems.add(items.get(j));
                }
            }
            SharedPreferenceUtils.saveLocalItems(context, gson.toJson(localItems), shopIds.get(i));
        }
    }

    public static final int sum_price_all(Plate[] plates) {
        int price_all = 0;
        for (int i = 0; i < plates.length; i++) {
            price_all += plates[i].getNumber() * plates[i].getPrice();
        }
        return price_all;
    }

    public static final float sum_price_items(OrderItem[] orders) {
        float price_all = 0;
        for (int i = 0; i < orders.length; i++) {
            price_all += orders[i].getPrice() * Float.parseFloat(orders[i].getCount());
        }
        return price_all;
    }

    public static final Plate[] getPlatesChosen(Shop[] shops) {

        ArrayList<Plate> plates_chosen = new ArrayList<Plate>();

        for (int i = 0; i < shops.length; i++) {
            for (int j = 0; j < shops[i].getPlates().length; j++) {
                if (shops[i].getPlates()[j].getNumber() > 0) {
                    plates_chosen.add(shops[i].getPlates()[j]);
                }
            }
        }

        Plate[] plates = plates_chosen.toArray(new Plate[0]);

        return plates;

    }

    public static final int getActiveUser(User[] users) {

        boolean active_found = false;
        int index = 0;

        if (users.length > 0) {
            for (int i = 0; i < users.length; i++) {
                if (users[i].getActive()) {
                    active_found = true;
                    index = i;
                    break;

                }
            }
            if (!active_found) {
                // no active user found
                return -2;
            } else {
                // found index of active user
                return index;
            }
        } else {
            // empty array
            return -1;
        }

    }

    public static final User[] setUsersDeactive(User[] users) {
        if (users.length > 0) {
            for (int i = 0; i < users.length; i++) {
                users[i].setActive(false);
            }
            return users;
        } else {
            return users;
        }
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

    public static final String getSystemTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static final String getAllPlateNames(Plate[] plates) {
        String plate_names = "";
        if (plates.length == 1) {
            plate_names = plates[0].getName();
        } else {
            for (int i = 0; i < (plates.length - 1); i++) {
                plate_names += plates[i].getName() + "、";
            }
            plate_names += plates[plates.length - 1].getName();
        }

        if (plate_names.length() >= 7) {
            plate_names = plate_names.substring(0, 7) + "...";
        }
        return plate_names;

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


    public static final User deleteOrder(User user, int position) {
        Order_user[] orders = user.getOrders();
        ArrayList<Order_user> orders_array = new ArrayList(Arrays.asList(orders));

        orders_array.remove(position);

        user.setOrders(orders_array.toArray(new Order_user[0]));

        return user;
    }

    public static final String getPostcode(String suburb) {
        if (Arrays.asList(DataResourceUtils.names_center).contains(suburb)) {
            return DataResourceUtils.post_center;
        } else if (Arrays.asList(DataResourceUtils.names_north).contains(suburb)) {
            return DataResourceUtils.post_north;
        } else if (Arrays.asList(DataResourceUtils.names_northeast).contains(suburb)) {
            return DataResourceUtils.post_northeast;
        } else if (Arrays.asList(DataResourceUtils.names_west).contains(suburb)) {
            return DataResourceUtils.post_west;
        } else if (Arrays.asList(DataResourceUtils.names_southeast).contains(suburb)) {
            return DataResourceUtils.post_southeast;
        } else {
            return "";
        }
    }

    public static final String getSuburbRegion(String suburb) {

        if (Arrays.asList(DataResourceUtils.names_center).contains(suburb)) {
            return "City";
        } else if (Arrays.asList(DataResourceUtils.names_north).contains(suburb)) {
            return "北";
        } else if (Arrays.asList(DataResourceUtils.names_northeast).contains(suburb)) {
            return "东北";
        } else if (Arrays.asList(DataResourceUtils.names_west).contains(suburb)) {
            return "西";
        } else if (Arrays.asList(DataResourceUtils.names_southeast).contains(suburb)) {
            return "东南";
        } else {
            return "";
        }
    }

    public static final int getSuburbDeliveryPrice(String suburb) {
        if (getSuburbRegion(suburb).equals("City")) {
            return 5;
        } else if (getSuburbRegion(suburb).equals("西")) {
            return 10;
        } else if (getSuburbRegion(suburb).equals("东北")) {
            return 5;
        } else if (getSuburbRegion(suburb).equals("北")) {
            return 10;
        } else if (getSuburbRegion(suburb).equals("东南")) {
            return 5;
        } else {
            return 0;
        }
    }

    public static final String[] getAllPlateNames() {


        String plates[] = new String[DataResourceUtils.plateNames[0].length + DataResourceUtils.plateNames[1].length + DataResourceUtils.plateNames[2].length];
        System.arraycopy(DataResourceUtils.plateNames[0], 0, plates, 0, DataResourceUtils.plateNames[0].length);
        System.arraycopy(DataResourceUtils.plateNames[1], 0, plates, DataResourceUtils.plateNames[0].length, DataResourceUtils.plateNames[1].length);
        System.arraycopy(DataResourceUtils.plateNames[2], 0, plates, DataResourceUtils.plateNames[0].length + DataResourceUtils.plateNames[1].length, DataResourceUtils.plateNames[2].length);
        return plates;
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
        Log.d("ITEMNAME", names);
        if (names.length() > 15) {
            names = names.substring(0, 15) + "...";
        }
        Log.d("ITEMNAME", names);
        return names;
    }

    public static final Area getAreaFromSuburb(Suburb suburb, Context context) {
        String mAreaString = SharedPreferenceUtils.getAreas(context);
        Log.d("AREA", mAreaString);
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
        Log.d("AREA", "marea name: " + mArea.getName());
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

    public static final Suburb getSuburbFromAreaNameAndSuburb(String area, String suburb, Context context){
        String areaString = SharedPreferenceUtils.getAreas(context);
        Type typeArea = new TypeToken<ArrayList<Area>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<Area> areas = gson.fromJson(areaString, typeArea);
        int area_index =0;
        for(int i=0;i<areas.size();i++){
            if(area.equals(areas.get(i))){
                area_index = i;
                break;
            }
        }
        int suburb_index =0;
        for(int i=0;i<areas.get(area_index).getSuburbs().size();i++){
            if(suburb.equals(areas.get(area_index).getSuburbs().get(i))){
                suburb_index = i;
                break;
            }
        }

        return areas.get(area_index).getSuburbs().get(suburb_index);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.i("NetWorkState", "Unavailabel");
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.i("NetWorkState", "Availabel");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

}
