package com.melbournestore.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;

import com.melbournestore.activities.R;
import com.melbournestore.adaptors.CategoryListAdapter;
import com.melbournestore.adaptors.ItemsSearchListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.models.Shop_iPhone;
import com.melbournestore.models.item_iphone;
import com.melbournestore.network.ItemManagerThread;
import com.melbournestore.network.SearchItemManagerThread;
import com.melbournestore.network.ShopManagerThread;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

public class PlateFragment extends Fragment {

    private Context mContext;

    private ProgressDialog progress;

    private ListView category;
    private CategoryListAdapter category_adapter;

    private ExpandableListView plates;
    private ItemsSearchListAdapter itemsSearchAdapter;

    private ListView searchList;
    //private PlateSearchListAdapter platesFilter_adapter;

    private DisplayImageOptions options;
    private ShopManagerThread mShopThread;
    private SearchItemManagerThread mSearchItemThread;

    private ActionBar actionBar;
    private SearchView search_plate;
    private ArrayList<Shop_iPhone> mShops = new ArrayList<Shop_iPhone>();
    private ArrayList<item_iphone> mSearchItems = new ArrayList<item_iphone>();
    private Handler mHandler_item = new Handler();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mShops = (ArrayList<Shop_iPhone>) msg.obj;
                    for(int i=0;i<mShops.size();i++){
                        ItemManagerThread itemThread = new ItemManagerThread(mHandler_item, mContext, mShops.get(i).getId());
                        itemThread.start();
                    }


                    category_adapter.refresh(mShops);
                    category.setAdapter(category_adapter);
                    progress.dismiss();
                    break;
                case 1:
                    mSearchItems = (ArrayList<item_iphone>) msg.obj;
                    itemsSearchAdapter.refresh(mSearchItems);
                    progress.dismiss();

                    break;
            }


        }
    };
    //private ArrayList<Shop> shopList = new ArrayList<Shop>();

    public PlateFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SysApplication.initImageLoader(mContext);




        mShopThread = new ShopManagerThread(mHandler, mContext);
        mShopThread.start();
        progress = new ProgressDialog(mContext ,R.style.dialog_loading);
        progress.show();


        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();
        category_adapter = new CategoryListAdapter(mContext, options, mShops);

        itemsSearchAdapter = new ItemsSearchListAdapter(mContext, mHandler, mSearchItems);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.plate_menu, menu);
        search_plate = (SearchView) menu.findItem(R.id.search_plate).getActionView();

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        search_plate.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        search_plate.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //expandAll();
            }
        });

        search_plate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {


                //platesFilter_adapter.filterData(query);

                mSearchItemThread = new SearchItemManagerThread(mHandler, mContext, query);
                mSearchItemThread.start();


                //expandAll();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchList.setVisibility(View.VISIBLE);
                //platesFilter_adapter.filterData(query);
                //expandAll();


                return false;
            }
        });
        search_plate.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //plates.setVisibility(View.INVISIBLE);
                searchList.setVisibility(View.INVISIBLE);
                //platesFilter_adapter.filterData("");
                //expandAll();
                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_plate:


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        View rootView = inflater.inflate(R.layout.fragment_plate, container,
                false);

        category = (ListView) rootView.findViewById(R.id.category);
        category.setAdapter(category_adapter);



        searchList =(ListView) rootView.findViewById(R.id.items_search_list);

        searchList.setAdapter(itemsSearchAdapter);

        searchList.setVisibility(View.INVISIBLE);

        plates = (ExpandableListView) rootView.findViewById(R.id.plate_search_list);



        //platesFilter_adapter = new PlateSearchListAdapter(getActivity(), shopList);

        //plates.setAdapter(platesFilter_adapter);

        plates.setVisibility(View.INVISIBLE);


        return rootView;
    }

    private void initActionBar() {

        actionBar = getActivity().getActionBar();

        actionBar.setDisplayShowHomeEnabled(false);
        //getActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mTitleView = mInflater.inflate(R.layout.main_action_bar_layout,
                null);
        actionBar.setCustomView(
                mTitleView,
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT)
        );


    }

//    //method to expand all groups
//    private void expandAll() {
//        int count = platesFilter_adapter.getGroupCount();
//        for (int i = 0; i < count; i++) {
//            plates.expandGroup(i);
//        }
//    }




}
