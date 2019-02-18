package com.baidu.network_rxjava2;


import java.util.Properties;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxjavaHelper {




    public static  SingleTransformer handleGrpcRequest() {
      return upstream -> {
          upstream.subscribeOn(Schedulers.io());
                  upstream.observeOn(AndroidSchedulers.mainThread());
          return upstream;
      };


    }

    public static ObservableTransformer handleObservableGrpcRequest() {
        return upstream -> {
            upstream.subscribeOn(Schedulers.io());
            upstream.observeOn(AndroidSchedulers.mainThread());
            return upstream;
        };
    }


    public static FlowableTransformer handleFlowable() {
        return upstream -> {

            upstream.subscribeOn(Schedulers.io());
            upstream.observeOn(AndroidSchedulers.mainThread());
            return upstream;
        };
    }



    public static <T> SingleTransformer<T,T> handleGrpc() {
        return
//                upstream -> upstream
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
        new SingleTransformer() {
            @Override
            public SingleSource apply(Single upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };




    }





}
