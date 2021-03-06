package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.SubmitListAdapter;
import com.melbournestore.adaptors.SubmitListCouponAdapter;
import com.melbournestore.adaptors.SubmitListMemoAdapter;
import com.melbournestore.adaptors.UseCouponAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Coupon;
import com.melbournestore.models.Order;
import com.melbournestore.models.OrderItem;
import com.melbournestore.models.item_iphone;
import com.melbournestore.models.user_coupon;
import com.melbournestore.models.user_iphone;
import com.melbournestore.network.CreateOrderThread;
import com.melbournestore.utils.MelbourneUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class SubmitOrderActivity extends Activity {

    public static final int result_code_address = 3;
    public static final int result_coupon = 4;
    float priceTotal;
    ProgressDialog progress;
    private Button mSubmitOrders;
    private TextView mSubmitPrice;
    private ListView mSubmitList;
    private ListView mSubmitMemoList;
    private ListView mSubmitCouponList;
    private ListView mUseCouponList;
    private SubmitListAdapter mSubmitListAdapter;
    private SubmitListMemoAdapter mSubmitListMemoAdapter;
    private SubmitListCouponAdapter mSubmitListCouponAdapter;
    private UseCouponAdapter mUseCouponListAdapter;
    private PopupWindow mTimePickerPopup;
    private user_iphone mUser;

    private String mUnitNo = "";
    private String mStreet = "";
    private String mSuburb = "";
    private String mArea = "";
    private int mFee = 0;

    private String mDeliveryTime = "";
    private String mRemark = "";

    private String mContactNumber = "";

    private OrderItem[] mOrderItems;
    private user_coupon mUser_coupon;

    private Gson gson = new Gson();

    private int mDiscount = 0;


    private ArrayList<user_coupon> mCoupons = new ArrayList<user_coupon>();
    private user_coupon mChosenCoupon;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                // get the suburb chosen
                case 1:
                    Bundle b1 = msg.getData();
                    int action = b1.getInt("action");
                    // popup the time picker
                    if (action == 2) {
                        //showTimePicker();
                        showDeliveryTimePicker();
                    }

                    break;
                case 2:
                    Bundle b2 = msg.getData();
                    mRemark = b2.getString("memo");
                    SharedPreferenceUtils.saveRemark(SubmitOrderActivity.this, mRemark);

                    break;
                case 3:

                    Bundle b3 = msg.getData();
                    mContactNumber = b3.getString("contact_number");
                    SharedPreferenceUtils.saveContactNumber(SubmitOrderActivity.this, mContactNumber);

                    break;
                case 4:

                    String chosenCouponString = SharedPreferenceUtils.getUserCoupons(SubmitOrderActivity.this);
                    Gson mGson = new Gson();
                    mChosenCoupon = mGson.fromJson(chosenCouponString, user_coupon.class);

                    mDiscount = 0;
                    //priceTotal -= (int)Float.parseFloat(mChosenCoupon.getCoupon().getDiscount());
                    mSubmitPrice.setText("共计费用：$" + String.valueOf(priceTotal + mFee));

                    mUseCouponListAdapter.refresh(mChosenCoupon);
                    mUseCouponList.setAdapter(mUseCouponListAdapter);
                    mUseCouponList.setVisibility(View.INVISIBLE);

                    break;

                case 5:
                    progress.dismiss();
                    showNotice("get csrf failed");

                    break;
                case 6:
                    progress.dismiss();
                    showNotice("submit order failed");

                    break;
                case 7:
                    progress.dismiss();
                    Order order = (Order) msg.obj;


                    ArrayList<item_iphone> items = MelbourneUtils.getAllChosenItems(SubmitOrderActivity.this);
                    for (int i = 0; i < items.size(); i++) {
                        int mShopId1 = items.get(i).getShopId();
                        items.get(i).setUnit(0);
                        SharedPreferenceUtils.saveLocalItems(SubmitOrderActivity.this, gson.toJson(items), mShopId1);
                    }
                    SharedPreferenceUtils.saveUserCoupons(SubmitOrderActivity.this, gson.toJson(new user_coupon(-1, -1, -1, "", new Coupon(-1, "", "", "", -1, "", "", -1, -1))));
                    SharedPreferenceUtils.saveDeliveryTime(SubmitOrderActivity.this, "");
                    SharedPreferenceUtils.saveRemark(SubmitOrderActivity.this, "");
                    SharedPreferenceUtils.saveContactNumber(SubmitOrderActivity.this, "");

                    Intent intent = new Intent(SubmitOrderActivity.this, OrderSubmittedActivity.class);
                    intent.putExtra("order", gson.toJson(order));
                    startActivity(intent);
                    break;
            }

        }

    };
    private long mExitTime;

    private void showNotice(String text) {
        new AlertDialog.Builder(SubmitOrderActivity.this)
                .setMessage(text)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface,
                                    int i) {

                            }
                        }
                ).show();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_order_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("提交订单");

        progress = new ProgressDialog(this, R.style.dialog_loading);

        String userString = SharedPreferenceUtils
                .getLoginUser(SubmitOrderActivity.this);
        Gson gson = new Gson();
        mUser = gson.fromJson(userString, user_iphone.class);

        mUnitNo = mUser.getUnitNo();
        mStreet = mUser.getStreet();
        mSuburb = mUser.getSuburb().getName();
        mArea = MelbourneUtils.getAreaFromSuburb(mUser.getSuburb(), this).getName();
        mFee = MelbourneUtils.getAreaFromSuburb(mUser.getSuburb(), this).getFee();

        Intent intent = getIntent();
        priceTotal = intent.getFloatExtra("total_price", 0);


        mContactNumber = SharedPreferenceUtils.getContactNumber(this);

        mSubmitOrders = (Button) findViewById(R.id.submit_order);
        mSubmitOrders.getBackground().setAlpha(80);

        mSubmitOrders.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                String users_string = SharedPreferenceUtils
