package com.melbournestore.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.R;
import com.melbournestore.db.DataResourceUtils;
import com.melbournestore.models.User;
import com.melbournestore.utils.BitmapUtils;
import com.melbournestore.utils.CircleImageView;
import com.melbournestore.utils.MelbourneUtils;

public class DrawerListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    Handler mHandler;
    Context mContext;
    User[] mUsers;

    public DrawerListAdapter(Context context, Handler handler, User[] users) {
        // TODO Auto-generated constructor stub

        mContext = context;
        mHandler = handler;
        mUsers = users;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(User[] users) {

        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 6;
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


            int active_value = MelbourneUtils.getActiveUser(mUsers);


            if (active_value < 0) {
                holder_login.phone_number.setText("未登录");
                holder_login.profile
                        .setImageResource(R.drawable.sidebar_userphoto_default);
            } else {

                User active_user = mUsers[active_value];

                holder_login.phone_number.setText(active_user.getPhoneNumber());

                Bitmap profile = BitmapUtils.getMyBitMap(active_user.getPhoneNumber());
                if (profile == null) {
                    holder_login.profile
                            .setImageResource(R.drawable.profile_userphoto);
                } else {
                    holder_login.profile.setImageBitmap(profile);
                }
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
