package com.baidu.network_rxjava2;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.baidu.network_rxjava2.databinding.ActivityMainBinding;
import com.baidu.network_rxjava2.rx2.Connectivity;
import com.baidu.network_rxjava2.rx2.ReactiveNetwork;
import com.baidu.network_rxjava2.rx2.network.observing.strategy.MarshmallowNetworkObservingStrategy;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Disposable networkDisposable;
    private Disposable internetDisposable;
    private BatteryReceiver batteryReceiver;
    private Son2 son2;
    public static final String TAG = "wwww";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        unregisterReceiver(batteryReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Log.e(TAG, "onCreate: " + Environment.getExternalStorageDirectory().getPath());
        Log.e(TAG, "onCreate: "+getExternalCacheDir());

        long num = 1207695295;
        float  progress = 0.965f;
        long l = (long) (num * progress);
        int ll = (int) (num * progress);
        Log.e(TAG, "onCreate: long==="+l+"int===="+ll );


        Disposable subscribe = Observable.interval(1000, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
//                        handleResultCheck(Car.getInstance().getDisk());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        //                ReactiveNetwork.observeNetworkConnectivity(this, new MarshmallowNetworkObservingStrategy())
//
//
//                        .subscribe(new Consumer<Connectivity>() {
//                    @Override
//                    public void accept(Connectivity connectivity) throws Exception {
//                        Log.e(TAG, "accept: "+connectivity.toString() );
//                    }
//                });
        ReactiveNetwork.observeInternetConnectivity().subscribe(new Consumer<NetConnected>() {
            @Override
            public void accept(NetConnected net) throws Exception {
                Log.e(TAG, "acceptbaidu===: "+ net.isConnectedBaidu+"server==="+net.isConnectedServer);
            }
        });

//        ReactiveNetwork.observeInternetConnectivity("server").subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                Log.e(TAG, "acceptserver: "+aBoolean );
//            }
//        });
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int dpi = metrics.densityDpi;
        Log.e(TAG, "onCreate: dpi===="+dpi );

        ApplicationInfo appInfo = null;
        int strInfo = 0;
        try {
             appInfo = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            strInfo = appInfo.metaData.getInt("screenDensityDpi", 0);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "onCreate: 有问题了" );
            e.printStackTrace();
        }
        Log.e(TAG, "onCreate: "+strInfo );



        activityMainBinding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext("1");
                        //                    int i = 0 / 1;
                        //                    int i1 = 1 / 0;
                        e.onComplete();
                    }
                }).onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: 我处理");
                        return "1";
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        Log.e(TAG, "accept: " + s);
                        int i = 0 / 1;
                        int i1 = 1 / 0;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ", throwable);
                    }
                });

            }
        });
        //                            .fromCallable(() -> requestEndCollection(taskId))

        activityMainBinding.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disposable subscribe = Single.fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "abc";
                    }
                })
                        .compose(RxjavaHelper.handleGrpc())
                        //                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                        // .mainThread())
                        .subscribe(
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {

                                        Log.e(TAG + "abc", "accept: " + s + Thread.currentThread());
                                        //                                        int i = 0 / 1;
                                        //                                        int i2 = 1 / 0;

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e(TAG + "abc", "accept: ", throwable);
                                    }
                                });

            }
        });

        activityMainBinding.bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.fromArray(1, 2, 3, 4, 5)
                        .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                            @Override
                            public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {

                                int delay = 0;
                                if (integer == 3) {
                                    delay = 500;//延迟500ms发射
                                }
                                return Observable.just(integer * 10).delay(delay, TimeUnit.MILLISECONDS);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.e(TAG, "accept:" + integer);
                            }
                        });

            }
        });
        activityMainBinding.bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        Thread.sleep(1000);
                        Log.e(TAG, "call: " + Thread.currentThread());
                        return 1;
                    }
                })
                        .flatMap(new Function<Integer, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {

                                int delay = 0;
                                if (integer == 1) {
                                    delay = 500;//延迟500ms发射
                                }
                                Log.e(TAG, "apply: " + Thread.currentThread());

                                return Observable.just(integer * 10 + "").delay(delay, TimeUnit.MILLISECONDS);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((Consumer<String>) s -> {
                            Log.e(TAG, "onClick: " + s + Thread.currentThread());
                        });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void test() {
    }

}

