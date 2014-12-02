package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OLEDCOMM on 01/12/2014.
 */
public class AddressManagerThread extends Thread {
    Handler mHandler;
    Context mContext;
    Gson gson = new Gson();
    String mUserNumber;
    String mUnitNo;
    String mStreet;
    int mSuburbId;


    public AddressManagerThread(Handler handler, Context context, String userNumber, String unitNo, String street, int suburbId) {
        mHandler = handler;
        mContext = context;
        mUserNumber = userNumber;
        mUnitNo = unitNo;
        mStreet = street;
        mSuburbId = suburbId;
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
        pairs.add(new BasicNameValuePair("number", mUserNumber));
        pairs.add(new BasicNameValuePair("unit_no", mUnitNo));
        pairs.add(new BasicNameValuePair("street", mStreet));
        pairs.add(new BasicNameValuePair("suburb_id", String.valueOf(mSuburbId)));
        String result = handlePut(Constant.URL_BASE + "user/address", pairs);

        Log.d("ADDRESSTHREAD", result);
//        user_coupon[] mCoupons = getCoupons(result);
//
//        Message message = mHandler.obtainMessage();
//        message.obj = mCoupons;
//        message.what = 0;
//        mHandler.sendMessage(message);
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
