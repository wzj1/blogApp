package com.wzj.lib_common.base.repository

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.wzj.lib_common.config.RouteConfig
import com.wzj.lib_common.config.UrlConfig.publicIp
import com.wzj.lib_common.entitys.LoginEntity
import com.wzj.lib_common.http.callback.CoroutineCallBack
import com.wzj.lib_common.http.header.BaseHeader
import com.wzj.lib_common.retrofit.HttpBuilder
import com.wzj.lib_common.util.ApplacationContextHolder
import com.ydph.lib_base.http.callback.HttpCallback
import kotlinx.coroutines.Deferred
import me.goldze.mvvmhabit.base.BaseModel
import me.goldze.mvvmhabit.base.BaseViewModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

/**
 *  ViewModel 的基类
 *  使用ViewModel 时，需要继承这个类，处理对应的业务逻辑
 * @property TAG String
 */
open class BaseRepository :BaseViewModel<BaseModel> {

    protected val TAG = "BaseRepository"

    constructor(application: Application):super(application){

    }

    companion object{
        var INSTANCE: BaseRepository? = null
        open fun getInstance(application: Application): BaseRepository? {
            if (INSTANCE == null) {
                synchronized(BaseRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = BaseRepository(application)
                    }
                }
            }
            return INSTANCE
        }
    }



    open fun <T> post(deferred: Deferred<Response<T>>, callback: HttpCallback<T>){
        CoroutineCallBack<T>()
            .setActivity(ApplacationContextHolder.activity)
            .setData(deferred)
            .setHttpCallBack(callback)
            .startLaunch()
    }

    open fun <T> post1(deferred: Deferred<T>, callback: HttpCallback<T>){
        CoroutineCallBack<T>()
            .setActivity(ApplacationContextHolder.activity)
            .setData1(deferred)
            .setHttpCallBack(callback)
            .startLaunch1()
    }

    open fun <T> post(deferred: Deferred<Response<T>>){
        CoroutineCallBack<T>()
            .setActivity(ApplacationContextHolder.activity)
            .setData(deferred)
            .startLaunch()
    }


    open fun <T> postUpload(
        deferred: Deferred<Response<T>>,
        requestBody: RequestBody,
        callback: HttpCallback<T>
    ){

        CoroutineCallBack<T>()
            .setActivity(ApplacationContextHolder.activity)
            .setData(deferred)
            .setHttpCallBack(callback)
            .startLaunch()
    }


    open fun destroyInstance() {
        INSTANCE = null
    }


    /**
     * 跳转首页
     */
    fun toHome(){

        ARouter.getInstance().build(RouteConfig.ROUTE_MAIN).navigation()
        finish()
    }


    /**
     * 获取外网IP
     * @param callBack
     */
     open fun getPublicIp(callBack: HttpCallback<ResponseBody?>){
        val map:HashMap<String,String> = hashMapOf()
        map["ie"] = "utf-8"
        post(HttpBuilder(publicIp,false).apiOkHttpCreate!!.getPublicIp(),callBack)

    }


}