package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengn on 2014/11/23.
 */
public class ItemGoodManagerThread extends Thread {

    Handler mHandler;
    Context mContext;
    Gson gson = new Gson();
    int mItemId;
    String mNumber;

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


    @Override
    public void run() {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        String result = handlePost(Constant.URL_BASE + "item/" + String.valueOf(mItemId)+"/user/"+mNumber, pairs);

        Log.d("GOODTHREAD", result);
//        item_iphone mItem = getItem(result);
//
//        String localItemsString = SharedPreferenceUtils.getLocalItems(mContext, mItem.getShopId());
//        Type type = new TypeToken<ArrayList<item_iphone>>() {}.getType();
//        ArrayList<item_iphone> localItems = gson.fromJson(localItemsString, type);
//        ArrayList<Integer> localItemIds = MelbourneUtils.getLocalItemsId(localItems);
//
//        if(!localItemIds.contains(mItem.getId())){
//            ArrayList<item_iphone> newItems = new ArrayList<item_iphone>();
//            newItems.clear();
//            newItems.addAll(localItems);
//            newItems.add(mItem);
//            SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(newItems), mItem.getShopId());
//        }
//
//        Message message = mHandler.obtainMessage();
//        message.obj = mItem;
//        try {
//            sleep(5);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        message.what = 0;
//        mHandler.sendMessage(message);
    }

}
