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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.melbournestore.models.Order_user;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;
import com.melbournestore.models.User;
import com.melbournestore.utils.MelbourneUtils;

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
    Fragment plate_fragment;
    Fragment myorders_fragment;
    Fragment googlemap_fragment;
    Fragment recommandation_fragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private DrawerListAdapter mDrawerListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;
    private Handler mHandler = new Handler();
    private boolean isLoggedIn;
    private String loginNumber;
    private Bitmap mProfile;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SysApplication.getInstance().addActivity(this);

        if (DEBUG) {
            SharedPreferenceUtils.SharedPreferenceClearCurrentChoice(this);
            SharedPreferenceUtils.SharedPreferenceClearCurrentOrder(this);
            SharedPreferenceUtils.SharedPreferenceLoginUser(this);
        }

        SharedPreferenceUtils.setUpCurrentChoice(this);

        SharedPreferenceUtils.setUpCurrentOrder(this);

        setUpLoginUser();

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

        // set up the drawer's list view with items and click listener
        // mDrawerList.setAdapter(new ArrayAdapter<String>(this,
        // R.layout.drawer_list_item, mMenuTitles));
        // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        String users_string = SharedPreferenceUtils
                .getLoginUser(MainActivity.this);
        Gson gson = new Gson();
        User[] users = gson.fromJson(users_string, User[].class);

        mDrawerListAdapter = new DrawerListAdapter(MainActivity.this, mHandler,
                users);

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

        String users_string = SharedPreferenceUtils
                .getLoginUser(MainActivity.this);
        Gson gson = new Gson();
        User[] users = gson.fromJson(users_string, User[].class);

        getActionBar().setTitle(mTitle);

        mDrawerListAdapter.refresh(users);
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

                String users_string = SharedPreferenceUtils
                        .getLoginUser(MainActivity.this);
                Gson gson = new Gson();
                User[] users = gson.fromJson(users_string, User[].class);

                mDrawerListAdapter.refresh(users);
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

                String users_string = SharedPreferenceUtils
                        .getLoginUser(MainActivity.this);
                Gson gson = new Gson();
                User[] users = gson.fromJson(users_string, User[].class);

                mDrawerListAdapter.refresh(users);
                mDrawerList.setAdapter(mDrawerListAdapter);
                mDrawerLayout.openDrawer(mDrawerList);

                selectItem(1);
            }
        }
    }// onActivityResult

    private void selectItem(int position) {

        FragmentManager fragmentManager = getFragmentManager();
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                // mDrawerList.setItemChecked(position, true);
                // setTitle(mMenuTitles[position]);

                String users_string = SharedPreferenceUtils
                        .getLoginUser(MainActivity.this);
                Gson gson = new Gson();
                User[] users = gson.fromJson(users_string, User[].class);

                // Not logged in yet
                if (MelbourneUtils.getActiveUser(users) < 0) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_CODE);
                } else {
                    Intent intent = new Intent(this, MyAccountActivity.class);
                    startActivityForResult(intent, MY_ACCOUNT_CODE);
                }

                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 1:

                // Fragment plate_fragment = new PlateFragment(this);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, plate_fragment).commit();

                // update selected myorder_list_item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(mMenuTitles[position - 1]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 2:

                // Fragment myorders_fragment = new MyOrdersFragment();

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, myorders_fragment).commit();

                // update selected myorder_list_item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(mMenuTitles[position - 1]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 3:

                // Fragment googlemap_fragment = new GoogleMapFragment(this);
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
//                mDrawerList.setItemChecked(position, true);
//                setTitle(mMenuTitles[position - 1]);
//                mDrawerLayout.closeDrawer(mDrawerList);
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

    private void setUpCurrentChoice() {
        Shop[] shops = new Shop[DataResourceUtils.shopItems.length];

        for (int i = 0; i < shops.length; i++) {

            Shop shop = new Shop();

            Plate[] plates = new Plate[DataResourceUtils.plateNames[i].length];

            for (int j = 0; j < plates.length; j++) {
                Plate plate = new Plate();

                plate.setName(DataResourceUtils.plateNames[i][j]);
                plate.setPrice(DataResourceUtils.platePrices[i][j]);
                plate.setNumber(0);
                plate.setStockMax(DataResourceUtils.plateStockMax[i][j]);
                plate.setLikeNum(DataResourceUtils.plateLikeNumbers[i][j]);
                plate.setImageId(DataResourceUtils.plateImages[i][j]);
                plate.setShopId(i);
                plate.setPlateId(j);

                plates[j] = plate;
            }
            shop.setPlates(plates);
            shops[i] = shop;
        }

        Gson gson = new Gson();
        String shopsJson = gson.toJson(shops);

        SharedPreferenceUtils.saveCurrentChoice(this, shopsJson);

    }

    private void setUpCurrentOrder() {

        Gson gson = new Gson();
        Order_user order = new Order_user();

        order.setDeliveryTime("");
        order.setRemark("");

        SharedPreferenceUtils.saveCurrentOrder(this, gson.toJson(order));

    }

    private void setUpLoginUser() {

        String users_string = SharedPreferenceUtils
                .getLoginUser(MainActivity.this);
        Gson gson = new Gson();
        User[] users = gson.fromJson(users_string, User[].class);

        if (users == null) {
            users = new User[0];
        }

        SharedPreferenceUtils.saveLoginUser(MainActivity.this,
                gson.toJson(users));

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