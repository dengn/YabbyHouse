package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.melbournestore.activities.R;

public class PlatesFilterListAdapter extends ArrayAdapter<String> {

    Context mContext;
    int mLayoutResourceId;
    String[] mPlates;


    public PlatesFilterListAdapter(Context context, int layoutResourceId, String[] plates) {

        super(context, layoutResourceId, plates);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mPlates = plates;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        rowView = inflater.inflate(mLayoutResourceId, parent, false);

        holder.plate = (TextView) rowView.findViewById(R.id.plate_item);
        holder.plate.setText(mPlates[position]);

        rowView.setTag(holder);

        return rowView;
    }

    public class Holder {

        TextView plate;

    }
}
