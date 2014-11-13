package com.melbournestore.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.melbournestore.activities.R;
import com.melbournestore.models.Area;
import com.melbournestore.models.Suburb;

import java.util.ArrayList;

/**
 * Created by OLEDCOMM on 13/11/2014.
 */
public class SuburbListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<Area> mHeadList;
    private ArrayList<Suburb> mChildList;

    public SuburbListAdapter(Context context, ArrayList<Area> headList, ArrayList<Suburb> childList) {
        mContext = context;
        mHeadList = new ArrayList<Area>();
        mHeadList.addAll(headList);
        mChildList = new ArrayList<Suburb>();
        mChildList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return mHeadList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mChildList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mHeadList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Suburb[] suburbList = mHeadList.get(groupPosition).getSuburbs();
        return suburbList[childPosition];
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

        Area area = (Area) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.suburb_head_item, null);
        }

        TextView heading = (TextView) convertView.findViewById(R.id.suburb_head);
        heading.setText(area.getName().trim());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Suburb suburb = (Suburb) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.suburb_child_item, null);
        }

        TextView child = (TextView) convertView.findViewById(R.id.suburb_head);
        child.setText(suburb.getName().trim());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void filterData(String query){

//        query = query.toLowerCase();
//
//        mHeadList.clear();
//
//        if(query.isEmpty()){
//            mHeadList.addAll(mChildList);
//        }
//        else {
//
//            for(Continent continent: originalList){
//
//                ArrayList<Country> countryList = continent.getCountryList();
//                ArrayList<Country> newList = new ArrayList<Country>();
//                for(Country country: countryList){
//                    if(country.getCode().toLowerCase().contains(query) ||
//                            country.getName().toLowerCase().contains(query)){
//                        newList.add(country);
//                    }
//                }
//                if(newList.size() > 0){
//                    Continent nContinent = new Continent(continent.getName(),newList);
//                    continentList.add(nContinent);
//                }
//            }
//        }
//
//        Log.v("MyListAdapter", String.valueOf(continentList.size()));
//        notifyDataSetChanged();

    }
}
