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
import android.widget.ListView;

import com.melbournestore.activities.R;
import com.melbournestore.adaptors.RecommandationListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.models.advertisements;
import com.melbournestore.network.RecommandationManagerThread;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class RecommandationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DisplayImageOptions options;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /**
     * The fragment's ListView/GridView.
     */
    private ListView mRecommandationList;

    ProgressDialog progress;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private RecommandationListAdapter mRecommadationListAdapter;

    private ArrayList<advertisements> mAd = new ArrayList<advertisements>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mAd = (ArrayList<advertisements>) msg.obj;
            mRecommadationListAdapter.refresh(mAd);
            progress.dismiss();
            //mRecommandationList.setAdapter(mRecommadationListAdapter);

//            switch (msg.what) {
//                case 1:
//                    break;
//                case 2:
//                    break;
//
//            }
        }
    };
    private Context mContext;
    ;
    private RecommandationManagerThread mRecommandationThread;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecommandationFragment() {
    }

    // TODO: Rename and change types of parameters
    public static RecommandationFragment newInstance(String param1, String param2) {
        RecommandationFragment fragment = new RecommandationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SysApplication.initImageLoader(mContext);

        mRecommandationThread = new RecommandationManagerThread(mHandler);
        mRecommandationThread.start();
        progress = new ProgressDialog(mContext ,R.style.dialog_loading);
        progress.show();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Change Adapter to display your content


        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();

        mRecommadationListAdapter = new RecommandationListAdapter(mContext, options, mAd);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommandation, container, false);

        // Set the adapter
        mRecommandationList = (ListView) view.findViewById(R.id.recommandation_list);
        mRecommandationList.setAdapter(mRecommadationListAdapter);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }


}
