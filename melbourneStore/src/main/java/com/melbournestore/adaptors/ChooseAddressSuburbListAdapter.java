package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.melbournestore.activities.R;
import com.melbournestore.utils.MelbourneUtils;

public class ChooseAddressSuburbListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Handler mHandler;
    private Context mContext;
    private String addr_suburb;

    public ChooseAddressSuburbListAdapter(Context context, Handler handler,
                                          String suburb) {
        // TODO Auto-generated constructor stub

        mContext = context;
        mHandler = handler;

        addr_suburb = suburb;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(String suburb) {

        addr_suburb = suburb;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        viewHolder_textView holder_textview = null;

        holder_textview = new viewHolder_textView();
        convertView = inflater.inflate(R.layout.submit_list_item_phone, parent,
                false);

        holder_textview.title = (TextView) convertView
                .findViewById(R.id.phone_title);
        holder_textview.info = (TextView) convertView
                .findViewById(R.id.phone_info);

        holder_textview.title.setText(MelbourneUtils
                .getSuburbRegion(addr_suburb));

        holder_textview.info.setText("配送费: $" + String.valueOf(MelbourneUtils
                .getSuburbDeliveryPrice(addr_suburb)));

        convertView.setTag(holder_textview);

        return convertView;
    }

    class viewHolder_textView {

        private TextView title;

        private TextView info;
    }

}
