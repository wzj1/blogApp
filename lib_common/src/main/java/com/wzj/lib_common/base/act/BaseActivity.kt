package com.wzj.lib_common.base.act

import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import com.wzj.lib_common.R
import com.wzj.lib_common.util.ApplacationContextHolder
import com.wzj.lib_common.util.ViewManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.goldze.mvvmhabit.base.BaseActivity
import me.goldze.mvvmhabit.base.BaseModel
import me.goldze.mvvmhabit.base.BaseViewModel

/**
 *
 * Activity基类
 * @name BaseActivity
 */
abstract class BaseActivity<T:ViewDataBinding,M:BaseViewModel<BaseModel>> : BaseActivity<T,M>(),CoroutineScope by MainScope() {


    override fun initParam() {
        ViewManager.instance.addActivity(this)
        ApplacationContextHolder.activity = this
        ApplacationContextHolder.mContext = this
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewManager.instance.finishActivity(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected fun setupToolBar(toolbar: Toolbar?, hideTitle: Boolean) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_background)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false)
            }
        }
    }



    override fun finish() {
        super.finish()
    }
}