//                        .getLoginUser(SubmitOrderActivity.this);
//                Gson gson = new Gson();
//                User[] users = gson.fromJson(users_string, User[].class);
//                User activeUser = users[MelbourneUtils.getActiveUser(users)];
//
//                String current_order = SharedPreferenceUtils
//                        .getCurrentOrder(SubmitOrderActivity.this);
//                Order_user currentOrder = gson.fromJson(current_order,
//                        Order_user.class);
//
//                String current_choice = SharedPreferenceUtils
//                        .getCurrentChoice(SubmitOrderActivity.this);
//                Shop[] shops = gson.fromJson(current_choice, Shop[].class);
//                Plate[] plates = MelbourneUtils.getPlatesChosen(shops);
//                currentOrder.setPlates(plates);
//                currentOrder.setDeliveryFee(MelbourneUtils.getSuburbDeliveryPrice(activeUser.getSuburb()));
//
//                if (activeUser.getOrders() == null) {
//                    Order_user[] userOrder = new Order_user[1];
//                    userOrder[0] = currentOrder;
//                    activeUser.setOrders(userOrder);
//                } else if (activeUser.getOrders().length == 0) {
//                    Order_user[] userOrder = new Order_user[1];
//                    userOrder[0] = currentOrder;
//                    activeUser.setOrders(userOrder);
//                } else {
//                    ArrayList<Order_user> userOrder_array = new ArrayList<Order_user>(
//                            Arrays.asList(activeUser.getOrders()));
//                    userOrder_array.add(currentOrder);
//                    activeUser.setOrders(userOrder_array
//                            .toArray(new Order_user[0]));
//                }
//                currentOrder.setStatus(0);
//                currentOrder.setCreateTime(MelbourneUtils.getSystemTime());
//
//                SharedPreferenceUtils.saveCurrentOrder(SubmitOrderActivity.this,
//                        gson.toJson(currentOrder));
//
//                SharedPreferenceUtils.saveLoginUser(SubmitOrderActivity.this,
//                        gson.toJson(users));

                ArrayList<item_iphone> items = MelbourneUtils.getAllChosenItems(SubmitOrderActivity.this);
