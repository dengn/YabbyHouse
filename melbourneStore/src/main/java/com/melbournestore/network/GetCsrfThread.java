package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by dengn on 2014/12/3.
 */
public class GetCsrfThread extends Thread{

    Handler mHandler;
    Context mContext;
    Gson gson = new Gson();



    public GetCsrfThread(Handler handler, Context context) {
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





    @Override
    public void run() {


        String csrf = handleGet(Constant.URL_BASE + "create_order");

        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> csrf_hash = gson.fromJson(csrf, type);

        String mCsrf = csrf_hash.get("csrf");
        //SharedPreferenceUtils.saveCsrf(mContext, mCsrf);
        Log.d("CREATEORDERTHREAD", "mCsrf: " + mCsrf);


    }
}
