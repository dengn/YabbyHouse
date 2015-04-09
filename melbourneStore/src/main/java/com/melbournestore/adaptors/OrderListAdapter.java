package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.item_iphone;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Handler mHandler;
    Context mContext;
    Gson gson = new Gson();
    ArrayList<item_iphone> mItems = new ArrayList<item_iphone>();

    public OrderListAdapter(Context context, Handler handler, ArrayList<item_iphone> items) {
        mContext = context;
        mHandler = handler;

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
        rowView = inflater.inflate(R.layout.shopping_list_item, null);
        holder.names_view = (TextView) rowView.findViewById(R.id.order_name);
        holder.prices_view = (TextView) rowView.findViewById(R.id.order_price);
        holder.numbers_view = (TextView) rowView
                .findViewById(R.id.order_number);

        holder.plus = (Button) rowView.findViewById(R.id.order_plus);
        holder.minus = (Button) rowView.findViewById(R.id.order_minus);

        holder.names_view.setText(mItems.get(position).getName());
        holder.prices_view.setText("$"
                + String.valueOf(mItems.get(position).getPrice()));
        holder.numbers_view.setText(String.valueOf(mItems.get(position)
                .getUnit()));

        setComponentsStatus(holder.plus, holder.minus, position);

        holder.plus.setOnClickListener(new OnClickListener() {
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
                    String shopItemsString1 = SharedPreferenceUtils.getLocalItems(mContext, mShopId1);
                    Type type1 = new TypeToken<ArrayList<item_iphone>>() {
                    }.getType();
                    ArrayList<item_iphone> shopItems1 = gson.fromJson(shopItemsString1, type1);
                    for(int i=0;i<shopItems1.size();i++){
                        if(shopItems1.get(i).getId()==mItems.get(position).getId()){
                            shopItems1.get(i).setUnit(mItems.get(position).getUnit() + 1);
                            break;
                        }
                    }
                    SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(shopItems1), mShopId1);

                    mItems.get(position).setUnit(mItems.get(position).getUnit() + 1);

                }

                holder.numbers_view.setText(String
                        .valueOf(mItems.get(position).getUnit()));


                setComponentsStatus(holder.plus, holder.minus, position);

                mHandler.sendMessage(message);
            }
        });

        holder.minus.setOnClickListener(new OnClickListener() {
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
                    String shopItemsString2 = SharedPreferenceUtils.getLocalItems(mContext, mShopId2);
                    Type type2 = new TypeToken<ArrayList<item_iphone>>() {
                    }.getType();
                    ArrayList<item_iphone> shopItems2 = gson.fromJson(shopItemsString2, type2);
                    for(int i=0;i<shopItems2.size();i++){
                        if(shopItems2.get(i).getId()==mItems.get(position).getId()){
                            shopItems2.get(i).setUnit(mItems.get(position).getUnit() - 1);
                            break;
                        }
                    }
                    SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(shopItems2), mShopId2);

                    mItems.get(position).setUnit(mItems.get(position).getUnit() - 1);
                }

                holder.numbers_view.setText(String
                        .valueOf(mItems.get(position).getUnit()));

                setComponentsStatus(holder.plus, holder.minus, position);

                mHandler.sendMessage(message);

            }
        });

        return rowView;
    }

    private void setComponentsStatus(Button plusButton, Button minusButton, int position) {
        long stock_num = mItems.get(position).getStock();
        int plate_num = mItems.get(position).getUnit();

        if (plate_num >= stock_num) {
            plusButton.setEnabled(false);
        } else {
            plusButton.setEnabled(true);
        }
        if (plate_num <= 0) {
            minusButton.setEnabled(false);
        } else {
            minusButton.setEnabled(true);
        }
    }

    public class Holder {
        TextView names_view;
        TextView prices_view;

        TextView numbers_view;

        Button plus;
        Button minus;
    }

}