//                mOrderItems = new OrderItem[items.size()];
//                for(int i=0;i<items.size();i++){
//                    item_iphone eachItem = items.get(i);
//                    mOrderItems[i]=new OrderItem(0, eachItem.getId(), eachItem.getName(), eachItem.getDesc(), (int)Float.parseFloat(eachItem.getPrice()), String.valueOf(eachItem.getUnit()));
//                }


                if (mContactNumber.equals("")) {
                    mContactNumber = mUser.getPhoneNumber();
                } else if ((mContactNumber.length() != 10)
                        || !mContactNumber.toString().subSequence(0, 2)
                        .equals("04")) {
                    showNotice("手机号码不是澳洲手机\n" +
                            "请输入04开头10位号码");
                } else if (mDeliveryTime.equals("")) {
                    showNotice("请填写送货时间");
                } else {

                    String chosenCouponString = SharedPreferenceUtils.getUserCoupons(SubmitOrderActivity.this);
                    Gson mGson = new Gson();
                    mChosenCoupon = mGson.fromJson(chosenCouponString, user_coupon.class);

                    CreateOrderThread mCreateOrderThread = new CreateOrderThread(mHandler, SubmitOrderActivity.this, mUser.getPhoneNumber(), mUnitNo, mStreet, mUser.getSuburb().getPostCode(), mUser.getSuburb().getId(), mDeliveryTime, mFee, mRemark, mContactNumber, items, mChosenCoupon.getId());
                    mCreateOrderThread.start();


                    progress.show();
                }
