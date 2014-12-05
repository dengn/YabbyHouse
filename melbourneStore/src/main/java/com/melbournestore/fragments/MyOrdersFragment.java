package com.melbournestore.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.melbournestore.activities.ChatActivity;
import com.melbournestore.activities.R;
import com.melbournestore.adaptors.MyOrderListAdapter;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Order;
import com.melbournestore.models.user_iphone;
import com.melbournestore.network.DeleteOrderManagerThread;
import com.melbournestore.network.OrderManagerThread;
import com.melbournestore.utils.SwipeListView;

import java.util.ArrayList;

public class MyOrdersFragment extends Fragment {


    MyOrderListAdapter myOrderListAdapter;
    ProgressDialog progress;
    private Order[] mOrders;
    private OrderManagerThread mOrderThread;
    private DeleteOrderManagerThread mDeleteOrderThread;
    private String mContactNumber;
    private Gson gson = new Gson();
    private Context mContext;
    private SwipeListView myOrdersList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mOrders = (Order[]) msg.obj;
                    myOrderListAdapter.refresh(mOrders);
                    progress.dismiss();
                    break;

                case 1:
                    //delete order
                    int deleteId = (Integer) msg.obj;
                    ArrayList<Order> updatedOrders = new ArrayList<Order>();
                    for (int i = 0; i < mOrders.length; i++) {
                        if (mOrders[i].getId() != deleteId) {
                            updatedOrders.add(mOrders[i]);
                        }
                    }
                    mOrders = updatedOrders.toArray(new Order[updatedOrders.size()]);
                    myOrderListAdapter.refresh(mOrders);

                    mDeleteOrderThread = new DeleteOrderManagerThread(mHandler, mContext, deleteId);
                    mDeleteOrderThread.start();
                    break;

            }

        }
    };

    public MyOrdersFragment() {

    }

    //private Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String userString = SharedPreferenceUtils.getLoginUser(mContext);
        user_iphone user = gson.fromJson(userString, user_iphone.class);
        mContactNumber = user.getPhoneNumber();

        mOrderThread = new OrderManagerThread(mHandler, mContext, mContactNumber);
        mOrderThread.start();
        progress = new ProgressDialog(mContext ,R.style.dialog_loading);
        progress.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.myorders_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle myorder_list_item selection
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

        myOrderListAdapter = new MyOrderListAdapter(mContext, myOrdersList.getRightViewWidth(), mHandler, mOrders);

        myOrdersList.setAdapter(myOrderListAdapter);

        return rootView;
    }



}
