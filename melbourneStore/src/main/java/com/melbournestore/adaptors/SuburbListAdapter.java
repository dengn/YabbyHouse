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
    private ArrayList<Area> mAreaList;
    private ArrayList<Area> mOriginalList;

    public SuburbListAdapter(Context context, ArrayList<Area> headList) {
        mContext = context;
        mAreaList = new ArrayList<Area>();
        mAreaList.addAll(headList);
        mOriginalList = new ArrayList<Area>();
        mOriginalList.addAll(headList);
    }

    @Override
    public int getGroupCount() {
        return mAreaList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Suburb> suburbList = mAreaList.get(groupPosition).getSuburbs();
        return suburbList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mAreaList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Suburb> suburbList = mAreaList.get(groupPosition).getSuburbs();
        return suburbList.get(childPosition);
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

        query = query.toLowerCase();

        mAreaList.clear();

        if (query.isEmpty()) {
            mAreaList.addAll(mOriginalList);
        } else {

            for (Area area : mOriginalList) {

                ArrayList<Suburb> suburbList = area.getSuburbs();
                ArrayList<Suburb> newList = new ArrayList<Suburb>();
                for (Suburb suburb : suburbList) {
                    if (suburb.getName().toLowerCase().contains(query)) {
                        newList.add(suburb);
                    }
                }
                if (newList.size() > 0) {
                    Area nArea = new Area(area.getId(), area.getName(), area.getFee(), area.getStatus(), newList, area.getUpdateTime());
                    mAreaList.add(nArea);
                }
            }
        }

        notifyDataSetChanged();

    }
}
