package cn.novate.rxjava2.demo;

import android.graphics.Shader;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/27.
 */

public class ChapterSeven  {


    private static Subscription mSubscription ;
    /**
     * Flowable最基本用法
     */
    public static void demo1(){
        // 创建一个上游：Flowable
        Flowable<Integer> upStream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.e("TAG" , "emit 1") ;
                emitter.onNext(1);
                Log.e("TAG" , "emit 2") ;
                emitter.onNext(2);
                Log.e("TAG" , "emit 3") ;
                emitter.onNext(3);

                Log.e("TAG" , "emit complete") ;
                emitter.onComplete();

            }   // 参数BackpressureStrategy.ERROR作用：
                // 用来选择背压，用于解决上下游发射数据速度不平衡问题，如果速度不一致，直接抛异常MissingBackpressureException
        } , BackpressureStrategy.ERROR) ;


        // 创建一个下游：Subscriber
        Subscriber<Integer> downStream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e("TAG" , "subscribe") ;
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.e("TAG" , "next -> " + integer) ;
            }

            @Override
            public void onError(Throwable t) {
                Log.e("TAG" , "error -> " + t) ;
            }

            @Override
            public void onComplete() {
                Log.e("TAG" , "complete") ;
            }
        } ;

        // 建立连接
        upStream.subscribe(downStream) ;
    }


    /**
     * Flowable用法：在onSubscribe()中不加 s.request(Long.MAX_VALUE);
     */
    public static void demo2(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.e("TAG" , "emit 1") ;
                emitter.onNext(1);
                Log.e("TAG" , "emit 2") ;
                emitter.onNext(2);
                Log.e("TAG" , "emit 3") ;
                emitter.onNext(3);

                Log.e("TAG" , "emit complete") ;
                emitter.onComplete();
            }
        } , BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e("TAG" , "subscribe") ;
            }

            @Override
            public void onNext(Integer integer) {
                Log.e("TAG" , "next -> " + integer) ;
            }

            @Override
            public void onError(Throwable t) {
                Log.e("TAG" , "error -> " + t) ;
            }

            @Override
            public void onComplete() {
                Log.e("TAG" , "complete") ;
            }
        });

    }


    /**
     * Flowable：
     *          让上、下游处于异步线程中，也就是说让上游在子线程中执行，下游在主线程中执行
     */
    public static void demo3(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.e("TAG" , "emit 1") ;
                emitter.onNext(1);
                Log.e("TAG" , "emit 2") ;
                emitter.onNext(2);
                Log.e("TAG" , "emit 3") ;
                emitter.onNext(3);

                Log.e("TAG" , "emit complete") ;
                emitter.onComplete();
            }
        } , BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e("TAG" , "subscribe") ;
                        mSubscription = s ;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("TAG" , "next -> " + integer) ;
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("TAG" , "error -> " + t) ;
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG" , "complete") ;
                    }
                }) ;
    }


    /**
     *
     */
    public static void demo4(){

    }
}
