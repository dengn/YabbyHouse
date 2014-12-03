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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melbournestore.adaptors.ChooseAddressListAdapter;
import com.melbournestore.adaptors.ChooseAddressSuburbListAdapter;
import com.melbournestore.application.SysApplication;
import com.melbournestore.db.SharedPreferenceUtils;
import com.melbournestore.models.Area;
import com.melbournestore.models.user_iphone;
import com.melbournestore.network.AddressManagerThread;
import com.melbournestore.utils.MelbourneUtils;

import java.util.ArrayList;

public class ChooseAddressActivity extends Activity {

    public static final int result_code_suburb = 2;

    private ListView chooseAddr_list;

    private ListView chooseAddrSuburb_list;

    private ChooseAddressListAdapter mChooseAddressListAdapter;

    private ChooseAddressSuburbListAdapter mChooseAddressSuburbListAdapter;

    private String addr_unit = "";
    private String addr_street = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();

            switch (msg.what) {


                // unit = 1
                case 1:
                    addr_unit = b.getString("unit");
                    Log.d("ADDRESS", "addr_unit: " + addr_unit);
                    // street = 2
                    break;
                case 2:
                    addr_street = b.getString("street");
                    Log.d("ADDRESS", "addr_street: " + addr_street);
                    break;

            }
        }
    };
    private String addr_suburb = "";
    private int suburbId;
    private String suburbPostCode = "";
    private String areaName = "";
    private int areaFee = 0;
    private long mExitTime;
    private user_iphone mUser;
    private ArrayList<Area> mAreas;
    private Gson gson = new Gson();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_address_layout);

        SysApplication.getInstance().addActivity(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("送货地址");


        String mUserString = SharedPreferenceUtils.getLoginUser(this);
        mUser = gson.fromJson(mUserString, user_iphone.class);
        addr_unit = mUser.getUnitNo();
        addr_street = mUser.getStreet();
        addr_suburb = mUser.getSuburb().getName();
        suburbId = mUser.getSuburb().getId();
        suburbPostCode = mUser.getSuburb().getPostCode();
        Area mArea = MelbourneUtils.getAreaFromSuburb(mUser.getSuburb(), this);
        areaName = mArea.getName();
        areaFee = mArea.getFee();


//        String users_string = SharedPreferenceUtils
//                .getLoginUser(ChooseAddressActivity.this);
//        Gson gson = new Gson();
//        User[] users = gson.fromJson(users_string, User[].class);
//        User activeUser = users[MelbourneUtils.getActiveUser(users)];
//
//        addr_unit = activeUser.getUnitNo();
//        addr_street = activeUser.getStreet();
//        addr_suburb = activeUser.getSuburb();


        chooseAddr_list = (ListView) findViewById(R.id.chooseAddr_list);

        mChooseAddressListAdapter = new ChooseAddressListAdapter(this,
                mHandler, addr_unit, addr_street, addr_suburb, suburbPostCode);
        chooseAddr_list.setAdapter(mChooseAddressListAdapter);

        chooseAddrSuburb_list = (ListView) findViewById(R.id.chooseAddr_suburb);

        mChooseAddressSuburbListAdapter = new ChooseAddressSuburbListAdapter(this,
                mHandler, areaName, areaFee);
        chooseAddrSuburb_list.setAdapter(mChooseAddressSuburbListAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
        savedInstanceState.putString("addr_unit", addr_unit);
        savedInstanceState.putString("addr_street", addr_street);
        savedInstanceState.putString("addr_suburb", addr_suburb);
        savedInstanceState.putInt("suburbid", suburbId);
        savedInstanceState.putString("postcode", suburbPostCode);
        savedInstanceState.putString("areaname", areaName);
        savedInstanceState.putInt("areafee", areaFee);

        Log.d("ADDRESS", "address status saved");
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        addr_unit = savedInstanceState.getString("addr_unit");
        addr_street = savedInstanceState.getString("addr_street");
        addr_suburb = savedInstanceState.getString("addr_suburb");
        suburbId = savedInstanceState.getInt("suburbId");
        suburbPostCode = savedInstanceState.getString("postcode");
        areaName = savedInstanceState.getString("areaname");
        areaFee = savedInstanceState.getInt("areafee");

        mChooseAddressListAdapter.refresh(addr_unit, addr_street,
                addr_suburb, suburbPostCode);
        mChooseAddressSuburbListAdapter.refresh(areaName, areaFee);
        Log.d("ADDRESS", "address status restored");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_address, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.finish).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == result_code_suburb) {
            if (resultCode == RESULT_OK) {
                // get the suburb

                addr_suburb = data.getStringExtra("name");
                suburbPostCode = data.getStringExtra("postcode");
                suburbId = data.getIntExtra("suburbId", 0);
                areaName = data.getStringExtra("area");
                areaFee = data.getIntExtra("fee", 0);


                mChooseAddressListAdapter.refresh(addr_unit, addr_street,
                        addr_suburb, suburbPostCode);
                mChooseAddressSuburbListAdapter.refresh(areaName, areaFee);
                //chooseAddr_list.setAdapter(mChooseAddressListAdapter);
                //chooseAddrSuburb_list.setAdapter(mChooseAddressSuburbListAdapter);

            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }// onActivityResult

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
            case R.id.finish:

                if (addr_unit.equals("") || addr_street.equals("")
                        || addr_suburb.equals("")) {
                    new AlertDialog.Builder(ChooseAddressActivity.this)
                            .setMessage("请填上完整地址")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface,
                                                int i) {

                                        }
                                    }
                            ).show();
                } else {
//                    String users_string = SharedPreferenceUtils
//                            .getLoginUser(ChooseAddressActivity.this);
//                    Gson gson = new Gson();
//                    User[] users = gson.fromJson(users_string, User[].class);
//                    User active_user = users[MelbourneUtils.getActiveUser(users)];
//                    active_user.setUnitNo(addr_unit);
//                    active_user.setStreet(addr_street);
//                    active_user.setSuburb(addr_suburb);
//
//                    SharedPreferenceUtils.saveLoginUser(ChooseAddressActivity.this,
//                            gson.toJson(users));

                    AddressManagerThread addressThread = new AddressManagerThread(mHandler, this, mUser.getPhoneNumber(), addr_unit, addr_street, suburbId);
                    addressThread.start();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("unit",addr_unit);
                    returnIntent.putExtra("street",addr_street);
                    returnIntent.putExtra("suburb",addr_suburb);
                    returnIntent.putExtra("area", areaName);
                    returnIntent.putExtra("fee", areaFee);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
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
