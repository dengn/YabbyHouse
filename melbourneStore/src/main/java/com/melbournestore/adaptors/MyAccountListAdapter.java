package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.R;
import com.melbournestore.models.User;
import com.melbournestore.utils.BitmapUtils;

public class MyAccountListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private User mActiveUser;

    public MyAccountListAdapter(Context context, Handler handler, User activeUser) {
        // TODO Auto-generated constructor stub

        mContext = context;
        mHandler = handler;
        mActiveUser = activeUser;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(User activeUser) {
        mActiveUser = activeUser;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
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

        viewHolder_profile holder_profile = null;
        viewHolder_like holder_like = null;


        switch (position) {
            case 0:
                holder_profile = new viewHolder_profile();
                convertView = inflater.inflate(R.layout.myaccount_list_profile,
                        parent, false);

                holder_profile.profile = (ImageView) convertView
                        .findViewById(R.id.myaccount_profile_image);
                holder_profile.number = (TextView) convertView
                        .findViewById(R.id.myaccount_profile_number);

                // set images
                if (BitmapUtils.getMyBitMap(mActiveUser.getPhoneNumber()) == null) {
                    holder_profile.profile
                            .setImageResource(R.drawable.profile_userphoto);
                } else {
                    holder_profile.profile.setImageBitmap(BitmapUtils.getMyBitMap(mActiveUser.getPhoneNumber()));
                }

                holder_profile.number.setText(mActiveUser.getPhoneNumber());

                holder_profile.profile.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Message message = new Message();

                        message.what = 1;

                        mHandler.sendMessage(message);
                    }

                });

                convertView.setTag(holder_profile);

                break;
            case 1:
                holder_like = new viewHolder_like();
                convertView = inflater.inflate(R.layout.myaccount_list_like,
                        parent, false);

                holder_like.like = (TextView) convertView
                        .findViewById(R.id.myaccount_like);
                holder_like.order = (TextView) convertView
                        .findViewById(R.id.myaccount_order);
                holder_like.coupon = (TextView) convertView
                        .findViewById(R.id.myaccount_coupon);

                holder_like.like.setText("0\n喜欢");
                if (mActiveUser.getOrders() == null) {
                    holder_like.order.setText("0\n订单");
                } else {
                    holder_like.order.setText(String.valueOf(mActiveUser.getOrders().length) + "\n订单");
                }

                holder_like.coupon.setText("0\n优惠券");

                convertView.setTag(holder_like);

                break;


            default:
                break;
        }
        return convertView;

    }

    class viewHolder_profile {

        private ImageView profile;

        private TextView number;
    }

    class viewHolder_like {

        private TextView like;

        private TextView order;

        private TextView coupon;
    }


}
