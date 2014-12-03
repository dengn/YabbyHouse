package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.item_iphone;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dengn on 2014/11/23.
 */
public class ItemManagerThread extends Thread {

    Handler mHandler;
    Context mContext;
    int mShopId;

    public ItemManagerThread(Handler handler, Context context, int shopId) {
        mHandler = handler;
        mContext = context;
        mShopId = shopId;
    }

    public static String handleGet(String strUrl) {
        String result = null;
        HttpGet request = new HttpGet(strUrl);//实例化get请求
        DefaultHttpClient client = new DefaultHttpClient();//实例化客户端
        try {
            HttpResponse response = client.execute(request);//执行该请求,得到服务器端的响应内容
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());//把响应结果转成String
            } else {
                result = response.getStatusLine().toString();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return result;
    }


    /**
     * Transform JSON String to shops
     */
    public static ArrayList<item_iphone> getItems(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, item_iphone[]>>() {
        }.getType();
        HashMap<String, item_iphone[]> mItems = gson.fromJson(jsonString, listType);
        item_iphone[] items = mItems.get("items");


        ArrayList<item_iphone> Items_array = new ArrayList<item_iphone>();

        for (int i = 0; i < items.length; i++) {
            Items_array.add(items[i]);
        }


        return Items_array;
    }


    @Override
    public void run() {

        String result = handleGet(Constant.URL_BASE + "shop/" + mShopId + "/items");


        Log.d("ITEMTHREAD", result);
        ArrayList<item_iphone> mItems = getItems(result);


        Gson gson = new Gson();
        String localItemsString = SharedPreferenceUtils.getLocalItems(mContext, mShopId);
        Type type = new TypeToken<ArrayList<item_iphone>>() {
        }.getType();
        ArrayList<item_iphone> localItems = gson.fromJson(localItemsString, type);

        Log.d("ITEMS", "mItems len: " + String.valueOf(mItems.size()));

        localItems = refreshLocalItems(localItems, mItems);
        SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(localItems), mShopId);

        Log.d("ITEMS", "localItems len: " + String.valueOf(localItems.size()));

        Message message = mHandler.obtainMessage();
        message.obj = localItems;
        message.what = 0;
        mHandler.sendMessage(message);
    }

    private ArrayList<item_iphone> refreshLocalItems(ArrayList<item_iphone> localItems, ArrayList<item_iphone> remoteItems) {


        if (localItems != null) {
            if (localItems.size() != 0) {
                //find out the already chosen ones
                HashMap<Integer, Integer> chosenItemsIds = new HashMap<Integer, Integer>();
                for (int i = 0; i < localItems.size(); i++) {
                    if (localItems.get(i).getUnit() > 0) {
                        chosenItemsIds.put(localItems.get(i).getId(), localItems.get(i).getUnit());
                    }
                }

                ArrayList<item_iphone> returnedItems = new ArrayList<item_iphone>();

                //copy remote items to local, and update their units with local ones
                for (int i = 0; i < remoteItems.size(); i++) {

                    if (chosenItemsIds.size() > 0) {
                        item_iphone chosenItem = new item_iphone();


                        //if the same item exists in remote items
                        if (chosenItemsIds.containsKey(remoteItems.get(i).getId())) {
                            chosenItem = remoteItems.get(i);
                            chosenItem.setUnit(chosenItemsIds.get(remoteItems.get(i).getId()));
                            returnedItems.add(chosenItem);

                        } else {
                            chosenItem = remoteItems.get(i);
                            returnedItems.add(chosenItem);
                        }


                    } else {
                        returnedItems.add(remoteItems.get(i));
                    }
                }

                //Log.d("ITEMS", "returnedItems len: " + String.valueOf(returnedItems));

                return returnedItems;
            } else {
                return remoteItems;
            }
        } else {
            return remoteItems;
        }
    }

}
