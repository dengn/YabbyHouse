package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.item_iphone;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dengn on 2014/11/23.
 */
public class ItemGoodManagerThread extends Thread {

    private static final boolean DEBUG = false;

    private Handler mHandler;
    private Context mContext;
    private Gson gson = new Gson();
    private int mItemId;
    private String mNumber;

    public ItemGoodManagerThread(Handler handler, Context context, int itemId, String number) {
        mHandler = handler;
        mContext = context;
        mItemId = itemId;
        mNumber = number;
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

    public static String handlePost(String strUrl, List<NameValuePair> params) {
        String result = null;
        HttpPost request = new HttpPost(strUrl);//实例化get请求
        DefaultHttpClient client = new DefaultHttpClient();//实例化客户端

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
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

    public static item_iphone getItem(String jsonString) {
        jsonString = jsonString.substring(0, jsonString.length() - 14) + "}";
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, item_iphone>>() {
        }.getType();
        HashMap<String, item_iphone> mItem = gson.fromJson(jsonString, listType);
        item_iphone item = mItem.get("item");


        return item;
    }

    @Override
    public void run() {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        String result = handlePost(Constant.URL_BASE + "item/" + String.valueOf(mItemId) + "/user/" + mNumber, pairs);

        if(DEBUG)
            Log.d("GOODTHREAD", result);

        if (result.equals("{\"result\": 1}")) {
            Message msg = mHandler.obtainMessage();
            msg.what = 3;
            mHandler.sendMessage(msg);
        } else if (result.contains("item")) {

            Message msg = mHandler.obtainMessage();
            msg.obj = getItem(result);
            msg.what = 4;
            mHandler.sendMessage(msg);

        }

    }

}
