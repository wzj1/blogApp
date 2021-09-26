package com.wzj.lib_common.http.callback

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wzj.lib_common.dialog.loading.ShowLoading
import com.wzj.lib_common.entitys.BaseData
import com.wzj.lib_common.entitys.BaseResult
import com.wzj.lib_common.handler.MainHandler
import com.wzj.lib_common.http.ErrorThrowableHander
import com.wzj.lib_common.permission.IPermissionCallback
import com.wzj.lib_common.permission.MyPermission
import com.wzj.lib_common.permission.PermissionRequestHelper
import com.wzj.lib_common.util.ApplacationContextHolder
import com.wzj.lib_common.util.NetworkUtils
import com.xuexiang.xutil.tip.ToastUtils
import com.ydph.lib_base.http.callback.HttpCallback
import com.ydph.lib_common.log.Logs
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

/**
 *  java 使用 kotlin 的 协程进行请求数据，封装的方法
 * @param T 接收数据实体类
 * @property data Deferred<Response<T>>? // 请求体
 * @property httpCallback HttpCallback<T>? // 返回回调
 */
open class CoroutineCallBack<T> : CoroutineScope by MainScope(), HttpCallback<T> {

    private var data: Deferred<Response<T>>? = null
    private var data1: Deferred<T>? = null
    private var httpCallback: HttpCallback<T>? = null
    private var isShowLoading: Boolean = true


    /**
     * 当前Activity对象
     * @param activity Activity
     * @return CoroutineCallBack<T> 返回当前类对象
     */
    fun setActivity(activity: Activity): CoroutineCallBack<T> {
        ShowLoading.getIncense().setActivity(activity)
        return this
    }

    /**
     *  设置是否显示loading
     * @param isShow Boolean  默认显示，true 显示  false 不显示
     * @return CoroutineCallBack<T>
     */
    fun setIsLoading(isShow: Boolean): CoroutineCallBack<T> {
        isShowLoading = isShow
        return this
    }

    /**
     *  设置请求体数据
     * @param datas Deferred<Response<T>>?  请求体
     * @return CoroutineCallBack<T> //返回当前类对象
     */
    fun setData(datas: Deferred<Response<T>>?): CoroutineCallBack<T> {
        this.data = datas
        return this
    }
    /**
     *  设置请求体数据
     * @param datas Deferred<Response<T>>?  请求体
     * @return CoroutineCallBack<T> //返回当前类对象
     */
    fun setData1(datas: Deferred<T>?): CoroutineCallBack<T> {
        this.data1 = datas
        return this
    }

    /**
     *  设置请求监听回调
     * @param httpCallback HttpCallback<T> 监听回调
     * @return CoroutineCallBack<T>
     */
    fun setHttpCallBack(httpCallback: HttpCallback<T>): CoroutineCallBack<T> {
        this.httpCallback = httpCallback
        return this
    }

    /**
     * 启动协程请求数据
     */
    fun startLaunch() {

        if (isShowLoading) {

            ShowLoading.getIncense().showLoading()
        }

        if (!NetworkUtils.isAvailable(ApplacationContextHolder.activity)) {
            failure(-1003, "网络异常，或服务器出小猜了，请稍后再试")
            return
        }

        CoroutineExceptionHandler { _, exception ->
            Logs.d("exceptionHandler ${exception.message}")
            val error = ErrorThrowableHander(exception)
            failure(error.resultCode(), error.resultMsg())
        }


        PermissionRequestHelper.requestPermissionForActivity(ApplacationContextHolder.activity,true,

            arrayOf<String>(
                MyPermission.READ_PHONE_STATE,
                MyPermission.CALL_PHONE,
                MyPermission.ACCESS_FINE_LOCATION,
                MyPermission.ACCESS_COARSE_LOCATION,
                MyPermission.WRITE_EXTERNAL_STORAGE
            ), object : IPermissionCallback {
                override fun result(grandAll: Boolean) {
                    if (grandAll) {
                        try {
                            var result: T? = null
                                GlobalScope.launch(Dispatchers.IO) {
                                    val data2 = withContext(Dispatchers.IO) {
                                        data
                                    }?.await()

                                    if (data2==null){
                                        //失败回调
                                        failure(-1000, "请求失败")
                                        return@launch
                                    }
                                    //判断是否成功
                                    if (!data2.isSuccessful) {
                                        //失败回调
                                        failure(data2.code(), "请求失败")
                                        return@launch
                                    }

                                    var data3 = data2.body()

                                    //判空
                                    if (data3 == null) {
                                        failure(300, "请求失败")
                                        return@launch
                                    } else {
                                        result = data3
                                        //成功回调
                                        onSuccess(result)
                                    }
                                }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Logs.d("is try catch ${e.message}")
                            failure(-1000, "请求失败")
                        }

                    } else {
                        ApplacationContextHolder.activity.runOnUiThread{
                            ToastUtils.toast("申请权限失败!!!")
                            failure(-1001, "申请权限失败!!!")
                        }
                    }
                }

            }
        )




    }


    /**
     * 启动协程请求数据
     */
    fun startLaunch1() {

        if (isShowLoading) {
            ShowLoading.getIncense().showLoading()
        }

        if (!NetworkUtils.isAvailable(ApplacationContextHolder.activity)) {
            failure(-1003, "网络异常，或服务器出小猜了，请稍后再试")
            return
        }

        CoroutineExceptionHandler { _, exception ->
            Logs.d("exceptionHandler ${exception.message}")
            val error = ErrorThrowableHander(exception)
            failure(error.resultCode(), error.resultMsg())
        }


        PermissionRequestHelper.requestPermissionForActivity(ApplacationContextHolder.activity,true,

            arrayOf<String>(
                MyPermission.READ_PHONE_STATE,
                MyPermission.CALL_PHONE,
                MyPermission.ACCESS_FINE_LOCATION,
                MyPermission.ACCESS_COARSE_LOCATION,
                MyPermission.WRITE_EXTERNAL_STORAGE
            ), object : IPermissionCallback {
                override fun result(grandAll: Boolean) {
                    if (grandAll) {
                        try {
                            var result: T? = null
                            GlobalScope.launch(Dispatchers.Main) {
                                val data2 = withContext(Dispatchers.IO) {
                                    data1
                                }?.await()
                                if (data2==null){
                                    //失败回调
                                    failure(-1000, "请求失败")
                                    return@launch
                                } else {
                                    result = data2
                                    //成功回调
                                    onSuccess(result)
                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Logs.d("is try catch ${e.message}")
                            failure(-1000, "请求失败")
                        }

                    } else {
                        ApplacationContextHolder.activity.runOnUiThread{
                            ToastUtils.toast("申请权限失败!!!")
                            failure(-1001, "申请权限失败!!!")
                        }
                    }
                }

            }
        )




    }

    /**
     * 成功回调
     * @param data T?
     */
    override fun onSuccess(data: T?) {
        MainHandler.getInstance().post {
            ShowLoading.getIncense().hideLoading()
            if (httpCallback != null) {
                httpCallback?.onSuccess(data)
            }
        }

    }

    /**
     * 失败回调
     * @param statusCode Int
     * @param e String?
     */
    override fun failure(statusCode: Int, e: String?) {

        MainHandler.getInstance().post {
            ShowLoading.getIncense().hideLoading()
            if (httpCallback != null) {
                httpCallback?.failure(statusCode, e)
            }
        }
    }
}

