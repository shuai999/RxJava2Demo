package cn.novate.rxjava2.demo;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.observable.ObservableAll;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/26 9:49
 * Version 1.0
 * Params:
 * Description:  教程一代码演示
*/

public class ChapterOne {


    /**
     * 最基本使用
     */
    public static void demo1(){
        // 创建一个上游 Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }) ;

        // 创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("TAG" , "subscribe") ;
            }

            @Override
            public void onNext(Integer value) {
                Log.e("TAG" , "" + value) ;
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG" , "error") ;
            }

            @Override
            public void onComplete() {
                Log.e("TAG" , "complete") ;
            }
        } ;

        // 建立连接
        observable.subscribe(observer);

        // 运行结果如下
        /*04-26 09:58:34.796 27647-27647/cn.novate.rxjava2 E/TAG: subscribe
        04-26 09:58:34.796 27647-27647/cn.novate.rxjava2 E/TAG: 1
        04-26 09:58:34.796 27647-27647/cn.novate.rxjava2 E/TAG: 2
        04-26 09:58:34.796 27647-27647/cn.novate.rxjava2 E/TAG: 3
        04-26 09:58:34.796 27647-27647/cn.novate.rxjava2 E/TAG: complete*/
    }

    /**
     * 链式调用把上边代码连接起来
     */
    public static void demo2(){
        ObservableAll.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("TAG" , "subscribe") ;
            }

            @Override
            public void onNext(Integer value) {
                Log.e("TAG" , ""+value) ;
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG" , "error") ;
            }

            @Override
            public void onComplete() {
                Log.e("TAG" , "complete") ;
            }
        });

        // 运行结果和上边一样
    }


    /**
     *  让上游依次发送1、2、3、complete、4，在下游接收到第二个事件后，调用 dispose()切断水管
     */
    public static void demo3(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                Log.e("TAG" , "emit 1") ;
                emitter.onNext(1);
                Log.e("TAG" , "emit 2") ;
                emitter.onNext(2);
                Log.e("TAG" , "emit 3") ;
                emitter.onNext(3);

                Log.e("TAG" , "complete") ;
                emitter.onComplete();

                Log.e("TAG" , "emit 4") ;
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {

            private Disposable mDisposable ;
            private int i ;
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("TAG" , "subscribe") ;
                mDisposable = d ;
            }

            @Override
            public void onNext(Integer value) {
                Log.e("TAG" , "next" + value) ;

                i++ ;
                if (i == 2){
                    Log.e("TAG" , "dispose") ;
                    mDisposable.dispose();
                    Log.e("TAG" , "isDisposed：" + mDisposable.isDisposed()) ;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG" , "error") ;
            }

            @Override
            public void onComplete() {
                Log.e("TAG" , "complete") ;
            }
        });

        // 运行结果
        /*04-26 10:39:25.721 13802-13802/cn.novate.rxjava2 E/TAG: subscribe
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: emit 1
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: next1
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: emit 2
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: next2
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: dispose
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: isDisposed：true
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: emit 3
        04-26 10:39:25.722 13802-13802/cn.novate.rxjava2 E/TAG: complete
        04-26 10:39:25.723 13802-13802/cn.novate.rxjava2 E/TAG: emit 4*/
    }


    /**
     * 带有一个参数的 Consumer参数的，表示只关心 onNext()事件，其余事件不关心
     */
    public static void demo4(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e("TAG" , "emit 1") ;
                emitter.onNext(1);
                Log.e("TAG" , "emit 2") ;
                emitter.onNext(2);
                Log.e("TAG" , "emit 3") ;
                emitter.onNext(3);

                Log.e("TAG" , "complete") ;
                emitter.onComplete();

                Log.e("TAG" , "emit 4") ;
                emitter.onNext(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("TAG" , "next " + integer) ;
            }
        }) ;
    }
}
