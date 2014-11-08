package com.melbournestore.application;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

public class SysApplication {
    private static SysApplication instance;
    private static boolean isLoggedIn = false;
    private List<Activity> mList = new LinkedList<Activity>();


    private SysApplication() {
    }

    public synchronized static SysApplication getInstance() {
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }

    public static boolean getLoginStatus() {
        return isLoggedIn;
    }

    public static void setLoginStatus(boolean status) {
        isLoggedIn = status;

    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


}
