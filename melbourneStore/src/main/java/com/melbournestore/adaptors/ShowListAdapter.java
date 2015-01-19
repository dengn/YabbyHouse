package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.melbournestore.activities.R;
import com.melbournestore.models.Show;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ShowListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context mContext;
    private Handler mHandler;
    private ArrayList<Show> mShows = new ArrayList<Show>();
    private DisplayImageOptions mOptions;
    private Gson gson = new Gson();


    public ShowListAdapter(Context context, Handler handler, DisplayImageOptions options, ArrayList<Show> Shows) {

        mContext = context;
        mHandler = handler;
        mOptions = options;
        mShows.clear();
        mShows.addAll(Shows);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(ArrayList<Show> Shows) {
        mShows.clear();
        mShows.addAll(Shows);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mShows == null) {
            return 0;
        } else {
            return mShows.size();
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.show_grid_item, null);

        holder.showImage = (ImageView) rowView.findViewById(R.id.show_img);
        ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mShows.get(position).getShow_image(), holder.showImage, mOptions);


        return rowView;
    }

    public class Holder {
        ImageView showImage;
    }

}
