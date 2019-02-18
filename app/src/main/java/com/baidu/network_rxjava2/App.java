package com.baidu.network_rxjava2;

import java.io.IOException;

import android.app.Application;
import android.util.Log;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by v_liwei10 on 2018/10/31.
 */

public class App extends Application {
    public static App app;
    @Override
    public void onCreate() {
        super.onCreate();
            app = this;
            initCrashHandler();
//            setRxJavaErrorHandler();
    }
    private void initCrashHandler() {
//        ActivityMonitor lifecycleCallbacks = new ActivityMonitor();
//        registerActivityLifecycleCallbacks(lifecycleCallbacks);
//        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(true, lifecycleCallbacks));
    }
    public static App getApplication(){
        return app;
    }
//    private void setRxJavaErrorHandler() {
//        RxJavaPlugins.setErrorHandler(e -> {
//            if (e instanceof UndeliverableException) {
//                e = e.getCause();
//            }
//            if (e instanceof IOException) {
//                // 没事，无关紧要的网络问题或API在取消时抛出的异常。
//                return;
//            }
//            if (e instanceof InterruptedException) {
//                // 没事，一些阻塞代码被dispose调用中断
//                return;
//            }
//            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
//                // 这可能是程序的一个bug
//                Thread.currentThread().getUncaughtExceptionHandler()
//                        .uncaughtException(Thread.currentThread(), e);
//                return;
//            }
//            if (e instanceof IllegalStateException) {
//                // 这是RxJava或自定义操作符的一个bug
//                Thread.currentThread().getUncaughtExceptionHandler()
//                        .uncaughtException(Thread.currentThread(), e);
//                return;
//            }
//            Log.e("App", "Undeliverable exception received, not sure what to do",e);
//        });
//    }
}
