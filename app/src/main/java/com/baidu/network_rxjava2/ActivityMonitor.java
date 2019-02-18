package com.baidu.network_rxjava2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

/**
 * Copyright (c) 2018 Baidu, Inc. All Rights Reserved.
 * Created by mashigui on 2018/6/8
 * 监听activity的生命周期类
 */

public class ActivityMonitor implements Application.ActivityLifecycleCallbacks {
    private List<Activity> activityList = Collections.synchronizedList(new ArrayList<Activity>());

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activityList.remove(activity);
    }

    public void exit() {
        Iterator<Activity> iterator = activityList.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            activity.finish();
            iterator.remove();
        }
        activityList.clear();
        System.exit(0);
    }

    public void restart() {
//        Intent intent = new Intent(App.getApplication(), MainActivity.class);
//        intent.putExtra("isExist", true); // system trick
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        App.getApplication().startActivity(intent);
//        System.exit(0);
    }

}
