package com.melbournestore.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.melbournestore.activities.R;
import com.melbournestore.adaptors.ShowListAdapter;
import com.melbournestore.models.Show;
import com.melbournestore.network.ShowManagerThread;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

public class ShowFragment extends Fragment {


    ShowListAdapter mShowListAdapter;
    GridView mGridView;
    ProgressDialog progress;
    DisplayImageOptions mOptions;


    private ShowManagerThread mShowThread;

    private Gson gson = new Gson();
    private Context mContext;

    private int mPageNum = 1;

    private ArrayList<Show> mShows = new ArrayList<Show>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mShows = (ArrayList<Show>) msg.obj;
                    mShowListAdapter.refresh(mShows);
//                    progress.dismiss();
                    break;


            }

        }
    };

    public ShowFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    //private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        mShowThread = new ShowManagerThread(mHandler, mContext, mPageNum);
        mShowThread.start();
//        progress = new ProgressDialog(mContext, R.style.dialog_loading);
//        progress.show();

        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.show_gridview);

        mShowListAdapter = new ShowListAdapter(mContext, mHandler, mOptions, mShows);

        mGridView.setAdapter(mShowListAdapter);

        return rootView;
    }


}