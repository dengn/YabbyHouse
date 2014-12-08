package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by OLEDCOMM on 03/12/2014.
 */
public class UploadImageManagerThread extends Thread{
    Handler mHandler;
    Context mContext;
    String mNumber;
    MultipartEntityBuilder mMultipartEntity;
    Gson gson = new Gson();


    public UploadImageManagerThread(Handler handler, Context context, String number, MultipartEntityBuilder multipartEntity) {
        mHandler = handler;
        mContext = context;
        mNumber = number;
        mMultipartEntity = multipartEntity;
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


    public String handlePost(String strUrl, List<NameValuePair> params) {
        String result = null;
        HttpPost request = new HttpPost(strUrl);//实例化get请求
        DefaultHttpClient client = new DefaultHttpClient();//实例化客户端

        try {
            request.setEntity(mMultipartEntity.build());
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

    public static String handlePut(String strUrl, List<NameValuePair> params) {
        String result = null;
        HttpPut request = new HttpPut(strUrl);//实例化get请求
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
        pairs.add(new BasicNameValuePair("number", mNumber));

        //String result = handlePut(Constant.URL_BASE+"user/verification", pairs);
        String result = handlePost(Constant.URL_BASE + "user/upload/headicon", pairs);


        Log.d("UPLOADTHREAD", result);

        if(result.contains("head_icon")){
            HashMap<String, String> headIcon = new HashMap<String, String>();
            Gson gson = new Gson();
            Type listType = new TypeToken<HashMap<String, String>>() {
            }.getType();
            headIcon = gson.fromJson(result, listType);
            Message msg = mHandler.obtainMessage();
            msg.obj = headIcon.get("head_icon");
            msg.what = 2;
            mHandler.sendMessage(msg);
        }else{
            Message msg = mHandler.obtainMessage();
            msg.what = 3;
            mHandler.sendMessage(msg);
        }



    }

}
