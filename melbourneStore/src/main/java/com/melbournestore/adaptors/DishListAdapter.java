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
import com.google.gson.reflect.TypeToken;
import com.melbournestore.activities.R;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.item_iphone;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DishListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    DisplayImageOptions mOptions;
    private Context mContext;
    private Handler mHandler;
    private item_iphone mItem;

    private Gson gson = new Gson();

    public DishListAdapter(Context context, Handler handler, DisplayImageOptions options, item_iphone item) {

        mContext = context;
        mHandler = handler;
        mItem = item;
        mOptions = options;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(item_iphone item) {

        mItem = item;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder_image holder_image = null;
        final viewHolder_dish holder_dish = new viewHolder_dish();
        viewHolder_text holder_text = null;
        switch (position) {
            case 0:
                holder_image = new viewHolder_image();
                convertView = inflater.inflate(R.layout.dish_list_image, parent,
                        false);

                holder_image.dishImage = (ImageView) convertView
                        .findViewById(R.id.dish_img);
                holder_image.dishText = (TextView) convertView
                        .findViewById(R.id.dish_img_text);


                ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mItem.getImage(), holder_image.dishImage, mOptions);

                if (mItem.getUnit() <= 0) {
                    holder_image.dishText.setVisibility(View.INVISIBLE);
                } else {
                    holder_image.dishText.setVisibility(View.VISIBLE);
                    holder_image.dishText.setText(String.valueOf(mItem.getUnit()));
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

                holder_dish.price.setText("$" + String.valueOf(mItem.getPrice()));
                holder_dish.like.setImageResource(R.drawable.other_icon_like);

                holder_dish.like_num.setText(String.valueOf(mItem.getGood()));
                holder_dish.stock.setText("今日库存" + String.valueOf(mItem.getStock()) + "份");


                setComponentsStatus(holder_dish.plus, holder_dish.minus);

                holder_dish.plus.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message message = new Message();
                        // plus = 1
                        message.what = 1;


//
//
//                        mPlate.setNumber(mPlate.getNumber() + 1);
//
//                        int shopId = mPlate.getShopId();
//                        int plateId = mPlate.getPlateId();
//                        String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                        Gson gson = new Gson();
//                        Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                        Plate[] plates = shops[shopId].getPlates();
//                        plates[plateId] = mPlate;
//                        shops[shopId].setPlates(plates);
//                        SharedPreferenceUtils
//                                .saveCurrentChoice(mContext, gson.toJson(shops));
//
//                        setComponentsStatus(holder_dish.plus, holder_dish.minus);

                        if (mItem.getUnit() < mItem.getStock()) {
                            int mShopId1 = mItem.getShopId();
                            String shopItemsString1 = SharedPreferenceUtils.getLocalItems(mContext, mShopId1);
                            Type type1 = new TypeToken<ArrayList<item_iphone>>() {
                            }.getType();
                            ArrayList<item_iphone> shopItems1 = gson.fromJson(shopItemsString1, type1);
                            for(int i=0;i<shopItems1.size();i++){
                                if(shopItems1.get(i).getId()==mItem.getId()){
                                    shopItems1.get(i).setUnit(mItem.getUnit() + 1);
                                    break;
                                }
                            }
                            SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(shopItems1), mShopId1);

                            mItem.setUnit(mItem.getUnit() + 1);

                        }


                        mHandler.sendMessage(message);

                    }
                });

                holder_dish.minus.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Message message = new Message();
                        // minus = 2
                        message.what = 2;


//
//
//                        mPlate.setNumber(mPlate.getNumber() - 1);
//
//                        int shopId = mPlate.getShopId();
//                        int plateId = mPlate.getPlateId();
//                        String shop_string = SharedPreferenceUtils.getCurrentChoice(mContext);
//                        Gson gson = new Gson();
//                        Shop[] shops = gson.fromJson(shop_string, Shop[].class);
//                        Plate[] plates = shops[shopId].getPlates();
//                        plates[plateId] = mPlate;
//                        shops[shopId].setPlates(plates);
//                        SharedPreferenceUtils
//                                .saveCurrentChoice(mContext, gson.toJson(shops));
//                        setComponentsStatus(holder_dish.plus, holder_dish.minus);
                        if (mItem.getUnit() > 0) {
                            int mShopId2 = mItem.getShopId();
                            String shopItemsString2 = SharedPreferenceUtils.getLocalItems(mContext, mShopId2);
                            Type type2 = new TypeToken<ArrayList<item_iphone>>() {
                            }.getType();
                            ArrayList<item_iphone> shopItems2 = gson.fromJson(shopItemsString2, type2);
                            for(int i=0;i<shopItems2.size();i++){
                                if(shopItems2.get(i).getId()==mItem.getId()){
                                    shopItems2.get(i).setUnit(mItem.getUnit() - 1);
                                    break;
                                }
                            }
                            SharedPreferenceUtils.saveLocalItems(mContext, gson.toJson(shopItems2), mShopId2);

                            mItem.setUnit(mItem.getUnit() - 1);

                        }

                        mHandler.sendMessage(message);
                    }
                });


                convertView.setTag(holder_dish);

                break;
            case 2:
                holder_text = new viewHolder_text();
                convertView = inflater.inflate(R.layout.dish_list_item_text, parent,
                        false);
                holder_text.dishDesc = (TextView) convertView
                        .findViewById(R.id.dish_desc);

                if(mItem.getDesc()==null||mItem.getDesc().equals("")){
                    convertView.setVisibility(View.INVISIBLE);
                }
                else {
                    convertView.setVisibility(View.VISIBLE);
                    holder_text.dishDesc.setText(mItem.getDesc());
                }


                convertView.setTag(holder_text);

//                if(mItem.getDesc().equals("")){
//                    convertView.setVisibility(View.INVISIBLE);
//                }
//                else{
//                    convertView.setVisibility(View.VISIBLE);
//                }
                break;

        }

        return convertView;
    }

    private void setComponentsStatus(Button plusButton, Button minusButton) {
        int stock_num = mItem.getStock();
        int plate_num = mItem.getUnit();

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

    class viewHolder_text {
        private TextView dishDesc;
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
