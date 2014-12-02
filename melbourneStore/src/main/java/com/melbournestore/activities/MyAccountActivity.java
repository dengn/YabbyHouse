package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.MyAccountListAdapter;
import com.melbournestore.adaptors.MyAccountListAddressAdapter;
import com.melbournestore.adaptors.MyAccountListCouponAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Suburb;
import com.melbournestore.models.user_iphone;
import com.melbournestore.network.CouponManagerThread;
import com.melbournestore.network.OrderManagerThread;
import com.melbournestore.network.UserLoginManagerThread;
import com.melbournestore.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MyAccountActivity extends Activity {

    public static final int choose_address_code = 4;

    public static final int REQUEST_CODE_CAMERA = 5;

    public static final int REQUEST_CODE_IMAGE = 6;

    // public static final int IMAGE_REQUEST_CODE = 6;

    private Button mLogout;

    private ListView mMyAccountList;
    private ListView mMyAccountListAddress;
    private ListView mMyAccountListCoupon;

    private MyAccountListAdapter mMyAccountListAdapter;
    private MyAccountListAddressAdapter mMyAccountListAdapterAddress;
    private MyAccountListCouponAdapter mMyAccountListAdapterCoupon;

    DisplayImageOptions options;

    private user_iphone mUser;

    private int mOrderNum=0;

    private int mCouponNum=0;

    private String mNumber="";
    private String mPassword="";

    private Gson gson = new Gson();

    private PopupWindow mpopupWindow;

    private OrderManagerThread mOrderThread;
    private CouponManagerThread mCouponThread;

    private int callOrderCode =0;
    private int callCouponCode = 0;

    private UserLoginManagerThread mLoginThread;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    // Popup Menu

                    showPopMenu();
                    break;

                case 1:
                    String mUserString =(String) msg.obj;
                    SharedPreferenceUtils.saveLoginUser(MyAccountActivity.this, mUserString);

                    mUser = gson.fromJson(mUserString, user_iphone.class);

                    mOrderNum = msg.arg1;
                    mCouponNum = msg.arg2;
                    mMyAccountListAdapter.refresh(mUser, mOrderNum, mCouponNum);
                    mMyAccountList.setAdapter(mMyAccountListAdapter);
                    mMyAccountListAdapterAddress.refresh(mUser);
                    mMyAccountListAddress.setAdapter(mMyAccountListAdapterAddress);

                    break;

//                case 1:
//                    //get Coupon
//                    mCouponNum = (Integer)msg.obj;
//                    Log.d("ACCOUNT", "mCouponNum: "+String.valueOf(mCouponNum));
//                    mMyAccountListAdapter = new MyAccountListAdapter(MyAccountActivity.this, mHandler, options, mUser, mOrderNum, mCouponNum);
//                    mMyAccountList.setAdapter(mMyAccountListAdapter);
//                    break;
//                case 2:
//
//                    break;
//
//                case 3:
//                    //get Order
//                    mOrderNum = (Integer)msg.obj;
//                    Log.d("ACCOUNT", "mOrderNum: "+String.valueOf(mCouponNum));
//                    mMyAccountListAdapter = new MyAccountListAdapter(MyAccountActivity.this, mHandler, options, mUser, mOrderNum, mCouponNum);
//                    mMyAccountList.setAdapter(mMyAccountListAdapter);
//                    break;
//                case 4:
//
//                    break;

            }
        }
    };
    private long mExitTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);


        getActionBar().setTitle("我的");

        Intent intent = getIntent();

        String users_string = intent.getStringExtra("user");
        mOrderNum = intent.getIntExtra("order_num", 0);
        mCouponNum = intent.getIntExtra("coupon_num", 0);

        mNumber = SharedPreferenceUtils.getUserNumber(this);
        mPassword = SharedPreferenceUtils.getUserPassword(this);


        mLoginThread = new UserLoginManagerThread(mHandler, this, mNumber, mPassword);
        mLoginThread.start();

        mUser = gson.fromJson(users_string, user_iphone.class);





        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_ads)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.loading_ads)  //image连接地址为空时
                .showImageOnFail(R.drawable.loading_ads)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .build();

        mLogout = (Button) findViewById(R.id.logout);
        mLogout.getBackground().setAlpha(80);

        mLogout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();

                //SysApplication.setLoginStatus(false);

                user_iphone user = new user_iphone("", "", "", 0, "", new Suburb(0, "", "", ""));
                SharedPreferenceUtils.saveLoginUser(MyAccountActivity.this, gson.toJson(user));
                SharedPreferenceUtils.saveUserNumber(MyAccountActivity.this, "");
                SharedPreferenceUtils.saveUserPassword(MyAccountActivity.this, "");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("logout", true);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

        });


        mMyAccountList = (ListView) findViewById(R.id.myaccount_list);
        mMyAccountListAddress = (ListView) findViewById(R.id.myaccount_list_address);
        mMyAccountListCoupon = (ListView) findViewById(R.id.myaccount_list_coupon);

        mMyAccountListAdapter = new MyAccountListAdapter(this, mHandler, options, mUser, mOrderNum, mCouponNum);
        mMyAccountList.setAdapter(mMyAccountListAdapter);

        mMyAccountListAdapterAddress = new MyAccountListAddressAdapter(this, mHandler,options, mUser);
        mMyAccountListAddress.setAdapter(mMyAccountListAdapterAddress);

        mMyAccountListAdapterCoupon = new MyAccountListCouponAdapter(this, mHandler);
        mMyAccountListCoupon.setAdapter(mMyAccountListAdapterCoupon);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action
                // bar.
                // Create a simple intent that starts the hierarchical parent
                // activity and
                // use NavUtils in the Support Package to ensure proper handling of
                // Up.

