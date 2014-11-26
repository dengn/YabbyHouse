package com.melbournestore.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.item_iphone;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by dengn on 2014/11/23.
 */
public class SingleItemManagerThread extends Thread {

    Handler mHandler;
    int mItemId;

    public SingleItemManagerThread(Handler handler, int itemId) {
        mHandler = handler;
        mItemId = itemId;
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
    public static item_iphone getItem(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, item_iphone>>() {
        }.getType();
        HashMap<String, item_iphone> mItem = gson.fromJson(jsonString, listType);
        item_iphone item = mItem.get("item");


        return item;
    }

    @Override
    public void run() {

        String result = handleGet(Constant.URL_BASE + "item/" + mItemId);

        Log.d("ITEMTHREAD", result);
        item_iphone mItem = getItem(result);

        Message message = mHandler.obtainMessage();
        message.obj = mItem;
        try {
            sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mHandler.sendMessage(message);
    }

}
