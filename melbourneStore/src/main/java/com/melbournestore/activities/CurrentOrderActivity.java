package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.ADPagerAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.models.Order;
import com.melbournestore.models.OrderItem;
import com.melbournestore.ui.CirclePageIndicator;
import com.melbournestore.utils.MelbourneUtils;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderActivity extends Activity implements View.OnTouchListener {

    private ViewPager pager_splash_ad;
    private ADPagerAdapter adapter;

    ProgressDialog progress;

    private int flaggingWidth;
    private int size = 0;
    private int lastX = 0;
    private int currentIndex = 0;
    private CirclePageIndicator indicator;
    private boolean locker = true;

    private Order mOrder;

    private Gson gson = new Gson();

    private TextView current_order_info;
    private long mExitTime;


    //private TextView submitted_totalprice, submitted_delivery_number, submitted_delivery_address, submitted_delivery_time, submitted_preference, submitted_ordernumber, submitted_ordertime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_order_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        String mOrderString = getIntent().getStringExtra("order");
        mOrder = gson.fromJson(mOrderString, Order.class);

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


        // TODO

        View view = getCurrentOrderStatusFlow(mOrder);
        views.add(view);


        views.add(getCurrentOrderStatusView(mOrder));
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


    private View getCurrentOrderStatusFlow(Order mOrder){
        View view = LayoutInflater.from(this).inflate(
                R.layout.current_order_process_layout, null);

        TextView currentOrder_status = (TextView) view.findViewById(R.id.currentOrder_status);
        TextView currentOrder_number = (TextView) view.findViewById(R.id.currentOrder_id);
        ImageView currentOrder_status1_circle = (ImageView) view.findViewById(R.id.currentOrder_status1_circle);
        TextView currentOrder_status1_time = (TextView) view.findViewById(R.id.currentOrder_status1_time);
        ImageView currentOrder_status2_circle = (ImageView) view.findViewById(R.id.currentOrder_status2_circle);
        TextView currentOrder_status2_time = (TextView) view.findViewById(R.id.currentOrder_status2_time);
        ImageView currentOrder_status3_circle = (ImageView) view.findViewById(R.id.currentOrder_status3_circle);
        TextView currentOrder_status3_time = (TextView) view.findViewById(R.id.currentOrder_status3_time);
        ImageView currentOrder_status4_circle = (ImageView) view.findViewById(R.id.currentOrder_status4_circle);
        TextView currentOrder_status4_time = (TextView) view.findViewById(R.id.currentOrder_status4_time);
        ImageView currentOrder_status5_circle = (ImageView) view.findViewById(R.id.currentOrder_status5_circle);
        TextView currentOrder_status5_time = (TextView) view.findViewById(R.id.currentOrder_status5_time);

        currentOrder_status.setText(MelbourneUtils.getCurrentOrderStatusString(mOrder.getStatus()));
        currentOrder_number.setText("订单号: "+String.valueOf(mOrder.getId()));
        if(mOrder.getStatus()==0) {
            currentOrder_status1_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status1_time.setText(mOrder.getCreateTime().split(" ")[1]);
        }

        else if(mOrder.getStatus()==1) {
            currentOrder_status1_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status2_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status1_time.setText(mOrder.getCreateTime().split(" ")[1]);
            currentOrder_status2_time.setText(mOrder.getConfirmTime().split(" ")[1]);
        }
        else if(mOrder.getStatus()==2){
            currentOrder_status1_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status2_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status3_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status1_time.setText(mOrder.getCreateTime().split(" ")[1]);
            currentOrder_status2_time.setText(mOrder.getConfirmTime().split(" ")[1]);
            currentOrder_status3_time.setText(mOrder.getDistributingTime().split(" ")[1]);
        }else if(mOrder.getStatus()==3){
            currentOrder_status1_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status2_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status3_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status4_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status1_time.setText(mOrder.getCreateTime().split(" ")[1]);
            currentOrder_status2_time.setText(mOrder.getConfirmTime().split(" ")[1]);
            currentOrder_status3_time.setText(mOrder.getDistributingTime().split(" ")[1]);
            currentOrder_status4_time.setText(mOrder.getDeliveryingTime().split(" ")[1]);
        }
        else if(mOrder.getStatus()==4){
            currentOrder_status1_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status2_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status3_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status4_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status5_circle.setBackgroundResource(R.drawable.circle_white);
            currentOrder_status1_time.setText(mOrder.getCreateTime().split(" ")[1]);
            currentOrder_status2_time.setText(mOrder.getConfirmTime().split(" ")[1]);
            currentOrder_status3_time.setText(mOrder.getDistributingTime().split(" ")[1]);
            currentOrder_status4_time.setText(mOrder.getDeliveryingTime().split(" ")[1]);
            currentOrder_status5_time.setText(mOrder.getCompleteTime().split(" ")[1]);
        }


        return view;
    }

    private View getCurrentOrderStatusView(Order mOrder) {

        View order_detail = LayoutInflater.from(this).inflate(R.layout.order_submitted_layout, null);


        final LinearLayout submitted_items_list = (LinearLayout) order_detail.findViewById(R.id.submitted_items_list);

        final OrderItem[] items = mOrder.getItems();


        String currentShopName = "";



        for (int i = 0; i < items.length; i++) {
            //order_info += DataResourceUtils.shopItems[plates[i].getShopId()] + "\n";
            //order_info += plates[i].getName() + " " + String.valueOf(plates[i].getNumber()) + "份  $" + String.valueOf(plates[i].getNumber() * plates[i].getPrice()) + "\n";


            if (!currentShopName.equals(MelbourneUtils.getShopNameFromItemId(items[i].getItemId(), CurrentOrderActivity.this))) {

                currentShopName = MelbourneUtils.getShopNameFromItemId(items[i].getItemId(), CurrentOrderActivity.this);

                TextView shop_view = new TextView(CurrentOrderActivity.this);
                shop_view.setTextColor(Color.WHITE);
                shop_view.setTextSize(20);
                shop_view.setTypeface(null, Typeface.BOLD);
                shop_view.setText(currentShopName);
                submitted_items_list.addView(shop_view);


                View whitebar_view = LayoutInflater.from(CurrentOrderActivity.this).inflate(R.layout.textview_whitebar, null);
                submitted_items_list.addView(whitebar_view);

            }


            //add view for each plate myorder_list_item

            View item_view = LayoutInflater.from(this).inflate(R.layout.submitted_item, null);
            TextView submitted_item_name = (TextView) item_view.findViewById(R.id.submitted_item_name);
            TextView submitted_item_number = (TextView) item_view.findViewById(R.id.submitted_item_number);
            TextView submitted_item_price = (TextView) item_view.findViewById(R.id.submitted_item_price);
            submitted_item_name.setText(items[i].getName());
            submitted_item_number.setText(String.valueOf((int) Float.parseFloat(items[i].getCount())) + "份");
            submitted_item_price.setText("$ " + String.valueOf((int) Float.parseFloat(items[i].getCount()) * items[i].getPrice()));
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


        submitted_totalprice.setText("总计费用: $" + String.valueOf(MelbourneUtils.sum_price_items(items)+mOrder.getDeliveryFee()));
        submitted_delivery_number.setText("送货电话: " + mOrder.getPhoneNumber());
        submitted_delivery_address.setText("送货地址: " + MelbourneUtils.getCompleteAddress(mOrder));
        submitted_delivery_time.setText("送货时间: " + mOrder.getDeliveryTime());
        submitted_preference.setText("偏   好: " + mOrder.getRemark());
        submitted_ordernumber.setText("订单号码: " + String.valueOf(mOrder.getId()));
        submitted_ordertime.setText("订单时间: " + mOrder.getCreateTime());


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
            if (currentIndex == 0) {
                getActionBar().setTitle("订单流程");
            } else if (currentIndex == 1) {
                getActionBar().setTitle("订单详情");
            }
        }


    }


}
