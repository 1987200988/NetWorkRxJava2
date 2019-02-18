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
package com.baidu.network_rxjava2.rx2.internet.observing.strategy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.baidu.network_rxjava2.NetConnected;
import com.baidu.network_rxjava2.rx2.Preconditions;
import com.baidu.network_rxjava2.rx2.internet.observing.InternetObservingStrategy;
import com.baidu.network_rxjava2.rx2.internet.observing.error.ErrorHandler;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Socket strategy for monitoring connectivity with the Internet.
 * It monitors Internet connectivity via opening socket connection with the remote host.
 */
public class SocketInternetObservingStrategy implements InternetObservingStrategy {
  private static final String EMPTY_STRING = "";
  private static final String DEFAULT_HOST = "www.google.com";
  private static final String HTTP_PROTOCOL = "http://";
  private static final String HTTPS_PROTOCOL = "https://";

  @Override public String getDefaultPingHost() {
    return DEFAULT_HOST;
  }

  @Override public Observable<NetConnected> observeInternetConnectivity(final int initialIntervalInMs,
      final int intervalInMs, final String host, final int port, final int timeoutInMs,
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

  @Override public Single<Boolean> checkInternetConnectivity(final String host, final int port,
      final int timeoutInMs, final ErrorHandler errorHandler) {
    checkGeneralPreconditions(host, port, timeoutInMs, errorHandler);

    return Single.create(new SingleOnSubscribe<Boolean>() {
      @Override public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
        emitter.onSuccess(isConnected(host, port, timeoutInMs, errorHandler));
      }
    });
  }

  /**
   * adjusts host to needs of SocketInternetObservingStrategy
   *
   * @return transformed host
   */
  protected String adjustHost(final String host) {
    if (host.startsWith(HTTP_PROTOCOL)) {
      return host.replace(HTTP_PROTOCOL, EMPTY_STRING);
    } else if (host.startsWith(HTTPS_PROTOCOL)) {
      return host.replace(HTTPS_PROTOCOL, EMPTY_STRING);
    }
    return host;
  }

  private void checkGeneralPreconditions(String host, int port, int timeoutInMs,
      ErrorHandler errorHandler) {
    Preconditions.checkNotNullOrEmpty(host, "host is null or empty");
    Preconditions.checkGreaterThanZero(port, "port is not a positive number");
    Preconditions.checkGreaterThanZero(timeoutInMs, "timeoutInMs is not a positive number");
    Preconditions.checkNotNull(errorHandler, "errorHandler is null");
  }

//  /**
//   * checks if device is connected to given host at given port
//   *
//   * @param host to connect
//   * @param port to connect
//   * @param timeoutInMs connection timeout
//   * @param errorHandler error handler for socket connection
//   * @return boolean true if connected and false if not
//   */
//  protected boolean isConnected(final String host, final int port, final int timeoutInMs,
//      final ErrorHandler errorHandler) {
//    final Socket socket = new Socket();
//    return isConnected(socket, host, port, timeoutInMs, errorHandler);
//  }
//
//  /**
//   * checks if device is connected to given host at given port
//   *
//   * @param socket to connect
//   * @param host to connect
//   * @param port to connect
//   * @param timeoutInMs connection timeout
//   * @param errorHandler error handler for socket connection
//   * @return boolean true if connected and false if not
//   */
//  protected boolean isConnected(final Socket socket, final String host, final int port,
//      final int timeoutInMs, final ErrorHandler errorHandler) {
//    boolean isConnected;
//    try {
//      socket.connect(new InetSocketAddress(host, port), timeoutInMs);
//      isConnected = socket.isConnected();
//    } catch (IOException e) {
//      isConnected = Boolean.FALSE;
//    } finally {
//      try {
//        socket.close();
//      } catch (IOException exception) {
//        errorHandler.handleError(exception, "Could not close the socket");
//      }
//    }
//    return isConnected;
//  }
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
}
