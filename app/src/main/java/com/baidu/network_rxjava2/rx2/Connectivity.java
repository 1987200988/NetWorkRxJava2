/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.network_rxjava2.rx2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;

/**
 * Connectivity class represents current connectivity status. It wraps NetworkInfo object.
 */
public class Connectivity {
    static final int UNKNOWN_TYPE = -1;
    static final int UNKNOWN_SUB_TYPE = -1;
    public static final int WIFI_VERY_STRONG = 1;
    public static final int WIFI_LITTLE_STRONG = 2;
    public static final int WIFI_LITTLE_WEEK = 3;
    public static final int WIFI_VERY_WEEK = 4;
    public static final int WIFI_ERROR = 5;
    private NetworkInfo.State state; // NOPMD
    private NetworkInfo.DetailedState detailedState; // NOPMD
    private int type; // NOPMD
    private int subType; // NOPMD
    private boolean available; // NOPMD
    private boolean failover; // NOPMD
    private boolean roaming; // NOPMD
    private String typeName; // NOPMD
    private String subTypeName; // NOPMD
    private String reason; // NOPMD
    private String extraInfo; // NOPMD
    private int gateway; // 网关
    private boolean hasCapability;//是否ping通网关
    private boolean wifiState;//wifi开关状态
    private int wifiLevel;//Wi-Fi强度等级
    private boolean wifiEnable;//Wi-Fi是否打开
    private String connectionFast;//获取带宽

