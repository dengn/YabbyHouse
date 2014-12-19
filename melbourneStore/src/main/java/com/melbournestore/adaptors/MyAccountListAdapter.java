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
import com.melbournestore.models.user_iphone;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyAccountListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private DisplayImageOptions mOptions;
    private user_iphone mUser;
    private int mOrderNum;
    private int mCouponNum;

    public MyAccountListAdapter(Context context, Handler handler, DisplayImageOptions options, user_iphone user, int orderNum, int couponNum) {

        mContext = context;
        mHandler = handler;
        mOptions = options;
        mUser = user;
        mOrderNum = orderNum;
        mCouponNum = couponNum;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(user_iphone user, int orderNum, int couponNum) {
        mUser = user;
        mOrderNum = orderNum;
        mCouponNum = couponNum;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return 2;
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


                if (mUser.getPhoneNumber().equals("")) {
                    holder_profile.number.setText("未登录");
                    holder_profile.profile
                            .setImageResource(R.drawable.sidebar_userphoto_default);
                } else {


                    holder_profile.number.setText(mUser.getPhoneNumber());


                    ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mUser.getHead_icon(), holder_profile.profile, mOptions);

                }



                holder_profile.profile.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Message message = new Message();

                        message.what = 0;

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

                holder_like.like.setText(String.valueOf(mUser.getGood_count())+"\n喜欢");


                holder_like.order.setText(String.valueOf(mOrderNum)+"\n订单");


                holder_like.coupon.setText(String.valueOf(mCouponNum)+"\n优惠券");

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
