package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.user_coupon;

import java.util.ArrayList;

public class MyCouponListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private user_coupon[] mCoupons;
    private Gson gson = new Gson();
    private int mCallSource = 0;
    private boolean CouponSelected = false;
    private ArrayList<user_coupon> mUserCoupons = new ArrayList<user_coupon>();



    private int mRightWidth = 0;

    public MyCouponListAdapter(Context context, Handler handler, user_coupon[] coupons, int callSource) {

        mContext = context;
        mHandler = handler;
        mCoupons = coupons;
        mCallSource = callSource;

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

        holder.coupon_tick = (ImageView) rowView.findViewById(R.id.coupon_tick);

        holder.coupon_tick.setVisibility(View.INVISIBLE);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallSource==0){
                    CouponSelected = false;
                    holder.coupon_tick.setVisibility(View.INVISIBLE);
                }
                else if(!CouponSelected){
                    CouponSelected = true;
                    holder.coupon_tick.setVisibility(View.VISIBLE);
                    mUserCoupons.add(mCoupons[position]);

                    SharedPreferenceUtils.saveUserCoupons(mContext, gson.toJson(mUserCoupons));

                }
                else{
                    CouponSelected = false;
                    holder.coupon_tick.setVisibility(View.INVISIBLE);
                    if(mUserCoupons.contains(mCoupons[position])){
                        mUserCoupons.remove(mCoupons[position]);
                        SharedPreferenceUtils.saveUserCoupons(mContext, gson.toJson(mUserCoupons));
                    }
                }
            }
        });


        holder.coupon_name.setText(mCoupons[position].getCoupon().getName());
        if (!mCoupons[position].getCoupon().getValid_date().equals("")) {
            holder.coupon_valid.setText("有效期至" + mCoupons[position].getCoupon().getValid_date());
        }
        return rowView;
    }

    public class Holder {
        TextView coupon_name;
        TextView coupon_valid;

        ImageView coupon_tick;

    }

}
