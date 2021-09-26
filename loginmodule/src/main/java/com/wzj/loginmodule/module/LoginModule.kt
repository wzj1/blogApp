package com.wzj.loginmodule.module
import android.app.Application
import androidx.databinding.ObservableField
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.wzj.lib_common.base.repository.BaseRepository
import com.wzj.lib_common.config.RouteConfig
import com.wzj.lib_common.entitys.BaseData
import com.wzj.lib_common.entitys.LoginEntity
import com.wzj.lib_common.retrofit.HttpBuilder
import com.wzj.lib_common.retrofit.ToRequestBody
import com.wzj.lib_common.sp.SPUtil
import com.wzj.lib_common.util.DesUtil
import com.ydph.lib_base.http.callback.HttpCallback
import com.ydph.lib_common.log.Logs
import me.goldze.mvvmhabit.utils.ToastUtils


open class LoginModule:BaseRepository,HttpCallback<BaseData<LoginEntity?>?> {
    constructor(application: Application):super(application){
    }


    //用户名的绑定
    var userName = ObservableField("wzj")

    //密码的绑定
    var password = ObservableField("admin123")


    //登录按钮
    fun toLogin(){
        val bodyMap = hashMapOf<String, Any?>()
        bodyMap["userName"] = userName.get()
        bodyMap["userPwd"] = DesUtil.encrypt(password.get().toString())
        bodyMap["login_type"] = 1
        val deferred = HttpBuilder(true).apiOkHttpCreate?.login(ToRequestBody.getRequestBody(bodyMap))!!
//        post(deferred,this)
        post1(deferred,object :HttpCallback<BaseData<LoginEntity?>?>{
            override fun onSuccess(data: BaseData<LoginEntity?>?) {
                Logs.d(Gson().toJson(data).toString())
                if (data!=null && !data.data?.token.isNullOrEmpty()) {
                    SPUtil.instance.putValue(SPUtil.SP_USERINFO, Gson().toJson(data.data).toString())
                    SPUtil.instance.putValue(SPUtil.SP_TOKEN, data.data?.token)
                    toHome()
                }
            }

            override fun failure(statusCode: Int, e: String?) {
                Logs.d("statusCode:$statusCode", e)
                ToastUtils.showShort("code:${statusCode} \r\n msg:${e}")
            }

        })

    }

    //跳转注册
    fun toRegister(){
       ARouter.getInstance().build(RouteConfig.ROUTE_REGISTER).navigation()
    }

    override fun onSuccess(data: BaseData<LoginEntity?>?) {
        Logs.d(Gson().toJson(data).toString())
//        if (data!=null && !data.token.isNullOrEmpty()) {
//            SPUtil.instance.putValue(SPUtil.SP_USERINFO, Gson().toJson(data).toString())
//            SPUtil.instance.putValue(SPUtil.SP_TOKEN, data.token)
//            toHome()
//        }

    }

    override fun failure(statusCode: Int, e: String?) {
        Logs.d("statusCode:$statusCode", e)
        ToastUtils.showShort("code:${statusCode} \r\n msg:${e}")
    }


}