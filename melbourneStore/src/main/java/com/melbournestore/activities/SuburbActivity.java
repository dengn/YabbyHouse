package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.SuburbListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.models.Area;
import com.melbournestore.network.AreaManagerThread;

import java.util.ArrayList;

public class SuburbActivity extends Activity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case 0:
                    mAreas = (ArrayList<Area>) msg.obj;
                    suburbListAdapter.refresh(mAreas);
                    suburbList.setAdapter(suburbListAdapter);
                    break;
                // get the suburb chosen
                case 1:
                    Bundle b = msg.getData();
                    String suburbName = b.getString("name");
                    String suburbPostCode = b.getString("postcode");
                    String areaName = b.getString("area");
                    int areaFee = b.getInt("fee");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", suburbName);
                    returnIntent.putExtra("postcode", suburbPostCode);
                    returnIntent.putExtra("area", areaName);
                    returnIntent.putExtra("fee", areaFee);

                    setResult(RESULT_OK, returnIntent);
                    finish();

                    break;

            }
        }

    };

    private SearchView search_suburb;
    //private AmazingListView suburbList;
    //private ArrayAdapter<String> suburb_chosen_adapter;
    //private String[] suburb_chosen_names;
    //private ArrayList<String> suburb_chosen_list;
    private long mExitTime;

    private SuburbListAdapter suburbListAdapter;
    private ExpandableListView suburbList;

    private AreaManagerThread mAreaThread;
    private Gson gson = new Gson();

    private ArrayList<Area> mAreas = new ArrayList<Area>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.suburb_layout);

        SysApplication.getInstance().addActivity(this);

        mAreaThread = new AreaManagerThread(mHandler, SuburbActivity.this);
        mAreaThread.start();


        suburbList = (ExpandableListView) findViewById(R.id.suburb_list);



        suburbListAdapter = new SuburbListAdapter(this, mHandler, mAreas);

        suburbList.setAdapter(suburbListAdapter);
//        suburbList.setPinnedHeaderView(LayoutInflater.from(this).inflate(
//                R.layout.item_composer_header, suburbList, false));

        //adapter = new SectionComposerAdapter(SuburbData.getAllData());

        //suburbList.setAdapter(adapter);


        //expand all Groups
        expandAll();

    }


    //method to expand all groups
    private void expandAll() {
        int count = suburbListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            suburbList.expandGroup(i);
        }
    }


    @Override
    public boolean onClose() {
        suburbListAdapter.filterData("");
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        suburbListAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        suburbListAdapter.filterData(query);
        expandAll();
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.plate_menu, menu);

        search_suburb = (SearchView) menu.findItem(R.id.search_plate).getActionView();;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        search_suburb.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //search_suburb.setIconifiedByDefault(false);
        search_suburb.setOnQueryTextListener(this);
        search_suburb.setOnCloseListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search_plate:
//                if (!search_enabled) {
//                    suburbList.addHeaderView(headerView);
//
//                    search_enabled = true;
//                } else {
//                    suburbList.removeHeaderView(headerView);
//                    search_enabled = false;
//
//                }
//                break;
                return true;

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

    private void initActionBar() {

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("123");

//		// 自定义标题栏
        getActionBar().setDisplayShowHomeEnabled(false);
        //getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mTitleView = mInflater.inflate(R.layout.suburb_action_bar_layout,
                null);
        getActionBar().setCustomView(
                mTitleView,
                new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT)
        );
        search_suburb = (SearchView) mTitleView.findViewById(R.id.search_view);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        search_suburb.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search_suburb.setIconifiedByDefault(false);
        search_suburb.setOnQueryTextListener(this);
        search_suburb.setOnCloseListener(this);
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
