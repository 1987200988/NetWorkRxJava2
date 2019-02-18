/*
 * Copyright (C) 2018 Piotr Wittchen
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
package com.baidu.network_rxjava2.rx2.internet.observing;

import com.baidu.network_rxjava2.rx2.internet.observing.error.DefaultErrorHandler;
import com.baidu.network_rxjava2.rx2.internet.observing.error.ErrorHandler;
import com.baidu.network_rxjava2.rx2.internet.observing.strategy.WalledGardenInternetObservingStrategy;

/**
 * Contains state of internet connectivity settings.
 * We should use its Builder for creating new settings
 */
@SuppressWarnings("PMD") // I want to have the same method names as variable names on purpose
public final class InternetObservingSettings {
  private final int initialInterval;
  private final int interval;
  private final String host;
  private final int port;
  private final int timeout;
  private final ErrorHandler errorHandler;
  private final InternetObservingStrategy strategy;

  private InternetObservingSettings(int initialInterval, int interval, String host, int port,
      int timeout,
      ErrorHandler errorHandler, InternetObservingStrategy strategy) {
    this.initialInterval = initialInterval;
    this.interval = interval;
    this.host = host;
    this.port = port;
    this.timeout = timeout;
    this.errorHandler = errorHandler;
    this.strategy = strategy;
  }

  /**
   * @return settings with default parameters
   */
  public static InternetObservingSettings create() {
    return new Builder().build();
  }

  private InternetObservingSettings(Builder builder) {
    this(builder.initialInterval, builder.interval, builder.host, builder.port, builder.timeout,
        builder.errorHandler, builder.strategy);
  }

  private InternetObservingSettings() {
    this(builder());
  }

  private static Builder builder() {
    return new Builder();
  }

  /**
   * @return initial ping interval in milliseconds
   */
  public int initialInterval() {
    return initialInterval;
  }

  /**
   * sets initial ping interval in milliseconds
   *
   * @param initialInterval in milliseconds
   * @return Builder
   */
  public static Builder initialInterval(final int initialInterval) {
    return builder().initialInterval(initialInterval);
  }

  /**
   * @return ping interval in milliseconds
   */
  public int interval() {
    return interval;
  }

  /**
   * sets ping interval in milliseconds
   *
   * @param interval in milliseconds
   * @return Builder
   */
  public static Builder interval(final int interval) {
    return builder().interval(interval);
  }

  /**
   * @return ping host
   */
  public String host() {
    return host;
  }

  /**
   * @return ping host
   */
  public static Builder host(final String host) {
    return builder().host(host);
  }

  /**
   * @return ping port
   */
  public int port() {
    return port;
  }

  /**
   * sets ping port
   *
   * @return Builder
   */
  public static Builder port(final int port) {
    return builder().port(port);
  }

  /**
   * @return ping timeout in milliseconds
   */
  public int timeout() {
    return timeout;
  }

  /**
   * sets ping timeout in milliseconds
   *
   * @param timeout in milliseconds
   * @return Builder
   */
  public static Builder timeout(final int timeout) {
    return builder().timeout(timeout);
  }

  /**
   * @return error handler for pings and connections
   */
  public ErrorHandler errorHandler() {
    return errorHandler;
  }

  /**
   * sets error handler for pings and connections
   *
   * @return Builder
   */
  public static Builder errorHandler(final ErrorHandler errorHandler) {
    return builder().errorHandler(errorHandler);
  }

  /**
   * @return internet observing strategy
   */
  public InternetObservingStrategy strategy() {
    return strategy;
  }

  /**
   * sets internet observing strategy
   *
   * @param strategy for observing and internet connection
   * @return Builder
   */
  public static Builder strategy(final InternetObservingStrategy strategy) {
    return builder().strategy(strategy);
  }

  /**
   * Settings builder, which contains default parameters
   */
  public final static class Builder {
    private int initialInterval = 0;
    private int interval = 2000;
    private String host = "http://clients3.google.com/generate_204";
    private int port = 80;
    private int timeout = 2000;
    private ErrorHandler errorHandler = new DefaultErrorHandler();
    private InternetObservingStrategy strategy = new WalledGardenInternetObservingStrategy();

    private Builder() {
    }

    public Builder initialInterval(int initialInterval) {
      this.initialInterval = initialInterval;
      return this;
    }

    public Builder interval(int interval) {
      this.interval = interval;
      return this;
    }

    public Builder host(String host) {
      this.host = host;
      return this;
    }

    public Builder port(int port) {
      this.port = port;
      return this;
    }

    public Builder timeout(int timeout) {
      this.timeout = timeout;
      return this;
    }

    public Builder errorHandler(ErrorHandler errorHandler) {
      this.errorHandler = errorHandler;
      return this;
    }

    public Builder strategy(InternetObservingStrategy strategy) {
      this.strategy = strategy;
      return this;
    }

    public InternetObservingSettings build() {
      return new InternetObservingSettings(this);
    }
  }
}
