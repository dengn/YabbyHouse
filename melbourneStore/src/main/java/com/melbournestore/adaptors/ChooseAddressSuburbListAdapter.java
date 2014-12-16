package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.melbournestore.activities.R;

public class ChooseAddressSuburbListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Handler mHandler;
    private Context mContext;
    private String areaName;
    private int areaFee;

    public ChooseAddressSuburbListAdapter(Context context, Handler handler,
                                          String areaName, int areaFee) {


        mContext = context;
        mHandler = handler;

        this.areaName = areaName;
        this.areaFee = areaFee;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(String areaName, int areaFee) {

        this.areaName = areaName;
        this.areaFee = areaFee;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return 1;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        viewHolder_textView holder_textview = null;

        holder_textview = new viewHolder_textView();
        convertView = inflater.inflate(R.layout.choose_address_area, parent,
                false);

        holder_textview.title = (TextView) convertView
                .findViewById(R.id.phone_title);
        holder_textview.info = (TextView) convertView
                .findViewById(R.id.phone_info);

        holder_textview.title.setText(areaName);

        holder_textview.info.setText("配送费: $" + String.valueOf(areaFee));

        convertView.setTag(holder_textview);

        return convertView;
    }

    class viewHolder_textView {

        private TextView title;

        private TextView info;
    }

}
