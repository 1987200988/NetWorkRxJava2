package com.baidu.network_rxjava2;

import android.util.Log;

/**
 * Created by v_liwei10 on 2019/3/25.
 */

public abstract class TestFather {


    private Object example;


    abstract Object getExample();

    public TestFather(){
         example = getExample();
        Log.e("TestFather", "TestFather: " );
    }



}
