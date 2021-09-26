package com.wzj.lib_common.entitys

import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wzj.lib_common.RSAUtil
import com.wzj.lib_common.config.RouteConfig
import com.wzj.lib_common.handler.MainHandler
import com.wzj.lib_common.http.rsa.RSAKey
import com.wzj.lib_common.sp.SPUtil
import com.wzj.lib_common.util.ApplacationContextHolder
import me.goldze.mvvmhabit.utils.ToastUtils

object BaseResult {

    fun <T> getResult(data: T): T?{
        data as BaseData<Any?>
       return when(data.code){
            200 ->data
            300 -> {
                MainHandler.getInstance().post {
                    ToastUtils.showShort("code:${data.code} \r\n msg:${data.msg}")
                }
               return null
            }
            //重新登录
            301 -> {
                MainHandler.getInstance().post {
                    ToastUtils.showShort("code:${data.code} \r\n msg:${data.msg}")
                }
                SPUtil.instance.putValue(SPUtil.SP_USERINFO,"")
                ARouter.getInstance().build(RouteConfig.ROUTE_LOGIN).navigation()
                ApplacationContextHolder.activity.finish()
                null
            }
            else -> data
        }

    }

}