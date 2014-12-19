package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Shop_iPhone;
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
public class ShopManagerThread extends Thread {

    private static final boolean DEBUG = false;

    private Handler mHandler;

    private Context mContext;

    public ShopManagerThread(Handler handler, Context context) {
        mHandler = handler;
        mContext = context;
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
        String result = handleGet(Constant.URL_BASE + "shops");


        if(DEBUG)
        Log.d("THREAD", result);
        ArrayList<Shop_iPhone> mShops = getShops(result);

        Gson gson = new Gson();
        SharedPreferenceUtils.saveLocalShops(mContext, gson.toJson(mShops));

        //if it's the first time to launch
        if (SharedPreferenceUtils.getFirstTimeLaunch(mContext)) {
            SharedPreferenceUtils.saveFirstTimeLaunch(mContext);
            for (int i = 0; i < mShops.size(); i++) {
                ArrayList<item_iphone> items = new ArrayList<item_iphone>();
                SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(items), mShops.get(i).getId());
            }
        }


        Message message = mHandler.obtainMessage();
        message.obj = mShops;
        message.what = 0;
        mHandler.sendMessage(message);
    }

}
