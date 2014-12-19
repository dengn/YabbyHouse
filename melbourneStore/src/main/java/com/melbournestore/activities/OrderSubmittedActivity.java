package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.application.SysApplication;
import com.melbournestore.models.Order;
import com.melbournestore.models.OrderItem;
import com.melbournestore.utils.MelbourneUtils;

public class OrderSubmittedActivity extends Activity {

    private long mExitTime;
    private Order mOrder;
    private Gson gson = new Gson();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.order_submitted_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("订单已提交");

        String mOrderString = getIntent().getStringExtra("order");
        mOrder = gson.fromJson(mOrderString, Order.class);

        setContentView(getCurrentOrderStatusView(mOrder));

    }

    private View getCurrentOrderStatusView(Order mOrder) {

        View order_detail = LayoutInflater.from(this).inflate(R.layout.order_submitted_layout, null);


        final LinearLayout submitted_items_list = (LinearLayout) order_detail.findViewById(R.id.submitted_items_list);

        final LinearLayout others_list = (LinearLayout) order_detail.findViewById(R.id.others_list);

        final OrderItem[] items = mOrder.getItems();


        String currentShopName = "";


        for (int i = 0; i < items.length; i++) {

            if (!currentShopName.equals(MelbourneUtils.getShopNameFromItemId(items[i].getItemId(), OrderSubmittedActivity.this))) {

                currentShopName = MelbourneUtils.getShopNameFromItemId(items[i].getItemId(), OrderSubmittedActivity.this);

                TextView shop_view = new TextView(OrderSubmittedActivity.this);
                shop_view.setTextColor(Color.WHITE);
                shop_view.setTextSize(20);
                shop_view.setTypeface(null, Typeface.BOLD);
                shop_view.setText(currentShopName);
                submitted_items_list.addView(shop_view);


                View whitebar_view = LayoutInflater.from(OrderSubmittedActivity.this).inflate(R.layout.textview_whitebar, null);
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



        View item_view = LayoutInflater.from(this).inflate(R.layout.submitted_item, null);
        TextView submitted_item_name = (TextView) item_view.findViewById(R.id.submitted_item_name);
        TextView submitted_item_number = (TextView) item_view.findViewById(R.id.submitted_item_number);
        TextView submitted_item_price = (TextView) item_view.findViewById(R.id.submitted_item_price);
        submitted_item_name.setText("运送费");
        submitted_item_number.setText("");
        submitted_item_price.setText("$ " + String.valueOf(mOrder.getDeliveryFee()));
        others_list.addView(item_view);

        if (mOrder.getCoupon() != null) {
            if (mOrder.getCoupon()[0].getId() != -1) {


                submitted_item_name.setText("优惠券");
                submitted_item_number.setText("");
                submitted_item_price.setText("$ " + mOrder.getCoupon()[0].getCoupon().getDiscount());
                others_list.addView(item_view);
            }
        }


        TextView submitted_totalprice, submitted_delivery_number, submitted_delivery_address, submitted_delivery_time, submitted_preference, submitted_ordernumber, submitted_ordertime;

        submitted_totalprice = (TextView) order_detail.findViewById(R.id.submitted_totalprice);
        submitted_delivery_number = (TextView) order_detail.findViewById(R.id.submitted_delivery_number);
        submitted_delivery_address = (TextView) order_detail.findViewById(R.id.submitted_delivery_address);
        submitted_delivery_time = (TextView) order_detail.findViewById(R.id.submitted_delivery_time);
        submitted_preference = (TextView) order_detail.findViewById(R.id.submitted_preference);
        submitted_ordernumber = (TextView) order_detail.findViewById(R.id.submitted_ordernumber);
        submitted_ordertime = (TextView) order_detail.findViewById(R.id.submitted_ordertime);


        float totalPrice = 0;
        if (mOrder.getCoupon() != null) {
            if (mOrder.getCoupon()[0].getId() != -1) {
                totalPrice = MelbourneUtils.sum_price_items(items) + (float) mOrder.getDeliveryFee() - Float.parseFloat(mOrder.getCoupon()[0].getCoupon().getDiscount());
            } else {
                totalPrice = MelbourneUtils.sum_price_items(items) + (float) mOrder.getDeliveryFee();
            }
        } else {
            totalPrice = MelbourneUtils.sum_price_items(items) + mOrder.getDeliveryFee();
        }
        submitted_totalprice.setText("总计费用: $" + String.valueOf(totalPrice));
        submitted_delivery_number.setText("送货电话: " + mOrder.getPhoneNumber());
        submitted_delivery_address.setText("送货地址: " + MelbourneUtils.getCompleteAddress(mOrder));
        submitted_delivery_time.setText("送货时间: " + mOrder.getDeliveryTime());
        submitted_preference.setText("偏   好: " + mOrder.getRemark());
        submitted_ordernumber.setText("订单号码: " + String.valueOf(mOrder.getId()));
        submitted_ordertime.setText("订单时间: " + mOrder.getCreateTime());


        return order_detail;
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

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(upIntent);
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
