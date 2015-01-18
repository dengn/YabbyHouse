package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.R;
import com.melbournestore.models.categories;
import com.melbournestore.utils.CircleImageView;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SelectCategoryListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context mContext;
    ArrayList<categories> mCategories = new ArrayList<categories>();
    int mSelectedCategory = 0;
    DisplayImageOptions mOptions;
    Handler mHandler;

    public SelectCategoryListAdapter(Context context, Handler handler, DisplayImageOptions options, ArrayList<categories> Categories, int SelectedCategory) {

        mContext = context;
        mHandler = handler;
        mOptions = options;
        mSelectedCategory = SelectedCategory;
        mCategories.clear();
        mCategories.addAll(Categories);


        Collections.sort(mCategories, new Comparator<categories>() {
            public int compare(categories category1, categories category2) {
                return category1.getSeq() - category2.getSeq();
            }
        });

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(ArrayList<categories> Categories) {
        mCategories.clear();
        mCategories.addAll(Categories);

        Collections.sort(mCategories, new Comparator<categories>() {
            public int compare(categories category1, categories category2) {
                return category1.getSeq() - category2.getSeq();
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.select_category_list_item, null);
        holder.categoryImg = (CircleImageView) rowView.findViewById(R.id.select_category_image);

        ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mCategories.get(position).getImage(), holder.categoryImg, mOptions);

        holder.categoryName = (TextView) rowView.findViewById(R.id.select_category_title);
        holder.categoryName.setText(mCategories.get(position).getName());


        holder.categoryChecked = (ImageView) rowView.findViewById(R.id.select_icon);
        holder.categoryChecked.setVisibility((position == mSelectedCategory) ? View.VISIBLE : View.INVISIBLE);


        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = mHandler.obtainMessage();
                msg.what = 3;
                msg.arg1 = position;
                msg.arg2 = mCategories.get(position).getId();
                msg.obj = mCategories.get(position).getName();
                mHandler.sendMessage(msg);

            }
        });
        return rowView;
    }

    public class Holder {

        CircleImageView categoryImg;
        ImageView categoryChecked;
        TextView categoryName;


    }

}
