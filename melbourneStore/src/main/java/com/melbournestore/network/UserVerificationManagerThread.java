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
public class UserVerificationManagerThread extends Thread {

    private static final boolean DEBUG = false;

    private Handler mHandler;
    private Context mContext;
    private String mNumber;
    private Gson gson = new Gson();


    public UserVerificationManagerThread(Handler handler, Context context, String number) {
        mHandler = handler;
        mContext = context;
        mNumber = number;
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


    public static String handlePost(String strUrl, List<NameValuePair> params) {
        String result = null;
        HttpPost request = new HttpPost(strUrl);//ʵ����get����
        DefaultHttpClient client = new DefaultHttpClient();//ʵ�����ͻ���

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
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

    public static String handlePut(String strUrl, List<NameValuePair> params) {
        String result = null;
        HttpPut request = new HttpPut(strUrl);//ʵ����get����
        DefaultHttpClient client = new DefaultHttpClient();//ʵ�����ͻ���

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
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


    @Override
    public void run() {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("number", mNumber));

        String result = handlePut(Constant.URL_BASE + "user/verification", pairs);

        if(DEBUG)
        Log.d("USERTHREAD", result);
        //verification code sent
        if (!result.equals("HTTP/1.1 404 NOT FOUND")) {
            Message message = mHandler.obtainMessage();
            message.what = 1;
            mHandler.sendMessage(message);
        } else {
            Message message = mHandler.obtainMessage();
            message.what = 0;
            mHandler.sendMessage(message);
        }

    }

}
