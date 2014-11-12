package com.melbournestore.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.PlateActivity;
import com.melbournestore.activities.R;
import com.melbournestore.utils.BitmapUtils;

public class CategoryListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context mContext;
    int[] imageId;
    String[] mShopText;
    String[] mShopSubtext;

    public CategoryListAdapter(Context context, int[] prgmImages, String[] shopText, String[] shopSubtext) {
        // TODO Auto-generated constructor stub

        mContext = context;
        imageId = prgmImages;
        mShopText = shopText;
        mShopSubtext = shopSubtext;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageId.length;
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
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.category_list_item, null);
        holder.img = (ImageView) rowView.findViewById(R.id.plates_image);


        Bitmap bm = BitmapUtils.readBitMap(mContext, imageId[position]);
        holder.img.setImageBitmap(bm);


        holder.shopText = (TextView) rowView.findViewById(R.id.shop_name);
        holder.shopText.setText(mShopText[position]);

        holder.shopSubtext = (TextView) rowView.findViewById(R.id.shop_description);
        holder.shopSubtext.setText(mShopSubtext[position]);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, PlateActivity.class);
                intent.putExtra("shopid", position);
                mContext.startActivity(intent);
            }
        });
        return rowView;
    }

    public class Holder {
        ImageView img;

        TextView shopText;
        TextView shopSubtext;
    }

}