//                Intent intent = new Intent(SubmitOrderActivity.this,
//                        OrderSubmittedActivity.class);
//                startActivity(intent);
            }

        });

        mSubmitPrice = (TextView) findViewById(R.id.submit_price_total);

        mSubmitList = (ListView) findViewById(R.id.submit_list);
        mSubmitMemoList = (ListView) findViewById(R.id.submit_memo_list);
        mSubmitCouponList = (ListView) findViewById(R.id.submit_coupon_list);
        mUseCouponList = (ListView) findViewById(R.id.use_coupon_list);


        if (mContactNumber.equals("")) {
            mContactNumber = mUser.getPhoneNumber();
        }

        mDeliveryTime = SharedPreferenceUtils.getDeliveryTime(this);

        mSubmitListAdapter = new SubmitListAdapter(this, mHandler, mUser, mContactNumber, mUnitNo, mStreet, mSuburb, mDeliveryTime);
        mSubmitList.setAdapter(mSubmitListAdapter);

        mRemark = SharedPreferenceUtils.getRemark(this);

        mSubmitListMemoAdapter = new SubmitListMemoAdapter(this, mHandler, mRemark);
        mSubmitMemoList.setAdapter(mSubmitListMemoAdapter);

        mSubmitListCouponAdapter = new SubmitListCouponAdapter(this, mHandler, mArea, mFee, mUser);
        mSubmitCouponList.setAdapter(mSubmitListCouponAdapter);

        String chosenCouponString = SharedPreferenceUtils.getUserCoupons(SubmitOrderActivity.this);
        Gson mGson = new Gson();
        mChosenCoupon = mGson.fromJson(chosenCouponString, user_coupon.class);


        mUseCouponListAdapter = new UseCouponAdapter(this, mHandler, mChosenCoupon);
        mUseCouponList.setAdapter(mUseCouponListAdapter);

        if (mChosenCoupon.getId() == -1) {
            mUseCouponList.setVisibility(View.INVISIBLE);
        } else {
            mUseCouponList.setVisibility(View.VISIBLE);
            mDiscount = (int) Float.parseFloat(mChosenCoupon.getCoupon().getDiscount());

        }

        mSubmitOrders.setText("确认下单");

        mSubmitPrice.setText("共计费用：$" + String.valueOf(priceTotal + mFee - mDiscount));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == result_code_address) {
            // Get the Address choosed

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // String address = data.getStringExtra("address");
                mUnitNo = data.getStringExtra("unit");
                mStreet = data.getStringExtra("street");
                mSuburb = data.getStringExtra("suburb");
                mArea = data.getStringExtra("area");
                mFee = data.getIntExtra("fee", 0);

                if (mContactNumber.equals("")) {
                    mContactNumber = mUser.getPhoneNumber();
                }

                mSubmitListAdapter.refresh(mContactNumber, mUnitNo, mStreet, mSuburb, mDeliveryTime);
                mSubmitList.setAdapter(mSubmitListAdapter);

                mSubmitListCouponAdapter.refresh(mArea, mFee, mUser);
                mSubmitCouponList.setAdapter(mSubmitListCouponAdapter);
//                String users_string = SharedPreferenceUtils
//                        .getLoginUser(SubmitOrderActivity.this);
//                Gson gson = new Gson();
//                User[] users = gson.fromJson(users_string, User[].class);
//                User activeUser = users[MelbourneUtils.getActiveUser(users)];
//
//                String current_order = SharedPreferenceUtils
//                        .getCurrentOrder(SubmitOrderActivity.this);
//                Order_user currentOrder = gson.fromJson(current_order,
//                        Order_user.class);
//
//                mSubmitListAdapter.refresh(activeUser, currentOrder);
//                mSubmitList.setAdapter(mSubmitListAdapter);
            }
        } else if (requestCode == result_coupon) {
            if (resultCode == RESULT_OK) {


                String couponString = SharedPreferenceUtils.getUserCoupons(SubmitOrderActivity.this);
                mChosenCoupon = gson.fromJson(couponString, user_coupon.class);

                mDiscount = (int) Float.parseFloat(mChosenCoupon.getCoupon().getDiscount());

                mSubmitPrice.setText("共计费用：$" + String.valueOf(priceTotal + mFee - mDiscount));

                mUseCouponList.setVisibility(View.VISIBLE);
                mUseCouponListAdapter.refresh(mChosenCoupon);
                mUseCouponList.setAdapter(mUseCouponListAdapter);
            }
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

    private void showDeliveryTimePicker() {
        View view = View.inflate(this, R.layout.delivery_time_popup, null);

        final Button deliveryTimeConfirm = (Button) view
                .findViewById(R.id.delivery_time_confirm);

        LinearLayout deliveryTimePicker = (LinearLayout) view.findViewById(R.id.delivery_time_picker);


//        WheelView hours = (WheelView) view.findViewById(R.id.delivery_hour);
//        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23, "%02d");
//        hourAdapter.setItemResource(R.layout.wheel_text_item);
//        hourAdapter.setItemTextResource(R.id.wheel_text);
//        hours.setViewAdapter(hourAdapter);

//        WheelView mins = (WheelView) view.findViewById(R.id.delivery_mins);
//        NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
//        minAdapter.setItemResource(R.layout.wheel_text_item);
//        minAdapter.setItemTextResource(R.id.wheel_text);
//        mins.setViewAdapter(minAdapter);
//        mins.setCyclic(true);


        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        final WheelView day = (WheelView) view.findViewById(R.id.delivery_day);
        day.setViewAdapter(new DayArrayAdapter(this, calendar));


        final WheelView hours = (WheelView) view.findViewById(R.id.delivery_hour);
        ArrayWheelAdapter<String> hourAdapter =
                new ArrayWheelAdapter<String>(this, getPossibleDeliveryHours(deliveryTimeConfirm));
        hourAdapter.setItemResource(R.layout.wheel_text_item);
        hourAdapter.setItemTextResource(R.id.wheel_text);
        hours.setViewAdapter(hourAdapter);


//        day.addChangingListener(new OnWheelChangedListener() {
//            @Override
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//
//            }
//        });

        // set current time

        hours.setCurrentItem(calendar.get(Calendar.HOUR));

        LinearLayout delivery_time_popup = (LinearLayout) view
                .findViewById(R.id.delivery_time_popup);
        delivery_time_popup.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.push_bottom_in));

        if (mTimePickerPopup == null) {
            mTimePickerPopup = new PopupWindow(this);
            mTimePickerPopup.setWidth(LayoutParams.MATCH_PARENT);
            mTimePickerPopup.setHeight(LayoutParams.MATCH_PARENT);
            mTimePickerPopup.setBackgroundDrawable(new BitmapDrawable());

            mTimePickerPopup.setFocusable(true);
            mTimePickerPopup.setOutsideTouchable(true);
        }

        mTimePickerPopup.setContentView(view);
        mTimePickerPopup.showAtLocation(deliveryTimePicker, Gravity.BOTTOM, 0, 0);
        mTimePickerPopup.update();


        deliveryTimeConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                Calendar c = Calendar.getInstance();
                String delivery_time = "";


                if (day.getCurrentItem() == 0) {
                    Date date = new Date();
                    date = c.getTime(); //这个时间就是日期往后推一天的结果
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                    delivery_time += formatter.format(date);
                } else {
                    Date date = new Date();
                    c.setTime(date);
                    c.add(Calendar.DATE, 1);
                    date = c.getTime(); //这个时间就是日期往后推一天的结果
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                    delivery_time += formatter.format(date);
                }

                delivery_time += " " + getPossibleDeliveryHours(deliveryTimeConfirm)[hours.getCurrentItem()];
                mDeliveryTime = delivery_time;
                SharedPreferenceUtils.saveDeliveryTime(SubmitOrderActivity.this, mDeliveryTime);

                if (mContactNumber.equals("")) {
                    mContactNumber = mUser.getPhoneNumber();
                }

                mSubmitListAdapter.refresh(mContactNumber, mUnitNo, mStreet, mSuburb, mDeliveryTime);
                mSubmitList.setAdapter(mSubmitListAdapter);
                mTimePickerPopup.dismiss();
            }

        });


    }


    private String[] getPossibleDeliveryHours(Button button) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int start_time = 19;
        if (hour < 19) {
            start_time = 19;
        } else {
            start_time = hour + 1;
        }
        //start_time = 24;

        ArrayList<String> time = new ArrayList<String>();
