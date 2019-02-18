/*
 * Copyright (C) 2017 Piotr Wittchen
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
package com.baidu.network_rxjava2.rx2.internet.observing.strategy;

import java.util.concurrent.TimeUnit;

import com.baidu.network_rxjava2.NetConnected;
import com.baidu.network_rxjava2.rx2.Connectivity;
import com.baidu.network_rxjava2.rx2.Preconditions;
import com.baidu.network_rxjava2.rx2.internet.observing.InternetObservingStrategy;
import com.baidu.network_rxjava2.rx2.internet.observing.error.ErrorHandler;

import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Walled Garden Strategy for monitoring connectivity with the Internet.
 * This strategy handle use case of the countries behind Great Firewall (e.g. China),
 * which does not has access to several websites like Google. It such case, different HTTP responses
 * are generated. Instead HTTP 200 (OK), we got HTTP 204 (NO CONTENT), but it still can tell us
 * if a device is connected to the Internet or not.
 */
public class WalledGardenInternetObservingStrategy implements InternetObservingStrategy {
    private static final String DEFAULT_HOST = "http://clients3.google.com/generate_204";
    private static final String HTTP_PROTOCOL = "http://";
    private static final String HTTPS_PROTOCOL = "https://";

    @Override
    public String getDefaultPingHost() {
        return DEFAULT_HOST;
    }

    @Override
    public Observable<NetConnected> observeInternetConnectivity(final int initialIntervalInMs,
                                                                final int intervalInMs, final String host,
                                                                final int port,
                                                                final int timeoutInMs,
                                                                final ErrorHandler errorHandler) {

        Preconditions.checkGreaterOrEqualToZero(initialIntervalInMs,
                "initialIntervalInMs is not a positive number");
        Preconditions.checkGreaterThanZero(intervalInMs, "intervalInMs is not a positive number");
        checkGeneralPreconditions(host, port, timeoutInMs, errorHandler);

        final String adjustedHost = adjustHost(host);

        return Observable.interval(initialIntervalInMs, 5000, TimeUnit.MILLISECONDS,
                Schedulers.io()).map(new Function<Long, NetConnected>() {
            @Override
            public NetConnected apply(@android.support.annotation.NonNull Long tick) {
                Boolean connected = isConnected(host, port, timeoutInMs, errorHandler);
                Boolean connectedBaidu = isConnectedBaidu(host, port, timeoutInMs, errorHandler);
                NetConnected netConnected = new NetConnected();
                netConnected.isConnectedBaidu = connectedBaidu;
                netConnected.isConnectedServer = connected;

                return netConnected;
            }
        })
                .distinctUntilChanged();
    }

    @Override
    public Single<Boolean> checkInternetConnectivity(final String host, final int port,
                                                     final int timeoutInMs, final ErrorHandler errorHandler) {
        checkGeneralPreconditions(host, port, timeoutInMs, errorHandler);

        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@android.support.annotation.NonNull SingleEmitter<Boolean> emitter) throws Exception {
                emitter.onSuccess(isConnected(host, port, timeoutInMs, errorHandler));
            }
        });
    }

    protected String adjustHost(final String host) {
        if (!host.startsWith(HTTP_PROTOCOL) && !host.startsWith(HTTPS_PROTOCOL)) {
            return HTTP_PROTOCOL.concat(host);
        }

        return host;
    }

    private void checkGeneralPreconditions(final String host, final int port, final int timeoutInMs,
                                           final ErrorHandler errorHandler) {
        Preconditions.checkNotNullOrEmpty(host, "host is null or empty");
        Preconditions.checkGreaterThanZero(port, "port is not a positive number");
        Preconditions.checkGreaterThanZero(timeoutInMs, "timeoutInMs is not a positive number");
        Preconditions.checkNotNull(errorHandler, "errorHandler is null");
    }

    /**
     * 判断当前的网络连接状态是否能用
     * return 0可用   其他值不可用
     */
    protected Boolean isConnected(final String host, final int port, final int timeoutInMs,
                                  final ErrorHandler errorHandler) {
        Runtime runtime = Runtime.getRuntime();
        try {
            //            Connectivity connectivity = Connectivity.create();
            //            if (connectivity.wifiState() && connectivity.extraInfo().contains("kinglong")) {
            Process p = runtime.exec("ping -c 1 192.168.10.6");
            int ret = p.waitFor();
            if (ret == 0) {
                return true;
            }
            //            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前的网络连接状态是否能用
     * return 0可用   其他值不可用
     */
    protected Boolean isConnectedBaidu(final String host, final int port, final int timeoutInMs,
                                       final ErrorHandler errorHandler) {
        Runtime runtime = Runtime.getRuntime();
        try {
            //            Connectivity connectivity = Connectivity.create();
            //            if (connectivity.wifiState() ) {
            Process p = runtime.exec("ping -c 1 www.baidu.com");
            int ret = p.waitFor();
            if (ret == 0) {
                return true;
            }
            //            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //
    //    protected HttpURLConnection createHttpUrlConnection(final String host, final int port,
    //                                                        final int timeoutInMs) throws IOException {
    //        URL initialUrl = new URL(host);
    //        URL url = new URL(initialUrl.getProtocol(), initialUrl.getHost(), port, initialUrl.getFile());
    //        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    //        urlConnection.setConnectTimeout(timeoutInMs);
    //        urlConnection.setReadTimeout(timeoutInMs);
    //        urlConnection.setInstanceFollowRedirects(false);
    //        urlConnection.setUseCaches(false);
    //        return urlConnection;
    //    }
}
