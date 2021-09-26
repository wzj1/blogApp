@file:Suppress("UNREACHABLE_CODE")

package com.wzj.lib_common.retrofit

import com.wzj.lib_common.config.UrlConfig
import com.ydph.lib_common.log.Logs
import com.wzj.lib_common.http.interfaces.ApiFactory
import kotlinx.coroutines.*

/**
 *@time 2021/1/28
 *@author wangzhijie
 */
@ExperimentalCoroutinesApi
open class RetrofitBuilder<T> {
    constructor() {
        Logs.init("RetrofitBuilder")
    }

    private var url: String = UrlConfig.baseUrl
    private var apiService: Class<T>? = null
    private var apiOkHttpCreate: T? = null
    private var isEncryption:Boolean = false

    private var baseHeader: HashMap<String, Any?>? = null

    fun setBaseUrl(baseUrl: String): RetrofitBuilder<T> {
        url = baseUrl
        return this
    }

    fun getBaseUrl(): String {
        return url
    }


    fun setApiService(apiService: Class<T>): RetrofitBuilder<T> {
        this.apiService = apiService
        return this
    }


    fun getApiService(): Class<T>? {
        return this.apiService
    }


    /**
     * 动态 创建 apiService  继承自ApiService
     */
    fun <M> getApiOkHttpCreate(isEncryption:Boolean,apiService: Class<M>): M {
        return ApiFactory.create(isEncryption,url, apiService)
    }

    /**
     * 动态 创建 apiService  继承自ApiService
     */
    fun <M> getApiOkHttpCreate(isEncryption:Boolean,baseUrl: String,apiService: Class<M>): M {
        return ApiFactory.create(isEncryption,baseUrl, apiService)
    }


    /**
     * 动态 创建 apiService  继承自ApiService
     */
    fun setApiOkHttpCreate(baseUrl: String, apiService: Class<T>): RetrofitBuilder<T> {
        this.url = baseUrl
        this.apiOkHttpCreate = ApiFactory.create(isEncryption,url, apiService)
        return this
    }

}

