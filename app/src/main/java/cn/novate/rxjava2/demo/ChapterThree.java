package cn.novate.rxjava2.demo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.novate.rxjava2.Api;
import cn.novate.rxjava2.RetrofitProvider;
import cn.novate.rxjava2.entity.LoginRequest;
import cn.novate.rxjava2.entity.LoginResponse;
import cn.novate.rxjava2.entity.RegisterRequest;
import cn.novate.rxjava2.entity.RegisterResponse;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/26 14:45
 * Version 1.0
 * Params:
 * Description:  教程三代码演示
*/

public class ChapterThree {


    /**
     * 登录
     */
    public static void login(final Context context){

        Api api = RetrofitProvider.get().create(Api.class) ;
        api.login(new LoginRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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


    /**
     * 注册
     */
    public static void register(final Context context){
        Api api = RetrofitProvider.get().create(Api.class) ;
        api.register(new RegisterRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RegisterResponse value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG" , "注册失败") ;
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG" , "注册成功") ;
                    }
                });
    }


    /**
     * map操作符：
     *         把圆形事件转为 矩形事件，也就是说下游接收的事件变为 矩形事件
     */
    public static void map(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("TAG" , "s -> " + s) ;
            }
        });

        // 运行结果
        /*cn.novate.rxjava2 E/TAG: s -> This is result 1
        cn.novate.rxjava2 E/TAG: s -> This is result 2
        cn.novate.rxjava2 E/TAG: s -> This is result 3*/
    }


    /**
     * flatMap操作符：
     *       将一个发射事件上游的Observable转换为多个发射事件的 Observable，
     *       然后把转换之后的多个事件合并后放到一个单独的 Observable里
     */
    public static void flatMap(){
        // 创建一个上游：Observable
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }

        /**
          * flatMap中把上游发射来的3个事件，转换为一个新的发射3个String事件的水管，
          * 为了看到flatMap是无效的，下边延迟10ms
         */
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<String>() ;
                for (int i = 0 ; i < 3 ; i++){
                    list.add("I am value " + integer) ;
                }
                return Observable.fromIterable(list).delay(10 , TimeUnit.MILLISECONDS);
            }

        // 建立连接、创建一个下游：Observer
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("TAG", "s -> " + s);
            }
        });
    }


    /**
     * concatMap操作符：可以保证每次发射事件的顺序
     */
    public static void concatMap(){
        // 创建一个上游：Observable
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<String>() ;
                for (int i = 0 ; i < 3 ; i++){
                    list.add("I am value " + integer) ;
                }
                return Observable.fromIterable(list).delay(10 , TimeUnit.MILLISECONDS);
            }

            // 建立连接、创建一个下游：Observer
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("TAG", "s -> " + s);
            }
        });
    }


    /**
     * 使用flatMap实现嵌套的联网请求：
     *          先注册、后登录
     */
    public static void flatMapPritice(final Context context){
        final Api api = RetrofitProvider.get().create(Api.class) ;
        api.register(new RegisterRequest())                 // 发起注册的请求
                .subscribeOn(Schedulers.io())               // 在io线程中进行联网请求
                .observeOn(AndroidSchedulers.mainThread())  // 切换到主线程中处理 注册返回的结果
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        // 根据请求注册接口的返回结果，然后做一些处理
                    }
                })
                .subscribeOn(Schedulers.io())           // 切换线程到主线程中 发起登录请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        return api.login(new LoginRequest());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  // 切换线程到主线程 处理登录返回的结果
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
