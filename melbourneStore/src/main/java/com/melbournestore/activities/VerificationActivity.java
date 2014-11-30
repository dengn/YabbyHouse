package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.melbournestore.application.SysApplication;
import com.melbournestore.network.UserSignUpManagerThread;
import com.melbournestore.network.UserVerificationManagerThread;

public class VerificationActivity extends Activity {

    private TextView verificationNotice;
    private EditText verificationCode;

    private EditText setPassword;

    private Button reSendButton;

    private Button finishSignUpButton;

    private String mNumber = "";
    private String mVerificationCode = "";
    private String mPassword = "";

    private MyCount mc;

    private long mExitTime;

    private Handler mHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mNumber = intent.getStringExtra("number");

        getActionBar().setTitle("注册");

        verificationNotice = (TextView) findViewById(R.id.verification_notice);
        verificationNotice.setText("验证码已经发送到" + mNumber);

        verificationCode = (EditText) findViewById(R.id.verification_code);
        verificationCode.setHint("请输入验证码");

        setPassword = (EditText) findViewById(R.id.set_password);
        setPassword.setHint("设置账户密码");


        reSendButton = (Button) findViewById(R.id.resend);
        reSendButton.getBackground().setAlpha(80);

        reSendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserVerificationManagerThread mVerificationThread = new UserVerificationManagerThread(mHandler, VerificationActivity.this, mNumber);
                mVerificationThread.start();
            }
        });

        //reSendButton.setText("登录");
        mc = new MyCount(60000, 1000);
        mc.start();

        finishSignUpButton = (Button) findViewById(R.id.finish_signup_button);
        finishSignUpButton.setText("完成注册");

        finishSignUpButton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
                if (verificationCode.getText().toString().equals("")) {
                    showNotice("请输入验证码");
                } else if (setPassword.getText().toString().equals("")) {
                    showNotice("请输入密码");
                } else if (setPassword.getText().length() < 6) {
                    showNotice("请输入6位以上密码");
                } else {
                    UserSignUpManagerThread mSignUpThread = new UserSignUpManagerThread(mHandler, VerificationActivity.this, mNumber, mVerificationCode, mPassword);
                    mSignUpThread.start();
                }
            }
        });

    }

    private void showNotice(String text) {
        new AlertDialog.Builder(VerificationActivity.this)
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

    /*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            reSendButton.setText("重新发送");
            reSendButton.setEnabled(true);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            reSendButton.setText(millisUntilFinished / 1000 + "秒");
            reSendButton.setEnabled(false);
        }
    }
}
