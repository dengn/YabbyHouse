package com.melbournestore.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
        HttpGet request = new HttpGet(strUrl);//ʵ����get����
        DefaultHttpClient client = new DefaultHttpClient();//ʵ�����ͻ���
        try {
            HttpResponse response = client.execute(request);//ִ�и�����,�õ��������˵���Ӧ����
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());//����Ӧ���ת��String
            } else {
                result = response.getStatusLine().toString();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return result;
    }

    /**
     * ��JSON�ַ���ת��ΪMap
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

    @Override
    public void run() {
        // TODO Auto-generated method stub
        String result = handleGet("http://106.187.34.107/api/shops");

        Log.d("THREAD", result);
        Map map = getMap(result);

        Message message = mHandler.obtainMessage();
        message.obj = map.get("book");
        try {
            sleep(30000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mHandler.sendMessage(message);
    }

}
