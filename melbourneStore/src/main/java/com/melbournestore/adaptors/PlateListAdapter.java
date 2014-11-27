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
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.activities.DishActivity;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.item_iphone;
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
    private boolean likeClicked = false;

    public PlateListAdapter(Context context, Handler handler,DisplayImageOptions options, ArrayList<item_iphone> items) {

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

        return mItems.size();
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

        holder.like_view.setImageResource(R.drawable.other_icon_like);

        holder.like_number_view.setText(String.valueOf(mItems.get(position).getGood())
                + "         今日库存" + mItems.get(position).getStock() + "份");

        holder.plus = (Button) rowView.findViewById(R.id.plate_plus);
        holder.minus = (Button) rowView.findViewById(R.id.plate_minus);

//        setComponentsStatus(holder.plus, holder.minus, holder.num_view,
//                position);

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

//                int shopId = mPlates[position].getShopId();
//                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                shops[shopId].setPlates(mPlates);
//                SharedPreferenceUtils
//                        .saveCurrentChoice(mContext, gson.toJson(shops));


                Intent intent = new Intent(mContext, DishActivity.class);
                intent.putExtra("item_id", mItems.get(position).getId());
                intent.putExtra("item_name", mItems.get(position).getName());

                ((Activity) mContext).startActivity(intent);

            }

        });

        holder.like_number_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!likeClicked) {
                    holder.like_view
                            .setImageResource(R.drawable.other_icon_liked);

                    holder.like_number_view.setText(String
                            .valueOf(mItems.get(position).getGood() + 1)
                            + "         今日库存"
                            + String.valueOf(mItems.get(position).getStock()) + "份");

//                    mItems.get(position).setGood(mItems.get(position).getGood() + 1);
//
//
//                    int shopId = mPlates[position].getShopId();
//                    String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                    Gson gson = new Gson();
//                    Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                    shops[shopId].setPlates(mPlates);
//                    SharedPreferenceUtils
//                            .saveCurrentChoice(mContext, gson.toJson(shops));

                    likeClicked = true;
                } else {
                    Toast.makeText(mContext, "亲，今天已经点过赞了。", Toast.LENGTH_SHORT)
                            .show();
                }
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

                mHandler.sendMessage(message);


//                mPlates[position].setNumber(mPlates[position].getNumber() + 1);
//
//                int shopId = mPlates[position].getShopId();
//                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                shops[shopId].setPlates(mPlates);
//                SharedPreferenceUtils
//                        .saveCurrentChoice(mContext, gson.toJson(shops));
                int mShopId1 = mItems.get(position).getShopId();
//                String itemsString1 = SharedPreferenceUtils.getLocalItems(mContext, mShopId1);
//                Type type1 = new TypeToken<ArrayList<item_iphone>>() {
//                }.getType();
//                ArrayList<item_iphone> items1 = gson.fromJson(itemsString1, type1);
//                items1.get(position).setUnit(items1.get(position).getUnit() + 1);
//                mItems.clear();
//                mItems.addAll(items1);
                mItems.get(position).setUnit(mItems.get(position).getUnit() + 1);
                SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(mItems), mShopId1);

                holder.num_view.setText(String.valueOf(mItems.get(position).getUnit()));

                setComponentsStatus(holder.plus, holder.minus, holder.num_view,
                        position);

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

                mHandler.sendMessage(message);


//                mPlates[position].setNumber(mPlates[position].getNumber() - 1);
//
//                int shopId = mPlates[position].getShopId();
//                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                shops[shopId].setPlates(mPlates);
//                SharedPreferenceUtils
//                        .saveCurrentChoice(mContext, gson.toJson(shops));

                int mShopId2 = mItems.get(position).getShopId();
//                String itemsString2 = SharedPreferenceUtils.getLocalItems(mContext, mShopId2);
//                Type type2 = new TypeToken<ArrayList<item_iphone>>() {
//                }.getType();
//                ArrayList<item_iphone> items2 = gson.fromJson(itemsString2, type2);
//                items2.get(position).setUnit(items2.get(position).getUnit() - 1);
//                mItems.clear();
//                mItems.addAll(items2);
                mItems.get(position).setUnit(mItems.get(position).getUnit() - 1);
                SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(mItems), mShopId2);


                holder.num_view.setText(String.valueOf(mItems.get(position).getUnit()));

                setComponentsStatus(holder.plus, holder.minus, holder.num_view,
                        position);

            }
        });

        return rowView;
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
