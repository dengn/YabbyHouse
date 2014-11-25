package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.melbournestore.activities.CurrentOrderActivity;
import com.melbournestore.activities.R;
import com.melbournestore.models.User;
import com.melbournestore.utils.MelbourneUtils;

public class MyOrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private User mUser;

    private int mRightWidth = 0;

    public MyOrderListAdapter(Context context, int rightWidth, Handler handler, User user) {

        mContext = context;
        mRightWidth = rightWidth;
        mHandler = handler;
        mUser = user;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(User user) {
        mUser = user;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mUser.getOrders() == null) {
            return 0;
        } else {
            return mUser.getOrders().length;
        }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.myorder_list_item, null);

        holder.time_view = (TextView) rowView.findViewById(R.id.myorder_time);
        holder.names_view = (TextView) rowView.findViewById(R.id.myorder_names);
        holder.price_view = (TextView) rowView.findViewById(R.id.myorder_price);
        holder.status_view = (TextView) rowView.findViewById(R.id.myorder_status);

        holder.item_left = (View) rowView.findViewById(R.id.item_left);
        holder.item_right = (View) rowView.findViewById(R.id.item_right);

        holder.delete = (TextView) rowView.findViewById(R.id.myorder_delete);

        holder.time_view.setText(mUser.getOrders()[position].getCreateTime());
        holder.names_view.setText(MelbourneUtils.getAllPlateNames(mUser.getOrders()[position].getPlates()));
        holder.price_view.setText(String.valueOf(MelbourneUtils.sum_price_all(mUser.getOrders()[position].getPlates()) + mUser.getOrders()[position].getDeliveryFee()));

        holder.status_view.setText(MelbourneUtils.getStatusString(mUser.getOrders()[position].getStatus()));

        holder.delete.setText("删除");

        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("DEBUG", "myorder_list_item clicked");

                Intent intent = new Intent(mContext, CurrentOrderActivity.class);
                intent.putExtra("position", position);
                ((Activity) mContext).startActivity(intent);
            }

        });

        holder.delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Message message = new Message();
                // delete
                message.what = 1;

                Bundle b = new Bundle();
                b.putInt("position", position);
                message.setData(b);

                mHandler.sendMessage(message);


            }

        });


        return rowView;
    }

    public class Holder {
        TextView time_view;
        TextView names_view;
        TextView price_view;

        TextView status_view;

        View item_left;

        View item_right;

        TextView delete;
    }

}
