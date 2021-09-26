package com.wzj.lib_common.http.interfaces

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.wzj.lib_common.http.interceptor.LoggingInterceptor
import com.wzj.lib_common.retrofit.FastJsonConverterFactory
import com.wzj.lib_common.retrofit.JsonConverterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 *@time 2020/11/9
 *@author wangzhijie
 * 创建 工厂
 */
object ApiFactory {
    //连接超时时间
    var CONNECTTIMEOUT: Long = 15
    //读取超时时间
    var READTIMEOUT:Long = 30
    //写入超时时间
    var WRITETIMEOUT:Long = 30

    var isInitClient:Boolean = false
    var mClientMessage:OkHttpClient?=null


    /**
     * @param okHttpClient okHttpClient
     *  无日志拦截器
     */
    fun initClient(okHttpClient: OkHttpClient?, isEncryption: Boolean): OkHttpClient {
//        val connectionPool = ConnectionPool(30, CONNECTTIMEOUT * 1000, TimeUnit.MILLISECONDS)
        isInitClient =true
        if (okHttpClient == null) {
            mClientMessage = OkHttpClient.Builder().apply {
                connectTimeout(CONNECTTIMEOUT, TimeUnit.SECONDS)// 连接时间：30s超时
                readTimeout(READTIMEOUT, TimeUnit.SECONDS)// 读取时间：10s超时
                writeTimeout(WRITETIMEOUT, TimeUnit.SECONDS)// 写入时间：10s超时
//                connectionPool(connectionPool)
                retryOnConnectionFailure(false) //自动重连设置为false
                if (isEncryption){
                    addInterceptor(LoggingInterceptor(isEncryption))
                }else {
                    addInterceptor(LoggingInterceptor(isEncryption))
                }
            }.build()
        }else {
            mClientMessage = okHttpClient
        }
        return mClientMessage!!
    }
    /**
     * @param okHttpClient okHttpClient
     * @param isAddInterceptor  是否添加默认日志拦截器
     */
    fun initClient(isAddInterceptor: Boolean, okHttpClient: OkHttpClient?): OkHttpClient {
        isInitClient =true
        if (okHttpClient == null) {
            mClientMessage = OkHttpClient.Builder().apply {
                connectTimeout(30, TimeUnit.SECONDS)// 连接时间：30s超时
                readTimeout(10, TimeUnit.SECONDS)// 读取时间：10s超时
                writeTimeout(10, TimeUnit.SECONDS)// 写入时间：10s超时
                if (isAddInterceptor)  addInterceptor(LoggingInterceptor(false))// 仅debug模式启用日志过滤器
            }.build()
        }else {
            mClientMessage = okHttpClient
        }
        return mClientMessage!!
    }

    /**
     * @param okHttpClient okHttpClient
     * @param interceptor 添加日志拦截器
     */
    fun initClient(interceptor: Interceptor, okHttpClient: OkHttpClient?): OkHttpClient {
        isInitClient =true
        if (okHttpClient == null) {
            mClientMessage = OkHttpClient.Builder().apply {
                connectTimeout(30, TimeUnit.SECONDS)// 连接时间：30s超时
                readTimeout(10, TimeUnit.SECONDS)// 读取时间：10s超时
                writeTimeout(10, TimeUnit.SECONDS)// 写入时间：10s超时
                addInterceptor(interceptor)// 仅debug模式启用日志过滤器
            }.build()
        }else {
            mClientMessage = okHttpClient
        }

        return mClientMessage!!
    }

    /**
     * 创建API Service接口实例
     * @property isEncryption Boolean  是否加解密
     */
    fun <T> create(isEncryption: Boolean = true, baseUrl: String, clazz: Class<T>): T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(initClient(null, isEncryption))
        .addConverterFactory(JsonConverterFactory<T>(isEncryption))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(clazz)

}