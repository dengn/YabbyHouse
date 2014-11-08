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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.PlateListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;
import com.melbournestore.utils.MelbourneUtils;

public class PlateActivity extends Activity {

    private static final String TAG = "Melbourne";

    private ListView mPlatesList;

    private PlateListAdapter mPlateListAdapter;

    private Button mConfirmChoice;

    private TextView mTotalPrice;

    private TextView mTotalNum;

    private int mShopId;

    private int totalPrice = 0;

    private int totalNum = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            int position = b.getInt("position");
            switch (msg.what) {
                // plus = 1
                case 1:
                    //totalPrice += DataResourceUtils.platePrices[mShopId][position];
                    //totalNum++;
                    // plateNumbers[position]++;

                    String shops_string1 = SharedPreferenceUtils.getCurrentChoice(PlateActivity.this);
                    Gson gson1 = new Gson();
                    Shop[] shops1 = gson1.fromJson(shops_string1, Shop[].class);

                    totalNum = MelbourneUtils.sum_number_all(shops1);
                    totalPrice = MelbourneUtils.sum_price_all(shops1);


                    mTotalPrice.setText("$" + String.valueOf(totalPrice));
                    mTotalNum.setText(String.valueOf(totalNum));
                    break;
                // minus = 2
                case 2:
                    if (totalPrice <= 0) {

                    } else {

                        //totalPrice -= DataResourceUtils.platePrices[mShopId][position];
                        //totalNum--;
                        // plateNumbers[position]--;

                        String shops_string2 = SharedPreferenceUtils.getCurrentChoice(PlateActivity.this);
                        Gson gson2 = new Gson();
                        Shop[] shops2 = gson2.fromJson(shops_string2, Shop[].class);

                        totalNum = MelbourneUtils.sum_number_all(shops2);
                        totalPrice = MelbourneUtils.sum_price_all(shops2);

                        mTotalPrice.setText("$" + String.valueOf(totalPrice));
                        mTotalNum.setText(String.valueOf(totalNum));
                    }
                    break;

            }
        }

    };
    private long mExitTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mShopId = intent.getIntExtra("shopid", 0);


        getActionBar().setTitle(DataResourceUtils.shopItems[mShopId]);

        String shops_string = SharedPreferenceUtils.getCurrentChoice(this);
        Gson gson = new Gson();
        Shop[] shops = gson.fromJson(shops_string, Shop[].class);

        totalNum = MelbourneUtils.sum_number_all(shops);
        totalPrice = MelbourneUtils.sum_price_all(shops);

        Plate[] plates = shops[mShopId].getPlates();


        mPlatesList = (ListView) findViewById(R.id.plates_list);


        mPlateListAdapter = new PlateListAdapter(this, mHandler, plates);
        mPlatesList.setAdapter(mPlateListAdapter);

        mConfirmChoice = (Button) findViewById(R.id.confirm_choice);
        mConfirmChoice.bringToFront();

        mConfirmChoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

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

        String shops_string = SharedPreferenceUtils.getCurrentChoice(this);
        Gson gson = new Gson();
        Shop[] shops = gson.fromJson(shops_string, Shop[].class);
        Plate[] plates = shops[mShopId].getPlates();

        mPlateListAdapter.refresh(plates);
        mPlatesList.setAdapter(mPlateListAdapter);

        totalNum = MelbourneUtils.sum_number_all(shops);
        totalPrice = MelbourneUtils.sum_price_all(shops);

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
    // Log.d(TAG, String.valueOf(position)+" plat item clicked");
    // }
    // }
    //

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

    private Plate[] getDataFromPreference() {

        String data = SharedPreferenceUtils.getCurrentChoice(this);
        Gson gson = new Gson();
        Plate[] plates = gson.fromJson(data, Plate[].class);
        return plates;

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
