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

import com.melbournestore.activities.ChooseAddressActivity;
import com.melbournestore.activities.MyAccountActivity;
import com.melbournestore.activities.R;
import com.melbournestore.models.user_iphone;
import com.melbournestore.utils.MelbourneUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MyAccountListAddressAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private DisplayImageOptions mOptions;
    private user_iphone mUser;

    public MyAccountListAddressAdapter(Context context, Handler handler,
                                       DisplayImageOptions options, user_iphone user) {

        mContext = context;
        mHandler = handler;
        mOptions = options;
        mUser = user;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(user_iphone user) {
        mUser = user;

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


        viewHolder_address holder_address = null;

        holder_address = new viewHolder_address();
        convertView = inflater.inflate(R.layout.myaccount_list_address, parent,
                false);

        holder_address.title = (TextView) convertView
                .findViewById(R.id.myaccount_address_title);
        holder_address.address = (TextView) convertView
                .findViewById(R.id.myaccount_address_info);
        holder_address.rightArrow = (ImageView) convertView
                .findViewById(R.id.myaccount_address_rightarrow);

        holder_address.title.setText("送货地址");

        String address = "";

        address = MelbourneUtils.getCompleteAddress(mUser);

        if(address.length()>26){
            address=address.substring(0,26)+"...";
        }

        holder_address.address.setText(address);
        holder_address.rightArrow
                .setImageResource(R.drawable.other_icon_rightarrow);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext,
                        ChooseAddressActivity.class);
                ((Activity) mContext).startActivityForResult(intent,
                        MyAccountActivity.choose_address_code);
            }

        });

        convertView.setTag(holder_address);

        return convertView;

    }

    class viewHolder_address {

        private TextView title;

        private TextView address;

        private ImageView rightArrow;
    }

}
