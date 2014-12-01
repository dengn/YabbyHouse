package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.ChooseAddressActivity;
import com.melbournestore.activities.R;
import com.melbournestore.activities.SubmitOrderActivity;
import com.melbournestore.models.Order_user;
import com.melbournestore.models.User;

public class SubmitListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    final int TYPE_TEXT = 0;
    final int TYPE_ACTIVITY = 1;
    final int TYPE_CHECKBOX = 2;
    Handler mHandler;
    Context mContext;
    User mActiveUser;
    Order_user mCurrentOrder;

    public SubmitListAdapter(Context context, Handler handler, User activeUser, Order_user currentOrder) {
        // TODO Auto-generated constructor stub


        mContext = context;
        mHandler = handler;
        mActiveUser = activeUser;
        mCurrentOrder = currentOrder;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(User activeUser, Order_user currentOrder) {

        mActiveUser = activeUser;
        mCurrentOrder = currentOrder;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        int p = position;
        if (p == 0)
            return TYPE_TEXT;
        else if (p == 1)
            return TYPE_ACTIVITY;
        else
            return TYPE_CHECKBOX;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
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
        viewHolder_text holder_text = null;
        viewHolder_activity holder_activity = null;
        viewHolder_checkbox holder_checkbox = null;


        int type = getItemViewType(position);

        switch (type) {
            case TYPE_TEXT:
                holder_text = new viewHolder_text();
                convertView = inflater.inflate(R.layout.submit_list_item_phone, parent, false);

                holder_text.title = (TextView) convertView.findViewById(R.id.phone_title);
                holder_text.info = (TextView) convertView.findViewById(R.id.phone_info);

                holder_text.title.setText("电话号码");
                holder_text.info.setText(mActiveUser.getPhoneNumber());

                convertView.setTag(holder_text);


                break;
            case TYPE_ACTIVITY:
                holder_activity = new viewHolder_activity();
                convertView = inflater.inflate(R.layout.submit_list_item_address, parent, false);

                holder_activity.title = (TextView) convertView.findViewById(R.id.address_title);
                holder_activity.info = (TextView) convertView.findViewById(R.id.address_info);
                holder_activity.rightArrow = (ImageView) convertView.findViewById(R.id.address_rightarrow);

                String address = "";

                //address = MelbourneUtils.getCompleteAddress(mActiveUser);

                holder_activity.title.setText("运送地址");
                holder_activity.info.setHint("详细地址");
                holder_activity.info.setText(address);
                holder_activity.rightArrow.setImageResource(R.drawable.other_icon_rightarrow);

                convertView.setTag(holder_activity);

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mContext, ChooseAddressActivity.class);
                        ((Activity) mContext).startActivityForResult(intent, SubmitOrderActivity.result_code_address);
                    }

                });

                break;
            case TYPE_CHECKBOX:

                holder_checkbox = new viewHolder_checkbox();
                convertView = inflater.inflate(R.layout.submit_list_item_time, parent, false);

                holder_checkbox.title = (TextView) convertView.findViewById(R.id.time_title);
                holder_checkbox.info = (TextView) convertView.findViewById(R.id.time_info);
                holder_checkbox.rightArrow = (ImageView) convertView.findViewById(R.id.time_rightarrow);


                holder_checkbox.title.setText("运送时间");
                holder_checkbox.info.setHint("范围20:00 - 24:00");
                holder_checkbox.info.setText(mCurrentOrder.getDeliveryTime());
                holder_checkbox.rightArrow.setImageResource(R.drawable.other_icon_rightarrow);

                convertView.setTag(holder_checkbox);

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //popup the delivery time picker
                        Message message = new Message();
                        Bundle b = new Bundle();
                        // send the action to do
                        // action = 2 open time picker
                        b.putInt("action", 2);
                        message.setData(b);


                        message.what = 1;

                        mHandler.sendMessage(message);
                    }

                });
                break;

            default:
                break;
        }
        return convertView;

    }


    class viewHolder_text {

        private TextView title;

        private TextView info;
    }

    class viewHolder_activity {

        private TextView title;

        private TextView info;

        private ImageView rightArrow;
    }

    class viewHolder_checkbox {

        private TextView title;

        private TextView info;

        private ImageView rightArrow;
    }


}
