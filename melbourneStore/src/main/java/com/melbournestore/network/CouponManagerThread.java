package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.user_coupon;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by dengn on 01/12/2014.
 */
public class CouponManagerThread extends Thread {

    private static final boolean DEBUG = false;

    private Handler mHandler;
    private Context mContext;
    private Gson gson = new Gson();
    private String mUserNumber;


    public CouponManagerThread(Handler handler, Context context, String userNumber) {
        mHandler = handler;
        mContext = context;
        mUserNumber = userNumber;

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
    public static user_coupon[] getCoupons(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, user_coupon[]>>() {
        }.getType();
        HashMap<String, user_coupon[]> mCoupons = gson.fromJson(jsonString, listType);
        user_coupon[] coupons = mCoupons.get("user_coupons");


        return coupons;
    }

    @Override
    public void run() {

        String result = handleGet(Constant.URL_BASE + "user/" + mUserNumber + "/coupons");

        if (DEBUG)
            Log.d("COUPONTHREAD", result);
        user_coupon[] mCoupons = getCoupons(result);

        Message message = mHandler.obtainMessage();
        message.obj = mCoupons;
        message.what = 0;
        mHandler.sendMessage(message);


    }

}
