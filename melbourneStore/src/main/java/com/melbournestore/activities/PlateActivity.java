/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.adaptors.PlateListAdapter;
import com.melbournestore.adaptors.SelectCategoryListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.categories;
import com.melbournestore.models.item_iphone;
import com.melbournestore.models.number_price;
import com.melbournestore.network.ItemManagerThread;
import com.melbournestore.utils.MelbourneUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlateActivity extends Activity {

    private static final String TAG = "Melbourne";
    DisplayImageOptions options;
    Gson gson = new Gson();
    ProgressDialog progress;
    private ListView mPlatesList;
    private PlateListAdapter mPlateListAdapter;
    private ItemManagerThread mItemThread;
    private Button mConfirmChoice;
    private TextView mTotalPrice;
    private TextView mTotalNum;
    private int mShopId;
    private String mShopName;
    private number_price sumNumberPrice;

    private float totalPrice = 0;
    private int totalNum = 0;
    private ArrayList<item_iphone> mItems = new ArrayList<item_iphone>();


    private int mSelectedCategoryIndex = 0;
    private int mSelectedCategoryId = 1;
    private String mCategoryName = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            Bundle b = msg.getData();
            int position = b.getInt("position");
            switch (msg.what) {

                case 0:
                    mItems = (ArrayList<item_iphone>) msg.obj;
                    mPlateListAdapter.refresh(mItems);
                    mPlatesList.setAdapter(mPlateListAdapter);
                    progress.dismiss();
                    break;
                // plus = 1
                case 1:

                    String itemsString1 = SharedPreferenceUtils.getLocalItems(PlateActivity.this, mShopId);
                    Type type1 = new TypeToken<ArrayList<item_iphone>>() {
                    }.getType();
                    ArrayList<item_iphone> items1 = gson.fromJson(itemsString1, type1);
                    mItems.clear();
                    mItems.addAll(items1);

                    mPlateListAdapter.refresh(mItems);
                    //mPlatesList.setAdapter(mPlateListAdapter);


                    sumNumberPrice = MelbourneUtils.sum_item_number_price(PlateActivity.this);

                    totalPrice = sumNumberPrice.getPrice();
                    totalNum = sumNumberPrice.getNumber();

                    mTotalPrice.setText("$" + String.valueOf(totalPrice));
                    mTotalNum.setText(String.valueOf(totalNum));

                    break;
                // minus = 2
                case 2:
                    if (totalPrice <= 0) {

                    } else {


                        String itemsString2 = SharedPreferenceUtils.getLocalItems(PlateActivity.this, mShopId);
                        Type type2 = new TypeToken<ArrayList<item_iphone>>() {
                        }.getType();
                        ArrayList<item_iphone> items2 = gson.fromJson(itemsString2, type2);
                        mItems.clear();
                        mItems.addAll(items2);


                        mPlateListAdapter.refresh(mItems);
                        //mPlatesList.setAdapter(mPlateListAdapter);

                        sumNumberPrice = MelbourneUtils.sum_item_number_price(PlateActivity.this);

                        totalPrice = sumNumberPrice.getPrice();
                        totalNum = sumNumberPrice.getNumber();

                        mTotalPrice.setText("$" + String.valueOf(totalPrice));
                        mTotalNum.setText(String.valueOf(totalNum));
                    }
                    break;
                case 3:
                    //update list by choosing categories
                    mSelectedCategoryIndex = msg.arg1;
                    mSelectedCategoryId = msg.arg2;
                    mCategoryName = (String) msg.obj;

                    dialog.dismiss();

                    if (mSelectedCategoryId == 1) {
                        String itemsString = SharedPreferenceUtils.getLocalItems(PlateActivity.this, mShopId);
                        Type type = new TypeToken<ArrayList<item_iphone>>() {
                        }.getType();
                        ArrayList<item_iphone> items = gson.fromJson(itemsString, type);
                        mItems.clear();
                        mItems.addAll(items);
                        getActionBar().setTitle(mShopName);

                        mPlateListAdapter.refresh(mItems);
                    } else {

                        ArrayList<item_iphone> items_category = MelbourneUtils.getItemsFromCategoryId(PlateActivity.this, mShopId, mSelectedCategoryId);
                        getActionBar().setTitle(mShopName + "-" + mCategoryName);

                        mItems.clear();
                        mItems.addAll(items_category);


                        mPlateListAdapter.refresh(mItems);

                    }


            }
        }

    };
    private Dialog dialog;
    private ArrayList<categories> mCategories = new ArrayList<categories>();
    private long mExitTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_layout);

        SysApplication.initImageLoader(this);

        SysApplication.getInstance().addActivity(this);


        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mShopId = intent.getIntExtra("shopid", 0);
        mShopName = intent.getStringExtra("shopName");

        String categoriesString = intent.getStringExtra("shopCategories");
        categories[] categoriesArray = gson.fromJson(categoriesString, categories[].class);
        for (int i = 0; i < categoriesArray.length; i++) {
            mCategories.add(categoriesArray[i]);
        }

        mItemThread = new ItemManagerThread(mHandler, this, mShopId);
        mItemThread.start();
        progress = new ProgressDialog(this, R.style.dialog_loading);
        progress.show();


        getActionBar().setTitle(mShopName);


        sumNumberPrice = MelbourneUtils.sum_item_number_price(PlateActivity.this);

        totalPrice = sumNumberPrice.getPrice();
        totalNum = sumNumberPrice.getNumber();


        mPlatesList = (ListView) findViewById(R.id.plates_list);


        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();


        mPlateListAdapter = new PlateListAdapter(this, mHandler, options, mItems);
        mPlatesList.setAdapter(mPlateListAdapter);

        mConfirmChoice = (Button) findViewById(R.id.confirm_choice);
        mConfirmChoice.bringToFront();

        mConfirmChoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlateActivity.this,
                        ShoppingCartActivity.class);
                startActivity(intent);

            }

        });

        mTotalPrice = (TextView) findViewById(R.id.confirm_price);
        mTotalPrice.setText("$" + String.valueOf(totalPrice));

        mTotalNum = (TextView) findViewById(R.id.plate_num_total);
        mTotalNum.setText(String.valueOf(totalNum));

    }

    @Override
    protected void onResume() {
        super.onResume();

        sumNumberPrice = MelbourneUtils.sum_item_number_price(PlateActivity.this);

        totalPrice = sumNumberPrice.getPrice();
        totalNum = sumNumberPrice.getNumber();

//        Intent intent = getIntent();
//        mShopId = intent.getIntExtra("shopId", 0);

        String itemsString = SharedPreferenceUtils.getLocalItems(PlateActivity.this, mShopId);
        Type type = new TypeToken<ArrayList<item_iphone>>() {
        }.getType();
        ArrayList<item_iphone> items = gson.fromJson(itemsString, type);

        mItems.clear();
        mItems.addAll(items);

        mPlateListAdapter.refresh(mItems);
        //mPlatesList.setAdapter(mPlateListAdapter);

        mTotalNum.setText(String.valueOf(totalNum));
        mTotalPrice.setText("$" + String.valueOf(totalPrice));
    }

    // /* The click listner for ListView in the navigation drawer */
    // private class PlateItemClickListener implements
    // ListView.OnItemClickListener {
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position,
    // long id) {
    // //selectItem(position);
    // Log.d(TAG, String.valueOf(position)+" plat myorder_list_item clicked");
    // }
    // }
    //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.items_category).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
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
            case R.id.items_category:

//                Dialog dialog = new Dialog(this, R.style.Dialog_Fullscreen);
//                dialog.setContentView(R.layout.select_category_layout);

                dialog = buildDialogView();

                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Dialog buildDialogView() {

        final Dialog dialog = new Dialog(this, R.style.Theme_D1NoTitleDim);

        dialog.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.select_category_layout, null);
        GridView selectCategoryGrid = (GridView) dialogView.findViewById(R.id.select_category_grid);
        TextView cancelDialog = (TextView) dialogView.findViewById(R.id.select_category_back);
        cancelDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        selectCategoryGrid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        SelectCategoryListAdapter selectCategoryGridAdapter = new SelectCategoryListAdapter(this, mHandler, options, mCategories, mSelectedCategoryIndex);
        selectCategoryGrid.setAdapter(selectCategoryGridAdapter);

        dialog.getWindow().setDimAmount(0.55f);

//        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
//        lp.dimAmount=0.55f;
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.getWindow()
//                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.setContentView(dialogView);
        return dialog;

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
