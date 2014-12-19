package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melbournestore.adaptors.DishListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.models.item_iphone;
import com.melbournestore.models.number_price;
import com.melbournestore.network.SingleItemManagerThread;
import com.melbournestore.utils.MelbourneUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class DishActivity extends Activity {

    private DisplayImageOptions options;


    private ProgressDialog progress;
    private ListView mDishList;
    private DishListAdapter mDishListAdapter;
    private Button mDishConfirmChoice;
    private TextView mDishTotalPrice;
    private TextView mDishTotalNum;


    private int mItemId;
    private String mItemName;
    private number_price sumNumberPrice;

    private float mTotalPrice;
    private int mTotalNum;

    private item_iphone mItem = new item_iphone();
    private SingleItemManagerThread mSingleItemThread;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {

                case 0:

                    mItem = (item_iphone) msg.obj;
                    mItem = MelbourneUtils.updateItemUnits(DishActivity.this, mItem);
                    mDishListAdapter.refresh(mItem);
                    mDishList.setAdapter(mDishListAdapter);
                    progress.dismiss();

                    break;
                case 1:
                    // plus = 1

                    mItem = MelbourneUtils.updateItemUnits(DishActivity.this, mItem);
                    mDishListAdapter.refresh(mItem);

                    sumNumberPrice = MelbourneUtils.sum_item_number_price(DishActivity.this);

                    mTotalNum = sumNumberPrice.getNumber();
                    mTotalPrice = sumNumberPrice.getPrice();

                    mDishTotalNum.setText(String.valueOf(mTotalNum));
                    mDishTotalPrice.setText("$" + String.valueOf(mTotalPrice));

                    break;
                case 2:
                    // minus = 2


                    mItem = MelbourneUtils.updateItemUnits(DishActivity.this, mItem);
                    mDishListAdapter.refresh(mItem);


                    sumNumberPrice = MelbourneUtils.sum_item_number_price(DishActivity.this);

                    mTotalNum = sumNumberPrice.getNumber();
                    mTotalPrice = sumNumberPrice.getPrice();

                    mDishTotalNum.setText(String.valueOf(mTotalNum));
                    mDishTotalPrice.setText("$" + String.valueOf(mTotalPrice));

                    break;
                case 3:
                    progress.dismiss();
                    showNotice("亲，您今天已经点过赞了。");
                    break;
                case 4:
                    // like = 4
                    mItem = (item_iphone) msg.obj;
                    mItem = MelbourneUtils.updateItemUnits(DishActivity.this, mItem);
                    mDishListAdapter.refresh(mItem);
                    mDishList.setAdapter(mDishListAdapter);
                    progress.dismiss();
                    break;

            }
        }
    };
    private long mExitTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_layout);

        SysApplication.initImageLoader(this);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mItemId = intent.getIntExtra("item_id", 0);
        mItemName = intent.getStringExtra("item_name");

        mSingleItemThread = new SingleItemManagerThread(mHandler, this, mItemId);
        mSingleItemThread.start();
        progress = new ProgressDialog(this, R.style.dialog_loading);
        progress.show();


        getActionBar().setTitle(mItemName);

        mDishList = (ListView) findViewById(R.id.dish_list);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();


        mDishListAdapter = new DishListAdapter(this, mHandler, options, mItem, progress);
        mDishList.setAdapter(mDishListAdapter);

        mDishConfirmChoice = (Button) findViewById(R.id.dish_confirm_choice);
        mDishTotalPrice = (TextView) findViewById(R.id.dish_price);
        mDishTotalNum = (TextView) findViewById(R.id.dish_num_total);

        sumNumberPrice = MelbourneUtils.sum_item_number_price(this);

        mTotalNum = sumNumberPrice.getNumber();
        mTotalPrice = sumNumberPrice.getPrice();

        mDishTotalNum.setText(String.valueOf(mTotalNum));
        mDishTotalPrice.setText("$" + String.valueOf(mTotalPrice));


        mDishConfirmChoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DishActivity.this,
                        ShoppingCartActivity.class);
                startActivity(intent);

            }

        });

    }


    private void showNotice(String text) {
        new AlertDialog.Builder(DishActivity.this)
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
