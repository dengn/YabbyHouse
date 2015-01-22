package com.melbournestore.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.adaptors.ShowImageAdapter;
import com.melbournestore.models.Show;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by dengn on 22/01/2015.
 */
public class ShowImageActivity extends Activity {

    ArrayList<Show> mShows = new ArrayList<Show>();
    private ShowImageAdapter mShowImageAdapter;
    private ViewPager viewPager;
    ProgressDialog mProgress;
    DisplayImageOptions mOptions;
    Gson gson = new Gson();
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                // quit = 0
                case 0:

                    finish();
                    break;


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_layout);

        viewPager = (ViewPager) findViewById(R.id.pager);



        Intent i = getIntent();
        String mShowString = i.getStringExtra("shows");
        int position = i.getIntExtra("position", 0);
        Type type = new TypeToken<ArrayList<Show>>() {
        }.getType();
        mShows = gson.fromJson(mShowString, type);


        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();

        mShowImageAdapter = new ShowImageAdapter(this, mHandler, mOptions, position,mShows,  mProgress);

        viewPager.setAdapter(mShowImageAdapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
