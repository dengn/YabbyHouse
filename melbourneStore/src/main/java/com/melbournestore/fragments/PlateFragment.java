package com.melbournestore.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
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
import com.melbournestore.models.Shop;

import java.util.ArrayList;

public class PlateFragment extends Fragment {

    Context mContext;

    ListView category;

    ExpandableListView plates;

    CategoryListAdapter category_adapter;
    PlateSearchListAdapter platesFilter_adapter;
    Boolean header_created = false;
    ActionBar actionBar = getActivity().getActionBar();
    private ArrayList<Shop> shopList = new ArrayList<Shop>();


    public PlateFragment(Context context) {
        mContext = context;
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.search_plate:


                if (!header_created) {
                    //category.addHeaderView(headerView);

                    item.setVisible(false);

                    header_created = true;
                } else {
                    //category.removeHeaderView(headerView);
                    header_created = false;
                    plates.setVisibility(View.INVISIBLE);
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initActionBar();

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

        //platesFilter_adapter = new PlatesFilterListAdapter(getActivity(), MelbourneUtils.getAllPlateNames());

        platesFilter_adapter = new PlateSearchListAdapter(getActivity(), shopList);

        plates.setAdapter(platesFilter_adapter);

        plates.setVisibility(View.INVISIBLE);

        // headerView.setVisibility(View.INVISIBLE);

        // category.setOnTouchListener(new OnTouchListener(){
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // // TODO Auto-generated method stub
        // // switch (event.getAction()) {
        // // case MotionEvent.ACTION_DOWN:
        // // headerView.setVisibility(View.VISIBLE);
        // // break;
        // // case MotionEvent.ACTION_UP:
        // // headerView.setVisibility(View.INVISIBLE);
        // // break;
        // // }
        // return false;
        // }
        //
        // });

        return rootView;
    }

    private void initActionBar() {

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
        SearchView search_suburb = (SearchView) mTitleView.findViewById(R.id.plate_search_view);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        search_suburb.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search_suburb.setIconifiedByDefault(false);
        search_suburb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        search_suburb.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

    }


}
