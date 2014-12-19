package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melbournestore.activities.DishActivity;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Shop_iPhone;
import com.melbournestore.models.item_iphone;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ItemsSearchListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private ArrayList<item_iphone> mItems = new ArrayList<item_iphone>();

    private Gson gson = new Gson();

    public ItemsSearchListAdapter(Context context, Handler handler, ArrayList<item_iphone> items) {

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



        View rowView;
        rowView = inflater.inflate(R.layout.suburb_head_item2, null);

        TextView text1 = (TextView) rowView.findViewById(R.id.suburb_head2);
        String eachText ="";

        String shopsString = SharedPreferenceUtils.getLocalShops(mContext);
        Type type = new TypeToken<ArrayList<Shop_iPhone>>() {
        }.getType();
        ArrayList<Shop_iPhone> shops = gson.fromJson(shopsString, type);

        for(int i=0;i<shops.size();i++){
            if(mItems.get(position).getShopId()==shops.get(i).getId()){
                eachText+=shops.get(i).getName();
                break;
            }
        }
        eachText+="---"+mItems.get(position).getName();


        text1.setText(eachText);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Intent intent = new Intent(mContext, DishActivity.class);
                intent.putExtra("item_id", mItems.get(position).getId());
                intent.putExtra("item_name", mItems.get(position).getName());

                ((Activity) mContext).startActivity(intent);

            }

        });



        return rowView;
    }





}
