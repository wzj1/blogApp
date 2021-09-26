package com.wzj.lib_common.base.app

import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.wzj.lib_common.R
import com.wzj.lib_common.util.ApplacationContextHolder
import com.wzj.lib_common.util.ClassUtils
import com.wzj.lib_common.util.Utils
import com.xuexiang.xui.XUI
import com.ydph.lib_common.log.Logs
import me.goldze.mvvmhabit.crash.CaocConfig
import me.goldze.mvvmhabit.utils.KLog
import me.goldze.mvvmhabit.base.BaseApplication


/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 * @name BaseApplication
 */
open class BaseAppApplication : BaseApplication() {
    val ROOT_PACKAGE = "com.wzj.myblogapp"
    private lateinit var mAppDelegateList: List<ApplicationDelegate>
    private lateinit var sInstance: BaseApplication

    override fun onCreate() {
        super.onCreate()

        sInstance = this
        ApplacationContextHolder.mContext = this
        Utils.init(this)
        KLog.init(true)
        if (Utils.isAppDebug)  Logs.logLevel= Logs.LogLevel.DEBUG else  Logs.logLevel= Logs.LogLevel.NOLOG

        MultiDex.install(this)
        //XUI 初始化
        XUI.init(this)
        XUI.debug(Utils.isAppDebug)

        if (Utils.isAppDebug) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)

        //配置全局异常崩溃操作
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
            .enabled(true) //是否启动全局异常捕获
            .showErrorDetails(true) //是否显示错误详细信息
            .showRestartButton(true) //是否显示重启按钮
            .trackActivities(true) //是否跟踪Activity
            .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
            .errorDrawable(R.mipmap.ic_launcher) //错误图标
//            .restartActivity(LoginActivity::class.java) //重新启动后的activity
            //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
            //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
            .apply()

        mAppDelegateList = ClassUtils.getObjectsWithInterface(this,ApplicationDelegate::class.java, ROOT_PACKAGE)
        for (delegate in mAppDelegateList) {
            delegate.onCreate()
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        for (delegate in mAppDelegateList) {
            delegate.onTerminate()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        for (delegate in mAppDelegateList) {
            delegate.onLowMemory()
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        for (delegate in mAppDelegateList) {
            delegate.onTrimMemory(level)
        }
    }
}