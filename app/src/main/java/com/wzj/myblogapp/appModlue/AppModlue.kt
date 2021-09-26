package com.wzj.myblogapp.appModlue

import android.app.Application
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.wzj.lib_common.base.repository.BaseRepository
import com.wzj.lib_common.config.RouteConfig
import com.wzj.lib_common.sp.SPUtil
import com.ydph.lib_base.http.callback.HttpCallback
import com.ydph.lib_common.log.Logs
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.ToastUtils
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject


open class AppModlue:BaseRepository {
    constructor(application: Application):super(application)

    fun toLogin() {
        ARouter.getInstance().build(RouteConfig.ROUTE_LOGIN).navigation()
        finish()
    }

    //点击事件
    var clearUserNameOnClickCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        ARouter.getInstance().build(RouteConfig.ROUTE_LOGIN).navigation()
        finish()
    })
    

    fun  clearUserNameOnClickCommand(): BindingCommand<Any?> = BindingCommand<Any?>(
        BindingAction {
            ARouter.getInstance().build(RouteConfig.ROUTE_LOGIN).navigation()
            finish()
        })

    fun init(){
        val token = SPUtil.instance.getToken()
        if (token.isNullOrBlank()||token == "null" ){
            toLogin()
        }else{
           toHome()
        }
    }


    fun  getPublicIp(){

        getPublicIp(object : HttpCallback<ResponseBody?> {
            override fun onSuccess(result1: ResponseBody?) {
                  val  result = result1?.string().toString()
                if (result!=null) {
                    Logs.d("$TAG---result", result.toString())
                    val str: String = result.substring(result.indexOf("{"), result.indexOf("}") + 1)
                    Logs.d("$TAG---str", str)
                    val js = JSONObject(str)
                    val cip = js.getString("cip")
                    val cid = js.getString("cid")
                    val cname = js.getString("cname")
                    SPUtil.instance.putValue(SPUtil.SP_PUBLICIP,cip)
                    init()
                }

            }

            override fun failure(statusCode: Int, e: String?) {
                Logs.d("statusCode:$statusCode", e)
                ToastUtils.showShort("code:${statusCode} \r\n msg:${e}")
                init()
            }

        })

    }

}