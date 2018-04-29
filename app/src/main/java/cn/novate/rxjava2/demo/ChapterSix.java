package cn.novate.rxjava2.demo;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/27 10:48
 * Version 1.0
 * Params:
 * Description:  解决上游、下游发射事件速度不平衡问题
*/

public class ChapterSix {


    /**
     * 从数量上解决：
     *          取样：每隔2秒从上游取出事件发射给下游
     */
    public static void demo1(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())   // 让上游的 for循环在子线程中执行
               .sample(2 , TimeUnit.SECONDS)  // 每隔2秒从上游取出事件发射给下游
               .observeOn(AndroidSchedulers.mainThread())  //下游切换到主线程中
               .subscribe(new Consumer<Integer>() {    // 建立连接后，new Consumer()然后复写 onNext()方法
                   @Override
                   public void accept(Integer integer) throws Exception {
                       Log.e("TAG" , "integer -> " + integer) ;
                   }
               });
    }


    /**
     * 从速度上解决：
     *      让上游的for循环执行完之后，延迟两秒发射数据给下游
     */
    public static void demo2(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                    Thread.sleep(2000);     // 在上游的for循环执行完之后，添加2秒延迟
                }
            }
        }).subscribeOn(Schedulers.io())     // 表示上游的for循环操作在子线程中执行
                .observeOn(AndroidSchedulers.mainThread())   // 切换到主线程中执行下边的操作
                .subscribe(new Consumer<Integer>() {        // 建立连接之后，new Consumer()之后，只是复写onNext()方法，不需要复写其他方法
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e("TAG" , "integer -> " + integer) ;
                    }
                });
    }


    /**
     * 从数量上解决：
     *          取样：每隔2秒从上游取出事件发射给下游
     */
    public static void demo3(){
        // 创建第一个上游：Observable1
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()).sample(2 , TimeUnit.SECONDS) ;  // 让上游的for循环在子线程中执行，并且是每隔2秒从上游发射事件给下游

        // 创建第二个上游：Observable2
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io()) ;


        // 通过zip操作符：把上游1、上游2组合，然后把组合后的事件再发射给下游
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Consumer<String>() {           // 建立连接之后，通过new Consumer()复写accept() -> 表示的是onNext()
            @Override                                  // 然后再new Consumer()复写 accept() -> 表示的是onError()
            public void accept(String s) throws Exception {
                Log.e("TAG" , "s -> " + s) ;
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("TAG" , "throwable -> " + throwable) ;
            }
        });
    }


    /**
     * 从速度上解决：
     *      让上游的for循环执行完之后，延迟两秒发射数据给下游
     */
    public static void demo4(){
        // 创建第一个上游：Observable1
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                    Thread.sleep(2000);   // 让上游的for循环完之后，然后延迟2秒，让上游减慢发射事件的速度
                }
            }
        }).subscribeOn(Schedulers.io()) ;

        // 创建第二个上游：Observable2
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }) ;

        // 通过zip操作符，把上游1、上游2组合，然后把组合后的事件发射给下游
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Consumer<String>() {    // 建立连接之后，然后new Consumer()，复写accept() -> 相当于 onNext()
            @Override                           // 然后再次new Consumer()，复写accept() -> 相当于 onError()
            public void accept(String s) throws Exception {
                Log.e("TAG" , "s -> " + s) ;
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("TAG" , "throwable -> " + throwable) ;
            }
        });
    }
}
