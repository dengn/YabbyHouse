package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.MyCouponListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.user_coupon;
import com.melbournestore.models.user_iphone;
import com.melbournestore.network.CouponManagerThread;

public class MyCouponActivity extends Activity {

    private long mExitTime;

    private ListView mCouponList;
    private MyCouponListAdapter mCouponListAdapter;

    private user_coupon[] mCoupons;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mCoupons = (user_coupon[]) msg.obj;
                    Log.d("COUPON", "mCoupons len: " + String.valueOf(mCoupons.length));
                    mCouponListAdapter.refresh(mCoupons);
                    mCouponList.setAdapter(mCouponListAdapter);
                    break;


            }

        }
    };
    private String mContactNumber;
    private Gson gson = new Gson();
    private CouponManagerThread mCouponThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycoupon_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("我的优惠券");

        mCouponList = (ListView) findViewById(R.id.coupon_list);
        mCouponListAdapter = new MyCouponListAdapter(this, mHandler, mCoupons);
        mCouponList.setAdapter(mCouponListAdapter);

        String userString = SharedPreferenceUtils.getLoginUser(this);
        user_iphone user = gson.fromJson(userString, user_iphone.class);
        mContactNumber = user.getPhoneNumber();


        mCouponThread = new CouponManagerThread(mHandler, this, mContactNumber);
        mCouponThread.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                SysApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
