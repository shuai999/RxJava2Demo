package cn.novate.rxjava2;

import cn.novate.rxjava2.entity.LoginRequest;
import cn.novate.rxjava2.entity.LoginResponse;
import cn.novate.rxjava2.entity.RegisterRequest;
import cn.novate.rxjava2.entity.RegisterResponse;
import cn.novate.rxjava2.entity.UserBaseInfoRequest;
import cn.novate.rxjava2.entity.UserBaseInfoResponse;
import cn.novate.rxjava2.entity.UserExtraInfoRequest;
import cn.novate.rxjava2.entity.UserExtraInfoResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/26 14:12
 * Version 1.0
 * Params:
 * Description:
*/

public interface Api {

    /**
     * 登录
     */
    @GET
    Observable<LoginResponse> login(@Body LoginRequest request) ;

    /**
     * 注册
     */
    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest request) ;

    /**
     * 获取用户基本信息
     */
    @GET
    Observable<UserBaseInfoResponse> getUserBaseInfo(@Body UserBaseInfoRequest request) ;

    /**
     * 获取用户额外信息
     */
    @GET
    Observable<UserExtraInfoResponse> getUserExtraInfo(@Body UserExtraInfoRequest request) ;
}
