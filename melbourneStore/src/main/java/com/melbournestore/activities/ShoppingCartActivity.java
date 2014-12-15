package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.OrderListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.item_iphone;
import com.melbournestore.utils.MelbourneUtils;

import java.util.ArrayList;

public class ShoppingCartActivity extends Activity {

    public static final int LOGIN_CODE = 8;

    private Button mConfirmOrders;

    private TextView mTotalPrice;

    private ListView mOrderList;

    private OrderListAdapter mOrderListAdapter;

    private ArrayList<item_iphone> mItems = new ArrayList<item_iphone>();

    private Gson gson = new Gson();

    private float totalPrice;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            int position = b.getInt("position");
            switch (msg.what) {
                // plus = 1
                case 1:

                    totalPrice = MelbourneUtils.sum_item_number_price(ShoppingCartActivity.this).getPrice();

                    mTotalPrice.setText("$" + String.valueOf(totalPrice));
                    break;
                // minus = 2
                case 2:

                    totalPrice = MelbourneUtils.sum_item_number_price(ShoppingCartActivity.this).getPrice();

                    mTotalPrice.setText("$" + String.valueOf(totalPrice));
                    break;

            }
        }

    };
    private long mExitTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("购物车");

//        String shops_string = SharedPreferenceUtils.getCurrentChoice(this);
//        Gson gson = new Gson();
//        Shop[] shops = gson.fromJson(shops_string, Shop[].class);
        mItems = MelbourneUtils.getAllChosenItems(this);

        totalPrice = MelbourneUtils.sum_item_number_price(this).getPrice();

//        Plate[] plates_chosen = MelbourneUtils.getPlatesChosen(shops);

        mConfirmOrders = (Button) findViewById(R.id.confirm_order);
        mConfirmOrders.getBackground().setAlpha(80);

        mTotalPrice = (TextView) findViewById(R.id.price_total);

        mOrderList = (ListView) findViewById(R.id.shopping_list);

        mOrderListAdapter = new OrderListAdapter(this, mHandler, mItems);
        mOrderList.setAdapter(mOrderListAdapter);

        mTotalPrice.setText("共计费用：$" + String.valueOf(totalPrice));

        mConfirmOrders.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                String userNumber = SharedPreferenceUtils
                        .getUserNumber(ShoppingCartActivity.this);

                Log.d("USERNUMBER", "userNumber: " + userNumber);
                if (!userNumber.equals("")) {

                    if (totalPrice == 0) {
                        new AlertDialog.Builder(ShoppingCartActivity.this)
                                .setMessage("请选择菜品")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialoginterface,
                                                    int i) {

                                            }
                                        }
                                ).show();
                    } else {

                        Intent intent = new Intent(ShoppingCartActivity.this,
                                SubmitOrderActivity.class);
                        intent.putExtra("total_price", totalPrice);

                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(ShoppingCartActivity.this,
                            SignUpActivity.class);
                    // intent.putExtra("total_price", priceTotal);

                    startActivityForResult(intent, LOGIN_CODE);
                }

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        switch (requestCode) {
            case LOGIN_CODE:
                // Get the Address chosen

                // Make sure the request was successful

                break;

        }

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
