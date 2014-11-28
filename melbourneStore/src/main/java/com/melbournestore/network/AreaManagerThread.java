package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.Area;
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
public class AreaManagerThread extends Thread {

    Handler mHandler;

    Context mContext;

    public AreaManagerThread(Handler handler, Context context) {
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
     * Transform JSON String to Area
     */
    public static ArrayList<Area> getAreas(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, Area[]>>() {
        }.getType();
        HashMap<String, Area[]> mAreas = gson.fromJson(jsonString, listType);
        Area[] areas = mAreas.get("area_list");


        ArrayList<Area> Areas_array = new ArrayList<Area>();

        for (int i = 0; i < areas.length; i++) {
            Areas_array.add(areas[i]);
        }


        return Areas_array;
    }

    @Override
    public void run() {
        String result = handleGet(Constant.URL_BASE+"areas");


        Log.d("THREAD", result);
        ArrayList<Area> mAreas = getAreas(result);



        Message message = mHandler.obtainMessage();
        message.obj = mAreas;
        try {
            sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        message.what = 0;
        mHandler.sendMessage(message);
    }

}
