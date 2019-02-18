package com.baidu.network_rxjava2;

import java.io.PrintWriter;
import java.io.StringWriter;


import android.util.Log;

/**
 * Copyright (c) 2018 Baidu, Inc. All Rights Reserved.
 * Created by mashigui on 2018/6/8
 * 崩溃后的处理程序
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private boolean isRestart;
    ActivityMonitor lifecycleCallbacks;

    public CrashHandler(boolean restart, ActivityMonitor lifecycleCallbacks) {
        isRestart = restart;
        this.lifecycleCallbacks = lifecycleCallbacks;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e("crash", "uncaughtException: ",e );
        Log.e("pilot-panel APP Crash ",  printStackTraceToString(e));
        if (isRestart) {
            lifecycleCallbacks.restart();
        } else {
            lifecycleCallbacks.exit();
        }
    }

    public static String printStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
}
