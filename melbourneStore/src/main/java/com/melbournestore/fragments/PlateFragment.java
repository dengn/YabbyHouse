package com.melbournestore.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
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
import com.melbournestore.adaptors.PlateSearchListAdapter;
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;
import com.melbournestore.network.ShopManagerThread;

import java.util.ArrayList;

public class PlateFragment extends Fragment {

    Context mContext;

    ListView category;

    ExpandableListView plates;

    ShopManagerThread mShopThread;
    CategoryListAdapter category_adapter;
    PlateSearchListAdapter platesFilter_adapter;
    boolean header_created = false;
    ActionBar actionBar;
    SearchView search_plate;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                case 2:


                    break;

            }
        }
    };
    private ArrayList<Shop> shopList = new ArrayList<Shop>();

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
                plates.setVisibility(View.VISIBLE);
                expandAll();
            }
        });

        search_plate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                //plates.setVisibility(View.VISIBLE);
                platesFilter_adapter.filterData(query);
                expandAll();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                //plates.setVisibility(View.VISIBLE);
                platesFilter_adapter.filterData(query);
                expandAll();
                return false;
            }
        });
        search_plate.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                plates.setVisibility(View.INVISIBLE);
                platesFilter_adapter.filterData("");
                expandAll();
                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item selection
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

        mShopThread = new ShopManagerThread(mHandler);
        mShopThread.start();

        View rootView = inflater.inflate(R.layout.fragment_plate, container,
                false);

        category = (ListView) rootView.findViewById(R.id.category);


        String[] shopSubItems = new String[DataResourceUtils.shopItemsImages.length];
        for (int i = 0; i < shopSubItems.length; i++) {
            shopSubItems[i] = DataResourceUtils.plateNames[i][0] + "、 "
                    + DataResourceUtils.plateNames[i][1] + "限时特卖中";
        }
        category_adapter = new CategoryListAdapter(getActivity(),
                DataResourceUtils.shopItemsImages, DataResourceUtils.shopItems,
                shopSubItems);
        category.setAdapter(category_adapter);


        plates = (ExpandableListView) rootView.findViewById(R.id.plate_search_list);

        loadSomeData();

        //platesFilter_adapter = new PlatesFilterListAdapter(getActivity(), MelbourneUtils.getAllPlateNames());

        platesFilter_adapter = new PlateSearchListAdapter(getActivity(), shopList);

        plates.setAdapter(platesFilter_adapter);

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

    //method to expand all groups
    private void expandAll() {
        int count = platesFilter_adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            plates.expandGroup(i);
        }
    }

    private void loadSomeData() {


        ArrayList<Plate> plateList = new ArrayList<Plate>();
        Plate plate = new Plate(15, "麻辣小龙虾", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "蒜泥小龙虾", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "泡椒小龙虾", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "咖喱小龙虾", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "小龙虾炒年糕", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);


        Shop shop = new Shop(1, "龙虾叔叔", "123", "123", "06458789", 1, "123", "456", plateList.toArray(new Plate[plateList.size()]));

        shopList.add(shop);

        plateList = new ArrayList<Plate>();

        plate = new Plate(15, "A套餐", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "B套餐", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "C套餐", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "D套餐", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "E套餐", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        shop = new Shop(2, "黑丸嫩仙草", "123", "123", "06458789", 1, "123", "456", plateList.toArray(new Plate[plateList.size()]));

        shopList.add(shop);


        plateList = new ArrayList<Plate>();

        plate = new Plate(15, "水饺", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "煎饺", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "猪肉水饺", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "芹菜水饺", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        plate = new Plate(45, "白菜水饺", 0, 55, 121, 0, 0, 1);
        plateList.add(plate);

        shop = new Shop(3, "水饺妞妞", "123", "123", "06458789", 1, "123", "456", plateList.toArray(new Plate[plateList.size()]));

        shopList.add(shop);
    }


}
