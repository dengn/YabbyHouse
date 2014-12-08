package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.user_coupon;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UseCouponAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    final int TYPE_URL = 0;
    final int TYPE_EMPTY = 1;
    Handler mHandler;
    Context mContext;
    private Gson gson = new Gson();
    ArrayList<user_coupon> mCoupons = new ArrayList<user_coupon>();

    public UseCouponAdapter(Context context, Handler handler, ArrayList<user_coupon> coupons) {
        // TODO Auto-generated constructor stub


        mContext = context;
        mHandler = handler;
        mCoupons.clear();
        mCoupons.addAll(coupons);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(ArrayList<user_coupon> coupons) {
        mCoupons.clear();
        mCoupons.addAll(coupons);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {

        return mCoupons.size();
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


        viewHolder holder = null;


        holder = new viewHolder();
        convertView = inflater.inflate(R.layout.usecoupon_list_item, parent, false);

        holder.title = (TextView) convertView.findViewById(R.id.usecoupon_name);
        holder.notUse = (Button) convertView.findViewById(R.id.not_use_coupon);

        holder.title.setText(mCoupons.get(position).getCoupon().getName());
        holder.notUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String couponString = SharedPreferenceUtils.getUserCoupons(mContext);
                Log.d("COUPON", "before: " + couponString);

                Type listType = new TypeToken<ArrayList<user_coupon>>() {
                }.getType();
                ArrayList<user_coupon> coupons = new ArrayList<user_coupon>();
                coupons = gson.fromJson(couponString, listType);
                coupons.remove(position);
                SharedPreferenceUtils.saveUserCoupons(mContext, gson.toJson(coupons));

                //SharedPreferenceUtils.saveUserCoupons(mContext, "[]");

                Message msg = mHandler.obtainMessage();
                msg.what = 4;
                mHandler.sendMessage(msg);
            }
        });

        return convertView;

    }


    class viewHolder {

        public TextView title;

        public Button notUse;

    }


}
