package com.melbournestore.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.melbournestore.activities.ChooseAddressActivity;
import com.melbournestore.activities.R;
import com.melbournestore.activities.SuburbActivity;
import com.melbournestore.utils.MelbourneUtils;

public class ChooseAddressListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private Handler mHandler;
    private Context mContext;
    private String addr_unit;
    private String addr_street;
    private String addr_suburb;

    public ChooseAddressListAdapter(Context context, Handler handler, String unit, String street, String suburb) {
        // TODO Auto-generated constructor stub

        mContext = context;
        mHandler = handler;
        addr_unit = unit;
        addr_street = street;
        addr_suburb = suburb;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(String unit, String street, String suburb) {
        addr_unit = unit;
        addr_street = street;
        addr_suburb = suburb;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 4;
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

        viewHolder_edittext holder_edittext = null;
        viewHolder_activity holder_activity = null;
        viewHolder_textView holder_textview = null;

        switch (position) {
            case 0:
                holder_edittext = new viewHolder_edittext();
                convertView = inflater.inflate(R.layout.choose_address_unit, parent, false);

                holder_edittext.title = (TextView) convertView.findViewById(R.id.unit_title);
                holder_edittext.text = (EditText) convertView.findViewById(R.id.unit_info);

                holder_edittext.title.setText("街号");

                holder_edittext.text.setText(addr_unit);

                holder_edittext.text.setHint("275");

                holder_edittext.text.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int position = v.getId();
                            final EditText Caption = (EditText) v;
                            addr_unit = Caption.getText().toString();

                            Message message = new Message();
                            Bundle b = new Bundle();
                            // send the position
                            b.putString("unit", addr_unit);
                            message.setData(b);

                            // unit = 1
                            message.what = 1;

                            mHandler.sendMessage(message);
                        }
                    }
                });

                convertView.setTag(holder_edittext);
                break;
            case 1:
                holder_edittext = new viewHolder_edittext();
                convertView = inflater.inflate(R.layout.choose_address_street, parent, false);

                holder_edittext.title = (TextView) convertView.findViewById(R.id.street_title);
                holder_edittext.text = (EditText) convertView.findViewById(R.id.street_info);

                holder_edittext.title.setText("街名");

                holder_edittext.text.setHint("King Street");

                holder_edittext.text.setText(addr_street);

                holder_edittext.text.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus) {
                            final int position = v.getId();
                            final EditText Caption = (EditText) v;
                            addr_street = Caption.getText().toString();

                            Message message = new Message();
                            Bundle b = new Bundle();
                            // send the position
                            b.putString("street", addr_street);
                            message.setData(b);

                            // street = 2
                            message.what = 2;

                            mHandler.sendMessage(message);
                        }
                    }
                });

                convertView.setTag(holder_edittext);

                break;
            case 2:
                holder_activity = new viewHolder_activity();
                convertView = inflater.inflate(R.layout.choose_address_suburb, parent, false);

                holder_activity.title = (TextView) convertView.findViewById(R.id.suburb_title);
                holder_activity.info = (TextView) convertView.findViewById(R.id.suburb_info);
                holder_activity.rightArrow = (ImageView) convertView.findViewById(R.id.address_rightarrow);


                holder_activity.title.setText("区域");
                holder_activity.info.setText(addr_suburb);
                holder_activity.info.setHint("City");
                holder_activity.rightArrow.setImageResource(R.drawable.other_icon_rightarrow);

                convertView.setTag(holder_activity);

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mContext, SuburbActivity.class);
                        ((Activity) mContext).startActivityForResult(intent, ChooseAddressActivity.result_code_suburb);
                    }

                });
                break;
            case 3:
                holder_textview = new viewHolder_textView();
                convertView = inflater.inflate(R.layout.choose_address_postcode, parent, false);

                holder_textview.title = (TextView) convertView.findViewById(R.id.postcode_title);
                holder_textview.info = (TextView) convertView.findViewById(R.id.postcode_info);

                holder_textview.title.setText("邮编");
                holder_textview.info.setText(MelbourneUtils.getPostcode(addr_suburb));

                convertView.setTag(holder_textview);

                break;


        }

        return convertView;
    }

    class viewHolder_edittext {

        private TextView title;

        private EditText text;

    }

    class viewHolder_activity {

        private TextView title;

        private TextView info;

        private ImageView rightArrow;
    }

    class viewHolder_textView {

        private TextView title;

        private TextView info;
    }

}
