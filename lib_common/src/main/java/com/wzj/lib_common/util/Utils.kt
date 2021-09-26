package com.wzj.lib_common.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 *
 * Utils初始化相关
 */
class Utils private constructor() {
    companion object {
        private var context: Context? = null

        /**
         * 初始化工具类
         *
         * @param context 上下文
         */
        fun init(context: Context) {
            Companion.context = context.applicationContext
        }

        /**
         * 全局获取ApplicationContext
         *
         * @return ApplicationContext
         */
        fun getContext(): Context? {
            if (context != null) return context
            throw NullPointerException("u should init first")
        }

        /**
         * View获取Activity的工具
         *
         * @param view view
         * @return Activity
         */
        fun getActivity(view: View): Activity {
            var context = view.context
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }
            throw IllegalStateException("View $view is not attached to an Activity")
        }

        /**
         * 全局获取String的方法
         *
         * @param id 资源Id
         * @return String
         */
        fun getString(@StringRes id: Int): String {
            return context!!.resources.getString(id)
        }

        /**
         * 判断App是否是Debug版本
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        val isAppDebug: Boolean
            get() = if (StringUtils.isSpace(context!!.packageName)) false else try {
                val pm = context!!.packageManager
                val ai = pm.getApplicationInfo(context!!.packageName, 0)
                ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                false
            }

        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         */
        fun addFragmentToActivity(
            fragmentManager: FragmentManager,
            fragment: Fragment, frameId: Int
        ) {
            checkNotNull(fragmentManager)
            checkNotNull(fragment)
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, fragment)
            transaction.commit()
        }

        fun <T> checkNotNull(obj: T?): T {
            if (obj == null) {
                throw NullPointerException()
            }
            return obj
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}