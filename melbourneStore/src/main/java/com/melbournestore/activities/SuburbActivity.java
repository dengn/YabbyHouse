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
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.melbournestore.adaptors.SuburbListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.models.Area;
import com.melbournestore.models.Suburb;

import java.util.ArrayList;

public class SuburbActivity extends Activity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                // get the suburb chosen
                case 1:
                    Bundle b = msg.getData();
                    String suburbChosen = b.getString("suburb");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("suburb", suburbChosen);
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

    private ArrayList<Area> areaList = new ArrayList<Area>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.suburb_layout);

        SysApplication.getInstance().addActivity(this);

        initActionBar();

        loadSomeData();
        // search_suburb = (SearchView) findViewById(R.id.search_view);
        suburbList = (ExpandableListView) findViewById(R.id.suburb_list);

        //headerView = new SuburbListHeaderView(this);


        suburbListAdapter = new SuburbListAdapter(this, mHandler, areaList);

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

    private void loadSomeData() {

        ArrayList<Suburb> suburbList = new ArrayList<Suburb>();
        Suburb suburb = new Suburb(1, "city", "92130", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(2, "issy les moulineaux", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(3, "seir", "92110", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(4, "w4teywr", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(5, "asfkowet", "92110", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(6, "wrteytrywt", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(7, "5yuurr", "92110", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(8, "wetyeryey", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(9, "sfafege", "92110", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(10, "dfhfjdstsdgtsegter", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(11, "serhthth", "92110", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(12, "asfwegwg", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(13, "qqqq", "92110", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(14, "yatst", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(15, "atrsrg", "92110", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(16, "Jiefang Bei", "92120", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(17, "Cross Roads", "92110", "12:30");
        suburbList.add(suburb);


        suburbList.add(suburb);

        Area area = new Area(1, "City", 5, 0, suburbList, "13:00");

        areaList.add(area);

        suburbList = new ArrayList<Suburb>();
        suburb = new Suburb(18, "chatelet", "75002", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(19, "Paristech", "16340", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(20, "Cite", "92220", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(21, "fggg", "75002", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(22, "rerwer", "16340", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(23, "rwetwywt", "92220", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(24, "yteyrt", "75002", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(25, "hdrhrey", "16340", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(26, "q3rowetwet", "92220", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(27, "wetwetwet", "75002", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(28, "sdgoweotowet", "16340", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(30, "South Area", "92220", "12:30");
        suburbList.add(suburb);

        area = new Area(2, "东南", 6, 0, suburbList, "13:00");
        areaList.add(area);


        suburbList = new ArrayList<Suburb>();
        suburb = new Suburb(31, "Airport", "75002", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(32, "Two River", "75002", "12:30");
        suburbList.add(suburb);

        area = new Area(3, "北", 6, 0, suburbList, "13:00");
        areaList.add(area);

        suburbList = new ArrayList<Suburb>();
        suburb = new Suburb(33, "University Town", "75002", "12:30");
        suburbList.add(suburb);

        area = new Area(4, "西", 6, 0, suburbList, "13:00");
        areaList.add(area);


        suburbList = new ArrayList<Suburb>();
        suburb = new Suburb(34, "Northeast Area", "75002", "12:30");
        suburbList.add(suburb);

        area = new Area(5, "西北", 6, 0, suburbList, "13:00");
        areaList.add(area);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.search_plate:
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
