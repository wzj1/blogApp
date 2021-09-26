package com.wzj.lib_common.sp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wzj.lib_common.entitys.LoginEntity
import com.wzj.lib_common.util.ApplacationContextHolder
import com.wzj.lib_common.util.Utils
import com.xuexiang.xutil.data.BaseSPUtil
import java.lang.reflect.Type

open class SPUtil private constructor(context: Context) : BaseSPUtil(context,"blogSP"){

    private  val SP_WANANDROID = "spwanandroid"

    fun <T> getValue(
        key: String,
        defaultVal: T
    ): T {
        return get(key,defaultVal)  as T
    }

    @JvmOverloads
    fun <T> putValue(
        key: String,
        value: T
    ) {
        put(key, value)
    }

    /**
     * 返回登录时的用户信息
     * @return LoginEntity
     */
    fun getUserInfo(): LoginEntity {
        try {
            return Gson().fromJson(getValue(SP_USERINFO, ""),object :TypeToken<LoginEntity>(){}.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return LoginEntity()
    }

    /**
     * 返回 登录时的token
     * @return String
     */
    fun getToken(): String {
        return getValue(SP_TOKEN, "")
    }



    companion object {
        @Volatile
        private var sInstance: SPUtil? = null

        //存储token
          val SP_TOKEN = "token"
        //用户信息
          val SP_USERINFO = "userInfo"
        //公网IP
          val SP_PUBLICIP = "publicIp"

        /**
         * 获取单例
         * @return
         */
        val instance: SPUtil
            get() {
                if (sInstance == null) {
                    synchronized(SPUtil::class.java) {
                        if (sInstance == null) {
                            sInstance = SPUtil(ApplacationContextHolder.mContext.applicationContext)
                        }
                    }
                }
                return sInstance!!
            }

    }

}