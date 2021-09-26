package com.wzj.lib_common.permission

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import java.lang.ref.WeakReference
import java.util.*
import com.wzj.lib_common.R
import com.wzj.lib_common.util.Utils
import pub.devrel.easypermissions.EasyPermissions

/**
 * 权限申请 Activity 透明主题
 */
open class PermissionRequestHelper : FragmentActivity() {
    /**
     * 申请权限列表
     */
    private var mPermissions: Array<String>? = null

    /**
     * 是否是必要权限
     */
    private var mMustPerssions = false

    /**
     * 未授权权限列表
     */
    private lateinit var mDeniedPermissions: Array<String>
    var materialDialog : MaterialDialog.Builder? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbackSucceed()
        }
        //获取待申请的权限类别
        mPermissions = intent.getStringArrayExtra(KEY_PERMISSIONS_LIST)
        if (mPermissions == null || mPermissions!!.isEmpty()) {
            callbackSucceed()
        }
        mMustPerssions = intent.getBooleanExtra(KEY_PERMISSIONS_MUST, false)
        //执行权限申请
        execute()
    }


    /**
     * 权限申请执行方法
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun execute() {
        if (mPermissions != null) {
            //获取未授权请求列表
            val deniedList = getDeniedPermissions(*mPermissions!!)
            mDeniedPermissions = deniedList.toTypedArray()
            if (mDeniedPermissions.isNotEmpty()) {
                requestPermissions(mPermissions!!, REQUEST_CODE_PERMISSIONS)
            } else {
                dispatchCallback()
            }
        } else {
            finish()
        }
    }

    /**
     * 重写申请结果回调，处理申请
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //调用
        dispatchCallback()
    }

    /**
     * 重写 onActivityResult，处理从Settings界面跳转回来的权限判断
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //从Settings界面跳转回来，标准代码，就这么写
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQ_CODE) {
            dispatchCallback()
        }
    }

    /**
     * 处理权限申请结果
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun dispatchCallback() {
        val deniedList = getDeniedPermissions(*mPermissions!!)
        if (deniedList.isEmpty()) {
            //请求申请成功
            callbackSucceed()
        } else {
            val rationaleList = getRationalePermissions(*mDeniedPermissions)
            if (!rationaleList.isEmpty()) {
                //用户拒绝部分权限，重新授权提示框
                if (mMustPerssions) {
                    showSettings(deniedList)
                } else {
                    callbackFailed(null)
                }
            } else {
                val notAgainList = getNotAgainPermissions(*mDeniedPermissions)
                if (!notAgainList.isEmpty()) {
                    //用户永久拒绝部分权限，需到设置页面设置
                    if (mMustPerssions) {
                        showSettings(notAgainList)
                    } else {
                        callbackFailed(notAgainList)
                    }
                } else {
                    //请求申请成功
                    callbackSucceed()
                }
            }
        }
    }

    /**
     * 显示永久禁止的权限申请提示框，跳转设置页面
     *
     * @param permissions
     */
    private fun showSettings(permissions: List<String>) {
        val permissionNames: List<String> = MyPermission.transformText(
            this@PermissionRequestHelper,
            permissions
        )
        val message: String = getString(
            R.string.my_message_permission_always_failed,
            TextUtils.join("，", permissionNames)
        )
        mPermissions = permissions.toTypedArray()

      MaterialAlertDialogBuilder(this,R.style.Dialog_One1)
           .setMessage(message)
           .setPositiveButton("确认") { v, _w ->
               v.dismiss()

               val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
               val uri = Uri.fromParts("package", packageName, null)
               intent.data = uri
               startActivityForResult(intent, SETTINGS_REQ_CODE)
           }
           .setNegativeButton("取消") { v, _w ->
                   v.dismiss()
               callbackFailed(permissions)
           }.show()



//        MaterialDialog.Builder(this).show()

//        MaterialDialog.Builder(this@PermissionRequestHelper)
//            .theme(Theme.LIGHT)
//            .content(message)
//            .title("提示")
//            .negativeText("确认")
//            .positiveText("取消")
//            .onPositive { dialog, which ->
//
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivityForResult(intent, SETTINGS_REQ_CODE)
//                dialog.dismiss()
//            }
//            .onNegative { dialog, which ->
//
//                callbackFailed(permissions)
//                dialog.dismiss()
//            }
//            .show()
    }

    /**
     * 权限申请成功回调
     */
    private fun callbackSucceed() {
        if (mCallback != null) {
            mCallback!!.result(true)
        }
        mCallback = null
        finish()
    }

    /**
     * 权限申请失败回调
     */
    private fun callbackFailed(deniedList: List<String>?) {
        if (mCallback != null) {
            mCallback!!.result(false)
        }
        mCallback = null
        finish()
    }

    /**
     * 获取未授权申请权限列表
     *
     * @param permissions 申请权限列表
     * @return
     */
    private fun getDeniedPermissions(vararg permissions: String): List<String> {
        val deniedList: MutableList<String> = ArrayList(1)
        for (permission in permissions) {
            if (!EasyPermissions.hasPermissions(Utils.getContext()!!, permission)) {
                deniedList.add(permission)
            }
        }
        return deniedList
    }

    /**
     * 获取用户永久拒绝的权限列表
     *
     * @param permissions 未授权权限列表
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun getNotAgainPermissions(vararg permissions: String): List<String> {
        val rationaleList: MutableList<String> = ArrayList(1)
        for (permission in permissions) {
            if (!shouldShowRequestPermissionRationale(permission)) {
                rationaleList.add(permission)
            }
        }
        return rationaleList
    }

    /**
     * 获取用户拒绝的权限列表
     *
     * @param permissions 未授权权限列表
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun getRationalePermissions(vararg permissions: String): List<String> {
        val rationaleList: MutableList<String> = ArrayList(1)
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                rationaleList.add(permission)
            }
        }
        return rationaleList
    }


    /**
     * 重写 onKeyDown 禁用返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }


    companion object {
        /**
         * 权限列表数据 KEY
         */
        private const val KEY_PERMISSIONS_LIST = "KEY_PERMISSIONS_LIST"

        /**
         * 必要权限申请标识 KEY
         */
        private const val KEY_PERMISSIONS_MUST = "KEY_PERMISSIONS_MUST"

        /**
         * 设置界面请求码
         */
        private const val SETTINGS_REQ_CODE = 0x001

        /**
         * 权限申请请求码
         */
        private const val REQUEST_CODE_PERMISSIONS = 0x980

        /**
         * 权限申请调用接口
         *
         * @param context     可以startActivity的context
         * @param permissions 申请权限列表
         */
        fun requestPermissionForActivity(
            context: Context,
            isMust: Boolean,
            permissions: Array<String>,
            callback: IPermissionCallback?
        ) {
            val weakReference: WeakReference<IPermissionCallback?> =
                WeakReference<IPermissionCallback?>(callback)
            if (weakReference.get() != null) {
                mCallback = weakReference.get()
            } else {
                return
            }
            if (hasPermissions(context, permissions)) {
                mCallback!!.result(true)
                mCallback = null
                return
            }
            val intent = Intent(context, PermissionRequestHelper::class.java)
            intent.putExtra(KEY_PERMISSIONS_LIST, permissions)
            intent.putExtra(KEY_PERMISSIONS_MUST, isMust)
            context.startActivity(intent)
        }

        private var mCallback: IPermissionCallback? = null

        /**
         * 判断是否权限是否已申请
         *
         * @param context
         * @param permissions
         * @return
         */
        fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
            return EasyPermissions.hasPermissions(
                context,
//                permissions.reduceIndexed { index, acc, s -> permissions[index] })
                permissions.forEach { action -> action }.toString()
            )
        }


    }
}