package com.melbournestore.adaptors;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.melbournestore.activities.R;

public class SubmitListMemoAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    Handler mHandler;
    Context mContext;
    private String mMemo;

    public SubmitListMemoAdapter(Context context, Handler handler, String memo) {
        // TODO Auto-generated constructor stub


        mContext = context;
        mHandler = handler;
        mMemo = memo;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh() {

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1;
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

        holder_edittext = new viewHolder_edittext();
        convertView = inflater.inflate(R.layout.submit_list_item_memo, parent, false);

        holder_edittext.title = (TextView) convertView.findViewById(R.id.memo_title);
        holder_edittext.memo = (EditText) convertView.findViewById(R.id.memo_info);

        holder_edittext.title.setText("备        注");
        holder_edittext.memo.setHint("请输入饮食偏好。\n如：少葱，加麻加辣等。");
        holder_edittext.memo.setText(mMemo);
        holder_edittext.memo.setSingleLine(false);
        holder_edittext.memo.setGravity(Gravity.TOP);
        holder_edittext.memo.setHorizontallyScrolling(false);

        holder_edittext.memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Message message = new Message();
                Bundle b = new Bundle();
                // send the position
                b.putString("memo", charSequence.toString());
                message.setData(b);

                // memo = 2
                message.what = 2;

                mHandler.sendMessage(message);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        convertView.setTag(holder_edittext);


        return convertView;

    }


    class viewHolder_edittext {

        private TextView title;

        private EditText memo;

    }

}
