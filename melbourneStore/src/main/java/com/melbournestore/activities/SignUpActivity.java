package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.application.SysApplication;
import com.melbournestore.network.UserLoginManagerThread;

public class SignUpActivity extends Activity {

    private TextView signInNotice;
    private EditText signInNumber;
    private EditText signInPassword;

    private Button signInButton;

    private String mNumber;
    private String mPassword;

    private String mUser;

    private int mOrderNum;
    private int mCouponNum;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //login failed, password or phone number is wrong
                case 0:
                    showNotice("登录失败\n手机号码或密码错误");
                    break;
                case 1:
                    //login success, return to the main activity

                    mUser = (String) msg.obj;
                    mOrderNum = msg.arg1;
                    mCouponNum = msg.arg2;
                    Log.d("LOGIN", mUser);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("user", mUser);
                    returnIntent.putExtra("order_num", mOrderNum);
                    returnIntent.putExtra("coupon_num", mCouponNum);
                    setResult(RESULT_OK, returnIntent);
                    finish();
            }
        }
    };
    private Gson gson = new Gson();
    private long mExitTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("用户登录");

        signInNotice = (TextView) findViewById(R.id.signup_notice);
        signInNotice.setText("请使用手机号码登录");

        signInNumber = (EditText) findViewById(R.id.signup_login_number);
        signInNumber.setHint("澳大利亚10位号码");

        signInPassword = (EditText) findViewById(R.id.signup_password);
        signInPassword.setHint("密码");


        signInButton = (Button) findViewById(R.id.signup_button);
        signInButton.getBackground().setAlpha(80);
        signInButton.setText("登录");

        signInButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (signInNumber.getText().toString().equals("")) {
                    showNotice("请输入手机号码");
                } else if ((signInNumber.getText().length() != 10)
                        || !signInNumber.getText().toString().subSequence(0, 2)
                        .equals("04")) {
                    showNotice("手机号码不是澳洲手机\n" +
                            "请输入04开头号码");
                } else if (signInPassword.getText().toString().equals("")) {
                    showNotice("请输入密码");
                } else if (signInPassword.getText().length() < 6) {
                    showNotice("请输入6位以上密码");
                } else {
                    //get the input number and password
                    mNumber = signInNumber.getText().toString();
                    mPassword = signInPassword.getText().toString();
                    Log.d("SIGNUP", "mNumber: " + mNumber + " mPassword: " + mPassword);
                    UserLoginManagerThread mUserLoginThread = new UserLoginManagerThread(mHandler, SignUpActivity.this, mNumber, mPassword);
                    mUserLoginThread.start();

                }

//                else {
//
//                    mPhoneNumber = loginNumber.getText().toString();
//
//                    String users_string = SharedPreferenceUtils
//                            .getLoginUser(SignUpActivity.this);
//                    Gson gson = new Gson();
//                    User[] users = gson.fromJson(users_string, User[].class);
//
//                    if (users.length > 0) {
//                        boolean user_found = false;
//                        for (int i = 0; i < users.length; i++) {
//                            if (users[i].getPhoneNumber().equals(mPhoneNumber)) {
//                                users[i].setActive(true);
//                                user_found = true;
//                                break;
//                            }
//                        }
//                        if (!user_found) {
//                            ArrayList<User> user_array = new ArrayList<User>(
//                                    Arrays.asList(users));
//                            User user = new User();
//                            user.setActive(true);
//                            user.setPhoneNumber(mPhoneNumber);
//                            user.setUnitNo("");
//                            user.setStreet("");
//                            user.setSuburb("");
//                            user_array.add(user);
//                            users = user_array.toArray(new User[0]);
//                        }
//
//                        SharedPreferenceUtils.saveLoginUser(SignUpActivity.this,
//                                gson.toJson(users));
//
//                    } else {
//                        users = new User[1];
//                        User user = new User();
//                        user.setActive(true);
//                        user.setPhoneNumber(mPhoneNumber);
//                        user.setUnitNo("");
//                        user.setStreet("");
//                        user.setSuburb("");
//
//                        users[0] = user;
//                        SharedPreferenceUtils.saveLoginUser(SignUpActivity.this,
//                                gson.toJson(users));
//                    }
//
//                    UserLoginManagerThread mUserLoginThread = new UserLoginManagerThread(mHandler, SignUpActivity.this, loginNumber.getText()
//                            .toString());
//                    mUserLoginThread.start();
//
//                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("number", loginNumber.getText()
//                            .toString());
//                    setResult(RESULT_OK, returnIntent);
//                    finish();
//
//                }
            }

        });

    }

    private void showNotice(String text) {
        new AlertDialog.Builder(SignUpActivity.this)
                .setMessage(text)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface,
                                    int i) {

                            }
                        }
                ).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.signup).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
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
                // Intent upIntent = NavUtils.getParentActivityIntent(this);
                // upIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // startActivity(upIntent);
                finish();
                return true;
            case R.id.signup:

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
