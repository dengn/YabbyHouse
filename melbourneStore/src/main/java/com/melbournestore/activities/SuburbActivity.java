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
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.foound.widget.AmazingAdapter;
import com.melbournestore.adaptors.SuburbListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.SuburbData;
import com.melbournestore.models.Area;
import com.melbournestore.models.Suburb;

import java.util.ArrayList;
import java.util.List;

public class SuburbActivity extends Activity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    SectionComposerAdapter adapter;
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
                case 2:
                    Bundle b2 = msg.getData();

                    String query = b2.getString("query");

                    //ArrayList<Pair<String, List<String>>> results = new ArrayList<Pair<String, List<String>>>(Arrays.asList(b2.getSerializable("query_result")));

                    adapter.refresh(SuburbData.getData(query));
                    suburbList.setAdapter(adapter);

                    headerView.requestFocus();
            }
        }

    };
    SuburbListHeaderView headerView;
    boolean search_enabled = false;
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

        headerView = new SuburbListHeaderView(this);


        suburbListAdapter = new SuburbListAdapter(this, areaList);

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
        suburb = new Suburb(3, "Montrouge", "92110", "12:30");
        suburbList.add(suburb);

        Area area = new Area(1, "center", 5, 0, suburbList, "13:00");

        areaList.add(area);

        suburbList = new ArrayList<Suburb>();
        suburb = new Suburb(4, "chatelet", "75002", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(5, "Paristech", "16340", "12:30");
        suburbList.add(suburb);
        suburb = new Suburb(6, "Cite", "92220", "12:30");
        suburbList.add(suburb);

        area = new Area(2, "north", 6, 0, suburbList, "13:00");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.plate_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search_plate:
                if (!search_enabled) {
                    suburbList.addHeaderView(headerView);

                    search_enabled = true;
                } else {
                    suburbList.removeHeaderView(headerView);
                    search_enabled = false;

                }
                break;

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

//		// 自定义标题栏
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mTitleView = mInflater.inflate(R.layout.suburb_action_bar_layout,
                null);
        getActionBar().setCustomView(
                mTitleView,
                new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
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

    class SectionComposerAdapter extends AmazingAdapter {
        List<Pair<String, List<String>>> all;


        public SectionComposerAdapter(List<Pair<String, List<String>>> list) {
            all = list;
        }

        public void refresh(List<Pair<String, List<String>>> list) {
            all.clear();
            all.addAll(list);
        }

        @Override
        public int getCount() {
            int res = 0;
            for (int i = 0; i < all.size(); i++) {
                res += all.get(i).second.size();
            }
            return res;
        }

        @Override
        public String getItem(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return all.get(i).second.get(position - c);
                }
                c += all.get(i).second.size();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        protected void onNextPageRequested(int page) {
        }

        @Override
        protected void bindSectionHeader(View view, int position,
                                         boolean displaySectionHeader) {
            if (displaySectionHeader) {
                view.findViewById(R.id.header).setVisibility(View.VISIBLE);
                TextView lSectionTitle = (TextView) view
                        .findViewById(R.id.header);
                lSectionTitle
                        .setText(getSections()[getSectionForPosition(position)]);
            } else {
                view.findViewById(R.id.header).setVisibility(View.GONE);
            }
        }


        @Override
        public View getAmazingView(int position, View convertView,
                                   ViewGroup parent) {
            View res = convertView;
            if (res == null)
                res = getLayoutInflater().inflate(R.layout.item_composer, null);

            TextView lName = (TextView) res.findViewById(R.id.lName);

            final String name = getItem(position);
            lName.setText(name);

            res.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Message message = new Message();
                    Bundle b = new Bundle();
                    // send the suburb chosen
                    b.putString("suburb", name);
                    message.setData(b);
                    message.what = 1;
                    mHandler.sendMessage(message);
                }

            });

            return res;
        }

        @Override
        public void configurePinnedHeader(View header, int position, int alpha) {
            TextView lSectionHeader = (TextView) header;
            lSectionHeader
                    .setText(getSections()[getSectionForPosition(position)]);
            // lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
            // lSectionHeader.setTextColor(alpha << 24 | (0x000000));
        }

        @Override
        public int getPositionForSection(int section) {
            if (section < 0)
                section = 0;
            if (section >= all.size())
                section = all.size() - 1;
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (section == i) {
                    return c;
                }
                c += all.get(i).second.size();
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return i;
                }
                c += all.get(i).second.size();
            }
            return -1;
        }

        @Override
        public String[] getSections() {
            String[] res = new String[all.size()];
            for (int i = 0; i < all.size(); i++) {
                res[i] = all.get(i).first;
            }
            return res;
        }

    }

    public class SuburbListHeaderView extends LinearLayout {

        private Context mContext;
        private SearchView mSearchView;

        public SuburbListHeaderView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            mContext = context;
            View view = LayoutInflater.from(mContext).inflate(R.layout.category_list_item_header, null);

            addView(view);
            mSearchView = (SearchView) view.findViewById(R.id.category_search_view);

            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO Auto-generated method stub

                    return false;
                }

                @Override
                public boolean onQueryTextChange(final String newText) {
                    // TODO Auto-generated method stub

//	        		mSearchView.setVisibility(View.INVISIBLE);
//	        		if (newText.length() != 0) {
//	        			mSearchView.setFilterText(newText);
//	        		} else {
//	        			mSearchView.clearTextFilter();
//	        		}

//	                if (TextUtils.isEmpty(newText)) {
//	                	plates.clearTextFilter();
//	                } else {
//	                	plates.setFilterText(newText.toString());
//	                    filter.filter(newText);
//	                }
                    Thread queryThread = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            // TODO Auto-generated method stub

                            //List<Pair<String, List<String>>> query_result = SuburbData.getData(newText);

                            Message message = new Message();
                            Bundle b = new Bundle();

                            b.putString("query", newText);
                            message.setData(b);
                            message.what = 2;
                            mHandler.sendMessage(message);
//			        		adapter.refresh(SuburbData.getData(newText));
//			        		suburbList.setAdapter(adapter);
                        }

                    });
                    queryThread.start();

//	        		adapter.refresh(SuburbData.getData(newText));
//	        		adapter.notifyDataSetChanged();
//	        		
////	        		adapter.refresh(newText);
//	        		suburbList.setAdapter(adapter);
//	        		suburbList.setVisibility(View.VISIBLE);
//	        		headerView.requestFocus();

                    return false;
                }
            });
            mSearchView.setSubmitButtonEnabled(false);
        }

    }

}
