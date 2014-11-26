package com.melbournestore.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.PlateActivity;
import com.melbournestore.activities.R;
import com.melbournestore.models.Shop_iPhone;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CategoryListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context mContext;
    ArrayList<Shop_iPhone> mShops = new ArrayList<Shop_iPhone>();

    DisplayImageOptions mOptions;

    public CategoryListAdapter(Context context, DisplayImageOptions options, ArrayList<Shop_iPhone> Shops) {
        // TODO Auto-generated constructor stub

        mContext = context;
        mOptions = options;
        mShops.clear();
        mShops.addAll(Shops);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(ArrayList<Shop_iPhone> Shops) {
        mShops.clear();
        mShops.addAll(Shops);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mShops.size();
    }

    @Override
    public Object getItem(int position) {
        return mShops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.category_list_item, null);
        holder.img = (ImageView) rowView.findViewById(R.id.plates_image);


//        Bitmap bm = BitmapUtils.readBitMap(mContext, imageId[position]);
//        holder.img.setImageBitmap(bm);
        ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mShops.get(position).getImage(), holder.img, mOptions);

        holder.shopText = (TextView) rowView.findViewById(R.id.shop_name);
        holder.shopText.setText(mShops.get(position).getName());

        holder.shopSubtext = (TextView) rowView.findViewById(R.id.shop_description);
        holder.shopSubtext.setText(mShops.get(position).getName());

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, PlateActivity.class);
                intent.putExtra("shopid", mShops.get(position).getId());
                intent.putExtra("shopName", mShops.get(position).getName());
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
