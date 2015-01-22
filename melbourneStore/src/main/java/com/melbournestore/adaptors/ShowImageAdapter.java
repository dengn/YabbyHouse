package com.melbournestore.adaptors;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.melbournestore.activities.R;
import com.melbournestore.models.Show;
import com.melbournestore.utils.Constant;
import com.melbournestore.utils.TouchImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by dengn on 22/01/2015.
 */
public class ShowImageAdapter extends PagerAdapter {

    private static LayoutInflater inflater = null;
    DisplayImageOptions mOptions;
    ProgressDialog mProgress;
    private Context mContext;
    private Handler mHandler;
    private ArrayList<Show> mShows= new ArrayList<Show>();
    private int mPosition;
    private Gson gson = new Gson();

    public ShowImageAdapter(Context context, Handler handler, DisplayImageOptions options, int position, ArrayList<Show> Shows, ProgressDialog progress) {

        mContext = context;
        mHandler = handler;
        mShows.clear();
        mShows.addAll(Shows);
        mPosition = position;
        mProgress = progress;
        mOptions = options;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(ArrayList<Show> Shows, int position) {

        mShows.clear();
        mShows.addAll(Shows);
        mPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @Override
    public int getCount() {
        if(mShows==null){
            return 0;
        }
        else{
            return mShows.size();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        final viewHolder_image holder = new viewHolder_image();
//
//        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView;
//        rowView = inflater.inflate(R.layout.show_image_item, null);
////        View viewLayout = inflater.inflate(R.layout.show_image_item, container,
////                false);
//
//        holder.showItem = (TouchImageView) rowView.findViewById(R.id.showItem);
//        ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mShows.get(position).getShow_image(), holder.showItem, mOptions);
//
//        holder.showItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Close this Activity
//                Message message = new Message();
//                message.what = 0;
//                mHandler.sendMessage(message);
//            }
//        });
//
//
//
//        return rowView;


        TouchImageView imgDisplay;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.show_image_item, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.showItem);

        ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mShows.get(position).getShow_image(), imgDisplay, mOptions);

        // close button click event
        imgDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close this Activity
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }




    class viewHolder_image {

        private TouchImageView showItem;

    }

}
