package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.R;
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


        holder.num_view.setText(String.valueOf(mItems.get(position).getUnit()));


//        rowView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                int shopId = mPlates[position].getShopId();
//                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                shops[shopId].setPlates(mPlates);
//                SharedPreferenceUtils
//                        .saveCurrentChoice(mContext, gson.toJson(shops));
//
//
//                Intent intent = new Intent(mContext, DishActivity.class);
//                intent.putExtra("plateId", position);
//                intent.putExtra("shopId", shopId);
//
//                ((Activity) mContext).startActivity(intent);
//
//            }
//
//        });
//
//        holder.like_number_view.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (!likeClicked) {
//                    holder.like_view
//                            .setImageResource(R.drawable.other_icon_liked);
//
//                    holder.like_number_view.setText(String
//                            .valueOf(mPlates[position].getLikeNum() + 1)
//                            + "         今日库存"
//                            + String.valueOf(mPlates[position].getStockMax()) + "份");
//
//                    mPlates[position].setLikeNum(mPlates[position].getLikeNum() + 1);
//
//
//                    int shopId = mPlates[position].getShopId();
//                    String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                    Gson gson = new Gson();
//                    Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                    shops[shopId].setPlates(mPlates);
//                    SharedPreferenceUtils
//                            .saveCurrentChoice(mContext, gson.toJson(shops));
//
//                    likeClicked = true;
//                } else {
//                    Toast.makeText(mContext, "亲，今天已经点过赞了。", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//
//        });

//        holder.plus.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Message message = new Message();
//                Bundle b = new Bundle();
//                // send the position
//                b.putInt("position", position);
//                message.setData(b);
//
//                // plus = 1
//                message.what = 1;
//
//                mHandler.sendMessage(message);
//
//
//                mPlates[position].setNumber(mPlates[position].getNumber() + 1);
//
//                int shopId = mPlates[position].getShopId();
//                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                shops[shopId].setPlates(mPlates);
//                SharedPreferenceUtils
//                        .saveCurrentChoice(mContext, gson.toJson(shops));
//
//
//                holder.num_view.setText(String.valueOf(mPlates[position].getNumber()));
//
//                setComponentsStatus(holder.plus, holder.minus, holder.num_view,
//                        position);
//
//            }
//        });
//
//        holder.minus.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Message message = new Message();
//                Bundle b = new Bundle();
//                // send the position
//                b.putInt("position", position);
//                message.setData(b);
//
//                // minus = 2
//                message.what = 2;
//
//                mHandler.sendMessage(message);
//
//
//                mPlates[position].setNumber(mPlates[position].getNumber() - 1);
//
//                int shopId = mPlates[position].getShopId();
//                String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                Gson gson = new Gson();
//                Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                shops[shopId].setPlates(mPlates);
//                SharedPreferenceUtils
//                        .saveCurrentChoice(mContext, gson.toJson(shops));
//
//
//                holder.num_view.setText(String.valueOf(mPlates[position].getNumber()));
//
//                setComponentsStatus(holder.plus, holder.minus, holder.num_view,
//                        position);
//
//            }
//        });

        return rowView;
    }

//    private void setComponentsStatus(Button plusButton, Button minusButton,
//                                     TextView numView, int position) {
//        int stock_num = mPlates[position].getStockMax();
//        int plate_num = mPlates[position].getNumber();
//
//        if (plate_num >= stock_num) {
//            plusButton.setEnabled(false);
//        } else {
//            plusButton.setEnabled(true);
//        }
//        if (plate_num <= 0) {
//            numView.setVisibility(View.INVISIBLE);
//            minusButton.setEnabled(false);
//        } else {
//            numView.setVisibility(View.VISIBLE);
//            minusButton.setEnabled(true);
//        }
//    }

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
