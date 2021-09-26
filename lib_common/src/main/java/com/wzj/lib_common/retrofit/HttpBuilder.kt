package com.wzj.lib_common.retrofit

import com.wzj.lib_common.config.UrlConfig
import com.wzj.lib_common.http.interfaces.ApiService


/**
 *@time 2021/2/2
 *@author wzj
 *
 */
open class HttpBuilder{

    private var httpBuilder: HttpBuilder? = null
     var retrofitBuilder: RetrofitBuilder<ApiService>? = null

    constructor(isEncryption: Boolean = true) {
        retrofitBuilder = RetrofitBuilder<ApiService>()
        apiOkHttpCreate = retrofitBuilder!!.getApiOkHttpCreate(isEncryption,UrlConfig.baseUrl,ApiService::class.java)
    }
    constructor(baseUrl: String,isEncryption: Boolean = true) {
        retrofitBuilder = RetrofitBuilder<ApiService>()
        apiOkHttpCreate = retrofitBuilder!!.getApiOkHttpCreate(isEncryption,baseUrl,ApiService::class.java)
    }

    fun clearAll() {
        httpBuilder = null
    }

    //通过BUider 通过接口类 以及参数封装得到协程请求对象
    var apiOkHttpCreate: ApiService? = null


    fun setBaseUrl(baseUrl: String): HttpBuilder {
        retrofitBuilder?.setBaseUrl(baseUrl)
        return this
    }

}