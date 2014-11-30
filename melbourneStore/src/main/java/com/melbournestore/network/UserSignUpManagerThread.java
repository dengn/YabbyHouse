package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengn on 2014/11/23.
 */
public class UserSignUpManagerThread extends Thread {

    Handler mHandler;
    Context mContext;
    String mNumber;
    String mCode;
    String mPassword;
    Gson gson = new Gson();


    public UserSignUpManagerThread(Handler handler, Context context, String number, String code, String password) {
        mHandler = handler;
        mContext = context;
        mNumber = number;
        mCode = code;
        mPassword = password;
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
        pairs.add(new BasicNameValuePair("code", mCode));
        //valid verification code
        String result = handlePost(Constant.URL_BASE + "user/verification", pairs);
        Log.d("USERTHREAD", "verification: " + result);

        String verificationError = "{\"message\": \"\\u9a8c\\u8bc1\\u7801\\u4e0d\\u6b63\\u786e\", \"result\": 400}";

        //TODO
        if (!result.equals(verificationError)) {
            //valid success
            pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("number", mNumber));
            pairs.add(new BasicNameValuePair("password", mPassword));
            String signUpResult = handlePost(Constant.URL_BASE + "user", pairs);
            Log.d("USERTHREAD", "sign up: " + signUpResult);
            if (!result.equals("HTTP/1.1 404 NOT FOUND")) {
                //everything is successful
                Message message = mHandler.obtainMessage();
                message.what = 0;
                mHandler.sendMessage(message);
            } else {
                //valid verification code, sign up failed
                Message message = mHandler.obtainMessage();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        } else {
            //failed
            Message message = mHandler.obtainMessage();
            message.what = 2;
            mHandler.sendMessage(message);
        }


    }

}
