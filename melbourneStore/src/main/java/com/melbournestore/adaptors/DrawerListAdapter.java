package com.melbournestore.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.R;
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.models.user_iphone;
import com.melbournestore.utils.CircleImageView;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DrawerListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    Handler mHandler;
    Context mContext;
    user_iphone mUser;
    DisplayImageOptions mOptions;

    public DrawerListAdapter(Context context, Handler handler, DisplayImageOptions options, user_iphone user) {

        mContext = context;
        mHandler = handler;
        mOptions = options;
        mUser = user;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(user_iphone user) {

        mUser = user;
        Log.d("DRAWER", "user phone number: "+mUser.getPhoneNumber());
        Log.d("DRAWER", "user head icon: "+mUser.getHead_icon());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return 6;
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

        viewHolder_normal holder_normal = null;
        viewHolder_login holder_login = null;

        // the first row
        if (position == 0) {
            holder_login = new viewHolder_login();
            convertView = inflater.inflate(R.layout.drawer_list_item_login,
                    parent, false);

            holder_login.phone_number = (TextView) convertView
                    .findViewById(R.id.drawer_login_number);
            holder_login.profile = (CircleImageView) convertView
                    .findViewById(R.id.drawer_login_image);

            holder_login.phone_number.setTextColor(Color.WHITE);


            if (mUser.getPhoneNumber().equals("")) {
                holder_login.phone_number.setText("未登录");
                holder_login.profile
                        .setImageResource(R.drawable.sidebar_userphoto_default);
            } else {


                holder_login.phone_number.setText(mUser.getPhoneNumber());


                ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mUser.getHead_icon(), holder_login.profile, mOptions);

            }

        } else {
            holder_normal = new viewHolder_normal();
            convertView = inflater.inflate(R.layout.drawer_list_item_normal,
                    parent, false);

            holder_normal.logo = (ImageView) convertView
                    .findViewById(R.id.drawer_normal_image);
            holder_normal.title = (TextView) convertView
                    .findViewById(R.id.drawer_normal_title);

            holder_normal.title.setTextColor(Color.WHITE);

            holder_normal.logo
                    .setImageResource(DataResourceUtils.drawerItemsImages[position - 1]);
            holder_normal.title
                    .setText(DataResourceUtils.drawerItemsTitles[position - 1]);
        }

        return convertView;
    }

    class viewHolder_normal {

        private ImageView logo;

        private TextView title;

    }

    class viewHolder_login {

        private CircleImageView profile;

        private TextView phone_number;

    }

}
