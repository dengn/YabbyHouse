package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.models.OrderItem;
import com.melbournestore.models.user_coupon;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dengn on 2014/12/3.
 */
public class CreateOrderThread extends Thread{

    Handler mHandler;
    Context mContext;
    Gson gson = new Gson();
    String mUserNumber;
    String mUnitNo;
    String mStreet;
    String mPostCode;
    int mSuburbId;
    String mDeliveryTime;
    int mDeliveryFee;
    String mRemark;
    String mContactNumber;
    OrderItem[] mOrderItems;
    String mCsrf;
    user_coupon mUserCoupon;



    public CreateOrderThread(Handler handler, Context context, String userNumber, String unitNo, String street, String postCode, int suburbId, String deliveryTime, int deliveryFee, String remark, String contactNumber, OrderItem[] orderItems, user_coupon userCoupon) {
        mHandler = handler;
        mContext = context;
        mUserNumber = userNumber;
        mUnitNo = unitNo;
        mStreet = street;
        mPostCode = postCode;
        mSuburbId = suburbId;
        mDeliveryTime = deliveryTime;
        mDeliveryFee = deliveryFee;
        mRemark = remark;
        mContactNumber = contactNumber;
        mOrderItems = orderItems;
        mUserCoupon = userCoupon;
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


        String csrf = handleGet(Constant.URL_BASE + "create_order");

        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> csrf_hash = gson.fromJson(csrf, type);

        mCsrf = csrf_hash.get("csrf");
        Log.d("CREATEORDERTHREAD", "mCsrf: " + mCsrf);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("contact_number", mUserNumber));
        pairs.add(new BasicNameValuePair("csrf_token", mCsrf));
        pairs.add(new BasicNameValuePair("phone_number", mUserNumber));
        pairs.add(new BasicNameValuePair("unit_no", mUnitNo));
        pairs.add(new BasicNameValuePair("street", mStreet));
        pairs.add(new BasicNameValuePair("post_code", mPostCode));
        pairs.add(new BasicNameValuePair("suburb_id", String.valueOf(mSuburbId)));
        pairs.add(new BasicNameValuePair("delivery_time", mDeliveryTime));
        pairs.add(new BasicNameValuePair("delivery_fee", String.valueOf(mDeliveryFee)));
        pairs.add(new BasicNameValuePair("remark", mRemark));
        pairs.add(new BasicNameValuePair("user_coupon_id", "-1"));
        //pairs.add(new BasicNameValuePair("items", gson.toJson(mOrderItems)));
        for (int i = 0; i < mOrderItems.length; i++) {
            pairs.add(new BasicNameValuePair("items-" + String.valueOf(i) + "-item_id", String.valueOf(mOrderItems[i].getId())));
            pairs.add(new BasicNameValuePair("items-" + String.valueOf(i) + "-name", mOrderItems[i].getName()));
            pairs.add(new BasicNameValuePair("items-" + String.valueOf(i) + "-desc", mOrderItems[i].getDesc()));
            pairs.add(new BasicNameValuePair("items-" + String.valueOf(i) + "-price", String.valueOf(mOrderItems[i].getPrice())));
            pairs.add(new BasicNameValuePair("items-" + String.valueOf(i) + "-count", String.valueOf(mOrderItems[i].getCount())));
        }

//        for (int i = 0; i < mOrderItems.length; i++) {
//
//            pairs.add(new BasicNameValuePair("items[]",mOrderItems[i]));
//
//        }


        String result = handlePost(Constant.URL_BASE + "create_order", pairs);

        Log.d("CREATEORDERTHREAD", result);



//        Message message = mHandler.obtainMessage();
//        message.obj = mOrders;
//        message.what = 0;
//        mHandler.sendMessage(message);


    }
}
