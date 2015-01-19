package com.melbournestore.adaptors;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by dengn on 19/01/2015.
 */
public class StartPagerAdapter extends PagerAdapter {

    private List<View> mLists;
    public StartPagerAdapter(List<View> pLists)
    {
        this.mLists=pLists;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mLists.size();
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0==arg1;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager)container).removeView(mLists.get(position));
    }
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager)container).addView(mLists.get(position));
        return mLists.get(position);
    }
}
