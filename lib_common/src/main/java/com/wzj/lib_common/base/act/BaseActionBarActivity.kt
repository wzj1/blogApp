
package com.wzj.lib_common.base.act

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.databinding.ViewDataBinding
import com.wzj.lib_common.R
import me.goldze.mvvmhabit.base.BaseModel
import me.goldze.mvvmhabit.base.BaseViewModel

/**
 * BaseActionBarActivity继承于BaseActivity，封装了actionBar的逻辑；
 * 继承于ActionBarBaseActivity的Activity都将默认带有ActionBar，并且只能使用AppTheme主题；
 * 只有那些ActionBar只带有Title和返回按钮的Activity方可继承
 * @name BaseActionBarActivity
 */
abstract class BaseActionBarActivity<T: ViewDataBinding,M: BaseViewModel<BaseModel>> : BaseActivity<T, M>() {
    /*默认的ActionBar*/
    protected var mActionBar: ActionBar? = null

    /**
     * 设置默认标题id
     *
     * @return 标题id
     */
    @StringRes
    protected abstract fun setTitleId(): Int

    /**
     * 更新标题
     *
     * @param title String标题
     */
    protected fun setTitle(title: String?) {
        if (mActionBar != null) {
            mActionBar!!.title = title
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //标题栏设置
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setHomeButtonEnabled(true)
            mActionBar!!.setTitle(setTitleId())
        }
    }
}