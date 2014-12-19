package com.melbournestore.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.advertisements;
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
public class RecommandationManagerThread extends Thread {

    private static final boolean DEBUG = false;
    private Handler mHandler;

    public RecommandationManagerThread(Handler handler) {
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
     * Transform JSON String to advertisement
     */
    public static ArrayList<advertisements> getAdvertisement(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, advertisements[]>>() {
        }.getType();
        HashMap<String, advertisements[]> mAds = gson.fromJson(jsonString, listType);
        advertisements[] ads = mAds.get("advertisements");


        ArrayList<advertisements> Ads_array = new ArrayList<advertisements>();

        for (int i = 0; i < ads.length; i++) {
            Ads_array.add(ads[i]);
        }


        return Ads_array;
    }

    @Override
    public void run() {

        String result = handleGet(Constant.URL_BASE + "advertisements");

        if (DEBUG)
            Log.d("THREAD", result);
        if (DEBUG)
            Log.d("THREAD", "result string len: " + String.valueOf(result.length()));
        ArrayList<advertisements> mAd = getAdvertisement(result);

        Message message = mHandler.obtainMessage();
        message.obj = mAd;
        mHandler.sendMessage(message);
    }

}
