package com.melbournestore.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.melbournestore.models.Show;
import com.melbournestore.models.ShowPage;
import com.melbournestore.utils.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 * Created by dengn on 2014/11/23.
 */
public class ShowManagerThread extends Thread {

    Handler mHandler;

    Context mContext;

    int mPageNum = 1;
    int mHasNext = 0;
    int mNextNum = 0;

    Gson gson = new Gson();

    public ShowManagerThread(Handler handler, Context context, int pageNum) {
        mHandler = handler;
        mContext = context;
        mPageNum = pageNum;
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
     * Transform JSON String to Show
     */
    public ArrayList<Show> getShows(String jsonString) {
        Gson gson = new Gson();
        ShowPage showPage = gson.fromJson(jsonString, ShowPage.class);

//        Type listType = new TypeToken<HashMap<String, Show[]>>() {
//        }.getType();
//        HashMap<String, Show[]> mShows = gson.fromJson(jsonString, listType);
//        Show[] shows = mShows.get("user_show_list");
        Log.d("SHOW", String.valueOf(showPage.getHasNext()));

        if(showPage.getHasNext()){
            mHasNext = 1;
        }
        else{
            mHasNext = 0;
        }
        mNextNum = showPage.getNextNum();

        Show[] shows = showPage.getShows();



        ArrayList<Show> Shows_array = new ArrayList<Show>();

        for (int i = 0; i < shows.length; i++) {
            Shows_array.add(shows[i]);
        }


        return Shows_array;
    }

    @Override
    public void run() {
        String result = handleGet(Constant.URL_BASE + "user/show?next_num=" + String.valueOf(mPageNum));


        Log.d("THREAD", result);
        ArrayList<Show> mShows = getShows(result);

        Message message = mHandler.obtainMessage();
        message.obj = mShows;
        message.arg1 = mHasNext;
        message.arg2 = mNextNum;

        mHandler.sendMessage(message);
    }

}
