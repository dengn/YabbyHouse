package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Coupon;
import com.melbournestore.models.user_coupon;

public class UseCouponAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;

    private Handler mHandler;
    private Context mContext;
    private user_coupon mChosenCoupon;
    private Gson gson = new Gson();

    public UseCouponAdapter(Context context, Handler handler, user_coupon chosenCoupon) {


        mContext = context;
        mHandler = handler;
        mChosenCoupon = chosenCoupon;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(user_coupon chosenCoupon) {
        mChosenCoupon = chosenCoupon;
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        viewHolder holder = null;


        holder = new viewHolder();
        convertView = inflater.inflate(R.layout.usecoupon_list_item, parent, false);

        holder.title = (TextView) convertView.findViewById(R.id.usecoupon_name);
        holder.notUse = (Button) convertView.findViewById(R.id.not_use_coupon);


        holder.title.setText(mChosenCoupon.getCoupon().getName());
        holder.notUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferenceUtils.saveUserCoupons(mContext, gson.toJson(new user_coupon(-1, -1, -1, "", new Coupon(-1, "", "", "", -1, "", "", -1, -1))));

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
