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
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Order_user;
import com.melbournestore.models.Plate;
import com.melbournestore.models.User;
import com.melbournestore.utils.MelbourneUtils;

public class OrderSubmittedActivity extends Activity {
    private TextView notice_info1;
    private TextView notice_info2;
    private long mExitTime;


    private TextView submitted_totalprice;
    private TextView submitted_delivery_number, submitted_delivery_address, submitted_delivery_time, submitted_preference, submitted_ordernumber, submitted_ordertime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_submitted_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("订单已提交");


        notice_info1 = (TextView) findViewById(R.id.order_submitted_info1);
        notice_info2 = (TextView) findViewById(R.id.order_submitted_info2);

        submitted_totalprice = (TextView) findViewById(R.id.submitted_totalprice);
        submitted_delivery_number = (TextView) findViewById(R.id.submitted_delivery_number);
        submitted_delivery_address = (TextView) findViewById(R.id.submitted_delivery_address);
        submitted_delivery_time = (TextView) findViewById(R.id.submitted_delivery_time);
        submitted_preference = (TextView) findViewById(R.id.submitted_preference);
        submitted_ordernumber = (TextView) findViewById(R.id.submitted_ordernumber);
        submitted_ordertime = (TextView) findViewById(R.id.submitted_ordertime);


        String order_info = "";


        String users_string = SharedPreferenceUtils
                .getLoginUser(OrderSubmittedActivity.this);

        Gson gson = new Gson();
        User[] users = gson.fromJson(users_string, User[].class);
        User activeUser = users[MelbourneUtils.getActiveUser(users)];

        String current_order = SharedPreferenceUtils.getCurrentOrder(OrderSubmittedActivity.this);
        Order_user order = gson.fromJson(current_order, Order_user.class);

        Plate[] plates = order.getPlates();

        LinearLayout submitted_items_list = (LinearLayout) findViewById(R.id.submitted_items_list);


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
            //add view for each plate myorder_list_item

            View item_view = LayoutInflater.from(this).inflate(R.layout.submitted_item, null);
            TextView submitted_item_name = (TextView) item_view.findViewById(R.id.submitted_item_name);
            TextView submitted_item_number = (TextView) item_view.findViewById(R.id.submitted_item_number);
            TextView submitted_item_price = (TextView) item_view.findViewById(R.id.submitted_item_price);
            submitted_item_name.setText(plates[i].getName());
            submitted_item_number.setText(String.valueOf(plates[i].getNumber()) + "份");
            submitted_item_price.setText("$ " + String.valueOf(plates[i].getNumber() * plates[i].getPrice()));
            submitted_items_list.addView(item_view);


        }

        //add view for other cost

        TextView other_view = new TextView(this);
        other_view.setTextColor(Color.WHITE);
        other_view.setTextSize(20);
        other_view.setTypeface(null, Typeface.BOLD);
        other_view.setText("其他");
        submitted_items_list.addView(other_view);

        View whitebar_view = LayoutInflater.from(this).inflate(R.layout.textview_whitebar, null);
        submitted_items_list.addView(whitebar_view);

        View delivery_fee_view = LayoutInflater.from(this).inflate(R.layout.submitted_item, null);
        TextView submitted_item_name = (TextView) delivery_fee_view.findViewById(R.id.submitted_item_name);
        TextView submitted_item_price = (TextView) delivery_fee_view.findViewById(R.id.submitted_item_price);

        submitted_item_name.setText("派送费");
        submitted_item_price.setText("$ " + String.valueOf(order.getDeliveryFee()));
        submitted_items_list.addView(delivery_fee_view);


//        order_info += "其他\n";
//        order_info += "派送费" + String.valueOf(order.getDeliveryFee()) + "\n";
//
//        order_info += "总计费用: $" + String.valueOf(MelbourneUtils.sum_price_all(plates) + order.getDeliveryFee() + "\n");
//
//        order_info += "送货电话: " + activeUser.getPhoneNumber() + "\n";
//        order_info += "送货地址: " + MelbourneUtils.getCompleteAddress(activeUser) + "\n";
//        order_info += "送货时间: " + order.getDeliveryTime() + "\n";
//        order_info += "偏好: " + order.getRemark() + "\n";
//        order_info += "订单号码: \n";
//        order_info += "订单时间: " + order.getCreateTime() + "\n";


        notice_info1.setText(order_info);
        notice_info2.setText("");

        submitted_totalprice.setText("总计费用: $" + String.valueOf(MelbourneUtils.sum_price_all(plates) + order.getDeliveryFee()));
        submitted_delivery_number.setText("送货电话: " + activeUser.getPhoneNumber());
        submitted_delivery_address.setText("送货地址: " + MelbourneUtils.getCompleteAddress(activeUser));
        submitted_delivery_time.setText("送货时间: " + order.getDeliveryTime());
        submitted_preference.setText("偏   好: " + order.getRemark());
        submitted_ordernumber.setText("订单号码: " + order.getCreateTime());
        submitted_ordertime.setText("订单时间: " + order.getCreateTime());
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


                SharedPreferenceUtils.setUpCurrentChoice(this);
                SharedPreferenceUtils.setUpCurrentOrder(this);

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
