package cn.novate.rxjava2.demo;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/28 16:20
 * Version 1.0
 * Params:
 * Description:
*/

public class ChapterEight {

    private static Subscription mSubscription ;


    /**
     * BUFFER：大的水缸，
     *          让上游无限的发送事件，下游一个也不处理，结果容易造成OOM
     */
    public static void demo1(){
        // 创建一个上游：Flowable
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {          // 无限for循环
                    Log.e("TAG" , "emit " + i) ;
                    emitter.onNext(i);
                }
            }
        } , BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())  // 让上游的for循环在子线程中执行
            .observeOn(AndroidSchedulers.mainThread())   // for循环执行完后切回到主线程
            .subscribe(new Subscriber<Integer>() {
                @Override
                public void onSubscribe(Subscription s) {
                    Log.e("TAG" , "subscribe") ;
                    mSubscription = s ;
                }

                @Override
                public void onNext(Integer integer) {
                    Log.e("TAG" , "next" + integer) ;
                }

                @Override
                public void onError(Throwable t) {
                    Log.e("TAG" , "error") ;
                }

                @Override
                public void onComplete() {
                    Log.e("TAG" , "complete") ;
                }
            }) ;
    }


    /**
     * Drop：丢弃存储不下的事件
     */
    public static void demo2(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10000; i++) {
                    emitter.onNext(i);
                }
            }
        } , BackpressureStrategy.DROP).subscribeOn(Schedulers.io())    // 让上游的for循环在子线程中执行
                .observeOn(AndroidSchedulers.mainThread())    // 切换到主线程执行下边的操作
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e("TAG" , "subscribe -> ") ;
                        mSubscription = s ;
                        s.request(128);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("TAG" , "next -> " + integer) ;
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("TAG" , "error") ;
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG" , "complete") ;
                    }
                }) ;

    }


    /**
     * latest：只保留最新数据
     */
    public static void demo3(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        } , BackpressureStrategy.LATEST).subscribeOn(Schedulers.io())   // 让上游的for循环在子线程中执行
                .observeOn(AndroidSchedulers.mainThread())  // 切换到主线程中执行下边操作
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e("TAG" , "subscribe") ;
                        mSubscription = s ;
                        s.request(128);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("TAG" , "next -> " + integer) ;
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("TAG" , "error") ;
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG" , "complete") ;
                    }
                }) ;
    }


    /**
     *  interval操作符
     */
    public static void demo4(){
        Flowable.interval(1 , TimeUnit.MICROSECONDS)
                .onBackpressureDrop()    // 添加背压策略
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e("TAG" , "subscribe") ;
                        mSubscription = s ;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e("TAG" , "next -> " + aLong) ;
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("TAG" , "error") ;
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG" , "complete") ;
                    }
                }) ;


    }


}
