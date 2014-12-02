package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.melbournestore.activities.R;
import com.melbournestore.models.user_coupon;

public class MyCouponListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private user_coupon[] mCoupons;
    private Gson gson = new Gson();

    private int mRightWidth = 0;

    public MyCouponListAdapter(Context context, Handler handler, user_coupon[] coupons) {

        mContext = context;
        mHandler = handler;
        mCoupons = coupons;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(user_coupon[] coupons) {
        mCoupons = coupons;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mCoupons == null) {
            return 0;
        } else {
            return mCoupons.length;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.mycoupon_list_item, null);

        holder.coupon_name = (TextView) rowView.findViewById(R.id.coupon_name);
        holder.coupon_valid = (TextView) rowView.findViewById(R.id.coupon_validtime);

        holder.coupon_name.setText(mCoupons[position].getCoupon().getName());
        if (!mCoupons[position].getCoupon().getValid_date().equals("")) {
            holder.coupon_valid.setText("有效期至" + mCoupons[position].getCoupon().getValid_date());
        }
        return rowView;
    }

    public class Holder {
        TextView coupon_name;
        TextView coupon_valid;

    }

}
