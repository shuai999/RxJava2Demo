package cn.novate.rxjava2.demo;

import android.util.Log;

import cn.novate.rxjava2.Api;
import cn.novate.rxjava2.RetrofitProvider;
import cn.novate.rxjava2.entity.UserBaseInfoRequest;
import cn.novate.rxjava2.entity.UserBaseInfoResponse;
import cn.novate.rxjava2.entity.UserExtraInfoRequest;
import cn.novate.rxjava2.entity.UserExtraInfoResponse;
import cn.novate.rxjava2.entity.UserInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/27.
 */

public class ChapterFour {

    /**
     * zip操作符：
     *      组合过程是分别从两根水管中各取一个事件进行组合，并且一个事件只能被使用一次，且严格按照事件发送的顺序，
     *      也就是说不会出现1与B、2与A进行组合
     */
    public static void demo1(){
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e("TAG" , "emit 1") ;
                emitter.onNext(1);
                Thread.sleep(1000);

                Log.e("TAG" , "emit 2") ;
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.e("TAG" , "emit 3") ;
                emitter.onNext(3);
                Thread.sleep(1000);

                Log.e("TAG" , "emit 4") ;
                emitter.onNext(4);
                Thread.sleep(1000);

                Log.e("TAG" , "emit complete1") ;
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) ;    // 让上游1（第一个水管）在子线程中执行


        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.e("TAG" , "emit A") ;
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.e("TAG" , "emit B") ;
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.e("TAG" , "emit C") ;
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.e("TAG" , "emit complete2") ;
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io()) ;      // 让上游2（第二个水管）在子线程中执行


        // 使用zip操作符，把Integer、String两个事件组合成String，最终发送的是String事件
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("TAG" , "subscribe") ;
            }

            @Override
            public void onNext(String value) {
                Log.e("TAG" , "next -> " + value) ;
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
    }


    /**
     * zip具体示例实践
     */
    public static void pritice(){
        final Api api = RetrofitProvider.get().create(Api.class) ;

        // 创建上游1，在子线程中请求用户基本信息接口
        Observable<UserBaseInfoResponse> observable1 =
                api.getUserBaseInfo(new UserBaseInfoRequest())
                        .subscribeOn(Schedulers.io()) ;

        // 创建上游2：在子线程中请求用户额外信息接口
        Observable<UserExtraInfoResponse> observable2 =
                api.getUserExtraInfo(new UserExtraInfoRequest())
                        .subscribeOn(Schedulers.io()) ;


        // 使用 zip操作符，进行组合事件  把 UserBaseInfoResponse、UserExtraInfoResponse组合成 UserInfo
        Observable.zip(observable1, observable2, new BiFunction<UserBaseInfoResponse, UserExtraInfoResponse, UserInfo>() {
            @Override
            public UserInfo apply(UserBaseInfoResponse userBaseInfoResponse, UserExtraInfoResponse userExtraInfoResponse) throws Exception {
                return new UserInfo(userBaseInfoResponse , userExtraInfoResponse);
            }
        }).observeOn(AndroidSchedulers.mainThread())   // 切换到主线程中，进行UI更新

                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        // 在这两个接口请求成功后，然后切换到主线程中，进行UI更新
                    }
                });
    }
}
