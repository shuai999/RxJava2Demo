package cn.novate.rxjava2.demo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import cn.novate.rxjava2.Api;
import cn.novate.rxjava2.RetrofitProvider;
import cn.novate.rxjava2.entity.LoginRequest;
import cn.novate.rxjava2.entity.LoginResponse;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/26 11:45
 * Version 1.0
 * Params:
 * Description:  教程二代码演示
*/

public class ChapterTwo {


    /**
     * 在主线程中创建一个上游 Observable 发送事件，则上游就在主线程中发送事件
     * 在主线程中创建一个下游 Observer 接收事件，则下游就在主线程中接收事件
     */
    public static void demo1(){
        // 创建一个上游：Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e("TAG" , "Observable thread is : " + Thread.currentThread().getName()) ;
                Log.e("TAG" , "emit 1") ;

                emitter.onNext(1);
            }
        }) ;

        // 创建一个下游：Observer
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("TAG" , "Observer thread is : " + Thread.currentThread().getName()) ;
                Log.e("TAG" , "next : " + integer) ;
            }
        } ;

        // 建立连接
        observable.subscribe(consumer);

        // 运行结果如下
        /*04-26 13:25:03.464 25525-25525/cn.novate.rxjava2 E/TAG: Observable thread is : main
        04-26 13:25:03.464 25525-25525/cn.novate.rxjava2 E/TAG: emit 1
        04-26 13:25:03.464 25525-25525/cn.novate.rxjava2 E/TAG: Observer thread is : main
        04-26 13:25:03.464 25525-25525/cn.novate.rxjava2 E/TAG: next : 1*/
    }


    /**
     * 让上游在子线程中发送事件，然后把下游切回到主线程接收事件
     */
    public static void demo2(){
        // 创建一个上游：Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e("TAG" , "Observable thread is " + Thread.currentThread().getName()) ;
                Log.e("TAG" , "emit 1") ;
                emitter.onNext(1);
            }
        }) ;

        // 创建一个下游：Observer
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("TAG" , "Observer thread is " + Thread.currentThread().getName()) ;
                Log.e("TAG" , "next :" + integer) ;
            }
        } ;

        // 建立连接
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }


    /**
     * 具体示例：
     *     通过演示登录成功与失败的功能来演示：
     *          如何把线程切换到子线程中让其执行耗时操作，然后再次切换到主线程中更新UI
     */
    public static void login(final Context context){
        Api api = RetrofitProvider.get().create(Api.class) ;
        api.login(new LoginRequest())
                .subscribeOn(Schedulers.io())     // 切换到io线程（子线程）中进行联网请求
                .observeOn(AndroidSchedulers.mainThread())  // 在耗时操作进行完之后切换到主线程中处理请求结果，来更新UI
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponse value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG" , "登录失败") ;
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG" , "登录成功") ;
                    }
                });
    }

}
