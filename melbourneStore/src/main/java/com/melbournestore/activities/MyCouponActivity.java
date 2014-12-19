package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

    private static final boolean DEBUG = false;

    private ProgressDialog progress;
    private long mExitTime;
    private ListView mCouponList;
    private MyCouponListAdapter mCouponListAdapter;

    private int mCallSource = 0;

    private user_coupon[] mCoupons;
    private user_coupon mChosenCoupon;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mCoupons = (user_coupon[]) msg.obj;
                    if (DEBUG)
                        Log.d("COUPON", "mCoupons len: " + String.valueOf(mCoupons.length));
                    if (mCoupons.length != 0) {
                        mCouponList.setVisibility(View.VISIBLE);
                    }
                    mCouponListAdapter.refresh(mCoupons, mChosenCoupon);
                    mCouponList.setAdapter(mCouponListAdapter);
                    progress.dismiss();
                    break;
                case 1:
                    mChosenCoupon = (user_coupon) msg.obj;
                    mCouponListAdapter.refresh(mCoupons, mChosenCoupon);
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

        Intent intent = getIntent();

        mCallSource = intent.getIntExtra("callSource", 0);


        String chosenCouponString = SharedPreferenceUtils.getUserCoupons(MyCouponActivity.this);
        mChosenCoupon = gson.fromJson(chosenCouponString, user_coupon.class);


        mCouponList = (ListView) findViewById(R.id.coupon_list);
        mCouponListAdapter = new MyCouponListAdapter(this, mHandler, mCoupons, mCallSource, mChosenCoupon);
        mCouponList.setAdapter(mCouponListAdapter);
        mCouponList.setVisibility(View.INVISIBLE);

        String userString = SharedPreferenceUtils.getLoginUser(this);
        user_iphone user = gson.fromJson(userString, user_iphone.class);
        mContactNumber = user.getPhoneNumber();


        mCouponThread = new CouponManagerThread(mHandler, this, mContactNumber);
        mCouponThread.start();
        progress = new ProgressDialog(this, R.style.dialog_loading);
        progress.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_address, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCallSource == 0) {
            menu.findItem(R.id.finish).setVisible(false);
        } else {
            menu.findItem(R.id.finish).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
            case R.id.finish:

                Intent returnIntent = new Intent();
                returnIntent.putExtra("coupons", SharedPreferenceUtils.getUserCoupons(MyCouponActivity.this));
                setResult(RESULT_OK, returnIntent);

                finish();

                break;

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
