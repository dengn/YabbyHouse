package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.DeliveryNoticeActivity;
import com.melbournestore.activities.MyCouponActivity;
import com.melbournestore.activities.R;
import com.melbournestore.activities.SubmitOrderActivity;
import com.melbournestore.models.Area;
import com.melbournestore.models.user_iphone;
import com.melbournestore.utils.MelbourneUtils;

public class SubmitListCouponAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    final int TYPE_URL = 0;
    final int TYPE_EMPTY = 1;
    Handler mHandler;
    Context mContext;
    String mArea;
    int mFee;
    user_iphone mUser;

    public SubmitListCouponAdapter(Context context, Handler handler, String area, int fee, user_iphone user) {
        // TODO Auto-generated constructor stub


        mContext = context;
        mHandler = handler;
        mArea = area;
        mFee = fee;
        mUser = user;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(String area, int fee, user_iphone user) {
        mArea = area;
        mFee = fee;
        mUser = user;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        int p = position;
        if (p == 0)
            return TYPE_URL;
        else
            return TYPE_EMPTY;

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

        viewHolder_url holder_url = null;
        viewHolder_coupon holder_coupon = null;

        int type = getItemViewType(position);

        switch (type) {
            case TYPE_URL:

                holder_url = new viewHolder_url();
                convertView = inflater.inflate(R.layout.submit_list_item_delivery, parent, false);

                holder_url.title = (TextView) convertView.findViewById(R.id.delivery_title);
                holder_url.info = (TextView) convertView.findViewById(R.id.delivery_info);

                String areaName ="";
                String areaFee ="";
                if(mArea.equals("")&&(mFee==0)){
                    Area area = MelbourneUtils.getAreaFromSuburb(mUser.getSuburb(), mContext);
                    areaName = area.getName();
                    areaFee = String.valueOf(area.getFee());
                }
                else{
                    areaName =mArea;
                    areaFee =String.valueOf(mFee);
                }

                holder_url.title.setText("配送费(" + areaName + " + $" + areaFee + ")");
                holder_url.info.setText(Html.fromHtml("<u>" + "派送说明" + "</u>"));

                convertView.setTag(holder_url);

                holder_url.info.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mContext, DeliveryNoticeActivity.class);
                        mContext.startActivity(intent);
                    }

                });

                break;
            case TYPE_EMPTY:

                holder_coupon = new viewHolder_coupon();
                convertView = inflater.inflate(R.layout.submit_list_item_coupon, parent, false);

                holder_coupon.title = (TextView) convertView.findViewById(R.id.coupon_title);
                holder_coupon.rightArrow = (ImageView) convertView.findViewById(R.id.coupon_rightarrow);

                holder_coupon.title.setText("使用优惠券");
                holder_coupon.rightArrow
                        .setImageResource(R.drawable.other_icon_rightarrow);

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mContext,
                                MyCouponActivity.class);
                        intent.putExtra("callSource", 1);
                        ((Activity) mContext).startActivityForResult(intent, SubmitOrderActivity.result_coupon);
                    }

                });

                convertView.setTag(holder_coupon);
                break;
            default:
                break;
        }
        return convertView;

    }


    class viewHolder_url {

        private TextView title;

        private TextView info;
    }

    class viewHolder_coupon {

        private TextView title;

        private ImageView rightArrow;
    }
}
