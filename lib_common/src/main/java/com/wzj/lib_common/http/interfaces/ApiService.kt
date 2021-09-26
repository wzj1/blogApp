package com.wzj.lib_common.http.interfaces

import com.wzj.blog.myblog.entity.ImageEntity
import com.wzj.lib_common.entitys.BaseData
import com.wzj.lib_common.entitys.LoginEntity
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


/**
 *@time 2021/1/28
 *@author wangzhijie
 *
 */
interface ApiService {

    /**
     * 登录请求
     */
    @POST(value = "/login")
    @Headers("Content-Type:application/json;charset=UTF-8")
    fun login(@Body data: RequestBody): Deferred<BaseData<LoginEntity?>?>

    /**
     * 登录请求
     */
    @POST(value = "/outLogin")
    @Headers("Content-Type:application/json;charset=UTF-8")
    fun outLogin(): Deferred<Response<Any?>>

    /**
     * 登录请求
     */
    @POST(value = "/user/registered")
    @Headers("Content-Type:application/json;charset=UTF-8")
    fun registered(@Body data: RequestBody): Deferred<BaseData<Any?>?>

  /**
     * 登录请求
     */
  @Multipart
  @POST(value = "/image/uploadImg")
//    @Headers("Content-Type:application/json;charset=UTF-8")
//    fun uploadImg(@Part file: List<MultipartBody.Part>?): Deferred<Response<BaseData<ImageEntity>>>
    fun uploadImg(@Part file: List<MultipartBody.Part>?,@Query("data") data:String): Deferred<ImageEntity?>


    /**
     * 获取当前公网IP
     * @return Deferred<Response<String>>
     */
    @GET(value = "/cityjson")
    fun getPublicIp(@Query(value = "ie",encoded = false) ie:String = "utf-8/"):Deferred<Response<ResponseBody?>>

    /**
     * 获取当前公网IP
     * @return Deferred<Response<String>>
     */
    @GET(value = "/cityjson")
    fun getPublicIp():Deferred<Response<ResponseBody?>>


    /**
     * 修改用户信息
     * @param data RequestBody
     * @return Deferred<Response<BaseData<LoginEntity>>>
     */
    @POST(value = "/user/upUserInfo")
    fun upUserInfo(@Body data: RequestBody):Deferred<BaseData<LoginEntity?>?>


    /**
     * 修改用户信息
     * @param data RequestBody
     * @return Deferred<Response<BaseData<LoginEntity>>>
     */
    @POST(value = "/user/findUserId")
    fun queryUserInfo(@Body data: RequestBody):Deferred<BaseData<LoginEntity?>?>


}