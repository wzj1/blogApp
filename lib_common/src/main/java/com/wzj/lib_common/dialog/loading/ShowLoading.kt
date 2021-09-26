package com.wzj.lib_common.dialog.loading

import android.app.Activity
import com.wzj.lib_common.R
import com.wzj.lib_common.util.ApplacationContextHolder
import com.xuexiang.xui.widget.dialog.LoadingDialog

/**
 * 显示Loading
 */
class ShowLoading {

    companion object {
        private var showlogding: ShowLoading? = null
        private var mloadingDialog: LoadingDialog? = null
        private var isShowLoading: Boolean = true
        private var mActivity: Activity = ApplacationContextHolder.activity
        fun getIncense(): ShowLoading {
            if (showlogding == null) {
                synchronized(ShowLoading::class.java) {
                    if (showlogding == null) {
                        showlogding = ShowLoading()
                    }
                }
            }
            showlogding?.setIsLoading(true)
            return showlogding!!
        }

    }


    fun setActivity(activity: Activity): ShowLoading {
        mActivity = activity
        return showlogding!!
    }

    fun getActivity(): Activity {
        return mActivity
    }


    /**
     *  设置是否显示loading
     * @param isShow Boolean  默认显示，true 显示  false 不显示
     * @return CoroutineCallBack<T>
     */
    fun setIsLoading(isShow: Boolean): ShowLoading {
        isShowLoading = isShow
        return showlogding!!
    }

    /**
     *  设置是否显示loading
     * @param isShow Boolean  默认显示，true 显示  false 不显示
     * @return CoroutineCallBack<T>
     */
    fun getIsLoading(): Boolean {
        return isShowLoading
    }


    /**
     *  显示loading
     */
    fun showLoading() {
        try {

            if (mloadingDialog != null) {
                if (mloadingDialog?.isShowing!!) {
                    mloadingDialog?.dismiss()
                }
                mloadingDialog = null
            }

            if (mloadingDialog == null && mActivity != null) {
                mloadingDialog = LoadingDialog(mActivity, "提示")
                    .setLoadingIcon(R.mipmap.ic_launcher)

                mloadingDialog?.setCanceledOnTouchOutside(false)
                mloadingDialog?.setCancelable(false)
                mloadingDialog?.create()
            }



            if (!mloadingDialog?.isShowing!!) {
                mloadingDialog?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 不显示loading
     */
    fun hideLoading() {
        try {
            if (mloadingDialog != null) {
                mloadingDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}