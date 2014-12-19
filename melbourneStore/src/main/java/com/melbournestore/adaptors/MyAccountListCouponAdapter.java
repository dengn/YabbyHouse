package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.MyCouponActivity;
import com.melbournestore.activities.R;

public class MyAccountListCouponAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;

    public MyAccountListCouponAdapter(Context context, Handler handler) {


        mContext = context;
        mHandler = handler;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh() {

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


        viewHolder_coupon holder_coupon = null;

        holder_coupon = new viewHolder_coupon();
        convertView = inflater.inflate(R.layout.myaccount_list_coupon, parent,
                false);

        holder_coupon.title = (TextView) convertView
                .findViewById(R.id.myaccount_coupon_title);
        holder_coupon.rightArrow = (ImageView) convertView
                .findViewById(R.id.myaccount_coupon_rightarrow);

        holder_coupon.title.setText("我的优惠券");
        holder_coupon.rightArrow
                .setImageResource(R.drawable.other_icon_rightarrow);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext,
                        MyCouponActivity.class);
                intent.putExtra("callSource", 0);
                ((Activity) mContext).startActivity(intent);
            }

        });


        convertView.setTag(holder_coupon);


        return convertView;

    }


    class viewHolder_coupon {

        private TextView title;

        private ImageView rightArrow;

    }

}
