package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.melbournestore.activities.DishActivity;
import com.melbournestore.activities.R;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;

import java.util.ArrayList;

/**
 * Created by dengn on 2014/11/17.
 */
public class PlateSearchListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    //private Handler mHandler;
    private ArrayList<Shop> mShopList;
    private ArrayList<Shop> mOriginalList;

    public PlateSearchListAdapter(Context context, ArrayList<Shop> headList) {
        mContext = context;
        //mHandler = handler;
        mShopList = new ArrayList<Shop>();
        mShopList.addAll(headList);
        mOriginalList = new ArrayList<Shop>();
        mOriginalList.addAll(headList);
    }

    @Override
    public int getGroupCount() {
        return mShopList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Plate> plateList = mShopList.get(groupPosition).getArrayPlates();
        return plateList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mShopList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Plate> plateList = mShopList.get(groupPosition).getArrayPlates();
        return plateList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Shop shop = (Shop) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.suburb_head_item, null);
        }

        TextView heading = (TextView) convertView.findViewById(R.id.suburb_head);
        heading.setText(shop.getName().trim());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Plate plate = (Plate) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.suburb_child_item, null);
        }

        TextView child = (TextView) convertView.findViewById(R.id.suburb_child);
        child.setText(plate.getName().trim());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //jump to the corresponding plate page
                Intent intent = new Intent(mContext, DishActivity.class);
                intent.putExtra("plateId", childPosition);
                intent.putExtra("shopId", groupPosition);

                ((Activity) mContext).startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void filterData(String query) {

        query = query.toLowerCase();

        mShopList.clear();

        if (query.isEmpty()) {
            mShopList.addAll(mOriginalList);
        } else {

            for (Shop shop : mOriginalList) {

                ArrayList<Plate> plateList = shop.getArrayPlates();
                ArrayList<Plate> newList = new ArrayList<Plate>();

                //Plate[] newListArray = newList.toArray(new Plate[newList.size()]);
                for (Plate plate : plateList) {
                    if (plate.getName().toLowerCase().contains(query)) {
                        newList.add(plate);
                    }
                }
                if (newList.size() > 0) {
                    Shop nShop = new Shop(shop.getId(), shop.getName(), shop.getDesc(), shop.getAddr(), shop.getContactNumber(), shop.getSeq(), shop.getImage(), shop.getUpdateTime(), newList.toArray(new Plate[newList.size()]));
                    mShopList.add(nShop);
                }
            }
        }

        notifyDataSetChanged();

    }
}
