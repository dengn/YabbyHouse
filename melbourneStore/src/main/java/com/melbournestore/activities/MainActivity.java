/*
 * Copyright 2013 The Android Open Source Project
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.DrawerListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.fragments.GoogleMapFragment;
import com.melbournestore.fragments.MyOrdersFragment;
import com.melbournestore.fragments.PlateFragment;
import com.melbournestore.fragments.RecommandationFragment;
import com.melbournestore.models.Coupon;
import com.melbournestore.models.Suburb;
import com.melbournestore.models.user_coupon;
import com.melbournestore.models.user_iphone;
import com.melbournestore.network.AreaManagerThread;
import com.melbournestore.utils.FontManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * This example illustrates a common usage of the DrawerLayout widget in the
 * Android support library.
 * <p/>
 * <p>
 * When a navigation (left) drawer is present, the host activity should detect
 * presses of the action bar's Up affordance as a signal to open and close the
 * navigation drawer. The ActionBarDrawerToggle facilitates this behavior. Items
 * within the drawer should fall into one of two categories:
 * </p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic
 * policies as list or tab navigation in that a view switch does not create
 * navigation history. This pattern should only be used at the root activity of
 * a task, leaving some form of Up navigation active for activities further down
 * the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an
 * alternate parent for Up navigation. This allows a user to jump across an
 * app's navigation hierarchy at will. The application should treat this as it
 * treats Up navigation from a different task, replacing the current task stack
 * using TaskStackBuilder or similar. This is the only form of navigation drawer
 * that should be used outside of the root activity of a task.</li>
 * </ul>
 * <p/>
 * <p>
 * Right side drawers should be used for actions, not navigation. This follows
 * the pattern established by the Action Bar that navigation should be to the
 * left and actions to the right. An action should be an operation performed on
 * the current contents of the window, for example enabling or disabling a data
 * overlay on top of the current content.
 * </p>
 */
public class MainActivity extends Activity {

    private static final String TAG = "Melbourne";

    private static final boolean DEBUG = false;

    private static final int LOGIN_CODE = 1;

    private static final int MY_ACCOUNT_CODE = 7;

    private Fragment plate_fragment;
    private Fragment myorders_fragment;
    private Fragment googlemap_fragment;
    private Fragment recommandation_fragment;

    private DisplayImageOptions options;

    private Gson gson = new Gson();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private DrawerListAdapter mDrawerListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;


    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;
    private Handler mHandler = new Handler() {
    };


    private long mExitTime;
    private int mOrderNum;
    private int mCouponNum;
    private user_iphone mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        FontManager.getContentView(this);

        SysApplication.getInstance().addActivity(this);


        //First launch of application, init shared preference data
        if (SharedPreferenceUtils.getFirstTimeLaunch(this)) {

            mUser = new user_iphone("", "", "", 0, "", new Suburb(0, "", "", ""));
            SharedPreferenceUtils.saveLoginUser(MainActivity.this, gson.toJson(mUser));
            SharedPreferenceUtils.saveUserCoupons(MainActivity.this, gson.toJson(new user_coupon(-1, -1, -1, "", new Coupon(-1, "", "", "", -1, "", "", -1, -1))));
            SharedPreferenceUtils.saveDeliveryTime(MainActivity.this, "");
            SharedPreferenceUtils.saveRemark(MainActivity.this, "");
            SharedPreferenceUtils.saveContactNumber(MainActivity.this, "");
        } else {
            String mUserString = SharedPreferenceUtils.getLoginUser(this);
            if(DEBUG)
                Log.d("LOGIN", mUserString);
            mUser = gson.fromJson(mUserString, user_iphone.class);
        }


        //Get List of Area
        AreaManagerThread mAreaThread = new AreaManagerThread(mHandler, this);
        mAreaThread.start();


        plate_fragment = new PlateFragment();
        plate_fragment.onAttach(this);
        myorders_fragment = new MyOrdersFragment();
        myorders_fragment.onAttach(this);
        googlemap_fragment = new GoogleMapFragment();
        googlemap_fragment.onAttach(this);
        recommandation_fragment = new RecommandationFragment();
        recommandation_fragment.onAttach(this);

        mTitle = mDrawerTitle = getTitle();
        mMenuTitles = DataResourceUtils.drawerItemsTitles;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);


        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();


        mDrawerListAdapter = new DrawerListAdapter(MainActivity.this, mHandler, options,
                mUser);

        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String mUserString = SharedPreferenceUtils.getLoginUser(this);
        mUser = gson.fromJson(mUserString, user_iphone.class);
        getActionBar().setTitle(mTitle);

        mDrawerListAdapter.refresh(mUser);
        mDrawerList.setAdapter(mDrawerListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        menu.findItem(R.id.web_search).setVisible(false);


        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.web_search:
                //Allow fragment to click on menu item
                return true;

            case R.id.search_plate:
                // Not implemented here
                return false;
            case R.id.chat:
                // Not implemented here
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        selectItem(1);

        if (requestCode == LOGIN_CODE) {
            if (resultCode == RESULT_OK) {
                // get the ID of the client

                String users_string = data.getStringExtra("user");
                mOrderNum = data.getIntExtra("order_num", 0);
                mCouponNum = data.getIntExtra("coupon_num", 0);

                if(DEBUG)
                    Log.d("LOGIN", "Main Activity: " + users_string);

                mUser = gson.fromJson(users_string, user_iphone.class);

                mDrawerListAdapter.refresh(mUser);
                mDrawerList.setAdapter(mDrawerListAdapter);
                mDrawerLayout.openDrawer(mDrawerList);

                selectItem(1);
            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        } else if (requestCode == MY_ACCOUNT_CODE) {
            if (resultCode == RESULT_OK) {
                // get the profile photo

                mDrawerListAdapter.refresh(mUser);
                mDrawerList.setAdapter(mDrawerListAdapter);
                mDrawerLayout.openDrawer(mDrawerList);

                selectItem(1);
            }
        }
    }


    private void showNotice(String text) {
        new AlertDialog.Builder(MainActivity.this)
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


    private void selectItem(int position) {

        FragmentManager fragmentManager = getFragmentManager();
        // update the main content by replacing fragments
        switch (position) {
            case 0:


                // Not logged in yet
                if (mUser.getPhoneNumber().equals("")) {
                    Intent intent = new Intent(this, SignUpActivity.class);
                    startActivityForResult(intent, LOGIN_CODE);
                }
                //Already logged in
                else {
                    Intent intent = new Intent(this, MyAccountActivity.class);
                    intent.putExtra("user", gson.toJson(mUser));
                    intent.putExtra("order_num", mOrderNum);
                    intent.putExtra("coupon_num", mCouponNum);
                    startActivityForResult(intent, MY_ACCOUNT_CODE);
                }

                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 1:



                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, plate_fragment).commit();


                mDrawerList.setItemChecked(position, true);
                setTitle(mMenuTitles[position - 1]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 2:


                if (mUser.getPhoneNumber().equals("")) {
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, myorders_fragment).commit();


                    mDrawerList.setItemChecked(position, true);
                    setTitle(mMenuTitles[position - 1]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                break;
            case 3:


                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, googlemap_fragment).commit();

                mDrawerList.setItemChecked(position, true);
                setTitle(mMenuTitles[position - 1]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 4:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Coming soon!")
                        .setCancelable(false)
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, recommandation_fragment).commit();
                mDrawerList.setItemChecked(position, true);
                setTitle(mMenuTitles[position - 1]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
            // Log.d(TAG, String.valueOf(position)+" drawer myorder_list_item clicked");
        }
    }

}