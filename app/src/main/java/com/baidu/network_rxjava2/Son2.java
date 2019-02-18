package com.baidu.network_rxjava2;

import android.databinding.Observable;
import android.databinding.ObservableField;

/**
 * Created by v_liwei10 on 2018/10/30.
 */

public class Son2 extends Father{
    public Son2(int type) {
        super(type);
    }

    public ObservableField<String> tag = new ObservableField<>();

}
