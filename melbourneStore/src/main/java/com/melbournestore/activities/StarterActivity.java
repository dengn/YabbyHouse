package com.melbournestore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melbournestore.adaptors.StartPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengn on 19/01/2015.
 */
public class StarterActivity extends Activity {
    private List<View> mLists=new ArrayList<View>();
    private ViewPager mViewPager;
    private StartPagerAdapter adapter;
    private Button mButton;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mLinearLayout;
    private int current=0;
    private ImageView[] points=new ImageView[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter_layout);
        mLayoutInflater=LayoutInflater.from(this);
        mLinearLayout=(LinearLayout)this.findViewById(R.id.liear);
        mViewPager=(ViewPager)this.findViewById(R.id.starter_viewpager);
        View view1=mLayoutInflater.inflate(R.layout.layout_01, null);
        View view2=mLayoutInflater.inflate(R.layout.layout_02, null);
        View view3=mLayoutInflater.inflate(R.layout.layout_03, null);
        View view4=mLayoutInflater.inflate(R.layout.layout_04, null);
        View view5=mLayoutInflater.inflate(R.layout.layout_05, null);
        mButton=(Button)view5.findViewById(R.id.btn_start);
        mButton.setOnClickListener(new MySetOnClickListener());
        mLists.add(view1);
        mLists.add(view2);
        mLists.add(view3);
        mLists.add(view4);
        mLists.add(view5);
        adapter=new StartPagerAdapter(mLists);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new MyOnPagerChangerListener());
        initPoints();
    }
    //初始化下面的小圆点
    private void initPoints()
    {
        for(int i=0;i<5;i++)
        {
            points[i]=(ImageView)mLinearLayout.getChildAt(i);
            points[i].setImageResource(R.drawable.sysclear_ic_storage_left);
        }
        current=0;//默认在第一页
        points[current].setImageResource(R.drawable.sysclear_ic_storage_used);//此刻处于第一页，把第一页的小圆圈设置为unenabled
    }
    class MyOnPagerChangerListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
        @Override
        public void onPageSelected(int arg0) {
            points[arg0].setImageResource(R.drawable.sysclear_ic_storage_used);
            points[current].setImageResource(R.drawable.sysclear_ic_storage_left);
            current=arg0;
        }

    }


    class  MySetOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent _Intent=new Intent();
            _Intent.setClass(StarterActivity.this, MainActivity.class);
            startActivity(_Intent);
            StarterActivity.this.finish();

        }

    }
}
