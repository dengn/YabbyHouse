package com.melbournestore.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.melbournestore.application.SysApplication;
import com.melbournestore.network.UserVerificationManagerThread;

public class LoginActivity extends Activity {

    private ProgressDialog progress;
    private TextView loginText;
    private EditText loginNumber;
    private CheckBox loginCheckbox;
    private TextView loginTextAgreement;
    private Button loginButton;
    private long mExitTime;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //login failed, password or phone number is wrong
                case 0:
                    progress.dismiss();
                    showNotice("验证码发送失败");
                    break;
                case 1:
                    progress.dismiss();
                    Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                    intent.putExtra("number", loginNumber.getText().toString());
                    startActivity(intent);
                    break;
            }
        }

    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("注册");

        loginText = (TextView) findViewById(R.id.login_notice);
        loginText.setText("请使用手机号码登录");

        loginNumber = (EditText) findViewById(R.id.login_number);
        loginNumber.setHint("澳大利亚10位号码");

        loginCheckbox = (CheckBox) findViewById(R.id.login_aggrement_checkbox);
        loginCheckbox.setText("");

        loginTextAgreement = (TextView) findViewById(R.id.login_text_agreement);
        loginTextAgreement.setText("同意<"
                + Html.fromHtml("<u>" + "墨尔本送餐服务协议" + "</u>") + ">");

        loginTextAgreement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        DeliveryAgreementActivity.class);
                startActivity(intent);
            }

        });

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.getBackground().setAlpha(80);
        loginButton.setText("同意协议并发送验证码");

        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!loginCheckbox.isChecked()) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("请同意墨尔本送餐服务协议")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface,
                                                int i) {

                                        }
                                    }
                            ).show();
                } else if ((loginNumber.getText().length() != 10)
                        || !loginNumber.getText().toString().subSequence(0, 2)
                        .equals("04")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("手机号码不是澳洲手机\n请输入04开头号码")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface,
                                                int i) {

                                        }
                                    }
                            ).show();
                } else {

                    UserVerificationManagerThread mVerificationThread = new UserVerificationManagerThread(mHandler, LoginActivity.this, loginNumber.getText().toString());
                    mVerificationThread.start();

                    progress = new ProgressDialog(LoginActivity.this, R.style.dialog_loading);
                    progress.show();
                }


            }

        });

    }

    private void showNotice(String text) {
        new AlertDialog.Builder(LoginActivity.this)
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
}
