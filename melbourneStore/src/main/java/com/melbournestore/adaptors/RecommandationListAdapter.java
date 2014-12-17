package com.melbournestore.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.R;
import com.melbournestore.models.advertisements;
import com.melbournestore.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.util.ArrayList;

public class RecommandationListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    Context mContext;
    DisplayImageOptions mOptions;
    ArrayList<advertisements> mAd = new ArrayList<advertisements>();

    public RecommandationListAdapter(Context context, DisplayImageOptions options, ArrayList<advertisements> Ad) {

        mContext = context;
        mOptions = options;
        mAd.clear();
        mAd.addAll(Ad);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(ArrayList<advertisements> Ad) {
        mAd.clear();
        mAd.addAll(Ad);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAd.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return mAd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (position == 0) {
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.recommandation_list_item, null);
            holder.ad_img = (ImageView) rowView.findViewById(R.id.ad_image);
            holder.ad_img.setImageResource(R.drawable.aboutus);
            holder.ad_name = (TextView) rowView.findViewById(R.id.ad_name);
            holder.ad_name.setVisibility(View.INVISIBLE);
            holder.ad_address = (TextView) rowView.findViewById(R.id.ad_address);
            holder.ad_address.setVisibility(View.INVISIBLE);
            holder.ad_icon = (ImageView) rowView.findViewById(R.id.ad_icon_location);
            holder.ad_icon.setVisibility(View.INVISIBLE);
            holder.ad_desc = (Button) rowView.findViewById(R.id.ad_desc);
            holder.ad_desc.setText("加盟热线");
            return rowView;
        } else {
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.recommandation_list_item, null);
            holder.ad_img = (ImageView) rowView.findViewById(R.id.ad_image);


            ImageLoader.getInstance().displayImage(Constant.URL_BASE_PHOTO + mAd.get(position - 1).getPic(), holder.ad_img, mOptions);


            holder.ad_name = (TextView) rowView.findViewById(R.id.ad_name);
            holder.ad_name.setText(mAd.get(position).getName());

            holder.ad_address = (TextView) rowView.findViewById(R.id.ad_address);
            holder.ad_address.setText(mAd.get(position).getAddress());

            holder.ad_desc = (Button) rowView.findViewById(R.id.ad_desc);
            holder.ad_desc.setText(mAd.get(position).getDesc());


            return rowView;
        }
    }

    public class Holder {
        ImageView ad_img;
        ImageView ad_icon;
        TextView ad_name;
        TextView ad_address;

        Button ad_desc;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
