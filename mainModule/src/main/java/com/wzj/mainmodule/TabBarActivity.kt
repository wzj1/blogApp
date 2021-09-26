package com.wzj.mainmodule

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.wzj.lib_common.base.act.BaseActivity
import com.wzj.lib_common.base.fragment.BaseFragment
import com.wzj.lib_common.base.repository.BaseRepository
import com.wzj.lib_common.config.RouteConfig
import com.wzj.mainmodule.databinding.ActivityTabBarBinding
import com.wzj.mainmodule.module.TabBarModule
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem
import me.majiajie.pagerbottomtabstrip.item.NormalItemView
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener
import java.util.*


@Route(path = RouteConfig.ROUTE_MAIN)
open class TabBarActivity : BaseActivity<ActivityTabBarBinding, TabBarModule>() {
    private var mFragments: MutableList<BaseFragment<ViewDataBinding,BaseRepository>>? = null
    private var number: Int = 100
    override fun initContentView(savedInstanceState: Bundle?): Int =R.layout.activity_tab_bar

    override fun initVariableId(): Int=BR.viewModel
    override fun initParam() {
        //初始化Fragment
        initFragment()
    }

    override fun initData() {
        //初始化底部Button
        initBottomTab()
    }

    private fun initFragment() {
        mFragments = ArrayList()
        mFragments?.add(ARouter.getInstance().build(RouteConfig.ROUTE_HOME).navigation() as BaseFragment<ViewDataBinding,BaseRepository>)
        mFragments?.add(ARouter.getInstance().build(RouteConfig.ROUTE_MESSAGE).navigation() as BaseFragment<ViewDataBinding,BaseRepository>)
        mFragments?.add(ARouter.getInstance().build(RouteConfig.ROUTE_MYINFO).navigation() as BaseFragment<ViewDataBinding,BaseRepository>)
//        mFragments?.add(ARouter.getInstance().build(RouteConfig.ROUTE_HOME).navigation() as Fragment)
        //默认选中第一个
        commitAllowingStateLoss(0)
    }

    private fun initBottomTab() {
        val navigationController = binding!!.pagerBottomTab.custom()
            .addItem(newItem(R.mipmap.ic_launcher, R.mipmap.icon_home_selected, "应用"))
//            .addItem(newItem(R.mipmap.ic_launcher, R.mipmap.icon_home_selected, "工作"))
            .addItem(newItem(R.mipmap.ic_launcher, R.mipmap.icon_home_selected, "消息"))
            .addItem(newItem(R.mipmap.ic_launcher, R.mipmap.icon_home_selected, "我的"))
            .build()
        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(object : OnTabItemSelectedListener {
            override fun onSelected(index: Int, old: Int) {
                commitAllowingStateLoss(index)
            }

            override fun onRepeat(index: Int) {}
        })

    }

    private fun commitAllowingStateLoss(position: Int) {
        hideAllFragment()

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        var currentFragment = supportFragmentManager.findFragmentByTag(position.toString() + "")
        if (currentFragment != null) {
            transaction.show(currentFragment)
        } else {
            currentFragment = mFragments!![position]
            transaction.add(R.id.frameLayout, currentFragment, position.toString() + "")
        }
        transaction.commitAllowingStateLoss()
    }

    //隐藏所有Fragment
    private fun hideAllFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        for (i in mFragments!!.indices) {
            val currentFragment = supportFragmentManager.findFragmentByTag(i.toString() + "")
            if (currentFragment != null) {
                transaction.hide(currentFragment)
            }
        }
        transaction.commitAllowingStateLoss()
    }

    fun newItem(drawable: Int, checkedDrawable: Int, text: String?): BaseTabItem {
        val normalItemView = NormalItemView(this)
        normalItemView.initialize(drawable, checkedDrawable, text)
        normalItemView.setTextDefaultColor(Color.GRAY)
        normalItemView.setTextCheckedColor(Color.RED)
        return normalItemView
    }
}