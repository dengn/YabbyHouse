package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.Coupon;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by OLEDCOMM on 01/12/2014.
 */
public class CouponManagerThread extends Thread {
    Handler mHandler;
    Context mContext;
    Gson gson = new Gson();
    String mUserNumber;


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
    public static Coupon[] getCoupons(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, Coupon[]>>() {
        }.getType();
        HashMap<String, Coupon[]> mCoupons = gson.fromJson(jsonString, listType);
        Coupon[] coupons = mCoupons.get("user_coupons");


        return coupons;
    }

    @Override
    public void run() {

        String result = handleGet(Constant.URL_BASE + "user/" + mUserNumber + "/coupons");

        Log.d("COUPONTHREAD", result);
        Coupon[] mCoupons = getCoupons(result);


//        switch(mCallCode){
//            case 0:
//                //called from MyAccountActivity page
//                Message message = mHandler.obtainMessage();
//                message.obj = mCoupons.length;
//                Log.d("ACCOUNT", "mCoupons.length: "+String.valueOf(mCoupons.length));
//                message.what = 1;
//                mHandler.sendMessage(message);
//                break;
//            case 1:
//                //called by the other
//                message = mHandler.obtainMessage();
//                message.obj = mCoupons;
//                message.what = 2;
//                mHandler.sendMessage(message);
//                break;
//        }

    }

}
