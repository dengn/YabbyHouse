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

import com.melbournestore.activities.R;
import com.melbournestore.models.item_iphone;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Handler mHandler;
    Context mContext;
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

                mHandler.sendMessage(message);
                // orderNumbers[position]++;

//                mPlates[position].setNumber(mPlates[position].getNumber() + 1);
//
//                int shopId = mPlates[position].getShopId();
//                int plateId = mPlates[position].getPlateId();
//                String shop_string = SharedPreferenceUtils
//                        .getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                Plate[] plates = shops[shopId].getPlates();
//                plates[plateId] = mPlates[position];
//
//                SharedPreferenceUtils.saveCurrentChoice(mContext,
//                        gson.toJson(shops));

                holder.numbers_view.setText(String
                        .valueOf(mItems.get(position).getUnit()));


                setComponentsStatus(holder.plus, holder.minus, position);
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

                mHandler.sendMessage(message);

//				if (orderNumbers[position] <= 0) {
//					holder.minus.setEnabled(false);
//					orderNumbers[position] = 0;
//				} else {
//					holder.minus.setEnabled(true);
//					orderNumbers[position]--;
//				}

//                mPlates[position].setNumber(mPlates[position].getNumber() - 1);
//
//                int shopId = mPlates[position].getShopId();
//                int plateId = mPlates[position].getPlateId();
//                String shop_string = SharedPreferenceUtils
//                        .getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                Plate[] plates = shops[shopId].getPlates();
//                plates[plateId] = mPlates[position];
//
//
//                SharedPreferenceUtils.saveCurrentChoice(mContext,
//                        gson.toJson(shops));

                holder.numbers_view.setText(String
                        .valueOf(mItems.get(position).getUnit()));

                setComponentsStatus(holder.plus, holder.minus, position);

            }
        });

        return rowView;
    }

    private void setComponentsStatus(Button plusButton, Button minusButton, int position) {
        int stock_num = mItems.get(position).getStock();
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