//			Intent returnIntent = new Intent();
//			returnIntent.putExtra("profile", mProfile);
//			setResult(RESULT_OK, returnIntent);
//			finish();

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(upIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        switch (requestCode) {
            case choose_address_code:
                // Get the Address chosen

                // Make sure the request was successful
                if (resultCode == RESULT_OK) {

                    //TODO

                    mMyAccountListAdapterAddress.refresh(mUser);
                    mMyAccountListAddress.setAdapter(mMyAccountListAdapterAddress);
                }
                break;
            case REQUEST_CODE_IMAGE:
                if (resultCode == RESULT_OK) {

                    Uri originalUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(), originalUri);


//                        String users_string = SharedPreferenceUtils.getLoginUser(MyAccountActivity.this);
//                        Gson gson = new Gson();
//                        User[] users = gson.fromJson(users_string, User[].class);
//                        User activeUser1 = users[MelbourneUtils.getActiveUser(users)];
                        //TODO

                        Bitmap scaledBitmap = BitmapUtils.scaleDownBitmap(bitmap, 100, getBaseContext());

                        BitmapUtils.saveMyBitmap(mUser.getPhoneNumber(), scaledBitmap);


                        mMyAccountListAdapter.refresh(mUser, mOrderNum, mCouponNum);
                        mMyAccountList.setAdapter(mMyAccountListAdapter);

//                        SharedPreferenceUtils.saveLoginUser(MyAccountActivity.this, gson.toJson(users));

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

//                    String users_string = SharedPreferenceUtils.getLoginUser(MyAccountActivity.this);
//                    Gson gson = new Gson();
//                    User[] users = gson.fromJson(users_string, User[].class);
//                    User activeUser2 = users[MelbourneUtils.getActiveUser(users)];

                    //TODO

                    Bitmap scaledBitmap = BitmapUtils.scaleDownBitmap(bitmap, 100, getBaseContext());

                    try {
                        BitmapUtils.saveMyBitmap(mUser.getPhoneNumber(), scaledBitmap);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    mMyAccountListAdapter.refresh(mUser, mOrderNum, mCouponNum);
                    mMyAccountList.setAdapter(mMyAccountListAdapter);

//                    SharedPreferenceUtils.saveLoginUser(MyAccountActivity.this, gson.toJson(users));
                }
                break;
        }

    }

    private void showPopMenu() {
        View view = View.inflate(this, R.layout.profile_popup_menu, null);

        RelativeLayout profile_camera = (RelativeLayout) view
                .findViewById(R.id.profile_camera);
        RelativeLayout profile_album = (RelativeLayout) view
                .findViewById(R.id.profile_album);
        Button profile_cancel = (Button) view.findViewById(R.id.profile_cancel);

        profile_camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Intent intentFromCapture = new Intent(
                // MediaStore.ACTION_IMAGE_CAPTURE);
                //
                //
                // File path = Environment
                // .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                // File file = new File(path,
                // IMAGE_FILE_NAME);
                // intentFromCapture.putExtra(
                // MediaStore.EXTRA_OUTPUT,
                // Uri.fromFile(file));
                // startActivityForResult(intentFromCapture,
                // CAMERA_REQUEST_CODE);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }

        });
        profile_album.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Intent intentFromGallery = new Intent();
                // intentFromGallery.setType("image/*");
                // intentFromGallery
                // .setAction(Intent.ACTION_GET_CONTENT);
                // startActivityForResult(intentFromGallery,
                // IMAGE_REQUEST_CODE);
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);

            }

        });
        profile_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mpopupWindow.dismiss();
            }

        });

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mpopupWindow.dismiss();
            }
        });

        view.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.fade_in));
        LinearLayout profile_popup = (LinearLayout) view
                .findViewById(R.id.profile_popup);
        profile_popup.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.push_bottom_in));

        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow(this);
            mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
            mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

            mpopupWindow.setFocusable(true);
            mpopupWindow.setOutsideTouchable(true);
        }

        mpopupWindow.setContentView(view);
        mpopupWindow.showAtLocation(profile_cancel, Gravity.BOTTOM, 0, 0);
        mpopupWindow.update();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                SysApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
