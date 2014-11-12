package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
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

public class CurrentOrderActivity extends Activity {

    private TextView current_order_info;
    private long mExitTime;

    private TextView submitted_totalprice, submitted_delivery_number, submitted_delivery_address, submitted_delivery_time, submitted_preference, submitted_ordernumber, submitted_ordertime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_submitted_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        int position = getIntent().getIntExtra("position", 0);

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("订单详情");


        current_order_info = (TextView) findViewById(R.id.order_submitted_info1);

        submitted_totalprice = (TextView) findViewById(R.id.submitted_totalprice);
        submitted_delivery_number = (TextView) findViewById(R.id.submitted_delivery_number);
        submitted_delivery_address = (TextView) findViewById(R.id.submitted_delivery_address);
        submitted_delivery_time = (TextView) findViewById(R.id.submitted_delivery_time);
        submitted_preference = (TextView) findViewById(R.id.submitted_preference);
        submitted_ordernumber = (TextView) findViewById(R.id.submitted_ordernumber);
        submitted_ordertime = (TextView) findViewById(R.id.submitted_ordertime);


        String order_info = "";


        String users_string = SharedPreferenceUtils
                .getLoginUser(CurrentOrderActivity.this);

        Gson gson = new Gson();
        User[] users = gson.fromJson(users_string, User[].class);
        User activeUser = users[MelbourneUtils.getActiveUser(users)];

        Order_user order = activeUser.getOrders()[position];

        Plate[] plates = order.getPlates();

        for (int i = 0; i < plates.length; i++) {
            order_info += DataResourceUtils.shopItems[plates[i].getShopId()] + "\n";
            order_info += plates[i].getName() + " " + String.valueOf(plates[i].getNumber()) + "份  $" + String.valueOf(plates[i].getNumber() * plates[i].getPrice()) + "\n";


        }

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

        current_order_info.setText(order_info);

        submitted_totalprice.setText("总计费用: $" + String.valueOf(MelbourneUtils.sum_price_all(plates) + order.getDeliveryFee()));
        submitted_delivery_number.setText("送货电话: " + activeUser.getPhoneNumber());
        submitted_delivery_address.setText("送货地址: " + MelbourneUtils.getCompleteAddress(activeUser));
        submitted_delivery_time.setText("送货时间: " + order.getDeliveryTime());
        submitted_preference.setText("偏   好: " + order.getRemark());
        submitted_ordernumber.setText("订单号码: "+order.getCreateTime());
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
