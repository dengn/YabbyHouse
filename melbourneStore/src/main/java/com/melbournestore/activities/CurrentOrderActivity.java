package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.ADPagerAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Order_user;
import com.melbournestore.models.Plate;
import com.melbournestore.models.User;
import com.melbournestore.ui.CirclePageIndicator;
import com.melbournestore.utils.MelbourneUtils;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderActivity extends Activity implements View.OnTouchListener {

    private ViewPager pager_splash_ad;
    private ADPagerAdapter adapter;
    private int flaggingWidth;
    private int size = 0;
    private int lastX = 0;
    private int currentIndex = 0;
    private CirclePageIndicator indicator;
    private boolean locker = true;

    private TextView current_order_info;
    private long mExitTime;

    //private TextView submitted_totalprice, submitted_delivery_number, submitted_delivery_address, submitted_delivery_time, submitted_preference, submitted_ordernumber, submitted_ordertime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_order_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        int position = getIntent().getIntExtra("position", 0);

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("订单详情");


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        flaggingWidth = dm.widthPixels / 2;

        List<String> splash_ad = (List<String>) getIntent()
                .getSerializableExtra("splash_ad");
        pager_splash_ad = (ViewPager) findViewById(R.id.currentOrder_pager);

        List<View> views = new ArrayList<View>();





        String order_info = "";


        String users_string = SharedPreferenceUtils
                .getLoginUser(CurrentOrderActivity.this);

        Gson gson = new Gson();
        User[] users = gson.fromJson(users_string, User[].class);
        User activeUser = users[MelbourneUtils.getActiveUser(users)];


        // TODO
        View view = LayoutInflater.from(this).inflate(
                R.layout.current_order_process_layout, null);

        views.add(view);


        views.add(getCurrentOrderStatusView(activeUser, position));
        size = views.size();

        adapter = new ADPagerAdapter(this, views);
        pager_splash_ad.setAdapter(adapter);
        indicator = (CirclePageIndicator) findViewById(R.id.page_indicator);
        indicator.setmListener(new MypageChangeListener());
        indicator.setViewPager(pager_splash_ad);
        if (views.size() == 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    gotoMain();
                }
            }, 1000);
        } else {
//			pager_splash_ad.setOnPageChangeListener(new MypageChangeListener());
            pager_splash_ad.setOnTouchListener(this);
        }




    }

    private void gotoMain() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((lastX - event.getX()) > flaggingWidth && (currentIndex == size - 1) && locker) {
                    locker = false;
                    System.err.println("-------1111-------");
                    gotoMain();
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action
                // bar.
                // Create a simple intent that starts the hierarchical parent
                // activity and
                // use NavUtils in the Support Package to ensure proper handling of
                // Up.


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

    private View getCurrentOrderStatusView(User activeUser, int position) {

        Order_user order = activeUser.getOrders()[position];

        Plate[] plates = order.getPlates();

        View order_detail = LayoutInflater.from(this).inflate(R.layout.order_submitted_layout, null);


        LinearLayout submitted_items_list = (LinearLayout) order_detail.findViewById(R.id.submitted_items_list);


        int currentshop = -1;

        for (int i = 0; i < plates.length; i++) {
            //order_info += DataResourceUtils.shopItems[plates[i].getShopId()] + "\n";
            //order_info += plates[i].getName() + " " + String.valueOf(plates[i].getNumber()) + "份  $" + String.valueOf(plates[i].getNumber() * plates[i].getPrice()) + "\n";

            if (currentshop != plates[i].getShopId()) {

                currentshop = plates[i].getShopId();

                TextView shop_view = new TextView(this);
                shop_view.setTextColor(Color.WHITE);
                shop_view.setTextSize(20);
                shop_view.setTypeface(null, Typeface.BOLD);
                shop_view.setText(DataResourceUtils.shopItems[plates[i].getShopId()]);
                submitted_items_list.addView(shop_view);


                View whitebar_view = LayoutInflater.from(this).inflate(R.layout.textview_whitebar, null);
                submitted_items_list.addView(whitebar_view);

            }
            //add view for each plate item

            View item_view = LayoutInflater.from(this).inflate(R.layout.submitted_item, null);
            TextView submitted_item_name = (TextView) item_view.findViewById(R.id.submitted_item_name);
            TextView submitted_item_number = (TextView) item_view.findViewById(R.id.submitted_item_number);
            TextView submitted_item_price = (TextView) item_view.findViewById(R.id.submitted_item_price);
            submitted_item_name.setText(plates[i].getName());
            submitted_item_number.setText(String.valueOf(plates[i].getNumber()) + "份");
            submitted_item_price.setText("$ " + String.valueOf(plates[i].getNumber() * plates[i].getPrice()));
            submitted_items_list.addView(item_view);


        }


        TextView submitted_totalprice, submitted_delivery_number, submitted_delivery_address, submitted_delivery_time, submitted_preference, submitted_ordernumber, submitted_ordertime;

        submitted_totalprice = (TextView) order_detail.findViewById(R.id.submitted_totalprice);
        submitted_delivery_number = (TextView) order_detail.findViewById(R.id.submitted_delivery_number);
        submitted_delivery_address = (TextView) order_detail.findViewById(R.id.submitted_delivery_address);
        submitted_delivery_time = (TextView) order_detail.findViewById(R.id.submitted_delivery_time);
        submitted_preference = (TextView) order_detail.findViewById(R.id.submitted_preference);
        submitted_ordernumber = (TextView) order_detail.findViewById(R.id.submitted_ordernumber);
        submitted_ordertime = (TextView) order_detail.findViewById(R.id.submitted_ordertime);


        submitted_totalprice.setText("总计费用: $" + String.valueOf(MelbourneUtils.sum_price_all(plates) + order.getDeliveryFee()));
        submitted_delivery_number.setText("送货电话: " + activeUser.getPhoneNumber());
        submitted_delivery_address.setText("送货地址: " + MelbourneUtils.getCompleteAddress(activeUser));
        submitted_delivery_time.setText("送货时间: " + order.getDeliveryTime());
        submitted_preference.setText("偏   好: " + order.getRemark());
        submitted_ordernumber.setText("订单号码: " + order.getCreateTime());
        submitted_ordertime.setText("订单时间: " + order.getCreateTime());


        return order_detail;
    }

    private class MypageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int position) {
//			System.err.println("------position---"+position);
//			currentItem = position;

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            currentIndex = arg0;
            if(currentIndex==0){
                getActionBar().setTitle("订单流程");
            }else if(currentIndex==1){
                getActionBar().setTitle("订单详情");
            }
        }


    }


}