    public static Connectivity create() {
        return builder().build();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Connectivity create(@NonNull Context context) {
        Preconditions.checkNotNull(context, "context == null");
        return create(context, getConnectivityManager(context), getWifiManager(context));
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        final String service = Context.CONNECTIVITY_SERVICE;
        return (ConnectivityManager) context.getSystemService(service);
    }

    private static WifiManager getWifiManager(Context context) {
        final String wifiService = Context.WIFI_SERVICE;
        return (WifiManager) context.getSystemService(wifiService);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected static Connectivity create(@NonNull Context context, ConnectivityManager manager,
                                         WifiManager wifiManager) {
        boolean hasCapability = false;
        boolean wifiState = false;
        Preconditions.checkNotNull(context, "context == null");

        if (manager == null && wifiManager == null) {
            return create();
        }
        //        NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
        //        if (networkCapabilities == null) {
        //            hasCapability = false;
        //        } else {
        //            hasCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        //        }
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            wifiState = false;
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            wifiState = true;
        }
        final NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        final DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        int rssi = wifiManager.getConnectionInfo().getRssi();
        int wifiLevel = getWifiLevel(rssi);
        return (networkInfo == null) ? create(dhcpInfo, hasCapability, isWifiEnabled, wifiLevel, wifiState)
                : create(networkInfo, dhcpInfo, hasCapability, isWifiEnabled, wifiLevel, wifiState);
    }

    /**
     * 得到的值是一个0到-100的区间值，是一个int型数据，其中0到-50表示信号最好，
     * -50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线，一般Wifi已断则值为-200。
     *
     * @param level
     *
     * @return
     */

    public static int getWifiLevel(int level) {
        if (level <= 0 && level >= -50) {
            return WIFI_VERY_STRONG;
        } else if (level < -50 && level >= -70) {
            return WIFI_LITTLE_STRONG;
        } else if (level < -70 && level >= -80) {
            return WIFI_LITTLE_WEEK;
        } else if (level < -80 && level >= -100) {
            return WIFI_VERY_WEEK;
        } else {
            return WIFI_ERROR;
        }
    }

    private static String isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return Network.NT_WIFI;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return Network.NT_2G_2; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return Network.NT_2G_1; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return Network.NT_2G_3; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return Network.NT_2G_4; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return Network.NT_3G_1; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return Network.NT_4G; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return Network.NT_3G_2; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return Network.NT_3G_5; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return Network.NT_3G_3; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return Network.NT_3G_4; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return Network.NT_3G_6; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return Network.NT_3G_7; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return Network.NT_3G_8; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return Network.NT_3G_9; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return Network.NT_2G_5; // ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return Network.NT_NONE;
            }
        } else {
            return Network.NT_NONE;
        }
    }

    private static Connectivity create(NetworkInfo networkInfo, DhcpInfo dhcpInfo, boolean hasCapability, boolean
            isWifiEnabled, int wifiLevel, boolean wifiState) {
        int type = networkInfo.getType();
        int subtype = networkInfo.getSubtype();
        String connectionFast = isConnectionFast(type, subtype);

        return new Builder()
                .state(networkInfo.getState())
                .detailedState(networkInfo.getDetailedState())
                .type(networkInfo.getType())
                .subType(networkInfo.getSubtype())
                .available(networkInfo.isAvailable())
                .failover(networkInfo.isFailover())
                .roaming(networkInfo.isRoaming())
                .typeName(networkInfo.getTypeName())
                .subTypeName(networkInfo.getSubtypeName())
                .reason(networkInfo.getReason())
                .extraInfo(networkInfo.getExtraInfo())
                .gateway(dhcpInfo.gateway)
                .wifiEnabled(isWifiEnabled)
                .wifiLevel(wifiLevel)
                .hasCapability(hasCapability)
                .wifiState(wifiState)
                .connectionFast(connectionFast)
                .build();
    }

    private static Connectivity create(DhcpInfo dhcpInfo, boolean hasCapability, boolean
            isWifiEnabled, int wifiLevel, boolean wifiState) {
        //        int type = networkInfo.getType();
        //        int subtype = networkInfo.getSubtype();
        //        String connectionFast = isConnectionFast(type, subtype);

        return new Builder()
                //                .state(networkInfo.getState())
                //                .detailedState(networkInfo.getDetailedState())
                //                .type(networkInfo.getType())
                //                .subType(networkInfo.getSubtype())
                //                .available(networkInfo.isAvailable())
                //                .failover(networkInfo.isFailover())
                //                .roaming(networkInfo.isRoaming())
                //                .typeName(networkInfo.getTypeName())
                //                .subTypeName(networkInfo.getSubtypeName())
                //                .reason(networkInfo.getReason())
                //                .extraInfo(networkInfo.getExtraInfo())
                .gateway(dhcpInfo.gateway)
                .wifiEnabled(isWifiEnabled)
                .wifiLevel(wifiLevel)
                .hasCapability(hasCapability)
                .wifiState(wifiState)
                //                .connectionFast(connectionFast)
                .build();
    }

    private Connectivity(Builder builder) {
        state = builder.state;
        detailedState = builder.detailedState;
        type = builder.type;
        subType = builder.subType;
        available = builder.available;
        failover = builder.failover;
        roaming = builder.roaming;
        typeName = builder.typeName;
        subTypeName = builder.subTypeName;
        reason = builder.reason;
        extraInfo = builder.extraInfo;
        gateway = builder.gateway;
        wifiLevel = builder.wifiLevel;
        wifiEnable = builder.wifiEnabled;
        hasCapability = builder.hasCapability;
        wifiState = builder.wifiState;
        connectionFast = builder.connectionFast;
    }

    private Connectivity() {
        this(builder());
    }

    private static Builder builder() {
        return new Builder();
    }

    public NetworkInfo.State state() {
        return state;
    }

    public static Builder state(NetworkInfo.State state) {
        return builder().state(state);
    }

    public NetworkInfo.DetailedState detailedState() {
        return detailedState;
    }

    public static Builder state(NetworkInfo.DetailedState detailedState) {
        return builder().detailedState(detailedState);
    }

    public int type() {
        return type;
    }

    public static Builder type(int type) {
        return builder().type(type);
    }

    public int subType() {
        return subType;
    }

    public static Builder subType(int subType) {
        return builder().subType(subType);
    }

    public boolean available() {
        return available;
    }

    public static Builder available(boolean available) {
        return builder().available(available);
    }

    public boolean failover() {
        return failover;
    }

    public static Builder failover(boolean failover) {
        return builder().failover(failover);
    }

    public boolean roaming() {
        return roaming;
    }

    public boolean hasCapability() {
        return hasCapability;
    }

    public boolean wifiState() {
        return wifiState;
    }

    public static Builder roaming(boolean roaming) {
        return builder().roaming(roaming);
    }

    public String typeName() {
        return typeName;
    }

    public static Builder typeName(String typeName) {
        return builder().typeName(typeName);
    }

    public String subTypeName() {
        return subTypeName;
    }

    public static Builder subTypeName(String subTypeName) {
        return builder().subTypeName(subTypeName);
    }

    public String reason() {
        return reason;
    }

    public static Builder reason(String reason) {
        return builder().reason(reason);
    }

    public String extraInfo() {
        return extraInfo;
    }

    public int gateway() {
        return gateway;
    } // 网关

    public int wifiLevel() {
        return wifiLevel;
    }//Wi-Fi强度等级

    public boolean wifiEnable() {
        return wifiEnable;
    }//Wi-Fi开发是否打开

    public String connectionFast() {
        return connectionFast;
    }//获取带宽

    public static Builder extraInfo(String extraInfo) {
        return builder().extraInfo(extraInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Connectivity that = (Connectivity) o;

        if (type != that.type) {
            return false;
        }
        if (subType != that.subType) {
            return false;
        }
        if (available != that.available) {
            return false;
        }
        if (failover != that.failover) {
            return false;
        }
        if (roaming != that.roaming) {
            return false;
        }
        if (state != that.state) {
            return false;
        }
        if (detailedState != that.detailedState) {
            return false;
        }
        if (!typeName.equals(that.typeName)) {
            return false;
        }
        if (subTypeName != null ? !subTypeName.equals(that.subTypeName) : that.subTypeName != null) {
            return false;
        }
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) {
            return false;
        }
        if (wifiState != that.wifiState){
            return false;
        }

        return extraInfo != null ? extraInfo.equals(that.extraInfo) : that.extraInfo == null;
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + (detailedState != null ? detailedState.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + subType;
        result = 31 * result + (available ? 1 : 0);
        result = 31 * result + (failover ? 1 : 0);
        result = 31 * result + (roaming ? 1 : 0);
        result = 31 * result + typeName.hashCode();
        result = 31 * result + (subTypeName != null ? subTypeName.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (extraInfo != null ? extraInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Connectivity{"
                + "state="
                + state
                + ", detailedState="
                + detailedState
                + ", type="
                + type
                + ", subType="
                + subType
                + ", available="
                + available
                + ", failover="
                + failover
                + ", roaming="
                + roaming
                + ", typeName='"
                + typeName
                + '\''
                + ", subTypeName='"
                + subTypeName
                + '\''
                + ", reason='"
                + reason
                + '\''
                + ", extraInfo='"
                + extraInfo
                + '\''
                + ", gateway='"
                + gateway
                + '\''
                + ", connectionFast='"
                + connectionFast
                + '\''
                + ", hasCapability='"
                + hasCapability
                + '\''
                + ", wifiState='"
                + wifiState
                + '\''
                + ", wifiEnable='"
                + wifiEnable
                + '\''
                + ", wifiLevel='"
                + wifiLevel
                + '\''
                + '}';
    }

    public static class Builder {

        // disabling PMD for builder class attributes
        // because we want to have the same method names as names of the attributes for builder

        private NetworkInfo.State state = NetworkInfo.State.DISCONNECTED; // NOPMD
        private NetworkInfo.DetailedState detailedState = NetworkInfo.DetailedState.IDLE; // NOPMD
        private int type = UNKNOWN_TYPE; // NOPMD
        private int subType = UNKNOWN_SUB_TYPE; // NOPMD
        private boolean available = false; // NOPMD
        private boolean failover = false; // NOPMD
        private boolean roaming = false; // NOPMD
        private String typeName = "NONE"; // NOPMD
        private String subTypeName = "NONE"; // NOPMD
        private String reason = ""; // NOPMD
        private String extraInfo = ""; // NOPMD

        private int gateway = UNKNOWN_TYPE;
        private boolean wifiEnabled = false;
        private int wifiLevel = WIFI_ERROR;
        private boolean hasCapability = false;
        private boolean wifiState = false;
        private String connectionFast = Network.NT_NONE;

        public Builder state(NetworkInfo.State state) {
            this.state = state;
            return this;
        }

        public Builder detailedState(NetworkInfo.DetailedState detailedState) {
            this.detailedState = detailedState;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder subType(int subType) {
            this.subType = subType;
            return this;
        }

        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        public Builder failover(boolean failover) {
            this.failover = failover;
            return this;
        }

        public Builder roaming(boolean roaming) {
            this.roaming = roaming;
            return this;
        }

        public Builder typeName(String name) {
            this.typeName = name;
            return this;
        }

        public Builder subTypeName(String subTypeName) {
            this.subTypeName = subTypeName;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder extraInfo(String extraInfo) {
            this.extraInfo = extraInfo;
            return this;
        }

        public Builder gateway(int gateway) {
            this.gateway = gateway;
            return this;
        }

        public Builder connectionFast(String connectionFast) {
            this.connectionFast = connectionFast;
            return this;
        }

        public Builder wifiEnabled(boolean wifiEnabled) {
            this.wifiEnabled = wifiEnabled;
            return this;
        }

        public Builder wifiLevel(int wifiLevel) {
            this.wifiLevel = wifiLevel;
            return this;
        }

        public Builder hasCapability(boolean hasCapability) {
            this.hasCapability = hasCapability;
            return this;
        }

        public Builder wifiState(boolean wifiState) {
            this.wifiState = wifiState;
            return this;
        }

        public Connectivity build() {
            return new Connectivity(this);
        }
    }

    public static class Network {
        public static String NT_2G_1 = "NT_2G_1";
        static String NT_2G_2 = "NT_2G_2";
        static String NT_2G_3 = "NT_2G_3";
        static String NT_2G_4 = "NT_2G_4";
        static String NT_2G_5 = "NT_2G_5";
        static String NT_3G_1 = "NT_3G_1";
        static String NT_3G_2 = "NT_3G_2";
        static String NT_3G_3 = "NT_3G_3";
        static String NT_3G_4 = "NT_3G_4";
        static String NT_3G_5 = "NT_3G_5";
        static String NT_3G_6 = "NT_3G_6";
        static String NT_3G_7 = "NT_3G_7";
        static String NT_3G_8 = "NT_3G_8";
        static String NT_3G_9 = "NT_3G_9";
        static String NT_4G = "NT_4G";
        static String NT_WIFI = "NT_WIFI";
        static String NT_NONE = "NT_NONE";

    }
}
