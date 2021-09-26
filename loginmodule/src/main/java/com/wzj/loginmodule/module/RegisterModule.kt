package com.wzj.loginmodule.module

import android.app.Application
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.wzj.lib_common.base.repository.BaseRepository
import com.wzj.lib_common.entitys.BaseData
import com.wzj.lib_common.entitys.BaseResult
import com.wzj.lib_common.entitys.LoginEntity
import com.wzj.lib_common.http.header.BaseHeader
import com.wzj.lib_common.retrofit.HttpBuilder
import com.wzj.lib_common.retrofit.ToRequestBody
import com.wzj.lib_common.sp.SPUtil
import com.ydph.lib_base.http.callback.HttpCallback
import com.ydph.lib_common.log.Logs
import me.goldze.mvvmhabit.utils.ToastUtils

open class RegisterModule: BaseRepository , HttpCallback<BaseData<Any?>?> {

    //用户名的绑定
    var userName = ObservableField("admin")

    //密码的绑定
    var password = ObservableField("admin123")
    var qqEmail = ObservableField("1211894156@qq.com")
    var age = ObservableField("22")
    var nianling = ObservableField("19940227")
    var phone = ObservableField("18611394277")

    constructor(application: Application):super(application)

    
    fun register(){

        val bodyMap = hashMapOf<String, Any?>()
        bodyMap["userIp"] =SPUtil.instance.getValue(SPUtil.SP_PUBLICIP,"")
        bodyMap["userName"] = userName.get()
        bodyMap["userEmail"] = qqEmail.get()
        bodyMap["userPhone"] = phone.get()
        bodyMap["userBirthDay"] = nianling.get()
        bodyMap["userAge"] = age.get()
        bodyMap["userNickName"] = userName.get()
        bodyMap["userPwd"] = password.get()
        bodyMap["login_type"] = 1
        val deferred = HttpBuilder(true).apiOkHttpCreate?.registered(ToRequestBody.getRequestBody(bodyMap))!!
        post1(deferred,this)

    }

    override fun onSuccess(data: BaseData<Any?>?) {
//        Logs.d(Gson().toJson(data).toString())
//
        if (data===null) return
        if (BaseResult.getResult(data)!=null){
            finish()
        }



//        SPUtil.putValue(key = SPUtil.SP_USERINFO,value = Gson().toJson(data.data))

    }

    override fun failure(statusCode: Int, e: String?) {
        Logs.d("statusCode:$statusCode", e)
        ToastUtils.showShort("code:${statusCode} \r\n msg:${e}")
    }

}