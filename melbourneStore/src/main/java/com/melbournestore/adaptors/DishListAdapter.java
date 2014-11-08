package com.melbournestore.adaptors;

import android.content.Context;
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

import com.google.gson.Gson;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Plate;
import com.melbournestore.models.Shop;

public class DishListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private Plate mPlate;

    public DishListAdapter(Context context, Handler handler, Plate plate) {
        // TODO Auto-generated constructor stub

        mContext = context;
        mHandler = handler;
        mPlate = plate;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(Plate plate) {

        mPlate = plate;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
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

        viewHolder_image holder_image = null;
        final viewHolder_dish holder_dish = new viewHolder_dish();

        switch (position) {
            case 0:
                holder_image = new viewHolder_image();
                convertView = inflater.inflate(R.layout.dish_list_image, parent,
                        false);

                holder_image.dishImage = (ImageView) convertView
                        .findViewById(R.id.dish_img);
                holder_image.dishText = (TextView) convertView
                        .findViewById(R.id.dish_img_text);

                holder_image.dishImage
                        .setImageResource(mPlate.getImageId());
                if (mPlate.getNumber() <= 0) {
                    holder_image.dishText.setVisibility(View.INVISIBLE);
                } else {
                    holder_image.dishText.setVisibility(View.VISIBLE);
                    holder_image.dishText.setText(String.valueOf(mPlate.getNumber()));
                }

                convertView.setTag(holder_image);

                break;
            case 1:
                //holder_dish = new viewHolder_dish();
                convertView = inflater.inflate(R.layout.dish_list_item, parent,
                        false);

                holder_dish.price = (TextView) convertView
                        .findViewById(R.id.dish_price);
                holder_dish.like = (ImageView) convertView
                        .findViewById(R.id.dish_like);
                holder_dish.like_num = (TextView) convertView
                        .findViewById(R.id.dish_like_number);
                holder_dish.stock = (TextView) convertView
                        .findViewById(R.id.dish_stock);
                holder_dish.plus = (Button) convertView
                        .findViewById(R.id.dish_plus);
                holder_dish.minus = (Button) convertView
                        .findViewById(R.id.dish_minus);

                holder_dish.price.setText("$" + String.valueOf(mPlate.getPrice()));
                holder_dish.like.setImageResource(R.drawable.other_icon_like);

                holder_dish.like_num.setText(String.valueOf(mPlate.getLikeNum()));
                holder_dish.stock.setText("今日库存" + String.valueOf(mPlate.getStockMax()) + "份");


                setComponentsStatus(holder_dish.plus, holder_dish.minus);

                holder_dish.plus.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Message message = new Message();
                        // plus = 1
                        message.what = 1;

                        mHandler.sendMessage(message);


                        mPlate.setNumber(mPlate.getNumber() + 1);

                        int shopId = mPlate.getShopId();
                        int plateId = mPlate.getPlateId();
                        String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
                        Gson gson = new Gson();
                        Shop[] shops = gson.fromJson(shop_string, Shop[].class);
                        Plate[] plates = shops[shopId].getPlates();
                        plates[plateId] = mPlate;
                        shops[shopId].setPlates(plates);
                        SharedPreferenceUtils
                                .saveCurrentChoice(mContext, gson.toJson(shops));

                        setComponentsStatus(holder_dish.plus, holder_dish.minus);

                    }
                });

                holder_dish.minus.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Message message = new Message();
                        // minus = 2
                        message.what = 2;

                        mHandler.sendMessage(message);


                        mPlate.setNumber(mPlate.getNumber() - 1);

                        int shopId = mPlate.getShopId();
                        int plateId = mPlate.getPlateId();
                        String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
                        Gson gson = new Gson();
                        Shop[] shops = gson.fromJson(shop_string, Shop[].class);
                        Plate[] plates = shops[shopId].getPlates();
                        plates[plateId] = mPlate;
                        shops[shopId].setPlates(plates);
                        SharedPreferenceUtils
                                .saveCurrentChoice(mContext, gson.toJson(shops));
                        setComponentsStatus(holder_dish.plus, holder_dish.minus);
                    }
                });


                convertView.setTag(holder_dish);

                break;

        }

        return convertView;
    }

    private void setComponentsStatus(Button plusButton, Button minusButton) {
        int stock_num = mPlate.getStockMax();
        int plate_num = mPlate.getNumber();

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

    class viewHolder_image {

        private ImageView dishImage;

        private TextView dishText;
    }

    class viewHolder_dish {

        private TextView price;

        private ImageView like;

        private TextView like_num;

        private TextView stock;

        private Button plus;

        private Button minus;

    }

}
