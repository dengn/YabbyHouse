package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.melbournestore.activities.DishActivity;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.item_iphone;
import com.melbournestore.network.ItemGoodManagerThread;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class PlateListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context mContext;
    Handler mHandler;
    ArrayList<item_iphone> mItems = new ArrayList<item_iphone>();
    DisplayImageOptions mOptions;
    private Gson gson = new Gson();


    public PlateListAdapter(Context context, Handler handler, DisplayImageOptions options, ArrayList<item_iphone> items) {

        mContext = context;
        mHandler = handler;
        mOptions = options;

        mItems.clear();
        mItems.addAll(items);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(ArrayList<item_iphone> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mItems.size() + 1;
    }

    @Override
    public Object getItem(int position) {

        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (position == mItems.size()) {
            View view;
            view = inflater.inflate(R.layout.empty, null);
            view.setVisibility(View.INVISIBLE);
            return view;
        } else {

            final Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.plate_list_item, null);
            holder.names_view = (TextView) rowView.findViewById(R.id.plate_name);
            holder.prices_view = (TextView) rowView.findViewById(R.id.plate_price);

            holder.imgs_view = (ImageView) rowView.findViewById(R.id.plate_img);
            holder.num_view = (TextView) rowView.findViewById(R.id.plate_number);

            holder.like_number_view = (TextView) rowView
                    .findViewById(R.id.plate_like_number_stock);
            holder.like_view = (ImageView) rowView
                    .findViewById(R.id.plate_like_heart);

            holder.like_view.setImageResource(R.drawable.other_icon_praise);


            holder.like_number_view.setText(String.valueOf(mItems.get(position).getGood())
                    + "         今日库存" + mItems.get(position).getStock() + "份");

            holder.plus = (Button) rowView.findViewById(R.id.plate_plus);
            holder.minus = (Button) rowView.findViewById(R.id.plate_minus);


            holder.names_view.setText(mItems.get(position).getName());
            holder.prices_view
                    .setText("$" + String.valueOf(mItems.get(position).getPrice()));


            ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mItems.get(position).getImage(), holder.imgs_view, mOptions);


            if (mItems.get(position).getUnit() <= 0) {
                holder.num_view.setVisibility(View.INVISIBLE);
            } else {
                holder.num_view.setVisibility(View.VISIBLE);
                holder.num_view.setText(String.valueOf(mItems.get(position).getUnit()));
            }



            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(mContext, DishActivity.class);
                    intent.putExtra("item_id", mItems.get(position).getId());
                    intent.putExtra("item_name", mItems.get(position).getName());

                    ((Activity) mContext).startActivity(intent);

                }

            });

            holder.like_number_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String number = SharedPreferenceUtils.getUserNumber(mContext);
                    ItemGoodManagerThread itemThread = new ItemGoodManagerThread(mHandler, mContext, mItems.get(position).getId(), number);
                    itemThread.start();


                }

            });

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Message message = new Message();
                    Bundle b = new Bundle();
                    // send the position
                    b.putInt("position", position);
                    message.setData(b);

                    // plus = 1
                    message.what = 1;

                    if (mItems.get(position).getUnit() < mItems.get(position).getStock()) {
                        int mShopId1 = mItems.get(position).getShopId();
                        mItems.get(position).setUnit(mItems.get(position).getUnit() + 1);
                        SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(mItems), mShopId1);
                    }

                    holder.num_view.setText(String.valueOf(mItems.get(position).getUnit()));

                    setComponentsStatus(holder.plus, holder.minus, holder.num_view,
                            position);

                    mHandler.sendMessage(message);

                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Message message = new Message();
                    Bundle b = new Bundle();
                    // send the position
                    b.putInt("position", position);
                    message.setData(b);

                    // minus = 2
                    message.what = 2;


                    if (mItems.get(position).getUnit() > 0) {
                        int mShopId2 = mItems.get(position).getShopId();
                        mItems.get(position).setUnit(mItems.get(position).getUnit() - 1);
                        SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(mItems), mShopId2);
                    }

                    holder.num_view.setText(String.valueOf(mItems.get(position).getUnit()));

                    setComponentsStatus(holder.plus, holder.minus, holder.num_view,
                            position);

                    mHandler.sendMessage(message);

                }
            });

            return rowView;
        }
    }

    private void setComponentsStatus(Button plusButton, Button minusButton,
                                     TextView numView, int position) {
        int stock_num = mItems.get(position).getStock();
        int plate_num = mItems.get(position).getUnit();

        if (plate_num >= stock_num) {
            plusButton.setEnabled(false);
        } else {
            plusButton.setEnabled(true);
        }
        if (plate_num <= 0) {
            numView.setVisibility(View.INVISIBLE);
            minusButton.setEnabled(false);
        } else {
            numView.setVisibility(View.VISIBLE);
            minusButton.setEnabled(true);
        }
    }

    public class Holder {
        TextView names_view;
        TextView prices_view;
        TextView stocks_view;
        ImageView imgs_view;

        TextView num_view;

        TextView like_number_view;
        ImageView like_view;

        Button plus;
        Button minus;
    }

}
