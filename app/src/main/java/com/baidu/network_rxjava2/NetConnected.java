package com.baidu.network_rxjava2;

import com.baidu.network_rxjava2.rx2.Connectivity;

/**
 * Created by v_liwei10 on 2019/1/2.
 */

public class NetConnected {

    public boolean isConnectedServer;
    public boolean isConnectedBaidu;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NetConnected that = (NetConnected) o;

        if (isConnectedServer != that.isConnectedServer) {
            return false;
        }
        if (isConnectedBaidu != that.isConnectedBaidu) {
            return false;
        }

        return true;
    }
}