//        time.add("19点-20点");
//        time.add("20点-21点");
//        time.add("21点-22点");
//        time.add("23点-24点");
        if (start_time <= 23) {
            for (int i = start_time; i < 24; i++) {

                time.add(String.valueOf(i) + "点-" + String.valueOf(i + 1) + "点");
                button.setEnabled(true);

            }
        } else {
            time.add("时间太晚了亲");
            button.setEnabled(false);
        }
//        for (int i = start_time; i < 24; i++) {
//            for (int j = 0; j < 4; j++) {
//                if (j == 0) {
//                    time.add(String.valueOf(i) + ":00");
//                } else {
//                    time.add(String.valueOf(i) + ":" + String.valueOf(j * 15));
//                }
//            }
//        }
        String[] returned_time = new String[time.size()];
        returned_time = time.toArray(returned_time);
        return returned_time;
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

    /**
     * Day adapter
     */
    private class DayArrayAdapter extends AbstractWheelTextAdapter {
        // Count of days to be shown
        private final int daysCount = 1;

        // Calendar
        Calendar calendar;

        /**
         * Constructor
         */
        protected DayArrayAdapter(Context context, Calendar calendar) {
            super(context, R.layout.time2_day, NO_RESOURCE);
            this.calendar = calendar;


            setItemTextResource(R.id.time2_monthday);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            int day = -daysCount / 2 + index;
            Calendar newCalendar = (Calendar) calendar.clone();
            newCalendar.roll(Calendar.DAY_OF_YEAR, day);

            View view = super.getItem(index, cachedView, parent);
            TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);

            DateFormat format1 = new SimpleDateFormat("EEE");
            weekday.setText(format1.format(newCalendar.getTime()));


            TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);

            DateFormat format2 = new SimpleDateFormat("MMM d");
            monthday.setText(format2.format(newCalendar.getTime()));
            monthday.setTextColor(0xFF111111);

            return view;
        }

        @Override
        public int getItemsCount() {
            return daysCount + 1;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return "";
        }
    }
}
