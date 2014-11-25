package com.melbournestore.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.Shop_iPhone;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dengn on 2014/11/23.
 */
public class ShopManagerThread extends Thread {

    Handler mHandler;

    public ShopManagerThread(Handler handler) {
        mHandler = handler;
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
     * 将JSON字符串转换为Map
     */
    public static Map<String, Object> getMap(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Transform JSON String to shops
     */
    public static ArrayList<Shop_iPhone> getShops(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, Shop_iPhone[]>>() {
        }.getType();
        HashMap<String, Shop_iPhone[]> mShops = gson.fromJson(jsonString, listType);
        Shop_iPhone[] shops = mShops.get("shops");


        ArrayList<Shop_iPhone> Shops_array = new ArrayList<Shop_iPhone>();

        for (int i = 0; i < shops.length; i++) {
            Shops_array.add(shops[i]);
        }


        return Shops_array;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        String result = handleGet("http://106.187.34.107/api/shops");

        Log.d("THREAD", result);
        ArrayList<Shop_iPhone> mAd = getShops(result);

        Message message = mHandler.obtainMessage();
        message.obj = mAd;
        try {
            sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mHandler.sendMessage(message);
    }

}
