package com.melbournestore.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.google.gson.Gson;
import com.melbournestore.activities.ChatActivity;
import com.melbournestore.activities.CurrentOrderActivity;
import com.melbournestore.activities.R;
import com.melbournestore.adaptors.MyOrderListAdapter;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.User;
import com.melbournestore.utils.MelbourneUtils;

public class MyOrdersFragment extends Fragment {


    MyOrderListAdapter myOrderListAdapter;
    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //delete clicked
                Bundle b = msg.getData();
                int position = b.getInt("position");

                String users_string = SharedPreferenceUtils
                        .getLoginUser(mContext);
                Gson gson = new Gson();
                User[] users = gson.fromJson(users_string, User[].class);
                User activeUser = users[MelbourneUtils.getActiveUser(users)];

                activeUser = MelbourneUtils.deleteOrder(activeUser, position);

                SharedPreferenceUtils.saveLoginUser(mContext, gson.toJson(users));

                myOrderListAdapter.refresh(activeUser);
                myOrdersList.setAdapter(myOrderListAdapter);


            }
        }
    };
    private SwipeListView myOrdersList;

    //private Context mContext;

    public MyOrdersFragment(Context context) {

        mContext = context;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.myorders_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.chat:

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myorders, container, false);
        myOrdersList = (SwipeListView) rootView.findViewById(R.id.my_orders);

        String users_string = SharedPreferenceUtils
                .getLoginUser(mContext);
        Gson gson = new Gson();
        User[] users = gson.fromJson(users_string, User[].class);
        User activeUser = users[MelbourneUtils.getActiveUser(users)];

        myOrderListAdapter = new MyOrderListAdapter(mContext, mHandler, activeUser);


        myOrdersList.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));

            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));

            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));


                //myOrdersList.openAnimate(position); //when you touch front view it will open
                Intent intent = new Intent(mContext, CurrentOrderActivity.class);
                intent.putExtra("position", position);
                ((Activity) mContext).startActivity(intent);

            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));

                //myOrdersList.closeAnimate(position);//when you touch back view it will close
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

            }

        });


        myOrdersList.setAdapter(myOrderListAdapter);

        return rootView;
    }
}
