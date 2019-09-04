package com.baidu.network_rxjava2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.network_rxjava2.databinding.ActivityMainBinding;
import com.baidu.network_rxjava2.rx2.Connectivity;
import com.baidu.network_rxjava2.rx2.ReactiveNetwork;
import com.baidu.network_rxjava2.rx2.network.observing.strategy.MarshmallowNetworkObservingStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
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
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Disposable networkDisposable;
    private Disposable internetDisposable;
    private BatteryReceiver batteryReceiver;
    private Son2 son2;
    public static final String TAG = "wwww";
    private String json = "[{'STATIC_PAD': 'pass'},{'STATIC_PAD_SUPPORT': 'fail'}]";
    List<Integer> listStatus = new ArrayList<Integer>();
    private int i;

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

        float  progress = 0.965f;

        TestFather testSon = new TestSon();
        if(testSon.getExample()!=null){
            Log.e(TAG, "onCreate: TestFather" );
        }


        ArrayList<Son> list = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Son son = new Son();
        son.sun = Sun.Fail;
        list.add(son);
        String s = gson.toJson(list);
        Log.e(TAG, "onCreate: "+s );



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
        ReactiveNetwork.observeNetworkConnectivity(this).subscribe(new Consumer<Connectivity>() {
            @Override
            public void accept(Connectivity connectivity) throws Exception {
                Log.e(TAG, "accept: "+ Build.VERSION.SDK_INT+"connectivity="+connectivity.toString());
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
//                        int i = 0 / 1;
//                        int i1 = 1 / 0;
                        e.onNext(String.valueOf(0/1));

                        e.onComplete();
                    }
                }).onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "apply123456: 我处理");
                        return "2";
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        Log.e(TAG, "accept123456:abcde " + s);
//                        int i = 0 / 1;
//                        int i1 = 1 / 0;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept123456: ", throwable);
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
                        int i = 0 / 1;
                        int i2 = 1 / 0;
                        return "abc";
                    }
                })
                        .compose(RxjavaHelper.handleGrpc())
                        //                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                        // .mainThread())
                        .onErrorReturn(new Function<Throwable, String>() {
                            @Override
                            public String apply(Throwable throwable) throws Exception {
                                Log.e(TAG, "apply: 我处理");
                                return "3";
                            }
                        })
                        .subscribe(
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {

                                        Log.e(TAG + "abc", "accept: " + s + Thread.currentThread());
//                                                                                int i = 0 / 1;
//                                                                                int i2 = 1 / 0;

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
        i = 0;

        activityMainBinding.bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test(i);
                i++;



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


        Observable.fromCallable(new Callable<String>() {

            @Override
            public String call() throws Exception {
                Log.e(TAG, "call: 1112312312312312312312312311"+Thread.currentThread().getName() );
                return 1+"";
            }
        }).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });









        CertificatePinner certificatePinner = new CertificatePinner.Builder().add("http://www.google.com",
                "sha256/sdfsdg").build();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient build = builder.certificatePinner(certificatePinner).build();
        build.newCall(new Request.Builder().url("http://www.google.com").build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("abcd", "onFailure: ",e );

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("abcd", "onResponse: "+response.toString() );
            }
        });



    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void test(int i) {
        switchCaseTest(i);
    }


    public void switchCaseTest(int i){
            String name = null;
        switch (i){
            case 0:
                name = "0";
                break;
            case 1:
                name = "1";

                break;
            case 2:
                name = "2";

                break;


            case 3:
                name = "3";
                break;
            default:
                break;


        }

        Log.e(TAG, "switchCaseTest: "+name );




    }


}

