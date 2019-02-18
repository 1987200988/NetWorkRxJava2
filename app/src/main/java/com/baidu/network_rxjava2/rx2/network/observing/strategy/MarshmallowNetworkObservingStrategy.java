/*
 * Copyright (C) 2016 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.network_rxjava2.rx2.network.observing.strategy;

import static com.baidu.network_rxjava2.rx2.ReactiveNetwork.LOG_TAG;

import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


import com.baidu.network_rxjava2.rx2.Connectivity;
import com.baidu.network_rxjava2.rx2.network.observing.NetworkObservingStrategy;

/**
 * Network observing strategy for devices with Android Marshmallow (API 23) or higher.
 * Uses Network Callback API and handles Doze mode.
 */
@TargetApi(23) public class MarshmallowNetworkObservingStrategy
    implements NetworkObservingStrategy {
  protected static final String ERROR_MSG_NETWORK_CALLBACK =
      "could not unregister network callback";
  protected static final String ERROR_MSG_RECEIVER = "could not unregister receiver";
  protected static final String ERROR_MSG_RECEIVER_WIFI_SWITCH = "could not unregister wifi_switch_receiver";
  private final String NETWORK_STATE_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

  @SuppressWarnings("NullAway") // it has to be initialized in the Observable due to Context
  private ConnectivityManager.NetworkCallback networkCallback;
  private PublishSubject<Connectivity> connectivitySubject = PublishSubject.create();
  private BroadcastReceiver idleReceiver;
//  private WifiBroadCastReceiver wifiSwitch = new WifiBroadCastReceiver();

  @SuppressWarnings("NullAway") // networkCallback cannot be initialized here
  public MarshmallowNetworkObservingStrategy() {
    this.idleReceiver = createIdleBroadcastReceiver();
  }

  @Override public Observable<Connectivity> observeNetworkConnectivity(final Context context) {
//    final String service = Context.CONNECTIVITY_SERVICE;
//    final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(service);
//    networkCallback = createNetworkCallback(context);
//
//    registerIdleReceiver(context);
//    registerWifiSwitchReceiver(context);
//
//    final NetworkRequest request =
//        new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
//            .build();

//    manager.registerNetworkCallback(request, networkCallback);



//    return connectivitySubject.toFlowable(BackpressureStrategy.LATEST).doOnCancel(new Action() {
//      @Override public void run() {
////        tryToUnregisterCallback(manager);
////        tryToUnregisterReceiver(context);
////        tryToUnregisterWifiSwitchReceiver(context);
//      }
//    }).startWith(Connectivity.create(context)).distinctUntilChanged().toObservable();
    return Observable.interval(5, 2, TimeUnit.MILLISECONDS,
            Schedulers.io()).map(new Function<Long, Connectivity>() {
      @Override public Connectivity apply(@io.reactivex.annotations.NonNull Long tick) throws Exception {
        Log.e("accept", "apply: " );
        return Connectivity.create(context);
      }
    }).distinctUntilChanged();
  }

//  private void registerWifiSwitchReceiver(Context context) {
//    IntentFilter filter = new IntentFilter();
//    filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//    filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//    filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//    filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
//
//    context.registerReceiver(wifiSwitch,filter);
//  }

  protected void registerIdleReceiver(final Context context) {
    final IntentFilter filter = new IntentFilter(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
    context.registerReceiver(idleReceiver, filter);
  }

  @NonNull protected BroadcastReceiver createIdleBroadcastReceiver() {
    return new BroadcastReceiver() {
      @Override public void onReceive(final Context context, final Intent intent) {
        if (isIdleMode(context)) {
          onNext(Connectivity.create());
        } else {
          onNext(Connectivity.create(context));
        }
      }
    };
  }

  protected boolean isIdleMode(final Context context) {
    final String packageName = context.getPackageName();
    final PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    boolean isIgnoringOptimizations = manager.isIgnoringBatteryOptimizations(packageName);
    return manager.isDeviceIdleMode() && !isIgnoringOptimizations;
  }

  protected void tryToUnregisterCallback(final ConnectivityManager manager) {
    try {
      manager.unregisterNetworkCallback(networkCallback);
    } catch (Exception exception) {
      onError(ERROR_MSG_NETWORK_CALLBACK, exception);
    }
  }

  protected void tryToUnregisterReceiver(Context context) {
    try {
      context.unregisterReceiver(idleReceiver);
    } catch (Exception exception) {
      onError(ERROR_MSG_RECEIVER, exception);
    }
  }

//  protected void tryToUnregisterWifiSwitchReceiver(Context context) {
//    try {
//      context.unregisterReceiver(wifiSwitch);
//    } catch (Exception exception) {
//      onError(ERROR_MSG_RECEIVER_WIFI_SWITCH, exception);
//    }
//  }

//  class WifiBroadCastReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
////      switch (intent.getAction()) {
////        case WifiManager.WIFI_STATE_CHANGED_ACTION:
////          int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
////                  WifiManager.WIFI_STATE_DISABLED);
////          switch (wifiState) {
////            case WifiManager.WIFI_STATE_DISABLED:
//////              doSomething();
////              onNext(Connectivity.create(context));
////              break;
////            case WifiManager.WIFI_STATE_ENABLED:
//////              doSomething();
////              onNext(Connectivity.create(context));
////              break;
////          }
////          break;
////        case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
////          break;
//      onNext(Connectivity.create(context));
//      }
//    }
//  }



  @Override public void onError(final String message, final Exception exception) {
    Log.e(LOG_TAG, message, exception);
  }

  protected ConnectivityManager.NetworkCallback createNetworkCallback(final Context context) {
    return new ConnectivityManager.NetworkCallback() {
      @Override public void onAvailable(Network network) {
        onNext(Connectivity.create(context));
      }

      @Override public void onLost(Network network) {
        onNext(Connectivity.create(context));
      }
    };
  }

  protected void onNext(Connectivity connectivity) {
    connectivitySubject.onNext(connectivity);
  }
}
