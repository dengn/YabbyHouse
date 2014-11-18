package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;
import com.melbournestore.utils.BitmapUtils;

public class PlateListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context mContext;
    Handler mHandler;
    Plate[] mPlates;
    private boolean likeClicked = false;

    public PlateListAdapter(Context context, Handler handler, Plate[] plates) {
        // TODO Auto-generated constructor stub

        mContext = context;
        mHandler = handler;

        mPlates = plates;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(Plate[] plates) {
        mPlates = plates;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mPlates.length;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


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

        holder.like_number_view.setText(String.valueOf(mPlates[position].getLikeNum())
                + "         今日库存" + mPlates[position].getStockMax() + "份");

        holder.plus = (Button) rowView.findViewById(R.id.plate_plus);
        holder.minus = (Button) rowView.findViewById(R.id.plate_minus);

        setComponentsStatus(holder.plus, holder.minus, holder.num_view,
                position);

        holder.names_view.setText(mPlates[position].getName());
        holder.prices_view
                .setText("$" + String.valueOf(mPlates[position].getPrice()));


        Bitmap bm = BitmapUtils.readBitMap(mContext, mPlates[position].getImageId());
        holder.imgs_view.setImageBitmap(bm);


        holder.num_view.setText(String.valueOf(mPlates[position].getNumber()));


        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int shopId = mPlates[position].getShopId();
                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
                Gson gson = new Gson();
                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
                shops[shopId].setPlates(mPlates);
                SharedPreferenceUtils
                        .saveCurrentChoice(mContext, gson.toJson(shops));


                Intent intent = new Intent(mContext, DishActivity.class);
                intent.putExtra("plateId", position);
                intent.putExtra("shopId", shopId);

                ((Activity) mContext).startActivity(intent);

            }

        });

        holder.like_number_view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!likeClicked) {
                    holder.like_view
                            .setImageResource(R.drawable.other_icon_liked);

                    holder.like_number_view.setText(String
                            .valueOf(mPlates[position].getLikeNum() + 1)
                            + "         今日库存"
                            + String.valueOf(mPlates[position].getStockMax()) + "份");

                    mPlates[position].setLikeNum(mPlates[position].getLikeNum() + 1);


                    int shopId = mPlates[position].getShopId();
                    String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
                    Gson gson = new Gson();
                    Shop[] shops = gson.fromJson(shop_string, Shop[].class);
                    shops[shopId].setPlates(mPlates);
                    SharedPreferenceUtils
                            .saveCurrentChoice(mContext, gson.toJson(shops));

                    likeClicked = true;
                } else {
                    Toast.makeText(mContext, "亲，今天已经点过赞了。", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        });

        holder.plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message message = new Message();
                Bundle b = new Bundle();
                // send the position
                b.putInt("position", position);
                message.setData(b);

                // plus = 1
                message.what = 1;

                mHandler.sendMessage(message);


                mPlates[position].setNumber(mPlates[position].getNumber() + 1);

                int shopId = mPlates[position].getShopId();
                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
                Gson gson = new Gson();
                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
                shops[shopId].setPlates(mPlates);
                SharedPreferenceUtils
                        .saveCurrentChoice(mContext, gson.toJson(shops));


                holder.num_view.setText(String.valueOf(mPlates[position].getNumber()));

                setComponentsStatus(holder.plus, holder.minus, holder.num_view,
                        position);

            }
        });

        holder.minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message message = new Message();
                Bundle b = new Bundle();
                // send the position
                b.putInt("position", position);
                message.setData(b);

                // minus = 2
                message.what = 2;

                mHandler.sendMessage(message);


                mPlates[position].setNumber(mPlates[position].getNumber() - 1);

                int shopId = mPlates[position].getShopId();
                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
                Gson gson = new Gson();
                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
                shops[shopId].setPlates(mPlates);
                SharedPreferenceUtils
                        .saveCurrentChoice(mContext, gson.toJson(shops));


                holder.num_view.setText(String.valueOf(mPlates[position].getNumber()));

                setComponentsStatus(holder.plus, holder.minus, holder.num_view,
                        position);

            }
        });

        return rowView;
    }

    private void setComponentsStatus(Button plusButton, Button minusButton,
                                     TextView numView, int position) {
        int stock_num = mPlates[position].getStockMax();
        int plate_num = mPlates[position].getNumber();

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
