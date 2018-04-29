package cn.novate.rxjava2.demo;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/27 9:32
 * Version 1.0
 * Params:
 * Description:
*/

public class ChapterFive {


    /**
     * 使用for循环无限的给水缸中存储数据，会导致OOM
     */
    public static void demo1(){

        // 创建第一个上游：Observable1
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()) ;   // 让for循环 在子线程中执行

        // 创建第二个上游：Observable2
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io()) ;  // 让onNext()方法 在子线程中执行


        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {      // 这里是把onNext()、onError()方法分开new(创建)，也可以直接new Observer，一次性创建所有回调方法
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("TAG", "s -> " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG" , "throwable -> " + throwable) ;
                    }
                });
    }


    /**
     * 通过单一的Observable分析：Backpressure
     */
    public static void demo2(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }

        // 这里建立连接之后，new Consumer只是让实现onNext()方法，不用实现其他方法 ；如果想要实现其他方法，就new Observer实现其他所有方法即可
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(2000);
                Log.e("TAG" , "integer -> " + integer) ;
            }
        });
    }


    /**
     * 让demo2()中的for循环切换到子线程中执行，然后切换到主线程中执行new Consumer：
     */
    public static void demo3(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())    // 这里让上边的 for循环在子线程中执行
                .observeOn(AndroidSchedulers.mainThread())  // 然后切换到主线程中，去执行new Consumer中的 accept()方法
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(2000);
                        Log.e("TAG" , "integer -> " + integer) ;
                    }
                });
    }
}
